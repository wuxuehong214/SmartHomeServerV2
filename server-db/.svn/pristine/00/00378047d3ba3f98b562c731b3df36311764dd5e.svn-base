package cn.iever.wxh.yjserver.db.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import cn.iever.wxh.yjserver.core.model.DeviceVo;
import cn.iever.wxh.yjserver.core.model.STATE;
import cn.iever.wxh.yjserver.core.util.MySessionFactory;

public class DeviceDao {
	
	Logger log = Logger.getLogger(DeviceDao.class);
	
	/**
	 * 查询所有设备
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DeviceVo> queryAllDevices(){
		List<DeviceVo> list = null;
		try{
			Session session = MySessionFactory.openSession();
			Transaction ta = session.beginTransaction();
			list = session.createQuery("from "+DeviceVo.class.getName()).list();
			ta.commit();
			session.close();
		}catch(Exception e){
			log.warn(e.getMessage());
			return null;
		}
		return list;
	}
	
	/**
	 * 根据用户名或者设备ID查询设备信息
	 * @param uidOrDid
	 * @return
	 */
	public DeviceVo queryDeviceVoByUserId(String uid){
		DeviceVo device = null;
		try{
		Session session = MySessionFactory.openSession();
		Transaction ta = session.beginTransaction();
		Query query = session.createQuery("from DeviceVo where userId = ?");
		query.setParameter(0, uid);
		device = (DeviceVo) query.uniqueResult();
		ta.commit();
		session.close();
		}catch(Exception e){
			log.warn(e.getMessage());
			return null;
		}
		return device;
	}
	
	/**
	 * 根据设备ID查询
	 * @param deviceId   eg.0672ff505649804987042821
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DeviceVo> querDevicesByDeviceId(String deviceId){
		List<DeviceVo> devices = null;
		try{
			Session session = MySessionFactory.openSession();
			Transaction ta = session.beginTransaction();
			Query query = session.createQuery("from DeviceVo where deviceId = ?");
			query.setParameter(0, deviceId);
			devices = query.list();
//			Iterator<DeviceVo>  it = query.iterate();
//			if(it.hasNext()){
//				devices = new ArrayList<DeviceVo>();
//				while(it.hasNext()){
//					devices.add(it.next());
//				}
//			}
			ta.commit();
			session.close();
		}catch(Exception e){
			log.warn(e.getMessage());
			return null;
		}
		return devices;
	}
	
	/**
	 * 更新设备信息 主要更新  IP以及最新上线时间
	 * @param device
	 */
	public void updateDevice(DeviceVo device){
		try{
			Session session = MySessionFactory.openSession();
			Transaction ta = session.beginTransaction();
			session.update(device);
			ta.commit();
			session.close();
		}catch(Exception e){
			log.warn(e.getMessage());
		}
	}
	
	/**
	 * 根据状态信息查询设备
	 * @param state
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DeviceVo> queryDevicesByState(STATE state){
		List<DeviceVo> devices = null;
		String str = null;
		try{
			Session session = MySessionFactory.openSession();
			Transaction ta = session.beginTransaction();
			switch (state) {
			case ON:
				str = "from DeviceVo where convert(varchar(100),lasttime,112)=convert(varchar(100),getdate(),112)";
				break;
			case OFF:
				str = "from DeviceVo where convert(varchar(100),lasttime,112)!=convert(varchar(100),getdate(),112)";
				break;
			case ALL:
			default:
				str = "from DeviceVo";
				break;
			}
			Query query = session.createQuery(str);
			devices = query.list();
			ta.commit();
			session.close();
		}catch(Exception e){
			log.warn(e.getMessage());
			return null;
		}
		return devices;
	}

	
	
	

}
