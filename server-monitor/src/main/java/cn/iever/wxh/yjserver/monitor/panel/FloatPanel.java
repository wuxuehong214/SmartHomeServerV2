package cn.iever.wxh.yjserver.monitor.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import cn.iever.wxh.yjserver.monitor.App;
import cn.iever.wxh.yjserver.monitor.event.ICommunicateService;
import cn.iever.wxh.yjserver.monitor.event.IServerListener;
import cn.iever.wxh.yjserver.resource.ResourcePic;

public class FloatPanel extends JDialog implements IServerListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7989439505965131676L;
	
	private int beForX = 0;
	private int beForY = 0;
	
	private App app;
	private ICommunicateService comService;
	private boolean connected = false;
	
	private JLabel jl_show;
	private JButton bt_connect;
	
	private ImagePanel imagePanel;
	
	
	public FloatPanel(App app,ICommunicateService comService){
		this.app = app;
		this.comService = comService;
		init();
		this.comService.addServerListener(this);
	}
	
	private void init(){
		this.setUndecorated(true);
		this.setSize(630, 36);
		this.setLayout(new BorderLayout());
		
		imagePanel = new ImagePanel(ResourcePic.getFloatBackgroudIcon());
		this.add(imagePanel);
		imagePanel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		jl_show = new JLabel("显示监控信息...");
//		jl_show.setBorder(BorderFactory.createLineBorder(Color.red));
		jl_show.setIcon(ResourcePic.getFloatTip());
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 2;
		c.weighty = 1;
		c.insets = new Insets(5, 5, 5, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		imagePanel.add(jl_show,c);
		
		bt_connect = new JButton("连接");
		bt_connect.setIcon(ResourcePic.getConnectIcon());
		c.gridx = 1;
		c.weightx = 0.1;
		c.anchor = GridBagConstraints.EAST;
		c.fill = GridBagConstraints.VERTICAL;
		c.insets = new Insets(5, 0, 5, 5);
		imagePanel.add(bt_connect,c);
		bt_connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(connected){
					comService.disconnect();
				}else{
					boolean t = comService.connect(comService.getConnectedIp(),comService.getConnectedPort());
					if(!t){
						JOptionPane.showMessageDialog(FloatPanel.this,"连接失败...", "警告", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		bt_connect.setEnabled(false);
		
		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    final Dimension frameSize = getSize();
	    
		this.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				int cx = FloatPanel.this.getLocation().x + e.getX() - beForX;
				int cy = FloatPanel.this.getLocation().y + e.getY() - beForY;
				
				if(cx < 100) cx = 0;
				if(cy < 100) cy = 0;
				
				if(screenSize.width-(cx+frameSize.width) < 100) cx = screenSize.width-frameSize.width;
				if(screenSize.height-(cy+frameSize.height) < 100) cy = screenSize.height-frameSize.height;
				FloatPanel.this.setLocation(cx,cy);
			}
			public void mousePressed(MouseEvent e) {
				beForX = e.getX();
				beForY = e.getY();
			}
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					FloatPanel.this.dispose();
					
					FloatPanel.this.app.setExtendedState(JFrame.NORMAL);
					FloatPanel.this.app.setVisible(true);
					FloatPanel.this.app.toFront();
				}
			}
		});
		
		this.addMouseMotionListener(new MouseMotionListener() {
			public void mouseMoved(MouseEvent e) {
			}
			public void mouseDragged(MouseEvent e) {
				FloatPanel.this.setLocation(FloatPanel.this.getLocation().x + e.getX() - beForX,FloatPanel.this.getLocation().y + e.getY() - beForY);
			}
		});
	    this.setLocation((screenSize.width - frameSize.width) / 2,0);
	}


	public void connected() {
		jl_show.setIcon(ResourcePic.getFloatTip());
		jl_show.setText("连接已建立....");
		
		bt_connect.setEnabled(true);
		bt_connect.setText("断开");
		bt_connect.setIcon(ResourcePic.getDisConnectIcon());
		connected = true;
	}


	public void disConnected() {
		jl_show.setIcon(ResourcePic.getFloatWarnIcon());
		jl_show.setText("连接已断开....");
		
		bt_connect.setText("连接");
		bt_connect.setIcon(ResourcePic.getConnectIcon());
		connected = false;
	}


	public void notifyQueryMessage(String msg) {
		jl_show.setText(msg);
	}


	public void notifyMonitorMessage(String msg) {
		jl_show.setText(msg);
	}
	
	class ImagePanel extends JPanel{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 9079580241070786016L;
		
		ImageIcon icon;
		
		public ImagePanel(ImageIcon icon){
			this.icon = icon;
			this.repaint();
		}
		
		public void paintComponent(Graphics g){
		    if(icon != null)
		    	g.drawImage(icon.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
		  }
		
	}
}


