/*
 * Copyright 2018 Benjamin Scherer
 *
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

import com.thatsnomoon.kda.globalCoroutineContext
import java.time.Duration
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.time.delay
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.events.Event

/**
 * Asynchronously waits for JDA events.
 *
 * This class will primarily be created by a helper function ([awaitEvents][com.thatsnomoon.kda.extensions.awaitEvents] or [awaitMessages][com.thatsnomoon.kda.extensions.awaitMessages]).
 *
 * This class is similar to a [Deferred], in that it will resolve to a value at some point in time.
 * There is also a method to create a Deferred that will resolve to the same result, [asDeferred].
 *
 * Until this resolves, it will continue to listen for events, adding any events of the correct type that pass the provided predicate to an internal list.
 * It will resolve to this internal list after either of these conditions are true:
 *  1. The amount of events that have matched the predicate function equals the count parameter
 *  2. The amount of time that has passed has exceeded the timeout parameter
 *
 * @param jda JDA instance to listen from
 * @param type Type of event to listen for
 * @param count Number of events to listen for before returning
 * @param timeout [Duration] to wait for events before timing out (and resolving to an empty list) (default: null, no timeout).
 * @param predicate Function to check each event received of the correct type against (default: none, all events of the correct type are returned)
 */
class KEventWaiter<out T: Event>(private val jda: JDA,
                                 private val type: Class<T>,
                                 private val count: Int = 1,
                                 timeout: Duration? = null,
                                 private val predicate: (T) -> Boolean = {true}
): KAsyncEventListener {

    private var timeoutJob: Job? = null

    private var result: CompletableDeferred<List<T>> = CompletableDeferred()
    private var events: List<T> = mutableListOf()

    init {
        if (count < 1) throw IllegalArgumentException("Count parameter passed to KEventWaiter must be greater than zero")
        jda.addEventListener(this)
        if (timeout != null) {
            timeoutJob = launch(globalCoroutineContext) {
                delay(timeout)
                result.complete(events)
                deregister()
            }
        }
    }


    /**
     * Internal event listening function.
     */
    override suspend fun onEventAsync(event: Event) {
        if (type.isInstance(event)) {
            try {
                @Suppress("UNCHECKED_CAST")
                if (predicate(event as T)) {
                    events += event
                    if (events.size == count) {
                        result.complete(events)
                        deregister()
                    }
                }
            } catch (t: Throwable) {
                result.completeExceptionally(t)
                deregister()
            }
        }
    }

    /**
     * Suspends until this event waiter is resolved.
     *
     * This function suspends until one of two things happens:
     *  1. The amount of events that match the predicate function equals count.
     *  2. The timeout duration has expired.
     *
     *  @return A List of all the events collected.
     */
    suspend fun await(): List<T> = result.await()

    /**
     * Returns a [Deferred] that resolves to this waiter's events when it is resolved.
     *
     * @return A [Deferred] that resolves when this waiter is resolved.
     */
    fun asDeferred(): Deferred<List<T>> = result

    /**
     * Cancels this event waiter; no further events will be recorded.
     *
     * If this was not already resolved, any calls to [await] will throw [CancellationException] after this call, and any [Deferred]s returned by [asDeferred] will be cancelled.
     * If this was already resolved, this call will throw [IllegalStateException]
     */
    fun cancel() {
        if (result.isActive) {
            deregister()
            result.cancel()
        } else {
            throw IllegalStateException("Cannot cancel a resolved event waiter")
        }
    }

    /**
     * Basically delete this event waiter, removing it from the jda instance and removing the timeout coroutine.
     */
    private fun deregister() {
        try {
            jda.removeEventListener(this)
        } catch (e: Exception) {/* ignore */}
        if (timeoutJob?.isCancelled != true && timeoutJob?.isActive != true) timeoutJob?.cancel()
    }
}