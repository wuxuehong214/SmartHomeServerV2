package cn.iever.wxh.yjserver.resource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import sun.audio.*;

/**
 * 声音资源
 * @author Administrator
 *
 */
public class ResourceTone {
	
	public static void main(String args[]){
		ResourceTone r  = new ResourceTone();
		r.notifyConnected();
		System.out.println("over");
	} 
	
	public void notifyMonitor(){
		playTone2("t_query.wav");
	}
	
	public void notifyConnected(){
		playTone2("t_connect.wav");
	}
	
	public void notifyDisconnected(){
		playTone2("t_disconnect.wav");
		try {
			Thread.sleep(600);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		playTone2("t_disconnect.wav");
	}
	
	public void notifyQuery(){
		playTone2("t_query.wav");
	}
	
	
	@SuppressWarnings({ "restriction", "unused" })
	private void playTone(String wav){
		AudioStream as = null;
		URL url = ResourceTone.class.getClassLoader().getResource(wav);
		try {
			as = new AudioStream(url.openStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		AudioPlayer.player.start(as);
	}
	
	
	@SuppressWarnings("restriction")
	private void playTone2(String wav){
		FileInputStream fis = null;
		AudioStream as = null;
		try {
			fis = new FileInputStream("tone/"+wav);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			as = new AudioStream(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
		AudioPlayer.player.start(as);
	}

}
