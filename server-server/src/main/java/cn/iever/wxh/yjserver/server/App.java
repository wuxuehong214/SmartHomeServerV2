package cn.iever.wxh.yjserver.server;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import cn.iever.wxh.yjserver.core.dbAPI.IDeviceServiceAPI;
import cn.iever.wxh.yjserver.core.model.Parameters;
import cn.iever.wxh.yjserver.db.service.DeviceService;
import cn.iever.wxh.yjserver.server.codec.KeepAliveMessageFactoryImpl;
import cn.iever.wxh.yjserver.server.codec.MyMessageCodecFactory;
import cn.iever.wxh.yjserver.server.codec.MyMessageDecoder;
import cn.iever.wxh.yjserver.server.codec.MyMessageEncoder;
import cn.iever.wxh.yjserver.server.communicate.CommunicateService;
import cn.iever.wxh.yjserver.server.handler.ServerHandler;

/**
 * Hello world!
 *
 */
public class App 
{
	//log instance
	private Logger logger = Logger.getLogger(App.class	);
	//db for device operation instance
	private IDeviceServiceAPI deviceService;
	//session operation instance
	private CommunicateService communicateService;
	//server parameters
	private Parameters defaultParameters;
	//server acceptor;
	private IoAcceptor acceptor;
	//help for server
//	private ServerHelp help;
	/**
	 * 构造函数
	 * @param args
	 */
	public App(String args[]){
		deviceService = new DeviceService();
		defaultParameters = new Parameters();
		communicateService = new CommunicateService(deviceService);
//		help = new ServerHelp(deviceService, communicateService);
		
		Parameters parameters = parameterDeal(args);
		if(parameters == null)  return;
		startServer();
//		help.startCommandService();
	}
	
	/**
	 * 参数处理
	 */
	private Parameters parameterDeal(String args[]){
		try{
		if(args != null)
			for(int i=0;i<args.length;i++){
				if(args[i].startsWith("-p") || args[i].startsWith("-P")){
					defaultParameters.setPort(Integer.parseInt(args[i].substring(2)));
				}else if(args[i].startsWith("-t") || args[i].startsWith("-T")){
					defaultParameters.setThreadNum(Integer.parseInt(args[i].substring(2)));
					if(defaultParameters.getThreadNum()<0 || defaultParameters.getThreadNum() > 10)throw new Exception("Unaviliabel threadNum!");
				}else if(args[i].startsWith("-h")||args[i].startsWith("-H")){
					defaultParameters.setHeartPeriod(Integer.parseInt(args[i].substring(2)));
				}else if(args[i].toLowerCase().equals("log")){
					defaultParameters.setDetailLog(true);
				}else if(args[i].toLowerCase().equals("avi")){
					MyMessageDecoder.showAviBag = true;
				}else if(args[i].toLowerCase().startsWith("-i")){
//					defaultParameters.setSlqServerIp(args[i].substring(2));
				}
			}
		}catch(Exception e){
			logger.warn("Bad parameters!"+e.getMessage());
			System.out.println(defaultParameters.getHelpInfo());
			return null;
		}
		return defaultParameters;
	}
	
	/**
	 * 启动服务
	 */
	private void startServer(){
		try{
			//创建接收器
			acceptor = new NioSocketAcceptor(defaultParameters.getThreadNum());
			//日志信息过滤
			if(defaultParameters.isDetailLog())
				acceptor.getFilterChain().addLast("logger", new LoggingFilter());
			//编码解码过滤
			acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MyMessageCodecFactory(new MyMessageDecoder(Charset.forName("utf-8")), new MyMessageEncoder(Charset.forName("utf-8")))));
			//心跳过滤
			KeepAliveMessageFactoryImpl kamfi = new KeepAliveMessageFactoryImpl();
			KeepAliveFilter kaf = new KeepAliveFilter(kamfi, IdleStatus.READER_IDLE);
			kaf.setForwardEvent(true); //idle事件回发  当session进入idle状态的时候 依然调用handler中的idled方法
			kaf.setRequestInterval(defaultParameters.getHeartPeriod());  //本服务器为被定型心跳  即需要每heartPeriod秒接受一个心跳请求  否则该连接进入空闲状态 并且发出idled方法回调
			acceptor.getFilterChain().addLast("heart", kaf);
			//绑定逻辑处理器
			acceptor.setHandler(new ServerHandler(deviceService, communicateService));
			//绑定端口
			acceptor.bind(new InetSocketAddress(defaultParameters.getPort()));
			logger.info(defaultParameters.toString());
		}catch(Exception e){
			logger.warn("Start server execption:"+e.getMessage());
			stopServer();
		}
		
	}
	
	/**
	 * 停止服务
	 */
	private void stopServer(){
		if(acceptor == null) return;
		acceptor.unbind();
		acceptor.dispose();
		acceptor = null;
	}
	
	
    public static void main( String[] args )
    {
        new App(args);
    }
}
