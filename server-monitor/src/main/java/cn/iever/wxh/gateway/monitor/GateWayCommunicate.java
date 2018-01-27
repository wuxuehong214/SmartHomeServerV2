package cn.iever.wxh.gateway.monitor;

import java.io.BufferedWriter;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 网关连接
 * @author Administrator
 *
 */
public class GateWayCommunicate {
	//监听对象
	private List<IGatewayListener> listeners = new ArrayList<IGatewayListener>();
	private SocketThread session;
	
	/**
	 * 新增监听
	 * @param listener
	 */
	public void addListener(IGatewayListener listener){
		synchronized (listeners) {
			listeners.add(listener);
		}
	}
	
	/**
	 * 移除监听
	 * @param listener
	 */
	public void removeListener(IGatewayListener listener){
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}
	
	/**
	 * 连接网关
	 * @param ip
	 */
	public void connect(String ip,int port){
		session = new SocketThread(ip,port);
		new Thread(session).start();
	}
	
	/**
	 * 断开连接
	 */
	public void disconnect(){
		if(session != null)
			session.disconnect();
	}
	/**
	 * 发送命令
	 */
	public void send(String msg){
		if(session != null)
		session.send(msg);
	}
	
	/**
	 * 通知连接成功
	 */
	private void notifyConnected(){
		for(IGatewayListener listener:listeners)listener.connected();
	}
	/**
	 * 通知断开连接
	 */
	private void notifyDisconnected(){
		for(IGatewayListener listener:listeners)listener.disconnected();
	}
	/**
	 * 通知命令已经发送
	 * @param msg
	 */
	private void notifyCommandSent(String msg){
		for(IGatewayListener listener:listeners)listener.commandSent(msg);
	}
	/**
	 * 通知日志反馈
	 * @param msg
	 */
	private void notifyLog(String msg){
		msg = msg.replace("\n\n", "\n");
		for(IGatewayListener listener:listeners)listener.log(msg);
	}
	/**
	 * 连接线程
	 * @author Administrator
	 *
	 */
	class SocketThread implements Runnable{
		
		private String ip;
		private int port;
		private Socket socket;
		private InputStream in;
		private OutputStream os;
		
		public SocketThread(String ip,int port){
			this.ip = ip;
			this.port = port;
		}
		
		public void disconnect(){
			try{
				if(in != null)in.close();
				if(os != null)os.close();
				if(socket != null)socket.close();
			}catch(Exception e){
				
			}
		}
		public void send(String msg){
			if(os != null){
				String msg2 = msg+"\n";
				 try {
					 os.write(msg2.getBytes());
					 os.flush();
					System.out.println("发送命令:"+msg);
					notifyCommandSent(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		public void run() {
			InetSocketAddress address  = new InetSocketAddress(ip,port);
			socket = new Socket();
			try {
				socket.connect(address);
				in = socket.getInputStream();
				os = socket.getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
				//断开连接
				notifyDisconnected();
				return;
			}
			
			//连接建立
			notifyConnected();
			
			//读取数据
			byte[] buffer = new byte[128];
			int len = -1;
			while(true){
				try{
					len = in.read(buffer);
					if(len == -1) break;
					
//					notifyLog(CommonUtil.bytesToHexStringWithSeparate(Arrays.copyOfRange(buffer, 0, len)));
					String str = new String(buffer,0,len,"utf-8");
					notifyLog(str);
				}catch(Exception e){
					 e.printStackTrace();
					 break;
				}
			}
			//断开连接
			notifyDisconnected();
		}
		
	}
}
