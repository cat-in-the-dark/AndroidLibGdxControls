package com.catinthedark.touchcontrols

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Created by kirill on 17.09.16.
 */

class Intervals(threadCount: Int = 1) {
    private val executor = Executors.newScheduledThreadPool(threadCount)

    fun deffer(delay: Long, timeUnit: TimeUnit, callback: () -> Unit) {
        executor.schedule({
            callback()
        }, delay, timeUnit)
    }
}
