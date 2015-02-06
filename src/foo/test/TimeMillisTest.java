package foo.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeMillisTest {

	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
/*		SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd");
//		String dateStr = dateformat.format(new Date());
		String dateStr = "2015-01-18";
		System.out.println(dateStr);
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateformat.parse(dateStr));
		long millis = cal.getTimeInMillis();
		System.out.println(millis);*/
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(1422594573838L);
		System.out.println(instance);
	}

}
