package com.luohj.privileges.core.control;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

@Controller
public class AbstractController {
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected HttpSession session;
	/**
	 * 拦截请求异常
	 * 
	 * @param request
	 * @param e
	 * @return
	 * @throws IOException
	 */
	@ExceptionHandler
	public void exception(HttpServletRequest request,
			HttpServletResponse response, Exception e) throws IOException {

		e.printStackTrace();
		// TODO 添加异常处理逻辑，如日志记录
		request.setAttribute("exceptionMessage", e.getMessage());
		if (e.getClass() == NoSuchRequestHandlingMethodException.class) {
			response.sendRedirect(request.getContextPath()
					+ "/common/view/404.jsp");
		} else {
			response.sendRedirect(request.getContextPath()
					+ "/common/view/500.jsp");
		}
	}

	@ModelAttribute
	public void initController(HttpServletRequest request,
			HttpServletResponse response) {
		this.request = request;
		this.response = response;
		this.session = request.getSession();
	}
}
