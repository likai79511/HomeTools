package com.avos.avoscloud.im.v2;

import android.util.Pair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.avos.avospush.util.FieldAttribute;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AVIMTypedMessage extends AVIMMessage {

  private static final String KEY_MESSAGE_ID = "msg_mid";
  private static final String KEY_MESSAGE_FROM = "msg_from";
  private static final String KEY_MESSAGE_TIMESTAMP = "msg_timestamp";
  private static final String KEY_MESSAGE_CONTENT = "msg";

  private int messageType;
  static ConcurrentHashMap<Class<? extends AVIMTypedMessage>, Map<String, FieldAttribute>> fieldCache =
      new ConcurrentHashMap<Class<? extends AVIMTypedMessage>, Map<String, FieldAttribute>>();

  public AVIMTypedMessage() {
    super();
    initMessageType();
  }

  private void initMessageType() {
    AVIMMessageType type = this.getClass().getAnnotation(AVIMMessageType.class);
    messageType = type.type();
  }

  public int getMessageType() {
    return messageType;
  }

  protected void setMessageType(int messageType) {
    this.messageType = messageType;
  }

  @Override
  public final String getContent() {
    JSONObject json = new JSONObject();
    json.put("_lctype", this.getMessageType());
    if (!fieldCache.contains(this.getClass())) {
      computeFieldAttribute(this.getClass());
    }
    Map<String, FieldAttribute> classFieldAttributesMap = fieldCache.get(this.getClass());
    for (FieldAttribute fieldAttribute : classFieldAttributesMap.values()) {
      Object fieldValue = fieldAttribute.get(this);
      json.put(fieldAttribute.getAliaName(), fieldValue);
    }
    return json.toJSONString();
  }

  @Override
  public final void setContent(String content) {
    Map<String, Object> contentMap = JSONObject.parseObject(content, Map.class);
    if (!fieldCache.contains(this.getClass())) {
      computeFieldAttribute(this.getClass());
    }
    Map<String, FieldAttribute> classFieldAttributesMap = fieldCache.get(this.getClass());
    for (FieldAttribute fieldAttribute : classFieldAttributesMap.values()) {
      Object value = contentMap.get(fieldAttribute.getAliaName());
      if (value instanceof Map && fieldAttribute.getFieldType() != null) {
        value = JSON.parseObject(JSON.toJSONString(value), fieldAttribute.getFieldType());
      }
      fieldAttribute.set(this, value);
    }
  }

  protected static void computeFieldAttribute(Class clazz) {
    HashMap<String, FieldAttribute> fieldAttributeMap = new HashMap<String, FieldAttribute>();
    Class tmpClazz = clazz;
    List<Pair<Method[], Class>> methodsClassPairs = new LinkedList<Pair<Method[], Class>>();
    int length = 0;
    while (tmpClazz != null && tmpClazz != Object.class) {
      Method[] declaredFields = tmpClazz.getDeclaredMethods();
      methodsClassPairs.add(Pair.create(declaredFields, tmpClazz));
      tmpClazz = tmpClazz.getSuperclass();
    }
    // 从父类先开始遍历，子类的属性或者方法以覆盖父类的
    Collections.reverse(methodsClassPairs);
    for (Pair<Method[], Class> pair : methodsClassPairs) {
      Class currentClazz = pair.second;
      Method[] currentClassMethods = pair.first;
      for (Method method : currentClassMethods) {
        String methodName = method.getName();
        if (methodName.length() < 3) {
          continue;
        }

        if (Modifier.isStatic(method.getModifiers())) {
          continue;
        }

        // support builder set
        if (method.getReturnType().equals(clazz)) {
          continue;
        }

        if (method.getParameterTypes().length != 1 && method.getParameterTypes().length != 0) {
          continue;
        }

        if (!(methodName.startsWith("set") || (!(method.getName().equals("getMetaClass")
            && method.getReturnType().getName().equals("groovy.lang.MetaClass")))
            || (methodName.startsWith("get") || methodName.startsWith("is")))) {
          continue;
        }
        String propertyName = null;
        Field field;
        boolean isSetterMethod = methodName.startsWith("set");
        boolean isGetterMethod = methodName.startsWith("get");
        boolean isBooleanGetterMethod = methodName.startsWith("is");
        if (isSetterMethod) {
          char c3 = methodName.charAt(3);

          if (Character.isUpperCase(c3)) {
            propertyName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
          } else if (c3 == '_') {
            propertyName = methodName.substring(4);
          } else if (c3 == 'f') {
            propertyName = methodName.substring(3);
          } else {
            continue;
          }


        } else if (isGetterMethod) {
          if (methodName.length() < 4) {
            continue;
          }

          if (methodName.equals("getClass")) {
            continue;
          }

          char c3 = methodName.charAt(3);

          if (Character.isUpperCase(c3)) {
            propertyName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
          } else if (c3 == '_') {
            propertyName = methodName.substring(4);
          } else if (c3 == 'f') {
            propertyName = methodName.substring(3);
          } else {
            continue;
          }
        } else if (isBooleanGetterMethod) {
          if (methodName.length() < 3) {
            continue;
          }
          char c2 = methodName.charAt(2);

          if (Character.isUpperCase(c2)) {
            propertyName = Character.toLowerCase(methodName.charAt(2)) + methodName.substring(3);
          } else if (c2 == '_') {
            propertyName = methodName.substring(3);
          } else if (c2 == 'f') {
            propertyName = methodName.substring(2);
          } else {
            continue;
          }
        }
        field = getField(currentClazz, propertyName);
        if (field == null && (isBooleanGetterMethod || isSetterMethod)) {
          String isFieldName =
              "is" + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
          field = getField(currentClazz, isFieldName);
          if (field != null) {
            propertyName = isFieldName;
          }
        }

        if (field != null) {
          AVIMMessageField messageField = field.getAnnotation(AVIMMessageField.class);

          if (messageField != null) {
            String annotatedName = messageField.name();
            FieldAttribute fieldAttribute = null;
            if (fieldAttributeMap.containsKey(propertyName)) {
              fieldAttribute = fieldAttributeMap.get(propertyName);
            }
            if (isSetterMethod) {
              if (fieldAttribute == null) {
                fieldAttribute =
                    new FieldAttribute(propertyName, annotatedName, null, method, field.getType());
                fieldAttributeMap.put(propertyName, fieldAttribute);
              } else {
                fieldAttribute.setSetterMethod(method);
              }
            } else if (isBooleanGetterMethod || isGetterMethod) {
              if (fieldAttribute == null) {
                fieldAttribute =
                    new FieldAttribute(propertyName, annotatedName, method, null, field.getType());
                fieldAttributeMap.put(propertyName, fieldAttribute);
              } else {
                fieldAttribute.setGetterMethod(method);
              }
            }
            method.setAccessible(true);
          }
        }
      }
    }
    fieldCache.put(clazz, fieldAttributeMap);
  }

  private static Field getField(Class<?> clazz, String fieldName) {
    try {
      return clazz.getDeclaredField(fieldName);
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * 从 conversation 数据中解析 lastMessage
   * @return
   */
  static AVIMMessage parseMessage(String conversationId, JSONObject jsonObject) {
    if (null != jsonObject && jsonObject.containsKey(KEY_MESSAGE_ID)) {
      try {
        String from = jsonObject.getString(KEY_MESSAGE_FROM);
        String data = jsonObject.getString(KEY_MESSAGE_CONTENT);
        long timestamp = jsonObject.getLong(KEY_MESSAGE_TIMESTAMP);
        String msgId = jsonObject.getString(KEY_MESSAGE_ID);
        AVIMMessage message = new AVIMMessage(conversationId, from, timestamp, -1);
        message.setMessageId(msgId);
        message.setContent(data);
        return AVIMMessageManagerHelper.parseTypedMessage(message);
      } catch (Exception e) {}
    }
    return null;
  }

  public static AVIMMessage getMessage(String cid, String mId, String data, String from, long timestamp, long ackAt, long readAt) {
    AVIMMessage message = new AVIMMessage(cid, from, timestamp, ackAt, readAt);
    message.setMessageId(mId);
    message.setContent(data);
    return AVIMMessageManagerHelper.parseTypedMessage(message);
  }
}
