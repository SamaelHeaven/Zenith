package org.samaelheaven.zenith.core

class Time private constructor() {
    private val startTime = System.nanoTime()
    private var lastFrameTime: Long = 0
    private var frameCount: Long = 0
    private var delta = 0f
    private var averageFPS = 0f

    companion object {
        private const val SLEEP_PRECISION = 2_000_000
        private var instance: Time? = null
            get() = (if (field == null) Time() else field).also { field = it }

        val ticks: Long
            get() = System.nanoTime() - instance!!.startTime

        val delta: Float
            get() = instance!!.delta

        val fixedDelta: Float
            get() = 1 / 60f

        val averageFPS: Float
            get() = instance!!.averageFPS

        val currentFPS: Float
            get() = if (delta == 0f) 0f else 1 / delta

        internal fun update() {
            instance!!.update()
        }
    }

    private fun update() {
        sync()
        refresh()
    }

    private fun refresh() {
        frameCount++
        delta = ((ticks - lastFrameTime) / 1e9).toFloat()
        val timeInSeconds = (ticks / 1e9).toFloat()
        averageFPS = if (timeInSeconds == 0f) 0f else (frameCount / timeInSeconds)
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
        get() = 1_000_000_000.0 / 120

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