package com.racetime.xsad.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import ch.qos.logback.core.PropertyDefinerBase;

public class LogbackCustom extends PropertyDefinerBase {

	@Override
	public String getPropertyValue() {
		String info;
		InetAddress netAddress = getInetAddress();
		// 获取主机名 linux多网卡无法根据环境指定具体网卡，此方法只能在windows下使用
//		info = getHostName(netAddress);
		info = getIp(netAddress);
		return info;
	}

	public static InetAddress getInetAddress() {
		try {
			return InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getHostName(InetAddress netAddress) {
		if (null == netAddress) {
			return null;
		}
		String hostName = netAddress.getHostName();
		return hostName;
	}
	
	public static String getIp(InetAddress netAddress) {
		if (null == netAddress) {
			return null;
		}
		String ip = netAddress.getHostAddress();
		return ip;
	}

}
