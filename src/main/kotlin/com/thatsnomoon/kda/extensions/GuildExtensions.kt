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

package com.thatsnomoon.kda.extensions

import com.thatsnomoon.kda.entities.KPromise
import com.thatsnomoon.kda.entities.promisify
import kotlinx.coroutines.experimental.delay
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Role
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.entities.VoiceChannel
import net.dv8tion.jda.core.requests.restaction.ChannelAction
import net.dv8tion.jda.core.requests.restaction.RoleAction

/**
 * Blocking function to create a Role using the RoleAction class as a builder.
 *
 * Normal restrictions apply; you must have the proper permissions to create Roles for this function not to throw.
 * @param init Function to call on a RoleAction to configure the desired role.
 * @return The resulting Role.
 */
inline fun Guild.buildRole(init: RoleAction.() -> Unit): Role {
    val action = this.controller.createRole()
    action.init()
    return action.complete()
}

/**
 * Asynchronous function to create a Role using the RoleAction class as a builder.
 *
 * Normal restrictions apply; you must have the proper permissions to create Roles for this function not to throw.
 * @param init Function to call on a RoleAction to configure the desired role.
 * @return A KPromise that will resolve to the Role returned by completing the RoleAction.
 */
inline fun Guild.buildRoleAsync(init: RoleAction.() -> Unit): KPromise<Role> {
    val action = this.controller.createRole()
    action.init()
    return action.promisify()
}

/**
 * Blocking function to create a TextChannel using the ChannelAction class as a builder.
 *
 * Normal restrictions apply; you must have the proper permissions to create Roles for this function not to throw.
 * @param name Name to call the resulting TextChannel.
 * @param init Function to call on a ChannelAction to configure the desired TextChannel.
 * @return The resulting TextChannel.
 */
inline fun Guild.buildTextChannel(name: String, init: ChannelAction.() -> Unit): TextChannel {
    val action = this.controller.createTextChannel(name)
    action.init()
    return action.complete() as TextChannel
}

/**
 * Asynchronous function to create a TextChannel using the ChannelAction class as a builder.
 *
 * Normal restrictions apply; you must have the proper permissions to create Roles for this function not to throw.
 * @param name Name to call the resulting TextChannel.
 * @param init Function to call on a ChannelAction to configure the desired TextChannel.
 * @return A KPromise that resolves to the resulting TextChannel.
 */
inline fun Guild.buildTextChannelAsync(name: String, init: ChannelAction.() -> Unit): KPromise<TextChannel> {
    val action = this.controller.createTextChannel(name)
    action.init()
    return promisify {
        val future = action.submit()
        while (!future.isDone) {
            delay(10)
        }
        future.get() as TextChannel
    }
}

/**
 * Blocking function to create a VoiceChannel using the ChannelAction class as a builder.
 *
 * Normal restrictions apply; you must have the proper permissions to create Roles for this function not to throw.
 * @param name Name to call the resulting TextChannel.
 * @param init Function to call on a ChannelAction to configure the desired VoiceChannel.
 * @return The resulting VoiceChannel.
 */
inline fun Guild.buildVoiceChannel(name: String, init: ChannelAction.() -> Unit): VoiceChannel {
    val action = this.controller.createVoiceChannel(name)
    action.init()
    return action.complete() as VoiceChannel
}

/**
 * Asynchronous function to create a VoiceChannel using the ChannelAction class as a builder.
 *
 * Normal restrictions apply; you must have the proper permissions to create Roles for this function not to throw.
 * @param name Name to call the resulting VoiceChannel.
 * @param init Function to call on a ChannelAction to configure the desired VoiceChannel.
 * @return A KPromise that resolves to the resulting VoiceChannel.
 */
inline fun Guild.buildVoiceChannelAsync(name: String, init: ChannelAction.() -> Unit): KPromise<VoiceChannel> {
    val action = this.controller.createVoiceChannel(name)
    action.init()
    return promisify {
        val future = action.submit()
        while (!future.isDone) {
            delay(10)
        }
        future.get() as VoiceChannel
    }
}