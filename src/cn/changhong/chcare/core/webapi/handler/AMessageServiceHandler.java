package cn.changhong.chcare.core.webapi.handler;

import java.util.List;

import cn.changhong.chcare.core.webapi.bean.OfflineMessageBean;
import cn.changhong.chcare.core.webapi.bean.ResponseBean;
import cn.changhong.chcare.core.webapi.server.ChCareWepApiServiceType;

public class AMessageServiceHandler<T> extends AsyncResponseCompletedHandler<T> {

	public AMessageServiceHandler(){

	}

	public AMessageServiceHandler(boolean getLoctionData){
		super.getLoctionData = getLoctionData;
	}

	
	public ResponseBean<?> getUserOfflineMessage(long startIndex,
			long endIndex, int count, int[] type,int mode){
		
		ResponseBean<List<?>> retBean = new ResponseBean<List<?>>();
		retBean.setLocData(true);
		/*StringBuffer strBuffer = new StringBuffer();
		int len = type.length;
		
		if(len > 0){
			
			len = len -1;
			strBuffer.append(" (Type = " +type[0]);	
			for(int i=1; i<len; i++ ){
				strBuffer.append(" or Type = " +type[i]);
			}
			strBuffer.append(")");
			
			String orderBy = "ID DESC";
			if(startIndex > 0){
				strBuffer.append(" and ID >" +startIndex);
			}

			if(endIndex > 0){
				strBuffer.append(" and ID <" +endIndex);
			}

			if(count < 0){
				orderBy = "ID ASC";
				count = Math.abs(count);
			}
			
			List<?> objs= getSqlitdb().query(OfflineMessageBean.class, true, strBuffer.toString(), null, null, orderBy, count+"");

			if(objs == null || objs.isEmpty()){
				retBean.setState(-3);
				return retBean;
			}

			retBean.setData(objs);
		}*/
		return retBean;
	}
	
	public ResponseBean<?> getOfflineMessageByRtype(long startIndex,
			long endIndex, int count, String routerType){
		
		ResponseBean<List<?>> retBean = new ResponseBean<List<?>>();
		retBean.setLocData(true);
		
		String where = "routerType ='"+routerType+"'";
		
		String orderBy = "ID DESC";
		if(startIndex > 0){
			where = where +" and ID >" +startIndex;
		}

		if(endIndex > 0){
			where = where +" and ID <" +endIndex;
		}

		if(count < 0){
			orderBy = "ID ASC";
			count = Math.abs(count);
		}
		
		List<?> objs= getSqlitdb().query(OfflineMessageBean.class, true, where, null, null, orderBy, count+"");

		if(objs == null || objs.isEmpty()){
			retBean.setState(-3);
			return retBean;
		}

		retBean.setData(objs);
		
		return retBean;
	}
	
	public void getUserOfflineMessageSave(ResponseBean<?> response){
		
		if (response != null && response.getState() >= 0) {

			List<?> offlineMessages = (List<?>) response.getData();

			if (!(offlineMessages == null || offlineMessages.isEmpty())) {

				for (Object obj : offlineMessages){
					OfflineMessageBean<?> item = (OfflineMessageBean<?>)obj;
					saveOrUpateObj(item, "ID=" + item.getID());
				}
			}
		}
	}
	
	@Override
	public T doCompleted(ResponseBean<?> response,
			ChCareWepApiServiceType servieType) {
		// TODO Auto-generated method stub
		return null;
	}

}
