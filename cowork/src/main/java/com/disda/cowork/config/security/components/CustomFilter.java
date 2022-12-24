package com.disda.cowork.config.security.components;

import com.disda.cowork.po.Menu;
import com.disda.cowork.po.Role;
import com.disda.cowork.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

/**
 * 菜单权限控制
 * 根据请求url分析请求所需的角色
 * 访问该url资源所需角色
 */
@Component
public class CustomFilter implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private IMenuService menuService;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 根据request请求获取访问资源所需权限
     * 用户GrantedAuthority与ConfigAttribute一对比就知道用户有没有权限访问该api了
     * @param o
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        //获取请求的url
        String requestUrl = ((FilterInvocation) o).getRequestUrl();
        List<Menu> menus = menuService.getMenusWithRole();
        for (Menu menu : menus) {
            //判断请求url与菜单角色是否匹配
            if (antPathMatcher.match(menu.getUrl(),requestUrl)) {
                //得到rname，并将其放入一个string集合中,一个url可能对应多个角色
                String[] str = menu.getRoles().stream().map(Role::getName).toArray(String[]::new);
                //将得到的角色放入List里
                return SecurityConfig.createList(str);
            }
        }
        //给一个默认登录即可访问角色
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
