package com.cmoc.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 设置静态参数初始化
 * @author: jeecg-boot
 */
@Component
@Data
public class StaticConfig {

    @Value("${jeecg.oss.accessKey}")
    private String accessKeyId;

    @Value("${jeecg.oss.secretKey}")
    private String accessKeySecret;

    @Value(value = "${spring.mail.username}")
    private String emailFrom;

    //本身邮件的发送者，来自邮件配置
    @Value(value = "${spring.mail.username}")
    private String userName;
    @Value(value = "${spring.mail.host}")
    private String host;
    @Value(value = "${spring.mail.password}")
    private String password;
    @Value(value = "${spring.mail.username}")
    private String from;

//    /**
//     * 签名密钥串
//     */
//    @Value(value = "${jeecg.signatureSecret}")
//    private String signatureSecret;


    /*@Bean
    public void initStatic() {
       DySmsHelper.setAccessKeyId(accessKeyId);
       DySmsHelper.setAccessKeySecret(accessKeySecret);
       EmailSendMsgHandle.setEmailFrom(emailFrom);
    }*/

}
