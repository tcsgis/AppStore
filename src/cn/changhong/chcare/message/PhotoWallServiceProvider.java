package cn.changhong.chcare.message;

public class PhotoWallServiceProvider extends IRouter {

	public static class Self {
		private static PhotoWallServiceProvider server;

		public static PhotoWallServiceProvider instance() {
			if (null == server) {
				server = new PhotoWallServiceProvider();
			}
			return server;
		}
	}

	private PhotoWallServiceProvider() {
	}

	@Override
	public <T> void receivedMessage(T msg) {

	}

}
