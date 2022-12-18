package com.disda.cowork.mailtask;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;

import com.disda.cowork.po.Employee;
import com.disda.cowork.po.MailConstants;
import com.disda.cowork.po.MailLog;
import com.disda.cowork.service.IEmployeeService;
import com.disda.cowork.service.IMailLogService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 消息落库
 *
 * 邮箱发送定时任务,开启确认回到和失败回调进行相应处理
 * (消费端 rabbitmq幂等性)
 */
@Component
public class MailTask {

    @Autowired
    private IMailLogService mailLogService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 邮件发送定时任务
     * 10秒执行一次
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void mailTask(){
        //从数据库中查询所有tryTime（重试时间）小于当前时间的数据
        List<MailLog> list = mailLogService.list(new QueryWrapper<MailLog>()
                .eq("status", MailConstants.DELIVERING)
                .lt("tryTime", LocalDateTime.now()));

        list.forEach(mailLog -> {
            //如果重试重试超过三次，更新状态为2，投递失败,不在投递
            if (MailConstants.MAX_TRY_COUNT<=mailLog.getCount()){
                mailLogService.update(new UpdateWrapper<MailLog>()
                        .set("status",MailConstants.FAILURE)
                        .eq("msgId",mailLog.getMsgId()));
            }
            //如果重试重试没有超过三次,重试次数+1，更新更新时间，更新重试时间
            mailLogService.update(new UpdateWrapper<MailLog>()
                    .set("count",mailLog.getCount()+1)
                    .set("updateTime",LocalDateTime.now())
                    .set("tryTime",LocalDateTime.now().plusMinutes(MailConstants.MSG_TIMEOUT)));

            //通过Eid获取用户信息
            Employee employee = employeeService.getAllEmployee(mailLog.getEid()).get(0);
            //发送消息
            rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME,
                    MailConstants.MAIL_ROUTING_KEY_NAME,employee,new CorrelationData(mailLog.getMsgId()));
        });

    }

}
