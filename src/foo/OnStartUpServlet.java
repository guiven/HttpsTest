package foo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;

import cn.web.sign.CronLogin;

import foo.utility.Config;


public class OnStartUpServlet {
//	static String urlString;
	public static HashMap<String, String> configMap = new HashMap<String, String>();
	public static void main(String[] args) throws FileNotFoundException, IOException {
		//配置读取
		Config.init();
		
		Timer timer=new Timer();
		String minString = (String)Config.prop.get("frequency");
		configMap.put("urlString", (String)Config.prop.get("urlString"));
		configMap.put("SMTP_HOST_NAME", (String)Config.prop.get("SMTP_HOST_NAME"));
		configMap.put("SMTP_PORT", (String)Config.prop.get("SMTP_PORT"));
		configMap.put("emailMsgTxt", (String)Config.prop.get("emailMsgTxt"));
		configMap.put("emailSubjectTxt", (String)Config.prop.get("emailSubjectTxt"));
		configMap.put("emailType", (String)Config.prop.get("emailType"));
		configMap.put("emailFromAddress", (String)Config.prop.get("emailFromAddress"));
		configMap.put("authName", (String)Config.prop.get("authName"));
		configMap.put("authPwd", (String)Config.prop.get("authPwd"));
		configMap.put("SSL_FACTORY", (String)Config.prop.get("SSL_FACTORY"));
		configMap.put("sendTo", (String)Config.prop.get("sendTo"));
		
		int min = Integer.parseInt(minString);
		timer.schedule(new ProbeTimerTask(),0,min*60*1000);
		timer.schedule(new CronLogin(),0,24*60*60*1000);
	}

}
