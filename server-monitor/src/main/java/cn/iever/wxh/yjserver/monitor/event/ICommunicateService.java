package cn.iever.wxh.yjserver.monitor.event;

/**
 * 通讯服务
 * @author Administrator
 *
 */
public interface ICommunicateService {
	
	public boolean connect(String ip, int port);
	
	public void disconnect();
	
	public void sendRequest(String msg);

	public void addServerListener(IServerListener listener);
	
	public void removeServerListener(IServerListener listener);
	
	public void notifyQueryMessage(String msg);
	
	public void notifyMonitorMessage(String msg);
	
	public void connected();
	
	public void disconnected();
	
	public String getConnectedIp();
	
	public int getConnectedPort();
}
