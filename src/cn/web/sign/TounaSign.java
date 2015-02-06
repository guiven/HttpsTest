package cn.web.sign;
/*
 * Created on 2003-12-7 by Liudong
 */
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;


import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.cookie.*;
import org.apache.commons.httpclient.methods.*;
import org.codehaus.jackson.map.ObjectMapper;

import cn.web.constants.HttpType;
/**
 * @author chenjie2
 */
public class TounaSign extends WebTemplete{
    static final String LOGON_SITE = "www.touna.cn";
//    static String GETURL = HttpType.HTTPS.getValue()+LOGON_SITE+"/auth.do?method=login&username=guiven&md5Pwd=f72c4a466ce21375799079e57d874c76&valicode=";
//    static String GETSIGNURL = HttpType.HTTP.getValue()+LOGON_SITE+"/bbs/bbsSign.do?method=getSignById";
//    static String SIGNURL = HttpType.HTTP.getValue()+LOGON_SITE+"/bbs/bbsSign.do?method=addSign&pic=&content=1";
    static final int LOGON_PORT = 8080;
    
    public static void main(String[] args) throws Exception{
        HttpClient client = new HttpClient();
/*        client.getHostConfiguration().setHost(LOGON_SITE, LOGON_PORT);
        //模拟登录页面login.jsp->main.jsp
        PostMethod post = new PostMethod("/main.jsp");
        GetMethod get = new GetMethod("");
        NameValuePair name = new NameValuePair("name", "ld");     
        NameValuePair pass = new NameValuePair("password", "ld");     
        post.setRequestBody(new NameValuePair[]{name,pass});
       int status = client.executeMethod(post);
       System.out.println(post.getResponseBodyAsString());
       post.releaseConnection();  */
       //查看cookie信息
/*		CookieSpec cookiespec = CookiePolicy.getDefaultSpec();
		Cookie[] cookies = cookiespec.match(LOGON_SITE, LOGON_PORT, "/", false,
				client.getState().getCookies());
		if (cookies.length == 0) {
			System.out.println("None");
		} else {
			for (int i = 0; i < cookies.length; i++) {
				System.out.println(cookies[i].toString());
			}
		}*/
		// 访问登陆页面
        
		GetMethod get = new GetMethod(composeUrl(HttpType.HTTPS,LOGON_SITE,composeUri("/auth.do?method=login&username=guiven&md5Pwd=f72c4a466ce21375799079e57d874c76&valicode=")));
		client.executeMethod(get);
		System.out.println("login:"+get.getStatusLine());
		System.out.println(get.getResponseBodyAsString());
		Cookie[] cookies = client.getState().getCookies();
		if (cookies.length == 0) {
			System.out.println("None");
		} else {
			for (int i = 0; i < cookies.length; i++) {
				System.out.println(cookies[i].toString());
			}
		}
		get.releaseConnection();
		// 访问bbs页面
		// 查看签到状态
//		Cookie[] cookies1={new Cookie("sid=C65507027981B4392F5031E26B4968711668751405912592")};
		GetMethod get1 = new GetMethod(composeUrl(HttpType.HTTP,LOGON_SITE,composeUri("/bbs/bbsSign.do?method=getSignById")));
//		get1.setRequestHeader("Cookie","sid=C65507027981B4392F5031E26B4968711668751405914791");
		client.executeMethod(get1);
		System.out.println("getSignById:"+get1.getStatusLine());
		String content = get1.getResponseBodyAsString();
		System.out.println(content);
		ObjectMapper mapper = new ObjectMapper();
		Map map = mapper.readValue(content, Map.class);
		LinkedHashMap result = (LinkedHashMap) map.get("result");
		// map = mapper.readValue(result, Map.class);
		int signFlag = (Integer) result.get("isSign");
		get1.releaseConnection();
		switch (signFlag) {
		case 0: 
			sign(client);
			break;
		case 1:
			System.out.println();
			break;
		default:
			System.out.println("default");
		}

    }
    
//    public static String composeUrl(String sourceUrl){
//    	String mTime = String.valueOf(System.currentTimeMillis());
//    	return new StringBuffer(sourceUrl).append("&subtime=").append(mTime).toString();
//    }
    
    public static String composeUri(String sourceUrl){
    	String mTime = String.valueOf(System.currentTimeMillis());
    	return new StringBuffer(sourceUrl).append("&subtime=").append(mTime).toString();
    }
    
    /**
     * 签到
     * @param client
     */
    private static void sign(HttpClient client){
        //签到
        GetMethod get2 = new GetMethod(composeUrl(HttpType.HTTP,LOGON_SITE,composeUri("/bbs/bbsSign.do?method=addSign&pic=&content=1")));
        try {
			client.executeMethod(get2);
			System.out.println("addSign:"+get2.getStatusLine());
	        System.out.println(get2.getResponseBodyAsString());
	        get2.releaseConnection();
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
} 