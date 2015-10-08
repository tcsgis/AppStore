package cn.changhong.chcare.core.webapi.server;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import cn.changhong.chcare.core.webapi.AbstractChCareWebApi;
import cn.changhong.chcare.core.webapi.bean.ResponseBean;
import cn.changhong.chcare.core.webapi.bean.ResponseBeanWithRange;
import cn.changhong.chcare.core.webapi.handler.AsyncResponseCompletedHandler;
import cn.changhong.chcare.core.webapi.util.HttpRequestException;

public abstract class IOfflineMessageService extends AbstractChCareWebApi
		implements IService {

	/**
	 * 
	 * new!获取消息
	 * GET <domain>/api/msg 参数(可选)
	 * ?startIndex={startIndex}&endIndex={endIndex}&count={count}&type={type[0],type[1],...}&mode={mode}
	 * startIndex: {int} (可选)开始查询的信息id(不含), 为null则表示从第一条开始 endIndex: {int}
	 * (可选)结束查询的信息id, &gt;startIndex, 用于count查询一段信息后用户继续拖动,
	 * 为null则表示到最近一条结束 count: {int}(可选)只查询最近的count条, 为null则查询所有
	 * MsgType:{int[]}模块的消息类型，如家庭模块：MsgType={101,102,103,104,105,106,107,108,109,150}
	 * mode:{QueryMode:byte?} (可选)消息类型, 默认0, 为1则只查询离线消息
	 * 
	 * @Title: getUserOfflineMessage  
	 * @Description: TODO  
	 * @param startIndex
	 * @param endIndex
	 * @param count
	 * @param type
	 * @param mode
	 * @param @return
	 * @param @throws HttpRequestException      
	 * @return {@code List<ChCareOfflineMessage<OfflineMessageBean<T>>>} T的类型根据消息类型决定  {@link OfflineMessageContent}或者相应基本数据的实体类如{@link Family},{@link User}等
	 * @throws
	 */
	public abstract ResponseBean getUserOfflineMessage(long startIndex,
			long endIndex, int count, int[]type,int mode) throws HttpRequestException;
	public <T> Future<ResponseBean> getUserOfflineMessage(final long startIndex,
			final long endIndex, final int count, final int[] type,final int mode,
			final AsyncResponseCompletedHandler<T> handler) {
		handler.onStart(startIndex,endIndex,count,type,mode);
		Future<ResponseBean> future = executorProvider
				.doTask(new Callable<ResponseBean>() {

					@Override
					public ResponseBean call() {
						ResponseBean bean = null;
						try {
							bean = getUserOfflineMessage(startIndex, endIndex,
									count, type, mode);
							handler.onCompleted(bean,ChCareWepApiServiceType.WebApi_OfflineMsg_getUserOfflineMessage_Service);
						} catch (HttpRequestException e) {
							handler.onThrowable(e,ChCareWepApiServiceType.WebApi_OfflineMsg_getUserOfflineMessage_Service);
						}
						return bean;
					}
				});
		return future;
	}
	/**
	 * new!获取通知(消息轮询)
	 * GET <domain>/api/msg/count?startIndex={startIndex}&endIndex={endIndex}
	 * startIndex:{int?} (可选)开始查询的信息id(不含), 为null则表示从第一条开始
	 * endIndex:{int?} (可选)结束查询的信息id, >startIndex, 为null则表示到最近一条结束
	 * ResultModel<{{MsgType(#MsgType "MsgType")}:{int}, {MsgType}:{int}}>消息条数 
	 * 例：{100:3, 104:4}表示，用户申请加入某家庭，发送消息到户主的这类消息有3条;用户退出家庭这类消息有4条
	 */
	public abstract ResponseBeanWithRange pollingMessage(long startIndex, long endIndex) throws HttpRequestException;
	public <T> Future<ResponseBeanWithRange > pollingMessage(final long startIndex,final long endIndex,final AsyncResponseCompletedHandler<T> handler){
		handler.onStart(startIndex,endIndex);
		Future<ResponseBeanWithRange> future = executorProvider.doTask(new Callable<ResponseBeanWithRange>() {

			@Override
			public ResponseBeanWithRange call() {
				ResponseBeanWithRange bean = null;
				try {
					bean = pollingMessage(startIndex, endIndex);
					handler.onCompleted(bean,ChCareWepApiServiceType.WebApi_OfflineMsg_pollingMessage_Service);
				} catch (HttpRequestException e) {
					handler.onThrowable(e,ChCareWepApiServiceType.WebApi_OfflineMsg_pollingMessage_Service);
				}
				return bean;
			}
		});
		return future;
		
	};
	/**
	 * new!设置消息已读
	 * 设置某一条消息为已读
	 * POST <domain>/api/msg/MarkRead?id={id}
	 * @param id:{int} 消息id
	 * @return ResultModel
	 */
	public abstract ResponseBean markMessage(int id) throws HttpRequestException;
	public <T> Future<ResponseBean> markMessage(final int id, final AsyncResponseCompletedHandler<T> handler){
		handler.onStart(id);
		Future<ResponseBean> future = executorProvider.doTask(new Callable<ResponseBean>() {

			@Override
			public ResponseBean call() {
				ResponseBean bean = null;
				try {
					bean = markMessage(id);
					handler.onCompleted(bean,ChCareWepApiServiceType.WebApi_OfflineMsg_markMessage_Service);
				} catch (HttpRequestException e) {
					handler.onThrowable(e,ChCareWepApiServiceType.WebApi_OfflineMsg_markMessage_Service);
				}
				return bean;
			}
		});
		return future;
		
	}
}
