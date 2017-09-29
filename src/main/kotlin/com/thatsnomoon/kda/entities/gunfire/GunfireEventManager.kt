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

package com.thatsnomoon.kda.entities.gunfire

import club.minnced.gunfire.core.Gun
import club.minnced.gunfire.core.fire
import net.dv8tion.jda.core.events.Event
import net.dv8tion.jda.core.hooks.IEventManager

class GunfireEventManager: Gun(), IEventManager {

    override fun handle(event: Event) {
        fire { EventBullet(event) }
    }

    override fun register(listener: Any?) {
        if (listener !is GunfireTarget) {
            throw IllegalArgumentException("Listener must implement GunfireTarget")
        }
        registerTarget(EventBullet::class.java, listener)
    }

    override fun getRegisteredListeners(): MutableList<Any> {
        return targets as MutableList<Any>
    }

    override fun unregister(listener: Any?) {
        targets.remove(listener)
    }

}