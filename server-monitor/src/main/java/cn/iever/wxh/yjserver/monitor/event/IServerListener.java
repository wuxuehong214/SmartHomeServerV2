package cn.iever.wxh.yjserver.monitor.event;

/**
 * 服务器监听
 * @author Administrator
 *
 */
public interface IServerListener {
	
	public void connected();
	
	public void disConnected();
	
	public void notifyQueryMessage(String msg);
	
	public void notifyMonitorMessage(String msg);

}
