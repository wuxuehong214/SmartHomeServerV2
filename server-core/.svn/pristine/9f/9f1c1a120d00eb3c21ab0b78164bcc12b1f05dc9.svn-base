package cn.iever.wxh.yjserver.core.msg;

/**
 * 网关主机请求建立长连接消息
 * 
 * @author Administrator
 * 
 */
public class GateWayLongConnectMessage extends AbstractMessage {

	// 设备ID
	private String deviceId;
	// 主机IP
	private byte[] ip;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public byte[] getIp() {
		return ip;
	}

	public void setIp(byte[] ip) {
		this.ip = ip;
	}

	@Override
	public byte getRequestType() {
		// TODO Auto-generated method stub
		return GATEWAY_REQ_CONNECT;
	}

}
