/**
 *  YongJing Client Software API V1.0 
 *  
 *  2013-10-22
 */
package cn.iever.wxh.yjserver.comm;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;

import cn.iever.wxh.yjserver.core.msg.HeartMessage;

/**
 *  心跳机制实现  
 *  被动型心跳机制
 *   当客户端发来心跳包的时候  服务器端同时反馈心跳包
 * @author wuxuehong
 * @date 2013-10-22
 * 
 */
public class KeepAliveMessageFactoryImpl implements KeepAliveMessageFactory {

	/* (non-Javadoc)
	 * @see org.apache.mina.filter.keepalive.KeepAliveMessageFactory#isRequest(org.apache.mina.core.session.IoSession, java.lang.Object)
	 */
	public boolean isRequest(IoSession session, Object message) {
		// TODO Auto-generated method stub
//		System.out.println("isRequest:"+message);
//		if(message instanceof HeartMessage){
////			System.out.println("isRequest heart");
//			return true;
//		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.apache.mina.filter.keepalive.KeepAliveMessageFactory#isResponse(org.apache.mina.core.session.IoSession, java.lang.Object)
	 */
	public boolean isResponse(IoSession session, Object message) {
		// TODO Auto-generated method stub
//		System.out.println("isResponse:"+message);
		if(message instanceof HeartMessage){
////			System.out.println("isResponse heart");
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.apache.mina.filter.keepalive.KeepAliveMessageFactory#getRequest(org.apache.mina.core.session.IoSession)
	 */
	public Object getRequest(IoSession session) {
		// TODO Auto-generated method stub
//		System.out.println("getRequest");
		return new HeartMessage();
	}

	/* (non-Javadoc)
	 * @see org.apache.mina.filter.keepalive.KeepAliveMessageFactory#getResponse(org.apache.mina.core.session.IoSession, java.lang.Object)
	 */
	public Object getResponse(IoSession session, Object request) {
		// TODO Auto-generated method stub
//		System.out.println(request);
		return null;
	}

}
