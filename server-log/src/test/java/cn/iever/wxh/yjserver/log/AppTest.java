package cn.iever.wxh.yjserver.log;

import org.apache.log4j.Logger;
import org.junit.Test;


/**
 * Unit test for simple App.
 */
public class AppTest 
{
	
	@Test
	public void testLog(){
		
		Logger log = Logger.getLogger(AppTest.class);
		log.debug("debug!!!"); 
		log.info("info!!!"); 
		log.warn("warn!!!"); 
		log.error("error!!!"); 
		log.fatal("fatal!!!");
		
	}
    
}
