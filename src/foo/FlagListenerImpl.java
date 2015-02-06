package foo;

public class FlagListenerImpl implements FlagListener {

	@Override
	public void handleEvent(DemoEvent de) {
//        System.out.println("Inside listener1...");     
//        de.say();//回调   
        de.sendMail();
	}

}
