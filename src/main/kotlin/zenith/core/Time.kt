package zenith.core

import kotlin.time.DurationUnit
import kotlin.time.toDuration

object Time {
    private const val SLEEP_PRECISION = 2_000_000
    private val launchTime = System.nanoTime()
    private var startTime = System.nanoTime()
    private var lastFrameTime = 0L
    private var frameCount = 0L
    private var _delta = 0f
    private var _averageFPS = 0f

    init {
        Game.throwIfUninitialized()
    }

    val elapsed get() = (System.nanoTime() - launchTime).toDuration(DurationUnit.NANOSECONDS)

    val delta get() = _delta

    val fixedDelta get() = 1 / 60f

    val averageFPS get() = _averageFPS

    val currentFPS get() = if (delta <= 0f) 0f else 1 / delta

    internal fun restart() {
        startTime = System.nanoTime()
        lastFrameTime = 0
        frameCount = 0
        _delta = 0f
        _averageFPS = 0f
    }

    internal fun update() {
        sync()
        refresh()
    }

    private val ticks get() = System.nanoTime() - startTime

    private val targetFrameTime: Double get() = 1_000_000_000.0 / Game.fpsTarget.toDouble()

    private fun refresh() {
        frameCount++
        _delta = ((ticks - lastFrameTime) / 1e9f)
        val timeInSeconds = (ticks / 1e9f)
        _averageFPS = if (timeInSeconds == 0f) 0f else (frameCount / timeInSeconds)
        lastFrameTime = ticks
    }

    private fun sync() {
        val targetFrameTime = targetFrameTime
        val waitTime = (targetFrameTime - (ticks - lastFrameTime)).toLong()
        if (waitTime > 0 && waitTime <= targetFrameTime) {
            sleep(waitTime)
        }
    }

    private fun sleep(nanoSeconds: Long) {
        val endTime = System.nanoTime() + nanoSeconds
        var timeLeft = nanoSeconds
        while (timeLeft > 0) {
            if (timeLeft > SLEEP_PRECISION) {
                Thread.sleep(1)
            } else {
                Thread.sleep(0)
            }
            timeLeft = endTime - System.nanoTime()
        }
    }
}