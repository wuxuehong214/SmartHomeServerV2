package cn.iever.wxh.yjserver.core.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "YJ_Device")
public class DeviceVo implements Serializable {

	private static final long serialVersionUID = 8606212129516415832L;

	/**
	 * 老版本中：用户名
	 * 新版本中： 家庭域名  HostName
	 */
	private String userId; // 用户名
	/**
	 * 老版本中：密码
	 * 新版本中：无用...
	 */
	private String userPwd; // 密码
	private String deviceId; // 设备ID
	private String remark; // 备注信息
	private int onoff; // 是否在心啊 1-在线 0-离线
	private int level1; // 1级锁定 网关锁
	private int level2; // 2级锁定 远程锁
	private String ip; // ip
	private Date lastTime; // 最新上线时间

	private static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
	@Id
	@Column(name="user_id",unique=true,nullable=false)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name="user_pwd")
	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	@Column(name="device_id")
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Column(name="remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name="onoff")
	public int getOnoff() {
		return onoff;
	}

	public void setOnoff(int onoff) {
		this.onoff = onoff;
	}

	@Column(name="level1")
	public int getLevel1() {
		return level1;
	}

	public void setLevel1(int level1) {
		this.level1 = level1;
	}

	@Column(name="level2")
	public int getLevel2() {
		return level2;
	}

	public void setLevel2(int level2) {
		this.level2 = level2;
	}

	@Column(name="ip")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name="lasttime")
	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	@Override
	public String toString() {
		return "DeviceVo [userId=" + userId + ", userPwd=" + userPwd
				+ ", deviceId=" + deviceId + ", remark=" + remark + ", onoff="
				+ onoff + ", level1=" + level1 + ", level2=" + level2 + ", ip="
				+ ip + ", lastTime=" + lastTime + "]";
	}

	public String toShortString(){
		return userId+":["+deviceId+"]-["+ip+"]";
	}
	
	public String toQueryString(){
		return userId+":["+deviceId+"]-["+ip+"]-["+sdf.format(lastTime)+"]";
	}
}
