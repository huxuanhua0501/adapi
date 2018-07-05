package com.racetime.xsad.dao;

import java.util.List;
import java.util.Map;

import com.racetime.xsad.model.ResponseAd;
import com.racetime.xsad.model.Strategy;
import com.racetime.xsad.model.ThirdInfo;
import com.racetime.xsad.model.ssp.Device;

public interface JdbcDao {

	public List<Map<String,String>> getSspAppType();

	public List<Map<String,String>> getAdslotRelation();

	public List<Map<String,String>> getAdslotDeviceRelation();
	
	public List<ThirdInfo> getThirdInfos();

	public List<Strategy> getStrategys();
	
	public List<ResponseAd> getMaterials(String[] ids);

	public List<Device> getDevices();
	
}
