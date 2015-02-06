package cn.web.sign;

import cn.web.constants.HttpType;

public class WebTemplete {
//	protected static String LOGON_SITE;
	
	/**
	 * 
	 * @param http
	 * @param specialUri
	 * @return
	 */
	protected static String composeUrl(HttpType http, String LOGON_SITE,String specialUri){
    	return new StringBuffer(http.getValue()).append(LOGON_SITE).append(specialUri).toString();
    }
}
