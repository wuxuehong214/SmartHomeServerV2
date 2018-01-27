package cn.iever.wxh.yjserver.resource;

import javax.swing.ImageIcon;

import cn.iever.wxh.yjserver.monitor.panel.DownStatePanel;

public class ResourcePic {
	
	/**
	 * 系统托盘图标
	 * @return
	 */
	public static ImageIcon getTrayIcon(){
		return getImageIcon("p_monitor.png");
	}
	
	/**
	 * 作者信息图标
	 * @return
	 */
	public static ImageIcon getAuthIcon(){
		return getImageIcon("p_auth.png");
	}
	
	/**
	 * 连接图标
	 * @return
	 */
	public static ImageIcon getConnectIcon(){
		return getImageIcon("p_connect.png");
	}
	
	/**
	 * 断开连接图标
	 * @return
	 */
	public static ImageIcon getDisConnectIcon(){
		return getImageIcon("p_disconnect.png");
	}
	
	/**
	 * 管理菜单图标
	 * @return
	 */
	public static ImageIcon getManageIcon(){
		return getImageIcon("p_manage.png");
	}
	/**
	 * 发送图标
	 * @return
	 */
	public static ImageIcon getSendIcon(){
		return getImageIcon("p_send.png");
	}
	
	/**
	 * 悬浮窗提示
	 * @return
	 */
	public static ImageIcon getFloatTip(){
		return getImageIcon("p_tip.png");
	}
	
	/**
	 * 悬浮窗警告
	 * @return
	 */
	public static ImageIcon getFloatWarnIcon(){
		return getImageIcon("p_warn.png");
	}
	
	/**
	 * 悬浮窗背景
	 * @return
	 */
	public static ImageIcon getFloatBackgroudIcon(){
		return getImageIcon("p_float.png");
	}
	public static ImageIcon getImageIcon(String icon){
		ImageIcon icons = new ImageIcon(DownStatePanel.class.getClassLoader().getResource(icon));
		return icons;
	}
}
