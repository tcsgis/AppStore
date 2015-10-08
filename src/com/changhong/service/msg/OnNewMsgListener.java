package com.changhong.service.msg;

import java.util.List;
import java.util.Map;

import cn.changhong.chcare.core.webapi.bean.OfflineMessageBean;
import cn.changhong.chcare.message.RouterType;

public interface OnNewMsgListener {
	
	/**
	 * @param map map包含收到的所有消息，以RouterType分类，
	 * 				map不会为null,
	 * 				当getMsgType返回的类型大于1时，map.get()的返回值可能为null
	 * 				ChCareOfflineMessage<?>中消息的类型为msg.getMessage()。getType(),消息的条数为 (Integer) msg.getMessage().getVal()
	 */
	public void onNewMsg(Map<RouterType, List<OfflineMessageBean<?>>> map);
	
	/**
	 * @return 需要各个模块实现，返回值是模块需要的消息类型数组，
	 * 			通过onNewMsg获得的map里面只会有，各个模块需要的消息类型
	 */
	public RouterType[] getMsgType();
}