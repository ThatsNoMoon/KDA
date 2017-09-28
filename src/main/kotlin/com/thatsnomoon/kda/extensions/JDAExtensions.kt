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

package com.thatsnomoon.kda.extensions

import com.thatsnomoon.kda.entities.KEventWaiter
import com.thatsnomoon.kda.entities.KPromise
import com.thatsnomoon.kda.entities.promisify
import kotlinx.coroutines.experimental.CommonPool
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.events.Event
import kotlin.coroutines.experimental.CoroutineContext

/**
 * Await an event from Discord.
 *
 * @param type Type of event to listen for.
 * @param count Optional: number of events to wait for (default: 1).
 * @param timeoutMS Optional: milliseconds to wait before returning however many events have been collected, even if that amount is less than the specified count (default: -1, no timeout).
 * @param context Optional: CoroutineContext in which to run the KPromise that this returns (default: ForkJoinPool.commonPool()).
 * @param check Optional: predicate to check received events against. If this function returns true when passed the event, the event is added to the result to be returned (default: none, all events of the correct type are returned).
 *
 * @return A KPromise of a list of the events collected that passed the check function and were received before the timeout elapsed.
 */
fun <T: Event> JDA.awaitEvents(type: Class<T>,
                              count: Int = 1,
                              timeoutMS: Long = -1,
                              context: CoroutineContext = CommonPool,
                              check: (T) -> Boolean = {true}
): KPromise<List<T>> {
    return promisify(context, KEventWaiter(this, type, count, timeoutMS, check))
}