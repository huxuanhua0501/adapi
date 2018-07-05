package com.racetime.xsad.model.ssp;

public class BaseModel {

	private String requestJson;
	
	private String request_id;
	
	private String ip;
	
	private StringBuffer request_url;

	private String sourceType;
	
	public String getRequestJson() {
		return requestJson;
	}

	public void setRequestJson(String requestJson) {
		this.requestJson = requestJson;
	}

	public String getRequest_id() {
		return request_id;
	}

	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public StringBuffer getRequest_url() {
		return request_url;
	}

	public void setRequest_url(StringBuffer request_url) {
		this.request_url = request_url;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	
}
