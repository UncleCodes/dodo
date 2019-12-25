package com.dodo.common.filter;

import com.octo.captcha.service.CaptchaService;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class DodoCaptchaFilter implements Filter {

	private CaptchaService captchaService;

	public void init(FilterConfig filterConfig) {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		resp.setHeader("Cache-Control", "no-store");
		resp.setHeader("Pragma", "no-cache");
		resp.setDateHeader("Expires", 0L);
		resp.setContentType("image/jpeg");
		ServletOutputStream servletOutputStream = null;
		try {
			servletOutputStream = resp.getOutputStream();
			BufferedImage bufferedImage = (BufferedImage) this.captchaService
					.getChallengeForID(req.getSession(true).getId(), req.getLocale());
			ImageIO.write(bufferedImage, "jpg", servletOutputStream);
			servletOutputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				servletOutputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void destroy() {
	}

	public void setCaptchaService(CaptchaService captchaService) {
		this.captchaService = captchaService;
	}
}
