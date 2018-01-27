package cn.iever.wxh.yjserver.core.msg;

/**
 * abstract message 
 * @author wuxuehong
 *
 * @date 2012-9-10
 */
public abstract class AbstractMessage {
	
	/**包协议头*/
	public static final byte[] HEAD = {(byte)0x59,(byte)0x4A};
	
	public static final byte CLIENT_REQ_LOGIN = (byte)0xF0;
	//服务器与网关的长连接中     部分反馈数据是一对一的  只需要反馈刚给特定的客户端会话，消息请求类型为0xef
	public static final byte CLIENT_ONE_ONE = (byte)0xEF;
	
	/**客户端请求连接IP/连接*/
	public static final byte CLIENT_REQ_BRIDGE = (byte) 0xE4; //客户端请求网关IP/请求建立中转连接
	public static final byte CLIENT_REQ_IP = (byte)0xF4; //客户端请求网关IP或者请求建立连接
	public static final byte CLIENT_REQ_HEART	= (byte)0xF3;  //心跳包
	public static final byte CLIENT_REQ_COMMAND = (byte)0xf1; //命令请求
	
	/**服务器反馈客户端内容*/
	public static final byte CLIENT_RESP_FAIL = (byte)0xE0;  //用户信息验证失败 用户名密码错误
	public static final byte CLIENT_RESP_IP = (byte)0xE1;//IP请求成功
	public static final byte CLIENT_RESP_NOTEXIST = (byte)0xE2; //账户信息不存在
	public static final byte CLIENT_RESP_OUTLINE = (byte)0xE3; //网关不在线
	public static final byte CLIENT_RESP_LOCKED = (byte)0xF7; //网关不在线
	public static final byte CLIENT_RESP_CONNECT = (byte)0xD1;  //远程连接建立
	public static final byte CLIENT_RESP_DISCONNECT = (byte)0xD0;  //远程连接断开 原因网关掉线
	public static final byte CLIENT_RESP_CAMERA = (byte)0xf9;   //视频反馈
	
	/**网关请求连接类型*/
	public static final byte GATEWAY_REQ_IP = (byte)0xE0; //网关发送身份信息
	public static final byte GATEWAY_REQ_CONNECT = (byte)0xE1;   //网关请求建立长连接
	
	/**客户端与网关之间的转发包 类型*/
	public static final byte CLIENT_GATWWAY_COMMUNICATE = (byte)0xA0;
	
	public static final byte INTERNAL_QUERY = (byte)0xBB;
	public static final byte INTERNAL_MONITOR = (byte)0xBC;
	
	/**
	 * 服务器所识别的请求类型
	 * 1-网关汇报IP请求
	 * 2-网关请求建立连接请求
	 * 3-客户端请求IP/建立连接请求
	 * 4-转发请求
	 * 
	 * @return
	 */
	public abstract byte getRequestType();
}
