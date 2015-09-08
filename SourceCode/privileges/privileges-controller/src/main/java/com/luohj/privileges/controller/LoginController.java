package com.luohj.privileges.controller;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.luohj.privileges.common.utils.IdentifyingCodeUtil;
import com.luohj.privileges.core.control.AbstractController;

@Controller
@RequestMapping("/system/login")
public class LoginController extends AbstractController {

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
		session.setAttribute("IdentifyCode", identifyStr);
		ImageIO.write(image, "JPEG", response.getOutputStream());
	}
}
