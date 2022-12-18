package com.disda.mail;

import com.disda.cowork.po.Employee;
import com.rabbitmq.client.Channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;

/**
 * 消息接受者
 *
 * 接受来自yeb-server发送过来的员工消息
 *
 * rabbitmq幂等性
 * (生产者端 消息落库)
 * 消费成功后在redis中存入msgId，下次再消费时，拿msgId判断
 */
@Component

public class MailReceiver {
    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(MailReceiver.class);

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    //邮件配置
    private MailProperties mailProperties;
    @Autowired
    //模板引擎
    private TemplateEngine templateEngine;

    @Autowired
    private RedisTemplate redisTemplate;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "yeb-email-queue",durable = "true",autoDelete = "false"),
            //交换机默认类型DIRECT，type = ExchangeTypes.DIRECT
            exchange = @Exchange(value = "yeb-email-exchange"),
            key = "yeb-email"
    ))
    @RabbitHandler
    public void welcomeHandler(Message message, Channel channel){
        Employee employee = (Employee)message.getPayload();
        MessageHeaders headers = message.getHeaders();
        //消息序号，手动确认时返回
        Long tag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);
        //拿到UUID，msgId
        String msgId = (String)headers.get("spring_returned_message_correlation");

        //自己准备redis的key，hash的key为tag，hash的value无所谓
        HashOperations hash = redisTemplate.opsForHash();
        try {
            //消费前判断该消息是否已被消费
            if (hash.entries("mail_log").containsKey(msgId)){
                LOGGER.error("消息已被消费==========>",tag);
                /**
                 * 手动确认消息
                 * tag:消息序号
                 * multiple:是否确认多条消息
                 */
                channel.basicAck(tag,false);
                return;
            }

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            //发件人(本人，公司邮箱)
            helper.setFrom(mailProperties.getUsername());
            //收件人(发给员工)
            helper.setTo(employee.getEmail());
            //主题
            helper.setSubject("入职欢迎邮件");
            //发送日期
            helper.setSentDate(new Date());
            //邮件内容
            Context context = new Context();
            context.setVariable("name",employee.getName());
            context.setVariable("posName",employee.getPosition().getName());
            context.setVariable("joblevelName",employee.getJoblevel().getName());
            context.setVariable("departmentName",employee.getDepartment().getName());

            String mail = templateEngine.process("welcomeMail", context);
            helper.setText(mail,true);

            //发送邮件
            javaMailSender.send(mimeMessage);

            LOGGER.info("邮件发送成功");
            //将msgId存入redis中
            hash.put("mail_log",msgId,"ok");
            //手动确认消息
            channel.basicAck(tag,false);
        } catch (Exception e) {
            try {
                /**
                 * 手动确认消息
                 * tag:消息序号
                 * multiple:是否确认多条消息
                 * requeue:是否退回到队列
                 */
                channel.basicNack(tag,false,true);
            } catch (IOException ioException) {
                LOGGER.error("邮件发送失败====>",e.getMessage());
            }
            LOGGER.error("邮件发送失败====>",e.getMessage());
        }

    }


}
