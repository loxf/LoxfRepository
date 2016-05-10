/**
 * ConsoleAction.java
 * luohj - 下午3:12:22
 * 
 */
package org.loxf.dubbo.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.loxf.registry.bean.AliveClient;
import org.loxf.registry.bean.Service;
import org.loxf.registry.main.IRegistryCenterManager;
import org.loxf.registry.main.RegistryCenterManager;
import org.loxf.registry.utils.MapCastList;

import net.sf.json.JSONArray;

/**
 * @author luohj
 *
 */
public class ConsoleAction extends HttpServlet {

	IRegistryCenterManager mgr = RegistryCenterManager.getInstance();
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String method = request.getParameter("method");
		if(StringUtils.isBlank(method)){
			throw new RuntimeException("请求参数method未传！");
		}
		List<?> list = null;
		if (method.equalsIgnoreCase("SERVICE")) {
			list = getService();
		} else if (method.equalsIgnoreCase("SERVER")) {
			list = getServer();
		}
		PrintWriter out = response.getWriter();
		String result = JSONArray.fromObject(list).toString();
		out.write(result);
		out.close();
	}

	@SuppressWarnings("unchecked")
	public List<Service> getService(){
		Map<String, Service> servicesMap = mgr.getServices();
		return (List<Service>)MapCastList.convert(servicesMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<AliveClient> getServer(){
		Map<String, AliveClient> aliveMap = mgr.getAliveClients();
		return (List<AliveClient>)MapCastList.convert(aliveMap);
		
	}
}
