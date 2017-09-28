package com.thatsnomoon.kda.extensions

import com.thatsnomoon.kda.entities.KPromise
import com.thatsnomoon.kda.entities.promisify
import kotlinx.coroutines.experimental.delay
import net.dv8tion.jda.core.requests.RestAction
import java.util.concurrent.TimeUnit

/**
 * Function to turn a RestAction into a KPromise that will resolve to the value of the completed RestAction.
 * @return KPromise that will resolve to the value of the completed RestAction.
 */
fun <T> RestAction<T>.promisify(): KPromise<T> {
    return promisify {
        val future = this@promisify.submit()
        while (!future.isDone) {
            delay(10)
        }
        future.get()
    }
}

/**
 * Function to turn a RestAction into a KPromise that will resolve to the value of the completed RestAction after the specified time.
 *
 * Note that while the KPromise will be returned immediately, the RestAction will be queued after the specified amount of time.
 * By default, delay is in milliseconds.
 * @param delay Time to wait before queuing this RestAction
 * @param unit TimeUnit to use for delay. Default is milliseconds.
 * @return KPromise that will resolve to the value of the completed RestAction.
 */
fun <T> RestAction<T>.promisifyAfter(delay: Long, unit: TimeUnit = TimeUnit.MILLISECONDS): KPromise<T> {
    return promisify {
        val future = this@promisifyAfter.submitAfter(delay, unit)
        delay(delay)
        while (!future.isDone) {
            delay(10)
        }
        future.get()
    }
}

/**
 * Convenience function to simultaneously turn this RestAction into a KPromise that will resolve to the value of the completed RestAction, and add a success callback to that KPromise.
 * @param success Success callback to add to the resulting KPromise.
 * @return KPromise that will resolve to the value of the completed RestAction, used for chaining callbacks.
 */
infix fun <T> RestAction<T>.then(success: suspend (T?) -> Unit): KPromise<T> {
    return this.promisify().then(success)
}

/**
 * Convenience function to simultaneously turn this RestAction into a KPromise that will resolve to the value of the completed RestAction, and add a failure callback to that KPromise.
 * @param failure Failure callback to add to the resulting KPromise.
 * @return KPromise that will resolve to the value of the completed RestAction, used for chaining callbacks.
 */
infix fun <T> RestAction<T>.catch(failure: suspend (Throwable) -> Unit): KPromise<T> {
    return this.promisify().catch(failure)
}

/**
 * Convenience function to simultaneously turn this RestAction into a KPromise that will resolve to the value of the completed RestAction, and to add both a success callback and a failure callback to that KPromise.
 * @param success Success callback to add to the resulting KPromise.
 * @param failure Failure callback to add to the resulting KPromise.
 * @return KPromise that will resolve to the value of the completed RestAction, used for chaining callbacks.
 */
fun <T> RestAction<T>.accept(success: suspend (T?) -> Unit, failure: suspend (Throwable) -> Unit): KPromise<T> {
    return this.promisify().accept(success, failure)
}