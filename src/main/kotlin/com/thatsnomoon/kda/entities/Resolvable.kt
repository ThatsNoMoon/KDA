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

/**
 * Interface for a suspending function that returns a value.
 *
 * This is similar to the [Callable][java.util.concurrent.Callable] interface, but uses a suspending function.
 */
interface Resolvable<out T> {

    suspend fun resolve(): T

    fun cancel()
}