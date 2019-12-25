package com.dodo.common.framework.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.mail.MessagingException;

import freemarker.template.TemplateException;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public interface MailService {
    // 单人发送模板邮件
    void sendMail(String subject, String templateFilePath, Map<String, Object> data, String toMail)
            throws TemplateException, MessagingException, IOException;

    // 单人发送邮件
    void sendMail(String subject, String msgContent, String toMail) throws MessagingException,
            UnsupportedEncodingException;

    // 多人发送模板邮件
    void sendMail(String subject, String templateFilePath, Map<String, Object> data, String[] toMail)
            throws IOException, TemplateException, MessagingException;

    // 多人发送邮件
    void sendMail(String subject, String msgContent, String[] toMail) throws MessagingException,
            UnsupportedEncodingException;
}
