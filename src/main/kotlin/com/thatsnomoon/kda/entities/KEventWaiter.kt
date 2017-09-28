/*
 * Copyright (c) 2017 Benjamin Scherer
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thatsnomoon.kda.entities

import kotlinx.coroutines.experimental.*
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.events.Event
import net.dv8tion.jda.core.hooks.EventListener

class KEventWaiter<out T: Event>(private val jda: JDA,
                                 private val type: Class<T>,
                                 private val count: Int = 1,
                                 private val timeoutMS: Long = -1,
                                 private val check: (T) -> Boolean = {true}
): EventListener, Resolvable<List<T>> {

    private var timeoutJob: Job

    private var collected = 0
    private var timedOut = false

    init {
        if (count < 1) throw IllegalArgumentException("Count parameter passed to KEventWaiter must be greater than zero")
        jda.addEventListener(this)
        timeoutJob = launch(CommonPool) {
            if (timeoutMS > 0) {
                delay(timeoutMS)
                timedOut = true
                deregister()
            }
        }
    }

    private var resolved = false

    private var result: List<T> = listOf()

    override fun onEvent(event: Event) {
        if (type.isInstance(event)) {
            try {
                if (check(event as T)) {
                    result += event
                    collected++
                    if (collected == count) {
                        resolved = true
                        deregister()
                    }
                    else if (timeoutMS > 0)
                        restartTimeout()
                }
            } catch (t: Throwable) {
                resolved = true
                throw RuntimeException("Check function passed to KEventWaiter failed", t)
            }
        }
    }

    override suspend fun resolve(): List<T> {
        try {
            while (!resolved) {
                delay(10)
                if (timedOut) {
                    deregister()
                    resolved = true
                }
            }
        } catch(e: CancellationException) {
            deregister()
            throw e
        }
        return result
    }

    private fun restartTimeout() {
        timeoutJob.cancel()
        timeoutJob = launch(CommonPool) {
            delay(timeoutMS)
            timedOut = true
            deregister()
        }
    }

    private fun deregister() {
        jda.removeEventListener(this)
        if (!timeoutJob.isCancelled && !timeoutJob.isActive) timeoutJob.cancel()
    }
}