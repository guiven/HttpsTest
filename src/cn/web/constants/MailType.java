package cn.web.constants;

import foo.utility.StringOpt;

public enum MailType {
	GMAIL("gmail","鸡妹儿"),
	NETEASE126("netEase126","网易126");
	public String getKey() {
		return Key;
	}
	public String getValue() {
		return Value;
	}
	private String Key;
	private String Value;
	MailType(String key,String value){
		this.Key = key;
		this.Value = value;
	}
    /**
     * 根据值查找enum
     * 
     * @param value
     * @return
     */
    public static MailType getEnumByKey(String key) {
        if (StringOpt.isBlank(key)) {
            return null;
        }
        for (MailType t : MailType.values()) {
            if (t.getKey().equals(key)) {
                return t;
            }
        }
        return null;
    }
}
