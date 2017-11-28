package com.avos.avospush.session;

import com.avos.avoscloud.LogUtil;
import com.avos.avoscloud.Messages;
import com.avos.avoscloud.AVUtils;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMMessageOption;
import com.google.protobuf.ByteString;

import java.util.List;

public class ConversationDirectMessagePacket extends PeerBasedCommandPacket {
  String conversationId;
  String message = null;
  boolean mentionAll = false;
  List<String> mentionList = null;
  String messageToken = null;
  AVIMMessageOption messageOption;
  ByteString binaryMessage = null;

  public ConversationDirectMessagePacket() {
    this.setCmd("direct");
  }

  public String getConversationId() {
    return conversationId;
  }

  public void setConversationId(String conversationId) {
    this.conversationId = conversationId;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  private void setMessageOption(AVIMMessageOption option) {
    this.messageOption = option;
  }

  public boolean isMentionAll() {
    return mentionAll;
  }

  public void setMentionAll(boolean mentionAll) {
    this.mentionAll = mentionAll;
  }

  public List<String> getMentionList() {
    return mentionList;
  }

  public void setMentionList(List<String> mentionList) {
    this.mentionList = mentionList;
  }

  public void setBinaryMessage(byte[] bytes) {
    if (null == bytes) {
      this.binaryMessage = null;
    } else {
      this.binaryMessage = ByteString.copyFrom(bytes);
    }
  }

  @Override
  protected Messages.GenericCommand.Builder getGenericCommandBuilder() {
    Messages.GenericCommand.Builder builder = super.getGenericCommandBuilder();
    builder.setDirectMessage(getDirectCommand());
    if (null != messageOption) {
      if (null != messageOption.getPriority()) {
        builder.setPriority(messageOption.getPriority().getNumber());
      }
    }
    return builder;
  }

  protected Messages.DirectCommand getDirectCommand() {
    Messages.DirectCommand.Builder builder = Messages.DirectCommand.newBuilder();
    if (null != message) {
      builder.setMsg(message);
    }
    builder.setCid(conversationId);
    if (mentionAll) {
      builder.setMentionAll(mentionAll);
    }
    if (null != mentionList && mentionList.size() > 0) {
      builder.addAllMentionPids(mentionList);
    }

    if (null != messageOption) {
      if (messageOption.isReceipt()) {
        builder.setR(true);
      }
      if (messageOption.isTransient()) {
        builder.setTransient(true);
      }

      String pushData = messageOption.getPushData();
      if (!AVUtils.isBlankString(pushData)) {
        builder.setPushData(pushData);
      }

      if (messageOption.isWill()) {
        builder.setWill(true);
      }
    }

    if (!AVUtils.isBlankString(messageToken)) {
      builder.setDt(messageToken);
    }

    if (null != binaryMessage) {
      builder.setBinaryMsg(binaryMessage);
    }

    return builder.build();
  }

  public static ConversationDirectMessagePacket getConversationMessagePacket(String peerId,
                                                                             String conversationId,
                                                                             String msg, byte[] binaryMsg, boolean mentionAll, List<String> mentionList,
                                                                             AVIMMessageOption messageOption, int requestId) {
    ConversationDirectMessagePacket cdmp = new ConversationDirectMessagePacket();
    if (AVIMClient.getClientsCount() > 1) {
      // peerId is necessary only when more than 1 client logined.
      cdmp.setPeerId(peerId);
    }
    cdmp.setConversationId(conversationId);
    cdmp.setRequestId(requestId);
    cdmp.setMessageOption(messageOption);
    cdmp.setMessage(msg);
    cdmp.setMentionAll(mentionAll);
    cdmp.setMentionList(mentionList);
    cdmp.setBinaryMessage(binaryMsg);
    return cdmp;
  }

  public static ConversationDirectMessagePacket getConversationMessagePacket(String peerId,
                                                                             String conversationId,
                                                                             String msg, byte[] binaryMsg, boolean mentionAll, List<String> mentionList,
                                                                             String messageToken, AVIMMessageOption option, int requestId) {
    ConversationDirectMessagePacket cdmp =
      getConversationMessagePacket(peerId, conversationId, msg, binaryMsg, mentionAll, mentionList, option, requestId);
    cdmp.messageToken = messageToken;
    return cdmp;
  }
}
