package cn.iever.wxh.yjserver.monitor.panel;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cn.iever.wxh.yjserver.monitor.event.ICommunicateService;
import cn.iever.wxh.yjserver.monitor.event.IServerListener;
import cn.iever.wxh.yjserver.resource.ResourcePic;

@SuppressWarnings("serial")
public class UpStatePanel extends JPanel implements IServerListener{
	
	private ICommunicateService comService;
	
	public UpStatePanel(ICommunicateService comService){
		this.comService = comService;
		init();
		comService.addServerListener(this);
	}
	
	private JTextField jtf_ip;
	private JTextField jtf_port;
	
	private JButton bt_connect;
	private JButton bt_disconnect;
	
	private void init(){
		
		this.setLayout(new FlowLayout());
		jtf_ip = new JTextField("www.iever.cn",15);
		this.add(jtf_ip);
		jtf_port = new JTextField("6000",6);
		this.add(jtf_port);
		bt_connect = new JButton("连接");
		bt_connect.setIcon(ResourcePic.getConnectIcon());
		bt_connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					String ip = jtf_ip.getText();
					int port =Integer.parseInt(jtf_port.getText()) ;
					boolean t = comService.connect(ip, port);
					if(!t){
						JOptionPane.showMessageDialog(UpStatePanel.this,"连接失败...", "警告", JOptionPane.WARNING_MESSAGE);
					}
				}catch(Exception e2){
					JOptionPane.showMessageDialog(UpStatePanel.this, e2.getMessage(), "Exception", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		this.add(bt_connect);
		bt_disconnect = new JButton("断开");
		bt_disconnect.setIcon(ResourcePic.getDisConnectIcon());
		bt_disconnect.setEnabled(false);
		bt_disconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 comService.disconnect();
			}
		});
		this.add(bt_disconnect);
		
//		this.setBorder(BorderFactory.createTitledBorder("服务器连接"));
		
//		bt_connect.setFont(ResourceFont.getButtonFont());
//		bt_disconnect.setFont(ResourceFont.getButtonFont());
//		jtf_ip.setFont(ResourceFont.getTextFieldFont());
//		jtf_port.setFont(ResourceFont.getTextFieldFont());
		this.validate();
	}

	public void connected() {
		bt_connect.setEnabled(false);
		bt_disconnect.setEnabled(true);
		jtf_ip.setEditable(false);
	}

	public void disConnected() {
		bt_connect.setEnabled(true);
		bt_disconnect.setEnabled(false);
		jtf_ip.setEditable(true);
	}

	public void notifyQueryMessage(String msg) {
		
	}

	public void notifyMonitorMessage(String msg) {
		
	}

}
