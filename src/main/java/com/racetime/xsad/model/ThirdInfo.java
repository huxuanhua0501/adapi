package com.racetime.xsad.model;

/**
 * 
* 项目名称：adapi   
* 类名称：ThirdInfo   
* 类描述：第三方信息表   
* 创建人：skg   
* 创建时间：2018-1-17 下午2:22:04   
* @version    
*
 */
public class ThirdInfo {

	private String adx_adslot_id;//上游广告位主键
	
	private String channel_id; //渠道(第三方id)
	
	private String channel_code; //渠道(第三方标识)

	private String request_url; // 请求URL
	
	private String app_id;		//app id

	private String adx_devicegroup_id;		//上游设备组id
	
	private String adslot_id;	//广告位id
	
	private int app_version_major; //主版本
	
	private int app_version_minor; //次版本
	
	private int app_version_micro; //最小版本
	
	private double price; //底价

	public String getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}

	public String getAdx_adslot_id() {
		return adx_adslot_id;
	}

	public void setAdx_adslot_id(String adx_adslot_id) {
		this.adx_adslot_id = adx_adslot_id;
	}

	public String getAdx_devicegroup_id() {
		return adx_devicegroup_id;
	}

	public void setAdx_devicegroup_id(String adx_devicegroup_id) {
		this.adx_devicegroup_id = adx_devicegroup_id;
	}

	public String getRequest_url() {
		return request_url;
	}

	public void setRequest_url(String request_url) {
		this.request_url = request_url;
	}

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public String getAdslot_id() {
		return adslot_id;
	}

	public void setAdslot_id(String adslot_id) {
		this.adslot_id = adslot_id;
	}

	public int getApp_version_major() {
		return app_version_major;
	}

	public void setApp_version_major(int app_version_major) {
		this.app_version_major = app_version_major;
	}

	public int getApp_version_minor() {
		return app_version_minor;
	}

	public void setApp_version_minor(int app_version_minor) {
		this.app_version_minor = app_version_minor;
	}

	public int getApp_version_micro() {
		return app_version_micro;
	}

	public void setApp_version_micro(int app_version_micro) {
		this.app_version_micro = app_version_micro;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getChannel_code() {
		return channel_code;
	}

	public void setChannel_code(String channel_code) {
		this.channel_code = channel_code;
	}
	
}
