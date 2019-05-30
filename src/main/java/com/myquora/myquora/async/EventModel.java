package com.myquora.myquora.async;

import java.util.HashMap;
import java.util.Map;

public class EventModel {
	private EventType type;
	private int actorId;
	private int entityId;
	private int entityType;
	private int entityOwnerId; //例如：被喜欢的评论的userId
	
	// 存取其他延伸信息， 形式类似viewobject
	private Map<String, String> exts = new HashMap<>();

	// 不带参数的构造函数也需要写
	public EventModel(EventType type) {
		super();
		this.type = type;
	}

	public EventModel() {
		super();
	}

	public EventType getType() {
		return type;
	}

	public EventModel setType(EventType type) {
		this.type = type;
		return this;
	}

	public int getActorId() {
		return actorId;
	}

	public EventModel setActorId(int actorId) {
		this.actorId = actorId;
		return this;
	}

	public int getEntityId() {
		return entityId;
	}

	public EventModel setEntityId(int entityId) {
		this.entityId = entityId;
		return this;
	}

	public int getEntityType() {
		return entityType;
	}

	public EventModel setEntityType(int entityType) {
		this.entityType = entityType;
		return this;
	}

	public int getEntityOwnerId() {
		return entityOwnerId;
	}

	public EventModel setEntityOwnerId(int entityOwnerId) {
		this.entityOwnerId = entityOwnerId;
		return this;
	}

	public String getExt(String key) {
		return exts.get(key);
	}

	public EventModel setExt(String key, String value) {
		exts.put(key, value);
		return this;
	}
	
	
	public Map<String, String> getExts() {
		return exts;
	}

	public EventModel setExts(Map<String, String> exts) {
		this.exts = exts;
		return this;
	}
}
