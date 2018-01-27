package cn.iever.wxh.gateway.monitor;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cn.iever.wxh.yjserver.resource.ResourcePic;

/**
 * 底部查询面板
 * @author Administrator
 *
 */
public class DownControlPanel extends JPanel implements IGatewayListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5331949599353676957L;
	
	private JTextField jtf_command;
	private JButton bt_send;
	private GateWayCommunicate communicate;
	
	
	public DownControlPanel(GateWayCommunicate communicate){
		this.communicate = communicate;
		init();
		communicate.addListener(this);
	}
	

	public void init(){
		this.setLayout(new BorderLayout());
		jtf_command = new JTextField("请输入命令...");
		jtf_command.setEditable(false);
		jtf_command.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(jtf_command.getText().startsWith("请输入命令"))
					jtf_command.setText("");
			}
		});
		jtf_command.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
						String query = jtf_command.getText();
						if("".equals(query.trim())){
							JOptionPane.showMessageDialog(DownControlPanel.this, "请输入查询命令!", "请输入命令", JOptionPane.WARNING_MESSAGE);
							return;
						}
						communicate.send(query);
						jtf_command.setText("");
				}
			}
		});
		this.add(jtf_command,BorderLayout.CENTER);
		bt_send = new JButton("发送");
		bt_send.setIcon(ResourcePic.getSendIcon());
		bt_send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String query = jtf_command.getText();
				if("".equals(query.trim())){
					JOptionPane.showMessageDialog(DownControlPanel.this, "请输入查询命令!", "请输入命令", JOptionPane.WARNING_MESSAGE);
					return;
				}
				communicate.send(query);
				jtf_command.setText("");
			}
		});
		bt_send.setEnabled(false);
		this.add(bt_send,BorderLayout.EAST);
	}


	public void connected() {
		// TODO Auto-generated method stub
		  bt_send.setEnabled(true);
		  jtf_command.setEditable(true);
	}


	public void disconnected() {
		// TODO Auto-generated method stub
		 bt_send.setEnabled(false);
		  jtf_command.setEditable(false);
	}


	public void log(String msg) {
		// TODO Auto-generated method stub
		
	}


	public void commandSent(String msg) {
		// TODO Auto-generated method stub
		
	}
}
