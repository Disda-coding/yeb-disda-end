package com.disda.cowork.po;

/**
 * 消息状态
 *
 * 消息落库
 */
public class MailConstants {
    /**
     * 消息投递中
     */
    public static final Integer DELIVERING = 0;

    /**
     * 消息投递成功
     */
    public static final Integer SUCCESS = 1;

    /**
     * 消息投递失败
     */
    public static final Integer FAILURE = 2;

    /**
     * 最大重试次数
     */
    public static final Integer MAX_TRY_COUNT = 3;

    /**
     * 消息超时时间(分钟)
     */
    public static final Integer MSG_TIMEOUT = 1;

    /**
     * 队列名称
     */
    public static final String MAIL_QUEUE_NAME = "yeb-email-queue";

    /**
     * 交换机名称
     */
    public static final String MAIL_EXCHANGE_NAME = "yeb-email-exchange";

    /**
     * 路由key
     */
    public static final String MAIL_ROUTING_KEY_NAME = "yeb-email";

}
