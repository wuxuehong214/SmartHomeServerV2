package cn.iever.wxh.yjserver.core.model;

/**
 * 登录服务器的客户端身份
 * @author Administrator
 * 
 * 各个连接上服务器的会话身份鉴别
 *
 */
public enum STATUS {
	TMEPCLIENT                       //临时会话      客户端登陆服务器请求IP
	,NEWCLIENT                        //新会话          通过服务器与网关建立中转连接   待登录验证
	,CLIENT                                //客户端会话    通过服务器与网关建立中转连接 已经进行过登录验证
	,GATWAY                              //网关会话    
	,MONITOR                           //服务器监视客户端
}
