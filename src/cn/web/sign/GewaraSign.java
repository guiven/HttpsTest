package cn.web.sign;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DecompressingHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.iteye.pic.OCR;

//import org.apache.commons.httpclient.Header;
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.HttpException;
//import org.apache.commons.httpclient.NameValuePair;
//import org.apache.commons.httpclient.methods.PostMethod;



public class GewaraSign {

    static final String LOGON_SITE = "www.gewara.com";
//    static String POSTURL = HttpType.HTTP.getValue()+LOGON_SITE+"/ajax/common/asynchLogin.dhtml";
//    static String GETURL = HttpType.HTTP.getValue()+LOGON_SITE+"/login.xhtml";
    static long timemill = System.currentTimeMillis();
    static String LOGINURL = HttpType.HTTP.getValue()+LOGON_SITE+"/login.xhtml";
    static String GETURL = HttpType.HTTP.getValue()+LOGON_SITE+"/getCaptchaId.xhtml?r=";
    static String GET1URL = HttpType.HTTPS.getValue()+LOGON_SITE+"/captcha.xhtml?";
    static String POSTURL = HttpType.HTTP.getValue()+LOGON_SITE+"/cas/check_user.xhtml";
    static String CAPTCHA = "";
    static String CAPTCHAID = "";
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
	public static void main(String[] args) throws InterruptedException {
		String html = null;
//		HttpClient client = new HttpClient();
		DefaultHttpClient httpClientOld = new DefaultHttpClient();// 创建httpClient对象
		
		
		
	  try {
		Parser parser = new Parser(GETURL);
		NodeFilter filter = new HasAttributeFilter( "id", "myLoginCaptcha" );
		NodeList nodes = parser.extractAllNodesThatMatch(filter);
		System.out.println(nodes.toHtml());
	} catch (ParserException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
		//识别验证码
		try {
			GETURL = GETURL+getSystemTime("update");
			URL geturl = new URL(GETURL);
			URI geturi = new URI(geturl.getProtocol(), geturl.getHost(),
					geturl.getPath(), geturl.getQuery(), null);// 防止pageUrl中出现空格等特殊字符导致请求失败
			HttpGet httpget = new HttpGet(geturi);// 以get体式格式请求该URL
			HttpResponse getresponse = httpClientOld.execute(httpget);// 获得response对象
			HttpEntity getentity = getresponse.getEntity();
			if (getentity != null) {
				html = EntityUtils.toString(getentity, ContentType
						.getOrDefault(getentity).getCharset().toString());// 对返回的html代码进行解析转码
			}
			System.out.println(html);
			html = html.substring(html.indexOf("{"));
			ObjectMapper mapper = new ObjectMapper();
			Map map = mapper.readValue(html, Map.class);
			String result = (String) map.get("retval");
			System.out.println(result);
			CAPTCHAID = result;
			while(true){
			GET1URL = GET1URL+"captchaId="+result+"&r="+getSystemTime("update");
			downloadImage(GET1URL);
			String res = new OCR().recognizeText(new File("chenjie"+getSystemTime("get") + ".png"), "png");
//			if((res=res.trim()).length()==4){
//			Pattern pattern = new Pattern("[a-zA-Z0-9]{4}$");
			if((res=res.trim()).matches("[a-zA-Z0-9]{4}$")){
				System.out.println(res);
				CAPTCHA=res;
				break;
			}
			else{
				System.out.println(res);
				continue;
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		DecompressingHttpClient httpClient = new DecompressingHttpClient(httpClientOld);//避免返回compressed格式的Entity出现乱码
		//debug:LSMBDPZq45bXXCuq596fc525&r=1407305789800
//		CAPTCHAID="La4BKwhXCN9JVFrL86170261";
//		CAPTCHA="d7pl";
//		Thread.sleep(2000);
		  try {
				URL url = new URL(POSTURL);
				URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(),
						url.getQuery(), null);// 防止pageUrl中出现空格等特殊字符导致请求失败
				HttpPost httppost = new HttpPost(uri);// 以get体式格式请求该URL
//				 httpClient.getParams().setParameter(, proxy);
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();  
				nvps.add(new BasicNameValuePair("TARGETURL", ""));
		        nvps.add(new BasicNameValuePair("j_username", "guiven.ch@gmail.com"));  
		        nvps.add(new BasicNameValuePair("j_password", "890121"));  
		        nvps.add(new BasicNameValuePair("captchaId", CAPTCHAID));
		        nvps.add(new BasicNameValuePair("captcha", CAPTCHA));
		        httppost.setEntity(new UrlEncodedFormEntity(nvps));  
		        System.out.println(EntityUtils.toString(httppost.getEntity()));
				HttpResponse response = httpClient.execute(httppost);// 获得response对象
				int resStatu = response.getStatusLine().getStatusCode();// 返回码
				if (resStatu == HttpStatus.SC_MOVED_TEMPORARILY) {// 200正常 其它就不合错误
					for(Header header : response.getAllHeaders()){
						System.out.println(header.getName()+" : "+header.getValue());
					}
					HttpEntity entity = response.getEntity(); // 获得响应实体

					if (entity != null) {
						html = EntityUtils.toString(entity, ContentType
								.getOrDefault(entity).getCharset().toString());// 对返回的html代码进行解析转码
					}
				} else {
					System.out.println(resStatu);
					System.out.println("状态返回异常");
				}
			} catch (Exception e) {
				// log.error(e.toString());
				System.out.println(e.toString());
			} finally {
				httpClient.getConnectionManager().shutdown();
			}
			// return html;
			System.out.println(html);  
	}
	
	
    private static void downloadImage() throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
        for (int i = 0; i < 30; i++) {
            String url = "https://www.gewara.com/captcha.xhtml";
            HttpGet getMethod = new HttpGet(url);
            try {
                HttpResponse response = httpClient.execute(getMethod, new BasicHttpContext());
                HttpEntity entity = response.getEntity();
                InputStream instream = entity.getContent(); 
                OutputStream outstream = new FileOutputStream(new File("chenjie"+i + ".png"));
                int l = -1;
                byte[] tmp = new byte[2048]; 
                while ((l = instream.read(tmp)) != -1) {
                    outstream.write(tmp);
                } 
                outstream.close();
            } finally {
                getMethod.releaseConnection();
            }
        }

        System.out.println("下载验证码完毕！");
    }
    
    private static void downloadImage(String url) throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
//            String url = "https://www.gewara.com/captcha.xhtml";
            HttpGet getMethod = new HttpGet(url);
            try {
                HttpResponse response = httpClient.execute(getMethod, new BasicHttpContext());
                HttpEntity entity = response.getEntity();
                InputStream instream = entity.getContent(); 
                OutputStream outstream = new FileOutputStream(new File("chenjie"+getSystemTime("get") + ".png"));
                int l = -1;
                byte[] tmp = new byte[2048]; 
                while ((l = instream.read(tmp)) != -1) {
                    outstream.write(tmp);
                } 
                outstream.close();
            } finally {
                getMethod.releaseConnection();
            }

        System.out.println("chenjie"+getSystemTime("get") + ".png"+"下载验证码完毕！");
    }
    
    private static long getSystemTime(String method){
    	if(method.equals("get"))
    		return timemill;
    	else if(method.equals("update"))
    		return timemill=System.currentTimeMillis();
    	else
    		return 0l;
    }

}
