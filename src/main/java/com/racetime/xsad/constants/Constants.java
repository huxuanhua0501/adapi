package com.racetime.xsad.constants;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.racetime.xsad.model.Strategy;
import com.racetime.xsad.model.ThirdInfo;
import com.racetime.xsad.model.ssp.Device;

/**
 * 
* 项目名称：adapi   
* 类名称：Constants   
* 类描述：   公共数据缓存
* 创建人：skg   
* 创建时间：2017-12-27 下午5:57:50   
* @version    
*
 */
public class Constants {

	public static final String HK_ADSLOT_CONFIG = "adslot_config"; //广告位配置

	public static final String HK_BD_DEVICE = "bd_device"; //上游已绑定设备hk

	public static final String HK_STANDBY_DEVICE = "standby_device"; //备用设备hk

	public static final String SK_UN_BD_DEVICE = "un_bd_device"; //未匹配设备sk

	public static final String SK_MATERIAL_WHITE_LIST = "material_white_list"; //物料白名单setkey

	public static final String HK_OWNPUT_MATERIAL = "ownput_material"; //自投放物料信息
	
	public static final String HK_STRATEGY = "strategy"; //投放策略

	
	
	public static final String HK_EXECUTE_NUM = "execute_num";//策略执行数量
	
	public static Map<String,Strategy> strategys; //投放策略
	
	public static Map<String, ThirdInfo> thirdInfos; //广告位配置信息
	
	public static Map<String,String> sspAppTypes;//下游APP类型    DOOH/OTT

	public static Map<String,List<String>> adslotRelations;//上下游广告位关系

	public static Map<String,String> adslotDeviceRelations;//设备广告位关系

	public static Map<String,Device> devices;//设备集
	
	public static List<String> materialWhiteList;//物料白名单
	
	
	public static Gson gson;
	
	static{
		gson = new Gson();
	}
}
