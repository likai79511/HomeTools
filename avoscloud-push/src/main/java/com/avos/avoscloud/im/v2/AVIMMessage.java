package com.avos.avoscloud.im.v2;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AVIMMessage implements Parcelable {

  public AVIMMessage() {
    this(null, null);
  }

  public AVIMMessage(String conversationId, String from) {
    this(conversationId, from, 0, 0);
  }

  public AVIMMessage(String conversationId, String from, long timestamp, long deliveredAt) {
    this(conversationId, from, timestamp, deliveredAt, 0);
  }

  public AVIMMessage(String conversationId, String from, long timestamp, long deliveredAt, long readAt) {
    this.ioType = AVIMMessageIOType.AVIMMessageIOTypeOut;
    this.status = AVIMMessageStatus.AVIMMessageStatusNone;
    this.conversationId = conversationId;
    this.from = from;
    this.timestamp = timestamp;
    this.deliveredAt = deliveredAt;
    this.readAt = readAt;
  }

  /**
   * 获取当前聊天对话对应的id
   * <p/>
   * 对应的是AVOSRealtimeConversations表中的objectId
   *
   * @return
   * @since 3.0
   */
  public String getConversationId() {
    return conversationId;
  }

  /**
   * 设置消息所在的conversationId，本方法一般用于从反序列化时
   *
   * @param conversationId
   */
  public void setConversationId(String conversationId) {
    this.conversationId = conversationId;
  }

  /**
   * 获取消息体的内容
   *
   * @return
   * @since 3.0
   */
  public String getContent() {
    return content;
  }

  /**
   * 设置消息体的内容
   *
   * @param content
   * @since 3.0
   */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * 获取消息的发送者
   *
   * @return
   */
  public String getFrom() {
    return from;
  }

  /**
   * 设置消息的发送者
   *
   * @param from
   * @since 3.7.3
   */
  public void setFrom(String from) {
    this.from = from;
  }

  /**
   * 获取消息发送的时间
   *
   * @return
   */
  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * @deprecated Please use {@link #getDeliveredAt()}
   * 获取消息成功到达接收方的时间
   *
   * @return
   * @see AVIMConversation#RECEIPT_MESSAGE_FLAG
   */
  public long getReceiptTimestamp() {
    return deliveredAt;
  }

  /**
   * @deprecated Please use {@link #setDeliveredAt(long)}
   *
   * @return
   * @see AVIMConversation#RECEIPT_MESSAGE_FLAG
   */
  public void setReceiptTimestamp(long receiptTimestamp) {
    this.deliveredAt = receiptTimestamp;
  }

  void setDeliveredAt(long deliveredAt) {
    this.deliveredAt = deliveredAt;
  }

  /**
   * 获取消息成功到达接收方的时间
   * @return
   */
  public long getDeliveredAt() {
    return deliveredAt;
  }

  void setReadAt(long readAt) {
    this.readAt = readAt;
  }

  long getReadAt() {
    return readAt;
  }

  /**
   * set the update time of the message
   * @param updateAt
   */
  public void setUpdateAt(long updateAt) {
    this.updateAt = updateAt;
  }

  /**
   * get the update time of the message
   * @return
   */
  public long getUpdateAt() {
    return updateAt;
  }

  /**
   * 设置消息当前的状态，本方法一般用于从反序列化时
   *
   * @param status
   */
  public void setMessageStatus(AVIMMessageStatus status) {
    this.status = status;
  }

  /**
   * 获取消息当前的状态
   *
   * @return
   */

  public AVIMMessageStatus getMessageStatus() {
    return this.status;
  }

  /**
   * 获取消息IO类型
   *
   * @return
   */
  public AVIMMessageIOType getMessageIOType() {
    return ioType;
  }

  /**
   * 设置消息的IO类型，本方法一般用于反序列化
   *
   * @param ioType
   */
  public void setMessageIOType(AVIMMessageIOType ioType) {
    this.ioType = ioType;
  }

  /**
   * 获取消息的全局Id
   * <p/>
   * 这个id只有在发送成功或者收到消息时才会有对应的值
   *
   * @return
   */
  public String getMessageId() {
    return messageId;
  }

  /**
   * 仅仅是用于反序列化消息时使用，请不要在其他时候使用
   *
   * @param messageId
   */
  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  /**
   * 判断消息里面是否 mention 了当前用户
   * @return
   */
  public boolean mentioned() {
    return isMentionAll() || (null != mentionList && mentionList.contains(currentClient));
  }

  /**
   * 设置 mention 用户列表
   * @param peerIdList mention peer id list
   */
  public void setMentionList(List<String> peerIdList) {
    this.mentionList = peerIdList;
  }

  /**
   * 获取 mention 用户列表
   * @return
   */
  public List<String> getMentionList() {
    return this.mentionList;
  }

  /**
   * 获取 mention 用户列表的字符串（逗号分隔）
   * @return
   */
  public String getMentionListString() {
    if (null == this.mentionList) {
      return "";
    }
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < this.mentionList.size(); i++) {
      builder.append(this.mentionList.get(i));
      if (i != this.mentionList.size() - 1) {
        builder.append(",");
      }
    }
    return builder.toString();
  }

  /**
   * 设置 mention 用户列表字符串（逗号分隔），功能与 #setMentionList(List<String> peerIdList) 相同，两者调用一个即可。
   * @param content
   */
  public void setMentionListString(String content) {
    if (AVUtils.isBlankString(content)) {
      this.mentionList = null;
    } else {
      String[] peerIdArray = content.split(",");
      this.mentionList = new ArrayList<>(peerIdArray.length);
      this.mentionList.addAll(Arrays.asList(peerIdArray));
    }
  }

  /**
   * 判断是否 mention 了所有人
   * @return
   */
  public boolean isMentionAll() {
    return mentionAll;
  }

  /**
   * 设置是否 mention 所有人
   * @param mentionAll
   */
  public void setMentionAll(boolean mentionAll) {
    this.mentionAll = mentionAll;
  }

  void setCurrentClient(String clientId) {
    this.currentClient = clientId;
  }

  String conversationId;
  String content;
  String from;
  long timestamp;
  long deliveredAt;
  long readAt;
  long updateAt;

  List<String> mentionList = null;
  boolean mentionAll = false;
  String currentClient = null;

  String messageId;
  String uniqueToken;

  AVIMMessageStatus status;
  AVIMMessageIOType ioType;

  @Override
  public void writeToParcel(Parcel out, int flags) {
    out.writeString(conversationId);
    out.writeString(this.getContent());
    out.writeString(from);
    out.writeLong(timestamp);
    out.writeLong(deliveredAt);
    out.writeLong(readAt);
    out.writeLong(updateAt);
    out.writeString(messageId);
    out.writeInt(status.getStatusCode());
    out.writeInt(ioType.getIOType());
    out.writeString(uniqueToken);

    out.writeInt(mentionAll? 1: 0);
    out.writeString(getMentionListString());
  }

  public AVIMMessage(Parcel in) {
    this.conversationId = in.readString();
    this.setContent(in.readString());
    this.setFrom(in.readString());
    this.setTimestamp(in.readLong());
    this.setDeliveredAt(in.readLong());
    this.setReadAt(in.readLong());
    this.setUpdateAt(in.readLong());
    this.setMessageId(in.readString());
    this.status = AVIMMessageStatus.getMessageStatus(in.readInt());
    this.ioType = AVIMMessageIOType.getMessageIOType(in.readInt());
    this.uniqueToken = in.readString();

    int mentionFlag = in.readInt();
    this.mentionAll =  (mentionFlag == 1);
    String mentionLst = in.readString();
    setMentionListString(mentionLst);

    this.initMessage(in);
  }

  /**
   * 如果存在需要在Activity中间传递，但是又不需要发送到服务器端的数据
   * 则可以在此处进行反序列化
   *
   * @param in
   */
  protected void initMessage(Parcel in) {

  }

  protected synchronized void generateUniqueToken() {
    if (AVUtils.isBlankString(uniqueToken)) {
      uniqueToken = UUID.randomUUID().toString();
    }
  }

  void setUniqueToken(String uniqueToken) {
    this.uniqueToken = uniqueToken;
  }

  protected String getUniqueToken() {
    return uniqueToken;
  }


  public static final AVIMMessageCreator<AVIMMessage> CREATOR = new AVIMMessageCreator(AVIMMessage.class);

  /**
   * Created by lbt05 on 3/17/15.
   * <p/>
   * 用于标注AVIMMessage现在所处的状态
   * 主要用于UI标注等用户
   */
  public enum AVIMMessageStatus {
    AVIMMessageStatusNone(0), AVIMMessageStatusSending(1), AVIMMessageStatusSent(2),
    AVIMMessageStatusReceipt(3), AVIMMessageStatusFailed(4), AVIMMessageStatusRecalled(5);
    int statusCode;

    AVIMMessageStatus(int status) {
      this.statusCode = status;
    }

    public int getStatusCode() {
      return statusCode;
    }

    public static AVIMMessageStatus getMessageStatus(int statusCode) {
      switch (statusCode) {
        case 0:
          return AVIMMessageStatusNone;
        case 1:
          return AVIMMessageStatusSending;
        case 2:
          return AVIMMessageStatusSent;
        case 3:
          return AVIMMessageStatusReceipt;
        case 4:
          return AVIMMessageStatusFailed;
        case 5:
          return AVIMMessageStatusRecalled;
        default:
          return null;
      }
    }
  }


  public enum AVIMMessageIOType {
    /**
     * 标记收到的消息
     */
    AVIMMessageIOTypeIn(1),
    /**
     * 标记发送的消息
     */
    AVIMMessageIOTypeOut(2);
    int ioType;

    AVIMMessageIOType(int type) {
      this.ioType = type;
    }

    public int getIOType() {
      return ioType;
    }

    public static AVIMMessageIOType getMessageIOType(int type) {
      switch (type) {
        case 1:
          return AVIMMessageIOTypeIn;
        case 2:
          return AVIMMessageIOTypeOut;
      }
      return AVIMMessageIOTypeOut;
    }
  }
}
