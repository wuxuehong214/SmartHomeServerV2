package cn.iever.wxh.yjserver.comm;


import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import cn.iever.wxh.yjserver.core.msg.MonitorMessage;
import cn.iever.wxh.yjserver.core.msg.QuerMessage;
import cn.iever.wxh.yjserver.monitor.event.ICommunicateService;

public class CommunicateHandler extends IoHandlerAdapter {
	
	private org.apache.log4j.Logger logger = Logger.getLogger(CommunicateHandler.class);
	private ICommunicateService comService;
	
	public CommunicateHandler(ICommunicateService comService){
		this.comService = comService;
	}
	
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		QuerMessage qm = new QuerMessage();
		qm.setQueryMsg("login");
		session.write(qm);
		comService.connected();
		logger.info("connected!");
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		comService.disconnected();
		logger.info("disConnected!");
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
//		logger.info("idled!");
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		logger.warn("exception:"+cause.getMessage());
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		if(message instanceof QuerMessage)
			comService.notifyQueryMessage(((QuerMessage)message).getQueryMsg());
		else if(message instanceof MonitorMessage){
			comService.notifyMonitorMessage(((MonitorMessage)message).getMonitor());
		}
	}

}
