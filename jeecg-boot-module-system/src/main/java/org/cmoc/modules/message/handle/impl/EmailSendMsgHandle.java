package org.cmoc.modules.message.handle.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.cmoc.common.util.SpringContextHolder;
import org.cmoc.common.util.SpringContextUtils;
import org.cmoc.common.util.oConvertUtils;
import org.cmoc.config.StaticConfig;
import org.cmoc.modules.message.handle.ISendMsgHandle;
import org.cmoc.modules.srm.service.IApproverCcMailService;
import org.cmoc.modules.system.entity.SysUser;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @Description: 邮箱发送信息
 * @author: jeecg-boot
 */
@Slf4j
public class EmailSendMsgHandle implements ISendMsgHandle {
    static String emailFrom = "smj0919@aliyun.com";

    //默认编码
    public static final String DEFAULT_ENCODING = "UTF-8";

    public static void setEmailFrom(String emailFrom) {
        EmailSendMsgHandle.emailFrom = emailFrom;
    }

    @Override
    public void SendMsg(String es_receiver, String es_title, String es_content) {
        JavaMailSender mailSender = (JavaMailSender) SpringContextUtils.getBean("mailSender");
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        //update-begin-author：taoyan date:20200811 for:配置类数据获取
        if(oConvertUtils.isEmpty(emailFrom)){
            StaticConfig staticConfig = SpringContextUtils.getBean(StaticConfig.class);
            setEmailFrom(staticConfig.getEmailFrom());
        }
        //update-end-author：taoyan date:20200811 for:配置类数据获取
        try {
            helper = new MimeMessageHelper(message, true);
            // 设置发送方邮箱地址
            helper.setFrom(emailFrom);
            helper.setTo(es_receiver);
            helper.setSubject(es_title);
            helper.setText(es_content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void sendTemplateMail(String subject, String context, List<String> toWho, List<String> ccPeoples,String cc) {
        //检验参数：邮件主题、收件人、邮件内容必须不为空才能够保证基本的逻辑执行
        if(toWho == null||toWho.size() == 0 || context==null){
            log.error("邮件-> {} 无法继续执行，因为缺少基本的参数：");
            throw new RuntimeException("模板邮件无法继续发送，因为缺少必要的参数！");
        }

        IApproverCcMailService approverCcMailService = SpringContextHolder.getBean(IApproverCcMailService.class);

        //增加抄送人信息
        try{
            List<SysUser> userList =   approverCcMailService.selectUserMails();
            if (userList!=null && userList.size()>0){
                if (ccPeoples==null){
                    ccPeoples = new ArrayList<>();
                }
                //job不发
                if("0".equals(cc)){
                    for (SysUser sysUser: userList){
                        if (StringUtils.isNotBlank(sysUser.getEmail())){
                            ccPeoples.add(sysUser.getEmail());
                        }
                    }
                }
            }
        }catch (Exception e){
            log.error("发送邮件出错->{}","未查询到抄送人员信息");
            log.error("发送邮件出错->{}",subject);
        }


        JavaMailSender mailSender = (JavaMailSender) SpringContextUtils.getBean("mailSender");
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true,DEFAULT_ENCODING);

            String process = null;
            String[] toWhoArr = null;
            String[] ccPeoplesArr = null;
            ListHelper.removeDuplicateWithOrder(toWho);
            if(toWho!=null){toWhoArr=toWho.toArray(new String[0]);}
            if(ccPeoples!=null){ccPeoplesArr=ccPeoples.toArray(new String[0]);}


            StaticConfig staticConfig = SpringContextUtils.getBean(StaticConfig.class);
            handleBasicInfo(helper,subject,context,toWhoArr,ccPeoplesArr,staticConfig);
            //发送邮件
            log.info("html开始发送邮件");
            JavaMailSenderImpl sender = new JavaMailSenderImpl();
            sender.setHost(staticConfig.getHost());
            sender.setPort(465);
            sender.setUsername(staticConfig.getUserName());
            sender.setPassword(staticConfig.getPassword());
            Properties javaMailProperties = new Properties();
            javaMailProperties.put("mail.smtp.auth", true);
            javaMailProperties.put("mail.smtp.timeout", 1000);
            javaMailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            javaMailProperties.put("mail.smtp.socketFactory.fallback", "false");
            javaMailProperties.put("mail.smtp.socketFactory.port", "465");
            sender.setJavaMailProperties(javaMailProperties);
            sender.send(message);
            log.info("html邮件发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("发送邮件出错->{}",subject);
        }
    }

    public void handleBasicInfo(MimeMessageHelper mimeMessageHelper, String subject, String content, String[] toWho, String[] ccPeoples,StaticConfig staticConfig){
        try {
            //设置发件人
            String from = staticConfig.getEmailFrom();
            log.info("邮箱配置参数:" + staticConfig.getHost() + ";" + staticConfig.getUserName() + ";" + staticConfig.getPassword());
            mimeMessageHelper.setFrom('<'+from+'>');
            //设置邮件的主题
            mimeMessageHelper.setSubject(subject);
            //设置邮件的内容
            mimeMessageHelper.setText(content,true);
            //设置邮件的收件人
            mimeMessageHelper.setTo(toWho);
            //设置邮件的抄送人
            if(ccPeoples!=null){
                mimeMessageHelper.setCc(ccPeoples);
            }
        } catch (MessagingException e) {
            log.error("html邮件基本信息出错->{}",subject);
        }
    }
}
