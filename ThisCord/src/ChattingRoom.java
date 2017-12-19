import java.util.Vector;

public class ChattingRoom {
	private String roomName ;
	private Vector<String> users = new Vector<String>();
	private Vector<Chatting> chat ;
	private String roomNum;
	
	public ChattingRoom(String roomName , String user,String roomNum) {
		this.roomName = roomName;
		this.users.add(user);
		this.roomNum = roomNum;
		this.chat = new Vector<Chatting>();
	}
	
	public String getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}

	public void addUsers(String user) {
		users.add(user);
	}
	public Vector<String> getUsers(){
		return users;
	}
	
	public String getRoomName() {
		return roomName;
	}
	public Vector<Chatting> getChatList(){
		return chat;
	}
}
