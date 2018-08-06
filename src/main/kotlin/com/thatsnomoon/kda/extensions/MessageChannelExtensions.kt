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
import net.dv8tion.jda.core.entities.Emote
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.MessageChannel
import net.dv8tion.jda.core.entities.MessageEmbed
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import java.io.File
import java.io.InputStream
import java.time.Duration

/**
 * Sends a message to this channel.
 *
 * @param text Message content to send
 * @param delay [Duration] to wait before queueing this action
 *
 * @return [Deferred] that resolves to the sent message
 */
fun MessageChannel.send(text: String, delay: Duration = Duration.ZERO): Deferred<Message> =
        this.sendMessage(text) after delay

/**
 * Sends a message to this channel.
 *
 * @param message Message to send
 * @param delay [Duration] to wait before queueing this action
 *
 * @return [Deferred] that resolves to the sent message
 */
fun MessageChannel.send(message: Message, delay: Duration = Duration.ZERO): Deferred<Message> =
        this.sendMessage(message) after delay

/**
 * Sends a message to this channel.
 *
 * @param delay [Duration] to wait before queueing this action
 * @param init Function to "build" the message to send, i.e. set content and options
 *
 * @return [Deferred] that resolves to the sent message
 */
inline fun MessageChannel.send(delay: Duration = Duration.ZERO, init: MessageBuilder.() -> Unit): Deferred<Message> {
    val builder = MessageBuilder()
    builder.init()
    return this.sendMessage(builder.build()) after delay
}

/**
 * Sends a message to this channel.
 *
 * @param embed Embed to send
 * @param delay [Duration] to wait before queueing this action
 *
 * @return [Deferred] that resolves to the sent message
 */
fun MessageChannel.send(embed: MessageEmbed, delay: Duration = Duration.ZERO): Deferred<Message> =
        this.sendMessage(MessageBuilder().setEmbed(embed).build()) after delay

/**
 * Sends a message with a file to this channel.
 *
 * @param data Data to send
 * @param filename Filename for discord to display
 * @param message Message to send with this file
 * @param delay [Duration] to wait before queueing this action
 *
 * @return [Deferred] that resolves to the sent message
 */
fun MessageChannel.sendWithFile(data: ByteArray, filename: String, message: Message? = null, delay: Duration = Duration.ZERO): Deferred<Message> =
        this.sendFile(data, filename, message) after delay

/**
 * Sends a message with a file to this channel.
 *
 * @param data Data to send
 * @param filename Filename for discord to display
 * @param delay [Duration] to wait before queueing this action
 * @param init Function to "build" the message to send, i.e. set content and options
 *
 * @return [Deferred] that resolves to the sent message
 */
inline fun MessageChannel.sendWithFile(data: ByteArray, filename: String, delay: Duration = Duration.ZERO, init: MessageBuilder.() -> Unit): Deferred<Message> {
    val builder = MessageBuilder()
    builder.init()
    return this.sendFile(data, filename, builder.build()) after delay
}

/**
 * Sends a message with a file to this channel.
 *
 * @param file File to send
 * @param filename Filename for discord to display
 * @param message Message to send with this file
 * @param delay [Duration] to wait before queueing this action
 *
 * @return [Deferred] that resolves to the sent message
 */
fun MessageChannel.sendWithFile(file: File, filename: String = file.name, message: Message? = null, delay: Duration = Duration.ZERO): Deferred<Message> =
        this.sendFile(file, filename, message) after delay

/**
 * Sends a message with a file to this channel.
 *
 * @param file File to send
 * @param filename Filename for discord to display
 * @param delay [Duration] to wait before queueing this action
 * @param init Function to "build" the message to send, i.e. set content and options
 *
 * @return [Deferred] that resolves to the sent message
 */
inline fun MessageChannel.sendWithFile(file: File, filename: String = file.name, delay: Duration = Duration.ZERO, init: MessageBuilder.() -> Unit): Deferred<Message> {
    val builder = MessageBuilder()
    builder.init()
    return this.sendFile(file, filename, builder.build()) after delay
}

/**
 * Sends a message with a file to this channel.
 *
 * @param data Data to send
 * @param filename Filename for discord to display
 * @param message Message to send with this file
 * @param delay [Duration] to wait before queueing this action
 *
 * @return [Deferred] that resolves to the sent message
 */
fun MessageChannel.sendWithFile(data: InputStream, filename: String, message: Message? = null, delay: Duration = Duration.ZERO): Deferred<Message> =
        this.sendFile(data, filename, message) after delay

/**
 * Sends a message with a file to this channel.
 *
 * @param data Data to send
 * @param filename Filename for discord to display
 * @param delay [Duration] to wait before queueing this action
 * @param init Function to "build" the message to send, i.e. set content and options
 *
 * @return [Deferred] that resolves to the sent message
 */
inline fun MessageChannel.sendWithFile(data: InputStream, filename: String, delay: Duration = Duration.ZERO, init: MessageBuilder.() -> Unit): Deferred<Message> {
    val builder = MessageBuilder()
    builder.init()
    return this.sendFile(data, filename, builder.build()) after delay
}

/**
 * Retrieves a message from this channel by its id.
 *
 * @param messageId ID of the message to retrieve
 * @param delay [Duration] to wait before queueing this action
 *
 * @return [Deferred] that resolves to the retrieved message
 */
fun MessageChannel.getMessage(messageId: Long, delay: Duration = Duration.ZERO): Deferred<Message> =
        this.getMessageById(messageId) after delay

/**
 * Retrieves a message from this channel by its id.
 *
 * @param messageId ID of the message to retrieve
 * @param delay [Duration] to wait before queueing this action
 *
 * @return [Deferred] that resolves to the retrieved message
 */
fun MessageChannel.getMessage(messageId: String, delay: Duration = Duration.ZERO): Deferred<Message> =
        this.getMessageById(messageId) after delay

/**
 * Adds a reaction to a message in this channel by its id.
 *
 * @param messageId ID of the message to react to
 * @param unicode Unicode emoji to react with
 * @param delay [Duration] to wait before queueing this action
 *
 * @return Empty [Deferred] that resolves when the action is complete
 */
fun MessageChannel.reactToMessage(messageId: Long, unicode: String, delay: Duration = Duration.ZERO): Deferred<Unit> =
        this.addReactionById(messageId, unicode) after delay map { Unit }

/**
 * Adds a reaction to a message in this channel by its id.
 *
 * @param messageId ID of the message to react to
 * @param unicode Unicode emoji to react with
 * @param delay [Duration] to wait before queueing this action
 *
 * @return Empty [Deferred] that resolves when the action is complete
 */
fun MessageChannel.reactToMessage(messageId: String, unicode: String, delay: Duration = Duration.ZERO): Deferred<Unit> =
        this.addReactionById(messageId, unicode) after delay map { Unit }

/**
 * Adds a reaction to a message in this channel by its id.
 *
 * @param messageId ID of the message to react to
 * @param emote Emote to react with
 * @param delay [Duration] to wait before queueing this action
 *
 * @return Empty [Deferred] that resolves when the action is complete
 */
fun MessageChannel.reactToMessage(messageId: Long, emote: Emote, delay: Duration = Duration.ZERO): Deferred<Unit> =
        this.addReactionById(messageId, emote) after delay map { Unit }

/**
 * Adds a reaction to a message in this channel by its id.
 *
 * @param messageId ID of the message to react to
 * @param emote Emote to react with
 * @param delay [Duration] to wait before queueing this action
 *
 * @return Empty [Deferred] that resolves when the action is complete
 */
fun MessageChannel.reactToMessage(messageId: String, emote: Emote, delay: Duration = Duration.ZERO): Deferred<Unit> =
        this.addReactionById(messageId, emote) after delay map { Unit }

/**
 * Removes a reaction from a message in this channel by its id.
 *
 * @param messageId ID of the message to remove a reaction from
 * @param unicode Emote to remove
 * @param delay [Duration] to wait before queueing this action
 *
 * @return Empty [Deferred] that resolves when the action is complete
 */
fun MessageChannel.deleteMessageReaction(messageId: Long, unicode: String, delay: Duration = Duration.ZERO): Deferred<Unit> =
        this.removeReactionById(messageId, unicode) after delay map { Unit }

/**
 * Removes a reaction from a message in this channel by its id.
 *
 * @param messageId ID of the message to remove a reaction from
 * @param unicode Emote to remove
 * @param delay [Duration] to wait before queueing this action
 *
 * @return Empty [Deferred] that resolves when the action is complete
 */
fun MessageChannel.deleteMessageReaction(messageId: String, unicode: String, delay: Duration = Duration.ZERO): Deferred<Unit> =
        this.removeReactionById(messageId, unicode) after delay map { Unit }

/**
 * Removes a reaction from a message in this channel by its id.
 *
 * @param messageId ID of the message to remove a reaction from
 * @param emote Emote to remove
 * @param delay [Duration] to wait before queueing this action
 *
 * @return Empty [Deferred] that resolves when the action is complete
 */
fun MessageChannel.deleteMessageReaction(messageId: Long, emote: Emote, delay: Duration = Duration.ZERO): Deferred<Unit> =
        this.removeReactionById(messageId, emote) after delay map { Unit }

/**
 * Removes a reaction from a message in this channel by its id.
 *
 * @param messageId ID of the message to remove a reaction from
 * @param emote Emote to remove
 * @param delay [Duration] to wait before queueing this action
 *
 * @return Empty [Deferred] that resolves when the action is complete
 */
fun MessageChannel.deleteMessageReaction(messageId: String, emote: Emote, delay: Duration = Duration.ZERO): Deferred<Unit> =
        this.removeReactionById(messageId, emote) after delay map { Unit }

/**
 * Deletes a message in this channel by its id.
 *
 * @param messageId ID of the message to delete
 * @param delay [Duration] to wait before queueing this action
 *
 * @return Empty [Deferred] that resolves when the action is complete
 */
fun MessageChannel.deleteMessage(messageId: Long, reason: String = "", delay: Duration = Duration.ZERO): Deferred<Unit> =
        this.deleteMessageById(messageId).reason(reason) after delay map { Unit }

/**
 * Deletes a message in this channel by its id.
 *
 * @param messageId ID of the message to delete
 * @param delay [Duration] to wait before queueing this action
 *
 * @return Empty [Deferred] that resolves when the action is complete
 */
fun MessageChannel.deleteMessage(messageId: String, reason: String = "", delay: Duration = Duration.ZERO): Deferred<Unit> =
        this.deleteMessageById(messageId).reason(reason) after delay map { Unit }

/**
 * Retrieves the pinned messages in this channel.
 *
 * @param delay [Duration] to wait before queueing this action
 *
 * @return [Deferred] that resolves to the pinned messages
 */
fun MessageChannel.getPins(delay: Duration = Duration.ZERO): Deferred<List<Message>> =
        this.pinnedMessages after delay

/**
 * Pins a message in this channel by its id.
 *
 * @param messageId ID of the message to pin
 * @param delay [Duration] to wait before queueing this action
 *
 * @return Empty [Deferred] that resolves when the action is complete
 */
fun MessageChannel.pinMessage(messageId: Long, delay: Duration = Duration.ZERO): Deferred<Unit> =
        this.pinMessageById(messageId) after delay map { Unit }

/**
 * Pins a message in this channel by its id.
 *
 * @param messageId ID of the message to pin
 * @param delay [Duration] to wait before queueing this action
 *
 * @return Empty [Deferred] that resolves when the action is complete
 */
fun MessageChannel.pinMessage(messageId: String, delay: Duration = Duration.ZERO): Deferred<Unit> =
        this.pinMessageById(messageId) after delay map { Unit }

/**
 * Unpins a message in this channel by its id.
 *
 * @param messageId ID of the message to unpin
 * @param delay [Duration] to wait before queueing this action
 *
 * @return Empty [Deferred] that resolves when the action is complete
 */
fun MessageChannel.unpinMessage(messageId: Long, delay: Duration = Duration.ZERO): Deferred<Unit> =
        this.unpinMessageById(messageId) after delay map { Unit }
/**
 * Unpins a message in this channel by its id.
 *
 * @param messageId ID of the message to unpin
 * @param delay [Duration] to wait before queueing this action
 *
 * @return Empty [Deferred] that resolves when the action is complete
 */
fun MessageChannel.unpinMessage(messageId: String, delay: Duration = Duration.ZERO): Deferred<Unit> =
        this.unpinMessageById(messageId) after delay map { Unit }
/**
 * Asynchronously await a message or multiple messages from this MessageChannel that match a given predicate before a timeout elapses.
 * @param count Number of messages to wait for
 * @param timeout [Duration] to wait before returning however many messages were collected, even if that amount is less than the provided count
 * @param predicate Predicate to predicate received events against. If this function returns true when passed the message, the message is added to the result to be returned (default: none, all messages are returned)
 *
 * @return [Deferred] that resolves to a possibly empty list of received messages that passed the predicate before the timeout elapsed.
 */
fun MessageChannel.awaitMessages(count: Int = 1, timeout: Duration? = null, predicate: (Message) -> Boolean = {true}): Deferred<List<Message>> {
    return this.jda.awaitEvents(MessageReceivedEvent::class.java, count, timeout) {
        it.channel.idLong == this@awaitMessages.idLong && predicate(it.message)
    } map { list -> list.map { event -> event.message } }
}