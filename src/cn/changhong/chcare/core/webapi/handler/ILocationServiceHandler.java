package cn.changhong.chcare.core.webapi.handler;

import java.util.List;

import cn.changhong.chcare.core.webapi.bean.Location;
import cn.changhong.chcare.core.webapi.bean.ResponseBean;

import com.changhong.util.CHLogger;

public abstract class ILocationServiceHandler<T> extends
		AsyncResponseCompletedHandler<T> {
	private Object obj;
	public ILocationServiceHandler(){
	}
	public ILocationServiceHandler(Object obj){
		this.obj = obj;
	}

/*	public ResponseBean<?> searchUserHistoryPosition(int userID, Date stTime,
			Date edTime, String type) {
		ResponseBean<List<Location>> retBean = new ResponseBean<List<Location>>();
		retBean.setLocData(true);
		StringBuilder wBuilder = new StringBuilder();
		wBuilder.append("userID = ").append(userID).append(" and type =")
				.append(type).append(" and CurTime between").append(stTime)
				.append(" and ").append(edTime);
		String where = wBuilder.toString();
		try {
			List<Location> list = getSqlitdb().query(Location.class, true,
					where, null, null, null, null);
			retBean.setData(list);

		} catch (Exception e) {
			// TODO: handle exception
			CHLogger.e(this, e.getMessage());
		}

		return retBean;
	}

	public void searchUserHistoryPositionSave(ResponseBean<?> response) {

		if (response != null && response.getState() >= 0) {

			List<Object> historyData = (List<Object>) response.getData();
			for (Object item : historyData) {
				Location view = (Location) item;
				saveOrUpateObj(view, "ID=" + view.getID());
			}
		}
	}*/

	public ResponseBean<?> searchUsersLastLocation(int userID) {
		ResponseBean<Location> retBean = new ResponseBean<Location>();
		retBean.setLocData(true);
		StringBuilder wBuilder = new StringBuilder();
		wBuilder.append("userID = ").append(userID);
		String where = wBuilder.toString();
		try {
			List<Location> list = getSqlitdb().query(Location.class, true,
					where, null, null, null, null);
			Location loc = list.get(0);
			retBean.setData(loc);

		} catch (Exception e) {
			// TODO: handle exception
			CHLogger.e(this, e.getMessage());
		}

		return retBean;
	}

	public void searchUsersLastLocationSave(ResponseBean<?> response) {
		if (response != null && response.getState() >= 0) {

			Object historyData =  response.getData();
			if(historyData!=null) {
				Location view = (Location) historyData;
				saveOrUpateObj(view, "ID=" + view.getID());
			}
		}
	}
/*
	public void deleteBindFPhoneSave(ResponseBean<?> response) {
		if (response != null && response.getState() >= 0) {
			String where = "DevId ="+;
			getSqlitdb().delete(FuncFamNumView.class,"devID = ");
		}
	}
*/
	/*
	public ResponseBean<?> getAllBindFPhonePosition() {
		return null;
	}

	public void getAllBindFPhonePositionSave(ResponseBean<?> response) {
	}

	public ResponseBean<?> getBindFPhonePosition(Long devId) {
		return null;
	}

	public void getBindFPhonePositionSave(ResponseBean<?> response) {
	}

	public ResponseBean<?> getAllBindFPhones() {
		return null;
	}

	public void getAllBindFPhonesSave(ResponseBean<?> response) {
	}

	public ResponseBean<?> getBindFPhoneAllPhoneNum(Long devId) {
		return null;
	}

	public void getBindFPhoneAllPhoneNumSave(ResponseBean<?> response) {
	}

	public ResponseBean<?> getBindFPhoneHistoryPositions(Long devId,
			Date stTime, Date edTime) {
		return null;
	}

	public void getBindFPhoneHistoryPositionsSave(ResponseBean<?> response) {
	}

	public ResponseBean<?> getBindFPhonePositionMode(Long devId) {
		return null;
	}

	public void getBindFPhonePositionModeSave(ResponseBean<?> response) {
	}

	public void updateBindFPhoneSave(ResponseBean<?> response) {
	}

	public void updateBindFPhoneNickInfoSave(ResponseBean<?> response) {
	}

	public void updateBindFPhonePositionModeSave(ResponseBean<?> response) {
	}

	public void unBindPhoneSave(ResponseBean<?> response) {
	}
*/
	

}
