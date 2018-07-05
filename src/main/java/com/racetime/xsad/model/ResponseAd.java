package com.racetime.xsad.model;

import java.util.List;

/**
 * 
* 项目名称：adapi
* 类名称：ResponseAd   
* 类描述：   返回媒体端广告数据实体类
* 创建人：skg   
* 创建时间：2018-1-10 下午3:48:29   
* @version    
*
 */
public class ResponseAd {

	private String id;
	
	private String ad_title;

	private String ad_type;
	
	private String ad_url;
	
	private String adx_adslot_id;
	
	private Integer video_duration;

	private String description;
	
	private Integer ad_width;
	
	private Integer ad_height;
	
	private Integer ad_size;

	private String click_url;
	
	private String ad_tracking;
	
	private List<String> callback_url;
	
	private List<String> third_monitor_url;

	public String getAdx_adslot_id() {
		return adx_adslot_id;
	}

	public void setAdx_adslot_id(String adx_adslot_id) {
		this.adx_adslot_id = adx_adslot_id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAd_title() {
		return ad_title;
	}

	public void setAd_title(String ad_title) {
		this.ad_title = ad_title;
	}

	public String getAd_url() {
		return ad_url;
	}

	public void setAd_url(String ad_url) {
		this.ad_url = ad_url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getAd_width() {
		return ad_width;
	}

	public void setAd_width(Integer ad_width) {
		this.ad_width = ad_width;
	}

	public Integer getAd_height() {
		return ad_height;
	}

	public void setAd_height(Integer ad_height) {
		this.ad_height = ad_height;
	}

	public Integer getAd_size() {
		return ad_size;
	}

	public void setAd_size(Integer ad_size) {
		this.ad_size = ad_size;
	}

	public String getClick_url() {
		return click_url;
	}

	public void setClick_url(String click_url) {
		this.click_url = click_url;
	}

	public String getAd_tracking() {
		return ad_tracking;
	}

	public void setAd_tracking(String ad_tracking) {
		this.ad_tracking = ad_tracking;
	}

	public List<String> getCallback_url() {
		return callback_url;
	}

	public void setCallback_url(List<String> callback_url) {
		this.callback_url = callback_url;
	}

	public List<String> getThird_monitor_url() {
		return third_monitor_url;
	}

	public void setThird_monitor_url(List<String> third_monitor_url) {
		this.third_monitor_url = third_monitor_url;
	}

	public String getAd_type() {
		return ad_type;
	}

	public void setAd_type(String ad_type) {
		this.ad_type = ad_type;
	}

	public Integer getVideo_duration() {
		return video_duration;
	}

	public void setVideo_duration(Integer video_duration) {
		this.video_duration = video_duration;
	}

}
