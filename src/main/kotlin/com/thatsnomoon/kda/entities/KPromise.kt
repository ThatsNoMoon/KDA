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

import kotlinx.coroutines.experimental.CommonPool
import kotlin.coroutines.experimental.CoroutineContext

internal var hasGlobalHandler = false
internal var globalHandler: suspend (Throwable) -> Unit = {}

/**
 * Sets the global uncaught promise handler.
 *
 * Any [KPromise]s that do not have any failure callbacks will call this if/when they complete exceptionally.
 * An [await][KPromise.await] call on a KPromise that resolves exceptionally will still throw an exception, and this handler will **not** be called when that KPromise resolves exceptionally.
 * @param handler Optionally suspending function to call when KPromises with no specified failure callbacks or await calls resolve exceptionally.
 */
fun setUncaughtPromiseHandler(handler: suspend (Throwable) -> Unit) {
    synchronized(globalHandler) {
        hasGlobalHandler = true
        globalHandler = handler
    }
}

/**
 * Helper function to turn a suspending or blocking function into a [KPromise].
 *
 * @param context Optional: CoroutineContext to run this KPromise in. If none is specified, CommonPool is used.
 * @param function Function to promisify. If this function returns normally, the returned KPromise will resolve with that return. If this function throws an exception, the returned KPromise will resolve with that exception.
 * @return A new KPromise that resolves to the result of the function parameter.
 */
fun <T> promisify(context: CoroutineContext = CommonPool, function: suspend () -> T): KPromise<T> {
    return KPromiseImpl(context, function)
}

/**
 * Helper function to create a [KPromise] from a [Resolvable]
 *
 * @param context Optional: CoroutineContext to run this KPromise in. If none is specified, CommonPool is used.
 * @param resolvable Resolvable to promisify. If calling this object's [resolve][Resolvable.resolve] method returns normally, the returned KPromise will resolve with that return. If resolve throws an exception, the returned KPromise will resolve with that exception.
 * @return A new KPromise that resolves to the result of calling the resolvable's resolve method.
 */
fun <T> promisify(context: CoroutineContext = CommonPool, resolvable: Resolvable<T>): KPromise<T> {
    return KPromiseResolvable(context, resolvable)
}

/**
 * KPromises are a form of turning a blocking or suspending function into a promise, returning the result of the function or the exception if one occurred while executing the function.
 *
 * When a KPromise resolves successfully (the initial function returned a value) the success callbacks will be called, in the order of how they were added.
 * When a KPromise resolves exceptionally (the initial function throws an exception) the failure callbacks will be called, in the order of how they were added.
 *
 * All callbacks are executed within a coroutine, thus callbacks can be suspending.
 *
 * If an active KPromise that is being awaited (by a call to [await]) completes exceptionally, the [await] call will throw the corresponding exception.
 * If [cancel] is called on an active KPromise, any running [await] calls will throw CancellationException, and any future calls to [await] will also throw CancellationException.
 */

interface KPromise<T> {

    /**
     * Blocking function that returns the result of this KPromise when it is complete.
     *
     * If this KPromise completes exceptionally, this method will throw that exception when it is called or when the promise resolves.
     * @return The resolved value of this KPromise, if one exists.
     */
    fun await(): T?

    /**
     * Add a success callback that will execute if/when this KPromise resolves successfully.
     *
     * Success callbacks are executed in the order that they are added.
     * @param success Optionally suspending success callback function to add to this KPromise.
     * @return This KPromise, used for chaining callbacks.
     *
     * @see accept
     */
    infix fun then(success: suspend (T?) -> Unit): KPromise<T>

    /**
     * Add a failure callback that will execute if/when this KPromise resolves exceptionally.
     *
     * Failure callbacks are executed in the order that they are added.
     * @param failure Optionally suspending failure callback function to add to this KPromise.
     * @return This KPromise, used for chaining callbacks.
     *
     * @see accept
     */
    infix fun catch(failure: suspend (Throwable) -> Unit): KPromise<T>

    /**
     * Add a success callback and a failure callback that will execute when this KPromise resolves.
     *
     * Callbacks are executed in the order that they are added.
     * @param success Optionally suspending success callback function to add to this KPromise.
     * @param failure Optionally suspending failure callback function to add to this KPromise.
     * @return This KPromise, used for chaining callbacks.
     *
     * @see then
     * @see catch
     */
    fun accept(success: suspend (T?) -> Unit, failure: suspend (Throwable) -> Unit): KPromise<T>

    /**
     * Cancel an active KPromise.
     *
     * Any running await() calls will throw CancellationException when this method is called.
     */
    fun cancel()

    /**
     * Check whether or not this KPromise is still active.
     * @return True if this KPromise is active (has not resolved yet), false otherwise.
     */
    fun isActive(): Boolean

    /**
     * Check whether or not this KPromise has completed successfully.
     * @return True if this KPromise completed successfully (returned a value), false otherwise.
     */
    fun isCompleted(): Boolean

    /**
     * Check whether or not this KPromise has completed exceptionally.
     * @return True if this KPromise completed exceptionally (threw an exception), false otherwise.
     */
    fun isCompletedExceptionally(): Boolean

    /**
     * Check whether or not this KPromise has been cancelled.
     * @return True if this KPromise was cancelled (using cancel()), false otherwise.
     */
    fun isCancelled(): Boolean
}