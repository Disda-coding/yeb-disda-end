package com.disda.cowork.config.security.components;

import io.jsonwebtoken.lang.Collections;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 权限控制
 * 判断用户角色
 * 判断用户角色是否可以访问其想访问的url资源
 */
@Component
public class CustomUrlDecisionManager implements AccessDecisionManager {

    /**
     * Authentication authentication
     * 认证之后的身份
     *
     * Object o
     * Object对象这里是要访问的受保护资源，他是一个FilterInvocation类型,你可以通过这个对象获取当前所访问的路径
     *
     * Collection<ConfigAttribute> configAttributes
     * Collection集合就是要操作当前资源，所需要的角色。
     *
     * https://blog.csdn.net/weixin_43836204/article/details/106310730
     */
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        for (ConfigAttribute attribute : configAttributes) {
            //当前url资源所需角色
            String needRole = attribute.getAttribute();
            //判断用户角色是否为url所需角色
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals(needRole)) {
                    return;
                }
            }
            //判断角色是否是登录即可访问的角色，此角色在CustomFilter中设置
            if ("ROLE_LOGIN".equals(needRole)) {
                //判断是否登录
                if (authentication instanceof AnonymousAuthenticationToken) {
                    throw new AccessDeniedException("尚未登录，请登录");
                } else {
                    return;
                }
            }
        }
        throw new AccessDeniedException("权限不足，请联系管理员");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return false;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
