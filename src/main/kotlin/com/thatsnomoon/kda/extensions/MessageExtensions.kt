package com.thatsnomoon.kda.extensions

import com.thatsnomoon.kda.entities.KPromise
import net.dv8tion.jda.core.entities.Message
import java.util.concurrent.TimeUnit

/**
 * Blocking function to send text to this MessageChannel.
 * @param lazyContent Function to evaluate to the text to send, optionally using the Message being replied to.
 * @return The sent Message.
 */
infix inline fun Message.reply(lazyContent: (Message) -> Any?): Message {
    return this.channel.sendMessage(lazyContent(this).toString()).complete()
}

/**
 * Asynchronous function to send text to this MessageChannel.
 * @param lazyContent Function to evaluate to the text to send, optionally using the Message being replied to.
 * @return A KPromise resolving to the sent Message.
 */
infix inline fun Message.replyAsync(lazyContent: (Message) -> Any?): KPromise<Message>? {
    val action = this.channel.sendMessage(lazyContent(this).toString())
    return action.promisify()
}

/**
 * Asynchronous function to send text to this MessageChannel.
 * @param delay Time to wait before replying to this Message.
 * @param unit TimeUnit to use for delay. Default is milliseconds.
 * @param lazyContent Function to evaluate to the text to send, optionally using the Message being replied to.
 * @return A KPromise resolving to the sent Message.
 */
inline fun Message.replyAfterAsync(delay: Long = 1, unit: TimeUnit = TimeUnit.MILLISECONDS, lazyContent: (Message) -> Any?): KPromise<Message> {
    return this.channel.sendMessage(lazyContent(this).toString()).promisifyAfter(delay, unit)
}

/**
 * Asynchronous function to delete this message.
 * @return KPromise that resolves when this action is completed.
 */
fun Message.deleteAsync(): KPromise<Void> {
    return this.delete().promisify()
}

/**
 * Asynchronous function to delete this message after a delay.
 * @param delay Time to wait before deleting this message.
 * @param unit TimeUnit to use for delay. Default is milliseconds.
 * @return KPromise that resolves when this action is completed.
 */
fun Message.deleteAfterAsync(delay: Long, unit: TimeUnit = TimeUnit.MILLISECONDS): KPromise<Void> {
    return this.delete().promisifyAfter(delay, unit)
}

/**
 * Blocking function to delete this message after a delay.
 * @param delay Time to wait before deleting this message.
 * @param unit TimeUnit to use for delay. Default is milliseconds.
 */
fun Message.deleteAfter(delay: Long, unit: TimeUnit = TimeUnit.MILLISECONDS) {
    this.delete().completeAfter(delay, unit)
}