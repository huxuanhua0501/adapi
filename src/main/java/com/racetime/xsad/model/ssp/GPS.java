package com.racetime.xsad.model.ssp;

public class GPS {
	
	private Double lon;
	
	private Double lat;
	
	private Long timestamp;

	private Integer city_code;
	
	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getCity_code() {
		return city_code;
	}

	public void setCity_code(Integer city_code) {
		this.city_code = city_code;
	}

}
