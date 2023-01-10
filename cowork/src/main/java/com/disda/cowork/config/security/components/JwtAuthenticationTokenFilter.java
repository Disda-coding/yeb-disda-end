package com.disda.cowork.config.security.components;



import com.disda.cowork.dto.RespBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: cowork-back
 * @description: jwt 登录授权过滤器,继承OncePerRequestFilter，使得每次请求只拦截一次
 * @author: disda
 * @create: 2022-01-24 14:52
 */
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 返回刷新后的token
     * @param httpServletResponse
     * @param tokenMap
     * @throws IOException
     */
    public void refreshToken(HttpServletResponse httpServletResponse, Map<String, String> tokenMap) throws IOException {
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");
        PrintWriter out = httpServletResponse.getWriter();
        RespBean bean = RespBean.success(tokenMap);
        bean.setCode(666);
        out.write(new ObjectMapper().writeValueAsString(bean));
        out.flush();
        out.close();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        /**
         * 浏览器传过来的request请求中
         * key value
         * key为配置中的tokenHeader
         * value为配置中的tokenHead+空格+token
         */

        String authHeader = request.getHeader(tokenHeader);
        // 如果存在token
        if (authHeader != null && authHeader.startsWith(tokenHead)) {
            String authToken = authHeader.substring(tokenHead.length());
            String username = jwtTokenUtil.getUserNameFromToken(authToken);
            // token存在用户名
            if (username!=null){
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                // 如果是合法的用户，且用户令牌快到ddl了
                if (username.equals(userDetails.getUsername()) && jwtTokenUtil.canRefresh(authToken)){
                    log.info("用户{}刷新了jwt,原jwt为{}",username,authToken);
                    //封装返回信息
                    Map<String, String> tokenMap = new HashMap<>(2);
                    String token = jwtTokenUtil.refreshToken(authToken);
                    //将token放入返回信息中
                    tokenMap.put("token", token);
                    //将token头放入返回信息中
                    tokenMap.put("tokenHead", tokenHead);
                    refreshToken(response,tokenMap);
                    // 不走拦截链了，但需要前端重发请求。
                    return;
                }
                // 如果用户未登录，且token不需要刷新
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    //  加入Redis作用可以踢出用户和加快每次请求速度
                    // UserDetails userDetails= (UserDetails) redisTemplate.opsForValue().get("login_"+username);
                    // 验证Token是否有效，重新设置用户对象
                    if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    }
                }

            }


        }
        filterChain.doFilter(request, response);
    }
}
