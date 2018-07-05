package com.racetime.xsad.model;

public enum CodeEnum {

	LACK_PARAMETER("1001", "请求参数有误"),
	EXIST_AD("1002", "成功获取广告"), 
	NO_AD("1003", "无广告"),
	INTERFACE_ERROR("1004","接口返回失败");

	private CodeEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}

	private String code;

	private String message;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return this.code + ":" + this.message;

	}

	public static void main(String[] args) {

		System.out.println(CodeEnum.EXIST_AD);
	}
}
