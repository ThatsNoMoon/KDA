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

import kotlinx.coroutines.experimental.Deferred
import net.dv8tion.jda.core.entities.Category
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.entities.VoiceChannel
import net.dv8tion.jda.core.requests.restaction.ChannelAction
import net.dv8tion.jda.core.requests.restaction.order.CategoryOrderAction
import java.time.Duration

/**
 * Builds a [TextChannel] in this category, using [ChannelAction] as a "builder" class.
 *
 * @param name Name to give the new channel
 * @param delay [Duration] to wait before queueing this action
 * @param init Block to "build" the channel, i.e. setting desired options
 *
 * @return [Deferred] that resolves to the created channel
 */
fun Category.buildTextChannel(name: String, delay: Duration = Duration.ZERO, init: ChannelAction.() -> Unit = {}): Deferred<TextChannel> {
    val action = this.createTextChannel(name)
    action.init()
    return action after delay map { it as TextChannel }
}

/**
 * Builds a [VoiceChannel] in this category, using [ChannelAction] as a "builder" class.
 *
 * @param name Name to give the new channel
 * @param delay [Duration] to wait before queueing this action
 * @param init Block to "build" the channel, i.e. set desired options
 *
 * @return [Deferred] that resolves to the created channel
 */
fun Category.buildVoiceChannel(name: String, delay: Duration = Duration.ZERO, init: ChannelAction.() -> Unit = {}): Deferred<VoiceChannel> {
    val action = this.createVoiceChannel(name)
    action.init()
    return action after delay map { it as VoiceChannel }
}

/**
 * Modifies text channel positions in this category, using [CategoryOrderAction] as a "builder" class to "build" the action.
 *
 * @param delay [Duration] to wait before queueing this action
 * @param init Block to "build" the action, i.e. set channel positions
 *
 * @return Empty [Deferred] that resolves when the action is complete
 */
fun Category.modifyTextChannelPositions(delay: Duration = Duration.ZERO, init: CategoryOrderAction<TextChannel>.() -> Unit): Deferred<Unit> {
    val action = this.modifyTextChannelPositions()
    action.init()
    return action after delay map { Unit }
}

/**
 * Modifies voice channel positions in this category, using [CategoryOrderAction] as a "builder" class to "build" the action.
 *
 * @param delay [Duration] to wait before queueing this action
 * @param init Block to "build" the action, i.e. set channel positions
 *
 * @return Empty [Deferred] that resolves when the action is complete
 */
fun Category.modifyVoiceChannelPositions(delay: Duration = Duration.ZERO, init: CategoryOrderAction<VoiceChannel>.() -> Unit): Deferred<Unit> {
    val action = this.modifyVoiceChannelPositions()
    action.init()
    return action after delay map { Unit }
}
