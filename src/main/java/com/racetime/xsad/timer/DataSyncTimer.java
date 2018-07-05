package com.racetime.xsad.timer;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.racetime.xsad.constants.Constants;
import com.racetime.xsad.dao.JdbcDao;
import com.racetime.xsad.dao.RedisDao;
import com.racetime.xsad.model.ResponseAd;
import com.racetime.xsad.model.Strategy;
import com.racetime.xsad.model.ThirdInfo;
import com.racetime.xsad.model.ssp.Device;
import com.racetime.xsad.util.DateUtil;

/**
 * 
 * @author skg
 *
 */
@Component
@Lazy(false) 
public class DataSyncTimer {

	public static Logger logger = LoggerFactory.getLogger(DataSyncTimer.class);  
	
	@Autowired
	private RedisDao redisDao;
	
	@Autowired
	private JdbcDao jdbcDao;
	
	/**
	 * 刷新日志记录，每隔1分钟执行一次
	 */
	@Scheduled(fixedRate = 1000*60*1) 
	public void logSync() {
		sspAppTypeSync();
		AdslotRelationSync();
		thirdInfoSync();
		deviceSync();
		materialWhiteSync();
		//刷新日志记录
		logger.info("");
	}
	
	/**
	 * 同步策略，1秒钟
	 */
	@Scheduled(fixedRate = 1000*1) 
	public void dataSync() {
		strategySync();
		//redis记录
		redisDao.cacheHash("heartbeat", "strategyTimer", "广告服务缓存刷新时间："+DateUtil.getTime());
		
	}
	
	/**
	 * ssp  APP类型同步
	 */
	private void sspAppTypeSync(){
		List<Map<String,String>> sspAppTypes = jdbcDao.getSspAppType();
		Map<String,String> map = null;
		if(sspAppTypes != null){
			map = new HashMap<String,String>();
			for(Map<String,String> sspType:sspAppTypes){
				map.put(sspType.get("app_id"), sspType.get("app_type"));
			}
		}
		Constants.sspAppTypes = map;
	}

	
	/**
	 * 上下游广告位对应关系
	 */
	private void AdslotRelationSync(){
		List<Map<String,String>> adslotRelations = jdbcDao.getAdslotRelation();
		Map<String,List<String>> map = null;
		if(adslotRelations != null){
			map = new HashMap<String,List<String>>();
			for(Map<String,String> adslotRelation:adslotRelations){
				String ssp_adslot_id = adslotRelation.get("ssp_adslot_id");
				if(map.containsKey(ssp_adslot_id)){
					map.get(ssp_adslot_id).add(adslotRelation.get("adx_adslot_id"));
				}else{
					List<String> list = new ArrayList<String>();
					list.add(adslotRelation.get("adx_adslot_id"));
					map.put(adslotRelation.get("ssp_adslot_id"), list);
				}
				
			}
		}
		Constants.adslotRelations = map;
	}
	
	/**
	 * 上游广告位配置信息
	 */
	private void thirdInfoSync(){
		List<ThirdInfo> thirdInfos = jdbcDao.getThirdInfos();
		Map<String,ThirdInfo> map = null;
		if(thirdInfos != null){
			map = new HashMap<String,ThirdInfo>();
			for(ThirdInfo thirdInfo:thirdInfos){
				map.put(thirdInfo.getAdslot_id(), thirdInfo);
			}
		}
		Constants.thirdInfos = map;
	}
	
	/**
	 * 同步投放计划（策略）
	 */
	private void strategySync(){
		List<Strategy> strategys = jdbcDao.getStrategys();
		Map<String,Strategy> map = new HashMap<String,Strategy>();
		if(strategys != null){
			for(Strategy strategy:strategys){
//				String adslot_id = strategy.getAdslot_id();
				String[] times = strategy.getTimes().split(",");
				String hour = new SimpleDateFormat("H").format(new Date());
				if(strategy.getExecuteNum() >= strategy.getTotal() || !Arrays.asList(times).contains(hour))
					continue;
				//redis获取策略已投放数量，如果超过投放计划总量，放弃添加策略
				String countStr = (String) redisDao.getHash(Constants.HK_EXECUTE_NUM, strategy.getStrategy_id()+"_"+DateUtil.getHours());
				if(countStr == null) countStr = "0";
				int count = Integer.parseInt(countStr);
				if(count>=strategy.getTotal()){
					continue;
				}
				
				//获取投放json
				String serving_info = strategy.getServing_Info();
				if(serving_info == null || serving_info.equals("")) continue;
				Type type = com.google.gson.internal.$Gson$Types
						.newParameterizedTypeWithOwner(null, ArrayList.class,Strategy.class);
				List<Strategy> serving_info_strategys = Constants.gson.fromJson(serving_info, type);
				for(Strategy info:serving_info_strategys){
					String adslot_id = info.getAdslot_id();
					if(adslot_id == null || adslot_id.equals("") 
							|| info.getApp_id() == null || info.getApp_id().equals("")
							|| info.getMaterial_ids() == null || info.getMaterial_ids().equals("") 
							|| info.getPrice() == 0)
						continue;
					info.setChannel_code(strategy.getChannel_code());
					info.setCitys(strategy.getCitys());
					info.setExecuteNum(strategy.getExecuteNum());
					info.setScene(strategy.getScene());
					info.setStrategy_id(strategy.getStrategy_id());
					info.setTimes(strategy.getTimes());
					info.setTotal(strategy.getTotal());
					info.setCount(count);
					info.setServing_Info(strategy.getServing_Info());

					//没超过投放计划总量
					if(!map.containsKey(adslot_id) || map.get(adslot_id).getPrice()<info.getPrice()){
						List<ResponseAd> materials = jdbcDao.getMaterials(info.getMaterial_ids().split(","));
						info.setMaterials(materials);
						map.put(adslot_id, info);
					}
				}
			}
		}
		Constants.strategys = map;
	}
	
	/**
	 * 设备同步
	 */
	private void deviceSync(){
		List<Device> devices = jdbcDao.getDevices();
		Map<String,Device> map = null ;
		if(devices != null){
			map = new HashMap<String,Device>();
			for(Device device:devices){
				map.put(device.getDevice_id(), device);
			}
		}
		Constants.devices = map;
	}
	
	/**
	 * 广告位，设备关系同步，（下游广告位id+设备id获取上游广告位id，适用于DOOH类型）
	 */
	/*private void adslotDeviceRelationSync(){
		List<Map<String,String>> adslotDeviceRelations = jdbcDao.getAdslotDeviceRelation();
		Map<String,String> map = null;
		if(adslotDeviceRelations != null){
			map = new HashMap<String,String>();
			for(Map<String,String> adslotDeviceRelation:adslotDeviceRelations){
				map.put(adslotDeviceRelation.get("ssp_adslot_id") + adslotDeviceRelation.get("device_id"), adslotDeviceRelation.get("adx_adslot_id"));
			}
		}
		Constants.adslotDeviceRelations = map;
	}*/
	
	/**
	 * 物料白名单同步
	 */
	private void materialWhiteSync() {
		List<ResponseAd> materials = jdbcDao.getMaterials(null);
		List<String> materialWhiteList = null;
		if(materials != null){
			materialWhiteList = new ArrayList<String>();
			for(ResponseAd material:materials){
				materialWhiteList.add(material.getAdx_adslot_id()+material.getAd_url());
			}
		}
		Constants.materialWhiteList = materialWhiteList;
	}
	
	
	
	
	
	
	
	/**
	 * 同步策略数据
	 */
	/*private void syncStrategy(){
		Map<String,Object> strategyMap = redisDao.getHashAll(Constants.HK_STRATEGY);
		Map<String, Strategy> strategys = null;
		if(strategyMap != null){
			strategys = new HashMap<String, Strategy>();
			Set<Entry<String, Object>> entres = strategyMap.entrySet();
			 Iterator<Entry<String, Object>> iterator = entres.iterator();
			 while(iterator.hasNext()){
				 Entry<String, Object> entry = iterator.next();
				 //处理value
				 Strategy strategy = Constants.gson.fromJson((String)entry.getValue(), Strategy.class);
				 
				 strategys.put(entry.getKey(), strategy);
			 }
		}
		Constants.strategys = strategys;
	}
	
	private void syncThirdInfo(){
		Map<String,Object> map = redisDao.getHashAll(Constants.HK_ADSLOT_CONFIG);
		Map<String, List<ThirdInfo>> thirdInfos = null;
		if(map != null){
			thirdInfos = new HashMap<String, List<ThirdInfo>>();
			Set<Entry<String, Object>> entres = map.entrySet();
			 Iterator<Entry<String, Object>> iterator = entres.iterator();
			 while(iterator.hasNext()){
				 Entry<String, Object> entry = iterator.next();
				 //处理value
				 Type type = com.google.gson.internal.$Gson$Types
							.newParameterizedTypeWithOwner(null, ArrayList.class,ThirdInfo.class);
				 List<ThirdInfo> list = Constants.gson.fromJson((String) entry.getValue(), type);
					
				 thirdInfos.put(entry.getKey(), list);
			 }
		}
		Constants.thirdInfos = thirdInfos;
	}*/
}
