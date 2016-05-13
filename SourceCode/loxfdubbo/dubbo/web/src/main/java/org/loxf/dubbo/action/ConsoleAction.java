/**
 * ConsoleAction.java
 * luohj - 下午3:12:22
 * 
 */
package org.loxf.dubbo.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.loxf.core.servlet.anno.Controller;
import org.loxf.core.servlet.anno.RequestMapping;
import org.loxf.core.utils.MapCastList;
import org.loxf.registry.action.BaseAction;
import org.loxf.registry.bean.AliveClient;
import org.loxf.registry.bean.Client;
import org.loxf.registry.bean.Server;
import org.loxf.registry.bean.Service;
import org.loxf.registry.main.IRegistryCenterManager;
import org.loxf.registry.main.RegistryCenterManager;

/**
 * @author luohj
 *
 */
@Controller
public class ConsoleAction extends BaseAction {

	IRegistryCenterManager mgr = RegistryCenterManager.getInstance();
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@RequestMapping("/console/getService")
	public List<Service> getService(HttpServletRequest request, HttpServletResponse response){
		Map<String, Service> servicesMap = mgr.getServices();
		List<Service> serviceList = (List<Service>)MapCastList.convert(servicesMap);
		if(serviceList==null || serviceList.size()==0){
			return null;
		}
		Map<String, AliveClient> aliveMap = mgr.getAliveClients();
		for(Service service : serviceList){
			HashMap<String, Client> realClient = new HashMap<String, Client>();
			List<Client> cilentList = (List<Client>)MapCastList.convert(service.getClients());
			if(cilentList!=null){
				for(Client client : cilentList){
					if(aliveMap.containsKey(client.toString())){
						realClient.put(client.toString(), client);
					}
				}
			}
			service.setClients(realClient);
		}
		return serviceList;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/console/getServer")
	public List<AliveClient> getServer(HttpServletRequest request, HttpServletResponse response){
		Map<String, AliveClient> aliveMap = mgr.getAliveClients();
		return (List<AliveClient>)MapCastList.convert(aliveMap);
	}

	@RequestMapping("/console/getServiceByServer")
	public List<Service> getServiceByServer(HttpServletRequest request, HttpServletResponse response){
		String serverKey = request.getParameter("server");
		if(StringUtils.isEmpty(serverKey)){
			throw new RuntimeException(request.getServletPath() + ":serverKey参数不能为空！");
		}
		String []t = serverKey.split(":");
		Server server = new Server(t[0], Integer.valueOf(t[1]));
		Set<Service> rst = mgr.getServiceByServer(server);
		if(rst==null){
			return new ArrayList<Service>();
		}
		return new ArrayList<Service>(rst);
	}
}
