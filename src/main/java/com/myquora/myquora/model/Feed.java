package com.myquora.myquora.model;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;

public class Feed {
	private int id;;
	private int type;
	private int userId;
	private Date createdDate;
	// JSON格式，可存取多个字段
	private String data;
	private JSONObject dataJSON = null;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
		dataJSON = JSONObject.parseObject(data);
	}
	
	// 从data中取值
	public String get(String key) {
		return dataJSON == null ? null : dataJSON.getString(key);
	}
	
	// 在setData中做过了
//	public void setDataJSON(JSONObject dataJSON) {
//		this.dataJSON = dataJSON;
//	}
	
}
