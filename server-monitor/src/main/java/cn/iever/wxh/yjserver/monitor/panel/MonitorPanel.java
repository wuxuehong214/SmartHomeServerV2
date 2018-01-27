package cn.iever.wxh.yjserver.monitor.panel;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import cn.iever.wxh.yjserver.monitor.event.ICommunicateService;
import cn.iever.wxh.yjserver.monitor.event.IServerListener;

public class MonitorPanel extends JPanel implements IServerListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7135134500631199099L;
	
	@SuppressWarnings("unused")
	private ICommunicateService comService;
	
	private JTextArea jta;

	public MonitorPanel(ICommunicateService comService){
		this.comService = comService;
		init();
		comService.addServerListener(this);
	}
	
	private void init(){
		this.setLayout(new BorderLayout());
		JScrollPane pane = new JScrollPane();
		jta = new JTextArea();
		jta.setEditable(false);
		this.add(pane);
		pane.setViewportView(jta);
		this.validate();
	}

	public void connected() {
		jtaAppend("连接建立了...\n");
	}

	public void disConnected() {
		jtaAppend("连接断开了...\n");
	}

	public void notifyQueryMessage(String msg) {
		
	}

	public void notifyMonitorMessage(String msg) {
		jtaAppend(msg);
	}
	
	private void jtaAppend(String msg){
		jta.append(msg);
		jta.setCaretPosition(jta.getText().length());
	}

}
