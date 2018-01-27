package cn.iever.wxh.yjserver.monitor;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import cn.iever.wxh.gateway.monitor.GatewayDialog;
import cn.iever.wxh.yjserver.comm.CommunicateService;
import cn.iever.wxh.yjserver.monitor.event.ICommunicateService;
import cn.iever.wxh.yjserver.monitor.event.IServerListener;
import cn.iever.wxh.yjserver.monitor.panel.DownStatePanel;
import cn.iever.wxh.yjserver.monitor.panel.FloatPanel;
import cn.iever.wxh.yjserver.monitor.panel.MonitorPanel;
import cn.iever.wxh.yjserver.monitor.panel.QueryPanel;
import cn.iever.wxh.yjserver.monitor.panel.UpStatePanel;
import cn.iever.wxh.yjserver.resource.ResourceFont;
import cn.iever.wxh.yjserver.resource.ResourcePic;

/**
 * Hello world!
 *
 */
public class App extends JFrame implements IServerListener
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 568736023079333250L;
	
	private UpStatePanel upPanel;
	private DownStatePanel downPanel;
	private QueryPanel queryPanel;
	private MonitorPanel monitorPanel;
	private FloatPanel floatPanel;
	
	private TrayIcon trayIcon;
	private  SystemTray tray;
	
	private ICommunicateService comService;
	
	
	public App(){
		
		comService = new CommunicateService();
		
		//全局字体设置
		Enumeration<Object> keys =  UIManager.getDefaults().keys();
		while(keys.hasMoreElements()){
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if(key instanceof String){
				if("Button.font".equals(key))
					UIManager.put(key, ResourceFont.getButtonFont());
			}
			
			if(value instanceof Font)
				UIManager.put(key, ResourceFont.getMenuFont());
		}
		
		//界面风格设置
		try {
			//com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		//初始化界面
		init();
		
		//添加监听
		comService.addServerListener(this);
		
		
	}
	
	public void init(){
		
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		this.setSize(850,550);
		this.setTitle("湖南永景科技智能家居服务器监控系统");
		this.setIconImage(ResourcePic.getTrayIcon().getImage());
		
		if(SystemTray.isSupported()){
			//系统托盘
			tray = SystemTray.getSystemTray();
			
			PopupMenu pm  = new PopupMenu();
			MenuItem exitItem = new MenuItem("exit");
			pm.add(exitItem);
			exitItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					 System.exit(0);
				}
			});
			
			trayIcon = new TrayIcon(ResourcePic.getTrayIcon().getImage(),"永景智能家居服务器监控系统",pm);
			trayIcon.setImageAutoSize(true);
			trayIcon.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount() == 2){
						App.this.setExtendedState(JFrame.NORMAL);
						App.this.setVisible(true);
						App.this.toFront();
					}
				}
			});
			
			try {
				tray.add(trayIcon);
			} catch (AWTException e1) {
				e1.printStackTrace();
			}
		
		}
		
		floatPanel = new FloatPanel(this,comService);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowIconified(WindowEvent e) {
				App.this.setVisible(false);
				App.this.dispose();
				
				floatPanel.setVisible(true);
				floatPanel.toFront();
			}
		});
		
		//菜单
		JMenuBar mb = new JMenuBar();
		this.setJMenuBar(mb);
//		mb.setBorder(BorderFactory.createLineBorder(Color.GRAY));
//		mb.setBackground(Color.cyan);
		JMenu menu = new JMenu("管理");
		menu.setIcon(ResourcePic.getManageIcon());
		mb.add(menu);
		JMenuItem item = new JMenuItem("网关监控"); 
		menu.add(item);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					GatewayDialog gd = new GatewayDialog();
					gd.setVisible(true);
			}
		});
		
		
		//面板分布
		upPanel = new UpStatePanel(comService);
//		upPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		this.add(upPanel, BorderLayout.NORTH);
		
		
		//中部面板分布
		JPanel centerPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.3;
		c.weighty = 1.3;
		monitorPanel = new MonitorPanel(comService);
		monitorPanel.setBorder(BorderFactory.createTitledBorder("服务器监控信息"));
		centerPanel.add(monitorPanel, c);
		
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		queryPanel = new QueryPanel(comService);
		queryPanel.setBorder(BorderFactory.createTitledBorder("查询信息"));
		centerPanel.add(queryPanel,c);
		this.add(centerPanel,BorderLayout.CENTER);
		centerPanel.validate();
		
		
		
		downPanel = new DownStatePanel(comService);
		this.add(downPanel,BorderLayout.SOUTH);
		
		//
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    Dimension frameSize = getSize();
	    this.setLocation((screenSize.width - frameSize.width) / 2,(screenSize.height - frameSize.height) / 2);
		this.setVisible(true);
	}
	
	
    public static void main( String[] args )
    {
       new App();
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
