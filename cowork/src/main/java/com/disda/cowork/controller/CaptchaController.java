package com.disda.cowork.controller;

import com.disda.cowork.config.VerificationCode;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 验证码
 */
@RestController
@Log
public class CaptchaController {

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @ApiOperation(value = "验证码")
    @GetMapping(value = "/captcha",produces = "image/jpeg")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        VerificationCode code = new VerificationCode();
        BufferedImage image = code.getImage();
        String text = code.getText();
        HttpSession session = request.getSession(true);
        session.setAttribute("captcha", text);
        VerificationCode.output(image,response.getOutputStream());
    }
//        // 定义response输出类型为image/jpeg类型
//        response.setDateHeader("Expires",0);
//        // Set standard HTTP/1.1 no-cache headers.
//        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
//        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
//        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
//        // Set standard HTTP/1.0 no-cache header.
//        response.setHeader("Pragma", "no-cache");
//        // return a jpeg
//        response.setContentType("image/jpeg");
//        //-------------------生成验证码 begin --------------------------
//        String text = defaultKaptcha.createText(); // 获取验证码文本内容
//        log.info("验证码文本内容：" + text);
//        request.getSession().setAttribute("captcha", text);
//        BufferedImage image = defaultKaptcha.createImage(text); // 根据文本内容创建图形验证码
//        ServletOutputStream outputStream = null;
//        try {
//            outputStream = response.getOutputStream();
//            ImageIO.write(image, "jpg", outputStream); // 输出流输出图片，格式为jpg
//            outputStream.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (outputStream != null) {
//                try {
//                    outputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        //-------------------生成验证码 end --------------------------
//    }

}
