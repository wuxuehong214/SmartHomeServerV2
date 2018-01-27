package cn.iever.wxh.gateway.monitor;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cn.iever.wxh.yjserver.monitor.panel.UpStatePanel;
import cn.iever.wxh.yjserver.resource.ResourcePic;

/**
 * 
 * 
 * 
 * @author Administrator
 *
 */
public class UpControlPanel extends JPanel implements IGatewayListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8931734586744558791L;
	
	private JTextField jtf_ip;   
	private JTextField jtf_port;
	private JButton bt_connect;
	private JButton bt_disconnect;
	private GateWayCommunicate communicate;
	
	public UpControlPanel(GateWayCommunicate communicate){
		this.communicate = communicate;
		init();
		communicate.addListener(this);
	}
	
	private void init(){
		this.setLayout(new FlowLayout());
		
		jtf_ip = new JTextField("192.168.1.233",15);
		this.add(jtf_ip);
		
		jtf_port = new JTextField("5001",10);
		this.add(jtf_port);
		
		bt_connect = new JButton("连接");
		bt_connect.setIcon(ResourcePic.getConnectIcon());
		this.add(bt_connect);
		bt_connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					String ip = jtf_ip.getText();
					int port = 5001;
					try{
						port = Integer.parseInt(jtf_port.getText());
					}catch(Exception e2){
						port = 5001;
					}
					
					communicate.connect(ip,port);
					bt_connect.setEnabled(false);
			}
		});
		
		bt_disconnect = new JButton("断开");
		bt_disconnect.setIcon(ResourcePic.getDisConnectIcon());
		bt_disconnect.setEnabled(false);
		bt_disconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 communicate.disconnect();
			}
		});
		
		this.add(bt_disconnect);
	}

	public void connected() {
		// TODO Auto-generated method stub
		bt_connect.setEnabled(false);
		jtf_ip.setEditable(false);
		bt_disconnect.setEnabled(true);
	}

	public void disconnected() {
		// TODO Auto-generated method stub
		bt_connect.setEnabled(true);
		jtf_ip.setEditable(true);
		bt_disconnect.setEnabled(false);
	}

	public void log(String msg) {
		// TODO Auto-generated method stub
		
	}

	public void commandSent(String msg) {
		// TODO Auto-generated method stub
		
	}

}
