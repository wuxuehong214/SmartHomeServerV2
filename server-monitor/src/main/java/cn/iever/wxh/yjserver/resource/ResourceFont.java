package cn.iever.wxh.yjserver.resource;

import java.awt.Font;

public class ResourceFont {
	
	/**
	 * 菜单字体
	 * @return
	 */
	public static Font getMenuFont(){
		return new Font("微软雅黑",Font.PLAIN,12);
	}
	
	/**
	 * 按钮字体
	 * @return
	 */
	public static Font getButtonFont(){
		return new Font("楷体",Font.PLAIN | Font.BOLD,12);
	}
	
	/**
	 * 文本框字体
	 * @return
	 */
	public  static Font getTextFieldFont(){
		return new Font("楷体",Font.PLAIN | Font.BOLD,12);
	}

}
