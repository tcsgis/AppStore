package cn.changhong.chcare.core.webapi;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import cn.changhong.chcare.core.webapi.bean.DiaryComment;
import cn.changhong.chcare.core.webapi.bean.DiaryInfo;
import cn.changhong.chcare.core.webapi.bean.Family;
import cn.changhong.chcare.core.webapi.bean.FamilyDateView;
import cn.changhong.chcare.core.webapi.bean.MsgThreadViewBean;
import cn.changhong.chcare.core.webapi.bean.OfflineMessageBean;
import cn.changhong.chcare.core.webapi.bean.OfflineMessageContent;
import cn.changhong.chcare.core.webapi.bean.ResponseBean;
import cn.changhong.chcare.core.webapi.bean.ResponseBeanWithRange;
import cn.changhong.chcare.core.webapi.photowalll.bean.PhotoView;
import cn.changhong.chcare.core.webapi.server.IOfflineMessageService;
import cn.changhong.chcare.core.webapi.util.HttpRequestException;
import cn.changhong.chcare.message.RouterType;

import com.changhong.service.msg.MessageType;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

public class ChCareWebApiOfflineMessageApi extends IOfflineMessageService {
	@Override
	public ResponseBean getUserOfflineMessage(long startIndex, long endIndex,
			int count, int[] type, int mode) throws HttpRequestException {
		String url = BASE_URL + "msg";
		StringBuilder sb = new StringBuilder();
		if (startIndex > 0) {
			sb.append("startIndex=").append(startIndex).append("&");
		}
		if (endIndex > 0) {
			sb.append("endIndex=").append(endIndex).append("&");
		}
		if (count > 0) {
			sb.append("count=").append(count).append("&");
		}
		if (type != null && type.length > 0) {
			sb.append("type=");
			for (int i = 0; i < type.length; i++) {
				if(i<type.length-1){
					sb.append(type[i]).append(",");
				}else{
					sb.append(type[i]);
				}
				
			}
			sb.append("&");
		}
		if (mode == 0 || mode == 1) {
			sb.append("mode=").append(mode).append("&");
		}
		if (sb.length() > 0) {
			String params = sb.toString().substring(0, sb.length() - 1);
			url += "?" + params;
		}
		
		String response = this.baseGetRequestUtil(url);

		ResponseBean<?> result = this.transToBean(response);
		if (result != null && result.getState() >= 0) {
			Type beanType = new TypeToken<ResponseBean<List<OfflineMessageBean>>>() {
			}.getType();
			ResponseBean responseBean = this.transToBean(response, beanType);
			result = transToLocalOfflineMessage(responseBean);
		}

		return result;
	}

	private ResponseBean transToLocalOfflineMessage(
			ResponseBean<List<OfflineMessageBean<?>>> responseBean) {

		if (responseBean.getState() < 0) {
			return responseBean;
		}

		ResponseBean<List<OfflineMessageBean<?>>> result = new ResponseBean<List<OfflineMessageBean<?>>>();
		result.setDesc(responseBean.getDesc());
		result.setState(responseBean.getState());

		List<OfflineMessageBean<?>> msgs = responseBean.getData();
		if (msgs != null) {
			List<OfflineMessageBean<?>> offlineMsgs = new ArrayList<OfflineMessageBean<?>>();
			for (Iterator<?> it = msgs.iterator(); it.hasNext();) {
				Object obj = it.next();
				OfflineMessageBean<?> offlineMsg = null;
				if (obj instanceof OfflineMessageBean) {
					OfflineMessageBean<?> tempBean = (OfflineMessageBean<?>) obj;
					Object val = tempBean.getVal();
					if (val instanceof String) {
						// 判断消息类型解析成相应的消息对象
						tempBean.setValString(val.toString());
						offlineMsg = transMsgBean(tempBean);
					}

				} else if (obj instanceof LinkedTreeMap) {
					LinkedTreeMap<String, Object> mapObj = (LinkedTreeMap<String, Object>) it.next();
					System.out.println(this.gson.toJson(obj)
							+ obj.getClass().getName());
					offlineMsg = this.getMessage(mapObj);
					if(offlineMsg.getVal() != null){
						offlineMsg.setValString(this.gson.toJson(offlineMsg.getVal()));
					}
				}
				offlineMsgs.add(offlineMsg);

			}
			result.setData(offlineMsgs);
		}
		return result;
	}
	
	public OfflineMessageBean<?> transMsgBean(OfflineMessageBean tempBean){
		
		try {
			String gsonStr = tempBean.getValString();
			
			int type = tempBean.getType();
			if (type >= 100 && type < 200) {
				tempBean.setVal(this.transToMessageContent(
						tempBean.getType(), gsonStr));
				tempBean.setRouterType(RouterType.FAMILY_MEMBER_SERVICE_ROUTER.getValue());
			} else if (type >= 210 && type < 220) {
				tempBean.setVal(this.transToAnniversaryContent(
						tempBean.getType(), gsonStr));
				tempBean
						.setRouterType(RouterType.FAMILY_ANNI_SERVICE_ROUTER.getValue());
			} else if (type >= 220 && type < 223) {
				tempBean.setVal(this.transToFamilyDiaryContent(
						tempBean.getType(), gsonStr));
				tempBean
						.setRouterType(RouterType.FAMILY_DIARY_SERVICE_ROUTER.getValue());
			} else if (type >= 200 && type < 300) {
				tempBean.setVal(this.transToPhotoWallContent(
						tempBean.getType(), gsonStr));
				tempBean
						.setRouterType(RouterType.FAMILY_PHOTOWALL_SERVICE_ROUTER.getValue());
			} else if (type >= 300 && type < 400) {
				tempBean
						.setRouterType(RouterType.FAMILY_MESSAGEBOARD_SERVICE_ROUTER.getValue());
			} else if (type >= 400 && type < 500) {
				tempBean
						.setRouterType(RouterType.FAMILY_HEALTHMANAGER_SERVICE_ROUTER.getValue());
			} else if (type >= 500 && type < 600) {
				tempBean
						.setRouterType(RouterType.FAMILY_SYSTEM_SERVICE_ROUTER.getValue());
			} else if (type >= 0x800 && type < 0x8ff) {
				tempBean
				.setRouterType(RouterType.BBS_SERVICE_ROUTER.getValue());
				tempBean.setVal(this.transToMessageContent(
						tempBean.getType(), gsonStr));
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return tempBean;
	}

	private Object transToBBSContent(int type, String gsonStr) {
		Object obj = gsonStr;
		switch (type) {
		case 220:
			obj = this.gson.fromJson(gsonStr, DiaryInfo.class);
			break;
		case 222:
			obj = this.gson.fromJson(gsonStr, DiaryComment.class);
			break;
		default:
			break;
		}

		return obj;
	}
	
	private Object transToFamilyDiaryContent(int type, String gsonStr) {
		Object obj = gsonStr;
		switch (type) {
		case 220:
			obj = this.gson.fromJson(gsonStr, DiaryInfo.class);
			break;
		case 222:
			obj = this.gson.fromJson(gsonStr, DiaryComment.class);
			break;
		default:
			break;
		}

		return obj;
	}

	private Object transToPhotoWallContent(int type, String msg) {
		Object obj = msg;
		switch (type) {
		case 200:
			obj = this.gson.fromJson(msg, PhotoView.class);
			break;
		case 201:
			System.out.println(type + "," + msg);
			try {
				obj = Integer.valueOf(msg.trim());
			} catch (NumberFormatException ex) {
				obj = -1;
			}
			break;
		}
		return obj;
	}

	private Object transToMessageContent(int type, String msg) {
		Object obj = null;
		switch (type) {
		case 150:// Family
			obj = this.gson.fromJson(msg, Family.class);
			break;
		case 0x802:// MsgThreadViewBean
		case 0x812:// MsgThreadViewBean
			obj = this.gson.fromJson(msg, MsgThreadViewBean.class);
			break;
		default:// OfflineMessageContent
			obj = this.gson.fromJson(msg, OfflineMessageContent.class);
			break;
		}
		return obj;
	}

	private Object transToAnniversaryContent(int type, String msg) {
		Object obj = msg;
		obj = this.gson.fromJson(msg, FamilyDateView.class);
		return obj;
	}

	private OfflineMessageBean<?> transToFamilyMemberContent(
			LinkedTreeMap<String, Object> msg, int type) {
		OfflineMessageBean<?> result = null;
		Type beanType = null;
		switch (type) {
		case 150:// Family
			beanType = new TypeToken<OfflineMessageBean<Family>>() {
			}.getType();
			break;
		default:// OfflineMessageContent
			beanType = new TypeToken<OfflineMessageBean<OfflineMessageContent>>() {
			}.getType();
			break;
		}
		result = this.gson.fromJson(this.gson.toJson(msg), beanType);
		return result;
	}

	private OfflineMessageBean<?> getMessage(LinkedTreeMap<String, Object> msg) {
		double type = Double.parseDouble(msg.get("Type").toString());
		OfflineMessageBean<?> msgBean = new OfflineMessageBean<Object>();
		
		if (type >= 100 && type < 200) {
			msgBean = transToFamilyMemberContent(msg, (int) type);
			msgBean.setRouterType(RouterType.FAMILY_MEMBER_SERVICE_ROUTER.getValue());
		} else if (type >= 210 && type < 220) {
			msgBean.setRouterType(RouterType.FAMILY_ANNI_SERVICE_ROUTER.getValue());
		} else if (type >= 220 && type < 223) {
			msgBean.setRouterType(RouterType.FAMILY_DIARY_SERVICE_ROUTER.getValue());
		} else if (type >= 200 && type < 300) {
			msgBean.setRouterType(RouterType.FAMILY_PHOTOWALL_SERVICE_ROUTER.getValue());
		} else if (type >= 300 && type < 400) {
			msgBean.setRouterType(RouterType.FAMILY_MESSAGEBOARD_SERVICE_ROUTER.getValue());
		} else if (type >= 400 && type < 500) {
			msgBean.setRouterType(RouterType.FAMILY_HEALTHMANAGER_SERVICE_ROUTER.getValue());
		} else if (type >= MessageType.BBS_REPLY_T && type <= MessageType.BBS_REPLY_R) {
			msgBean.setRouterType(RouterType.BBS_SERVICE_ROUTER.getValue());
		} else {
			msgBean.setRouterType(RouterType.FAMILY_SYSTEM_SERVICE_ROUTER.getValue());
		}

		return msgBean;
	}

	public static void main(String[] args) throws HttpRequestException {
		/*ChCareWebApiOfflineMessageApi offlineMessageHandler = new ChCareWebApiOfflineMessageApi();
		//
		String str = "{\"Data\":[{\"ID\":10,\"Type\":101,\"Val\":{\"userid\":007,\"familyID\":123456,\"userName\":\"hadoop\"},\"SendTime\":\"2014-08-29 14:45:22.017\",\"SUID\":10001,\"RID\":100001,\"RType\":2}],\"State\":0,\"Desc\":\"操作成功\"}";
		// // ResponseBean responseBean=offlineMessageHandler.transToBean(str);
		str = "{\"Data\":[{\"ID\":1467,\"Type\":100,\"Val\":\"{\"familyID\":100344,\"userName\":\"13402858512\"}\",\"SendTime\":\"2014-09-15 10:12:17.017\",\"SUID\":10021,\"RID\":10026,\"RType\":1},{\"ID\":1468,\"Type\":100,\"Val\":\"{\"familyID\":100344,\"userName\":\"13402858512\"}\",\"SendTime\":\"2014-09-15 10:31:00.071\",\"SUID\":10021,\"RID\":10026,\"RType\":1},{\"ID\":1469,\"Type\":100,\"Val\":\"{\"familyID\":100344,\"userName\":\"13402858512\"}\",\"SendTime\":\"2014-09-15 10:35:03.017\",\"SUID\":10021,\"RID\":10026,\"RType\":1}],\"State\":0,\"Desc\":\"操作成功\"}";
		// str="{\"Data\":[{\"ID\":1467,\"Type\":100,\"Val\":\"{\"familyID\":100344,\"userName\":\"13402858512\"}\",\"SendTime\":\"2014-09-15 10:12:17\",\"SUID\":10021,\"RID\":10026,\"RType\":1},{\"ID\":1468,\"Type\":100,\"Val\":\"{\"familyID\":100344,\"userName\":\"13402858512\"}\",\"SendTime\":\"2014-09-15 10:31:00\",\"SUID\":10021,\"RID\":10026,\"RType\":1},{\"ID\":1469,\"Type\":100,\"Val\":\"{\"familyID\":100344,\"userName\":\"13402858512\"}\",\"SendTime\":\"2014-09-15 10:35:03\",\"SUID\":10021,\"RID\":10026,\"RType\":1}],\"State\":0,\"Desc\":\"操作成功\"}";
		// //str="{\"Data\":[{\"ID\":13,\"Type\\":100,\"Val\":\"{\"userID\":0,\"familyID\":100001,\"userName\":\"12345678912\",\"nickName\":\"魏叔老婆\",\"familyName\":null,\"remark\":\"who care\"}","SendTime":"2014-09-09 17:57:21","SUID":10002,"RID":10001,"RType":1}],"State":1,"Desc":"操作成功"}";
		ResponseBean baseBean = offlineMessageHandler.transToBean(str);
		ResponseBean secondBean = offlineMessageHandler
				.transToLocalOfflineMessage(baseBean);

		List<ChCareOfflineMessage> chcareBeans = (List<ChCareOfflineMessage>) secondBean
				.getData();
		ChCareOfflineMessage chcareBean = chcareBeans.get(0);
		System.out.println(chcareBean.getRouter());
		OfflineMessageBean offlineBean = chcareBean.getMessage();
		OfflineMessageContent content = (OfflineMessageContent) offlineBean
				.getVal();
		System.out.println(offlineBean.getID()
				+ offlineBean.getVal().getClass().getName() + ","
				+ content.getFamilyID());*/

		// responseBean=offlineMessageHandler.transToLocalOfflineMessage(responseBean);
		// List<OfflineMessageBean>
		// offlineBean=(List<OfflineMessageBean>)responseBean.getData();
		// OfflineMessageContent
		// content=(OfflineMessageContent)offlineBean.get(0).getVal();
		// System.out.println(content.getUserid());
	}

	@Override
	public ResponseBeanWithRange pollingMessage(long startIndex, long endIndex)
			throws HttpRequestException {
		String url = BASE_URL + "msg/count?";
		StringBuilder sb = new StringBuilder();
		if (startIndex > 0) {
			sb.append("startIndex=").append(startIndex).append("&");
		}
		if (endIndex > 0) {
			sb.append("endIndex=").append(endIndex).append("&");
		}
		if (sb.length() > 0) {
			String params = sb.toString().substring(0, sb.length() - 1);
			url += params;
		}
		System.out.println("request url:" + url);
		String response = this.baseGetRequestUtil(url);
		System.out.println("response:" + response);
		ResponseBeanWithRange<?> result = this.transToRangeBean(response);
		if (result != null && result.getState() >= 0) {
			if (result.getData() instanceof LinkedTreeMap) {

				ResponseBeanWithRange<List<OfflineMessageBean<Object>>> resultBean = new ResponseBeanWithRange<List<OfflineMessageBean<Object>>>();
				resultBean.setDesc(result.getDesc());
				resultBean.setState(result.getState());
				resultBean.setEndIndex(result.getEndIndex());
				resultBean.setCount(result.getCount());
				resultBean.setLimit(result.getLimit());
				resultBean.setStartIndex(result.getStartIndex());
				 List<OfflineMessageBean<Object>> messageList =	getCountMessage((LinkedTreeMap<String, Object>) result
						.getData());
				resultBean
						.setData(messageList);
				return resultBean;
			}
		}

		return null;
	}

	private List<OfflineMessageBean<Object>> getCountMessage(
			LinkedTreeMap<String, Object> msg) {

		List<OfflineMessageBean<Object>> offlineMsgs = new ArrayList<OfflineMessageBean<Object>>();
		for (Entry<String, Object> mapObj : msg.entrySet()) {

			int type = Integer.valueOf(mapObj.getKey());
			OfflineMessageBean<Object> result = getRouterType(type);
			
			result.setType(type);
			Double count = (Double)mapObj.getValue();
			result.setVal(count.intValue());

			offlineMsgs.add(result);
		}
		return offlineMsgs;
	}

	private OfflineMessageBean<Object> getRouterType(int type) {

		OfflineMessageBean<Object> result = new OfflineMessageBean<Object>();
		if (type >= 100 && type < 200) {
			result.setRouterType(RouterType.FAMILY_MEMBER_SERVICE_ROUTER.getValue());
		} else if (type >= 210 && type < 220) {
			result.setRouterType(RouterType.FAMILY_ANNI_SERVICE_ROUTER.getValue());
		} else if (type >= 220 && type < 223) {
			result.setRouterType(RouterType.FAMILY_DIARY_SERVICE_ROUTER.getValue());
		} else if (type >= 200 && type < 300) {
			result.setRouterType(RouterType.FAMILY_PHOTOWALL_SERVICE_ROUTER.getValue());
		} else if (type >= 300 && type < 400) {
			result.setRouterType(RouterType.FAMILY_MESSAGEBOARD_SERVICE_ROUTER.getValue());
		} else if (type >= 400 && type < 500) {
			result.setRouterType(RouterType.FAMILY_HEALTHMANAGER_SERVICE_ROUTER.getValue());
		} else if (type >= MessageType.BBS_REPLY_T && type <= MessageType.BBS_REPLY_R) {
			result.setRouterType(RouterType.BBS_SERVICE_ROUTER.getValue());
		} else {
			result.setRouterType(RouterType.FAMILY_SYSTEM_SERVICE_ROUTER.getValue());
		}

		return result;
	}

	@Override
	public ResponseBean markMessage(int id) throws HttpRequestException {
		String url = BASE_URL + "msg/MarkRead?";
		if(id>=0){
			 url+="id="+id;
		}
		String response  = this.baseGetRequestUtil(url);
		ResponseBean<?> result = transToBean(response);
		
		return result;
	}
}
