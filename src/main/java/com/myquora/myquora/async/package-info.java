/**
 * 
 */
/**
 * @author miaohj
 * EventProducer
 * EventConsumer
 * EventHandler接口
 * EventType枚举优先级
 * EventModel
 * 具体Handler
 * applicationContext.getBeanOfType(EventHandler.class)获取所有具体Handler-->
 * 查找每个Handler support的事件 -->
 * 转换为config<事件, List<Handler>>
 * 多线程，异步处理 redis list lpush brpop 反序列化
 * 同时添加响应代码到生成 key工具 commentController等
 */
package com.myquora.myquora.async;