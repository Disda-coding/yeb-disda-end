package com.disda.cowork.config;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.disda.cowork.po.MailConstants;
import com.disda.cowork.po.MailLog;
import com.disda.cowork.service.IMailLogService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * rabbitmq配置类
 *
 * 用与消息落库，监听回调
 */
@Configuration
public class RabbitMqConfig {

    //打印日志
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqConfig.class);

    //RabbitTemplate使用CachingConnectionFactory作为连接工厂
    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;

    @Autowired
    private IMailLogService mailLogService;


    @Bean
    public RabbitTemplate rabbitTemplate(){


        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        //使用JSON序列化
//        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        /**
         * ConfirmCallback,消息发送成功回调
         * Data:消息唯一标识
         * ack:确认状态
         * cause:造成(失败)原因
         */
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData data, boolean ack, String cause) {
                String msgId = data.getId();
                if (ack){
                    LOGGER.info("{}========>消息发送成功",msgId);
                    //更新数据库状态
                    mailLogService.update(new UpdateWrapper<MailLog>().eq("msgId",msgId).set("status", MailConstants.SUCCESS));
                } else {
                    LOGGER.error("{}========>消息发送=失败",msgId);
                }
            }
        });

        /**
         * 消息失败回调
         * ReturnedMessage 封装以下参数
         *
         * message：消息主题
         * replyCode：响应码
         * replyText：响应描述
         * exchange：交换机
         * routingKey：路由键
         */
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returnedMessage) {
                LOGGER.error("{}========>消息发送=失败",returnedMessage.getMessage().getBody());
            }
        });

        return rabbitTemplate;
    }


}
