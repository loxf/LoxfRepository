/**
 * DispatchServlet.java
 * luohj - 下午1:33:48
 * 
 */
package org.loxf.dubbo.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.loxf.common.servlet.Forward;
import org.loxf.common.servlet.Redirect;
import org.loxf.registry.bean.ServletBean;
import org.loxf.registry.context.ApplicationContext;
import org.loxf.registry.utils.CommonUtil;

import net.sf.json.JSONArray;

/**
 * servlet转发
 * 
 * @author luohj
 *
 */
public class DispatchServlet extends HttpServlet {
	ApplicationContext ctx = ApplicationContext.getInstance();
	private String[] excludePaths ;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void init() throws ServletException {  
		String basepath = CommonUtil.valueofString(this.getInitParameter("basePath"), "org.loxf");
		ctx.loadServlet(basepath);
		String excludePathStr = this.getInitParameter("excludePath");
		if(!StringUtils.isBlank(excludePathStr)){
			excludePathStr = excludePathStr.replaceAll("[\\t\\n\\r]", "").replaceAll(" ", "");
			excludePaths = excludePathStr.split(",");
		}
	}

	public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = req.getServletPath().replace(".do", "");
		if(CommonUtil.contains(excludePaths, path)){
			return ;
		}
		ServletBean servletBean = (ServletBean)ctx.getServlet(path);
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html"); 
		if(servletBean==null){
			resp.sendError(404, "No handler found for [" + path + "]");  
            System.err.println("No handler found for " + path);  
            return; 
		}
		try {
			Object result = execute(servletBean, path, req, resp);
			if(result!=null){
				PrintWriter w = resp.getWriter();
				if(result instanceof String || result instanceof Integer || result instanceof Boolean
					 || result instanceof Character || result instanceof Short || result instanceof Byte
					 || result instanceof Long || result instanceof Float || result instanceof Double){
					w.print(result);
				}
				else {
					w.write(JSONArray.fromObject(result).toString());
				}
				w.close();
			}
		} catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException
				| InstantiationException e) {
			e.printStackTrace();
			throw new IOException(e.getMessage());
		}
	}

	/**
	 * 执行servlet引擎。
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 * @throws InvocationTargetException 
	 * @throws InstantiationException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	private Object execute(ServletBean servlet, String path, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, InvocationTargetException, IllegalAccessException,
			IllegalArgumentException, InstantiationException {
		try {
			Method m = servlet.getMethod().get(path);
			Object o = servlet.getServlet().newInstance();
			Object result = m.invoke(o, req, resp);
			if (result instanceof Forward) {
					req.getRequestDispatcher(((Forward) result).getLocation()).forward(req, resp);
			} else if(result instanceof Redirect){
				resp.sendRedirect(((Redirect) result).getLocation());				
			}
			return result;
		} catch (InvocationTargetException e) {
			throw e;
		}
	}

}
