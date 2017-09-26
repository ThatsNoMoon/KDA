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

import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newFixedThreadPoolContext
import net.dv8tion.jda.core.entities.impl.JDAImpl
import kotlin.repeat
import net.dv8tion.jda.core.events.Event
import net.dv8tion.jda.core.hooks.EventListener
import net.dv8tion.jda.core.hooks.IEventManager
import java.util.concurrent.CopyOnWriteArraySet

/**
 * Asynchronous event manager using Kotlin coroutines for handling events.
 * @param threadCount Thread count for the internal async thread pool for handling events
 */

class KAsyncEventManager(threadCount: Int = 1): IEventManager {

    private val listeners = CopyOnWriteArraySet<EventListener>()
    private val context = newFixedThreadPoolContext(threadCount, "Async-Event-Context")

    override fun handle(event: Event) {
        repeat(listeners.size) { index ->
            launch(context) {
                try {
                    listeners.elementAt(index).onEvent(event)
                } catch(t: Throwable) {
                    JDAImpl.LOG.fatal("An EventListener had an uncaught exception: \n$t\n\tat ${t.stackTrace.joinToString("\n\tat ")}")
                }
            }
        }
    }

    override fun register(listener: Any?) {
        if (listener !is EventListener) {
            throw IllegalArgumentException("Listener must implement EventListener")
        }
        listeners.add(listener)
    }

    override fun getRegisteredListeners(): MutableList<Any> {
        return listeners as MutableList<Any>
    }

    override fun unregister(listener: Any?) {
        listeners.remove(listener)
    }
}