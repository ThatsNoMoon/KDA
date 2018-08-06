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
import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.MessageEmbed
import java.time.Duration

/**
 * Bans this member.
 *
 * @param delDays Days to delete this Member's messages in
 * @param reason Reason to ban this Member, recorded in the Audit Log
 * @param delay [Duration] to wait before queueing this action
 *
 * @return Empty [Deferred] that resolves when this action is complete
 */
fun Member.ban(delDays: Int = 0, reason: String = "", delay: Duration = Duration.ZERO): Deferred<Unit> =
        this.guild.controller.ban(this, delDays, reason) after delay map { Unit }

/**
 * Kicks this member.
 *
 * @param reason Reason to kick this Member, recorded in the Audit Log
 * @param delay [Duration] to wait before queueing this action
 *
 * @return Empty [Deferred] that resolves when this action is complete
 */
fun Member.kick(reason: String = "", delay: Duration = Duration.ZERO): Deferred<Unit> =
        this.guild.controller.kick(this, reason) after delay map { Unit }

/**
 * Sends this member a direct message.
 *
 * @param text Message content to send
 * @param delay [Duration] to wait before queueing this action
 *
 * @return [Deferred] that resolves to the sent Message
 */
fun Member.send(text: String, delay: Duration = Duration.ZERO): Deferred<Message> =
        this.user.openPrivateChannel() flatMap { it.send(text, delay) }

/**
 * Sends this member a direct message.
 *
 * @param message Message to send
 * @param delay [Duration] to wait before queueing this action
 *
 * @return [Deferred] that resolves to the sent Message
 */
fun Member.send(message: Message, delay: Duration = Duration.ZERO): Deferred<Message> =
        this.user.openPrivateChannel() flatMap { it.send(message, delay) }

/**
 * Sends this member a direct message.
 *
 * @param init Function to "build" the message to send, i.e. set content and options
 * @param delay [Duration] to wait before queueing this action
 *
 * @return [Deferred] that resolves to the sent Message
 */
inline fun Member.send(delay: Duration = Duration.ZERO, init: MessageBuilder.() -> Unit): Deferred<Message> {
    val builder = MessageBuilder()
    builder.init()
    return this.send(builder.build(), delay)
}

/**
 * Sends this member a direct message.
 *
 * @param embed Embed to send
 * @param delay [Duration] to wait before queueing this action
 *
 * @return [Deferred] that resolves to the sent Message
 */
fun Member.send(embed: MessageEmbed, delay: Duration = Duration.ZERO): Deferred<Message> =
        this.user.openPrivateChannel() flatMap { it.send(embed, delay) }
