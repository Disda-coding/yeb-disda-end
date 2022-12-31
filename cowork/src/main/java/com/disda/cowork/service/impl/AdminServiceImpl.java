package com.disda.cowork.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.disda.cowork.config.security.components.JwtTokenUtil;
import com.disda.cowork.dto.RespBean;
import com.disda.cowork.error.BusinessException;
import com.disda.cowork.error.EmBusinessError;
import com.disda.cowork.mapper.AdminMapper;
import com.disda.cowork.mapper.AdminRoleMapper;
import com.disda.cowork.mapper.RoleMapper;
import com.disda.cowork.po.Admin;
import com.disda.cowork.po.AdminRole;
import com.disda.cowork.po.Role;
import com.disda.cowork.service.IAdminService;
import com.disda.cowork.utils.AdminUtils;
import com.disda.cowork.utils.AesUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author disda
 * @since 2022-01-24
 */
@Service
@Slf4j
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {
    // 注册用户的时候提供默认密码选项
    @Value("${default.password}")
    String defaultPassword;
    @Value("${default.userFace}")
    String userFace;

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AdminRoleMapper adminRoleMapper;

    /**
     * 登录后返回token
     *
     * @param username
     * @param password
     * @param request
     * @return
     */
    @Override
    public Map<String, String> login(String username, String password, HttpServletRequest request) throws Exception {
        //根据用户名从数据库中获取用户信息
        // 因为我们重写了userDetailService中的loaduserbyusername的方法，如果直接引用会造成循环依赖问题
        Admin admin = getAdminByUserName(username);
        admin.setRoles(getRoles(admin.getId()));
        UserDetails userDetails = admin;
        //如果userDetails为空 或 密码不匹配
        password = AesUtils.decrypt(password, (String) redisTemplate.opsForValue().get("salt_" + username));
        if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        //判断用户账号是否被禁用
        if (!userDetails.isEnabled()) {
            throw new BusinessException(EmBusinessError.USER_LOCKED);
        }
        //更新security登录用户对象
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        //放入security全局中（单机部署情况下，可以放入redis中）
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //生成token
        String token = jwtTokenUtil.generateToken(userDetails);
        //封装返回信息
        Map<String, String> tokenMap = new HashMap<>(2);
        //将token放入返回信息中
        tokenMap.put("token", token);
        //将token头放入返回信息中
        tokenMap.put("tokenHead", tokenHead);
        //将token返回给前端
        return tokenMap;
    }

    @Override
    public Admin getAdminByUserName(String username) {
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", username).eq("enabled", true));
    }

    @Override
    public Boolean getExistUserByUserName(String username) {
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", username)) != null;
    }

    @Override
    public Map<String, String> login(String username, String password, String code, HttpServletRequest request) throws Exception {
        String captcha = (String) request.getSession().getAttribute("captcha");

        log.debug("captcha: "+captcha);
        if (!StringUtils.hasText(code) || !captcha.equalsIgnoreCase(code)) {
            // 验证码错误
            throw new BusinessException(EmBusinessError.CAPTCHA_ERROR);
        }
        //防止暴力破解
        request.getSession().removeAttribute("captcha");
        return login(username, password, request);
    }

    /**
     * 根据用户id查找角色列表
     *
     * @param adminId
     * @return
     */
    @Override
    public List<Role> getRoles(Integer adminId) {
        return roleMapper.getRoles(adminId);
    }

    /**
     * 获取所有操作员
     *
     * @param keywords
     * @return
     */
    @Override
    public List<Admin> getAllAdmins(String keywords) {
        //获取当前操作员
        Integer id = AdminUtils.getCurrentAdmin().getId();
        return adminMapper.getAllAdmins(id, keywords);
    }

    /**
     * 更新操作员角色
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor=Exception.class)
    public boolean updateAdminRole(Integer adminId, Integer[] rids) {
        //通过adminId删除原id角色
        adminRoleMapper.delete(new QueryWrapper<AdminRole>().eq("adminId", adminId));
        Integer result = adminRoleMapper.updateAdminRole(adminId, rids);
        if (rids.length == result) {
            return true;
        }
        return false;
    }

    /**
     * 更新用户密码
     *
     * @param oldPass
     * @param pass
     * @param adminId
     * @return
     */
    @Override
    public boolean updateAdminPassword(String oldPass, String pass, Integer adminId) {
        Admin admin = adminMapper.selectById(adminId);
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //比较输入的旧密码是否正确
        if (passwordEncoder.matches(oldPass, admin.getPassword())) {
            //设置新密码
            admin.setPassword(passwordEncoder.encode(pass));
            int result = adminMapper.updateById(admin);
            if (1 == result) {
                return true;
            }
        }
        return false;
    }

    /**
     * 更新用户头像
     *
     * @param url
     * @param adminId
     * @param authentication
     * @return
     */
    @Override
    public boolean updateAdminUserFace(String url, Integer adminId, Authentication authentication) {
        Admin admin = adminMapper.selectById(adminId);
        admin.setUserFace(url);
        int result = adminMapper.updateById(admin);
        //更新成功
        if (1 == result) {
            //更新全局对象
            Admin principal = (Admin) authentication.getPrincipal();
            principal.setUserFace(url);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            return true;
        }
        return false;
    }

    @Override
    public int insert(Admin admin) {
        if (admin.getPassword() == null || admin.getPassword().trim().length() == 0) {
            admin.setPassword(passwordEncoder.encode(defaultPassword));
        }
        if (admin.getUserFace() == null || admin.getUserFace().trim().length() == 0) {

        }
        int result = adminMapper.insert(admin);
        return result;
    }



}
