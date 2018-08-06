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

package com.thatsnomoon.kda.entities

import kotlinx.coroutines.experimental.runBlocking
import net.dv8tion.jda.core.events.Event
import net.dv8tion.jda.core.hooks.EventListener

/**
 * A suspending version of [EventListener].
 *
 * This also extends EventListener; any calls to [onEvent] simply use [runBlocking] to call [onEventAsync] blocking.
 *
 * @see EventListener
 * @see KAsyncListenerAdapter
 */
interface KAsyncEventListener: EventListener {

    /**
     * Handles an event.
     *
     * @param event The event to handle
     */
    suspend fun onEventAsync(event: Event)

    override fun onEvent(event: Event) = runBlocking {
        this@KAsyncEventListener.onEventAsync(event)
    }
}