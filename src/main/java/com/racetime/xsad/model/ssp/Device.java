package com.racetime.xsad.model.ssp;

/**
 * 
* 项目名称：ad-outdoors   
* 类名称：Device   
* 类描述：    设备实体类
* 创建人：skg   
* 创建时间：2018-1-16 下午2:25:05   
* @version    
*
 */
public class Device {

	private String device_id;

	private String vendor;

	private String model;

	private Integer screen_width;

	private Integer screen_height;

	private String os_type;

	private String os_version;
	
	private String ssp_devicegroup_id;
	
	private String adx_devicegroup_id;
	
	private String city_code;
	
	private String scene_id;

	public String getSsp_devicegroup_id() {
		return ssp_devicegroup_id;
	}

	public void setSsp_devicegroup_id(String ssp_devicegroup_id) {
		this.ssp_devicegroup_id = ssp_devicegroup_id;
	}

	public String getAdx_devicegroup_id() {
		return adx_devicegroup_id;
	}

	public void setAdx_devicegroup_id(String adx_devicegroup_id) {
		this.adx_devicegroup_id = adx_devicegroup_id;
	}

	public String getCity_code() {
		return city_code;
	}

	public void setCity_code(String city_code) {
		this.city_code = city_code;
	}

	public String getScene_id() {
		return scene_id;
	}

	public void setScene_id(String scene_id) {
		this.scene_id = scene_id;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Integer getScreen_width() {
		return screen_width;
	}

	public void setScreen_width(Integer screen_width) {
		this.screen_width = screen_width;
	}

	public Integer getScreen_height() {
		return screen_height;
	}

	public void setScreen_height(Integer screen_height) {
		this.screen_height = screen_height;
	}

	public String getOs_type() {
		return os_type;
	}

	public void setOs_type(String os_type) {
		this.os_type = os_type;
	}

	public String getOs_version() {
		return os_version;
	}

	public void setOs_version(String os_version) {
		this.os_version = os_version;
	}

}
