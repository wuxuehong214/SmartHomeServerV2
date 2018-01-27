package cn.iever.wxh.gateway.monitor;

public interface IGatewayListener {
	
	public  void connected();
	
	public void disconnected();
	
	public void log(String msg);
	
	public void commandSent(String msg);

}
