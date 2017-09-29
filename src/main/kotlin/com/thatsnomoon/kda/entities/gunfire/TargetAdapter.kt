/*
 *     Copyright 2015-2017 Austin Keener & Michael Ritter & Florian Spieß
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

package com.thatsnomoon.kda.entities.gunfire

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
import net.dv8tion.jda.core.events.message.priv.react.PrivateMessageReactionRemoveAllEvent
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
import net.dv8tion.jda.core.events.user.*

open class TargetAdapter: GunfireTarget {
    
    override fun invoke(event: EventBullet<Event>) {
        onGenericEvent(event)
        when (event) {
            is ReadyEvent -> onReady(EventBullet(event))
            is ResumedEvent -> onResume(EventBullet(event))
            is ReconnectedEvent -> onReconnect(EventBullet(event))
            is DisconnectEvent -> onDisconnect(EventBullet(event))
            is ShutdownEvent -> onShutdown(EventBullet(event))
            is StatusChangeEvent -> onStatusChange(EventBullet(event))
            is ExceptionEvent -> onException(EventBullet(event))
            is GuildMessageReceivedEvent -> onGuildMessageReceived(EventBullet(event))
            is GuildMessageUpdateEvent -> onGuildMessageUpdate(EventBullet(event))
            is GuildMessageDeleteEvent -> onGuildMessageDelete(EventBullet(event))
            is GuildMessageEmbedEvent -> onGuildMessageEmbed(EventBullet(event))
            is GuildMessageReactionAddEvent -> onGuildMessageReactionAdd(EventBullet(event))
            is GuildMessageReactionRemoveEvent -> onGuildMessageReactionRemove(EventBullet(event))
            is GuildMessageReactionRemoveAllEvent -> onGuildMessageReactionRemoveAll(EventBullet(event))
            is PrivateMessageReceivedEvent -> onPrivateMessageReceived(EventBullet(event))
            is PrivateMessageUpdateEvent -> onPrivateMessageUpdate(EventBullet(event))
            is PrivateMessageDeleteEvent -> onPrivateMessageDelete(EventBullet(event))
            is PrivateMessageEmbedEvent -> onPrivateMessageEmbed(EventBullet(event))
            is PrivateMessageReactionAddEvent -> onPrivateMessageReactionAdd(EventBullet(event))
            is PrivateMessageReactionRemoveEvent -> onPrivateMessageReactionRemove(EventBullet(event))
            is PrivateMessageReactionRemoveAllEvent -> onPrivateMessageReactionRemoveAll(EventBullet(event))
            is MessageReceivedEvent -> onMessageReceived(EventBullet(event))
            is MessageUpdateEvent -> onMessageUpdate(EventBullet(event))
            is MessageDeleteEvent -> onMessageDelete(EventBullet(event))
            is MessageBulkDeleteEvent -> onMessageBulkDelete(EventBullet(event))
            is MessageEmbedEvent -> onMessageEmbed(EventBullet(event))
            is MessageReactionAddEvent -> onMessageReactionAdd(EventBullet(event))
            is MessageReactionRemoveEvent -> onMessageReactionRemove(EventBullet(event))
            is MessageReactionRemoveAllEvent -> onMessageReactionRemoveAll(EventBullet(event))
            is UserNameUpdateEvent -> onUserNameUpdate(EventBullet(event))
            is UserAvatarUpdateEvent -> onUserAvatarUpdate(EventBullet(event))
            is UserGameUpdateEvent -> onUserGameUpdate(EventBullet(event))
            is UserOnlineStatusUpdateEvent -> onUserOnlineStatusUpdate(EventBullet(event))
            is UserTypingEvent -> onUserTyping(EventBullet(event))
            is SelfUpdateAvatarEvent -> onSelfUpdateAvatar(EventBullet(event))
            is SelfUpdateEmailEvent -> onSelfUpdateEmail(EventBullet(event))
            is SelfUpdateMFAEvent -> onSelfUpdateMFA(EventBullet(event))
            is SelfUpdateNameEvent -> onSelfUpdateName(EventBullet(event))
            is SelfUpdateVerifiedEvent -> onSelfUpdateVerified(EventBullet(event))
            is TextChannelCreateEvent -> onTextChannelCreate(EventBullet(event))
            is TextChannelUpdateNameEvent -> onTextChannelUpdateName(EventBullet(event))
            is TextChannelUpdateTopicEvent -> onTextChannelUpdateTopic(EventBullet(event))
            is TextChannelUpdatePositionEvent -> onTextChannelUpdatePosition(EventBullet(event))
            is TextChannelUpdatePermissionsEvent -> onTextChannelUpdatePermissions(EventBullet(event))
            is TextChannelUpdateNSFWEvent -> onTextChannelUpdateNSFW(EventBullet(event))
            is TextChannelUpdateParentEvent -> onTextChannelUpdateParent(EventBullet(event))
            is TextChannelDeleteEvent -> onTextChannelDelete(EventBullet(event))
            is VoiceChannelCreateEvent -> onVoiceChannelCreate(EventBullet(event))
            is VoiceChannelUpdateNameEvent -> onVoiceChannelUpdateName(EventBullet(event))
            is VoiceChannelUpdatePositionEvent -> onVoiceChannelUpdatePosition(EventBullet(event))
            is VoiceChannelUpdateUserLimitEvent -> onVoiceChannelUpdateUserLimit(EventBullet(event))
            is VoiceChannelUpdateBitrateEvent -> onVoiceChannelUpdateBitrate(EventBullet(event))
            is VoiceChannelUpdatePermissionsEvent -> onVoiceChannelUpdatePermissions(EventBullet(event))
            is VoiceChannelUpdateParentEvent -> onVoiceChannelUpdateParent(EventBullet(event))
            is VoiceChannelDeleteEvent -> onVoiceChannelDelete(EventBullet(event))
            is CategoryCreateEvent -> onCategoryCreate(EventBullet(event))
            is CategoryUpdateNameEvent -> onCategoryUpdateName(EventBullet(event))
            is CategoryUpdatePositionEvent -> onCategoryUpdatePosition(EventBullet(event))
            is CategoryUpdatePermissionsEvent -> onCategoryUpdatePermissions(EventBullet(event))
            is CategoryDeleteEvent -> onCategoryDelete(EventBullet(event))
            is PrivateChannelCreateEvent -> onPrivateChannelCreate(EventBullet(event))
            is PrivateChannelDeleteEvent -> onPrivateChannelDelete(EventBullet(event))
            is GuildJoinEvent -> onGuildJoin(EventBullet(event))
            is GuildLeaveEvent -> onGuildLeave(EventBullet(event))
            is GuildAvailableEvent -> onGuildAvailable(EventBullet(event))
            is GuildUnavailableEvent -> onGuildUnavailable(EventBullet(event))
            is UnavailableGuildJoinedEvent -> onUnavailableGuildJoined(EventBullet(event))
            is GuildBanEvent -> onGuildBan(EventBullet(event))
            is GuildUnbanEvent -> onGuildUnban(EventBullet(event))
            is GuildUpdateAfkChannelEvent -> onGuildUpdateAfkChannel(EventBullet(event))
            is GuildUpdateSystemChannelEvent -> onGuildUpdateSystemChannel(EventBullet(event))
            is GuildUpdateAfkTimeoutEvent -> onGuildUpdateAfkTimeout(EventBullet(event))
            is GuildUpdateIconEvent -> onGuildUpdateIcon(EventBullet(event))
            is GuildUpdateMFALevelEvent -> onGuildUpdateMFALevel(EventBullet(event))
            is GuildUpdateNameEvent -> onGuildUpdateName(EventBullet(event))
            is GuildUpdateNotificationLevelEvent -> onGuildUpdateNotificationLevel(EventBullet(event))
            is GuildUpdateOwnerEvent -> onGuildUpdateOwner(EventBullet(event))
            is GuildUpdateRegionEvent -> onGuildUpdateRegion(EventBullet(event))
            is GuildUpdateSplashEvent -> onGuildUpdateSplash(EventBullet(event))
            is GuildUpdateVerificationLevelEvent -> onGuildUpdateVerificationLevel(EventBullet(event))
            is GuildMemberJoinEvent -> onGuildMemberJoin(EventBullet(event))
            is GuildMemberLeaveEvent -> onGuildMemberLeave(EventBullet(event))
            is GuildMemberRoleAddEvent -> onGuildMemberRoleAdd(EventBullet(event))
            is GuildMemberRoleRemoveEvent -> onGuildMemberRoleRemove(EventBullet(event))
            is GuildMemberNickChangeEvent -> onGuildMemberNickChange(EventBullet(event))
            is GuildVoiceJoinEvent -> onGuildVoiceJoin(EventBullet(event))
            is GuildVoiceMoveEvent -> onGuildVoiceMove(EventBullet(event))
            is GuildVoiceLeaveEvent -> onGuildVoiceLeave(EventBullet(event))
            is GuildVoiceMuteEvent -> onGuildVoiceMute(EventBullet(event))
            is GuildVoiceDeafenEvent -> onGuildVoiceDeafen(EventBullet(event))
            is GuildVoiceGuildMuteEvent -> onGuildVoiceGuildMute(EventBullet(event))
            is GuildVoiceGuildDeafenEvent -> onGuildVoiceGuildDeafen(EventBullet(event))
            is GuildVoiceSelfMuteEvent -> onGuildVoiceSelfMute(EventBullet(event))
            is GuildVoiceSelfDeafenEvent -> onGuildVoiceSelfDeafen(EventBullet(event))
            is GuildVoiceSuppressEvent -> onGuildVoiceSuppress(EventBullet(event))
            is RoleCreateEvent -> onRoleCreate(EventBullet(event))
            is RoleDeleteEvent -> onRoleDelete(EventBullet(event))
            is RoleUpdateColorEvent -> onRoleUpdateColor(EventBullet(event))
            is RoleUpdateHoistedEvent -> onRoleUpdateHoisted(EventBullet(event))
            is RoleUpdateMentionableEvent -> onRoleUpdateMentionable(EventBullet(event))
            is RoleUpdateNameEvent -> onRoleUpdateName(EventBullet(event))
            is RoleUpdatePermissionsEvent -> onRoleUpdatePermissions(EventBullet(event))
            is RoleUpdatePositionEvent -> onRoleUpdatePosition(EventBullet(event))
            is EmoteAddedEvent -> onEmoteAdded(EventBullet(event))
            is EmoteRemovedEvent -> onEmoteRemoved(EventBullet(event))
            is EmoteUpdateNameEvent -> onEmoteUpdateName(EventBullet(event))
            is EmoteUpdateRolesEvent -> onEmoteUpdateRoles(EventBullet(event))
            is HttpRequestEvent -> onHttpRequest(EventBullet(event))
        }
        when (event) {
            is GenericGuildMessageEvent -> onGenericGuildMessage(EventBullet(event))
            is GenericMessageReactionEvent -> onGenericMessageReaction(EventBullet(event))
            is GenericPrivateMessageEvent -> onGenericPrivateMessage(EventBullet(event))
            is GenericPrivateMessageReactionEvent -> onGenericPrivateMessageReaction(EventBullet(event))
            is GenericTextChannelUpdateEvent -> onGenericTextChannelUpdate(EventBullet(event))
            is GenericCategoryUpdateEvent -> onGenericCategoryUpdate(EventBullet(event))
            is GenericGuildMessageReactionEvent -> onGenericGuildMessageReaction(EventBullet(event))
            is GenericVoiceChannelUpdateEvent -> onGenericVoiceChannelUpdate(EventBullet(event))
            is GenericGuildUpdateEvent -> onGenericGuildUpdate(EventBullet(event))
            is GenericGuildMemberEvent -> onGenericGuildMember(EventBullet(event))
            is GenericGuildVoiceEvent -> onGenericGuildVoice(EventBullet(event))
            is GenericRoleUpdateEvent -> onGenericRoleUpdate(EventBullet(event))
            is GenericEmoteUpdateEvent -> onGenericEmoteUpdate(EventBullet(event))
        }
        when (event) {
            is GenericMessageEvent -> onGenericMessage(EventBullet(event))
            is GenericUserEvent -> onGenericUser(EventBullet(event))
            is GenericSelfUpdateEvent -> onGenericSelfUpdate(EventBullet(event))
            is GenericTextChannelEvent -> onGenericTextChannel(EventBullet(event))
            is GenericVoiceChannelEvent -> onGenericVoiceChannel(EventBullet(event))
            is GenericCategoryEvent -> onGenericCategory(EventBullet(event))
            is GenericGuildEvent -> onGenericGuild(EventBullet(event))
            is GenericRoleEvent -> onGenericRole(EventBullet(event))
            is GenericEmoteEvent -> onGenericEmote(EventBullet(event))
        }

        if (event.event.jda.accountType == AccountType.CLIENT) {
            when (event) {
                is FriendAddedEvent -> onFriendAdded(EventBullet(event))
                is FriendRemovedEvent -> onFriendRemoved(EventBullet(event))
                is UserBlockedEvent -> onUserBlocked(EventBullet(event))
                is UserUnblockedEvent -> onUserUnblocked(EventBullet(event))
                is FriendRequestSentEvent -> onFriendRequestSent(EventBullet(event))
                is FriendRequestCanceledEvent -> onFriendRequestCanceled(EventBullet(event))
                is FriendRequestReceivedEvent -> onFriendRequestReceived(EventBullet(event))
                is FriendRequestIgnoredEvent -> onFriendRequestIgnored(EventBullet(event))
                is GroupJoinEvent -> onGroupJoin(EventBullet(event))
                is GroupLeaveEvent -> onGroupLeave(EventBullet(event))
                is GroupUserJoinEvent -> onGroupUserJoin(EventBullet(event))
                is GroupUserLeaveEvent -> onGroupUserLeave(EventBullet(event))
            }
            when (event) {
                is GroupMessageReceivedEvent -> onGroupMessageReceived(EventBullet(event))
                is GroupMessageUpdateEvent -> onGroupMessageUpdate(EventBullet(event))
                is GroupMessageDeleteEvent -> onGroupMessageDelete(EventBullet(event))
                is GroupMessageEmbedEvent -> onGroupMessageEmbed(EventBullet(event))
                is GroupMessageReactionAddEvent -> onGroupMessageReactionAdd(EventBullet(event))
                is GroupMessageReactionRemoveEvent -> onGroupMessageReactionRemove(EventBullet(event))
                is GroupMessageReactionRemoveAllEvent -> onGroupMessageReactionRemoveAll(EventBullet(event))
                is GroupUpdateIconEvent -> onGroupUpdateIcon(EventBullet(event))
                is GroupUpdateNameEvent -> onGroupUpdateName(EventBullet(event))
                is GroupUpdateOwnerEvent -> onGroupUpdateOwner(EventBullet(event))
                is CallCreateEvent -> onCallCreate(EventBullet(event))
                is CallDeleteEvent -> onCallDelete(EventBullet(event))
                is CallUpdateRegionEvent -> onCallUpdateRegion(EventBullet(event))
                is CallUpdateRingingUsersEvent -> onCallUpdateRingingUsers(EventBullet(event))
                is CallVoiceJoinEvent -> onCallVoiceJoin(EventBullet(event))
                is CallVoiceLeaveEvent -> onCallVoiceLeave(EventBullet(event))
                is CallVoiceSelfMuteEvent -> onCallVoiceSelfMute(EventBullet(event))
                is CallVoiceSelfDeafenEvent -> onCallVoiceSelfDeafen(EventBullet(event))
            }
            when (event) {
                is GenericRelationshipAddEvent -> onGenericRelationshipAdd(EventBullet(event))
                is GenericRelationshipRemoveEvent -> onGenericRelationshipRemove(EventBullet(event))
                is GenericGroupMessageEvent -> onGenericGroupMessage(EventBullet(event))
                is GenericGroupMessageReactionEvent -> onGenericGroupMessageReaction(EventBullet(event))
                is GenericGroupUpdateEvent -> onGenericGroupUpdate(EventBullet(event))
                is GenericCallUpdateEvent -> onGenericCallUpdate(EventBullet(event))
                is GenericCallVoiceEvent -> onGenericCallVoice(EventBullet(event))
            }
            when (event) {
                is GenericRelationshipEvent -> onGenericRelationship(EventBullet(event))
                is GenericGroupEvent -> onGenericGroup(EventBullet(event))
                is GenericCallEvent -> onGenericCall(EventBullet(event))
            }
        }
    }

    open fun onGenericEvent(event: EventBullet<Event>) {}
    open fun onReady(event: EventBullet<ReadyEvent>) {}
    open fun onResume(event: EventBullet<ResumedEvent>) {}
    open fun onReconnect(event: EventBullet<ReconnectedEvent>) {}
    open fun onDisconnect(event: EventBullet<DisconnectEvent>) {}
    open fun onShutdown(event: EventBullet<ShutdownEvent>) {}
    open fun onStatusChange(event: EventBullet<StatusChangeEvent>) {}
    open fun onException(event: EventBullet<ExceptionEvent>) {}
    open fun onUserNameUpdate(event: EventBullet<UserNameUpdateEvent>) {}
    open fun onUserAvatarUpdate(event: EventBullet<UserAvatarUpdateEvent>) {}
    open fun onUserOnlineStatusUpdate(event: EventBullet<UserOnlineStatusUpdateEvent>) {}
    open fun onUserGameUpdate(event: EventBullet<UserGameUpdateEvent>) {}
    open fun onUserTyping(event: EventBullet<UserTypingEvent>) {}
    open fun onSelfUpdateAvatar(event: EventBullet<SelfUpdateAvatarEvent>) {}
    open fun onSelfUpdateEmail(event: EventBullet<SelfUpdateEmailEvent>) {}
    open fun onSelfUpdateMFA(event: EventBullet<SelfUpdateMFAEvent>) {}
    open fun onSelfUpdateName(event: EventBullet<SelfUpdateNameEvent>) {}
    open fun onSelfUpdateVerified(event: EventBullet<SelfUpdateVerifiedEvent>) {}
    open fun onGuildMessageReceived(event: EventBullet<GuildMessageReceivedEvent>) {}
    open fun onGuildMessageUpdate(event: EventBullet<GuildMessageUpdateEvent>) {}
    open fun onGuildMessageDelete(event: EventBullet<GuildMessageDeleteEvent>) {}
    open fun onGuildMessageEmbed(event: EventBullet<GuildMessageEmbedEvent>) {}
    open fun onGuildMessageReactionAdd(event: EventBullet<GuildMessageReactionAddEvent>) {}
    open fun onGuildMessageReactionRemove(event: EventBullet<GuildMessageReactionRemoveEvent>) {}
    open fun onGuildMessageReactionRemoveAll(event: EventBullet<GuildMessageReactionRemoveAllEvent>) {}
    open fun onPrivateMessageReceived(event: EventBullet<PrivateMessageReceivedEvent>) {}
    open fun onPrivateMessageUpdate(event: EventBullet<PrivateMessageUpdateEvent>) {}
    open fun onPrivateMessageDelete(event: EventBullet<PrivateMessageDeleteEvent>) {}
    open fun onPrivateMessageEmbed(event: EventBullet<PrivateMessageEmbedEvent>) {}
    open fun onPrivateMessageReactionAdd(event: EventBullet<PrivateMessageReactionAddEvent>) {}
    open fun onPrivateMessageReactionRemove(event: EventBullet<PrivateMessageReactionRemoveEvent>) {}
    open fun onPrivateMessageReactionRemoveAll(event: EventBullet<PrivateMessageReactionRemoveAllEvent>) {}
    open fun onMessageReceived(event: EventBullet<MessageReceivedEvent>) {}
    open fun onMessageUpdate(event: EventBullet<MessageUpdateEvent>) {}
    open fun onMessageDelete(event: EventBullet<MessageDeleteEvent>) {}
    open fun onMessageBulkDelete(event: EventBullet<MessageBulkDeleteEvent>) {}
    open fun onMessageEmbed(event: EventBullet<MessageEmbedEvent>) {}
    open fun onMessageReactionAdd(event: EventBullet<MessageReactionAddEvent>) {}
    open fun onMessageReactionRemove(event: EventBullet<MessageReactionRemoveEvent>) {}
    open fun onMessageReactionRemoveAll(event: EventBullet<MessageReactionRemoveAllEvent>) {}
    open fun onTextChannelDelete(event: EventBullet<TextChannelDeleteEvent>) {}
    open fun onTextChannelUpdateName(event: EventBullet<TextChannelUpdateNameEvent>) {}
    open fun onTextChannelUpdateTopic(event: EventBullet<TextChannelUpdateTopicEvent>) {}
    open fun onTextChannelUpdatePosition(event: EventBullet<TextChannelUpdatePositionEvent>) {}
    open fun onTextChannelUpdatePermissions(event: EventBullet<TextChannelUpdatePermissionsEvent>) {}
    open fun onTextChannelUpdateNSFW(event: EventBullet<TextChannelUpdateNSFWEvent>) {}
    open fun onTextChannelUpdateParent(event: EventBullet<TextChannelUpdateParentEvent>) {}
    open fun onTextChannelCreate(event: EventBullet<TextChannelCreateEvent>) {}
    open fun onVoiceChannelDelete(event: EventBullet<VoiceChannelDeleteEvent>) {}
    open fun onVoiceChannelUpdateName(event: EventBullet<VoiceChannelUpdateNameEvent>) {}
    open fun onVoiceChannelUpdatePosition(event: EventBullet<VoiceChannelUpdatePositionEvent>) {}
    open fun onVoiceChannelUpdateUserLimit(event: EventBullet<VoiceChannelUpdateUserLimitEvent>) {}
    open fun onVoiceChannelUpdateBitrate(event: EventBullet<VoiceChannelUpdateBitrateEvent>) {}
    open fun onVoiceChannelUpdatePermissions(event: EventBullet<VoiceChannelUpdatePermissionsEvent>) {}
    open fun onVoiceChannelUpdateParent(event: EventBullet<VoiceChannelUpdateParentEvent>) {}
    open fun onVoiceChannelCreate(event: EventBullet<VoiceChannelCreateEvent>) {}
    open fun onCategoryDelete(event: EventBullet<CategoryDeleteEvent>) {}
    open fun onCategoryUpdateName(event: EventBullet<CategoryUpdateNameEvent>) {}
    open fun onCategoryUpdatePosition(event: EventBullet<CategoryUpdatePositionEvent>) {}
    open fun onCategoryUpdatePermissions(event: EventBullet<CategoryUpdatePermissionsEvent>) {}
    open fun onCategoryCreate(evnet: EventBullet<CategoryCreateEvent>) {}
    open fun onPrivateChannelCreate(event: EventBullet<PrivateChannelCreateEvent>) {}
    open fun onPrivateChannelDelete(event: EventBullet<PrivateChannelDeleteEvent>) {}
    open fun onGuildJoin(event: EventBullet<GuildJoinEvent>) {}
    open fun onGuildLeave(event: EventBullet<GuildLeaveEvent>) {}
    open fun onGuildAvailable(event: EventBullet<GuildAvailableEvent>) {}
    open fun onGuildUnavailable(event: EventBullet<GuildUnavailableEvent>) {}
    open fun onUnavailableGuildJoined(event: EventBullet<UnavailableGuildJoinedEvent>) {}
    open fun onGuildBan(event: EventBullet<GuildBanEvent>) {}
    open fun onGuildUnban(event: EventBullet<GuildUnbanEvent>) {}
    open fun onGuildUpdateAfkChannel(event: EventBullet<GuildUpdateAfkChannelEvent>) {}
    open fun onGuildUpdateSystemChannel(event: EventBullet<GuildUpdateSystemChannelEvent>) {}
    open fun onGuildUpdateAfkTimeout(event: EventBullet<GuildUpdateAfkTimeoutEvent>) {}
    open fun onGuildUpdateIcon(event: EventBullet<GuildUpdateIconEvent>) {}
    open fun onGuildUpdateMFALevel(event: EventBullet<GuildUpdateMFALevelEvent>) {}
    open fun onGuildUpdateName(event: EventBullet<GuildUpdateNameEvent>) {}
    open fun onGuildUpdateNotificationLevel(event: EventBullet<GuildUpdateNotificationLevelEvent>) {}
    open fun onGuildUpdateOwner(event: EventBullet<GuildUpdateOwnerEvent>) {}
    open fun onGuildUpdateRegion(event: EventBullet<GuildUpdateRegionEvent>) {}
    open fun onGuildUpdateSplash(event: EventBullet<GuildUpdateSplashEvent>) {}
    open fun onGuildUpdateVerificationLevel(event: EventBullet<GuildUpdateVerificationLevelEvent>) {}
    open fun onGuildMemberJoin(event: EventBullet<GuildMemberJoinEvent>) {}
    open fun onGuildMemberLeave(event: EventBullet<GuildMemberLeaveEvent>) {}
    open fun onGuildMemberRoleAdd(event: EventBullet<GuildMemberRoleAddEvent>) {}
    open fun onGuildMemberRoleRemove(event: EventBullet<GuildMemberRoleRemoveEvent>) {}
    open fun onGuildMemberNickChange(event: EventBullet<GuildMemberNickChangeEvent>) {}
    open fun onGuildVoiceJoin(event: EventBullet<GuildVoiceJoinEvent>) {}
    open fun onGuildVoiceMove(event: EventBullet<GuildVoiceMoveEvent>) {}
    open fun onGuildVoiceLeave(event: EventBullet<GuildVoiceLeaveEvent>) {}
    open fun onGuildVoiceMute(event: EventBullet<GuildVoiceMuteEvent>) {}
    open fun onGuildVoiceDeafen(event: EventBullet<GuildVoiceDeafenEvent>) {}
    open fun onGuildVoiceGuildMute(event: EventBullet<GuildVoiceGuildMuteEvent>) {}
    open fun onGuildVoiceGuildDeafen(event: EventBullet<GuildVoiceGuildDeafenEvent>) {}
    open fun onGuildVoiceSelfMute(event: EventBullet<GuildVoiceSelfMuteEvent>) {}
    open fun onGuildVoiceSelfDeafen(event: EventBullet<GuildVoiceSelfDeafenEvent>) {}
    open fun onGuildVoiceSuppress(event: EventBullet<GuildVoiceSuppressEvent>) {}
    open fun onRoleCreate(event: EventBullet<RoleCreateEvent>) {}
    open fun onRoleDelete(event: EventBullet<RoleDeleteEvent>) {}
    open fun onRoleUpdateColor(event: EventBullet<RoleUpdateColorEvent>) {}
    open fun onRoleUpdateHoisted(event: EventBullet<RoleUpdateHoistedEvent>) {}
    open fun onRoleUpdateMentionable(event: EventBullet<RoleUpdateMentionableEvent>) {}
    open fun onRoleUpdateName(event: EventBullet<RoleUpdateNameEvent>) {}
    open fun onRoleUpdatePermissions(event: EventBullet<RoleUpdatePermissionsEvent>) {}
    open fun onRoleUpdatePosition(event: EventBullet<RoleUpdatePositionEvent>) {}
    open fun onEmoteAdded(event: EventBullet<EmoteAddedEvent>) {}
    open fun onEmoteRemoved(event: EventBullet<EmoteRemovedEvent>) {}
    open fun onEmoteUpdateName(event: EventBullet<EmoteUpdateNameEvent>) {}
    open fun onEmoteUpdateRoles(event: EventBullet<EmoteUpdateRolesEvent>) {}
    open fun onHttpRequest(event: EventBullet<HttpRequestEvent>) {}
    open fun onGenericMessage(event: EventBullet<GenericMessageEvent>) {}
    open fun onGenericMessageReaction(event: EventBullet<GenericMessageReactionEvent>) {}
    open fun onGenericGuildMessage(event: EventBullet<GenericGuildMessageEvent>) {}
    open fun onGenericGuildMessageReaction(event: EventBullet<GenericGuildMessageReactionEvent>) {}
    open fun onGenericPrivateMessage(event: EventBullet<GenericPrivateMessageEvent>) {}
    open fun onGenericPrivateMessageReaction(event: EventBullet<GenericPrivateMessageReactionEvent>) {}
    open fun onGenericUser(event: EventBullet<GenericUserEvent>) {}
    open fun onGenericSelfUpdate(event: EventBullet<GenericSelfUpdateEvent>) {}
    open fun onGenericTextChannel(event: EventBullet<GenericTextChannelEvent>) {}
    open fun onGenericTextChannelUpdate(event: EventBullet<GenericTextChannelUpdateEvent>) {}
    open fun onGenericVoiceChannel(event: EventBullet<GenericVoiceChannelEvent>) {}
    open fun onGenericVoiceChannelUpdate(event: EventBullet<GenericVoiceChannelUpdateEvent>) {}
    open fun onGenericCategory(event: EventBullet<GenericCategoryEvent>) {}
    open fun onGenericCategoryUpdate(event: EventBullet<GenericCategoryUpdateEvent>) {}
    open fun onGenericGuild(event: EventBullet<GenericGuildEvent>) {}
    open fun onGenericGuildUpdate(event: EventBullet<GenericGuildUpdateEvent>) {}
    open fun onGenericGuildMember(event: EventBullet<GenericGuildMemberEvent>) {}
    open fun onGenericGuildVoice(event: EventBullet<GenericGuildVoiceEvent>) {}
    open fun onGenericRole(event: EventBullet<GenericRoleEvent>) {}
    open fun onGenericRoleUpdate(event: EventBullet<GenericRoleUpdateEvent>) {}
    open fun onGenericEmote(event: EventBullet<GenericEmoteEvent>) {}
    open fun onGenericEmoteUpdate(event: EventBullet<GenericEmoteUpdateEvent>) {}

    // ==========================================================================================
    // |                                   Client Only Events                                   |
    // ==========================================================================================
    
    open fun onFriendAdded(event: EventBullet<FriendAddedEvent>) {}
    open fun onFriendRemoved(event: EventBullet<FriendRemovedEvent>) {}
    open fun onUserBlocked(event: EventBullet<UserBlockedEvent>) {}
    open fun onUserUnblocked(event: EventBullet<UserUnblockedEvent>) {}
    open fun onFriendRequestSent(event: EventBullet<FriendRequestSentEvent>) {}
    open fun onFriendRequestCanceled(event: EventBullet<FriendRequestCanceledEvent>) {}
    open fun onFriendRequestReceived(event: EventBullet<FriendRequestReceivedEvent>) {}
    open fun onFriendRequestIgnored(event: EventBullet<FriendRequestIgnoredEvent>) {}
    open fun onGroupJoin(event: EventBullet<GroupJoinEvent>) {}
    open fun onGroupLeave(event: EventBullet<GroupLeaveEvent>) {}
    open fun onGroupUserJoin(event: EventBullet<GroupUserJoinEvent>) {}
    open fun onGroupUserLeave(event: EventBullet<GroupUserLeaveEvent>) {}
    open fun onGroupMessageReceived(event: EventBullet<GroupMessageReceivedEvent>) {}
    open fun onGroupMessageUpdate(event: EventBullet<GroupMessageUpdateEvent>) {}
    open fun onGroupMessageDelete(event: EventBullet<GroupMessageDeleteEvent>) {}
    open fun onGroupMessageEmbed(event: EventBullet<GroupMessageEmbedEvent>) {}
    open fun onGroupMessageReactionAdd(event: EventBullet<GroupMessageReactionAddEvent>) {}
    open fun onGroupMessageReactionRemove(event: EventBullet<GroupMessageReactionRemoveEvent>) {}
    open fun onGroupMessageReactionRemoveAll(event: EventBullet<GroupMessageReactionRemoveAllEvent>) {}
    open fun onGroupUpdateIcon(event: EventBullet<GroupUpdateIconEvent>) {}
    open fun onGroupUpdateName(event: EventBullet<GroupUpdateNameEvent>) {}
    open fun onGroupUpdateOwner(event: EventBullet<GroupUpdateOwnerEvent>) {}
    open fun onCallCreate(event: EventBullet<CallCreateEvent>) {}
    open fun onCallDelete(event: EventBullet<CallDeleteEvent>) {}
    open fun onCallUpdateRegion(event: EventBullet<CallUpdateRegionEvent>) {}
    open fun onCallUpdateRingingUsers(event: EventBullet<CallUpdateRingingUsersEvent>) {}
    open fun onCallVoiceJoin(event: EventBullet<CallVoiceJoinEvent>) {}
    open fun onCallVoiceLeave(event: EventBullet<CallVoiceLeaveEvent>) {}
    open fun onCallVoiceSelfMute(event: EventBullet<CallVoiceSelfMuteEvent>) {}
    open fun onCallVoiceSelfDeafen(event: EventBullet<CallVoiceSelfDeafenEvent>) {}
    open fun onGenericRelationship(event: EventBullet<GenericRelationshipEvent>) {}
    open fun onGenericRelationshipAdd(event: EventBullet<GenericRelationshipAddEvent>) {}
    open fun onGenericRelationshipRemove(event: EventBullet<GenericRelationshipRemoveEvent>) {}
    open fun onGenericGroup(event: EventBullet<GenericGroupEvent>) {}
    open fun onGenericGroupMessage(event: EventBullet<GenericGroupMessageEvent>) {}
    open fun onGenericGroupMessageReaction(event: EventBullet<GenericGroupMessageReactionEvent>) {}
    open fun onGenericGroupUpdate(event: EventBullet<GenericGroupUpdateEvent>) {}
    open fun onGenericCall(event: EventBullet<GenericCallEvent>) {}
    open fun onGenericCallUpdate(event: EventBullet<GenericCallUpdateEvent>) {}
    open fun onGenericCallVoice(event: EventBullet<GenericCallVoiceEvent>) {}
}