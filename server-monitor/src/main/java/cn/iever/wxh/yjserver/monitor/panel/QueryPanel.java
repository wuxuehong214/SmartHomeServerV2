package cn.iever.wxh.yjserver.monitor.panel;

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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import cn.iever.wxh.yjserver.monitor.event.ICommunicateService;
import cn.iever.wxh.yjserver.monitor.event.IServerListener;
import cn.iever.wxh.yjserver.resource.ResourcePic;

@SuppressWarnings("serial")
public class QueryPanel extends JPanel implements IServerListener{
	
//	private JPanel upPanel;
	private JPanel centerPanel;
	private JPanel downPanel;
	
//	private JTextField jtf;
//	private JButton bt_query;
	
	private JTextArea jta;
	
//	private JButton bt_online;
//	private JButton bt_gateway;
	private JTextField jtf_command;
	private JButton bt_send;
	
	private ICommunicateService comService;
	
	public QueryPanel(ICommunicateService comService){
		this.comService = comService;
		init();
		comService.addServerListener(this);
	}
	
	
	private void init(){
		this.setLayout(new BorderLayout(10,10));
		
//		upPanel = new JPanel();
//		upPanel.setLayout(new FlowLayout());
//		jtf = new JTextField(20);
//		bt_query = new JButton("查询");
//		bt_query.setEnabled(false);
//		bt_query.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				  String text = jtf.getText();
//				  text = "ls "+text;
//				  comService.sendRequest(text);
//			}
//		});
//		upPanel.add(jtf);
//		upPanel.add(bt_query);
//		this.add(upPanel,BorderLayout.NORTH);
		
		centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		JScrollPane pane = new JScrollPane();
		jta = new JTextArea();
		jta.setEditable(false);
		pane.setViewportView(jta);
		centerPanel.add(pane);
		this.add(centerPanel,BorderLayout.CENTER);
		
		downPanel = new JPanel();
		downPanel.setLayout(new BorderLayout());
		jtf_command = new JTextField("请输入命令...");
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
							JOptionPane.showMessageDialog(QueryPanel.this, "请输入查询命令!", "请输入命令", JOptionPane.WARNING_MESSAGE);
							return;
						}
						jtaAppend("查询命令:"+query+"\n");
						comService.sendRequest(query);
						jtf_command.setText("");
				}
			}
		});
		downPanel.add(jtf_command,BorderLayout.CENTER);
		bt_send = new JButton("发送");
		bt_send.setIcon(ResourcePic.getSendIcon());
		bt_send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String query = jtf_command.getText();
				if("".equals(query.trim())){
					JOptionPane.showMessageDialog(QueryPanel.this, "请输入查询命令!", "请输入命令", JOptionPane.WARNING_MESSAGE);
					return;
				}
				jtaAppend("查询命令:"+query+"\n");
				comService.sendRequest(query);
				jtf_command.setText("");
			}
		});
		bt_send.setEnabled(false);
		downPanel.add(bt_send,BorderLayout.EAST);
		this.add(downPanel,BorderLayout.SOUTH);
		
		this.validate();
	}


	public void connected() {
		bt_send.setEnabled(true);
	}


	public void disConnected() {
		bt_send.setEnabled(false);
	}


	public void notifyQueryMessage(String msg) {
		jtaAppend(msg);
	}
	
	private void jtaAppend(String msg){
		jta.append(msg);
		jta.setCaretPosition(jta.getText().length());
	}


	public void notifyMonitorMessage(String msg) {
		
	}

}
