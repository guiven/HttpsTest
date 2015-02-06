package foo.test;

import foo.DemoEvent;
import foo.FlagBean;
import foo.FlagListener;
import foo.FlagListenerImpl;

public class TestDemo {   
	   FlagBean ds;   
	   public TestDemo(){
	      try{   
	         ds = new FlagBean();   
	         //将监听器在事件源对象中登记：   
	         FlagListenerImpl listener1 = new FlagListenerImpl();   
	         ds.addDemoListener(listener1);   
	         ds.addDemoListener(new FlagListener() {   
	            public void handleEvent(DemoEvent event) {   
	            System.out.println("Method come from 匿名类...");   
	         }
	        });   
	       ds.notifyFlagEvent();//触发事件、通知监听器   
	     }catch(Exception ex){
	       ex.printStackTrace();
	       } 
	    }   
	  
	    public static void main(String args[]) {   
	           new TestDemo();   
	    }   
	}  
