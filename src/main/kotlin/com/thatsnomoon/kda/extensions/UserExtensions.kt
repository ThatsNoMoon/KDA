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
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.entities.*

/**
 * Blocking function to open a private channel with this User.
 * @return The User's PrivateChannel.
 */
fun User.getPrivateChannel(): PrivateChannel {
    return openPrivateChannel().complete()
}

/**
 * Asynchronous function to open a private channel with this User.
 * @return A KPromise that will resolve to this User's PrivateChannel.
 */
fun User.getPrivateChannelAsync(): KPromise<PrivateChannel> {
    return openPrivateChannel().promisify()
}

/**
 * Blocking function to send a message to this User.
 * @param message Message to send to this User.
 * @return The sent Message.
 */
infix fun User.send(message: Message): Message {
    return sendMessageToUser(this, message)
}

/**
 * Asynchronous function to send a message to this User.
 * @param message Message to send.
 * @return KPromise resolving to the sent message.
 */
infix fun User.sendAsync(message: Message): KPromise<Message> {
    return sendMessageToUserAsync(this, message)
}

/**
 * Blocking function to send a message to this User.
 * @param init Function to call on a MessageBuilder to form a Message to send.
 * @return The sent Message.
 */
infix fun User.send(init: MessageBuilder.() -> Unit): Message {
    val builder = MessageBuilder()
    builder.init()
    return sendMessageToUser(this, builder.build())
}

/**
 * Asynchronous function to send a message to this User.
 * @param init Function to call on a MessageBuilder to form a Message to send.
 * @return KPromise resolving to the sent message.
 */
infix fun User.sendAsync(init: MessageBuilder.() -> Unit): KPromise<Message> {
    val builder = MessageBuilder()
    builder.init()
    return sendMessageToUserAsync(this, builder.build())
}

/**
 * Blocking function to send text to this User.
 * @param content Content of the Message to send.
 * @return The sent Message.
 */
infix fun User.sendText(content: String): Message {
    return sendMessageToUser(this, MessageBuilder().append(content).build())
}

/**
 * Asynchronous function to send text to this User.
 * @param content Content of the Message to send.
 * @return KPromise resolving to the sent message.
 */
infix fun User.sendTextAsync(content: String): KPromise<Message> {
    return sendMessageToUserAsync(this, MessageBuilder().append(content).build())
}

/**
 * Blocking function to send text to this User.
 * @param lazyContent Function to evaluate to the text to send.
 * @return The sent Message.
 */
infix fun User.sendText(lazyContent: () -> Any?): Message {
    return sendMessageToUser(this, MessageBuilder().append(lazyContent().toString()).build())
}

/**
 * Asynchronous function to send text to this User.
 * @param lazyContent Function to evaluate to the text to send.
 * @return KPromise resolving to the sent message.
 */
infix fun User.sendTextAsync(lazyContent: () -> Any?): KPromise<Message> {
    return sendMessageToUserAsync(this, MessageBuilder().append(lazyContent().toString()).build())
}

/**
 * Blocking function to send an embed to this User.
 * @param embed MessageEmbed to send.
 * @return The sent Message.
 */
infix fun User.sendEmbed(embed: MessageEmbed): Message {
    return sendMessageToUser(this, MessageBuilder().setEmbed(embed).build())
}

/**
 * Asynchronous function to send an embed to this User.
 * @param embed MessageEmbed to send.
 * @return KPromise resolving to the sent message.
 */
infix fun User.sendEmbedAsync(embed: MessageEmbed): KPromise<Message> {
    return sendMessageToUserAsync(this, MessageBuilder().setEmbed(embed).build())
}

/**
 * Blocking function to send an embed to this User.
 * @param init Function to call on an EmbedBuilder to form a MessageEmbed to send.
 * @return The sent Message.
 */
infix fun User.sendEmbed(init: EmbedBuilder.() -> Unit): Message {
    val builder = EmbedBuilder()
    builder.init()
    return sendMessageToUser(this, MessageBuilder().setEmbed(builder.build()).build())
}

/**
 * Asynchronous function to send an embed to this User.
 * @param init Function to call on an EmbedBuilder to form a MessageEmbed to send.
 * @return KPromise resolving to the sent message.
 */
infix fun User.sendEmbedAsync(init: EmbedBuilder.() -> Unit): KPromise<Message> {
    val builder = EmbedBuilder()
    builder.init()
    return sendMessageToUserAsync(this, MessageBuilder().setEmbed(builder.build()).build())
}

private fun sendMessageToUser(user: User, message: Message): Message {
    return user.openPrivateChannel().complete().sendMessage(message).complete()
}

private fun sendMessageToUserAsync(user: User, message: Message): KPromise<Message> {
    return promisify {
        val channelFuture = user.openPrivateChannel().submit()
        while (!channelFuture.isDone) {
            delay(10)
        }
        val channel = channelFuture.get()
        val msgFuture = channel.sendMessage(message).submit()
        while (!msgFuture.isDone) {
            delay(10)
        }
        msgFuture.get()
    }
}
