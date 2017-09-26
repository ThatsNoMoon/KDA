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
import net.dv8tion.jda.core.entities.Member

/**
 * Blocking function to ban this Member from their associated guild.
 * @param delDays Days to delete this Member's messages in.
 * @param reason Optional: reason to ban this Member, recorded in the Audit Log.
 */
fun Member.ban(delDays: Int = 0, reason: String? = null) {
    this.guild.controller.ban(this, delDays, reason).complete()
}

/**
 * Asynchronous function to ban this Member from their associated guild.
 * @param delDays Days to delete this Member's messages in.
 * @param reason Optional: reason to ban this Member, recorded in the Audit Log.
 * @return KPromise that resolves when this action is completed.
 */
fun Member.banAsync(delDays: Int = 0, reason: String? = null): KPromise<Void> {
    return this.guild.controller.ban(this, delDays, reason).promisify()
}

/**
 * Blocking function to kick this Member from their associated guild.
 * @param reason Optional: reason to kick this Member, recorded in the Audit Log.
 */
fun Member.kick(reason: String? = null) {
    this.guild.controller.kick(this, reason).complete()
}

/**
 * Asynchronous function to kick this Member from their associated guild.
 * @param reason Optional: reason to kick this Member, recorded in the Audit Log.
 * @return KPromise that resolves when this action is completed.
 */
fun Member.kickAsync(reason: String? = null): KPromise<Void> {
    return this.guild.controller.kick(this, reason).promisify()
}