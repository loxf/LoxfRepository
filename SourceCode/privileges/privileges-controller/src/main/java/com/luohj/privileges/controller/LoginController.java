package com.luohj.privileges.controller;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.luohj.privileges.common.constants.SystemConstant;
import com.luohj.privileges.common.utils.DES3;
import com.luohj.privileges.common.utils.IdentifyingCodeUtil;
import com.luohj.privileges.core.control.AbstractController;
import com.luohj.privileges.core.model.Result;
import com.luohj.privileges.core.model.View;
import com.luohj.privileges.model.User;
import com.luohj.privileges.service.service.ILoginService;

@Controller
@RequestMapping("/system/login")
public class LoginController extends AbstractController {

	@Resource
	private ILoginService loginService;
	/**
	 * TODO:获取登录验证码
	 * 
	 * @throws IOException
	 * @author:luohj
	 */
	@RequestMapping("/getIdentifyCode")
	public void getIdentifyCode() throws IOException {
		// 设置不缓存图片
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "No-cache");
		response.setDateHeader("Expires", 0);
		// 指定生成的相应图片
		response.setContentType("image/jpeg");
		IdentifyingCodeUtil idCode = new IdentifyingCodeUtil();
		BufferedImage image = new BufferedImage(idCode.getWidth(),
				idCode.getHeight(), BufferedImage.TYPE_INT_BGR);
		Graphics2D g = image.createGraphics();
		// 定义字体样式
		Font myFont = new Font("黑体", Font.BOLD, 16);
		// 设置字体
		g.setFont(myFont);

		g.setColor(idCode.getRandomColor(200, 250));
		// 绘制背景
		g.fillRect(0, 0, idCode.getWidth(), idCode.getHeight());

		g.setColor(idCode.getRandomColor(180, 200));
		idCode.drawRandomLines(g, 160);
		String identifyStr = idCode.drawRandomString(4, g);
		g.dispose();
		session.setAttribute(SystemConstant.IDENTIFYCODE, identifyStr);
		ImageIO.write(image, "JPEG", response.getOutputStream());
	}
	
	/**
	 * TODO:登录
	 * 
	 * @return
	 * @author:ouxin
	 */
	@ResponseBody
	@RequestMapping("/login")
	public Result login(User user){
		//SystemConstant.USERID;
		Result result = new Result ();
		if(user==null||user.getUserName()==null||user.getUserPassword()==null
				||user.getIdentifyCode()==null||user.getIdentifyCode().equals("")
				||user.getUserName().equals("")||user.getUserPassword().equals("")){
			result.setResCode(SystemConstant.ERROR);
			result.setResMsg("参数错误！");
			return result;
		}
		String sessionCode = (String) session.getAttribute(SystemConstant.IDENTIFYCODE);
		if (!sessionCode.toLowerCase().equals(
				user.getIdentifyCode().toLowerCase())) {
			result.setResCode(SystemConstant.ERROR);
			result.setResMsg("验证码不正确！");
		}else{
			try {
				DES3 des3 = new DES3();
				user.setUserPassword(des3.encrypt(user.getUserPassword()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			User ur = loginService.login(user);
			if(ur==null){
				result.setResCode(SystemConstant.ERROR);
				result.setResMsg("用户名或密码错误！");
			} else {
				session.setAttribute(SystemConstant.USERID, ur.getUserId());
				result.setResCode(SystemConstant.SUCCESS);
				result.setResMsg("登录成功！");
				result.setObj(ur);
				result.setView(new View("/system/login/toIndex"));
			}
		}
		return result;
	}
	
	@RequestMapping("/toIndex")
	public ModelAndView toIndex(User user){
		ModelAndView view = new ModelAndView("main/index");
		view.addObject("userName", user.getUserName());
		return view;
	}
}
