package foo;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProbeTimerTask extends TimerTask{
	
//	FlagBean ds;
//	{
//		ds = new FlagBean();
//        //将监听器在事件源对象中登记：   
//        FlagListenerImpl listener1 = new FlagListenerImpl();   
//        ds.addDemoListener(listener1); 
//	}
	private static final Logger logger = LoggerFactory.getLogger(ProbeTimerTask.class);

	@Override
	public void run() {
//        String urlString = OnStartUpServlet.urlString;
		String urlString = OnStartUpServlet.configMap.get("urlString");
        if(urlString==null)
        	urlString = "https://116.228.208.250/index.php";
        
        String output = new String(HttpsUtil.getMethod(urlString));
        if(output.length()>0){
//        	System.out.println("链接正常");
        	logger.debug("链接正常");
        	if(true!=SingletonFlag.getInstance()){
//        		System.out.println("原状态是断开，现为连接");
        		logger.info("原状态是断开，现为连接");
        		SingletonFlag.setFlag(true);
        		SingletonFlag.getFlagBean().notifyFlagEvent();//触发事件、通知监听器  
        	}
        }
	}

}
