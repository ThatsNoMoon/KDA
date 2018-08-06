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

package com.thatsnomoon.kda

import net.dv8tion.jda.core.*
import net.dv8tion.jda.core.entities.*

/**
 * Builds a JDA instance using JDABuilder.
 *
 * This function blocks until building is complete. For asynchronous building, see [buildClient].
 *
 * @param accountType AccountType to use for this JDA instance.
 * @param init Block to "build" the JDA instance, i.e. set token and configure options
 *
 * @return A JDA instance configured by the init function.
 */
inline fun buildClientBlocking(accountType: AccountType, init: JDABuilder.() -> Unit): JDA {
    val builder = JDABuilder(accountType)
    builder.init()
    return builder.buildBlocking()
}

/**
 * Builds a JDA instance using JDABuilder.
 *
 * This function does not block until building is complete. For blocking building, see [buildClientBlocking].
 *
 * @param accountType AccountType to use for this JDA instance.
 * @param init Block to "build" the JDA instance, i.e. set token and configure options
 *
 * @return A JDA instance configured by the init function after calling buildAsync() on a JDABuilder, meaning it may not be finished loading.
 */
inline fun buildClient(accountType: AccountType, init: JDABuilder.() -> Unit): JDA {
    val builder = JDABuilder(accountType)
    builder.init()
    return builder.buildAsync()
}

/**
 * Builds a message, using a [MessageBuilder].
 *
 * @param init Block to "build" the message, i.e. set content and options
 *
 * @return A Message configured by the init function.
 */
inline fun buildMessage(init: MessageBuilder.() -> Unit): Message {
    val builder = MessageBuilder()
    builder.init()
    return builder.build()
}

/**
 * Builds a [MessageEmbed], using an [EmbedBuilder]
 *
 * @param init Block to "build" the embed, i.e. set fields and options
 *
 * @return A MessageEmbed configured by the init function.
 */
inline fun buildEmbed(init: EmbedBuilder.() -> Unit): MessageEmbed {
    val builder = EmbedBuilder()
    builder.init()
    return builder.build()
}