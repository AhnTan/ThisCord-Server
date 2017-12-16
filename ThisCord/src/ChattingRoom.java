import java.util.Vector;

public class ChattingRoom {
	private String roomName ;
	private Vector<String> users = new Vector<String>();
	private Vector<Chatting> chat ;
	
	public ChattingRoom(String roomName , String user) {
		this.roomName = roomName;
		this.users.add(user);
		this.chat = new Vector<Chatting>();
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
