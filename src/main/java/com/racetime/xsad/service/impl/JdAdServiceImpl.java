package com.racetime.xsad.service.impl;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.racetime.xsad.constants.Constants;
import com.racetime.xsad.model.AdLog;
import com.racetime.xsad.model.ResponseAd;
import com.racetime.xsad.model.Strategy;
import com.racetime.xsad.model.ThirdInfo;
import com.racetime.xsad.model.ssp.BaseModel;
import com.racetime.xsad.model.ssp.RequestAd;

@Service
public class JdAdServiceImpl extends AdServiceImpl {

	@Override
	public ResponseAd getAd(RequestAd requestAd, BaseModel baseModel, ThirdInfo thirdInfo) throws Exception {
		Strategy strategy = Constants.strategys.get(thirdInfo.getAdx_adslot_id());
		new AdLog(baseModel.getRequest_id(),baseModel.getSourceType(), "4", strategy).writeToJson();//4.平台投放策略
		if(strategy == null) return null;
		List<ResponseAd> responseAds = strategy.getMaterials();
		if(responseAds != null && !responseAds.isEmpty()){
			int index = (int)(Math.random()*responseAds.size());
			ResponseAd responseAd = responseAds.get(index);
			
			List<String> urlList = new ArrayList<String>();
			StringBuffer callback_url = baseModel.getRequest_url()
					.append("/callback?request_id=")
					.append(baseModel.getRequest_id())
					.append("&strategy_id=")
					.append(strategy.getStrategy_id());
//					.append("&source_type=")
//					.append(baseModel.getSourceType());
			urlList.add(URLEncoder.encode(callback_url.toString(),"utf-8"));
			
			/******模拟测试*******/
			urlList.add(URLEncoder.encode(callback_url.toString(),"utf-8"));
			List<String> thirdList = new ArrayList<String>();
			thirdList.add(URLEncoder.encode("www.baidu.com","utf-8"));
			thirdList.add(URLEncoder.encode("www.baidu.com","utf-8"));
			responseAd.setThird_monitor_url(thirdList);
			/******模拟测试*******/
			
			responseAd.setCallback_url(urlList);
			
			new AdLog(baseModel.getRequest_id(),baseModel.getSourceType(), "5", responseAd).writeToJson();//6.平台獲取到的投放物料
			
			responseAd.setAdx_adslot_id(null);
			redisDao.cacheValue_db2(baseModel.getRequest_id() + "_win_notice_url", "PT", 60*15);
			return responseAd;
		}
		return null;
	}
}
