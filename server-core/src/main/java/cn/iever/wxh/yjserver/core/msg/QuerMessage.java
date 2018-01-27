package cn.iever.wxh.yjserver.core.msg;

/**
 * 查询命令
 * @author Administrator
 *
 */
public class QuerMessage extends AbstractMessage {
	
	//查询/反馈消息
	private String queryMsg;
	
	
	public String getQueryMsg() {
		return queryMsg;
	}
	public void setQueryMsg(String queryMsg) {
		this.queryMsg = queryMsg;
	}

	@Override
	public byte getRequestType() {
		// TODO Auto-generated method stub
		return INTERNAL_QUERY;
	}

}
