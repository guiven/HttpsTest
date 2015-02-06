package foo;

import foo.utility.SendMail;

public class DemoEvent extends java.util.EventObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6896301993420286700L;

	public DemoEvent(Object source) {
		super(source);// source—事件源对象—如在界面上发生的点击按钮事件中的按钮
		// 所有 Event 在构造时都引用了对象 "source"，在逻辑上认为该对象是最初发生有关 Event 的对象
	}

	public void say() {
		System.out.println("This is say method...");
	}
	
	public void sendMail(){
		try {
			new SendMail().note();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
