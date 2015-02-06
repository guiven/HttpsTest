package cn.web.sign;

import java.util.TimerTask;

public class CronLogin extends TimerTask {

	static final String LOGON_SITE = "www.touna.cn";
	@Override
	public void run() {

		try {
			TounaSignPost.main(null);
		} catch (Exception e) {
			System.out.println("投哪儿签到抽奖失败！");
			e.printStackTrace();
		}

	}

}
