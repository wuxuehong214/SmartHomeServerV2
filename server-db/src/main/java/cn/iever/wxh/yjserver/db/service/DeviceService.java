package cn.iever.wxh.yjserver.db.service;

import java.util.Date;
import java.util.List;

import cn.iever.wxh.yjserver.core.dbAPI.IDeviceServiceAPI;
import cn.iever.wxh.yjserver.core.model.DeviceVo;
import cn.iever.wxh.yjserver.core.model.STATE;
import cn.iever.wxh.yjserver.db.dao.DeviceDao;

/**
 * 设备信息访问服务层
 * @author Administrator
 *
 */
public class DeviceService  implements IDeviceServiceAPI{
	
	private DeviceDao deviceDao;
	
	public DeviceService(){
		deviceDao = new DeviceDao();
	}
	
	/**
	 * 根据userid查询设备信息 
	 * @param uid
	 * @return  返回null/DeviceVo
	 */
	public DeviceVo queryDeviceByUserId(String uid){
		return deviceDao.queryDeviceVoByUserId(uid);
	}
	
	/**
	 * 根据设备ID查询设备信息
	 * 一个设备ID有可能对应多个设备信息 (多账号登录)
	 * @param deviceId
	 * @return  null/List
	 */
	public List<DeviceVo> queryDevicesByDeviceId(String deviceId){
		return deviceDao.querDevicesByDeviceId(deviceId);
	}
	
	/**
	 * 根据设备状态信息 统计设备数量
	 * @param state
	 * @return  0/n
	 */
	public int countByState(STATE state){
		List<DeviceVo> devices = deviceDao.queryDevicesByState(state);
		return devices==null?0:devices.size();
	}
	
	/**
	 * 根据状态信息查询设备详细信息
	 * @param state 
	 * @return   null/list
	 */
	public List<DeviceVo> queryDevicesByState(STATE state){
		return deviceDao.queryDevicesByState(state);
	}
	
	
	/**
	 * 更新设备信息 主要更新设备IP以及最新上线时间信息
	 * @param device
	 */
	public void updateDeviceVo(DeviceVo device){
		device.setLastTime(new Date());
		deviceDao.updateDevice(device);
	}
	
	

}
