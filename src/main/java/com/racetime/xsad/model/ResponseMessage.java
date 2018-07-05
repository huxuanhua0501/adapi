package com.racetime.xsad.model;

/**
 * 
* 项目名称：ad-service   
* 类名称：ResponseMessage   
* 类描述：返回终端广告信息实体类   
* 创建人：skg   
* 创建时间：2017-7-12 下午6:02:41   
* @version    
*
 */
public class ResponseMessage {

	private String id;
	
	private String code;
	
	private String message;
	
	private Object data;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public ResponseMessage setCode(String code) {
		this.code = code;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public ResponseMessage setMessage(String message) {
		this.message = message;
		return this;
	}

	public Object getData() {
		return data;
	}

	public ResponseMessage setData(Object data) {
		this.data = data;
		return this;
	}
}
