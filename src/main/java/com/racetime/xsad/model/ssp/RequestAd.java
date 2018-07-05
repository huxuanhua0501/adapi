package com.racetime.xsad.model.ssp;

/**
 * 
* 项目名称：ad-service   
* 类名称：RequestAd   
* 类描述：   终端请求数据实体类
* 创建人：skg   
* 创建时间：2017-7-12 下午5:22:19   
* @version    
*
 */
public class RequestAd {

	private String id;

	private String app_id;
	
	private String adslot_id;
	
	private String api_version;
	
	private String ad_time;

	private Device device;

	private NetWork network;

	private GPS gps;
	
	private Media media;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getApi_version() {
		return api_version;
	}

	public void setApi_version(String api_version) {
		this.api_version = api_version;
	}

	public String getAd_time() {
		return ad_time;
	}

	public void setAd_time(String ad_time) {
		this.ad_time = ad_time;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public NetWork getNetwork() {
		return network;
	}

	public void setNetwork(NetWork network) {
		this.network = network;
	}

	public GPS getGps() {
		return gps;
	}

	public void setGps(GPS gps) {
		this.gps = gps;
	}

	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

}
