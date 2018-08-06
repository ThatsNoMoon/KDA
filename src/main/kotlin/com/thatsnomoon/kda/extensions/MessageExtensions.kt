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
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.entities.Emote
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.MessageEmbed
import java.time.Duration

/**
 * Replies to this message.
 *
 * This simply sends a message to the [net.dv8tion.jda.core.entities.MessageChannel] that this message came from.
 *
 * @param text Text to send
 * @param delay [Duration] to wait before queueing this action
 *
 * @return [Deferred] that resolves to the sent message
 */
fun Message.reply(text: String, delay: Duration = Duration.ZERO): Deferred<Message> =
        this.channel.send(text, delay)

/**
 * Replies to this message.
 *
 * This simply sends a message to the [net.dv8tion.jda.core.entities.MessageChannel] that this message came from.
 *
 * @param message Message to send
 * @param delay [Duration] to wait before queueing this action
 *
 * @return [Deferred] that resolves to the sent message
 */
fun Message.reply(message: Message, delay: Duration = Duration.ZERO): Deferred<Message> =
        this.channel.send(message, delay)

/**
 * Replies to this message.
 *
 * This simply sends a message to the [net.dv8tion.jda.core.entities.MessageChannel] that this message came from.
 *
 * @param delay [Duration] to wait before queueing this action
 * @param init Function to "build" the message to send, i.e. set content and options
 *
 * @return [Deferred] that resolves to the sent message
 */
fun Message.reply(delay: Duration = Duration.ZERO, init: MessageBuilder.() -> Unit): Deferred<Message> =
        this.channel.send(delay, init)

/**
 * Replies to this message.
 *
 * This simply sends a message to the [net.dv8tion.jda.core.entities.MessageChannel] that this message came from.
 *
 * @param embed Embed to send
 * @param delay [Duration] to wait before queueing this action
 *
 * @return [Deferred] that resolves to the sent message
 */
fun Message.reply(embed: MessageEmbed, delay: Duration = Duration.ZERO): Deferred<Message> =
        this.channel.send(embed, delay)

/**
 * Edits this message.
 *
 * @param text New content for this message
 * @param delay [Duration] to wait before queueing this action
 *
 * @return [Deferred] that resolves to the new message
 */
fun Message.edit(text: String, delay: Duration = Duration.ZERO): Deferred<Message> =
        this.editMessage(text) after delay

/**
 * Edits this message.
 *
 * @param message New content for this message
 * @param delay [Duration] to wait before queueing this action
 *
 * @return [Deferred] that resolves to the new message
 */
fun Message.edit(message: Message, delay: Duration = Duration.ZERO): Deferred<Message> =
        this.editMessage(message) after delay

/**
 * Edits this message.
 *
 * @param init Function to "build" the new message, i.e. set content and options
 * @param delay [Duration] to wait before queueing this action
 *
 * @return [Deferred] that resolves to the new message
 */
fun Message.edit(delay: Duration = Duration.ZERO, init: MessageBuilder.() -> Unit): Deferred<Message> {
    val builder = MessageBuilder()
    builder.init()
    return this.edit(builder.build(), delay)
}

/**
 * Edits this message.
 *
 * @param embed New content for this message
 * @param delay [Duration] to wait before queueing this action
 *
 * @return [Deferred] that resolves to the new message
 */
fun Message.edit(embed: MessageEmbed, delay: Duration = Duration.ZERO): Deferred<Message> =
        this.editMessage(embed) after delay

/**
 * Deletes this message.
 *
 * @param delay [Duration] to wait before queueing this action
 *
 * @return Empty [Deferred] that resolves when the action is complete
 */
fun Message.delete(reason: String = "", delay: Duration = Duration.ZERO): Deferred<Unit> =
        this.delete().reason(reason) after delay map { Unit }

/**
 * Reacts to this message.
 *
 * @param unicode Unicode emoji to react with
 * @param delay [Duration] to wait before queueing this action
 *
 * @return Empty [Deferred] that resolves when the action is complete
 */
fun Message.react(unicode: String, delay: Duration = Duration.ZERO): Deferred<Unit> =
        this.addReaction(unicode) after delay map { Unit }

/**
 * Reacts to this message.
 *
 * @param emote Emote to react with
 * @param delay [Duration] to wait before queueing this action
 *
 * @return Empty [Deferred] that resolves when the action is complete
 */
fun Message.react(emote: Emote, delay: Duration = Duration.ZERO): Deferred<Unit> =
        this.addReaction(emote) after delay map { Unit }

/**
 * Deletes reactions from this message.
 *
 * @param delay [Duration] to wait before queueing this action
 *
 * @return Empty [Deferred] that resolves when the action is complete
 */
fun Message.deleteReactions(delay: Duration = Duration.ZERO): Deferred<Unit> =
        this.clearReactions() after delay map { Unit }
