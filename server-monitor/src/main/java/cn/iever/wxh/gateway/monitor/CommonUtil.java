package cn.iever.wxh.gateway.monitor;
/**
 * 常用函数工具�? * @author wuxuehong
 *
 */
public class CommonUtil {
	
	/**
	 * turn bytes array to string
	 * @param src
	 * @return
	 */
	public static String bytesToHexString(byte[] src){  
	    StringBuilder stringBuilder = new StringBuilder("");  
	    if (src == null || src.length <= 0) {  
	        return null;  
	    }  
	    for (int i = 0; i < src.length; i++) {  
	        int v = src[i] & 0xFF;  
	        String hv = Integer.toHexString(v);  
	        if (hv.length() < 2) {  
	            stringBuilder.append(0);  
	        }  
	        stringBuilder.append(hv);  
	    }  
	    return stringBuilder.toString();  
	}  
	
	/**
	 * 
	 * @param src
	 * @return
	 */
	public static String bytesToHexStringWithSeparate(byte[] src){  
	    StringBuilder stringBuilder = new StringBuilder("");  
	    if (src == null || src.length <= 0) {  
	        return null;  
	    }  
	    for (int i = 0; i < src.length; i++) {  
	        int v = src[i] & 0xFF;  
	        String hv = Integer.toHexString(v);  
	        if (hv.length() < 2) {  
	            stringBuilder.append(0);  
	        }  
	        stringBuilder.append(hv+" ");  
	    }  
	    return stringBuilder.toString();  
	}  
	
	/**
	 * turn in to byte array
	 * @param intValue
	 * @return
	 */
	public static byte[] int2byte(int intValue){
		byte[] b = new byte[4];
		for(int i=0;i<4;i++){
			b[i]=(byte)(intValue>>8*(3-i)&0xFF);
		}
		return b;
	}
	
	/**
	 * turn byte array to int
	 * @param data
	 * @return
	 */
	public static int byte2Int(byte[] data){
		int value = 0;
        for (int i = 0; i < data.length; i++) {
            int shift = (data.length - 1 - i) * 8;
            value += (data[i] & 0xFF) << shift;
        }
        return value;
	}

}
