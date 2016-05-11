/**
 * HelloAction.java
 * luohj - 下午3:16:11
 * 
 */
package org.loxf.dubbo.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.loxf.api.demo.IHello;
import org.loxf.api.demo.IWho;
import org.loxf.registry.annotation.Controller;
import org.loxf.registry.annotation.Customer;
import org.loxf.registry.annotation.RequestMapping;

/**
 * @author luohj
 *
 */
@Controller
public class HelloAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Customer
	private IHello hello;
	@Customer(group = "WhoBoy")
	protected IWho whoBoy;
	@Customer(group = "WhoGirl")
	public IWho whoGirl;

	@RequestMapping("/app/hello")
	public String hello(HttpServletRequest request, HttpServletResponse response) throws IOException {
		return hello.sayHi();
	}

	@RequestMapping("/app/whoBoy")
	public String whoBoy(HttpServletRequest request, HttpServletResponse response) throws IOException {
		return whoBoy.who();
	}

	@RequestMapping("/app/whoGirl")
	public String whoGirl(HttpServletRequest request, HttpServletResponse response) throws IOException {
		return whoGirl.who();
	}

	@RequestMapping("/app/iam")
	public String iam(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String name = request.getParameter("name");
		name = (StringUtils.isEmpty(name) ? "Lilei" : name);
		return whoBoy.iam(name);
	}
}
