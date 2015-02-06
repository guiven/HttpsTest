package foo.utility;

import cn.web.constants.HttpType;

public class URLOperation {
	public static String generalComposeUrl(HttpType http, String LOGON_SITE,String specialUri){
    	return new StringBuffer(http.getValue()).append(LOGON_SITE).append(specialUri).toString();
    }
	
    public static String generalComposeUri(String... sourceUrl){
//    	long mTime = System.currentTimeMillis();
//    	long mTime = 1421510445690L;
    	StringBuffer tmp = new StringBuffer();
    	if(sourceUrl[0].indexOf("?")>-1)
    		;
    	else
    		sourceUrl[0]=sourceUrl[0].concat("?");
    	for(String param:sourceUrl){
    		tmp.append("&").append(param);
    	}
    	return tmp.substring(1);
    }
}
