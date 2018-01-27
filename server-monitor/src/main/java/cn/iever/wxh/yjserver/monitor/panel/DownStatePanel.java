package cn.iever.wxh.yjserver.monitor.panel;


import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import cn.iever.wxh.yjserver.monitor.event.ICommunicateService;
import cn.iever.wxh.yjserver.monitor.event.IServerListener;
import cn.iever.wxh.yjserver.resource.ResourcePic;

public class DownStatePanel extends JPanel implements IServerListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1107981617375376732L;
	private ICommunicateService comService;;
	
	private JLabel l_auth; 
	
	public DownStatePanel(ICommunicateService comService){
		this.comService = comService;
		init();
		this.comService.addServerListener(this);
	}
	
	private void init(){
		l_auth = new JLabel("");
		l_auth.setIcon(ResourcePic.getAuthIcon());
		l_auth.setText("作者：武学鸿    日期： 2014-05-07  版本：V1.0      ");
		this.setLayout(new BorderLayout());
		this.add(l_auth,BorderLayout.WEST);
	}

	public void connected() {
		
	}

	public void disConnected() {
		
	}

	public void notifyQueryMessage(String msg) {
		
	}

	public void notifyMonitorMessage(String msg) {
		
	}

}
