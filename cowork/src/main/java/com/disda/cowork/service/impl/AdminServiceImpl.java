package com.disda.cowork.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.disda.cowork.config.security.components.JwtTokenUtil;
import com.disda.cowork.mapper.AdminMapper;
import com.disda.cowork.mapper.AdminRoleMapper;
import com.disda.cowork.mapper.RoleMapper;
import com.disda.cowork.po.Admin;
import com.disda.cowork.po.AdminRole;
import com.disda.cowork.dto.RespBean;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    public RespBean login(String username, String password, HttpServletRequest request) throws Exception {
        //根据用户名从数据库中获取用户信息
        // 因为我们重写了userDetailService中的loaduserbyusername的方法，如果直接引用会造成循环依赖问题
        Admin admin = getAdminByUserName(username);
        admin.setRoles(getRoles(admin.getId()));
        UserDetails userDetails = admin;
        //如果userDetails为空 或 密码不匹配
        password = AesUtils.decrypt(password, (String) redisTemplate.opsForValue().get("salt_" + username));
//        log.info("pswd"+password);
        if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            return RespBean.error("用户名或密码错误");
        }
        //判断用户账号是否被禁用
        if (!userDetails.isEnabled()) {
            return RespBean.error("账号被禁用,请联系管理员");
        }


        //更新security登录用户对象
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        //放入security全局中（单机部署情况下，可以放入redis中）
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);


        //生成token
        String token = jwtTokenUtil.generateToken(userDetails);
        //封装返回信息
        Map<String, String> tokenMap = new HashMap<>();
        //将token放入返回信息中
        tokenMap.put("token", token);
        //将token头放入返回信息中
        tokenMap.put("tokenHead", tokenHead);
        //将token返回给前端
        return RespBean.success("登录成功", tokenMap);
    }

    @Override
    public Admin getAdminByUserName(String username) {
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", username).eq("enabled", true));
    }

    @Override
    public Boolean getExistUserByUserName(String username) {
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", username))!=null;
    }

    @Override
    public RespBean login(String username, String password, String code, HttpServletRequest request) throws Exception {
        String captcha = (String) request.getSession().getAttribute("captcha");

        log.error(captcha);
        if (!StringUtils.hasText(code) || !captcha.equalsIgnoreCase(code)) {
            return RespBean.error("验证码错误，请重新输入");
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
    @Transactional
    public RespBean updateAdminRole(Integer adminId, Integer[] rids) {
        //通过adminId删除原id角色
        adminRoleMapper.delete(new QueryWrapper<AdminRole>().eq("adminId", adminId));
        Integer result = adminRoleMapper.updateAdminRole(adminId, rids);
        if (rids.length == result) {
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
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
    public RespBean updateAdminPassword(String oldPass, String pass, Integer adminId) {
        Admin admin = adminMapper.selectById(adminId);
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //比较输入的旧密码是否正确
        if (passwordEncoder.matches(oldPass, admin.getPassword())) {
            //设置新密码
            admin.setPassword(passwordEncoder.encode(pass));
            int result = adminMapper.updateById(admin);
            if (1 == result) {
                return RespBean.success("更新成功");
            }
        }
        return RespBean.error("更新失败");
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
    public RespBean updateAdminUserFace(String url, Integer adminId, Authentication authentication) {
        Admin admin = adminMapper.selectById(adminId);
        admin.setUserFace(url);
        int result = adminMapper.updateById(admin);
        if (1 == result) {//更新成功
            //更新全局对象
            Admin principal = (Admin) authentication.getPrincipal();
            principal.setUserFace(url);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            return RespBean.success("更新成功", url);
        }
        return RespBean.error("更新失败");
    }

    @Override
    public RespBean insert(Admin admin) {
        if (admin.getPassword() == null || admin.getPassword().trim().length() == 0){
            admin.setPassword(passwordEncoder.encode(defaultPassword));
        }
        if (admin.getUserFace()==null || admin.getUserFace().trim().length() == 0){

        }
        int result = adminMapper.insert(admin);
        if (1 == result) {//更新成功
            return RespBean.success("成功新建管理员!");
        }
        return RespBean.error("新建管理员失败!");
    }

//    private UserModel convertFromDataObject(Admin userDO, AdminInfo userPasswordDO) {
//        if (userDO==null) return null;
//        UserModel userModel=new UserModel();
//        BeanUtils.copyProperties(userDO, userModel);
//        if (userPasswordDO!=null)
//            userModel.setPassword(userPasswordDO.getPassword());
//        return userModel;
//    }

}
