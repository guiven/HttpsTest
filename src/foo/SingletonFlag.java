package foo;

public class SingletonFlag {
	private static boolean flag = false;
	private SingletonFlag(){};
	
	private static FlagBean ds;
	
	static public FlagBean getFlagBean(){
		if(ds==null){
			ds = new FlagBean();
	        //将监听器在事件源对象中登记：   
	        FlagListenerImpl listener1 = new FlagListenerImpl();   
	        ds.addDemoListener(listener1); 
		}
		return ds;
	}
	
	static public boolean getInstance(){
		return flag;
	}
	public boolean isFlag() {
		return flag;
	}
	public static void setFlag(boolean flag1) {
		flag = flag1;
	}
}
