package com.disda.cowork.config.security.components;


import com.disda.cowork.dto.RespBean;
import com.disda.cowork.po.Admin;
import com.disda.cowork.service.IAdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

	@Value("${jwt.tokenHead}")
	private String tokenHead;

	@Autowired
	IAdminService adminService;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Admin admin = adminService.getAdminByUserName(authentication.getName());
		// admin.setRoles(adminService.getRoles(admin.getId()));
		UserDetails userDetails = admin;
		//生成token
		String token = jwtTokenUtil.generateToken(userDetails);
		//封装返回信息
		Map<String, String> tokenMap = new HashMap<>(2);
		//将token放入返回信息中
		tokenMap.put("token", token);
		//将token头放入返回信息中
		tokenMap.put("tokenHead", tokenHead);


		RespBean bean = RespBean.success("登录成功！",tokenMap);
		out.write(new ObjectMapper().writeValueAsString(bean));
		out.flush();
		out.close();
	}

}
