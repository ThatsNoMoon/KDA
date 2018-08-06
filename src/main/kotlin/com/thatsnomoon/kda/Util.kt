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

package com.thatsnomoon.kda

import kotlinx.coroutines.experimental.CommonPool
import java.time.Duration
import java.util.concurrent.TimeUnit
import kotlin.coroutines.experimental.CoroutineContext

/**
 * The global [CoroutineContext] for the library.
 *
 * This context is used by everything that launches a coroutine other than [com.thatsnomoon.kda.entities.KAsyncEventManager].
 */
var globalCoroutineContext: CoroutineContext = CommonPool

/**
 * Converts a number and a time unit to a [Duration].
 *
 * @param units Units of [time]
 * @param time Time in [units]
 *
 * @return Duration representation of the number in units
 */
fun toDuration(time: Long, units: TimeUnit): Duration {
    return when (units) {
        TimeUnit.NANOSECONDS -> Duration.ofNanos(time)
        TimeUnit.MICROSECONDS -> Duration.ofNanos(time * 1000)
        TimeUnit.MILLISECONDS -> Duration.ofMillis(time)
        TimeUnit.SECONDS -> Duration.ofSeconds(time)
        TimeUnit.MINUTES -> Duration.ofMinutes(time)
        TimeUnit.HOURS -> Duration.ofHours(time)
        TimeUnit.DAYS -> Duration.ofDays(time)
    }
}