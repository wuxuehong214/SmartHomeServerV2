package cn.iever.wxh.yjserver.core.msg;


/**
 * 心跳包
 * @author Administrator
 *
 */
public class HeartMessage extends AbstractMessage {

	@Override
	public byte getRequestType() {
		// TODO Auto-generated method stub
		return CLIENT_REQ_HEART;
	}

}
