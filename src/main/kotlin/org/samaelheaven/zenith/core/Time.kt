package org.samaelheaven.zenith.core

object Time {
    private const val SLEEP_PRECISION = 2_000_000
    private val startTime = System.nanoTime()
    private var lastFrameTime: Long = 0
    private var frameCount: Long = 0
    private var _delta = 0f
    private var _averageFPS = 0f

    init {
        Game.throwIfUninitialized()
    }

    val ticks: Long
        get() = System.nanoTime() - startTime

    val delta: Float
        get() = _delta

    val fixedDelta: Float
        get() = 1 / 60f

    val averageFPS: Float
        get() = _averageFPS

    val currentFPS: Float
        get() = if (delta == 0f) 0f else 1 / delta

    internal fun update() {
        sync()
        refresh()
    }

    private fun refresh() {
        frameCount++
        _delta = ((ticks - lastFrameTime) / 1e9).toFloat()
        val timeInSeconds = (ticks / 1e9).toFloat()
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

    private val targetFrameTime: Double
        get() = 1_000_000_000.0 / Game.fpsTarget.toDouble()

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