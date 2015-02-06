package cn.web.constants;

public enum HttpType{
	HTTP("http://"),
	HTTPS("https://");
	private String value;
	HttpType(String value){
		this.value = value;
	}
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
