package cn.iever.wxh.yjserver.core.dbAPI;

import java.util.List;

import cn.iever.wxh.yjserver.core.model.DeviceVo;
import cn.iever.wxh.yjserver.core.model.STATE;

/**
 * 设备服务API
 * @author Administrator
 *
 */
public interface IDeviceServiceAPI {
	
	/**
	 * 根据用户名查询设备
	 * @param uid
	 * @return
	 */
	public DeviceVo queryDeviceByUserId(String uid);
	
	/**
	 * 根据设备ID查询设备
	 * @param deviceId
	 * @return
	 */
	public List<DeviceVo> queryDevicesByDeviceId(String deviceId);
	
	/**
	 * 根据设备在线/离线状态查询设备数量
	 * @param state
	 * @return
	 */
	public int countByState(STATE state);
	
	/**
	 * 根据设备状态查询设备信息列表
	 * @param state
	 * @return
	 */
	public List<DeviceVo> queryDevicesByState(STATE state);
	
	/**
	 * 更新设备信息 主要更新IP以及最新上线时间
	 * @param device
	 */
	public void updateDeviceVo(DeviceVo device);

}
