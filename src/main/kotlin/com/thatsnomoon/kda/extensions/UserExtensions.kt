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
import net.dv8tion.jda.core.entities.*
import java.io.File
import java.io.InputStream
import java.time.Duration

/**
 * Opens a private channel with this User.
 *
 * @param delay [Duration] to wait before queueing this action
 *
 * @return A [Deferred] that resolves to the User's [PrivateChannel]
 */
fun User.getPrivateChannel(delay: Duration = Duration.ZERO): Deferred<PrivateChannel> =
        this.openPrivateChannel() after delay

/**
 * Sends a message to this User.
 *
 * @param message Message to send to this User.
 * @param delay [Duration] to wait before queueing this action
 *
 * @return A [Deferred] that resolves to the sent Message
 */
fun User.send(message: Message, delay: Duration = Duration.ZERO): Deferred<Message> =
        this.getPrivateChannel() flatMap { it.send(message, delay) }

/**
 * Sends a message to this User, using a MessageBuilder to build the message.
 *
 * @param init Function to call on a MessageBuilder to form a Message to send.
 * @param delay [Duration] to wait before queueing this action
 *
 * @return A [Deferred] that resolves to the sent Message
 */
inline fun User.send(delay: Duration = Duration.ZERO, init: MessageBuilder.() -> Unit): Deferred<Message> {
    val builder = MessageBuilder()
    builder.init()
    return this.getPrivateChannel() flatMap { it.send(builder.build(), delay) }
}

/**
 * Sends a message to this User.
 *
 * @param text Content of the Message to send.
 * @param delay [Duration] to wait before queueing this action
 *
 * @return A [Deferred] that resolves to the sent Message
 */
fun User.send(text: String, delay: Duration = Duration.ZERO): Deferred<Message> =
        this.getPrivateChannel() flatMap { it.send(text, delay) }

/**
 * Sends an embed to this User.
 *
 * @param embed MessageEmbed to send
 * @param delay [Duration] to wait before queueing this action
 *
 * @return A [Deferred] that resolves to the sent Message
 */
fun User.send(embed: MessageEmbed, delay: Duration = Duration.ZERO): Deferred<Message> =
        this.getPrivateChannel() flatMap { it.send(embed, delay) }

/**
 * Sends a message with a file to this User.
 *
 * @param data File to send
 * @param filename Name of the file to send
 * @param message Message to send with this file
 * @param delay [Duration] to wait before queueing this action
 *
 * @return A [Deferred] that resolves to the sent Message
 */
fun User.sendWithFile(data: ByteArray, filename: String, message: Message? = null, delay: Duration = Duration.ZERO): Deferred<Message> =
        this.getPrivateChannel() flatMap { it.sendWithFile(data, filename, message, delay) }

/**
 * Sends a message with a file to this User.
 *
 * @param data File to send
 * @param filename Name of the file to send
 * @param init Function to "build" the message to send, i.e. set content and options
 * @param delay [Duration] to wait before queueing this action
 *
 * @return A [Deferred] that resolves to the sent Message
 */
fun User.sendWithFile(data: ByteArray, filename: String, delay: Duration = Duration.ZERO, init: MessageBuilder.() -> Unit): Deferred<Message> {
    val builder = MessageBuilder()
    builder.init()
    return this.getPrivateChannel() flatMap { it.sendWithFile(data, filename, builder.build(), delay) }
}

/**
 * Sends a message with a file to this User.
 *
 * @param file File to send
 * @param filename Name of the file to send (default: [file].name)
 * @param message Message to send with this file
 * @param delay [Duration] to wait before queueing this action
 *
 * @return A [Deferred] that resolves to the sent Message
 */
fun User.sendWithFile(file: File, filename: String = file.name, message: Message? = null, delay: Duration = Duration.ZERO): Deferred<Message> =
        this.getPrivateChannel() flatMap { it.sendWithFile(file, filename, message, delay) }

/**
 * Sends a message with a file to this User.
 *
 * @param file File to send
 * @param filename Name of the file to send (default: [file].name)
 * @param init Function to "build" the message to send, i.e. set content and options
 * @param delay [Duration] to wait before queueing this action
 *
 * @return A [Deferred] that resolves to the sent Message
 */
fun User.sendWithFile(file: File, filename: String = file.name, delay: Duration = Duration.ZERO, init: MessageBuilder.() -> Unit): Deferred<Message> {
    val builder = MessageBuilder()
    builder.init()
    return this.getPrivateChannel() flatMap { it.sendWithFile(file, filename, builder.build(), delay) }
}

/**
 * Sends a message with a file to this User.
 *
 * @param data File to send
 * @param filename Name of the file to send
 * @param message Message to send with this file
 * @param delay [Duration] to wait before queueing this action
 *
 * @return A [Deferred] that resolves to the sent Message
 */
fun User.sendWithFile(data: InputStream, filename: String, message: Message? = null, delay: Duration = Duration.ZERO): Deferred<Message> =
        this.getPrivateChannel() flatMap { it.sendWithFile(data, filename, message, delay) }

/**
 * Sends a message with a file to this User.
 *
 * @param data File to send
 * @param filename Name of the file to send
 * @param init Function to "build" the message to send, i.e. set content and options
 * @param delay [Duration] to wait before queueing this action
 *
 * @return A [Deferred] that resolves to the sent Message
 */
fun User.sendWithFile(data: InputStream, filename: String, delay: Duration = Duration.ZERO, init: MessageBuilder.() -> Unit): Deferred<Message> {
    val builder = MessageBuilder()
    builder.init()
    return this.getPrivateChannel() flatMap { it.sendWithFile(data, filename, builder.build(), delay) }
}

