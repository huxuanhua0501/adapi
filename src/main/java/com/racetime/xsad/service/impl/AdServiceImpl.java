package com.racetime.xsad.service.impl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.racetime.xsad.constants.Constants;
import com.racetime.xsad.dao.RedisDao;
import com.racetime.xsad.model.AdLog;
import com.racetime.xsad.model.Callback;
import com.racetime.xsad.model.CodeEnum;
import com.racetime.xsad.model.ResponseAd;
import com.racetime.xsad.model.ResponseMessage;
import com.racetime.xsad.model.ThirdInfo;
import com.racetime.xsad.model.ssp.BaseModel;
import com.racetime.xsad.model.ssp.Device;
import com.racetime.xsad.model.ssp.RequestAd;
import com.racetime.xsad.service.AdService;

@Service
public class AdServiceImpl implements AdService{
	
	public static Logger logger = LoggerFactory.getLogger(AdServiceImpl.class); 
	
	@Autowired
	protected RedisDao redisDao;
	
	@Autowired
	private RtbAdServiceImpl rtbAdServiceImpl;

	@Autowired
	private JdAdServiceImpl jdAdServiceImpl;
	
	private RequestAd getRequestAd(BaseModel baseModel) {
		//获取终端请求json数据，并转化为RequestAd对象
		RequestAd requestAd = null;
		try {
			requestAd = Constants.gson.fromJson(baseModel.getRequestJson(),RequestAd.class);
		} catch (Exception e) {
//			e.printStackTrace();
		}finally{
			new AdLog(baseModel.getRequest_id(),baseModel.getSourceType(),"1", null).writeStringTojson(baseModel.getRequestJson());//请求日志
		}
		
		if (requestAd == null || requestAd.getId() == null
				|| requestAd.getApp_id() == null
				|| requestAd.getAdslot_id() == null
				|| requestAd.getApi_version() == null
				|| requestAd.getAd_time() == null
				|| requestAd.getDevice() == null
				|| requestAd.getDevice().getDevice_id() == null
				|| requestAd.getDevice().getModel() == null
				|| requestAd.getDevice().getOs_type() == null
				|| requestAd.getDevice().getOs_version() == null
				|| requestAd.getDevice().getScreen_height() == null
				|| requestAd.getDevice().getScreen_width() == null
				|| requestAd.getDevice().getVendor() == null
				|| requestAd.getNetwork() == null
				|| requestAd.getNetwork().getConnection_type() == null
				|| requestAd.getNetwork().getIpv4() == null
				|| requestAd.getNetwork().getOperator_type() == null
				|| requestAd.getGps() == null
//				|| requestAd.getGps().getLat() == null
//				|| requestAd.getGps().getLon() == null
//				|| requestAd.getGps().getTimestamp() == null
				|| requestAd.getGps().getCity_code() == null) {
			return null;
		}
		return requestAd;
	}
	
	@Override
	public ResponseMessage getResponseMessage(BaseModel baseModel) throws Exception {
		RequestAd requestAd = getRequestAd(baseModel);
		if(requestAd == null)
			return getReponseMessage(null, CodeEnum.LACK_PARAMETER, null);

		List<ThirdInfo> thirdInfos = getThirdInfoConfig(baseModel,requestAd);

		while(thirdInfos != null && !thirdInfos.isEmpty()){
			ThirdInfo thirdInfo = selectThirdInfo(thirdInfos);
			if(thirdInfo == null) continue;
			ResponseAd responseAd = null;
			
			switch (thirdInfo.getChannel_code()) {
			case "BD":
				baseModel.setSourceType("BD");
				responseAd = rtbAdServiceImpl.getAd(requestAd,baseModel,thirdInfo);
				break;
			case "JD":

				break;
			case "PT":
				baseModel.setSourceType("PT");
				responseAd = jdAdServiceImpl.getAd(requestAd,baseModel,thirdInfo);
				break;
			default:
				break;
			}
			//物料白名单验证通过后返回广告
			if(responseAd != null && Constants.materialWhiteList.contains(thirdInfo.getAdx_adslot_id()+responseAd.getAd_url())){
				if(responseAd.getCallback_url() == null){
					List<String> urlList = new ArrayList<String>();
					StringBuffer callback_url = baseModel.getRequest_url()
							.append("/callback?request_id=")
							.append(baseModel.getRequest_id());
//							.append("&source_type=").append(baseModel.getSourceType());
					urlList.add(URLEncoder.encode(callback_url.toString(),"utf-8"));
					responseAd.setCallback_url(urlList);
				}
				
				List<ResponseAd> responseAdList = new ArrayList<ResponseAd>();
				responseAdList.add(responseAd);
				return getReponseMessage(requestAd.getId(),CodeEnum.EXIST_AD, responseAdList);
			}
		}
		
		return getReponseMessage(requestAd.getId(), CodeEnum.NO_AD, null);
	}
	
	/**
	 * 获取上游第三方信息
	 * @param baseModel
	 * @param requestAd
	 * @return
	 */
	private List<ThirdInfo> getThirdInfoConfig(BaseModel baseModel,RequestAd requestAd){
		String appType = Constants.sspAppTypes.get(requestAd.getApp_id());
		if(appType == null) return null;
		String adx_devicegroup_id = null;
		if(appType.equals("DOOH")){
			if(Constants.devices == null) return null;
			Device device = Constants.devices.get(requestAd.getDevice().getDevice_id());
			new AdLog(baseModel.getRequest_id(),baseModel.getSourceType(),"2", device).writeToJson();//设备信息
			if(device == null) return null;
			adx_devicegroup_id = device.getAdx_devicegroup_id();
		}
		List<String> upAdslot_ids = Constants.adslotRelations.get(requestAd.getAdslot_id());
		if(upAdslot_ids == null) return null;
		List<ThirdInfo> list = new ArrayList<ThirdInfo>();
		for(String upAdslot_id:upAdslot_ids){
			ThirdInfo thirdInfo = Constants.thirdInfos.get(upAdslot_id);
			if(appType.equals("DOOH")){
				if(thirdInfo == null || !thirdInfo.getAdx_devicegroup_id().equals(adx_devicegroup_id))
					continue;
			}
			list.add(thirdInfo);
		}
		new AdLog(baseModel.getRequest_id(),baseModel.getSourceType(),"3", list).writeToJson();//广告位配置等信息
		return list;
	}
	
	/**
	 * 平台自投放策略满足本次广告请求要求的，如多条，优先选择CPM出价最高的策略，
	 * 用平台投放价格+所有渠道对应广告位的底价之和做分母，各自的价格做分子计算概率，通过概率随机机制，
	 * 决策获取广告的渠道（平台、百度、京东等），若该渠道不返回广告，则继续随机选择第二个渠道
	 * @param thirdInfos
	 * @return
	 */
	private ThirdInfo selectThirdInfo(List<ThirdInfo> thirdInfos){
		double total = 0;
		List<ThirdInfo> removeTarget = new ArrayList<ThirdInfo>();
		Map<Double,ThirdInfo> map = new TreeMap<Double,ThirdInfo>();
		for(ThirdInfo thirdInfo:thirdInfos){
			if(thirdInfo == null) continue;
			if(thirdInfo.getChannel_code().equals("SDT")){
				if (Constants.strategys != null
						&& Constants.strategys.get(thirdInfo.getAdslot_id()) != null) {
					total += Constants.strategys.get(thirdInfo.getAdslot_id()).getPrice();
				}else{
//					thirdInfos.remove(thirdInfo);
					removeTarget.add(thirdInfo);
					continue;
				}
			}else{
				total += thirdInfo.getPrice();
			}
			map.put(total, thirdInfo);
		}
		
		Random r = new Random();
		double randomDouble = r.nextDouble()*total;
		Set<Double> keySet = map.keySet();
		Iterator<Double> iterator = keySet.iterator();
		while(iterator.hasNext()){
			Double prince = iterator.next();
			if(randomDouble <= prince){
//				thirdInfos.remove(map.get(prince));
				removeTarget.add(map.get(prince));
				thirdInfos.removeAll(removeTarget);
				return map.get(prince);
			}
		}
		return null;
	}
	
	@Override
	public ResponseAd getAd(RequestAd requestAd, BaseModel baseModel, ThirdInfo thirdInfo) throws Exception {
		
		return null;
	}

	/**
	 * 获取第三方信息
	 * @param requestAd
	 * @return
	 */
	/*protected List<ThirdInfo> getThirdInfo(String adslot_id){
		Object values = redisDao.getHash(Constants.HK_ADSLOT_CONFIG, adslot_id);
		if(values == null) return null;
		Type type = com.google.gson.internal.$Gson$Types
				.newParameterizedTypeWithOwner(null, ArrayList.class,ThirdInfo.class);
		return Constants.gson.fromJson(values.toString(), type);
	}*/
	/**
	 * 根据状态码填充返回信息对象
	 * @param responseCode
	 * @param responseAd
	 * @return
	 */
	private ResponseMessage getReponseMessage(String id, CodeEnum responseCode, List<ResponseAd> responseAd){
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setId(id);
		switch (responseCode) {
		case LACK_PARAMETER:
			responseMessage.setCode(CodeEnum.LACK_PARAMETER.getCode())
					.setMessage(CodeEnum.LACK_PARAMETER.getMessage());
			break;
		case EXIST_AD:
			responseMessage.setCode(CodeEnum.EXIST_AD.getCode())
					.setMessage(CodeEnum.EXIST_AD.getMessage())
					.setData(responseAd);
			break;
		default:
			responseMessage.setCode(CodeEnum.NO_AD.getCode())
					.setMessage(CodeEnum.NO_AD.getMessage());
			break;
		}
		return responseMessage;
	}

	@Override
	public void callback(String request_id, String source_type,String strategy_id) {
		String callback_type = "0";
		Callback callback = new Callback();
		try {
			String win_notice_url = "";
			String third_monitor_url = "";
//			if(source_type != null && source_type.equals("RTB")){
				win_notice_url = redisDao.getValue_db2(request_id + "_win_notice_url");
				if(win_notice_url != null){
					redisDao.remove_db2(request_id + "_win_notice_url");
					if(win_notice_url.equals("PT")){
						callback_type = "1";
					}else{
						callback_type = urlConnection(request_id,win_notice_url);
						win_notice_url = URLEncoder.encode(win_notice_url, "utf-8");
					}
				}else{
					win_notice_url = "";
				}
				
				/*third_monitor_url = redisDao.getValue_db2(request_id + "_third_monitor_url");
				if(third_monitor_url != null){
					redisDao.remove_db2(request_id + "_third_monitor_url");
					urlConnection(request_id,third_monitor_url);
					third_monitor_url = URLEncoder.encode(third_monitor_url, "utf-8");
				}else{
					third_monitor_url = "";
				}*/
//			}
			
			callback.setCallback_type(callback_type);
			callback.setWin_notice_url(win_notice_url);
			callback.setStrategy_id(strategy_id);
//			callback.setThird_monitor_url(third_monitor_url);
			
		} catch (Exception e) {
			logger.error("广告回调接口异常，["+request_id+"]堆栈轨迹如下：", e);
		}finally{
			//回调日志
			AdLog adLog = new AdLog(request_id,source_type,"7", callback);
			adLog.writeToDecodeJson();
			if(strategy_id != null && callback_type.equals("1"))
				redisDao.cacheSet("callback", Constants.gson.toJson(adLog));
		}
	}
	
	/**
	 * 访问URL 并记录错误日志
	 * @param request_id
	 * @param urlName
	 * @param url
	 * @throws Exception 
	 */
	private String urlConnection(String request_id,String url) {
		int i = 3;
		while(i-- > 0){
			HttpURLConnection connection = null;
			try{
				URL u = new URL(url);
				connection = (HttpURLConnection) u.openConnection();
				connection.setConnectTimeout(3000);
				int responseCode = connection.getResponseCode();
//				int responseCode = HttpClientUtil.sendBaiduGet(url);
				if(responseCode == 200 || responseCode == 404 || responseCode == 302) {
					if(i < 2){
						logger.error("广告回调request_id："+request_id+"第<"+(2-i)+">次重试成功！");
					}
					return "1";
				}
			}catch (Exception e) {
				logger.error("广告回调接口发生异常，堆栈轨迹如下：", e);
			}finally{
				if(connection != null)
					connection.disconnect();
			}
			logger.error("广告回调request_id："+request_id+"即将进行第<"+(3-i)+">次重试！");
		}
		return "0";
	}
	
	@Override
	public Device bindDevice(Device device,String adslot_id) {
		String up_device_id = (String)redisDao.getHash(Constants.HK_BD_DEVICE, device.getDevice_id());
		if(up_device_id == null || up_device_id.equals("")){
			//从备用设备库获取设备
			List<String> device_ids = (List<String>)redisDao.getHash(Constants.HK_STANDBY_DEVICE,adslot_id);
			if(device_ids == null || device_ids.size() == 0)
				return null;
			up_device_id = device_ids.get(0);
			//更新备用设备库
			if(device_ids.isEmpty()){
				redisDao.removeOneOfHash(Constants.HK_STANDBY_DEVICE, adslot_id);
			}else{
				redisDao.cacheHash(Constants.HK_STANDBY_DEVICE, adslot_id, Constants.gson.toJson(device_ids));
			}
			//插入已绑定设备库
			redisDao.cacheHash(Constants.HK_BD_DEVICE, device.getDevice_id(), up_device_id);
			//插入未匹配设备队列， 用于持久化数据库
			Map<String,String> map = new HashMap<String,String>();
			map.put("device_id", device.getDevice_id());
			map.put("up_device_id", up_device_id);
			map.put("adslot", adslot_id);
			redisDao.cacheList(Constants.SK_UN_BD_DEVICE, Constants.gson.toJson(map));
			
		}
		device.setDevice_id(up_device_id);	
		return device;
	}
}
