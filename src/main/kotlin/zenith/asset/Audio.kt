package zenith.asset

import javafx.beans.NamedArg
import org.lwjgl.BufferUtils
import org.lwjgl.openal.AL
import org.lwjgl.openal.AL11
import org.lwjgl.openal.ALC
import org.lwjgl.openal.ALC11
import org.lwjgl.stb.STBVorbis
import org.lwjgl.system.MemoryUtil
import org.lwjgl.system.libc.LibCStdlib
import zenith.core.CustomProperty
import zenith.core.Property
import zenith.io.ByteBufferUtils
import zenith.io.Resource
import java.io.InputStream
import java.net.URL
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.math.max
import kotlin.math.min

class Audio(
    @NamedArg("input") input: InputStream,
    @NamedArg("volume") volume: Float = 1f,
    @NamedArg("loop") val loop: Boolean = false
) {
    private val inputStreamBuffer = ByteBufferUtils.fromInputStream(input)
    private val clips = mutableListOf<Clip>()
    private val disposedClips = mutableListOf<Clip>()

    val volumeProperty: Property<Float> = CustomProperty(volume) { property, value, setter ->
        cleanClips()
        val newValue = clampVolume(value)
        if (newValue == property.value) {
            return@CustomProperty
        }
        setter(newValue)
        for (clip in clips) {
            clip.changeVolume(newValue)
        }
    }

    var volume: Float
        get() = volumeProperty.value
        set(value) {
            volumeProperty.value = value
        }

    companion object {
        private val executor: ExecutorService = Executors.newCachedThreadPool {
            val thread = Thread(it)
            thread.isDaemon = true
            thread
        }
        private val globalClips = mutableListOf<Clip>()
        private var audioContext = MemoryUtil.NULL
        private var audioDevice = MemoryUtil.NULL

        val volumeProperty: Property<Float> = CustomProperty(1f) { property, value, setter ->
            val newValue = clampVolume(value)
            if (newValue == property.value) {
                return@CustomProperty
            }
            setter(newValue)
            for (clip in globalClips.toTypedArray()) {
                clip.changeVolume(clip.volume)
            }
        }

        var volume: Float
            get() = volumeProperty.value
            set(value) {
                volumeProperty.value = value
            }

        init {
            initializeOpenAL()
        }

        private fun initializeOpenAL() {
            openDevice()
            createContext()
            createCapabilities()
            Runtime.getRuntime().addShutdownHook(Thread { disposeOpenAL() })
        }

        private fun openDevice() {
            audioDevice = ALC11.alcOpenDevice(ALC11.alcGetString(0, ALC11.ALC_DEFAULT_DEVICE_SPECIFIER))
        }

        private fun createContext() {
            audioContext = ALC11.alcCreateContext(audioDevice, intArrayOf(0))
            ALC11.alcMakeContextCurrent(audioContext)
        }

        private fun createCapabilities() {
            val alcCapabilities = ALC.createCapabilities(audioDevice)
            val alCapabilities = AL.createCapabilities(alcCapabilities)
            if (!alCapabilities.OpenAL11) {
                throw RuntimeException("Could not create OpenAL capabilities")
            }
        }

        private fun disposeOpenAL() {
            ALC11.alcDestroyContext(audioContext)
            ALC11.alcCloseDevice(audioDevice)
        }

        private fun clampVolume(volume: Float): Float {
            return max(0.0, min(1.0, volume.toDouble())).toFloat()
        }
    }

    constructor(
        @NamedArg("url") url: URL, @NamedArg("volume") volume: Float = 1f, @NamedArg("loop") loop: Boolean = false
    ) : this(url.openStream(), volume, loop)

    constructor(
        @NamedArg("resource") resource: String,
        @NamedArg("volume") volume: Float = 1f,
        @NamedArg("loop") loop: Boolean = false
    ) : this(Resource.stream(resource), volume, loop)

    fun play() {
        cleanClips()
        if (state == AudioState.PAUSED) {
            for (clip in clips) {
                clip.play()
            }
            return
        }
        val clip = Clip(inputStreamBuffer, volume, loop)
        globalClips.add(clip)
        clips.add(clip)
        clip.play()
        addDisposeClipListener(clip)
    }

    fun pause() {
        cleanClips()
        for (clip in clips) {
            if (clip.state == AudioState.PLAYING) {
                clip.pause()
            }
        }
    }

    fun stop() {
        cleanClips()
        for (clip in clips) {
            if (clip.state == AudioState.PLAYING) {
                clip.stop()
            }
        }
        clips.clear()
    }

    val state: AudioState
        get() {
            cleanClips()
            for (clip in clips) {
                if (clip.state == AudioState.PAUSED) {
                    return AudioState.PAUSED
                }
                if (clip.state == AudioState.PLAYING) {
                    return AudioState.PLAYING
                }
            }
            return AudioState.STOPPED
        }

    private fun cleanClips() {
        val tempClips = disposedClips.toList()
        clips.removeAll(tempClips)
        disposedClips.removeAll(tempClips)
    }

    private fun addDisposeClipListener(clip: Clip) {
        executor.submit {
            while (clip.state != AudioState.STOPPED) {
                TimeUnit.SECONDS.sleep(1)
            }
            clip.dispose()
            globalClips.remove(clip)
            disposedClips.add(clip)
        }
    }

    private class Clip(inputStreamBuffer: ByteBuffer, var volume: Float, loop: Boolean) {
        private val sourceId: Int
        private val bufferId: Int

        val state: AudioState
            get() {
                val state = AL11.alGetSourcei(sourceId, AL11.AL_SOURCE_STATE)
                return when (state) {
                    AL11.AL_PLAYING -> AudioState.PLAYING
                    AL11.AL_PAUSED -> AudioState.PAUSED
                    else -> AudioState.STOPPED
                }
            }

        init {
            val channelsBuffer = BufferUtils.createIntBuffer(1)
            val sampleRateBuffer = BufferUtils.createIntBuffer(1)
            val rawAudioBuffer = STBVorbis.stb_vorbis_decode_memory(inputStreamBuffer, channelsBuffer, sampleRateBuffer)
            val format = getFormat(channelsBuffer[0])
            bufferId = AL11.alGenBuffers()
            AL11.alBufferData(bufferId, format, rawAudioBuffer!!, sampleRateBuffer[0])
            sourceId = AL11.alGenSources()
            AL11.alSourcei(sourceId, AL11.AL_BUFFER, bufferId)
            AL11.alSourcei(sourceId, AL11.AL_LOOPING, if (loop) 1 else 0)
            AL11.alSourcei(sourceId, AL11.AL_POSITION, 0)
            AL11.alSourcef(sourceId, AL11.AL_GAIN, volume * volumeProperty.value)
            LibCStdlib.free(rawAudioBuffer)
        }

        fun play() {
            AL11.alSourcePlay(sourceId)
        }

        fun pause() {
            AL11.alSourcePause(sourceId)
        }

        fun stop() {
            AL11.alSourceStop(sourceId)
        }

        fun changeVolume(volume: Float) {
            this.volume = volume
            AL11.alSourcef(sourceId, AL11.AL_GAIN, volume * volumeProperty.value)
        }

        fun dispose() {
            AL11.alDeleteSources(sourceId)
            AL11.alDeleteBuffers(bufferId)
        }

        private fun getFormat(channels: Int): Int {
            return when (channels) {
                1 -> AL11.AL_FORMAT_MONO16
                2 -> AL11.AL_FORMAT_STEREO16
                else -> -1
            }
        }
    }
}