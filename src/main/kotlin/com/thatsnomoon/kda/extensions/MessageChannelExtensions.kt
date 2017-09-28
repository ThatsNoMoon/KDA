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
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.MessageChannel
import net.dv8tion.jda.core.entities.MessageEmbed
import net.dv8tion.jda.core.events.message.MessageReceivedEvent

/**
 * Blocking function to send a message to this MessageChannel.
 * @param message Message to send to this MessageChannel.
 * @return The sent Message.
 */
infix fun MessageChannel.send(message: Message): Message {
    return sendMessage(message).complete()
}

/**
 * Asynchronous function to send a message to this MessageChannel.
 * @param message Message to send.
 * @return KPromise resolving to the sent message.
 */
infix fun MessageChannel.sendAsync(message: Message): KPromise<Message> {
    return sendMessage(message).promisify()
}

/**
 * Blocking function to send a message to this MessageChannel.
 * @param init Function to call on a MessageBuilder to form a Message to send.
 * @return The sent Message.
 */
inline infix fun MessageChannel.send(init: MessageBuilder.() -> Unit): Message {
    val builder = MessageBuilder()
    builder.init()
    return sendMessage(builder.build()).complete()
}

/**
 * Asynchronous function to send a message to this MessageChannel.
 * @param init Function to call on a MessageBuilder to form a Message to send.
 * @return KPromise resolving to the sent message.
 */
inline infix fun MessageChannel.sendAsync(init: MessageBuilder.() -> Unit): KPromise<Message> {
    val builder = MessageBuilder()
    builder.init()
    return sendMessage(builder.build()).promisify()
}

/**
 * Blocking function to send text to this MessageChannel.
 * @param content Content of the Message to send.
 * @return The sent Message.
 */
infix fun MessageChannel.sendText(content: String): Message {
    return sendMessage(content).complete()
}

/**
 * Asynchronous function to send text to this MessageChannel.
 * @param content Content of the Message to send.
 * @return KPromise resolving to the sent message.
 */
infix fun MessageChannel.sendTextAsync(content: String): KPromise<Message> {
    return sendMessage(content).promisify()
}

/**
 * Blocking function to send text to this MessageChannel.
 * @param lazyContent Function to evaluate to the text to send.
 * @return The sent Message.
 */
inline infix fun MessageChannel.sendText(lazyContent: () -> Any?): Message {
    return sendMessage(lazyContent().toString()).complete()
}

/**
 * Asynchronous function to send text to this MessageChannel.
 * @param lazyContent Function to evaluate to the text to send.
 * @return KPromise resolving to the sent message.
 */
inline infix fun MessageChannel.sendTextAsync(lazyContent: () -> Any?): KPromise<Message> {
    return sendMessage(lazyContent().toString()).promisify()
}

/**
 * Blocking function to send an embed to this MessageChannel.
 * @param embed MessageEmbed to send.
 * @return The sent Message.
 */
infix fun MessageChannel.sendEmbed(embed: MessageEmbed): Message {
    return sendMessage(MessageBuilder().setEmbed(embed).build()).complete()
}

/**
 * Asynchronous function to send an embed to this MessageChannel.
 * @param embed MessageEmbed to send.
 * @return KPromise resolving to the sent message.
 */
infix fun MessageChannel.sendEmbedAsync(embed: MessageEmbed): KPromise<Message> {
    return sendMessage(MessageBuilder().setEmbed(embed).build()).promisify()
}

/**
 * Blocking function to send an embed to this MessageChannel.
 * @param init Function to call on an EmbedBuilder to form a MessageEmbed to send.
 * @return The sent Message.
 */
inline infix fun MessageChannel.sendEmbed(init: EmbedBuilder.() -> Unit): Message {
    val builder = EmbedBuilder()
    builder.init()
    return sendMessage(MessageBuilder().setEmbed(builder.build()).build()).complete()
}

/**
 * Asynchronous function to send an embed to this MessageChannel.
 * @param init Function to call on an EmbedBuilder to form a MessageEmbed to send.
 * @return KPromise resolving to the sent message.
 */
inline infix fun MessageChannel.sendEmbedAsync(init: EmbedBuilder.() -> Unit): KPromise<Message> {
    val builder = EmbedBuilder()
    builder.init()
    return sendMessage(MessageBuilder().setEmbed(builder.build()).build()).promisify()
}

/**
 * Asynchronously await a message or multiple messages from this MessageChannel that match a given predicate before a timeout elapses.
 * @param count Number of messages to wait for.
 * @param timeoutMS Number of milliseconds to wait before returning however many events were received.
 * @param check Predicate to check the received event against.
 *
 * @return A KPromise that resolves to a non-null, possibly empty list of received messages that passed the check before the timeout elapsed.
 */
fun MessageChannel.awaitMessages(count: Int = 1, timeoutMS: Long = -1, check: (MessageReceivedEvent) -> Boolean = {true}): KPromise<List<MessageReceivedEvent>> {
    return this.jda.awaitEvents(MessageReceivedEvent::class.java, count, timeoutMS) {
        it.channel.idLong == this@awaitMessages.idLong && check(it)
    }
}