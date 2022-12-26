package com.disda.cowork.controller;

import com.disda.cowork.po.Admin;
import com.disda.cowork.po.ChatMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

/**
 * webSocket
 */
@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    /**
     * Authentication authentication,这个全局对象不是从登陆时获取的，
     * 是WebSocketConfig.configureClientInboundChannel中从前端拿到token，
     * 重新设置的全局对象
     * 
     * @param authentication
     * @param chatMsg
     */
    @MessageMapping("/ws/chat")
    public void handleMsg(Authentication authentication, ChatMsg chatMsg){
        Admin admin = (Admin)authentication.getPrincipal();
        chatMsg.setFrom(admin.getUsername());
        chatMsg.setFromNickName(admin.getName());
        chatMsg.setDate(LocalDateTime.now());

        simpMessagingTemplate.convertAndSendToUser(chatMsg.getTo(),"/queue/chat",chatMsg);
    }

}
