package com.disda.cowork.service.impl;

import com.disda.cowork.error.BusinessException;
import com.disda.cowork.error.EmBusinessError;
import com.disda.cowork.service.IVerificationCodeService;
import com.disda.cowork.utils.IpUtils;
import com.disda.cowork.utils.RndUtils;
import com.disda.cowork.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @program: cowork-back
 * @description:
 * @author: Disda
 * @create: 2022-07-16 15:06
 */

@Service("mail")
@Slf4j
public class SendMailServiceImpl implements IVerificationCodeService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    //发送人
    private String from = "disda@vip.qq.com";
    //接收人
    private String to = "497457669@qq.com";
    //标题
    private String subject = "云E办帐号--邮箱验证服务";
    //正文
    private String registryContent = "<!DOCTYPE html><html lang=\"en\"><head>    <meta charset=\"UTF-8\">    <title>Title</title>    <div id=\"mailContentContainer\" class=\"qmbox qm_con_body_content qqmail_webmail_only\" style=\"opacity: 1;\">        <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"max-width: 600px;\">            <tbody>            <tr>                <td>                    <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">                        <tbody>                        <tr>                            <td align=\"left\"><img width=\"92\" height=\"32\"                                                  src=\"https://ssl.gstatic.com/images/branding/googlelogo/2x/googlelogo_color_188x64dp.png\"                                                  style=\"display: block; width: 92px; height: 32px;\"></td>                            <td align=\"right\"><img width=\"32\" height=\"32\"                                                   style=\"display: block; width: 32px; height: 32px;\"                                                   src=\"https://ssl.gstatic.com/accountalerts/email/keyhole.png\"></td>                        </tr>                        </tbody>                    </table>                </td>            </tr>            <tr height=\"16\"></tr>            <tr>                <td>                    <table bgcolor=\"#4184F3\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"                           style=\"min-width: 332px; max-width: 600px; border: 1px solid #E0E0E0; border-bottom: 0; border-top-left-radius: 3px; border-top-right-radius: 3px;\">                        <tbody>                        <tr>                            <td height=\"72px\" colspan=\"3\"></td>                        </tr>                        <tr>                            <td width=\"32px\"></td>                            <td style=\"font-family: Roboto-Regular,Helvetica,Arial,sans-serif; font-size: 24px; color: #FFFFFF; line-height: 1.25;\">                                ☁️E办 验证码                            </td>                            <td width=\"32px\"></td>                        </tr>                        <tr>                            <td height=\"18px\" colspan=\"3\"></td>                        </tr>                        </tbody>                    </table>                </td>            </tr>            <tr>                <td>                    <table bgcolor=\"#FAFAFA\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"                           style=\"min-width: 332px; max-width: 600px; border: 1px solid #F0F0F0; border-bottom: 1px solid #C0C0C0; border-top: 0; border-bottom-left-radius: 3px; border-bottom-right-radius: 3px;\">                        <tbody>                        <tr height=\"16px\">                            <td width=\"32px\" rowspan=\"3\"></td>                            <td></td>                            <td width=\"32px\" rowspan=\"3\"></td>                        </tr>                        <tr>                            <td><p>尊敬的 ☁️E办 用户：</p>                                <p>我们收到了一项请求，要求通过您的电子邮件地址访问您的 ☁️E办 帐号 <span style=\"color: #659CEF\" dir=\"ltr\">                                        {username}</span>。您的☁️E办 验证码为：</p>                                <div style=\"text-align: center;\"><p dir=\"ltr\"><strong                                        style=\"text-align: center; font-size: 24px; font-weight: bold;\">{verificationCode}</strong>                                </p></div>                                <p>如果您并未请求此验证码，则可能是他人正在尝试访问以下 ☁️E办 帐号：<span style=\"color: #659CEF\"                                                                            dir=\"ltr\">{username}</span>。<strong>请勿将此验证码转发给或提供给任何人。</strong>                                </p>                                <p>此致</p>                                <p>☁️E办 帐号团队敬上</p></td>                        </tr>                        <tr height=\"32px\"></tr>                        </tbody>                    </table>                </td>            </tr>            <tr height=\"16\"></tr>            <tr>                <td style=\"max-width: 600px; font-family: Roboto-Regular,Helvetica,Arial,sans-serif; font-size: 10px; color: #BCBCBC; line-height: 1.5;\">                    <table>                        <tbody>                        <tr>                            <td>此电子邮件地址无法接收回复。如需更多信息，请访问 <a                                    href=\"{addr}\"                                    style=\"text-decoration: none; color: #4d90fe;\" rel=\"noopener\" target=\"_blank\">                                帐号帮助中心</a>。<br>© Disda Inc., 1600 Amphitheatre Parkway, Mountain View, CA 94043, USA                                <table style=\"font-family: Roboto-Regular,Helvetica,Arial,sans-serif; font-size: 10px; color: #666666; line-height: 18px; padding-bottom: 10px\"></table>                            </td>                        </tr>                        </tbody>                    </table>                </td>            </tr>            </tbody>        </table>        <style type=\"text/css\">.qmbox style, .qmbox script, .qmbox head, .qmbox link, .qmbox meta {            display: none !important;        }</style>    </div></head><body></body></html>";


    public void sendMail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from + "(云E办)");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(registryContent);
        javaMailSender.send(message);
    }

    public boolean checkAddr(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    public void sendVerificationCode(String mailAddr, String username, String verificationCode) throws BusinessException {

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from+"(☁️E办)");
            helper.setTo(mailAddr);
            helper.setSubject(subject);
            registryContent = registryContent.replace("{verificationCode}", verificationCode);
            registryContent = registryContent.replace("{username}",username);
            registryContent = registryContent.replace("{addr}",mailAddr);

            helper.setText(registryContent,true);		//此处设置正文支持html解析
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new BusinessException(EmBusinessError.MAIL_NOT_EXIST);
        }
    }
    //地方有问题
    @Override
    public RespBean verificationCodeGenerate(String username, String mailAddr, HttpServletRequest request) throws BusinessException {
        if(checkAddr(mailAddr) == false){
            log.error(IpUtils.getIpAddr(request) +"绕过前端攻击:前端检查邮箱格式被绕过");
            return RespBean.error("非法提交邮箱地址");
        }
        if (Boolean.TRUE.equals(redisTemplate.hasKey("verificationCode_" + mailAddr))) {
            log.error(IpUtils.getIpAddr(request) +"绕过前端攻击:前端限制多次请求被绕过");
            return RespBean.error("非法重复请求邮箱验证码");
        }
        String verificationCode = RndUtils.generatorCode();
        redisTemplate.opsForValue().set("verificationCode_" + mailAddr,verificationCode, 1, TimeUnit.MINUTES);
        sendVerificationCode(mailAddr,username,verificationCode);
        return RespBean.success("已发送邮箱，请及时查看！");
    }


}