package zenith.io

import org.lwjgl.BufferUtils
import org.lwjgl.system.MemoryUtil
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.channels.Channels

object ByteBufferUtils {
    fun resize(buffer: ByteBuffer, newCapacity: Int): ByteBuffer {
        val newBuffer = BufferUtils.createByteBuffer(newCapacity)
        buffer.flip()
        newBuffer.put(buffer)
        return newBuffer
    }

    fun fromInputStream(inputStream: InputStream): ByteBuffer {
        val rbc = Channels.newChannel(inputStream)
        var buffer = BufferUtils.createByteBuffer(2)
        while (true) {
            val bytes = rbc.read(buffer)
            if (bytes == -1) {
                break
            }
            if (buffer.remaining() == 0) {
                buffer = resize(buffer, buffer.capacity() * 3 / 2)
            }
        }
        rbc.close()
        buffer.flip()
        return MemoryUtil.memSlice(buffer)
    }
}