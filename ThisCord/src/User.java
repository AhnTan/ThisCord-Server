

// 사용자 정보 클래스
public class User { 
	String id;
	String pwd;
	String port;
	public String getId() {
		return id;
	}
	public String getPwd() {
		return pwd;
	}
	public String getPort() {
		return port;
	}
	public User(String id, String pwd,String port) {
		this.id = id;
		this.pwd = pwd;
		this.port = port;
	}

}
