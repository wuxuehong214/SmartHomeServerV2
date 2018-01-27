package cn.iever.wxh.gateway.monitor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class GatewayDialog extends JDialog implements IGatewayListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7582798496096850829L;
	private GateWayCommunicate communicate;
	private UpControlPanel upPanel;
	private JPanel centerPanel;
	private JTextArea jta;
	private DownControlPanel downPanel;
	
	public GatewayDialog(){
		this.communicate = new GateWayCommunicate();
		init();
		communicate.addListener(this);
	}
	
	private void init(){
		
		this.setLayout(new BorderLayout());
		this.setSize(600, 550);
		upPanel = new UpControlPanel(communicate);
		this.add(upPanel, BorderLayout.NORTH);
		
		centerPanel = new JPanel();
		JScrollPane pane = new JScrollPane();
		jta = new JTextArea();
		jta.setEditable(false);
		jta.setWrapStyleWord(true);
		jta.setLineWrap(true);
		this.add(centerPanel,BorderLayout.CENTER);
		centerPanel.setLayout(new BorderLayout());
		pane.setViewportView(jta);
		centerPanel.add(pane);
		centerPanel.setBorder(BorderFactory.createTitledBorder("网关控信息"));
		
		downPanel = new DownControlPanel(communicate);
		this.add(downPanel,BorderLayout.SOUTH);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				System.out.println("关闭了...网关监控");
				communicate.disconnect();
				GatewayDialog.this.setVisible(false);
				GatewayDialog.this.dispose();
			}
		});
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    Dimension frameSize = getSize();
	    this.setLocation((screenSize.width - frameSize.width) / 2,(screenSize.height - frameSize.height) / 2);
	}

	public void connected() {
		// TODO Auto-generated method stub
		jta.append("连接建立...\n");
		jta.setCaretPosition(jta.getText().length());
	}

	public void disconnected() {
		// TODO Auto-generated method stub
		jta.append("连接断开了...\n");
		jta.setCaretPosition(jta.getText().length());
	}

	public void log(String msg) {
		// TODO Auto-generated method stub
		jta.append(msg);
		jta.setCaretPosition(jta.getText().length());
	}

	public void commandSent(String msg) {
		// TODO Auto-generated method stub
		jta.append("=========Command:"+msg+"========\n");
		jta.setCaretPosition(jta.getText().length());
	}

}
