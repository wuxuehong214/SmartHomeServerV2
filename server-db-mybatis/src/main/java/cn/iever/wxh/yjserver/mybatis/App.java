package cn.iever.wxh.yjserver.mybatis;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
    	
    	String resource = "mybatis-config.xml";
    	InputStream inputStream = Resources.getResourceAsStream(resource);
    	//应用范围  创建一次
    	SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    	//方法范围
    	SqlSession session = sqlSessionFactory.openSession();
    	
    	try{
    	
    		
    	}finally{
    		session.close();
    	}
    	
    	System.out.println(sqlSessionFactory);
        System.out.println( "Hello World!" );
    }
}
