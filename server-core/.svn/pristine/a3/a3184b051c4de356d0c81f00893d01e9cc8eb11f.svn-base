package cn.iever.wxh.yjserver.core.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class MySessionFactory {
	
	private static final SessionFactory sessionFactory;
	
	static{
		sessionFactory = new Configuration().configure().buildSessionFactory();
	}
	
	public static Session openSession(){
		return sessionFactory.openSession();
	}

	
}
