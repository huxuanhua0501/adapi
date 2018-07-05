package com.racetime.xsad.service;

import java.io.UnsupportedEncodingException;

import com.racetime.xsad.model.ResponseAd;
import com.racetime.xsad.model.ResponseMessage;
import com.racetime.xsad.model.ThirdInfo;
import com.racetime.xsad.model.ssp.BaseModel;
import com.racetime.xsad.model.ssp.Device;
import com.racetime.xsad.model.ssp.RequestAd;

/**
 * 
* 项目名称：adapi   
* 类名称：AdService   
* 类描述：   广告服务逻辑处理接口
* 创建人：skg
* 创建时间：2017-7-11 上午10:43:25   
* @version    
*
 */
public interface AdService {

	/**
	 * 获取返回终端json串
	 * @param requestAd
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws Exception 
	 */
	public ResponseMessage getResponseMessage(BaseModel baseModel) throws Exception;
	
	/**
	 * 绑定设备信息
	 * @param device
	 * @return
	 */
	public Device bindDevice(Device device,String adslot);
	/**
	 * 终端请求获取广告，返回终端需要的广告信息
	 * @param requestAd
	 * @param baseModel
	 * @param thirdInfo
	 * @return
	 * @throws Exception
	 */
	public ResponseAd getAd( RequestAd requestAd, BaseModel baseModel, ThirdInfo thirdInfo) throws Exception;
	
	/**
	 * 终端回调
	 * @param request_id
	 * @param source_type
	 */
	public void callback(String request_id, String source_type,String strategy_id);
	
}
