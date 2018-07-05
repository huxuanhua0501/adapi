package com.racetime.xsad.model;

/**
 * 
* 项目名称：ad-service   
* 类名称：Callback   
* 类描述：终端回调实体封装   
* 创建人：skg
* 创建时间：2017-7-18 下午1:32:05   
* @version    
*
 */
public class Callback {
	
	private String type;

	private String win_notice_url;
	
	private String third_monitor_url;

	private String callback_type;

	private String strategy_id;
	
	public String getStrategy_id() {
		return strategy_id;
	}

	public void setStrategy_id(String strategy_id) {
		this.strategy_id = strategy_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getWin_notice_url() {
		return win_notice_url;
	}

	public void setWin_notice_url(String win_notice_url) {
		this.win_notice_url = win_notice_url;
	}

	public String getThird_monitor_url() {
		return third_monitor_url;
	}

	public void setThird_monitor_url(String third_monitor_url) {
		this.third_monitor_url = third_monitor_url;
	}

	public String getCallback_type() {
		return callback_type;
	}

	public void setCallback_type(String callback_type) {
		this.callback_type = callback_type;
	}
	
}
