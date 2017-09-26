package com.thatsnomoon.kda.extensions

import com.thatsnomoon.kda.entities.KPromise
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.Role

/**
 * Blocking function to create a copy of this Role. Normal restrictions apply; you must have the proper permissions to create roles for this function not to throw.
 * @return A copy of this Role.
 */
fun Role.createCopy(): Role {
    return this.guild.controller.createCopyOfRole(this).complete()
}

/**
 * Asynchronous function to create a copy of this Role. Normal restrictions apply; you must have the proper permissions to create roles for this function not to throw.
 * @return A KPromise that resolves to a copy of this Role.
 */
fun Role.createCopyAsync(): KPromise<Role> {
    return this.guild.controller.createCopyOfRole(this).promisify()
}

/**
 * Blocking function to add this role to a member. Normal restrictions apply; you must have the proper permissions to add roles for this function not to throw.
 * @param member Member to add this role to.
 */
fun Role.addToMember(member: Member) {
    this.guild.controller.addSingleRoleToMember(member, this).complete()
}

/**
 * Asynchronous function to add this role to a member. Normal restrictions apply; you must have the proper permissions to add roles for this function not to throw.
 * @param member Member to add this role to.
 * @return A KPromise that resolves when this action is complete.
 */
fun Role.addToMemberAsync(member: Member): KPromise<Void> {
    return this.guild.controller.addSingleRoleToMember(member, this).promisify()
}

/**
 * Blocking function to remove this role from a member. Normal restrictions apply; you must have the proper permissions to remove roles for this function not to throw.
 * @param member Member to remove this role from.
 * @param reason Optional: reason to remove this role from the Member, displays in Audit Log.
 */
fun Role.removeFromMember(member: Member, reason: String? = null) {
    this.guild.controller.removeSingleRoleFromMember(member, this).reason(reason).complete()
}

/**
 * Asynchronous function to remove this role from a member. Normal restrictions apply; you must have the proper permissions to remove roles for this function not to throw.
 * @param member Member to remove this role from.
 * @param reason Optional: reason to remove this role from the Member, displays in Audit Log.
 * @return A KPromise that resolves when this action is complete.
 */
fun Role.removeFromMemberAsync(member: Member, reason: String? = null): KPromise<Void> {
    return this.guild.controller.removeSingleRoleFromMember(member, this).reason(reason).promisify()
}