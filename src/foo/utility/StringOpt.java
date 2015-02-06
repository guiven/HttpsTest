package foo.utility;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class StringOpt {
	
	final static Calendar calendar = new GregorianCalendar();
	/**
	 * 字符串连接
	 * @param names
	 * @return
	 */
	public static String concat(String...names){
		StringBuilder rt = new StringBuilder("");
		for(int i=0;i<names.length;i++){
			rt.append(names[i]);
		}
		return rt.toString();
	}
	
	/**
	 * 获取<B>当月</B>年月字符串<br>
	 * 格式为“XXXX-XX”
	 */
	public static String getCurrYYMM(){
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
		return concat(year,"-",month);
	}
	
	/**
	 * 获取<B>上月</B>年月字符串<br>
	 * 格式为“XXXX-XX”
	 */
	public static String getPrevYYMM(){
		String year, month;
		if(calendar.get(Calendar.MONTH)==Calendar.JANUARY){
			year = String.valueOf(calendar.get(Calendar.YEAR)-1);
			month = "12";
		}
		else{
			year = String.valueOf(calendar.get(Calendar.YEAR));
			month = String.valueOf(calendar.get(Calendar.MONTH));
		}
		return concat(year,"-",month);
	}
	
	
	/**
	 * 获取<B>下月</B>年月字符串<br>
	 * 格式为“XXXX-XX”
	 */
	public static String getNextYYMM(){
		String year, month;
		if(calendar.get(Calendar.MONTH)==Calendar.DECEMBER){
			year = String.valueOf(calendar.get(Calendar.YEAR)+1);
			month = "1";
		}else{
			year = String.valueOf(calendar.get(Calendar.YEAR));
			month = String.valueOf(calendar.get(Calendar.MONTH)+2);
		}
		return concat(year,"-",month);
	}
	
    // Empty checks
    //-----------------------------------------------------------------------
    /**
     * <p>Checks if a String is empty ("") or null.</p>
     *
     * <pre>
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty("")        = true
     * StringUtils.isEmpty(" ")       = false
     * StringUtils.isEmpty("bob")     = false
     * StringUtils.isEmpty("  bob  ") = false
     * </pre>
     *
     * <p>NOTE: This method changed in Lang version 2.0.
     * It no longer trims the String.
     * That functionality is available in isBlank().</p>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if the String is empty or null
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
    
    /**
     * <p>Checks if a String is whitespace, empty ("") or null.</p>
     *
     * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if the String is null, empty or whitespace
     * @since 2.0
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

}
