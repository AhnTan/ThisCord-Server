
public class Chatting {
	String user;
	String time;
	String message;
	String room;
	
	public Chatting(String user, String time, String room, String msg) {
		this.user = user;
		this.time = time;
		this.message = room;
		this.room = msg;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	

	
}
