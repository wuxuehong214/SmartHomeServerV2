package cn.iever.wxh.yjserver.core.msg;

/**
 * 客户端登录请求/反馈包
 * 
 * 客户端发送该登录包  由服务器判断登录结果，
 * 
 * 如果客户端连接的网关设备存在长连接 则直接将客户端与网关建立起中转连接
 * 如果客户端连接的网关不存在长连接 则反馈网关对外IP信息给客户端
 * 
 * @author Administrator
 * 
 */
public class ClientLoginMessage extends AbstractMessage {

	// for request
	private String userName; // 用户名
	private String passWord; // 密码

	// for response
	private byte[] ip; // 反馈IP
	private byte result; // 请求结果

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
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

	@Override
	public byte getRequestType() {
		// TODO Auto-generated method stub
		return CLIENT_REQ_IP;
	}

}
