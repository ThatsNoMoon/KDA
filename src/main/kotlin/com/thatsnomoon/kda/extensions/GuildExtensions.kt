/*
 * Copyright 2018 Benjamin Scherer
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
import net.dv8tion.jda.core.entities.*
import net.dv8tion.jda.core.requests.restaction.ChannelAction
import net.dv8tion.jda.core.requests.restaction.RoleAction
import java.time.Duration

/**
 * Builds a [Role], using [RoleAction] as a "builder" class.
 *
 * Normal restrictions apply; you must have the proper permissions to create Roles for this function not to throw.
 *
 * @param delay [Duration] to wait before queueing this action
 * @param init Block to "build" the new Role, i.e. set desired options
 *
 * @return [Deferred] that will resolve to the created Role
 */
inline fun Guild.buildRole(delay: Duration = Duration.ZERO, init: RoleAction.() -> Unit): Deferred<Role> {
    val action = this.controller.createRole()
    action.init()
    return action after delay
}

/**
 * Builds a [TextChannel], using [ChannelAction] as a "builder" class.
 *
 * Normal restrictions apply; you must have the proper permissions to create Roles for this function not to throw.
 *
 * @param name Name to call the new TextChannel.
 * @param delay [Duration] to wait before queueing this action
 * @param init Block to "build" the new TextChannel, i.e. set desired options.
 *
 * @return [Deferred] that will resolve to the created TextChannel
 */
inline fun Guild.buildTextChannel(name: String, delay: Duration = Duration.ZERO, init: ChannelAction.() -> Unit): Deferred<TextChannel> {
    val action = this.controller.createTextChannel(name)
    action.init()
    return action after delay map { it as TextChannel }
}

/**
 * Builds a [VoiceChannel], using [ChannelAction] as a "builder" class.
 *
 * Normal restrictions apply; you must have the proper permissions to create Roles for this function not to throw.
 *
 * @param name Name to call the new VoiceChannel.
 * @param delay [Duration] to wait before queueing this action
 * @param init Block to "build" the new VoiceChannel, i.e. set desired options.
 *
 * @return [Deferred] that will resolve to the created VoiceChannel
 */
inline fun Guild.buildVoiceChannel(name: String, delay: Duration = Duration.ZERO, init: ChannelAction.() -> Unit): Deferred<VoiceChannel> {
    val action = this.controller.createVoiceChannel(name)
    action.init()
    return action after delay map { it as VoiceChannel }
}

/**
 * Retrieves the [Guild.Ban]s for this Guild.
 *
 * @param delay [Duration] to wait before queueing this action
 *
 * @return [Deferred] that will resolve to the list of bans
 */
fun Guild.bans(delay: Duration = Duration.ZERO): Deferred<List<Guild.Ban>> = this.banList after delay

/**
 * Retrieves the [Invite]s for this Guild.
 *
 * @param delay [Duration] to wait before queueing this action
 *
 * @return [Deferred] that will resolve to the list of invites
 */
fun Guild.invites(delay: Duration = Duration.ZERO): Deferred<List<Invite>> = this.invites after delay
