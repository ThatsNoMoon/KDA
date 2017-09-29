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
 * @param context Optional: CoroutineContext in which to run the KPromise that this returns (default: CommonPool).
 * @param check Optional: predicate to check received events against. If this function returns true when passed the event, the event is added to the result to be returned (default: none, all events of the correct type are returned).
 *
 * @return A [KPromise] of a list of the events collected that passed the check function and were received before the timeout elapsed.
 */
fun <T: Event> JDA.awaitEvents(type: Class<T>,
                               count: Int = 1,
                               timeoutMS: Long = -1,
                               context: CoroutineContext = CommonPool,
                               check: (T) -> Boolean = {true}
): KPromise<List<T>> {
    return promisify(context, KEventWaiter(this, type, count, timeoutMS, check))
}