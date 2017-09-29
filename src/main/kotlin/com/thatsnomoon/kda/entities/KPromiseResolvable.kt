package com.thatsnomoon.kda.entities

import kotlinx.coroutines.experimental.CancellationException
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlin.coroutines.experimental.CoroutineContext

/**
 * Implementation of KPromise, using a Resolvable interface.
 */
internal class KPromiseResolvable<T>(context: CoroutineContext, private val resolvable: Resolvable<T>): KPromise<T> {

    private var active = true
    private var completed = false
    private var completedExceptionally = false
    private var cancelled = false

    private var caught = false

    private var failureCallbacks = mutableListOf<suspend (Throwable) -> Unit>()
    private var successCallbacks = mutableListOf<suspend (T?) -> Unit>()

    private var result: T? = null
    private var exception = Throwable()

    private val job: Job

    init {
        job = launch(context) {
            try {
                result = resolvable.resolve()
                completed = true
                active = false
                successCallbacks.forEach { it(result) }
            } catch (t: Throwable) {
                exception = t
                completedExceptionally = true
                active = false
                when {
                    caught -> failureCallbacks.forEach { it(exception) }
                    hasGlobalHandler -> globalHandler(t)
                    else -> throw t
                }
            }
        }
    }

    override fun await(): T? {
        caught = true
        if (completed) {
            return result
        }
        if (completedExceptionally) {
            throw exception
        }
        if (cancelled) {
            throw CancellationException("KPromise cancelled while awaiting")
        }
        while (active) {
            Thread.sleep(10)
            if (completed) {
                return result
            }
            if (completedExceptionally) {
                throw exception
            }
            if (cancelled) {
                throw CancellationException("KPromise cancelled while awaiting")
            }
        }
        throw RuntimeException("What just happened?! Loop managed to exit without being active...")

    }

    override fun then(success: suspend (T?) -> Unit): KPromise<T> {
        successCallbacks.add(success)
        return this
    }

    override fun catch(failure: suspend (Throwable) -> Unit): KPromise<T> {
        failureCallbacks.add(failure)
        caught = true
        return this
    }

    override fun accept(success: suspend (T?) -> Unit, failure: suspend (Throwable) -> Unit): KPromise<T> {
        successCallbacks.add(success)
        failureCallbacks.add(failure)
        caught = true
        return this
    }

    override fun cancel() {
        job.cancel()
        cancelled = true
    }

    override fun isActive() = active

    override fun isCompleted() = completed

    override fun isCompletedExceptionally() = completedExceptionally

    override fun isCancelled() = cancelled

}