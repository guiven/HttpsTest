package com.unionpay.wallet;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.codehaus.jackson.map.ObjectMapper;

import cn.web.constants.HttpType;
import cn.web.constants.LoginSite;

import static foo.utility.URLOperation.generalComposeUrl;
import static foo.utility.URLOperation.generalComposeUri;

public class GetCityList {
	public static HashMap<String, String> CityPair = new HashMap<String, String>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			getCityList();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void getCityList() throws InterruptedException{
		try{
	        HttpClient client = new HttpClient();
	        String uri = generalComposeUri("/wm-non-biz-web/restlet/classification/cityList","version=1.0","source=1");
//	        System.out.println(uri);
	        String url = generalComposeUrl(HttpType.HTTP,LoginSite.Unionpay_Wallet,uri);
//	        System.out.println(url);
	        GetMethod get = new GetMethod(url);
	        get.addRequestHeader("Accept", "application/json, text/javascript, */*; q=0.01");
	        get.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.6,en;q=0.4");
	        get.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.91 Safari/537.36");
	        client.executeMethod(get);
//	        System.out.println(get.getResponseBodyAsString());
	        byte[] responseBody = get.getResponseBody();
	        String response = new String(responseBody,Charset.forName("utf-8"));
			System.out.println();
			ObjectMapper mapper = new ObjectMapper();
			Map map = mapper.readValue(response, Map.class);
			if(!((String)map.get("statusCode")).equals("00")){
				System.out.println(map.get("statusCode"));
				System.out.println(map.get("msg"));
				throw new Exception("服务器应答异常，需要重试");
			}
			ArrayList<Map> result = (ArrayList<Map>) map.get("data");
			for(int i=0;i<result.size();i++){
				Map cityInfo = result.get(i);
				String key = (String)cityInfo.get("cityCd");
				String value = (String)cityInfo.get("cityNm");
//				System.out.println(key+":"+value);
				CityPair.put(key, value);
			}
			get.releaseConnection();
		}catch(Exception e){
			System.out.println("执行失败,稍后重试");
//			System.out.println(e);
			e.printStackTrace();
			Thread.sleep(30000);
			main(null);
		}finally{
			System.out.println("城市成功加载条数："+CityPair.size());
		}
	}

}
