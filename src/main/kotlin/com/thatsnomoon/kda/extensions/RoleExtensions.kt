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
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.Role
import net.dv8tion.jda.core.requests.restaction.RoleAction
import java.time.Duration

/**
 * Copies this role asynchronously, using [RoleAction] as a "builder" class.
 *
 * Normal restrictions apply; you must have the proper permissions to create roles for this function not to throw.
 *
 * @param reason Reason to add to the audit log
 * @param delay [Duration] to wait before queueing this action
 *
 * @return A [Deferred] that resolves to the copy of the role.
 */
fun Role.copy(reason: String = "", delay: Duration = Duration.ZERO, init: RoleAction.() -> Unit): Deferred<Role> {
    val action = this.createCopy()
    action.reason(reason)
    action.init()
    return action after delay
}

/**
 * Adds this role to a member. Normal restrictions apply; you must have the proper permissions to add roles for this function not to throw.
 *
 * @param member Member to add this role to
 * @param reason Reason to add to the audit log
 * @param delay [Duration] to wait before queueing this action
 *
 * @return Empty [Deferred] that resolves when this role has been added
 */
fun Role.addToMember(member: Member, reason: String = "", delay: Duration = Duration.ZERO): Deferred<Unit> =
        this.guild.controller.addSingleRoleToMember(member, this).reason(reason) after delay map { Unit }

/**
 * Removes this role from a member. Normal restrictions apply; you must have the proper permissions to remove roles for this function not to throw.
 *
 * @param member Member to remove this role from.
 * @param reason Reason to add to the audit log
 * @param delay [Duration] to wait before queueing this action
 */
fun Role.removeFromMember(member: Member, reason: String = "", delay: Duration = Duration.ZERO): Deferred<Unit> =
        this.guild.controller.removeSingleRoleFromMember(member, this).reason(reason) after delay map { Unit }
