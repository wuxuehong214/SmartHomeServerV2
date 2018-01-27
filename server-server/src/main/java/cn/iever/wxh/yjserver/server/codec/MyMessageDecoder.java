package cn.iever.wxh.yjserver.server.codec;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

import cn.iever.wxh.yjserver.core.msg.AbstractMessage;
import cn.iever.wxh.yjserver.core.msg.ClientLoginMessage;
import cn.iever.wxh.yjserver.core.msg.ClientLoginMessageNew;
import cn.iever.wxh.yjserver.core.msg.GateWayLongConnectMessage;
import cn.iever.wxh.yjserver.core.msg.GateWayShortConnectMessage;
import cn.iever.wxh.yjserver.core.msg.HeartMessage;
import cn.iever.wxh.yjserver.core.msg.QuerMessage;
import cn.iever.wxh.yjserver.core.msg.TransferedMessage;
import cn.iever.wxh.yjserver.server.communicate.CommunicateService;

/**
 * decoder
 * @author wuxuehong
 *
 * @date 2012-9-10
 */
public class MyMessageDecoder implements MessageDecoder {

	private Logger logger = Logger.getLogger(MyMessageDecoder.class);
	
	public static boolean showAviBag = false;

	public MyMessageDecoder() {
	}

	public MyMessageDecoder(Charset charset) {
	}

	public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
		return MessageDecoderResult.OK;
	}

	public MessageDecoderResult decode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		byte[][] bags = getAviliableNetBag(in);
		if(bags != null){
			int len = bags.length;
			for(int i=0;i<len;i++){
				if(bags[i] != null){
					byte[] bag =  decodeNetData(bags[i]);  //0-requestType 1...-request data
					if(bag != null && bag.length != 0){
						if(bag[0] != AbstractMessage.CLIENT_REQ_HEART && showAviBag)
							System.out.println("avi bag:"+bytesToHexStringSep(bag, 0, bag.length));
						  byte requestType = bag[0];
							switch (requestType) {
							//gateway report the ip 
							case AbstractMessage.GATEWAY_REQ_IP:
								processGatewayShortConnect(bag, out, session);
								break;
							case AbstractMessage.GATEWAY_REQ_CONNECT:
								processGatewayLongConnect(bag, out, session);
								break;
							case AbstractMessage.CLIENT_REQ_IP:
								processClientConnect(bag, out, session);
								break;
							case AbstractMessage.CLIENT_REQ_HEART:
								processHeart(bag, out, session);
								break;
							case AbstractMessage.CLIENT_REQ_BRIDGE:
								processClientConnectNew(bag, out, session);
								break;
							case AbstractMessage.INTERNAL_QUERY:
								processQueryMessage(bag, out, session);
								break;
							default:
								processTransferedMessage(bag, out, session);
								break;
							}
					}
				}
			}
		}
		if(in.remaining()>0){
			return MessageDecoder.NEED_DATA;
		}
		return MessageDecoderResult.OK;
	}

	public void finishDecode(IoSession arg0, ProtocolDecoderOutput arg1)
			throws Exception {
	}
	
	/**
	 * 服务器内部查询消息  
	 * @param bag
	 * @param out
	 * @param session
	 */
	private void processQueryMessage(byte[] bag, ProtocolDecoderOutput out, IoSession session){
		try{
			String query = new String(bag, 1, bag.length-1,"utf-8");
			QuerMessage msg = new QuerMessage();
			msg.setQueryMsg(query);
			out.write(msg);
		}catch(Exception e){
			logger.warn("processQueryMessage:"+e.getMessage());
		}
	}
	
	/**
	 * 客户端根据家庭主机域名请求网关与网关系统连接
	 * @param bag
	 * @param out
	 * @param session
	 */
	private void processClientConnectNew(byte[] bag, ProtocolDecoderOutput out, IoSession session){
		try{
			ClientLoginMessageNew msg = new ClientLoginMessageNew();
			int hostNameLen = bag[1]&0xff;// get the hostName  length
			String hostName = new String(bag,2,hostNameLen);
			msg.setHostName(hostName);
			
			int userNameLen = bag[2+hostNameLen]&0xff;  //get the username length
			String userName = new String(bag,3+hostNameLen,userNameLen);
			msg.setUserName(userName);
			
			int userPwdLen = bag[3+hostNameLen+userNameLen];   //get the pwd length
			String userPwd = new String(bag,4+hostNameLen+userNameLen,userPwdLen);
			msg.setUserPwd(userPwd);
			
			logger.debug("Client login: HostName:"+hostName+"\tUsername:"+userName+"\tUserPwd:"+userPwd);
			out.write(msg);
		}catch(Exception e){
			logger.warn("processClientConnectNew:"+e.getMessage());
		}
	}
	
	/**
	 * 网关请求短连接：汇报IP
	 */
	private void processGatewayShortConnect(byte[] bag, ProtocolDecoderOutput out,IoSession session){
		try{
			GateWayShortConnectMessage msg = new GateWayShortConnectMessage();
			byte idlength = bag[1];// get the id length
			String id = null;
			if (idlength > 0) {
				id = bytesToHexString(bag, 2, idlength);
			}
			msg.setDeviceId(id);
			
			//如果网关客户端汇报了IP 则使用汇报IP
			if(bag.length == 18){
				try{
				  byte[] ip = Arrays.copyOfRange(bag, 14, 18);
				  logger.info("Gateway ip:"+ip2Str(ip));	
				  if(ip[0] == 0x00){
					  ip = ((InetSocketAddress) session.getRemoteAddress()).getAddress().getAddress();
				  }
				  msg.setIp(ip);
				}catch(Exception e){
					logger.warn("processGatewayShortConnect:"+e.getMessage());	
					byte[] ip = ((InetSocketAddress) session.getRemoteAddress()).getAddress().getAddress();
					msg.setIp(ip);
				}
			}else{
				byte[] ip = ((InetSocketAddress) session.getRemoteAddress()).getAddress().getAddress();
				msg.setIp(ip);
			}
			out.write(msg);
		}catch(Exception e){
			logger.warn("processGatewayShortConnect:"+e.getMessage());	
		}
	}
	
	/**
	 * 网关请求长连接
	 */
	private void processGatewayLongConnect(byte[] bag, ProtocolDecoderOutput out,IoSession session){
		try{
			GateWayLongConnectMessage msg2 = new GateWayLongConnectMessage();
			int idlength = bag[1];// get the id length
			String id = null;
			if (idlength > 0) {
				id = bytesToHexString(bag, 2, idlength);
			}
			msg2.setDeviceId(id);
			byte[] ip = ((InetSocketAddress) session.getRemoteAddress()).getAddress().getAddress();
			msg2.setIp(ip);
			out.write(msg2);
		}catch(Exception e){
			logger.warn("processGatewayLongConnect:"+e.getMessage());
		}
	}
	
	/**
	 * 客户端连接服务器请求
	 */
	private void processClientConnect(byte[] bag, ProtocolDecoderOutput out,IoSession session){
		try{
			ClientLoginMessage msg = new ClientLoginMessage();
			byte userLen = bag[1];// get user name length
			String username = null;
			if (userLen > 0) {
				username = new String(bag, 2, userLen);// get username
			}
			msg.setUserName(username);
			byte passLen = bag[userLen + 2];// get pwd length
			String password = null;
			if (passLen > 0) {
				password = new String(bag, userLen + 3, passLen);// get pwd
			}
			msg.setPassWord(password);
			out.write(msg);
		}catch(Exception e){
			logger.warn("processClientConnect:"+e.getMessage());
		}
	}
	
	/**
	 * 处理心跳包
	 * @param bag
	 * @param out
	 * @param session
	 */
	private void processHeart(byte[] bag, ProtocolDecoderOutput out,IoSession session){
		//需要身份信息验证
		if(session.getAttribute(CommunicateService.AUTHORIZATION) == null) {
			logger.warn("authorization");
			session.close(true);
		}else{
			HeartMessage m = new HeartMessage();
			out.write(m);
		}
	}
	
	/**
	 * 处理转发消息
	 * @param bag
	 * @param out
	 * @param session
	 */
	private void processTransferedMessage(byte[] bag, ProtocolDecoderOutput out,IoSession session){
		try{
			//需要身份信息验证
			if(session.getAttribute(CommunicateService.AUTHORIZATION) == null) {
				logger.warn("invalid authorization");
				session.close(true);
			}else{
				TransferedMessage msg = new TransferedMessage();
				msg.setRealRequestType(bag[0]);
				msg.setData(Arrays.copyOfRange(bag, 1, bag.length));
				out.write(msg);
			}
		}catch(Exception e){
			logger.warn("processTransferedMessage:"+e.getMessage());
		}
	}
	
	/**
     * 
     * 分析缓冲区中数据  获取符合网络协议包(包头)的数据
     * 
     * @param buffer  数据缓冲区
     * 
     * @return       符合网络协议包的数据包
     * 
     */
    private byte[][] getAviliableNetBag(IoBuffer buffer){
    	try{
		        //获取缓冲区数据长度
		        int len = buffer.limit();
		        byte[] data = new byte[len];
		        buffer.get(data);
		        //定义二维数组变量
		        byte[][] command = null;        //return result
		        //符合协议头YJ的包的个数
		        int size = 0;
		        //根据协议头来判断包的个数
		        for(int i=0; i<len-1; i++){
		            if(data[i] == AbstractMessage.HEAD[0] && data[i+1] == AbstractMessage.HEAD[1]){
		                size++;
		            }
		        }
		        //如果没有包那么反回null
		        if(size == 0) {
		        	  if(data[len-1] == AbstractMessage.HEAD[0]){
		              	buffer.position(len-1);
		              	buffer.put(len-1,AbstractMessage.HEAD[0]);
		              }
		            return command;
		        }
		        //创建command维度
		        command = new byte[size][];
		        int index = -1;
		        int length = 0;
		        int lastIndex = len;
		        //遍历从0----len-2  
		        //注意len-1没有遍历到
		        for(int i=0; i<len-1; i++){
		            try{
		                if(data[i] == AbstractMessage.HEAD[0] && data[i+1] == AbstractMessage.HEAD[1]){
		                    index ++;
		                    //检查协议中数据长度指示的字节
		                    if(i+3 < len){
		                        //命令长度会有溢出  所以务必转换成正确的长度
		                        length = (int)(data[i+3]&0xff);
		                        //如果剩余数据不符合一个数据包  
		                        //如果这个数据包不是最后一个数据包 则说明该数据包不符合协议
		                        //如果这个数据包所最后一个数据包  则说迷宫你该数据包可能没接受完全 记录位置
		                        if(data.length-(i+4) < length+1) {
		                            if(index == size-1)
		                                lastIndex = i;
		                        }
		                        else
		                            command[index] = Arrays.copyOfRange(data, i, i+4+length+1);  //完整网络包 
		                    }else{
		                        //标识未完整网络包的起始位置
		                        if(index == size-1) lastIndex = i;
		                    }
		                }
		            }catch (Exception e) {
		                e.printStackTrace();
		            }
		        }
		//        buffer.clear();
		        //将不满一个数据帧的数据重新放入缓冲区
		        if(lastIndex != len)
		        	buffer.position(lastIndex);
		        for(int i=lastIndex;i<len;i++) {
		        	buffer.put(i,data[i]);
		        }
		        //如果缓冲区中最后一个字节所HEAD[0]则将其放回到缓冲区
		        if(data[len-1] == AbstractMessage.HEAD[0]){
		        	buffer.position(len-1);
		        	buffer.put(len-1,AbstractMessage.HEAD[0]);
		        }
		        return command;
    	}catch(Exception e){
    		buffer.clear();
    		logger.warn("getAviliableNetBag:"+e.getMessage());
    		return null;
    	}
    } 
    
    
    /**
     * 
     * 对符合网络协议包(包头)的数据包进行解码  主要判断协议头，数据区长度
     * 
     * @param  data   符合网络协议包包头的数据包
     * 
     * @return  [0]-请求类型  [1-n]-请求数据
     * 
     */
    private byte[] decodeNetData(byte[] data) {
    	try{
		        if(data == null) return null;
		        byte[] command = null;
		        int size = 0;
		        byte type = 0;
		        if(data.length>=4 && data[0] == AbstractMessage.HEAD[0] && data[1] ==AbstractMessage. HEAD[1]){
		            //请求类型
		            type = data[2];
		            //数据区长度
		            size = (int)(data[3]&0xff)+1; 
		            //反馈命令码
		            command = new byte[size];
		            //【0】--请求类型
		            command[0] = type;  
		             System.arraycopy(data, 4, command, 1, size-1);  //copy the data to the command
		        }
		        return command;
        }catch (Exception e) {
        	logger.warn("decodeNetData:"+e.getMessage());
        	return null;
        }
    }



	/**
	 * 判断是否需要更多数据来完成一个包
	 * @param data
	 * @return   0-NOT OK   1-OK    2-NEED DATA
	 */
	public int isDecodeAble(byte[] data){
		for(int i=0;i<data.length-1;i++){
			 if(data[i] == AbstractMessage.HEAD[0] && data[i+1] == AbstractMessage.HEAD[1]){
				   if(i+3<data.length){
					   int len = data[i+3];  //获取数据内容长度
					   //如果data中剩余数据长度 大于len+1表明该包可解
					   if(data.length-(i+3+1)>=(len+1)) return 1;
					   else return 2;
				   }
			 }
		}
		return 0;
	}
	/**
	 * turn bytes array to string
	 * @param buf
	 * @param offset
	 * @param length
	 * @return
	 */
	private String bytesToHexString(byte[] buf, int offset, int length) {
		StringBuffer sb = new StringBuffer("");
		if (buf == null || buf.length <= 0)
			return "";
		for (int i = offset; i < length+offset; i++) {
			int v = buf[i] & 0xFF;// 
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				sb.append("0");
			}
			sb.append(hv);
		}
		return sb.toString();
	}
	
	/**
	 * turn bytes array to string
	 * @param buf
	 * @param offset
	 * @param length
	 * @return
	 */
	private String bytesToHexStringSep(byte[] buf, int offset, int length) {
		StringBuffer sb = new StringBuffer("");
		if (buf == null || buf.length <= 0)
			return "";
		for (int i = offset; i <length+offset; i++) {
			int v = buf[i] & 0xFF;// 
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				sb.append("0");
			}
			sb.append(hv+" ");
		}
		return sb.toString();
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
}
