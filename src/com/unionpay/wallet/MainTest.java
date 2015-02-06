package com.unionpay.wallet;

public class MainTest {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		String[] focusCities = {"上海市","盐城市","连云港市"};
		GetCityList.getCityList();
//		for(int i=0;i<focusCities.length;i++){
//			if()
//		}
		Long start = System.currentTimeMillis();
		for (String cityCode:GetCityList.CityPair.keySet()) {
			boolean isFocus = false;
			for(int i=0;i<focusCities.length;i++){
				if(GetCityList.CityPair.get(cityCode).trim().equals(focusCities[i].trim())){
					isFocus = true;
					break;
				}
			}
			new GetAvailableCoupons().getAvailableCouponList(cityCode,isFocus);
			GetAvailableCoupons.AvailableCoupon.clear();
			GetAvailableCoupons.WaitingCoupon.clear();
			GetAvailableCoupons.UnhandleCoupon.clear();
		}
		System.out.println("共执行："+(System.currentTimeMillis()-start)+"毫秒");
	}

}
