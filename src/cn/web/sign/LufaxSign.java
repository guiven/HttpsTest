package cn.web.sign;

import java.io.IOException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;


public class LufaxSign {

    static final String LOGON_SITE = "user.lufax.com";
//    static String POSTURL = HttpType.HTTPS.getValue()+LOGON_SITE+"user/login?returnPostURL=http%3A%2F%2Fwww.lufax.com%2F";
    static String POSTURL = HttpType.HTTPS.getValue()+LOGON_SITE+"/user/login?returnPostURL=http://www.lufax.com/";
//    static String GETSIGNURL = HttpType.HTTP.getValue()+LOGON_SITE+"/bbs/bbsSign.do?method=getSignById";
//    static String SIGNURL = HttpType.HTTP.getValue()+LOGON_SITE+"/bbs/bbsSign.do?method=addSign&pic=&content=1";
    static final int LOGON_PORT = 8080;
    
    enum HttpType{
    	HTTP("http://"),
    	HTTPS("https://");
    	private String value;
    	HttpType(String value){
    		this.value = value;
    	}
    	public String getValue() {
    		return value;
    	}

    	public void setValue(String value) {
    		this.value = value;
    	}

    }
	public static void main(String[] args) {
		HttpClient client = new HttpClient();
		client.setConnectionTimeout(5000);
		client.setTimeout(5000);
//		client.getHostConfiguration().setHost(LOGON_SITE, LOGON_PORT);
		// 模拟登录页面login.jsp->main.jsp
		PostMethod post = new PostMethod(POSTURL);
		System.out.println(POSTURL);
		post.setRequestHeader("ContentType",
			     "application/x-www-form-urlencoded");
		post.setRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36");
		post.setRequestHeader("X-Requested-With", "XMLHttpRequest");
		post.setRequestHeader("Origin", "https://user.lufax.com");
//		post.setRequestHeader("Host", "user.lufax.com");
		post.setRequestHeader("Referer", "https://user.lufax.com/user/login?returnPostURL=http%3A%2F%2Fwww.lufax.com%2F");
		NameValuePair pass = new NameValuePair("password", "A644F39CAB8BB23E04B666808626B2594E204B2B11854D24AEDC8C88C73793995556439F6DF93FDEB9F7D1AAD99B971E4B95F4D6C6473E9DE3759A3633F2CD3E0B85ADE9A643F5E35F5F1AC176BB2E7D45C77124B23D709EF4078C7A176E7F73385ACEB12EE1E81F1206E2CAB81DCDCB8494C1B15343B90C58A725DE56F05A3C");
		NameValuePair name = new NameValuePair("userNameLogin", "guiven");
		NameValuePair pwd = new NameValuePair("pwd", "************");
//		NameValuePair valid = new NameValuePair("validNum","1234");
//		post.setRequestBody(new NameValuePair[] { name, pass,pwd,valid });
		post.setRequestBody(new NameValuePair[] { name, pass,pwd });
//		System.out.println(post.getRequestEntity().toString());
		try {
			int status = client.executeMethod(post);
			System.out.println(status);
			Header[] headers = post.getResponseHeaders();
			for(Header header:headers){
				System.out.println(header.getName()+" : "+header.getValue());
			}
			System.out.println(post.getResponseBodyAsString());
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			post.releaseConnection();
		}
	}

}
