package cn.web.sign;

import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.NoHttpResponseException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.codehaus.jackson.map.ObjectMapper;

import cn.web.constants.HttpType;
/**
 * @author chenjie2
 */
public class TounaSignPost extends WebTemplete{
    static final String LOGON_SITE = "www.touna.cn";
//    static String GETURL = HttpType.HTTPS.getValue()+LOGON_SITE+"/auth.do?method=login&username=guiven&md5Pwd=f72c4a466ce21375799079e57d874c76&valicode=";
//    static String GETSIGNURL = HttpType.HTTP.getValue()+LOGON_SITE+"/bbs/bbsSign.do?method=getSignById";
//    static String SIGNURL = HttpType.HTTP.getValue()+LOGON_SITE+"/bbs/bbsSign.do?method=addSign&pic=&content=1";
    static final int LOGON_PORT = 8080;
    static final int SUCCESS_CODE = 200;
    
    public static void main(String[] args) throws Exception{
    	try{
        HttpClient client = new HttpClient();
        // 判断登陆状态
        GetMethod get = new GetMethod(composeUrl(HttpType.HTTPS,LOGON_SITE,composeUri("/auth.do?method=isLogin")));
        get.addRequestHeader("Accept", "application/json, text/javascript, */*; q=0.01");
//        get.addRequestHeader("Accept-Encoding", "gzip, deflate, sdch");
        get.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.6,en;q=0.4");
        get.addRequestHeader("Connection", "keep-alive");
        get.addRequestHeader("Cache-Control", "max-age=0");
//        get.addRequestHeader("Host", LOGON_SITE);
//        get.addRequestHeader("Content-Type", "application/json; charset=UTF-8");
        get.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.91 Safari/537.36");
        get.addRequestHeader("X-Requested-With","XMLHttpRequest");
		client.executeMethod(get);
//		System.out.println("isLogin:"+get.getStatusLine());
//		System.out.println(get.getResponseCharSet());
//		for(Header head:get.getResponseHeaders()){
//			System.out.println(head.getName()+":"+head.getValue());
//		}
//		if(get.getResponseHeader("Content-Encoding").getValue().toLowerCase().equals("gzip")){
//			response.setEntity(new GzipDecompressingEntity(response.getEntity())); 
//		}
//		System.out.println(get.getResponseBodyAsString());
		byte[] responseBody = get.getResponseBody();
//		System.out.println(new String(responseBody,Charset.forName("utf-8")));
/*		Cookie[] cookies = client.getState().getCookies();
		if (cookies.length == 0) {
			System.out.println("None");
		} else {
			for (int i = 0; i < cookies.length; i++) {
				System.out.println(cookies[i].toString());
			}
		}*/
		if(get.getStatusCode()!=SUCCESS_CODE){
			System.out.println(get.getStatusCode());
			throw new Exception("通信失败，需要重试");
		}
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = mapper.readValue(new String(responseBody,Charset.forName("utf-8")), Map.class);
		if((Integer)map.get("status")!=SUCCESS_CODE){
			System.out.println(map.get("status"));
			throw new Exception("服务器应答异常，需要重试");
		}
		map = null;
		mapper = null;
		get.releaseConnection();
		// 访问登陆页面
		PostMethod post = new PostMethod(composeUrl(HttpType.HTTPS,LOGON_SITE,"/auth.do"));
		NameValuePair[] data = {
	               new NameValuePair("method", "login"),
	               new NameValuePair("username", "guiven"),
	               new NameValuePair("md5Pwd", ""),
	               new NameValuePair("valicode", ""),
	               new NameValuePair("subtime", String.valueOf(System.currentTimeMillis())) };
		post.setRequestBody(data);
		post.addRequestHeader("Connection", "keep-alive");
		post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		post.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.91 Safari/537.36");
		try{
			client.executeMethod(post);
		}catch(NoHttpResponseException ne){
			Thread.sleep(10000);
			client.executeMethod(post);
		}catch(ConnectTimeoutException ce){
			Thread.sleep(10000);
			client.executeMethod(post);
		}
//		System.out.println("login:"+post.getStatusLine());
//		System.out.println(post.getResponseBodyAsString());
//		Cookie[] cookies = client.getState().getCookies();
//		if (cookies.length == 0) {
//			System.out.println("None");
//		} else {
//			for (int i = 0; i < cookies.length; i++) {
//				System.out.println(cookies[i].toString());
//			}
//		}
		if(post.getStatusCode()!=SUCCESS_CODE){
			System.out.println(post.getStatusCode());
			throw new Exception("通信失败，需要重试");
		}
		mapper = new ObjectMapper();
		map = mapper.readValue(post.getResponseBodyAsString(), Map.class);
		if((Integer)map.get("status")!=SUCCESS_CODE){
			System.out.println(map.get("status"));
			throw new Exception("服务器应答异常，需要重试");
		}
		map = null;
		mapper = null;
		post.releaseConnection();
		
		// 访问bbs页面
		// 查看签到状态
		GetMethod get1 = new GetMethod(composeUrl(HttpType.HTTP,LOGON_SITE,composeUri("/bbs/bbsSign.do?method=getSignById")));
		get1.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.91 Safari/537.36");
		try{
			client.executeMethod(get1);
		}catch(NoHttpResponseException ne){
			Thread.sleep(10000);
			client.executeMethod(get1);
		}catch(ConnectTimeoutException ce){
			Thread.sleep(10000);
			client.executeMethod(get1);
		}
//		System.out.println("getSignById:"+get1.getStatusLine());
		if(get1.getStatusCode()!=SUCCESS_CODE){
			System.out.println(get1.getStatusCode());
			throw new Exception("通信失败，需要重试");
		}
		mapper = new ObjectMapper();
		map = mapper.readValue(get1.getResponseBodyAsString(), Map.class);
		if((Integer)map.get("status")!=SUCCESS_CODE){
			System.out.println(map.get("status"));
			throw new Exception("服务器应答异常，需要重试");
		}
		LinkedHashMap<String, Object> result = (LinkedHashMap<String, Object>) map.get("result");
		int signFlag = (Integer) result.get("isSign");
		map = null;
		mapper = null;
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
		
		//抽奖
//		doAward(client);
    	}catch(Exception e){
    		System.out.println("执行失败,稍后重试");
    		System.out.println(e);
    		Thread.sleep(10000);
    		main(null);
    	}
    }
    
//    public static String composeUrl(String sourceUrl){
//    	String mTime = String.valueOf(System.currentTimeMillis());
//    	return new StringBuffer(sourceUrl).append("&subtime=").append(mTime).toString();
//    }
    
    public static String composeUri(String... sourceUrl){
    	long mTime = System.currentTimeMillis();
//    	long mTime = 1421510445690L;
    	StringBuffer tmp = new StringBuffer();
    	for(String param:sourceUrl){
    		tmp.append(param);
    	}
    	return tmp.append("&subtime=").append(mTime).append("&_=").append(mTime+1).toString();
    }
    
    /**
     * 签到
     * @param client
     */
    private static void sign(HttpClient client) throws Exception{
        //签到
        GetMethod get2 = new GetMethod(composeUrl(HttpType.HTTP,LOGON_SITE,composeUri("/bbs/bbsSign.do?method=addSign&pic=&content=1")));
			client.executeMethod(get2);
//			System.out.println("addSign:"+get2.getStatusLine());
//	        System.out.println(get2.getResponseBodyAsString());
			if(get2.getStatusCode()!=SUCCESS_CODE){
				System.out.println(get2.getStatusCode());
				throw new Exception("通信失败，需要重试");
			}
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> map = mapper.readValue(get2.getResponseBodyAsString(), Map.class);
			if((Integer)map.get("status")!=SUCCESS_CODE){
				System.out.println(map.get("status"));
				throw new Exception("服务器应答异常，需要重试");
			}
			map = null;
			mapper = null;
	        get2.releaseConnection();
    }
    
    /**
     * 抽奖
     * @param client
     */
    private static void doAward(HttpClient client) throws Exception{
		GetMethod get2 = new GetMethod(composeUrl(HttpType.HTTP,LOGON_SITE,composeUri("/coupon.do?method=doAward")));
		get2.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.91 Safari/537.36");
			client.executeMethod(get2);
//			System.out.println("doAward:"+get2.getStatusLine());
//			System.out.println(get2.getResponseBodyAsString());
			if(get2.getStatusCode()!=SUCCESS_CODE){
				System.out.println(get2.getStatusCode());
				throw new Exception("通信失败，需要重试");
			}
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> map = mapper.readValue(get2.getResponseBodyAsString(), Map.class);
			if((Integer)map.get("status")!=SUCCESS_CODE){
				System.out.println(map.get("status"));
				throw new Exception("服务器应答异常，需要重试");
			}
			map = null;
			mapper = null;
	        get2.releaseConnection();
    }
} 