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

/**
 * Class to await JDA events.
 *
 * To use this class, either use one of the helper functions ([awaitEvents][com.thatsnomoon.kda.extensions.awaitEvents] or [awaitMessages][com.thatsnomoon.kda.extensions.awaitMessages]) or initialize it with the primary constructor, and call [resolve], which is a suspending function that will suspend until one of two things happens:
 *  1. The amount of events that have matched the check function equals count.
 *  2. The amount of milliseconds that have passed exceeded timeoutMS.
 *
 * Note that when used with a [KPromise] (i.e. [awaitEvents][com.thatsnomoon.kda.extensions.awaitEvents]), default implementation will **never** return a null value.
 *
 * @param jda JDA instance to listen from.
 * @param type Type of event to listen for.
 * @param count Optional: number of events to listen for before returning (default: 1).
 * @param timeoutMS Optional: amount of time, in milliseconds, to wait for events before timing out (and resolving to an empty list) (default: -1, no timeout).
 * @param check Optional: function to check each event received of the correct type against (default: none, all events of the correct type are returned).
 */
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

    private var result: List<T> = mutableListOf()

    /**
     * Internal event listening function.
     */
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
                }
            } catch (t: Throwable) {
                resolved = true
                deregister()
                throw RuntimeException("Check function passed to KEventWaiter failed", t)
            }
        }
    }

    /**
     * Suspends until this event waiter is resolved.
     *
     * This function suspends until one of two things happens:
     *  1. The amount of events that match the check function equals count.
     *  2. The amount of milliseconds that have passed exceeded timeoutMS.
     *
     *  @return A List of all the events collected.
     */
    override suspend fun resolve(): List<T> {
        try {
            while (!resolved) {
                delay(10)
                if (timedOut) {
                    resolved = true
                }
            }
        } catch(e: CancellationException) {
            deregister()
            throw e
        }
        return result
    }

    /**
     * Cancels this event waiter, forcing any [resolve] calls to return any events collected so far.
     */
    override fun cancel() {
        deregister()
        resolved = true
    }

    /**
     * Basically delete this event waiter, removing it from the jda instance and removing the timeout coroutine.
     */
    private fun deregister() {
        try {
            jda.removeEventListener(this)
        } catch (e: Exception) {/* ignore */}
        if (!timeoutJob.isCancelled && !timeoutJob.isActive) timeoutJob.cancel()
    }
}