package cn.iever.wxh.yjserver.server.handler;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import cn.iever.wxh.yjserver.core.dbAPI.IDeviceServiceAPI;
import cn.iever.wxh.yjserver.core.model.DeviceVo;
import cn.iever.wxh.yjserver.core.msg.AbstractMessage;
import cn.iever.wxh.yjserver.core.msg.ClientLoginMessage;
import cn.iever.wxh.yjserver.core.msg.ClientLoginMessageNew;
import cn.iever.wxh.yjserver.core.msg.GateWayLongConnectMessage;
import cn.iever.wxh.yjserver.core.msg.GateWayShortConnectMessage;
import cn.iever.wxh.yjserver.core.msg.QuerMessage;
import cn.iever.wxh.yjserver.core.msg.TransferedMessage;
import cn.iever.wxh.yjserver.server.communicate.CommunicateService;

/**
 * sever handler 
 * 
 * deal with all the logic operations
 * 
 * @author wuxuehong
 * 
 *         2012-5-3
 */
public class ServerHandler extends IoHandlerAdapter {
	
	private IDeviceServiceAPI deviceService;
	private CommunicateService comService;
	private Logger logger = Logger.getLogger(ServerHandler.class);
	
	/**
	 * 构造函数
	 * @param dbService
	 */
	public ServerHandler(IDeviceServiceAPI deviceService,CommunicateService comService){
		this.deviceService = deviceService;
		this.comService = comService;
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
//		logger.info("session opened");
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		AbstractMessage msg = (AbstractMessage) message;
		byte requestType = msg.getRequestType();
		switch (requestType) {
		// report the ip of the device
		case AbstractMessage.GATEWAY_REQ_IP:                      //网关请求汇报IP
			processGatewayShortConnect(session, message);
			break;
		case AbstractMessage.GATEWAY_REQ_CONNECT:           //网关请求建立长连接
			processGatewayLongConnect(session, message);
			break;
		//client request ip
		case AbstractMessage.CLIENT_REQ_IP:         //客户端请求IP(老版本 单用户，账户信息由服务器和网关同步维护)
			processClientConnect(session, message);
			break;
		case AbstractMessage.CLIENT_REQ_BRIDGE:  //客户端请求IP (新版本 支持多级用户,账户信息由网关维护)
			processClientConnectNew(session, message);
			break;
		case AbstractMessage.INTERNAL_QUERY:
			processQueryMessage(session, message);
			break;
		default:
			processTransfer(session, message);
			break;
		}
	}
	
	/**
	 * 处理查询命令
	 * @param session
	 * @param message
	 */
	public void processQueryMessage(IoSession session, Object message){
		QuerMessage msg = (QuerMessage)message;
		comService.processQueryMessage(session, msg);
	}
	
	/**
	 * 处理网关设备短连接请求
	 * 
	 * @param session
	 * @param message
	 */
	private void processGatewayShortConnect(IoSession session, Object message){
		GateWayShortConnectMessage gsm = (GateWayShortConnectMessage) message;
		String devid = gsm.getDeviceId();// get device ID
		List<DeviceVo> devices = deviceService.queryDevicesByDeviceId(devid);
		if(devices != null && devices.size()>0){
			byte[] clientIP = gsm.getIp();// get IP
			String ip = ip2Str(clientIP);
			for(int i=0;i<devices.size();i++){
				devices.get(i).setIp(ip);
				deviceService.updateDeviceVo(devices.get(i));
			}
			logger.info("[" + devid+"]Logined[S]: ["+ip+"]-["+devices.get(0).getUserId()+"]");
			comService.processMonitorMessage("网关汇报IP:["+devid+"]-["+ip+"]-["+devices.get(0).getUserId()+"]");
		}else{
			logger.warn("Uknown device id:"+devid);
			comService.processMonitorMessage("网关汇报IP:["+devid+"]-[设备ID未知]");
		}
		session.close(false);
	}
	
	/**
	 * 处理网关设备长连接请求
	 * @param session
	 * @param message
	 */
	private void processGatewayLongConnect(IoSession session, Object message){
		GateWayLongConnectMessage gcrm = (GateWayLongConnectMessage)message;
		String deviceId = gcrm.getDeviceId();
		List<DeviceVo> devices = deviceService.queryDevicesByDeviceId(deviceId);
		if(devices != null && devices.size()>0){
			//新增网关客户端会话
			comService.newGatewaySession(session,devices.get(0));
			
			//建立长连接的同时也将IP信息更新到数据库
			byte[] clientIP = gcrm.getIp();// get IP
			String ip = ip2Str(clientIP);
			for(int i=0;i<devices.size();i++){
					devices.get(i).setIp(ip);
					deviceService.updateDeviceVo(devices.get(i));
			}
			logger.info("[" + deviceId+"]Logined[L]: ["+ip+"]-["+devices.get(0).getUserId()+"]");
			comService.processMonitorMessage("网关与服务器建立长连接:["+deviceId+"]-["+devices.get(0).getUserId()+"]");
		}else{
			logger.warn("Uknown device id:"+deviceId);
			comService.processMonitorMessage("网关与服务器建立长连接:["+deviceId+"]-[设备ID未知]");
		}
	}
	
	/**
	 * 处理客户端连接  新版多级用户客户端连接处理
	 * @param session
	 * @param message
	 */
	private void processClientConnectNew(IoSession session, Object message){
		boolean connected = false;
		ClientLoginMessageNew msg = (ClientLoginMessageNew)message;
		String hostName = msg.getHostName();
		DeviceVo device = deviceService.queryDeviceByUserId(hostName);
		if(device == null){
			logger.info("["+hostName+"]:Host is not exist!");
			msg.setResult(AbstractMessage.CLIENT_RESP_NOTEXIST);// host not exists
			msg.setIp(new byte[] { 0x00, 0x00, 0x00, 0x00 });
			comService.processMonitorMessage("客户端请求服务器:["+hostName+"]-[域名未知]");
		}else if(device.getLevel2() == 1){
			logger.info("["+hostName+"]:Remote visit is locked");
			msg.setResult(AbstractMessage.CLIENT_RESP_LOCKED);
			msg.setIp(new byte[] { 0x00, 0x00, 0x00, 0x00 });
			comService.processMonitorMessage("客户端请求服务器:["+hostName+"]-[远程锁定]");
		}else {
			if(comService.isGateWayExist(device.getDeviceId())){  //如果当前网关会话存在 则直接建立连接  ： 需要继续发送身份信息验证
				comService.processMonitorMessage("客户端请求服务器:["+hostName+"]-[建立中转连接...]");
				comService.newClientSessionNew(session,device,msg.getUserName(),msg.getUserPwd());
				logger.info("["+hostName+"]:login.....!");
				connected = true;
			}else{
				msg.setResult(AbstractMessage.CLIENT_RESP_IP);
				msg.setIp(str2Ip(device.getIp()));
				logger.info("["+hostName+"]:Response IP new:"+device.getIp());
				comService.processMonitorMessage("客户端请求服务器:["+hostName+"]-[获取IP:"+device.getIp()+"]");
			}
		}
		if(!connected) {
			session.write(msg);
			session.close(false);
		}
	}
	
	/**
	 * 客户端连接请求
	 * @param session
	 * @param message
	 */
	private void processClientConnect(IoSession session, Object message){
		boolean connected = false;
		ClientLoginMessage cim = (ClientLoginMessage) message;
		String username = cim.getUserName();
		String password = cim.getPassWord();
		DeviceVo device = deviceService.queryDeviceByUserId(username);
		
		if(device == null){    //网关设备信息不存在
			logger.info("["+username+"]:User not exist!");
			cim.setResult(AbstractMessage.CLIENT_RESP_NOTEXIST);// user not exists
			cim.setIp(new byte[] { 0x00, 0x00, 0x00, 0x00 });
			comService.processMonitorMessage("OLD客户端请求服务器:["+username+"]-[用户名不存在]");
		}else if(device.getLevel2() == 1){   //远程锁锁定
			logger.info("["+username+"]:Remote visit is locked");
			cim.setResult(AbstractMessage.CLIENT_RESP_LOCKED);
			cim.setIp(new byte[] { 0x00, 0x00, 0x00, 0x00 });
			comService.processMonitorMessage("OLD客户端请求服务器:["+username+"]-[远程锁定]");
		}else if(password != null && !password.equals(device.getUserPwd())){   //用户名密码错误
			logger.info("["+username+"]:Password error!");
			cim.setResult(AbstractMessage.CLIENT_RESP_FAIL);// password error
			cim.setIp(new byte[] { 0x00, 0x00, 0x00, 0x00 });
			comService.processMonitorMessage("OLD客户端请求服务器:["+username+"]-[密码错误]");
		}else{        //验证通过
			if(comService.isGateWayExist(device.getDeviceId())){  //如果当前网关会话存在 则直接建立连接   :  已经经过身份信息验证可以直接发送控制信息
				cim.setResult(AbstractMessage.CLIENT_RESP_CONNECT);
				cim.setIp(str2Ip(device.getIp()));
				logger.info("["+username+"]:Logined!");
				comService.newClientSession(session,device);
				connected = true;
				comService.processMonitorMessage("OLD客户端请求服务器:["+username+"]-[远程连接建立]");
			}else{                                                                       //如果当前网关会话 不存在  则反馈IP
				cim.setResult(AbstractMessage.CLIENT_RESP_IP);
				cim.setIp(str2Ip(device.getIp()));
				logger.info("["+username+"]:Response IP:"+device.getIp());
				comService.processMonitorMessage("OLD客户端请求服务器:["+username+"]-[获取IP:"+device.getIp()+"]");
			}
		}
		session.write(cim);
		if(!connected) 	session.close(false);
	}
	
	/**
	 * 处理消息转发
	 * @param session
	 * @param message
	 */
	private void processTransfer(IoSession session, Object message){
		comService.processData(session, (TransferedMessage) message);
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		//after send the client response , close the session
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		comService.removeSession(session);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
	    session.close(true);
	    logger.info("Session idle:"+session.getRemoteAddress().toString());
	    comService.processMonitorMessage("连接空闲:["+session.getRemoteAddress().toString()+"]");
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		 logger.warn("Exception:"+cause.getMessage());
		 comService.processMonitorMessage("服务器异常:["+cause.getMessage()+"]");
		 session.close(true);
	}

	/**
	 * byte arrays to str
	 * @param ip
	 * @return
	 */
	private String ip2Str(byte[] ip){
		if(ip == null ||ip.length != 4) return "";
		String r;
		r  = ""+(ip[0]&0xff)+"."+(ip[1]&0xff)+"."+(ip[2]&0xff)+"."+(ip[3]&0xff);
		return r;
	}
	
	/**
	 * str to byte arrays
	 * @param ip
	 * @return
	 */
	private byte[] str2Ip(String ip){
		byte[] def = new byte[]{0x00,0x00,0x00,0x00};
		try{
			if(ip == null || "".equals(ip)) return def;
			String[] str = ip.split("\\.");
			if(str.length != 4) return def;
			byte[] d = new byte[4];
			for(int i=0;i<4;i++)d[i] = (byte) Integer.parseInt(str[i]);
			return d;
		}catch(Exception e){
			return def;
		}
	}
}
