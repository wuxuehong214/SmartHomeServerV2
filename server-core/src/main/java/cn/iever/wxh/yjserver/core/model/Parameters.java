package cn.iever.wxh.yjserver.core.model;

/**
 * 服务器参数
 * @author Administrator
 *
 */
public class Parameters {
	
	/**
	 *服务器启动后的监听端口
	 *默认为6000 
	 */
	private int port = 6000;
	
	/**
	 * 服务器会话处理的处理器个数   默认为CPU核数+1
	 */
	private int threadNum = Runtime.getRuntime().availableProcessors() + 1;
	
	/**
	 * 心跳包时间  客户端与服务器维持会话的最大时间间隔
	 * 当客户端与服务器端在该时间内没有任何通信 即认为客户端离线
	 */
	private int heartPeriod = 40;
	
	/**
	 * 是否要显示客户端与服务器通信的详细日志信息  由MINA框架提供
	 * 默认为不显示
	 */
	private boolean isDetailLog = false;
	
	/**
	 * 数据库访问IP
	 * 默认为本机IP 即认为数据库与服务器在同一主机
	 */
	//private String slqServerIp = "127.0.0.1";

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}

	public int getHeartPeriod() {
		return heartPeriod;
	}

	public void setHeartPeriod(int heartPeriod) {
		this.heartPeriod = heartPeriod;
	}

	public boolean isDetailLog() {
		return isDetailLog;
	}

	public void setDetailLog(boolean isDetailLog) {
		this.isDetailLog = isDetailLog;
	}

//	public String getSlqServerIp() {
//		return slqServerIp;
//	}
//
//	public void setSlqServerIp(String slqServerIp) {
//		this.slqServerIp = slqServerIp;
//	}

	@Override
	public String toString() {
		return "Parameters [port=" + port + ", threadNum=" + threadNum
				+ ", heartPeriod=" + heartPeriod + ", isDetailLog="
				+ isDetailLog + "]";
	}
	
	/**
	 * 帮助信息
	 * @return
	 */
	public String getHelpInfo(){
		StringBuffer sb = new StringBuffer();
		sb.append("Usage:java -jar server.jar [-p] [-t] [-h] [-i] [log]\n");
		sb.append("[-p]: server listening port!\n");
		sb.append("[-t]: processor/threads number, default (CPU core num)+1!\n");
		sb.append("[-h]: client heart interval, no heart msg, the client will be closed!\n");
		sb.append("[-i]: ip for sql server!\n");
		sb.append("[log]: logger the msg for server!\n");
		sb.append("Example:java -jar server.jar [-p6000] [-t5] [-h10] [-i127.0.0.1]  [log]\n");
		return sb.toString();
	}
	
}
