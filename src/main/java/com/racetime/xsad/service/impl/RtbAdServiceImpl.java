package com.racetime.xsad.service.impl;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.protobuf.ByteString;
import com.racetime.xsad.model.AdLog;
import com.racetime.xsad.model.ResponseAd;
import com.racetime.xsad.model.ThirdInfo;
import com.racetime.xsad.model.ssp.BaseModel;
import com.racetime.xsad.model.ssp.RequestAd;
import com.racetime.xsad.proto.TsUiApi;
import com.racetime.xsad.proto.TsUiApi.MaterialMeta;
import com.racetime.xsad.proto.TsUiApi.MaterialType;
import com.racetime.xsad.util.HttpClientUtil;

@Service
public class RtbAdServiceImpl extends AdServiceImpl {

	@Override
	public ResponseAd getAd(RequestAd requestAd, BaseModel baseModel, ThirdInfo thirdInfo) throws Exception {
		TsUiApi.TsApiRequest.Builder tsApiRequestBuilder = getTsApiRequest(baseModel, requestAd, thirdInfo);
		TsUiApi.TsApiRequest tsApiRequest= tsApiRequestBuilder.build();
		new AdLog(baseModel.getRequest_id(),baseModel.getSourceType(), "4", null).writeProtoBufToJson(tsApiRequest);//4.请求百度日志
		
		int num = 3;
		//请求广告，失败重试2次
		while(num-- > 0){
			byte[] responseByt = HttpClientUtil.sendBaiduPost(thirdInfo.getRequest_url(), tsApiRequest.toByteArray());
			if(responseByt == null || responseByt.length == 0) {
				logger.error("广告请求request_id："+baseModel.getRequest_id()+"即将进行第<"+(3-num)+">次重试！");
				continue;
			}
			TsUiApi.TsApiResponse tsApiResponse = TsUiApi.TsApiResponse.parseFrom(responseByt);
			//请求响应无误，正确返回广告
			if(tsApiResponse != null) {
				if(num<2) logger.error("广告请求request_id："+baseModel.getRequest_id()+"第<"+(2-num)+">次重试请求成功！");
				new AdLog(baseModel.getRequest_id(),baseModel.getSourceType(), "5", null).writeProtoBufToJson(tsApiResponse);//5.百度返回日志
				if(tsApiResponse.getRequestId().toStringUtf8().equals(baseModel.getRequest_id())
						&& tsApiResponse.getErrorCode() == 0
						&& tsApiResponse.getAdsCount() > 0) {
					//如果返回多个广告?  目前只获取第一个广告
					TsUiApi.Ad ad = tsApiResponse.getAds(0);
					return getResponseAd(ad,baseModel);
				}
				break;
			}
		}
		return null;
	}
	
	/**
	 * 封装请求广告proto数据
	 * @param request_id
	 * @param requestAd
	 * @return
	 */
	private TsUiApi.TsApiRequest.Builder getTsApiRequest(BaseModel baseModel, RequestAd requestAd, ThirdInfo thirdInfo){
		TsUiApi.TsApiRequest.Builder tsApiRequest = TsUiApi.TsApiRequest.newBuilder();
		//requestId
		tsApiRequest.setRequestId(ByteString.copyFromUtf8(baseModel.getRequest_id()));
		//api版本
		tsApiRequest.setApiVersion(TsUiApi.Version.newBuilder()
				.setMajor(thirdInfo.getApp_version_major())
				.setMinor(thirdInfo.getApp_version_minor())
				.setMicro(thirdInfo.getApp_version_micro()));
		//资源方id
		tsApiRequest.setAppId(ByteString.copyFromUtf8(thirdInfo.getApp_id()));
		/*****************广告位信息****************/
		TsUiApi.SlotInfo.Builder slotInfo = TsUiApi.SlotInfo.newBuilder();
		//广告位id
		slotInfo.setAdslotId(ByteString.copyFromUtf8(thirdInfo.getAdslot_id()));
		tsApiRequest.setSlot(slotInfo);
		/*****************设备参数****************/
		TsUiApi.Device.Builder device = TsUiApi.Device.newBuilder();
		TsUiApi.UdId.Builder udId = TsUiApi.UdId.newBuilder();
		//设备id类型
		udId.setIdType(TsUiApi.UdIdType.MEDIA_ID);
		//设备id
		udId.setId(ByteString.copyFromUtf8(requestAd.getDevice().getDevice_id()));
		device.setUdid(udId);
		//操作系统
		device.setOsType(TsUiApi.OsType.valueOf(requestAd.getDevice().getOs_type()));
		//操作系统版本
		String[] osVersion = requestAd.getDevice().getOs_version().split("\\.");
		int osMajor = 0;
		if(osVersion != null && osVersion.length>0){
			osMajor = Integer.parseInt(osVersion[0]);
		}
		device.setOsVersion(TsUiApi.Version.newBuilder().setMajor(osMajor));
		//设备厂商
		device.setVendor(ByteString.copyFromUtf8(requestAd.getDevice().getVendor()));
		//机型
		device.setModel(ByteString.copyFromUtf8(requestAd.getDevice().getModel()));
		//设备屏幕尺寸
		TsUiApi.Size.Builder size = TsUiApi.Size.newBuilder();
		size.setWidth(requestAd.getDevice().getScreen_width());
		size.setHeight(requestAd.getDevice().getScreen_height());
		device.setScreenSize(size);
		tsApiRequest.setDevice(device);
		/*****************网络参数****************/
		TsUiApi.Network.Builder network = TsUiApi.Network.newBuilder();
//		network.setIpv4(ByteString.copyFromUtf8(requestAd.getNetwork().getIpv4()));
		network.setIpv4(ByteString.copyFromUtf8(baseModel.getIp()));
		network.setConnectionType(TsUiApi.Network.ConnectionType.valueOf(requestAd.getNetwork().getConnection_type()));
		network.setOperatorType(TsUiApi.Network.OperatorType.valueOf(requestAd.getNetwork().getOperator_type()));
		tsApiRequest.setNetwork(network);
		
		return tsApiRequest;
	}
	
	/**
	 * 封装返回ad信息
	 * @param ad
	 * @param baseModel
	 * @return
	 * @throws Exception
	 */
	private ResponseAd getResponseAd(TsUiApi.Ad ad,BaseModel baseModel) throws Exception{
		if(ad.getMaterialMetasCount()<=0) return null;
		MaterialMeta materialMeta = ad.getMaterialMetas(0);
		ResponseAd responseAd = new ResponseAd();
		String ad_url = null;
		if (materialMeta.getMaterialType().equals(MaterialType.IMAGE)) {
			ad_url = materialMeta.getImageSrc(0).toStringUtf8();
			responseAd.setAd_type("IMAGE");
		}
		if (materialMeta.getMaterialType().equals(MaterialType.VEDIO)) {
			ad_url = materialMeta.getVideoUrl().toStringUtf8();
			responseAd.setAd_type("VEDIO");
		}
		//有图片返回广告，无图片不返回广告
		if(ad_url == null) return null;
		responseAd.setAd_title(ad.getTitle().toStringUtf8());
		/***********************/
//		if(ad_url.equals("http://jpaccess.baidu.com/material/a29e57b1_5589280_1526875779.jpg"))
			ad_url = "http://jpaccess.baidu.com/material/6B406381B4B58F25BFDAA130BA09D7D2.jpg";
		/***********************/
		responseAd.setAd_url(ad_url);
		responseAd.setVideo_duration(materialMeta.getVideoDuration());
		responseAd.setDescription(ad.getDescription().toStringUtf8());
		responseAd.setAd_width(materialMeta.getMaterialWidth());
		responseAd.setAd_height(materialMeta.getMaterialHeight());
		responseAd.setAd_size(materialMeta.getMaterialSize());
		responseAd.setClick_url(materialMeta.getClickUrl().toStringUtf8());
		if(ad.getAdTrackingCount()>0 && ad.getAdTracking(0).getTrackingUrlCount()>0){
			responseAd.setAd_tracking(ad.getAdTracking(0).getTrackingUrl(0).toStringUtf8());
		}
		if(ad.getWinNoticeUrlCount()>0){
			String win_notice_url = ad.getWinNoticeUrl(0).toStringUtf8();
			if(win_notice_url != null && !win_notice_url.equals("")){
				redisDao.cacheValue_db2(baseModel.getRequest_id() + "_win_notice_url", win_notice_url, 60*15);
			}
		}
		if(ad.getThirdMonitorUrlCount()>0){
//			String third_monitor_url = .getThirdMonitorUrl(0).toStringUtf8();
//			if(third_monitor_url != null && !third_monitor_url.equals("")){
//				redisDao.cacheValue_db2(baseModel.getRequest_id() + "_third_monitor_url", third_monitor_url, 60*10);
//			}
			List<String> third_monitor_urls = new ArrayList<String>();
			for(ByteString third_monitor_url:ad.getThirdMonitorUrlList()){
				third_monitor_urls.add(URLEncoder.encode(third_monitor_url.toStringUtf8(),"utf-8"));
				
				redisDao.cacheValue_db2(baseModel.getRequest_id() + "_third_monitor_url", third_monitor_url.toStringUtf8(), 60*15);
			}
			responseAd.setThird_monitor_url(third_monitor_urls);
		}
		
		return responseAd;
	}
}
