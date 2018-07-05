package com.racetime.xsad.model;

import java.util.List;


public class Strategy {

	private String strategy_id;
	
	private String adslot_id;

	private String app_id;
	
	private String channel_code;
	
	private String scene;
	
	private String times;
	
	private int total;
	
	private int count;
	
	private double price;

	private String citys;
	
	private String material_ids;
	
	private List<ResponseAd> materials;
	
	private int executeNum;

	private String serving_Info;
	
	public String getServing_Info() {
		return serving_Info;
	}

	public void setServing_Info(String serving_Info) {
		this.serving_Info = serving_Info;
	}

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public int getExecuteNum() {
		return executeNum;
	}

	public void setExecuteNum(int executeNum) {
		this.executeNum = executeNum;
	}

	public String getAdslot_id() {
		return adslot_id;
	}

	public void setAdslot_id(String adslot_id) {
		this.adslot_id = adslot_id;
	}

	public String getStrategy_id() {
		return strategy_id;
	}

	public void setStrategy_id(String strategy_id) {
		this.strategy_id = strategy_id;
	}

	public String getChannel_code() {
		return channel_code;
	}

	public void setChannel_code(String channel_code) {
		this.channel_code = channel_code;
	}

	public String getScene() {
		return scene;
	}

	public void setScene(String scene) {
		this.scene = scene;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getCitys() {
		return citys;
	}

	public void setCitys(String citys) {
		this.citys = citys;
	}

	public String getMaterial_ids() {
		return material_ids;
	}

	public void setMaterial_ids(String material_ids) {
		this.material_ids = material_ids;
	}

	public List<ResponseAd> getMaterials() {
		return materials;
	}

	public void setMaterials(List<ResponseAd> materials) {
		this.materials = materials;
	}

}
