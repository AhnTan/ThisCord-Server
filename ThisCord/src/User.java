

// ����� ���� Ŭ����
public class User { 
	String id;
	String pwd;
	String ip;
	String port;
	public void setIP(String ip) {
		this.ip = ip;
	}
	public String getIP() {
		return ip;
	}
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
