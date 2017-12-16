import java.util.Vector;

public class RoomManager {
	
	private Vector<ChattingRoom> rooms  ;
	
	public RoomManager() {
		rooms = new Vector<ChattingRoom>();
	}
	
	public void addRoom(ChattingRoom room) {
		rooms.add(room);
	}
	
	public Vector<ChattingRoom> getRoom(){
		return rooms;
	}
}
