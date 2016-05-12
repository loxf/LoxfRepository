package org.loxf.core.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 服务器信息工具
 * @author luohj
 *
 */
public class ComputerInfoUtil {
	public static String getIp() throws UnknownHostException{
		InetAddress addr = InetAddress.getLocalHost();
		String ip=addr.getHostAddress();//获得本机IP
		return ip;      
	}
	public static String getHostName() throws UnknownHostException{
		InetAddress addr = InetAddress.getLocalHost();
		String hostName=addr.getHostName();//获得本机名称
		return hostName;
	}
	public static String getPort(){
		return "1234";
	}
}
