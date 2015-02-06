package com.unionpay.wallet;

import static foo.utility.URLOperation.generalComposeUri;
import static foo.utility.URLOperation.generalComposeUrl;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.codehaus.jackson.map.ObjectMapper;

import cn.web.constants.HttpType;
import cn.web.constants.LoginSite;

public class GetAvailableCoupons {

	public static HashMap<String, String> AvailableCoupon = new HashMap<String, String>();
	public static HashMap<String, String> WaitingCoupon = new HashMap<String, String>();
	public static HashMap<String, String> UnhandleCoupon = new HashMap<String, String>();
	public static SimpleDateFormat tf=new SimpleDateFormat("HHmmss");  
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new GetAvailableCoupons().getAvailableCouponList(args[0],Boolean.parseBoolean(args[1]));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void getAvailableCouponList(String cityCode,boolean isFocus) throws InterruptedException{
		try{
			//打印城市名
			if(isFocus){
				System.out.println(GetCityList.CityPair.get(cityCode));
			}
	        HttpClient client = new HttpClient();
	        String uri = generalComposeUri("/wm-non-biz-web/restlet/seckill/seckillCouponList","cityCd="+cityCode,"currentPage=1","pageSize=50","version=1.0","source=1");
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
//			System.out.println();
			ObjectMapper mapper = new ObjectMapper();
			Map map = mapper.readValue(response, Map.class);
			if(!((String)map.get("statusCode")).equals("00")){
				System.out.println(map.get("statusCode"));
				System.out.println(map.get("msg"));
				throw new Exception("服务器应答异常，需要重试");
			}
			ArrayList<Map> result = (ArrayList<Map>) map.get("data");
			Date date=new Date();
			SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");
			String day=df.format(date);
			String time=tf.format(date);
//			System.out.println(day);
			for(int i=0;i<result.size();i++){
				Map couponInfo = result.get(i);
				String dlDt = (String)couponInfo.get("downloadBeginDt");
				String seckillTm = (String)couponInfo.get("seckillBeginTm");
				String couponId = (String)couponInfo.get("couponId");
				if(day.compareTo(dlDt)>0){
					int leftNum = Integer.parseInt((String)couponInfo.get("leftNum"));
					if(leftNum>0){
						AvailableCoupon.put(couponId, (String)couponInfo.get("couponNm"));
					}
				}else if(day.compareTo(dlDt)<0){
					WaitingCoupon.put(couponId, (String)couponInfo.get("couponNm")
							+"("+dlDt+"-"+seckillTm+"开抢)");
				}else{
					if(time.compareTo(seckillTm)>=0)
						AvailableCoupon.put(couponId, (String)couponInfo.get("couponNm"));
					else
						WaitingCoupon.put(couponId, (String)couponInfo.get("couponNm")
								+"("+dlDt+"-"+seckillTm+"开抢)");
					UnhandleCoupon.put(couponId, (String)couponInfo.get("couponNm"));
				}
//				String value = (String)cityInfo.get("cityNm");
//				System.out.println(cityCode+":"+value);
//				CityPair.put(key, value);
			}
			if(isFocus){
				System.out.print("已开抢超值精选：");
				System.out.println(convertCollction2String(AvailableCoupon.values()));
				System.out.print("待开抢超值精选：");
				System.out.println(convertCollction2String(WaitingCoupon.values()));
				System.out.print("今日抢超值精选：");
				System.out.println(convertCollction2String(UnhandleCoupon.values()));
			}
			get.releaseConnection();
		}catch(Exception e){
			System.out.println("执行失败,稍后重试");
//			System.out.println(e);
			e.printStackTrace();
			Thread.sleep(30000);
			main(new String[]{cityCode,String.valueOf(isFocus)});
		}finally{
			/*if(AvailableCoupon.size()>0)
				System.out.println("AvailableCoupon:"+AvailableCoupon.size());
			if(WaitingCoupon.size()>0)
				System.out.println("WaitingCoupon:"+WaitingCoupon.size());
			if(UnhandleCoupon.size()>0)
				System.out.println("UnhandleCoupon:"+UnhandleCoupon.size());*/
		}
	}
	
	private String convertCollction2String(Collection<String> collection){
		Iterator<String> i = collection.iterator();
        if (!i.hasNext())
            return "[]";
 
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (;;) {
            String e = i.next();
            sb.append(e);
            if (!i.hasNext())
                return sb.append(']').toString();
            sb.append(", ");
        }
	}
}
