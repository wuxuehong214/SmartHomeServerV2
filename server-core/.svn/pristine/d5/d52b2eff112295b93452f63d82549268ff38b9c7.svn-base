package cn.iever.wxh.yjserver.core.model;

/**
 * 
 * 登录者身份
 * 
 * @author Administrator
 * 
 */
public class Authorization {

	/**
	 * 当前身份类型
	 * 
	 * 每个连接到服务器上的会话身份并不一致，可能是来自于客户端会话，也可能是来自于网关会话
	 * 该变量来标识会话身份信息
	 * 
	 */
	private STATUS status;
	
	/**
	 * 家庭主机域名
	 * 
	 * 家庭主机域名类似于每台网关设备的地址，该地址名称与网关设备ID一一对应。
	 * 服务器根据客户端请求时附带的家庭主机域名来查询到相应的网关信息，从而为客户端提供对应的服务
	 * 
	 */
	private String hostName;
	
	/**
	 * 网关设备ID
	 * 
	 * 每一台网关设备都有唯一ID，该ID又设备内部IC决定
	 * 每台设备出厂的时候都会注册该ID，当且仅当注册了该ID以后系统才可以正常使用
	 */
	private String deviceId;
	
	/**
	 * 客户端会话登录所用的用户名
	 */
	private String userName;
	
	/**
	 * 客户端会话登录所用的密码
	 */
	private String userPwd;
	
	/**
	 * session编号
	 * 由于网关系统加入了多级用户功能，不同客户端会话有不同控制权限，
	 *  当服务器执行中转服务器功能的时候，由于服务器与网关之间只有一个连接，
	 * 那么如何区分发到网关系统中的控制命令是来自于哪一个客户端会话呢？
	 * 
	 * 为了区分不同客户端，我们就需要标记每个请求包是哪个客户端发送的
	 * 因此为每个客户端会话进行唯一编号，当客户端会话在发送控制命令后，
	 * 服务器在做中转的时候为每个请求包中附加上客户端会话编号信息（默认加到请求数据内容的最后1byte），
	 * 这样在我网关中就可以根据该唯一编号来判断该客户的权限
	 * 
	 * 当session编号为-1的时候 即表示服务器在转发命令的时候不需要加入该编号信息 ， 为了兼容老版本客户端！
	 */
	private int sessionID = -1;
	
	public Authorization(){
		
	}
	
	public Authorization(STATUS status, String deviceId, String hostName){
		this.status = status;
		this.deviceId = deviceId;
		this.hostName = hostName;
	}
	
	public Authorization(STATUS status, String deviceId, String hostName, int sessionID){
		this(status,deviceId,hostName);
		this.sessionID = sessionID;
	}
		
	public STATUS getStatus() {
		return status;
	}

	public void setStatus(STATUS status) {
		this.status = status;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public int getSessionID() {
		return sessionID;
	}

	public void setSessionID(int sessionID) {
		this.sessionID = sessionID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

}
