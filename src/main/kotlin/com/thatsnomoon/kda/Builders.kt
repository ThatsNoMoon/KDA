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

package com.thatsnomoon.kda

import net.dv8tion.jda.core.*
import net.dv8tion.jda.core.entities.*

/**
 * Blocking convenience function to build a JDA instance with a JDABuilder.
 * @param accountType AccountType to use for this JDA instance.
 * @param init Function to call on a JDABuilder to configure the resulting JDA.
 * @return A JDA instance configured by the init function.
 */
inline fun buildClient(accountType: AccountType, init: JDABuilder.() -> Unit): JDA {
    val builder = JDABuilder(accountType)
    builder.init()
    return builder.buildBlocking()
}

/**
 * Asynchronous convenience function to build a JDA instance with a JDABuilder.
 * @param accountType AccountType to use for this JDA instance.
 * @param init Function to call on a JDABuilder to configure the resulting JDA.
 * @return A JDA instance configured by the init function after calling buildAsync() on a JDABuilder, meaning it may not be finished loading.
 */
inline fun buildClientAsync(accountType: AccountType, init: JDABuilder.() -> Unit): JDA {
    val builder = JDABuilder(accountType)
    builder.init()
    return builder.buildAsync()
}

/**
 * Convenience method to build a Message with a MessageBuilder.
 * @param init Function to call on a MessageBuilder to configure the resulting Message.
 * @return A Message configured by the init function.
 */
inline fun buildMessage(init: MessageBuilder.() -> Unit): Message {
    val builder = MessageBuilder()
    builder.init()
    return builder.build()
}

/**
 * Convenience method to build a MessageEmbed with an EmbedBuilder.
 * @param init Function to call on an EmbedBuilder to configure the resulting MessageEmbed.
 * @return A MessageEmbed configured by the init function.
 */
inline fun buildEmbed(init: EmbedBuilder.() -> Unit): MessageEmbed {
    val builder = EmbedBuilder()
    builder.init()
    return builder.build()
}