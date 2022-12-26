package com.disda.cowork.config;


import com.disda.cowork.config.security.components.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * webSocket配置类
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 添加这个Endpoints，这样在网页可以通过websocket连接上服务
     * 也就是我们配置websocket的服务地址，并且可以指定是否使用socketJS
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        /**
         * 1.将 "/wc/ep" 路径注册为stomp的端点，用户连接这个端点就可以进行webSocket通讯
         * 2.setAllowedOrigins("*")：允许跨域
         * 3.withSockJS()：支持socketJS访问
         */
        registry.addEndpoint("/ws/ep").setAllowedOriginPatterns("*").withSockJS();
    }

    /**
     * 输入通道参数配置
     *
     * 如果没用jwt，可以不配置，用了jwt，必须配置，
     * 获取令牌，做相应处理，是否会被JwtAuthencationTokenFilter(jwt 登录授权过滤器)拦截
     * @param registration
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            //预发送
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                /**
                 * Accessor 访问器
                 * 拿到 accessor 判断是否是链接，如果是要获取对应token,并且设置用户对象
                 */
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                //判断是否是链接
                if (StompCommand.CONNECT.equals(accessor.getCommand())){
                    //"Auth-Token" 前端传的
                    String token = accessor.getFirstNativeHeader("Auth-Token");
                    //判断token中是否存在
                    if (StringUtils.hasLength(token)){
                        String authToken = token.substring(tokenHead.length()).trim();
                        String username = jwtTokenUtil.getUserNameFromToken(authToken);
                        //判断token中是否存在用户名
                        if (StringUtils.hasLength(username)){
                            //登录
                            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                            //判断token是否有效,重新设置用户对象
                            if (jwtTokenUtil.validateToken(authToken,userDetails)){
                                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                                accessor.setUser(authenticationToken);
                            }
                        }
                    }

                }
                return message;
            }
        });
    }

    /**
     * 配置消息代理
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //配置代理域，可以配置多个，配置代理目的的前缀为/queue，可以再配置域上想客户端推送消息
        registry.enableSimpleBroker("/queue");
    }
}
