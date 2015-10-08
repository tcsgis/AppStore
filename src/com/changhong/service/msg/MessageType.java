package com.changhong.service.msg;

public class MessageType {
	//管理员	
	public final static int ADMIN_NEW_BARBER = 101;	//有新的自由发型师需要审核
	public final static int ADMIN_NEW_SALON = 102;	//	有新的发廊需要审核

	//顾客	
	public final static int CUSTOM_NEW_BID = 201;	//（收到消息后，先获取新的竞价信息，获取成功后再提示）	有新的发型师参与竞标
	public final static int CUSTOM_ORDER_ACCEPT = 202;	//	预约成功
	public final static int CUSTOM_ORDER_DENY = 203;	//	预约被拒绝
	public final static int CUSTOM_NEW_COUPON = 204;	//	收到新的优惠券
	
	//发型师
	public final static int BARBER_REGISTER_PASS = 301;	//	注册通过审核
	public final static int BARBER_REGISTER_DENY = 302;	//	注册未通过审核
	public final static int BARBER_MODIFY_PASS = 303;	//	修改信息通过审核
	public final static int BARBER_MODIFY_DENY = 304;	//	修改信息未通过审核
	public final static int BARBER_BID_SUCCESS = 305;	//	竞标成功
	public final static int BARBER_BID_FAIL = 306;	//	竞标失败（包括顾客取消竞价和没有中标）
	public final static int BARBER_ORDER_ACCEPT = 307;	//	预约成功（发廊和发型师都接受预约）
	public final static int BARBER_ORDER_DENY = 308;	//	预约失败（发型师接受预约，发廊拒绝预约）
	public final static int BARBER_ORDER_CANCEL = 309;	//	顾客取消预约
	public final static int BARBER_ORDER_DONE = 310;	//	顾客确认完成服务
	public final static int BARBER_ORDER_SHARE = 311;	//	"顾客确认完成后通过微信分享了发型师（这个时候发型师可以给顾客发优惠券）"
	public final static int BARBER_SALON_LOSS = 312;	//	有发廊解绑发型师
	public final static int BARBER_NEW_ORDER = 313;	//	新的预约

	//发廊	
	public final static int SALON_REGISTER_PASS = 401;	//	注册通过审核
	public final static int SALON_REGISTER_DENY = 402;	//	注册未通过审核
	public final static int SALON_MODIFY_PASS = 403;	//	修改信息通过审核
	public final static int SALON_MODIFY_DENY = 404;	//	修改信息未通过审核
	public final static int SALON_ORDER_ACCEPT = 405;	//	预约成功（发廊和发型师都接受预约）
	public final static int SALON_ORDER_DENY = 406;	//	预约失败（发廊接受预约，发型师拒绝预约）
	public final static int SALON_ORDER_CANCEL = 407;	//	顾客取消预约
	public final static int SALON_ORDER_DONE = 408;	//	顾客确认完成服务
	public final static int SALON_ORDER_SHARE = 409;	//	"顾客确认完成后通过微信分享了发廊（这个时候发廊可以给顾客发优惠券）"
	public final static int SALON_BARBER_ADD = 410;	//	有自由发型师绑定发廊
	public final static int SALON_BARBER_LOSS = 411;	//	有自由发型师解绑发廊
	public final static int SALON_NEW_ORDER = 412;	//	新的预约

	
	
	
	// 家庭成员消息
	public final static int FAMILY_MEMBER_REQUEST = 100;// 用户主动加入某家庭圈发送消息到户主
	public final static int FAMILY_MEMBER_AGREE_REQUEST = 101;// 户主同意用户加入后发送广播消息
	public final static int FAMILY_MEMBER_INVITE = 102;// 户主邀请用户加入家庭，向用户发送消息
	public final static int FAMILY_MEMBER_AGREE_INVITE = 103;// 用户同意被邀请加入家庭
	public final static int FAMILY_MEMBER_QUIT = 104;// 用户退出家庭
	public final static int FAMILY_MEMBER_DELETE = 105;// 用户被移出家庭
	public final static int FAMILY_MEMBER_DELETE_BC = 106;// 用户被移出家庭
	public final static int FAMILY_MEMBER_REJECT_INVITE = 107;// 用户拒绝被邀请加入家庭
	public final static int FAMILY_MEMBER_REJECT_REQUEST = 108;// 用户拒绝被邀请加入家庭
	public final static int FAMILY_MEMBER_FAMILY_DISMISS = 109;// 用户拒绝被邀请加入家庭
	public final static int FAMILY_MSG_CHANGED = 150;// 家庭信息变更
	// 家庭纪念日
	public final static int FAMILY_ANNI_ADD = 210;// 家庭成员添加纪念日
	public final static int FAMILY_ANNIL_DELETE = 211;// 家庭成员删除纪念日消息
	public final static int FAMILY_ANNIL_MODIFY = 212;// 家庭成员修改纪念日消息
	//家庭日记
	public final static int FAMILY_DIARY_ADD = 220;// 家庭添加日记
	public final static int FAMILY_DIARY_DELETE = 221;// 家庭删除日记
	public final static int FAMILY_DIARY_REPLY = 222;// 家庭日记回复
	// 家庭墙消息
	public final static int FAMILY_PHOTOWALL_ADD = 200;// 家庭成员添加新的照片
	public final static int FAMILY_PHOTOWALL_DELETE = 201;// 家庭成员删除照片消息
	public final static int FAMILY_PHOTOWALL_CLOSE = 202;// 家庭墙开关关闭
	public final static int FAMILY_PHOTOWALL_OPEN = 203;// 家庭墙开关打开

	// 生活圈消息
	public final static int BBS_REPLY_T = 0x802;// 回复帖子
	public final static int BBS_REPLY_R = 0x812;// 回复评论
	// 留言板消息
	public final static int FAMILY_MESSAGEBOARD_NEW = 300;// 新增留言消息
	public final static int FAMILY_MESSAGEBOARD_READ = 301;// 新消息被读
	public final static int FAMILY_MESSAGEBOARD_NEW_COMMENT = 302;// 新消息被评论
	// 健康消息
	public final static int FAMILY_HEALTHMODEL_NEWMEASURE_BP = 401;// 家庭成员新测血压
	public final static int FAMILY_HEALTHMODEL_NEWMEASURE_SP = 402;// 家庭成员新测血糖
	// 系统推送服务消息
	public final static int FAMILY_SYSTEM_ADS = 501;// 系统广告
	public final static int FAMILY_SYSTEM_FAMILYCARE = 502;// 系统关爱


	public static final int DB_MSG_HAS_READ = -1;
	public static final int DB_MSG_NOT_READ = 0;
	public static final int DB_MSG_HAS_AGREE = 1;//for request or invite join family
	public static final int DB_MSG_HAS_REJECT = 2;//for request or invite join family
	public static final int DB_MSG_INVALID = 3;//for invite join family when the family does not exist


	public static enum ResultState
	{
		UNKNOWN_ERR(-0xff),

		SEND_VFR_ERRO(-18),// 发送验证码失败

		HAS_USER(-17),// 用户名已存在

		DATA_OVERDUE(-16),// 数据过期

		REMOTE_LOGIN(-15), // 账号异地登录

		ERR_NOT_FOUND(-12),//未找到指定对象(视使用环境而定,譬如日记评论等)

		ERR_REMOTE_SERV_NOT_FOUND(-11),// 远程服务器未找到

		ERR_REMOTE_SERV_CONN_FAIL(-10),// 远程服务器连接失败

		ERR_REMOTE_SERV_ERR(-9),// 远程服务器错误

		VFY_ERR(-8),// 验证码错误

		PERM_DENY(-7),// 没有权限, 企图获取或设置不属于当前用户的对象信息时会提示此错误

		DATA_CONFLICT(-6),// 数据冲突

		INV_ARGS(-5),// 非法参数

		PASS_ERR(-4), // 用户名或密码错误

		NO_USER(-3),// 找不到此用户

		NOT_LOGIN(-2),// 未登录或登录信息过期

		DB_ERR(-1), // 数据库出错

		SUCCESS(0),// 默认情况, 无异常, 也无特殊描述

		SUCCESS_PARTLY(1),// 执行成功, 但只返回部分结果(分页)

		SUCCESS_DENY(2);// 申请被拒绝

		private int value;
		private ResultState(int v){
			value = v;
		}

		public int getValue(){
			return value;
		}
	}

}
