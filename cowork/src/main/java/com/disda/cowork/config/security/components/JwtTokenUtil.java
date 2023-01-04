package com.disda.cowork.config.security.components;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JwtToken工具类
 */
@Component
public class JwtTokenUtil {

    private static final String CLAIM_KEY_USERNAME="sub";
    private static final String CLAIM_KEY_CREATED="created";

    //JWT 加解密使用的密钥
    @Value("${jwt.secret}")
    private String secret;

    //JWT的超期限时间（60*60*24）
    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.deadline}")
    private Long deadline;


    /**
     * 根据用户信息生成token
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>(2);
        // 荷载放入用户名
        claims.put(CLAIM_KEY_USERNAME,userDetails.getUsername());
        // 获取当前时间
        claims.put(CLAIM_KEY_CREATED,new Date());
        return generateToken(claims);
    }

    /**
     * 从token中获取登录用户名
     * @param token
     * @return
     */
    public String getUserNameFromToken(String token){
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 判断token是否有效
     * 名字 时间
     * @param token
     * @param userDetails
     * @return
     */
    public boolean validateToken(String token,UserDetails userDetails){
        String username = getUserNameFromToken(token);
        //判断从token中取的username是否与userDetails中的一致，并且判断token是否失效
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * 判断token是否可以被刷新
     * 如果已经失效(isTokenExpired返回false),则刷新
     * @param token
     * @return
     */
    public boolean canRefresh(String token){
        //获取过期时间
        if (isTokenExpired(token)) {
            return false;
        }
        Date expireDate = getExpiredDateFormToken(token);
        Date now = new Date();
        Long diff =now.getTime() - expireDate.getTime();
        long diffSeconds = diff / 1000 % 60;
        return diffSeconds<=deadline;
    }

    /**
     * 刷新过期时间，刷新token
     * 只更新了时间
     * @param token
     * @return
     */
    public String refreshToken(String token){
        Claims claims = getClaimsFromToken(token);
        claims.put(CLAIM_KEY_CREATED,new Date());
        return generateToken(claims);
    }

    /**
     * 判断token时间是否失效
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token) {
        //获取过期时间
        Date expireDate = getExpiredDateFormToken(token);
        //before 之前，判断当前时间是否是获取的时间之前(未过期)
        return expireDate.before(new Date());
    }

    /**
     * 从token中获取过期时间
     * @param token
     * @return
     */
    private Date getExpiredDateFormToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }


    /**
     * 从token中获取荷载
     * @param token
     * @return
     */
    private Claims getClaimsFromToken(String token){
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    //签名
                    .setSigningKey(secret)
                    //需要转荷载的token
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claims;
    }

    /**
     * 根据荷载生成JWT token
     * @param claims
     * @return
     */
    private String generateToken(Map<String, Object> claims){
        return Jwts.builder()
                //荷载
                .setClaims(claims)
                //失效时间
                .setExpiration(generateExpirationDate())
                //签名,加密方式
                .signWith(SignatureAlgorithm.HS512,secret)
                .compact();
    }

    /**
     * 生成token失效时间
     * @return
     */
    private Date generateExpirationDate() {
        //当前系统时间 + 配置的失效时间
        return new Date(System.currentTimeMillis()+expiration * 1000);
    }

}