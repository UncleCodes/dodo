package com.dodo.common.framework.service.impl;

import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.dodo.common.framework.service.MailService;
import com.dodo.utils.config.DodoFrameworkConfigUtil.DodoMailSenderUtil;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class MailServiceImpl implements MailService {
	private FreeMarkerConfigurer freeMarkerConfigurer;

	private JavaMailSender javaMailSender;

	private TaskExecutor taskExecutor;

	// 增加邮件发送任务
	public void addMailTask(final MimeMessage mimeMessage) {
		taskExecutor.execute(new Runnable() {
			public void run() {
				for (int i = 0; i < 3; i++) {
					try {
						javaMailSender.send(mimeMessage);
						break;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	public void sendMail(String subject, String templateFilePath,
			Map<String, Object> data, String toMail) throws IOException, TemplateException, MessagingException {
		sendMail(subject, templateFilePath,data,  new String[]{toMail});
	}

	@Override
	public void sendMail(String subject, String msgContent, String toMail) throws MessagingException, UnsupportedEncodingException {
		sendMail(subject, msgContent, new String[]{toMail});
	}

	@Override
	public void sendMail(String subject, String templateFilePath,
		Map<String, Object> data, String[] toMail) throws IOException, TemplateException, MessagingException {
		Template localTemplate = freeMarkerConfigurer.getConfiguration()
				.getTemplate(templateFilePath);
		String msgContent = FreeMarkerTemplateUtils
				.processTemplateIntoString(localTemplate, data);
		sendMail(subject, msgContent, toMail);
	}

	@Override
	public void sendMail(String subject, String msgContent, String[] toMail) throws MessagingException, UnsupportedEncodingException {
		MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(
				mimeMessage, false, "utf-8");
		messageHelper.setFrom(MimeUtility
				.encodeWord(DodoMailSenderUtil.smtpCompanyName)
				+ "<"
				+ DodoMailSenderUtil.smtpFromMail + ">");
		messageHelper.setTo(toMail);
		messageHelper.setSubject(subject);
		messageHelper.setText(msgContent, true);
		addMailTask(mimeMessage);
	}

	public FreeMarkerConfigurer getFreeMarkerConfigurer() {
		return freeMarkerConfigurer;
	}

	public JavaMailSender getJavaMailSender() {
		return javaMailSender;
	}

	public TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
		this.freeMarkerConfigurer = freeMarkerConfigurer;
	}

	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}
}