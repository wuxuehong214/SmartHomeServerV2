package cn.iever.wxh.yjserver.server.help;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import cn.iever.wxh.yjserver.core.dbAPI.IDeviceServiceAPI;
import cn.iever.wxh.yjserver.core.model.DeviceVo;
import cn.iever.wxh.yjserver.core.model.STATE;
import cn.iever.wxh.yjserver.server.codec.MyMessageDecoder;
import cn.iever.wxh.yjserver.server.communicate.CommunicateService;

/**
 * 服务器帮助类
 * 
 * @author wuxuehong
 * 
 */
public class ServerHelp {

//	private IDeviceServiceAPI deviceService;
//	private CommunicateService communicateService;
//	private Date starttime;
//
//	public ServerHelp(IDeviceServiceAPI deviceService,
//			CommunicateService communicateService) {
//		starttime = new Date();
//		this.deviceService = deviceService;
//		this.communicateService = communicateService;
//	}
//
//	public void startCommandService() {
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//		String str = null;
//		while (true) {
//			try {
//				str = br.readLine().toLowerCase();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			if("exit".equals(str)){
//				System.exit(0);
//			}else if("help".equals(str)){
//				System.out.println(getHelpInfo());
//			}else if("starttime".equals(str)){
//				SimpleDateFormat sdf = new SimpleDateFormat(
//						"yyyy-MM-dd HH:mm:ss");
//				System.out.println(sdf.format(starttime));
//			}else if("count all".equals(str)){
//				try {
//					System.out.println("Total devices:"
//							+ deviceService.countByState(STATE.ALL));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}else if("count online".equals(str)){
//				try {
//					System.out.println("Online devices:"
//							+  deviceService.countByState(STATE.ON));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}else if("count outline".equals(str)){
//				try {
//					System.out.println("Outline devices:"
//							+  deviceService.countByState(STATE.OFF));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}else if( "ls all".equals(str)){
//				try {
//					List<DeviceVo> devices = deviceService.queryDevicesByState(STATE.ALL);
//					for (int i = 0; i < devices.size(); i++) {
//						System.out.println((i + 1) + ":"
//								+ devices.get(i).toShortString());
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}else if("ls online".equals(str)){
//				try {
//					List<DeviceVo> devices = deviceService.queryDevicesByState(STATE.ON);
//					for (int i = 0; i < devices.size(); i++) {
//						System.out.println((i + 1) + ":"
//								+ devices.get(i).toShortString());
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}else if("ls outline".equals(str)){
//				try {
//					List<DeviceVo> devices = deviceService.queryDevicesByState(STATE.OFF);
//					for (int i = 0; i < devices.size(); i++) {
//						System.out.println((i + 1) + ":"
//								+ devices.get(i).toShortString());
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}else if("export all".equals(str)){
//				try {
//					export(deviceService.queryDevicesByState(STATE.ALL), "all.txt");
//					System.out.println("OK");
//				} catch (Exception e1) {
//					e1.printStackTrace();
//				}
//			}else if("export online".equals(str)){
//				try {
//					export(deviceService.queryDevicesByState(STATE.ON), "online.txt");
//					System.out.println("OK");
//				} catch (Exception e1) {
//					e1.printStackTrace();
//				}
//			}else if("export outline".equals(str)){
//				try {
//					export(deviceService.queryDevicesByState(STATE.OFF), "outline.txt");
//					System.out.println("OK");
//				} catch (Exception e1) {
//					e1.printStackTrace();
//				}
//			}else if("connect gateway".equals(str)){
//				System.out.println("Current connected gateways:"
//						+ communicateService.getConnectedGateway());
//			}else if("connect client".equals(str)){
//				System.out.println("Current connected clients:"
//						+ communicateService.getConnectedClients());
//			}else if("detail gateway".equals(str)){
//				communicateService.detailGateway();
//			}else if("detail client".equals(str)){
//				communicateService.detailClient();
//			}else if("show avi".equals(str)){
//				MyMessageDecoder.showAviBag = true;
//				System.out.println("show bags ok!");
//			}else if("hide avi".equals(str)){
//				MyMessageDecoder.showAviBag = false;
//				System.out.println("hide bags ok!");
//			}else{
//				if (str.startsWith("ls")) {
//					@SuppressWarnings("resource")
//					Scanner s = new Scanner(str);
//					s.next();
//					if (s.hasNext()) {
//						String id = s.next();
//						DeviceVo device = null;
//						device = deviceService.queryDeviceByUserId(id);
//						if (device != null) {
//							System.out.println(device.toString());
//						} else
//							System.out.println("Device not exists!");
//					}
//				} else
//					System.out.println("Unkown command request:" + str);
//			}
//		}
//	}
//
//	/**
//	 * 获取帮助信息
//	 * @return
//	 */
//	private String getHelpInfo(){
//		StringBuffer sb = new StringBuffer();
//		sb.append("---------help info-----------\n");
//		sb.append("starttime: get the system start time!\n");
//		sb.append("count all: query the total devices!\n");
//		sb.append("count online: query the online devices!\n");
//		sb.append("count outline: query the outline devices!\n");
//		sb.append("ls [device id or user id]: display the detail information for the device!\n");
//		sb.append("ls all: display all the devices information to the txt file!\n");
//		sb.append("ls online: display the online devices information to the txt file!\n");
//		sb.append("ls outline: display the outline devices information to the txt file!\n");
//		sb.append("export all: export all the devices information to the txt file!\n");
//		sb.append("export online: export the online devices information to the txt file!\n");
//		sb.append("export outline: export the outline devices information to the txt file!\n");
//		sb.append("connect gateway: show current connected gateways!\n");
//		sb.append("connect client: show current connected clients!\n");
//		sb.append("detail gateway: show current connected gateways in detail!\n");
//		sb.append("detail client: show current connected clients in detail!\n");
//		sb.append("show/hide avi: show aviliable bags!\n");
//		sb.append("------------end------------\n");
//		return sb.toString();
//	}
//	
//	/**
//	 * export the devices
//	 * @param devices
//	 * @param filename
//	 */
//	public void export(List<DeviceVo> devices, String filename){
//		if(devices == null)
//			System.out.println("Null to export!");
//		try{
//			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filename)));
//			for(int i=0;i<devices.size();i++){
//				DeviceVo device = devices.get(i);
//				bw.write(device.toShortString());
//				bw.newLine();
//			}
//			bw.flush();
//			bw.close();
//		}catch(IOException e){System.out.println(e.getMessage());}
//	}
}
