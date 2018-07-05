package com.racetime.xsad.controller;

import java.io.IOException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.racetime.xsad.constants.Constants;
import com.racetime.xsad.model.AdLog;
import com.racetime.xsad.model.ResponseMessage;
import com.racetime.xsad.model.ssp.BaseModel;
import com.racetime.xsad.service.impl.AdServiceImpl;
import com.racetime.xsad.util.UUIDUtil;

/**
 * 
* 项目名称：adapi  
* 类名称：AdController   
* 类描述：    广告服务接口
* 创建人：skg   
* 创建时间：2017-12-27 下午5:57:27   
* @version    
*
 */
@RestController
@RequestMapping("/ad")
public class AdController extends BaseController{
	
	@Autowired
	private AdServiceImpl adServiceImpl;

	/**
	 * 终端请求广告
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping("/getAd")
	public void getAd(HttpServletRequest request, HttpServletResponse response) {
		String request_id = UUIDUtil.getUuid();
		ResponseMessage responseMessage = null;
		BaseModel baseModel = null;
		try {
			baseModel = getBaseModel(request,request_id);
			responseMessage = adServiceImpl.getResponseMessage(baseModel);
			//返回终端数据转换成json
			jsonOut(response, URLDecoder.decode(Constants.gson.toJson(responseMessage),"utf-8"));
		} catch (Exception e) {
			logger.error("广告请求接口发生异常，堆栈轨迹如下：",e);
		}finally{
			new AdLog(request_id,baseModel == null?null:baseModel.getSourceType(),"6", responseMessage).writeToDecodeJson();//返回终端日志
		}
	}

	private BaseModel getBaseModel(HttpServletRequest request,String request_id) throws Exception{
		StringBuffer url = request.getRequestURL();
		url = url.delete(url.indexOf("/getAd"),url.length());
		BaseModel baseModel = new BaseModel();
		baseModel.setIp(getIp(request));
		baseModel.setRequest_url(url);
		baseModel.setRequest_id(request_id);
		baseModel.setRequestJson(receivePost(request));
		baseModel.setSourceType("");
		return baseModel;
	}
	/**
	 * 终端回调方法
	 * @param request
	 * @param response
	 * @param request_id
	 * @param type
	 * @param win_notice_url
	 * @param third_monitor_url
	 */
	@GetMapping("/callback")
	public String callback(HttpServletRequest request,HttpServletResponse response, 
			String request_id, String strategy_id, String source_type) {
		adServiceImpl.callback(request_id, source_type,strategy_id);
		try {
			jsonOut(response, "");
		} catch (IOException e) {
			logger.error("广告回掉接口发生异常，堆栈轨迹如下：",e);
		}
		return null;
	}
	
}
