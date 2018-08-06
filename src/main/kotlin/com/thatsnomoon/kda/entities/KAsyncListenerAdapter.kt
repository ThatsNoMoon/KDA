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

package com.thatsnomoon.kda.entities

import net.dv8tion.jda.client.events.call.CallCreateEvent

import net.dv8tion.jda.client.events.call.CallDeleteEvent
import net.dv8tion.jda.client.events.call.GenericCallEvent
import net.dv8tion.jda.client.events.call.update.CallUpdateRegionEvent
import net.dv8tion.jda.client.events.call.update.CallUpdateRingingUsersEvent
import net.dv8tion.jda.client.events.call.update.GenericCallUpdateEvent
import net.dv8tion.jda.client.events.call.voice.*
import net.dv8tion.jda.client.events.group.*
import net.dv8tion.jda.client.events.group.update.GenericGroupUpdateEvent
import net.dv8tion.jda.client.events.group.update.GroupUpdateIconEvent
import net.dv8tion.jda.client.events.group.update.GroupUpdateNameEvent
import net.dv8tion.jda.client.events.group.update.GroupUpdateOwnerEvent
import net.dv8tion.jda.client.events.message.group.*
import net.dv8tion.jda.client.events.message.group.react.GenericGroupMessageReactionEvent
import net.dv8tion.jda.client.events.message.group.react.GroupMessageReactionAddEvent
import net.dv8tion.jda.client.events.message.group.react.GroupMessageReactionRemoveAllEvent
import net.dv8tion.jda.client.events.message.group.react.GroupMessageReactionRemoveEvent
import net.dv8tion.jda.client.events.relationship.*
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.events.*
import net.dv8tion.jda.core.events.channel.category.CategoryCreateEvent
import net.dv8tion.jda.core.events.channel.category.CategoryDeleteEvent
import net.dv8tion.jda.core.events.channel.category.GenericCategoryEvent
import net.dv8tion.jda.core.events.channel.category.update.CategoryUpdateNameEvent
import net.dv8tion.jda.core.events.channel.category.update.CategoryUpdatePermissionsEvent
import net.dv8tion.jda.core.events.channel.category.update.CategoryUpdatePositionEvent
import net.dv8tion.jda.core.events.channel.category.update.GenericCategoryUpdateEvent
import net.dv8tion.jda.core.events.channel.priv.PrivateChannelCreateEvent
import net.dv8tion.jda.core.events.channel.priv.PrivateChannelDeleteEvent
import net.dv8tion.jda.core.events.channel.text.GenericTextChannelEvent
import net.dv8tion.jda.core.events.channel.text.TextChannelCreateEvent
import net.dv8tion.jda.core.events.channel.text.TextChannelDeleteEvent
import net.dv8tion.jda.core.events.channel.text.update.*
import net.dv8tion.jda.core.events.channel.voice.GenericVoiceChannelEvent
import net.dv8tion.jda.core.events.channel.voice.VoiceChannelCreateEvent
import net.dv8tion.jda.core.events.channel.voice.VoiceChannelDeleteEvent
import net.dv8tion.jda.core.events.channel.voice.update.*
import net.dv8tion.jda.core.events.emote.EmoteAddedEvent
import net.dv8tion.jda.core.events.emote.EmoteRemovedEvent
import net.dv8tion.jda.core.events.emote.GenericEmoteEvent
import net.dv8tion.jda.core.events.emote.update.EmoteUpdateNameEvent
import net.dv8tion.jda.core.events.emote.update.EmoteUpdateRolesEvent
import net.dv8tion.jda.core.events.emote.update.GenericEmoteUpdateEvent
import net.dv8tion.jda.core.events.guild.*
import net.dv8tion.jda.core.events.guild.member.*
import net.dv8tion.jda.core.events.guild.update.*
import net.dv8tion.jda.core.events.guild.voice.*
import net.dv8tion.jda.core.events.http.HttpRequestEvent
import net.dv8tion.jda.core.events.message.*
import net.dv8tion.jda.core.events.message.guild.*
import net.dv8tion.jda.core.events.message.guild.react.GenericGuildMessageReactionEvent
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionRemoveAllEvent
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionRemoveEvent
import net.dv8tion.jda.core.events.message.priv.*
import net.dv8tion.jda.core.events.message.priv.react.GenericPrivateMessageReactionEvent
import net.dv8tion.jda.core.events.message.priv.react.PrivateMessageReactionAddEvent
import net.dv8tion.jda.core.events.message.priv.react.PrivateMessageReactionRemoveEvent
import net.dv8tion.jda.core.events.message.react.GenericMessageReactionEvent
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveAllEvent
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent
import net.dv8tion.jda.core.events.role.GenericRoleEvent
import net.dv8tion.jda.core.events.role.RoleCreateEvent
import net.dv8tion.jda.core.events.role.RoleDeleteEvent
import net.dv8tion.jda.core.events.role.update.*
import net.dv8tion.jda.core.events.self.*
import net.dv8tion.jda.core.events.user.GenericUserEvent
import net.dv8tion.jda.core.events.user.UserTypingEvent
import net.dv8tion.jda.core.events.user.update.*

/**
 * An abstract implementation of [KAsyncEventListener][KAsyncEventListener] which divides [Events][Event] for you.
 *
 * This is an exact translation of [ListenerAdapter][net.dv8tion.jda.core.hooks.ListenerAdapter] but using suspending functions.
 *
 *
 * **Example:**
 * ```
 * class MyListener: KListenerAdapter() {
 *     override suspend fun onReady(event: ReadyEvent) {
 *         println("Bot online!")
 *     }
 *
 *     override suspend fun onMessageReceived(event: MessageReceivedEvent) {
 *         println("Got message in channel ${event.channel.id}: ${event.message.contentDisplay}")
 *     }
 * }
 * ```
 *
 * @see KAsyncEventListener
 *
 * @see net.dv8tion.jda.core.hooks.ListenerAdapter
 *
 * @see net.dv8tion.jda.core.hooks.InterfacedEventManager
 */
abstract class KAsyncListenerAdapter : KAsyncEventListener {
    open suspend fun onGenericEvent(event: Event) {}
    open suspend fun onGenericUpdate(event: UpdateEvent<*, *>) {}

    open suspend fun onReady(event: ReadyEvent) {}

    open suspend fun onResume(event: ResumedEvent) {}
    open suspend fun onReconnect(event: ReconnectedEvent) {}
    open suspend fun onDisconnect(event: DisconnectEvent) {}
    open suspend fun onShutdown(event: ShutdownEvent) {}
    open suspend fun onStatusChange(event: StatusChangeEvent) {}
    open suspend fun onException(event: ExceptionEvent) {}

    open suspend fun onUserUpdateName(event: UserUpdateNameEvent) {}
    open suspend fun onUserUpdateDiscriminator(event: UserUpdateDiscriminatorEvent) {}
    open suspend fun onUserUpdateAvatar(event: UserUpdateAvatarEvent) {}
    open suspend fun onUserUpdateOnlineStatus(event: UserUpdateOnlineStatusEvent) {}
    open suspend fun onUserUpdateGame(event: UserUpdateGameEvent) {}
    open suspend fun onUserTyping(event: UserTypingEvent) {}

    open suspend fun onSelfUpdateAvatar(event: SelfUpdateAvatarEvent) {}
    open suspend fun onSelfUpdateEmail(event: SelfUpdateEmailEvent) {}
    open suspend fun onSelfUpdateMFA(event: SelfUpdateMFAEvent) {}
    open suspend fun onSelfUpdateName(event: SelfUpdateNameEvent) {}
    open suspend fun onSelfUpdateVerified(event: SelfUpdateVerifiedEvent) {}

    open suspend fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {}
    open suspend fun onGuildMessageUpdate(event: GuildMessageUpdateEvent) {}
    open suspend fun onGuildMessageDelete(event: GuildMessageDeleteEvent) {}
    open suspend fun onGuildMessageEmbed(event: GuildMessageEmbedEvent) {}
    open suspend fun onGuildMessageReactionAdd(event: GuildMessageReactionAddEvent) {}
    open suspend fun onGuildMessageReactionRemove(event: GuildMessageReactionRemoveEvent) {}
    open suspend fun onGuildMessageReactionRemoveAll(event: GuildMessageReactionRemoveAllEvent) {}

    open suspend fun onPrivateMessageReceived(event: PrivateMessageReceivedEvent) {}
    open suspend fun onPrivateMessageUpdate(event: PrivateMessageUpdateEvent) {}
    open suspend fun onPrivateMessageDelete(event: PrivateMessageDeleteEvent) {}
    open suspend fun onPrivateMessageEmbed(event: PrivateMessageEmbedEvent) {}
    open suspend fun onPrivateMessageReactionAdd(event: PrivateMessageReactionAddEvent) {}
    open suspend fun onPrivateMessageReactionRemove(event: PrivateMessageReactionRemoveEvent) {}

    open suspend fun onMessageReceived(event: MessageReceivedEvent) {}
    open suspend fun onMessageUpdate(event: MessageUpdateEvent) {}
    open suspend fun onMessageDelete(event: MessageDeleteEvent) {}
    open suspend fun onMessageBulkDelete(event: MessageBulkDeleteEvent) {}
    open suspend fun onMessageEmbed(event: MessageEmbedEvent) {}
    open suspend fun onMessageReactionAdd(event: MessageReactionAddEvent) {}
    open suspend fun onMessageReactionRemove(event: MessageReactionRemoveEvent) {}
    open suspend fun onMessageReactionRemoveAll(event: MessageReactionRemoveAllEvent) {}

    open suspend fun onTextChannelDelete(event: TextChannelDeleteEvent) {}
    open suspend fun onTextChannelUpdateName(event: TextChannelUpdateNameEvent) {}
    open suspend fun onTextChannelUpdateTopic(event: TextChannelUpdateTopicEvent) {}
    open suspend fun onTextChannelUpdatePosition(event: TextChannelUpdatePositionEvent) {}
    open suspend fun onTextChannelUpdatePermissions(event: TextChannelUpdatePermissionsEvent) {}
    open suspend fun onTextChannelUpdateNSFW(event: TextChannelUpdateNSFWEvent) {}
    open suspend fun onTextChannelUpdateParent(event: TextChannelUpdateParentEvent) {}
    open suspend fun onTextChannelCreate(event: TextChannelCreateEvent) {}

    open suspend fun onVoiceChannelDelete(event: VoiceChannelDeleteEvent) {}
    open suspend fun onVoiceChannelUpdateName(event: VoiceChannelUpdateNameEvent) {}
    open suspend fun onVoiceChannelUpdatePosition(event: VoiceChannelUpdatePositionEvent) {}
    open suspend fun onVoiceChannelUpdateUserLimit(event: VoiceChannelUpdateUserLimitEvent) {}
    open suspend fun onVoiceChannelUpdateBitrate(event: VoiceChannelUpdateBitrateEvent) {}
    open suspend fun onVoiceChannelUpdatePermissions(event: VoiceChannelUpdatePermissionsEvent) {}
    open suspend fun onVoiceChannelUpdateParent(event: VoiceChannelUpdateParentEvent) {}
    open suspend fun onVoiceChannelCreate(event: VoiceChannelCreateEvent) {}

    open suspend fun onCategoryDelete(event: CategoryDeleteEvent) {}
    open suspend fun onCategoryUpdateName(event: CategoryUpdateNameEvent) {}
    open suspend fun onCategoryUpdatePosition(event: CategoryUpdatePositionEvent) {}
    open suspend fun onCategoryUpdatePermissions(event: CategoryUpdatePermissionsEvent) {}
    open suspend fun onCategoryCreate(event: CategoryCreateEvent) {}

    open suspend fun onPrivateChannelCreate(event: PrivateChannelCreateEvent) {}
    open suspend fun onPrivateChannelDelete(event: PrivateChannelDeleteEvent) {}

    open suspend fun onGuildJoin(event: GuildJoinEvent) {}
    open suspend fun onGuildLeave(event: GuildLeaveEvent) {}
    open suspend fun onGuildAvailable(event: GuildAvailableEvent) {}
    open suspend fun onGuildUnavailable(event: GuildUnavailableEvent) {}
    open suspend fun onUnavailableGuildJoined(event: UnavailableGuildJoinedEvent) {}
    open suspend fun onGuildBan(event: GuildBanEvent) {}
    open suspend fun onGuildUnban(event: GuildUnbanEvent) {}
    open suspend fun onGuildUpdateAfkChannel(event: GuildUpdateAfkChannelEvent) {}
    open suspend fun onGuildUpdateSystemChannel(event: GuildUpdateSystemChannelEvent) {}
    open suspend fun onGuildUpdateAfkTimeout(event: GuildUpdateAfkTimeoutEvent) {}
    open suspend fun onGuildUpdateExplicitContentLevel(event: GuildUpdateExplicitContentLevelEvent) {}
    open suspend fun onGuildUpdateIcon(event: GuildUpdateIconEvent) {}
    open suspend fun onGuildUpdateMFALevel(event: GuildUpdateMFALevelEvent) {}
    open suspend fun onGuildUpdateName(event: GuildUpdateNameEvent) {}
    open suspend fun onGuildUpdateNotificationLevel(event: GuildUpdateNotificationLevelEvent) {}
    open suspend fun onGuildUpdateOwner(event: GuildUpdateOwnerEvent) {}
    open suspend fun onGuildUpdateRegion(event: GuildUpdateRegionEvent) {}
    open suspend fun onGuildUpdateSplash(event: GuildUpdateSplashEvent) {}
    open suspend fun onGuildUpdateVerificationLevel(event: GuildUpdateVerificationLevelEvent) {}
    open suspend fun onGuildUpdateFeatures(event: GuildUpdateFeaturesEvent) {}

    open suspend fun onGuildMemberJoin(event: GuildMemberJoinEvent) {}
    open suspend fun onGuildMemberLeave(event: GuildMemberLeaveEvent) {}
    open suspend fun onGuildMemberRoleAdd(event: GuildMemberRoleAddEvent) {}
    open suspend fun onGuildMemberRoleRemove(event: GuildMemberRoleRemoveEvent) {}
    open suspend fun onGuildMemberNickChange(event: GuildMemberNickChangeEvent) {}

    open suspend fun onGuildVoiceUpdate(event: GuildVoiceUpdateEvent) {}
    open suspend fun onGuildVoiceJoin(event: GuildVoiceJoinEvent) {}
    open suspend fun onGuildVoiceMove(event: GuildVoiceMoveEvent) {}
    open suspend fun onGuildVoiceLeave(event: GuildVoiceLeaveEvent) {}
    open suspend fun onGuildVoiceMute(event: GuildVoiceMuteEvent) {}
    open suspend fun onGuildVoiceDeafen(event: GuildVoiceDeafenEvent) {}
    open suspend fun onGuildVoiceGuildMute(event: GuildVoiceGuildMuteEvent) {}
    open suspend fun onGuildVoiceGuildDeafen(event: GuildVoiceGuildDeafenEvent) {}
    open suspend fun onGuildVoiceSelfMute(event: GuildVoiceSelfMuteEvent) {}
    open suspend fun onGuildVoiceSelfDeafen(event: GuildVoiceSelfDeafenEvent) {}
    open suspend fun onGuildVoiceSuppress(event: GuildVoiceSuppressEvent) {}

    open suspend fun onRoleCreate(event: RoleCreateEvent) {}
    open suspend fun onRoleDelete(event: RoleDeleteEvent) {}
    open suspend fun onRoleUpdateColor(event: RoleUpdateColorEvent) {}
    open suspend fun onRoleUpdateHoisted(event: RoleUpdateHoistedEvent) {}
    open suspend fun onRoleUpdateMentionable(event: RoleUpdateMentionableEvent) {}
    open suspend fun onRoleUpdateName(event: RoleUpdateNameEvent) {}
    open suspend fun onRoleUpdatePermissions(event: RoleUpdatePermissionsEvent) {}
    open suspend fun onRoleUpdatePosition(event: RoleUpdatePositionEvent) {}

    open suspend fun onEmoteAdded(event: EmoteAddedEvent) {}
    open suspend fun onEmoteRemoved(event: EmoteRemovedEvent) {}
    open suspend fun onEmoteUpdateName(event: EmoteUpdateNameEvent) {}
    open suspend fun onEmoteUpdateRoles(event: EmoteUpdateRolesEvent) {}

    open suspend fun onHttpRequest(event: HttpRequestEvent) {}

    open suspend fun onGenericMessage(event: GenericMessageEvent) {}
    open suspend fun onGenericMessageReaction(event: GenericMessageReactionEvent) {}
    open suspend fun onGenericGuildMessage(event: GenericGuildMessageEvent) {}
    open suspend fun onGenericGuildMessageReaction(event: GenericGuildMessageReactionEvent) {}
    open suspend fun onGenericPrivateMessage(event: GenericPrivateMessageEvent) {}
    open suspend fun onGenericPrivateMessageReaction(event: GenericPrivateMessageReactionEvent) {}
    open suspend fun onGenericUser(event: GenericUserEvent) {}
    open suspend fun onGenericUserPresence(event: GenericUserPresenceEvent<*>) {}
    open suspend fun onGenericSelfUpdate(event: GenericSelfUpdateEvent<*>) {}
    open suspend fun onGenericTextChannel(event: GenericTextChannelEvent) {}
    open suspend fun onGenericTextChannelUpdate(event: GenericTextChannelUpdateEvent<*>) {}
    open suspend fun onGenericVoiceChannel(event: GenericVoiceChannelEvent) {}
    open suspend fun onGenericVoiceChannelUpdate(event: GenericVoiceChannelUpdateEvent<*>) {}
    open suspend fun onGenericCategory(event: GenericCategoryEvent) {}
    open suspend fun onGenericCategoryUpdate(event: GenericCategoryUpdateEvent<*>) {}
    open suspend fun onGenericGuild(event: GenericGuildEvent) {}
    open suspend fun onGenericGuildUpdate(event: GenericGuildUpdateEvent<*>) {}
    open suspend fun onGenericGuildMember(event: GenericGuildMemberEvent) {}
    open suspend fun onGenericGuildVoice(event: GenericGuildVoiceEvent) {}
    open suspend fun onGenericRole(event: GenericRoleEvent) {}
    open suspend fun onGenericRoleUpdate(event: GenericRoleUpdateEvent<*>) {}
    open suspend fun onGenericEmote(event: GenericEmoteEvent) {}
    open suspend fun onGenericEmoteUpdate(event: GenericEmoteUpdateEvent<*>) {}

    open suspend fun onFriendAdded(event: FriendAddedEvent) {}
    open suspend fun onFriendRemoved(event: FriendRemovedEvent) {}
    open suspend fun onUserBlocked(event: UserBlockedEvent) {}
    open suspend fun onUserUnblocked(event: UserUnblockedEvent) {}
    open suspend fun onFriendRequestSent(event: FriendRequestSentEvent) {}
    open suspend fun onFriendRequestCanceled(event: FriendRequestCanceledEvent) {}
    open suspend fun onFriendRequestReceived(event: FriendRequestReceivedEvent) {}
    open suspend fun onFriendRequestIgnored(event: FriendRequestIgnoredEvent) {}

    open suspend fun onGroupJoin(event: GroupJoinEvent) {}
    open suspend fun onGroupLeave(event: GroupLeaveEvent) {}
    open suspend fun onGroupUserJoin(event: GroupUserJoinEvent) {}
    open suspend fun onGroupUserLeave(event: GroupUserLeaveEvent) {}
    open suspend fun onGroupMessageReceived(event: GroupMessageReceivedEvent) {}
    open suspend fun onGroupMessageUpdate(event: GroupMessageUpdateEvent) {}
    open suspend fun onGroupMessageDelete(event: GroupMessageDeleteEvent) {}
    open suspend fun onGroupMessageEmbed(event: GroupMessageEmbedEvent) {}
    open suspend fun onGroupMessageReactionAdd(event: GroupMessageReactionAddEvent) {}
    open suspend fun onGroupMessageReactionRemove(event: GroupMessageReactionRemoveEvent) {}
    open suspend fun onGroupMessageReactionRemoveAll(event: GroupMessageReactionRemoveAllEvent) {}
    open suspend fun onGroupUpdateIcon(event: GroupUpdateIconEvent) {}
    open suspend fun onGroupUpdateName(event: GroupUpdateNameEvent) {}
    open suspend fun onGroupUpdateOwner(event: GroupUpdateOwnerEvent) {}

    open suspend fun onCallCreate(event: CallCreateEvent) {}
    open suspend fun onCallDelete(event: CallDeleteEvent) {}
    open suspend fun onCallUpdateRegion(event: CallUpdateRegionEvent) {}
    open suspend fun onCallUpdateRingingUsers(event: CallUpdateRingingUsersEvent) {}
    open suspend fun onCallVoiceJoin(event: CallVoiceJoinEvent) {}
    open suspend fun onCallVoiceLeave(event: CallVoiceLeaveEvent) {}
    open suspend fun onCallVoiceSelfMute(event: CallVoiceSelfMuteEvent) {}
    open suspend fun onCallVoiceSelfDeafen(event: CallVoiceSelfDeafenEvent) {}

    open suspend fun onGenericRelationship(event: GenericRelationshipEvent) {}
    open suspend fun onGenericRelationshipAdd(event: GenericRelationshipAddEvent) {}
    open suspend fun onGenericRelationshipRemove(event: GenericRelationshipRemoveEvent) {}
    open suspend fun onGenericGroup(event: GenericGroupEvent) {}
    open suspend fun onGenericGroupMessage(event: GenericGroupMessageEvent) {}
    open suspend fun onGenericGroupMessageReaction(event: GenericGroupMessageReactionEvent) {}
    open suspend fun onGenericGroupUpdate(event: GenericGroupUpdateEvent) {}
    open suspend fun onGenericCall(event: GenericCallEvent) {}
    open suspend fun onGenericCallUpdate(event: GenericCallUpdateEvent) {}
    open suspend fun onGenericCallVoice(event: GenericCallVoiceEvent) {}

    override suspend fun onEventAsync(event: Event) {
        onGenericEvent(event)
        when (event) {
            is UpdateEvent<*, *> -> onGenericUpdate(event as UpdateEvent<*, *>)
        }
        when (event) {
            is ReadyEvent -> onReady(event)
            is ResumedEvent -> onResume(event)
            is ReconnectedEvent -> onReconnect(event)
            is DisconnectEvent -> onDisconnect(event)
            is ShutdownEvent -> onShutdown(event)
            is StatusChangeEvent -> onStatusChange(event)
            is ExceptionEvent -> onException(event)
            is GuildMessageReceivedEvent -> onGuildMessageReceived(event)
            is GuildMessageUpdateEvent -> onGuildMessageUpdate(event)
            is GuildMessageDeleteEvent -> onGuildMessageDelete(event)
            is GuildMessageEmbedEvent -> onGuildMessageEmbed(event)
            is GuildMessageReactionAddEvent -> onGuildMessageReactionAdd(event)
            is GuildMessageReactionRemoveEvent -> onGuildMessageReactionRemove(event)
            is GuildMessageReactionRemoveAllEvent -> onGuildMessageReactionRemoveAll(event)
            is PrivateMessageReceivedEvent -> onPrivateMessageReceived(event)
            is PrivateMessageUpdateEvent -> onPrivateMessageUpdate(event)
            is PrivateMessageDeleteEvent -> onPrivateMessageDelete(event)
            is PrivateMessageEmbedEvent -> onPrivateMessageEmbed(event)
            is PrivateMessageReactionAddEvent -> onPrivateMessageReactionAdd(event)
            is PrivateMessageReactionRemoveEvent -> onPrivateMessageReactionRemove(event)
            is MessageReceivedEvent -> onMessageReceived(event)
            is MessageUpdateEvent -> onMessageUpdate(event)
            is MessageDeleteEvent -> onMessageDelete(event)
            is MessageBulkDeleteEvent -> onMessageBulkDelete(event)
            is MessageEmbedEvent -> onMessageEmbed(event)
            is MessageReactionAddEvent -> onMessageReactionAdd(event)
            is MessageReactionRemoveEvent -> onMessageReactionRemove(event)
            is MessageReactionRemoveAllEvent -> onMessageReactionRemoveAll(event)
            is UserUpdateNameEvent -> onUserUpdateName(event)
            is UserUpdateDiscriminatorEvent -> onUserUpdateDiscriminator(event)
            is UserUpdateAvatarEvent -> onUserUpdateAvatar(event)
            is UserUpdateGameEvent -> onUserUpdateGame(event)
            is UserUpdateOnlineStatusEvent -> onUserUpdateOnlineStatus(event)
            is UserTypingEvent -> onUserTyping(event)
            is SelfUpdateAvatarEvent -> onSelfUpdateAvatar(event)
            is SelfUpdateEmailEvent -> onSelfUpdateEmail(event)
            is SelfUpdateMFAEvent -> onSelfUpdateMFA(event)
            is SelfUpdateNameEvent -> onSelfUpdateName(event)
            is SelfUpdateVerifiedEvent -> onSelfUpdateVerified(event)
            is TextChannelCreateEvent -> onTextChannelCreate(event)
            is TextChannelUpdateNameEvent -> onTextChannelUpdateName(event)
            is TextChannelUpdateTopicEvent -> onTextChannelUpdateTopic(event)
            is TextChannelUpdatePositionEvent -> onTextChannelUpdatePosition(event)
            is TextChannelUpdatePermissionsEvent -> onTextChannelUpdatePermissions(event)
            is TextChannelUpdateNSFWEvent -> onTextChannelUpdateNSFW(event)
            is TextChannelUpdateParentEvent -> onTextChannelUpdateParent(event)
            is TextChannelDeleteEvent -> onTextChannelDelete(event)
            is VoiceChannelCreateEvent -> onVoiceChannelCreate(event)
            is VoiceChannelUpdateNameEvent -> onVoiceChannelUpdateName(event)
            is VoiceChannelUpdatePositionEvent -> onVoiceChannelUpdatePosition(event)
            is VoiceChannelUpdateUserLimitEvent -> onVoiceChannelUpdateUserLimit(event)
            is VoiceChannelUpdateBitrateEvent -> onVoiceChannelUpdateBitrate(event)
            is VoiceChannelUpdatePermissionsEvent -> onVoiceChannelUpdatePermissions(event)
            is VoiceChannelUpdateParentEvent -> onVoiceChannelUpdateParent(event)
            is VoiceChannelDeleteEvent -> onVoiceChannelDelete(event)
            is CategoryCreateEvent -> onCategoryCreate(event)
            is CategoryUpdateNameEvent -> onCategoryUpdateName(event)
            is CategoryUpdatePositionEvent -> onCategoryUpdatePosition(event)
            is CategoryUpdatePermissionsEvent -> onCategoryUpdatePermissions(event)
            is CategoryDeleteEvent -> onCategoryDelete(event)
            is PrivateChannelCreateEvent -> onPrivateChannelCreate(event)
            is PrivateChannelDeleteEvent -> onPrivateChannelDelete(event)
            is GuildJoinEvent -> onGuildJoin(event)
            is GuildLeaveEvent -> onGuildLeave(event)
            is GuildAvailableEvent -> onGuildAvailable(event)
            is GuildUnavailableEvent -> onGuildUnavailable(event)
            is UnavailableGuildJoinedEvent -> onUnavailableGuildJoined(event)
            is GuildBanEvent -> onGuildBan(event)
            is GuildUnbanEvent -> onGuildUnban(event)
            is GuildUpdateAfkChannelEvent -> onGuildUpdateAfkChannel(event)
            is GuildUpdateSystemChannelEvent -> onGuildUpdateSystemChannel(event)
            is GuildUpdateAfkTimeoutEvent -> onGuildUpdateAfkTimeout(event)
            is GuildUpdateExplicitContentLevelEvent -> onGuildUpdateExplicitContentLevel(event)
            is GuildUpdateIconEvent -> onGuildUpdateIcon(event)
            is GuildUpdateMFALevelEvent -> onGuildUpdateMFALevel(event)
            is GuildUpdateNameEvent -> onGuildUpdateName(event)
            is GuildUpdateNotificationLevelEvent -> onGuildUpdateNotificationLevel(event)
            is GuildUpdateOwnerEvent -> onGuildUpdateOwner(event)
            is GuildUpdateRegionEvent -> onGuildUpdateRegion(event)
            is GuildUpdateSplashEvent -> onGuildUpdateSplash(event)
            is GuildUpdateVerificationLevelEvent -> onGuildUpdateVerificationLevel(event)
            is GuildUpdateFeaturesEvent -> onGuildUpdateFeatures(event)
            is GuildMemberJoinEvent -> onGuildMemberJoin(event)
            is GuildMemberLeaveEvent -> onGuildMemberLeave(event)
            is GuildMemberRoleAddEvent -> onGuildMemberRoleAdd(event)
            is GuildMemberRoleRemoveEvent -> onGuildMemberRoleRemove(event)
            is GuildMemberNickChangeEvent -> onGuildMemberNickChange(event)
            is GuildVoiceJoinEvent -> onGuildVoiceJoin(event)
            is GuildVoiceMoveEvent -> onGuildVoiceMove(event)
            is GuildVoiceLeaveEvent -> onGuildVoiceLeave(event)
            is GuildVoiceMuteEvent -> onGuildVoiceMute(event)
            is GuildVoiceDeafenEvent -> onGuildVoiceDeafen(event)
            is GuildVoiceGuildMuteEvent -> onGuildVoiceGuildMute(event)
            is GuildVoiceGuildDeafenEvent -> onGuildVoiceGuildDeafen(event)
            is GuildVoiceSelfMuteEvent -> onGuildVoiceSelfMute(event)
            is GuildVoiceSelfDeafenEvent -> onGuildVoiceSelfDeafen(event)
            is GuildVoiceSuppressEvent -> onGuildVoiceSuppress(event)
            is RoleCreateEvent -> onRoleCreate(event)
            is RoleDeleteEvent -> onRoleDelete(event)
            is RoleUpdateColorEvent -> onRoleUpdateColor(event)
            is RoleUpdateHoistedEvent -> onRoleUpdateHoisted(event)
            is RoleUpdateMentionableEvent -> onRoleUpdateMentionable(event)
            is RoleUpdateNameEvent -> onRoleUpdateName(event)
            is RoleUpdatePermissionsEvent -> onRoleUpdatePermissions(event)
            is RoleUpdatePositionEvent -> onRoleUpdatePosition(event)
            is EmoteAddedEvent -> onEmoteAdded(event)
            is EmoteRemovedEvent -> onEmoteRemoved(event)
            is EmoteUpdateNameEvent -> onEmoteUpdateName(event)
            is EmoteUpdateRolesEvent -> onEmoteUpdateRoles(event)
            is HttpRequestEvent -> onHttpRequest(event)
        }

        when (event) {
            is GuildVoiceUpdateEvent -> onGuildVoiceUpdate(event)
        }

        when (event) {
            is GenericMessageReactionEvent -> onGenericMessageReaction(event)
            is GenericPrivateMessageReactionEvent -> onGenericPrivateMessageReaction(event)
            is GenericTextChannelUpdateEvent<*> -> onGenericTextChannelUpdate(event)
            is GenericCategoryUpdateEvent<*> -> onGenericCategoryUpdate(event)
            is GenericGuildMessageReactionEvent -> onGenericGuildMessageReaction(event)
            is GenericVoiceChannelUpdateEvent<*> -> onGenericVoiceChannelUpdate(event)
            is GenericGuildUpdateEvent<*> -> onGenericGuildUpdate(event)
            is GenericGuildMemberEvent -> onGenericGuildMember(event)
            is GenericGuildVoiceEvent -> onGenericGuildVoice(event)
            is GenericRoleUpdateEvent<*> -> onGenericRoleUpdate(event)
            is GenericEmoteUpdateEvent<*> -> onGenericEmoteUpdate(event)
            is GenericUserPresenceEvent<*> -> onGenericUserPresence(event)
        }

        when (event) {
            is GenericMessageEvent -> onGenericMessage(event)
            is GenericPrivateMessageEvent -> onGenericPrivateMessage(event)
            is GenericGuildMessageEvent -> onGenericGuildMessage(event)
            is GenericUserEvent -> onGenericUser(event)
            is GenericSelfUpdateEvent<*> -> onGenericSelfUpdate(event)
            is GenericTextChannelEvent -> onGenericTextChannel(event)
            is GenericVoiceChannelEvent -> onGenericVoiceChannel(event)
            is GenericCategoryEvent -> onGenericCategory(event)
            is GenericRoleEvent -> onGenericRole(event)
            is GenericEmoteEvent -> onGenericEmote(event)
        }

        when (event) {
            is GenericGuildEvent -> onGenericGuild(event)
        }

        if (event.jda.accountType == AccountType.CLIENT) {
            when (event) {
                is FriendAddedEvent -> onFriendAdded(event)
                is FriendRemovedEvent -> onFriendRemoved(event)
                is UserBlockedEvent -> onUserBlocked(event)
                is UserUnblockedEvent -> onUserUnblocked(event)
                is FriendRequestSentEvent -> onFriendRequestSent(event)
                is FriendRequestCanceledEvent -> onFriendRequestCanceled(event)
                is FriendRequestReceivedEvent -> onFriendRequestReceived(event)
                is FriendRequestIgnoredEvent -> onFriendRequestIgnored(event)
                is GroupJoinEvent -> onGroupJoin(event)
                is GroupLeaveEvent -> onGroupLeave(event)
                is GroupUserJoinEvent -> onGroupUserJoin(event)
                is GroupUserLeaveEvent -> onGroupUserLeave(event)
            }

            when (event) {
                is GroupMessageReceivedEvent -> onGroupMessageReceived(event)
                is GroupMessageUpdateEvent -> onGroupMessageUpdate(event)
                is GroupMessageDeleteEvent -> onGroupMessageDelete(event)
                is GroupMessageEmbedEvent -> onGroupMessageEmbed(event)
                is GroupMessageReactionAddEvent -> onGroupMessageReactionAdd(event)
                is GroupMessageReactionRemoveEvent -> onGroupMessageReactionRemove(event)
                is GroupMessageReactionRemoveAllEvent -> onGroupMessageReactionRemoveAll(event)
                is GroupUpdateIconEvent -> onGroupUpdateIcon(event)
                is GroupUpdateNameEvent -> onGroupUpdateName(event)
                is GroupUpdateOwnerEvent -> onGroupUpdateOwner(event)
                is CallCreateEvent -> onCallCreate(event)
                is CallDeleteEvent -> onCallDelete(event)
                is CallUpdateRegionEvent -> onCallUpdateRegion(event)
                is CallUpdateRingingUsersEvent -> onCallUpdateRingingUsers(event)
                is CallVoiceJoinEvent -> onCallVoiceJoin(event)
                is CallVoiceLeaveEvent -> onCallVoiceLeave(event)
                is CallVoiceSelfMuteEvent -> onCallVoiceSelfMute(event)
                is CallVoiceSelfDeafenEvent -> onCallVoiceSelfDeafen(event)
            }

            when (event) {
                is GenericRelationshipAddEvent -> onGenericRelationshipAdd(event)
                is GenericRelationshipRemoveEvent -> onGenericRelationshipRemove(event)
                is GenericGroupMessageReactionEvent -> onGenericGroupMessageReaction(event)
                is GenericGroupUpdateEvent -> onGenericGroupUpdate(event)
                is GenericCallUpdateEvent -> onGenericCallUpdate(event)
                is GenericCallVoiceEvent -> onGenericCallVoice(event)

            }
            when (event) {
                is GenericGroupMessageEvent -> onGenericGroupMessage(event)
            }

            when (event) {
                is GenericRelationshipEvent -> onGenericRelationship(event)
                is GenericGroupEvent -> onGenericGroup(event)
                is GenericCallEvent -> onGenericCall(event)
            }
        }
    }
}
