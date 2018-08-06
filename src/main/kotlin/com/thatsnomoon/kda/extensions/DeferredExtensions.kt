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
import kotlinx.coroutines.experimental.CompletableDeferred
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking

/**
 * Blocks until this [Deferred] is resolved, returning the result or throwing the resulting exception.
 *
 * @return The successful resolved value of this Deferred, if one exists
 */
fun <T> Deferred<T>.block(): T = runBlocking { this@block.await() }

/**
 * Maps the resolution value of this [Deferred], returning a new Deferred.
 *
 * In more words, this extension returns a new Deferred that resolves to the return value of [success] when passed the resolved value of this Deferred.
 *
 * This is a functional-style extension that allows for nicer chaining than is otherwise possible, especially in non-suspending contexts.
 *
 * This extension is similar to other methods of the same name in Java and Kotlin standard library, such as [Iterable.map] and [java.util.Optional.map].
 *
 * @param success Function to map the resolution value of this Deferred
 *
 * @return A new Deferred that will resolve to the return of [success]
 */
inline infix fun <T, U> Deferred<T>.map(crossinline success: suspend (T) -> U): Deferred<U> {
    val ret: CompletableDeferred<U> = CompletableDeferred()
    launch(globalCoroutineContext) {
        try {
            val res = this@map.await()
            ret.complete(success(res))
        } catch (t: Throwable) {
            ret.completeExceptionally(t)
        }
    }
    return ret
}

/**
 * Executes a function after this [Deferred] is resolved successfully (if it is resolved successfully).
 *
 * This extension is literally [Deferred.map] but does not return a new Deferred. It is therefore not truly functional, but may fit better into some contexts.
 *
 * @param success Function to call after this Deferred is resolved successfully
 */
inline infix fun <T> Deferred<T>.then(crossinline success: suspend (T) -> Unit) {
    launch(globalCoroutineContext) {
        try {
            success(this@then.await())
        } catch (t: Throwable) { }
    }
}

/**
 * Maps the resolution value of this [Deferred], simultaneously "flattening" a nested Deferred, returning a new Deferred.
 *
 * This extension is useful when you want to map a Deferred, but your function would return another Deferred.
 * This extension will remove the extra Deferred, removing any need for extra [Deferred.await] calls or Deferred<Deferred<*>> types.
 *
 * In more words, this extension returns a new Deferred that resolves to the resolved value of the return value of [success] when passed the resolved value of this Deferred.
 *
 * This is a functional-style extension that allows for nicer chaining than is otherwise possible, especially in non-suspending contexts.
 *
 * This extension is similar to other methods of the same name in Java and Kotlin standard library, such as [Iterable.flatMap] and [java.util.stream.Stream.flatMap]
 *
 * @param success Function to map the resolution value of this Deferred to a new Deferred
 *
 * @return A new Deferred that resolves to the resolved value of the return of [success]
 */
inline infix fun <T, U> Deferred<T>.flatMap(crossinline success: suspend (T) -> Deferred<U>): Deferred<U> {
    val ret: CompletableDeferred<U> = CompletableDeferred()
    launch(globalCoroutineContext) {
        try {
            val res = this@flatMap.await()
            ret.complete(success(res).await())
        } catch (t: Throwable) {
            ret.completeExceptionally(t)
        }
    }
    return ret
}

/**
 * Maps an exceptionally completed [Deferred] to a successfully completed one, returning a new Deferred.
 *
 * In more words, this extension returns a new Deferred that resolves to the resolved value of this Deferred if it resolves successfully, or if this Deferred resolves with an exception, it resolves to the return value of [failure] when passed that exception.
 *
 * As its name suggests, this extension is useful for handling and recovering from errors without interrupting a logic chain.
 *
 * @param failure Function to map an exceptional result to a successful one
 *
 * @return A new Deferred that will resolve to the successful resolution value of this Deferred, or the return of [failure] if this Deferred resolves exceptionally
 */
inline infix fun <T> Deferred<T>.handle(crossinline failure: suspend (Throwable) -> T): Deferred<T> {
    val ret: CompletableDeferred<T> = CompletableDeferred()
    launch(globalCoroutineContext) {
        try {
            val res = this@handle.await()
            ret.complete(res)
        } catch (t: Throwable) {
            ret.complete(failure(t))
        }
    }
    return ret
}

/**
 * Executes a function after this [Deferred] is resolved exceptionally (if it is resolved exceptionally).
 *
 * This extension is literally [Deferred.handle] but does not return a new Deferred. It is therefore not truly functional, but may fit better into some contexts.
 *
 * @param failure Function to call after this Deferred is resolved exceptionally
 */
inline infix fun <T> Deferred<T>.catch(crossinline failure: suspend (Throwable) -> Unit) {
    launch(globalCoroutineContext) {
        try {
            this@catch.await()
        } catch (t: Throwable) {
            failure(t)
        }
    }
}

/**
 * Maps the resolution value or exception of this [Deferred], returning a new Deferred.
 *
 * This extension is essentially a combination of [Deferred.map] and [Deferred.handle], see their documentation for more information.
 *
 * @param success Function to map a successful resolution value to a new value
 * @param failure Function to map an exceptional resolution value to a new value
 *
 * @return A new Deferred that resolves to the return of [success] or [failure], depending on the resolution of this Deferred
 *
 * @see [Deferred.map]
 * @see [Deferred.handle]
 */
inline fun <T, U> Deferred<T>.apply(crossinline success: suspend (T) -> U, crossinline failure: suspend (Throwable) -> U): Deferred<U> {
    val ret: CompletableDeferred<U> = CompletableDeferred()
    launch(globalCoroutineContext) {
        try {
            val res = this@apply.await()
            ret.complete(success(res))
        } catch (t: Throwable) {
            ret.complete(failure(t))
        }
    }
    return ret
}

/**
 * Executes a function after this Deferred resolves, exceptionally or successfully.
 *
 * This extension is essentially a combination of [Deferred.then] and [Deferred.catch], see their documentation for more information.
 *
 * @param success Function to call after successful resolution
 * @param failure Function to call after exceptional resolution
 *
 * @see [Deferred.then]
 * @see [Deferred.catch]
 */
inline fun <T> Deferred<T>.accept(crossinline success: suspend (T) -> Unit, crossinline failure: suspend (Throwable) -> Unit) {
    launch(globalCoroutineContext) {
        try {
            val res = this@accept.await()
            success(res)
        } catch (t: Throwable) {
            failure(t)
        }
    }
}