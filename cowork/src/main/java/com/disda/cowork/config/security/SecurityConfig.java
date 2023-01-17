package com.disda.cowork.config.security;


import com.disda.cowork.config.security.components.*;
import com.disda.cowork.po.Admin;
import com.disda.cowork.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security配置类
 * @author Disda
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    LoginFailureHandler loginFailureHandler;

    @Autowired
    LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private IAdminService adminService;

    @Autowired
    private RestAuthorizationEntryPoint restAuthorizationEntryPoint;

    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;

    @Autowired
    private CustomUrlDecisionManager customUrlDecisionManager;

    @Autowired
    private CustomFilter customFilter;

    @Override
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    //以后security会走自己写的userDetailsService和passwordEncoder
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    //可以放走一些路径，不走拦截链
    @Override
    public void configure(WebSecurity web) throws Exception {
        /**
         * regexMatchers()
         * regexMatchers(httpmethod.post/get,正则表达式)
         *
         * mvcMatchers()
         * 需要 /demo/hello 才能访问
         * .mvcMatchers("/hello").servletPath("/demo").permitAll()
         * 等效 antMatchers("/demo/hello").permitAll()
         */
        web.ignoring().antMatchers(
                "/login",
                "/actuator/**",
                "/forgot",
                "/register/**",
                "/getSalt",
                "/loginC",
                "/logout",
                "/css/**",
                "/js/**",
                "/index.html",
                "favicon.ico",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources/**",
                "/v2/api-docs/**",
                "/captcha",
                "/ws/**"
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //使用JWT，不需要csrf，关闭csrf防范，因为前后端分离我们不需要cookie防止跨域请求了
        http
                .csrf().disable()

                //使用springboot自带的登录逻辑，不够灵活
                .formLogin()
                .loginProcessingUrl("/test/login")
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)

                .and()
                //基于token，不需要session
                .sessionManagement()
                // 无状态，不用session保存了
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)


                .and()
                //授权认证
                .authorizeRequests()
                //所有请求都要求认证,必须登录后被访问
                .anyRequest().authenticated()
                //动态权限配置
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setAccessDecisionManager(customUrlDecisionManager);
                        object.setSecurityMetadataSource(customFilter);
                        return object;
                    }
                })

                .and()
                //禁用缓存
                .headers()
                .cacheControl();

        //添加jwt 登录授权过滤器
        // 不使用formlogin就不存在UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthencationTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        //异常处理
        // 添加自定义未授权和未登录结果返回
        http.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthorizationEntryPoint);

    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        /**
         * 重写了userDetailsService中根据用户名获取用户信息的方法loadByUserName
         * 实现类也可以写在Service层里面
         *
         */
        return  username -> {
            Admin admin = adminService.getAdminByUserName(username);
            if (null!=admin){
                admin.setRoles(adminService.getRoles(admin.getId()));
                return admin;
            }
            throw new UsernameNotFoundException("用户名或密码不正确");
        };
    }

    /**
     * 使用BCryptPasswordEncoder做为我们的密码加密工具
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 注入我们自定义的JWT令牌过滤器
     * @return
     */
    @Bean
    public JwtAuthenticationTokenFilter jwtAuthencationTokenFilter(){
        return new JwtAuthenticationTokenFilter();
    }

}
