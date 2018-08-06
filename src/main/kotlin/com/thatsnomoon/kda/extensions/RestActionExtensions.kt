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

import com.thatsnomoon.kda.globalCoroutineContext
import com.thatsnomoon.kda.toDuration
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.future.asDeferred
import kotlinx.coroutines.experimental.future.await
import kotlinx.coroutines.experimental.time.delay
import net.dv8tion.jda.core.requests.RestAction
import net.dv8tion.jda.core.requests.restaction.pagination.PaginationAction
import java.time.Duration
import java.util.concurrent.TimeUnit

/**
 * Queues and converts this RestAction into a [Deferred]3
 *
 * @return Deferred that resolves to the result of this action
 */
fun <T> RestAction<T>.asDeferred(): Deferred<T> = this.submit().asDeferred()

/**
 * Suspends until this RestAction is complete3
 *
 * @return The result of this action
 */
suspend fun <T> RestAction<T>.await(): T = this.submit().await()

/**
 * Queues this RestAction after [delay] and converts it into a [Deferred].
 *
 * @param delay [Duration] to wait before queueing this action
 *
 * @return Deferred that resolves to the result of this action
 */
infix fun <T> RestAction<T>.after(delay: Duration): Deferred<T> = async(globalCoroutineContext) {
    delay(delay)
    this@after.await()
}

/**
 * Queues this RestAction after [delay] in [TimeUnit] and converts it into a [Deferred].
 *
 * @param delay Time to wait before queueing this action
 * @param unit Unit of [delay]
 *
 * @return Deferred that resolves to the result of this action
 */
fun <T> RestAction<T>.after(delay: Long, unit: TimeUnit): Deferred<T> = this after toDuration(delay, unit)

/**
 * Maps the result of this [RestAction] to a new value by calling [success].
 *
 * This is simply a convenience function over [Deferred.map], using [asDeferred].
 *
 * @param success Function to map the resulting value of this RestAction
 * @return A Deferred that resolves to the return value of [success]
 */
inline infix fun <T, U> RestAction<T>.map(crossinline success: suspend (T) -> U): Deferred<U> = this.asDeferred() map success

/**
 * Adds a suspending success callback to this RestAction.
 *
 * This is simply a convenience function over [Deferred.then], using [asDeferred].
 *
 * @param success Callback to add to this RestAction
 */
inline infix fun <T> RestAction<T>.then(crossinline success: suspend (T) -> Unit) = this.asDeferred() then success

/**
 * Maps the result of this RestAction to a new value, "flattening" a nested [Deferred], by calling [success].
 *
 * This is simply a convenience function over [Deferred.flatMap], using [asDeferred].
 *
 * @param success Function to map the resulting value of this RestAction
 * @return Deferred that resolves to the resolution value of the return value of [success]
 */
inline infix fun <T, U> RestAction<T>.flatMap(crossinline success: suspend (T) -> Deferred<U>): Deferred<U> = this.asDeferred() flatMap success

/**
 * Adds a suspending failure callback to this RestAction.
 *
 * This is simply a convenience function over [Deferred.catch], using [asDeferred].
 *
 * @param failure Failure callback to add to this RestAction
 */
inline infix fun <T> RestAction<T>.catch(crossinline failure: suspend (Throwable) -> Unit) = this.asDeferred() catch failure

/**
 * Adds suspending success and failure callbacks to this RestAction.
 *
 * This is simply a convenience function over [Deferred.accept], using [asDeferred]
 *
 * @param success Success callback to add to this RestAction
 * @param failure Failure callback to add to this RestAction
 */
inline fun <T> RestAction<T>.accept(crossinline success: suspend (T) -> Unit, crossinline failure: suspend (Throwable) -> Unit) = this.asDeferred().accept(success, failure)

/**
 * Maps a failed RestAction to a successfully completed [Deferred].
 *
 * This is simply a convenience function over [Deferred.handle], using [asDeferred]
 *
 * @param failure Function to map the error of this RestAction to a successful value
 * @return A Deferred that resolves to the result of this action if successful, or the return value of [failure] otherwise
 */
inline infix fun <T> RestAction<T>.handle(crossinline failure: suspend (Throwable) -> T): Deferred<T> = this.asDeferred() handle failure

/**
 * Maps a successful or failed RestAction to a successfully completed [Deferred].
 *
 * This is simply a convenience function over [Deferred.apply], using [asDeferred]
 *
 * @param success Function to map the successful result of this RestAction
 * @param failure Function to map the error of this RestAction
 * @return A Deferred that resolves to the return value of [success] or [failure]
 */
inline fun <T, U> RestAction<T>.apply(crossinline success: suspend (T) -> U, crossinline failure: suspend (Throwable) -> U): Deferred<U> = this.asDeferred().apply(success, failure)

/**
 * Converts this PaginationAction into a [ReceiveChannel] that produces its values.
 *
 * @return ReceiveChannel that produces the values of this PaginationAction
 */
fun <T, M: PaginationAction<T, M>> PaginationAction<T, M>.toChannel(): ReceiveChannel<T> = produce(globalCoroutineContext) {
    var items: List<T> = this@toChannel.await()
    var cursor = 0
    while (cursor < items.size) {
        send(items[cursor])
        cursor += 1
        if (cursor == items.size) {
            items = this@toChannel.await()
            cursor = 0
        }
    }
}