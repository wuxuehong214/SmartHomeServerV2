package cn.iever.wxh.yjserver.core.msg;

/**
 * 
 * 客户端登录远程服务器获取网关IP或者利用服务器建立中转连接
 * 
 * 客户端需要汇报 家庭网关域名
 * 
 * 服务器根据客户端请求主机域名信息  获取到相应的网关信息
 * 如果当前网关在线  则将客户端与网关系统建立中转连接
 * 如果网关不在线 则反馈网关IP到客户端
 * @author Administrator
 *
 */
public class ClientLoginMessageNew extends AbstractMessage {
	
	//家庭主机域名
	private String hostName;
	//用户名
	private String userName;
	//用户密码
	private String userPwd;
	
	// for response
	private byte[] ip; // 反馈IP
	private byte result; // 请求结果
	private byte role = (byte)0xff;   //角色ID

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public byte[] getIp() {
		return ip;
	}

	public void setIp(byte[] ip) {
		this.ip = ip;
	}

	public byte getResult() {
		return result;
	}

	public void setResult(byte result) {
		this.result = result;
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

	public byte getRole() {
		return role;
	}

	public void setRole(byte role) {
		this.role = role;
	}

	@Override
	public byte getRequestType() {
		// TODO Auto-generated method stub
		return CLIENT_REQ_BRIDGE;
	}

}
