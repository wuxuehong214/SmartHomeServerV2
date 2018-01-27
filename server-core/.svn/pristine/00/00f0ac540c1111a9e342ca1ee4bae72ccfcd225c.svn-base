package cn.iever.wxh.yjserver.core.msg;

/**
 * 服务器需要转发的消息
 * 
 * @author Administrator
 * 
 */
public class TransferedMessage extends AbstractMessage {
	
	/**
	 * 附加数据
	 * 
	 * 鉴于服务器与网关之间只有一条通信通道
	 * 当多个客户端会话同时使用该通信通道时候，需要一些附加的数据来区分每个控制消息来自于哪个客户端会话
	 * 附加消息为一个字节，默认附加到数据区内容的最后(1byte)
	 * 
	 */
	private byte attachData = -1; 

	/**
	 * 真正的请求类型
	 * 
	 * 服务器在做中转的时候，除了一些特殊的请求信息需要服务器截获管理，其他的请求控制信息都统一定义为“中转信息”并且
	 * 将所有请求包类型设置为服务器所认识的“中转类型”信息以供服务器内部识别处理。即@CLIENT_GATWWAY_COMMUNICATE
	 * 然而当服务器将该“中转信息”下发给客户端/网关的时候 需要将该数据包的真实请求类型还原，
	 * 因此该变量用于存储“中转信息”包的真实请求类型
	 */
	private byte realRequestType;
	
	/**
	 * 详细请求数据
	 * 
	 * 满足系统协议规定中数据区内容
	 */
	private byte[] data;

	public byte getRealRequestType() {
		return realRequestType;
	}

	public void setRealRequestType(byte realRequestType) {
		this.realRequestType = realRequestType;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public byte getAttachData() {
		return attachData;
	}

	public void setAttachData(byte attachData) {
		this.attachData = attachData;
	}

	@Override
	public byte getRequestType() {
		// TODO Auto-generated method stub
		return CLIENT_GATWWAY_COMMUNICATE;
	}

}
