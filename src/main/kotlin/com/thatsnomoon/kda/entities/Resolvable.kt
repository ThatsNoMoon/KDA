package com.thatsnomoon.kda.entities

/**
 * Interface for a suspending function that returns a value.
 *
 * This is similar to the [Callable][java.util.concurrent.Callable] interface, but uses a suspending function.
 */
interface Resolvable<out T> {

    suspend fun resolve(): T

    fun cancel()
}