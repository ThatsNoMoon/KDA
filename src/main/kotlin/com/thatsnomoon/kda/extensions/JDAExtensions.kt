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

package com.thatsnomoon.kda.extensions

import com.thatsnomoon.kda.entities.KEventWaiter
import kotlinx.coroutines.experimental.Deferred
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.events.Event
import java.time.Duration

/**
 * Await events from Discord.
 *
 * @param type Type of event to listen for
 * @param count Number of events to wait for
 * @param timeout [Duration] to wait before returning however many events have been collected, even if that amount is less than the specified count (default: null, no timeout)
 * @param predicate Predicate to check received events against. If this function returns true when passed the event, the event is added to the result to be returned (default: none, all events of the correct type are returned)
 *
 * @return [Deferred] that will resolve to a list of the events collected that passed the check function and were received before the timeout elapsed
 */

fun <E: Event> JDA.awaitEvents(type: Class<E>,
                               count: Int = 1,
                               timeout: Duration? = null,
                               predicate: (E) -> Boolean = { true }
): Deferred<List<E>> =
        KEventWaiter(this, type, count, timeout, predicate).asDeferred()