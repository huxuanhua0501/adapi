package com.racetime.xsad.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * 
* 项目名称：adapi   
* 类名称：BaseController   
* 类描述：   
* 创建人：skg   
* 创建时间：2017-12-27 下午5:57:38   
* @version    
*
 */
public class BaseController {

	public static Logger logger = LoggerFactory.getLogger(BaseController.class);  
	
	/**
	 * 获取请求json数据
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public String receivePost(HttpServletRequest request) throws Exception {
		// 读取请求内容
		BufferedReader br = new BufferedReader(new InputStreamReader(
				request.getInputStream()));
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		// 将资料解码
		String reqBody = sb.toString();
		return URLDecoder.decode(reqBody, "utf-8");
	}
	
	/**
	 * 获取请求IP
	 * @param request
	 * @return
	 */
	public String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if(!StringUtils.isEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if(index != -1){
                return ip.substring(0,index);
            }else{
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if(!StringUtils.isEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            return ip;
        }
        return request.getRemoteAddr();
    }
	
	public void jsonOut(HttpServletResponse response, String json) throws IOException{
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().println(json);
	}
}
