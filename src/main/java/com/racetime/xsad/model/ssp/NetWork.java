package com.racetime.xsad.model.ssp;

/**
 * 
* 项目名称：ad-outdoors   
* 类名称：Network   
* 类描述：网络环境实体类   
* 创建人：skg   
* 创建时间：2018-1-10 下午2:09:47   
* @version    
*
 */
public class NetWork {

	private String connection_type;

	private String operator_type;
	
	private String ipv4;

	public String getConnection_type() {
		return connection_type;
	}

	public void setConnection_type(String connection_type) {
		this.connection_type = connection_type;
	}

	public String getOperator_type() {
		return operator_type;
	}

	public void setOperator_type(String operator_type) {
		this.operator_type = operator_type;
	}

	public String getIpv4() {
		return ipv4;
	}

	public void setIpv4(String ipv4) {
		this.ipv4 = ipv4;
	}
	
}
