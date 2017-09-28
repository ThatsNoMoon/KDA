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
import net.dv8tion.jda.core.entities.Channel

/**
 * Blocking function to create a copy of this Channel.
 *
 * Normal restrictions apply; you must have the proper permissions to create channels for this function not to throw.
 * @return A copy of this Channel.
 */
fun Channel.createCopy(): Channel {
    return this.guild.controller.createCopyOfChannel(this).complete()
}

/**
 * Asynchronous function to cerate a copy of this Channel.
 *
 * Normal restrictions apply; you must have the proper permissions to create channels for this function not to throw.
 * @return A KPromise that will resolve to a copy of this Channel.
 */
fun Channel.createCopyAsync(): KPromise<Channel> {
    return this.guild.controller.createCopyOfChannel(this).promisify()
}