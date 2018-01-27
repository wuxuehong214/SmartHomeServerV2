package cn.iever.wxh.yjserver.db.dao;

import java.util.List;

import org.junit.Test;

import cn.iever.wxh.yjserver.core.model.DeviceVo;
import cn.iever.wxh.yjserver.core.model.STATE;
import cn.iever.wxh.yjserver.db.service.DeviceService;

/**
 * dao层测试
 * @author Administrator
 *
 */
public class TestDeviceDao {

	//@Test
	public void testQueryAllDevices(){
		DeviceDao ddao = new DeviceDao();
		List<DeviceVo> devices = ddao.queryAllDevices();
		System.out.println(devices.size());
	}
	
	//@Test
	public void testQueryDeviceByUserid(){
		DeviceDao ddao = new DeviceDao();
		DeviceVo device = ddao.queryDeviceVoByUserId("iever");
		System.out.println(device.toString());
		device.setRemark("永景智能家居展示厅");
		ddao.updateDevice(device);
	}
	
	@Test
	public void testQueryDeviceByDeviceId(){
		//066aff505649804987042018
		DeviceDao ddao = new DeviceDao();
		List<DeviceVo> devices = ddao.querDevicesByDeviceId("066aff505649804987042018");
		System.out.println(devices.size());
		for(int i=0;i<devices.size();i++){
			System.out.println(devices.get(i));
			ddao.updateDevice(devices.get(i));
		}
	}
	
	//@Test
	public void testUpdateDevice(){
		DeviceDao ddao = new DeviceDao();
		DeviceVo device = ddao.queryDeviceVoByUserId("iever");
		System.out.println(device);
		device.setRemark("test update!");
		ddao.updateDevice(device);
		System.out.println(device);
	}
	
	//@Test
	public void testQueryDeviceByState(){
		DeviceService dservice = new DeviceService();
		List<DeviceVo> devices = dservice.queryDevicesByState(STATE.OFF);
		System.out.println(devices.size());
	}
	
}
