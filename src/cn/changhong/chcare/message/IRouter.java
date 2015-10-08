package cn.changhong.chcare.message;

public abstract class IRouter {
	public abstract <T> void receivedMessage(T msg);
}
