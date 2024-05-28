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
        private val time: Time
            get() = (if (instance == null) Time() else instance).also { instance = it }!!

        val ticks: Long
            get() = System.nanoTime() - instance!!.startTime

        val delta: Float
            get() = time.delta

        val fixedDelta: Float
            get() = 1 / 60f

        val averageFPS: Float
            get() = time.averageFPS

        val currentFPS: Float
            get() = if (delta == 0f) 0f else 1 / delta

        internal fun update() {
            time.update()
        }
    }

    init {
        Game.throwIfUninitialized()
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