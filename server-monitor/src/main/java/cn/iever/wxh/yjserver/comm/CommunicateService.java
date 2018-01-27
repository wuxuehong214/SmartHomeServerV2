package cn.iever.wxh.yjserver.comm;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import cn.iever.wxh.yjserver.core.msg.QuerMessage;
import cn.iever.wxh.yjserver.monitor.event.ICommunicateService;
import cn.iever.wxh.yjserver.monitor.event.IServerListener;
import cn.iever.wxh.yjserver.resource.ResourceTone;

/**
 * 通讯服务
 * @author Administrator
 *
 */
public class CommunicateService  implements ICommunicateService{
	
	private List<IServerListener> listeners = new ArrayList<IServerListener>();
	
	private SimpleDateFormat sdf = new SimpleDateFormat("[MM-dd HH:mm:ss]");
	private NioSocketConnector connector;
	private CommunicateHandler handler;
	private IoSession session;
	private String ip = "www.iever.cn";
	private int port = 6000;
	
	private ResourceTone rt;
	
	public CommunicateService(){
		connector = new NioSocketConnector();
		handler = new CommunicateHandler(this);
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MyMessageCodecFactory(new MyMessageDecoder(), new MyMessageEncoder())));
		
		KeepAliveMessageFactoryImpl kam = new KeepAliveMessageFactoryImpl();
		KeepAliveFilter kaf = new KeepAliveFilter(kam,IdleStatus.READER_IDLE,KeepAliveRequestTimeoutHandler.CLOSE);
		kaf.setForwardEvent(true);
		kaf.setRequestInterval(10);
		kaf.setRequestTimeout(10);
		connector.getFilterChain().addLast("heart", kaf);
		
		connector.setHandler(handler);
		
		rt = new ResourceTone();
		
	}
	
	public boolean connect(String ip, int port) {
		// TODO Auto-generated method stub
		connector.setConnectTimeoutMillis(4000);
		ConnectFuture cf = connector.connect(new InetSocketAddress(ip,port));
		cf.awaitUninterruptibly();
		if(!cf.isConnected())return false;
		session = cf.getSession();
		this.ip = ip;
		this.port = port;
		return true;
	}

	public void disconnect() {
		// TODO Auto-generated method stub
		session.close(true);
	}

	public void sendRequest(String msg) {
		// TODO Auto-generated method stub
		QuerMessage message = new QuerMessage();
		message.setQueryMsg(msg);
		if(session != null && session.isConnected())
			session.write(message);
		else{
			notifyQueryMessage("请先连接服务器...");
		}
	}

	public void addServerListener(IServerListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	public void removeServerListener(IServerListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
		
	}

   public void notifyQueryMessage(String msg){
	   rt.notifyQuery();
	   String date = sdf.format(new Date());
	   for(int i=0;i<listeners.size();i++)
			listeners.get(i).notifyQueryMessage(date+"\n"+msg+"\n");
   }
	
	public void notifyMonitorMessage(String msg){
		 rt.notifyMonitor();
		 String date = sdf.format(new Date());
		   for(int i=0;i<listeners.size();i++)
				listeners.get(i).notifyMonitorMessage(date+"\n"+msg+"\n");
	}

	public void connected() {
		rt.notifyConnected();
		for(int i=0;i<listeners.size();i++)
			listeners.get(i).connected();
	}

	public void disconnected() {
		rt.notifyDisconnected();
		for(int i=0;i<listeners.size();i++)
			listeners.get(i).disConnected();
	}

	public String getConnectedIp() {
		return ip;
	}

	public int getConnectedPort() {
		return port;
	}

}
