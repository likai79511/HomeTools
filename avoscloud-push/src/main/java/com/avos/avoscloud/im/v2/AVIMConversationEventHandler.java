package com.avos.avoscloud.im.v2;

import android.annotation.TargetApi;
import android.util.Pair;

import com.avos.avoscloud.AVIMEventHandler;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;

import java.util.List;

/**
 * 用于处理AVIMConversation中产生的事件
 * Created by lbt05 on 1/29/15.
 */
@TargetApi(11)
public abstract class AVIMConversationEventHandler extends AVIMEventHandler {
  /**
   * 实现本方法以处理聊天对话中的参与者离开事件
   *
   * @param client
   * @param conversation
   * @param members 离开的参与者
   * @param kickedBy 离开事件的发动者，有可能是离开的参与者本身
   * @since 3.0
   */

  public abstract void onMemberLeft(AVIMClient client,
      AVIMConversation conversation, List<String> members, String kickedBy);

  /**
   * 实现本方法以处理聊天对话中的参与者加入事件
   *
   * @param client
   * @param conversation
   * @param members 加入的参与者
   * @param invitedBy 加入事件的邀请人，有可能是加入的参与者本身
   * @since 3.0
   */

  public abstract void onMemberJoined(AVIMClient client,
      AVIMConversation conversation, List<String> members, String invitedBy);

  /**
   * 实现本方法来处理当前用户被踢出某个聊天对话事件
   *
   * @param client
   * @param conversation
   * @param kickedBy 踢出你的人
   * @since 3.0
   */

  public abstract void onKicked(AVIMClient client, AVIMConversation conversation,
      String kickedBy);

  /**
   * 实现本方法来处理当前用户被邀请到某个聊天对话事件
   *
   * @param client
   * @param conversation 被邀请的聊天对话
   * @param operator 邀请你的人
   * @since 3.0
   */
  public abstract void onInvited(AVIMClient client, AVIMConversation conversation,
      String operator);

  /**
   * 实现本地方法来处理未读消息数量的通知
   * @param client
   * @param conversation
   */
  public void onUnreadMessagesCountUpdated(AVIMClient client, AVIMConversation conversation) {}

  /**
   * 实现本地方法来处理对方已经接收消息的通知
   */
  public void onLastDeliveredAtUpdated(AVIMClient client, AVIMConversation conversation) {}

  /**
   * 实现本地方法来处理对方已经阅读消息的通知
   */
  public void onLastReadAtUpdated(AVIMClient client, AVIMConversation conversation) {}

  /**
   * 实现本地方法来处理消息的更新事件
   * @param client
   * @param conversation
   * @param message
   */
  public void onMessageUpdated(AVIMClient client, AVIMConversation conversation, AVIMMessage message) {}

  /**
   * 实现本地方法来处理消息的撤回事件
   * @param client
   * @param conversation
   * @param message
   */
  public void onMessageRecalled(AVIMClient client, AVIMConversation conversation, AVIMMessage message) {}

  @Override
  protected final void processEvent0(final int operation, final Object operator, final Object operand,
      Object eventScene) {
    final AVIMConversation conversation = (AVIMConversation) eventScene;
    if (conversation.isShouldFetch()) {
      conversation.fetchInfoInBackground(new AVIMConversationCallback() {
        @Override
        public void done(AVIMException e) {
          if (null != e && e.getCode() > 0) {
            conversation.latestConversationFetch = System.currentTimeMillis();
          }
          processConversationEvent(operation, operator, operand, conversation);
        }
      });
    } else {
      processConversationEvent(operation, operator, operand, conversation);
    }
  }

  private void processConversationEvent(int operation, Object operator, Object operand, AVIMConversation conversation) {
    switch (operation) {
      case Conversation.STATUS_ON_MEMBERS_LEFT:
        onMemberLeft(conversation.client, conversation, (List<String>) operand, (String) operator);
        break;
      case Conversation.STATUS_ON_MEMBERS_JOINED:
        onMemberJoined(conversation.client, conversation, (List<String>) operand, (String) operator);
        break;
      case Conversation.STATUS_ON_JOINED:
        onInvited(conversation.client, conversation, (String) operator);
        break;
      case Conversation.STATUS_ON_KICKED_FROM_CONVERSATION:
        onKicked(conversation.client, conversation, (String) operator);
        break;
      case Conversation.STATUS_ON_UNREAD_EVENT:
        Pair<Integer, Boolean> unreadInfo = (Pair<Integer, Boolean>)operand;
        conversation.updateUnreadCountAndMessage((AVIMMessage)operator, unreadInfo.first, unreadInfo.second);
        onUnreadMessagesCountUpdated(conversation.client, conversation);
        break;
      case Conversation.STATUS_ON_MESSAGE_READ:
        conversation.setLastReadAt((long)operator, true);
        onLastReadAtUpdated(conversation.client, conversation);
        break;
      case Conversation.STATUS_ON_MESSAGE_DELIVERED:
        conversation.setLastDeliveredAt((long)operator, true);
        onLastDeliveredAtUpdated(conversation.client, conversation);
        break;
      case Conversation.STATUS_ON_MESSAGE_UPDATED:
        AVIMMessage message = (AVIMMessage)operator;
        conversation.updateLocalMessage(message);
        onMessageUpdated(conversation.client, conversation, message);
        break;
      case Conversation.STATUS_ON_MESSAGE_RECALLED:
        AVIMMessage recalledMessage = (AVIMMessage)operator;
        conversation.updateLocalMessage(recalledMessage);
        onMessageRecalled(conversation.client, conversation, recalledMessage);
    }
  }
}
