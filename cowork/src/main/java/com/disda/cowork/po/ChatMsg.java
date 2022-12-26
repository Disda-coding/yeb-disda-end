package com.disda.cowork.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * webSocket聊天
 * 消息
 */
@Data
@EqualsAndHashCode(callSuper = false)//自动的给model bean实现equals方法和hashcode方法
//@EqualsAndHashCode(callSuper = true) 用自己的属性和从父类继承的属性 来生成hashcode
@Accessors(chain = true)//开启链式编程
@ToString
public class ChatMsg {
    /**
     * 谁发的
     */
    private String from;

    /**
     * 发到哪里去
     */
    private String to;

    /**
     * 对应的内容
     */
    private String content;

    /**
     * 时间
     */
    private LocalDateTime date;

    /**
     * 发送者昵称
     */
    private String fromNickName;

}
