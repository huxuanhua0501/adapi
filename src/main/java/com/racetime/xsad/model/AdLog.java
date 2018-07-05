package com.racetime.xsad.model;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.Message;
import com.googlecode.protobuf.format.JsonFormat;
import com.racetime.xsad.constants.Constants;
import com.racetime.xsad.util.DateUtil;


/**
 * 
*    
* 项目名称：ad-service   
* 类名称：AdLog   
* 类描述：   日志记录实体类
* 创建人：skg
* 创建时间：2017-7-10 下午6:01:15   
* @version    
*
 */
public class AdLog {

	private static Logger logger = LoggerFactory.getLogger(AdLog.class);  
	
	public String request_id = "";//请求ID
	
	public String source_type = "";//请求类型

	public String log_type = "";//请求类型
	
	public String create_time = "";//创建时间
	
	public Object log_data;//json数据

	public AdLog(String request_id, String source_type, String log_type, Object log_data) {
		this.request_id = request_id;
		this.source_type = source_type;
		this.log_type = log_type;
		this.log_data = log_data == null ? "" : log_data;
		this.create_time = DateUtil.getTime();
	}

	public void writeToJson(){
		logger.info(Constants.gson.toJson(this));
	}
	
	public void writeToDecodeJson() {
		try {
			logger.info(URLDecoder.decode(Constants.gson.toJson(this), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public void writeProtoBufToJson(Message message){
		logger.info(Constants.gson.toJson(this).replace("\"\"}",JsonFormat.printToString(message)) + "}");
	}
	
	public void writeStringTojson(String json){
		logger.info(Constants.gson.toJson(this).replace("\"\"}", String.valueOf(json)) + "}");
	}
}
