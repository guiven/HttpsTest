package foo;

import java.util.Enumeration;
import java.util.Vector;

public class FlagBean {
	private Vector<FlagListener> repository = new Vector<FlagListener>();//监听自己的监听器队列    
	
	public void addDemoListener(FlagListener fl) {     
        repository.addElement(fl);
	}
	
	public void notifyFlagEvent() {//通知所有的监听器     
        Enumeration<FlagListener> enumSet = repository.elements();     
        while(enumSet.hasMoreElements()) {
        	FlagListener dl = enumSet.nextElement();     
               dl.handleEvent(new DemoEvent(this));     
        }
	}
	
}
