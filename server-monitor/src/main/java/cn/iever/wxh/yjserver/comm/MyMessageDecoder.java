package cn.iever.wxh.yjserver.comm;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

import cn.iever.wxh.yjserver.core.msg.AbstractMessage;
import cn.iever.wxh.yjserver.core.msg.HeartMessage;
import cn.iever.wxh.yjserver.core.msg.MonitorMessage;
import cn.iever.wxh.yjserver.core.msg.QuerMessage;

/**
 * decoder
 * @author wuxuehong
 *
 * @date 2012-9-10
 */
public class MyMessageDecoder implements MessageDecoder {

	private Logger logger = Logger.getLogger(MyMessageDecoder.class);
	
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
						if(bag[0] != AbstractMessage.CLIENT_REQ_HEART )
						  logger.info("avi bag:"+bytesToHexStringSep(bag, 0, bag.length));
						  byte requestType = bag[0];
							switch (requestType) {
							//gateway report the ip 
							case AbstractMessage.CLIENT_REQ_HEART:
								processHeart(bag, out, session);
								break;
							case AbstractMessage.INTERNAL_QUERY:
								processQueryMessage(bag, out, session);
								break;
							case AbstractMessage.INTERNAL_MONITOR:
								processMonitorMessage(bag, out, session);
								break;
							default:
								logger.info("Unknown msg!");
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
	 * 服务器内部状态消息  
	 * @param bag
	 * @param out
	 * @param session
	 */
	private void processMonitorMessage(byte[] bag, ProtocolDecoderOutput out, IoSession session){
		try{
			String query = new String(bag, 1, bag.length-1,"utf-8");
			MonitorMessage msg = new MonitorMessage();
			msg.setMonitor(query);
			out.write(msg);
		}catch(Exception e){
			logger.warn("processMonitorMessage:"+e.getMessage());
		}
	}
	
	/**
	 * 处理心跳包
	 * @param bag
	 * @param out
	 * @param session
	 */
	private void processHeart(byte[] bag, ProtocolDecoderOutput out,IoSession session){
			out.write(new HeartMessage());
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

}
