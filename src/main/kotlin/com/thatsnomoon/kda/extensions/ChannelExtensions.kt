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
import net.dv8tion.jda.core.entities.*
import net.dv8tion.jda.core.requests.restaction.ChannelAction
import net.dv8tion.jda.core.requests.restaction.InviteAction
import net.dv8tion.jda.core.requests.restaction.PermissionOverrideAction
import java.time.Duration

/**
 * Copies this Channel, using [ChannelAction] as a "builder" class.
 *
 * Normal restrictions apply; you must have the proper permissions to create channels for this function not to throw.
 *
 * @param guild [Guild] to create the copy in; defaults to this Channel's Guild
 * @param delay Duration to wait before queueing this action
 * @param init Block to "build" the channel, i.e. set desired options
 *
 * @return A [Deferred] that will resolve to a copy of this Channel
 */
fun Channel.copy(guild: Guild = this.guild, delay: Duration = Duration.ZERO, init: ChannelAction.() -> Unit = {}): Deferred<Channel> {
    val action = this.createCopy(guild)
    action.init()
    return action after delay
}

/**
 * Creates an [Invite] to this Channel, using [InviteAction] as a "builder" class.
 *
 * Normal restrictions apply; you must have the proper permissions to create invites for this function not to throw.
 *
 * @param delay [Duration] to wait before queueing this action
 * @param init Block to "build" the invite, i.e. set desired options
 *
 * @return A [Deferred] that will resolve to the invite to this Channel
 */
fun Channel.invite(delay: Duration = Duration.ZERO, init: InviteAction.() -> Unit = {}): Deferred<Invite> {
    val action = this.createInvite()
    action.init()
    return action after delay
}

/**
 * Retrieves the [Invite]s to this channel.
 *
 * @param delay [Duration] to wait before queueing this action
 *
 * @return A [Deferred] that will resolve to the list of invites to this channel
 */
fun Channel.invites(delay: Duration = Duration.ZERO): Deferred<List<Invite>> = this.invites after delay

/**
 * Creates a [PermissionOverride] for a [Role] in this channel.
 *
 * @param role Role to add a permission override for
 * @param delay [Duration] to wait before queueing this action
 * @param init Block to "Build" the permission override, i.e. set desired options
 *
 * @return a [Deferred] that will resolve to the permission override
 */
fun Channel.createOverride(role: Role, delay: Duration = Duration.ZERO, init: PermissionOverrideAction.() -> Unit): Deferred<PermissionOverride> {
    val action = this.putPermissionOverride(role)
    action.init()
    return action after delay
}

/**
 * Creates a [PermissionOverride] for a [Member] in this channel.
 *
 * @param member Member to add a permission override for
 * @param delay [Duration] to wait before queueing this action
 * @param init Block to "Build" the permission override, i.e. set desired options
 *
 * @return a [Deferred] that will resolve to the permission override
 */
fun Channel.createOverride(member: Member, delay: Duration = Duration.ZERO, init: PermissionOverrideAction.() -> Unit): Deferred<PermissionOverride> {
    val action = this.putPermissionOverride(member)
    action.init()
    return action after delay
}
