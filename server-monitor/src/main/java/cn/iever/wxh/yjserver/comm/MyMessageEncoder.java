package cn.iever.wxh.yjserver.comm;

import java.nio.charset.Charset;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import cn.iever.wxh.yjserver.core.msg.AbstractMessage;
import cn.iever.wxh.yjserver.core.msg.ClientLoginMessage;
import cn.iever.wxh.yjserver.core.msg.ClientLoginMessageNew;
import cn.iever.wxh.yjserver.core.msg.QuerMessage;
import cn.iever.wxh.yjserver.core.msg.TransferedMessage;

/**
 * 协议编码器
 * @author wuxuehong
 *
 * @date 2012-9-10
 */
public class MyMessageEncoder implements MessageEncoder<AbstractMessage> {
	
	private Logger logger = Logger.getLogger(MyMessageEncoder.class);
	
	public MyMessageEncoder(){
		
	}

	public MyMessageEncoder(Charset charset) {
	}

	
	public void encode(IoSession session, AbstractMessage message,
			ProtocolEncoderOutput out) throws Exception {
		IoBuffer buf = IoBuffer.allocate(100).setAutoExpand(true);
		AbstractMessage msg = (AbstractMessage)message;
		byte key = msg.getRequestType();
		switch (key) {
		case AbstractMessage.CLIENT_REQ_IP:
			ClientLoginMessage ipResponse=(ClientLoginMessage)message;
			buf.put(AbstractMessage.HEAD);//把YJ放进缓冲区
			buf.put(AbstractMessage.CLIENT_REQ_IP);//0xf4:请求IP的命令
			buf.put((byte) 0x05);//把数据区长度（4）放进缓冲区
			buf.put(ipResponse.getResult());//把请求结果信息（登陆成功或失败） 放进缓冲区
			buf.put(ipResponse.getIp());//把请求的IP放进去
			buf.put((byte) 0x01);//把帧结束符号放进缓冲区
			buf.flip();
			out.write(buf);
			break;
		case AbstractMessage.CLIENT_GATWWAY_COMMUNICATE:
			TransferedMessage transferMsg = (TransferedMessage)message;
			buf.put(AbstractMessage.HEAD);//把YJ放进缓冲区
			buf.put(transferMsg.getRealRequestType());//数据请求类型
			if(transferMsg.getAttachData() != -1)
				buf.put((byte) (transferMsg.getData().length+1));//把数据区长度放进缓冲区
			else
				buf.put((byte) transferMsg.getData().length);//把数据区长度放进缓冲区
			buf.put(transferMsg.getData());//把请求结果信息 放进缓冲区
			//如果附加数据不为-1 则加入包中
			if(transferMsg.getAttachData() != -1){
				buf.put(transferMsg.getAttachData());
			}
			buf.put((byte) 0x01);//把帧结束符号放进缓冲区
			buf.flip();
			out.write(buf);
			break;
		case AbstractMessage.CLIENT_REQ_HEART:
			buf.put(AbstractMessage.HEAD);//把YJ放进缓冲区
			buf.put(AbstractMessage.CLIENT_REQ_HEART);//0xf4:请求IP的命令
			buf.put((byte) 0x00);//把数据区长度放进缓冲区
			buf.put((byte) 0x01);//把帧结束符号放进缓冲区
			buf.flip();
			out.write(buf);
			break;
		case AbstractMessage.CLIENT_REQ_BRIDGE:  //远程登录结果反馈
			ClientLoginMessageNew glm = (ClientLoginMessageNew)message;
			buf.put(AbstractMessage.HEAD);//把YJ放进缓冲区
			buf.put(AbstractMessage.CLIENT_REQ_BRIDGE);
			buf.put((byte)0x06);
			buf.put(glm.getRole());        //角色ID
			buf.put(glm.getResult());     //结果
			buf.put(glm.getIp());            //ip信息
			buf.put((byte) 0x01);//把帧结束符号放进缓冲区
			buf.flip();
			out.write(buf);
			break;
		case AbstractMessage.INTERNAL_QUERY:  //查询信息结果反馈
			QuerMessage qm = (QuerMessage)message;
			buf.put(AbstractMessage.HEAD);//把YJ放进缓冲区
			buf.put(AbstractMessage.INTERNAL_QUERY);
			byte[] b = qm.getQueryMsg().getBytes("utf-8");
			buf.put((byte)b.length);
			buf.put(b);
			buf.put((byte)0x01);
			buf.flip();
			out.write(buf);
			break;
		default:
			logger.warn("Unkown responsed messages!");
			break;
		}
	}

}
