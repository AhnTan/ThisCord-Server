import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

public class User_Info extends Thread {

	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;
	private Socket user_socket;
	private Vector<User_Info> user_vc;
	private Vector<User> user;
	private RoomManager rm;
	private String ID = "";
	private String PWD = "";


	private int flag = 0; // 첫날방 플래그

	public User_Info(Socket soc, Vector<User_Info> vc, Vector<User> uv, RoomManager rm) {
		this.user_socket = soc;
		this.user_vc = vc;
		this.user = uv;
		this.rm = rm;
		user_init();
	}

	public void user_init() {
		try {
			is = user_socket.getInputStream();
			dis = new DataInputStream(is);
			os = user_socket.getOutputStream();
			dos = new DataOutputStream(os);
			/*
			 * byte[] b = new byte[1024]; dis.read(b);
			 */

			// String IDPW = new String(b);
			String IDPW = dis.readUTF();
			String[] ID_info = IDPW.split(" ");
			this.ID = ID_info[0].trim();
			this.PWD = ID_info[1].trim();
			for(int i=0 ; i <user.size(); i++) {
				if(this.ID.equals(user.get(i).id)) {
					user.get(i).setIP(ID_info[2].trim()); // ip 를 set 해준다 
				}
			}
			// 임의의 방
			/*
			 * if (flag == 0) { ChattingRoom newRoom = new ChattingRoom("첫날방", this.ID);
			 * rm.addRoom(newRoom); } flag++;
			 */
			Server.textArea.append(ID + " Login ! \n");
			int chk = CheckValid(ID, PWD);
			if (chk == 1) {
				// 다 맞은 경우
				String user_names = "#OK ";
				for (int i = 0; i < user_vc.size(); i++) {
					user_names += user_vc.get(i).ID + " ";
				}
				send_Message(user_names);
				// dos.writeUTF(str);;
			} else if (chk == 2) {
				send_Message("#PNOK "); // 비밀번호 틀린경우
			} else if (chk == 4) {
				send_Message("#USED ");
			} else
				send_Message("#NOK ");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void send_Message(String string) {

		try {
			// byte[] bb ;
			// bb = string.getBytes();
			dos.writeUTF(string);
			System.out.println(string+" 보내짐");
		} catch (IOException e) {
			System.out.print("msg send Error \n");
		}

	}

	private void send_BroadMsg(String string, ChattingRoom index) {
		Vector<String> name = index.getUsers();

		System.out.println(string);
		for (int j = 0; j < name.size(); j++) {
			for (int i = 0; i < user_vc.size(); i++) {
				if(name.get(j).equals(user_vc.get(i).ID)) { //접속해 있는 유저 아이디와 비교하여 같은 유저에게 보낸다.
					DataOutputStream dos = user_vc.get(i).dos;
					try {
						System.out.println("브로드캐스팅"+string);
						dos.writeUTF(string);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println("msg sendTo Error \n");
						e.printStackTrace();
					}
				}
			}
		}
	
		
		
		
	}

	int CheckValid(String id, String pwd) {

		String user_id = "";
		/*
		 * for (int i = 0; i < user_vc.size(); i++) { user_id = user_vc.get(i).ID; if
		 * (user_id.equals(id)) { return 4; } }
		 */
		for (int i = 0; i < user.size(); i++) {
			if (user.get(i).getId().equals(id)) {
				if (user.get(i).getPwd().equals(pwd)) {
					return 1; // 다 맞은 경우
				} else
					return 2;// 비밀번호는 틀린경우
			}
		}
		return 0; // 아이디가 없는 경우
	}

	public void run() // 스레드 정의
	{

		while (true) {
			try {

				// 사용자에게 받는 메세지
				/*
				 * byte[] b = new byte[128]; dis.read(b); String msg = new String(b);
				 */
				String msg = dis.readUTF();
				String msg2 = msg;
				msg = msg.trim();
				System.out.println("첫번째 "+msg);
				String[] msg1 = msg.split(" ");
				String cmd = msg1[1];
				if (cmd.equals("refresh") || cmd.equals("create") || cmd.equals("getroom") ||cmd.equals("getchat")) {
					MsgControll(msg1);
				}
				else if(cmd.equals("destroy")) {
					String str = "destroy";
					send_Message(str);
				}
				else if (cmd.equals("invite")) {
					InviteUser(msg1);
				} 
				else if (cmd.equals("message")) {
					BroadCastMsg(msg);
					
				}
				InMessage(msg);
				System.out.println(msg);
			} catch (IOException e) {

				try {
					dos.close();
					dis.close();
					user_socket.close();
					user_vc.removeElement(this); // 에러가난 현재 객체를 벡터에서 지운다

					break;

				} catch (Exception ee) {

				} // catch문 끝
			} // 바깥 catch문끝

		}

	}// run메소드 끝

	private void BroadCastMsg(String msg) {
		String[] msg1 = msg.split("%3"); //msg1[1] 은 메세지 
		String[] cmd = msg1[0].split(" ");
		//msg1[0] = 사람,커맨드,시간,방이름,msg1[1] = 메세지
		//cmd[0] = 보낸사람 , cmd[1] = 커맨드 , cmd[2] = 시간 , cmd[3] = 방이름  
		//String name,String cmd,String time,String msg
		String sendMsg = "#" + cmd[1] + "%3";
		Vector<ChattingRoom> cr = rm.getRoom();
		ChattingRoom check = null;
		for (int i = 0; i < cr.size(); i++) {
			if (cmd[3].equals(cr.get(i).getRoomName())) {
				check = cr.get(i);
			}
		}
		//cmd2[0] 은 방이름 cmd2[1] 은 채팅 메세지
		//check 는 해당 채팅방
		try {
		sendMsg += cmd[0] + "%3" +cmd[2] + "%3" + cmd[3] +"%3"+msg1[1];
		}catch(Exception e) {
			return;
		}
		String saveMsg = cmd[0]+"%3"+cmd[2]+"%3"+msg1[1]+"%3"+cmd[3];
		save_Message(saveMsg,check);
		System.out.println(sendMsg);
		send_BroadMsg(sendMsg, check);

	}

	private void save_Message(String sendMsg, ChattingRoom check) {
		String[] msg = sendMsg.split("%3");
		/*if(msg[3].equals("null")) {
			return;
		}*/
		Vector<Chatting> chatlist = check.getChatList();
		Chatting chat = new Chatting(msg[0],msg[1],msg[2],msg[3]);
		chatlist.add(chat);
		
	}

	// 방에 초대하는 메소드
	private void InviteUser(String[] cmd) {

		// int checkIDX = -1;
		Vector<ChattingRoom> cr = rm.getRoom();

		// 해당 방을 찾는다 그 방에 초대할 유저를 더해준다.
		for (int i = 0; i < cr.size(); i++) {
			if (cmd[2].equals(cr.get(i).getRoomName())) {
				try {
					cr.get(i).addUsers(cmd[3]);
				}catch(Exception e) {
					return;
				}
			}
		}

		/*
		 * for(int i =0;i<user_vc.size(); i++) { if(cmd[3].equals(user_vc.get(i).ID)){
		 * checkIDX = i; } }
		 */

		// sendMsg+= cmd[0] +" "+ cmd[2]; // 초대자 + 방이름

		// send_MessageTo(sendMsg,user_vc.get(checkIDX).dos);
		// System.out.println(sendMsg);
	}

	// 메세지 명령 처리
	private void MsgControll(String[] cmd) { // 사용자 전송 "refresh" ,"create"
		String sendMsg = "#" + cmd[1] + " "; // cmd[1] 은 명령어
		if (cmd[1].equals("refresh")) {
			for (int i = 0; i < user_vc.size(); i++) {
				sendMsg += user_vc.get(i).ID + " ";
			}

		} else if (cmd[1].equals("create")) {
			try {
			sendMsg += cmd[2] ; // 방이름
			}catch(Exception e) {
				return;
			}
			ChattingRoom newRoom = new ChattingRoom(cmd[2], cmd[0],cmd[3]); // 방이름 , 사용자명         바꾼코드
			
			rm.addRoom(newRoom);
			System.out.println(sendMsg + " 보낸다");
			
			
		} else if (cmd[1].equals("getroom")) {
			System.out.println("겟 룸 들어왔다.");

			Vector<ChattingRoom> rv = rm.getRoom();
			for (int i = 0; i < rv.size(); i++) {
				Vector<String> userv = rv.get(i).getUsers(); // 방 안의 유저 벡터
				for (int j = 0; j < userv.size(); j++) { // 유저 수 만큼 돌면서
					if (ID.equals(userv.get(j))) { // 가져온다
						sendMsg += rv.get(i).getRoomName() + " " + rv.get(i).getRoomNum()+" "; // 바꾼 코드
					}
				}
			}
		} else if(cmd[1].equals("getchat")) {
			System.out.println("getchat 들어옴 "+cmd[0]+" "+cmd[1]+" "+cmd[2]+"\n");
			//sendMsg = "#message";
			sendMsg="";
			Vector<ChattingRoom> rv = rm.getRoom();
			System.out.println(sendMsg);
			for(int i =0;i<rv.size();i++) {
				
				if(rv.get(i).getRoomName().equals(cmd[2])) { // 방이름과 같은 방에
					Vector<Chatting> cv=rv.get(i).getChatList(); // 채팅리스트를 가지고 온다
					for(int j=0;j<cv.size(); j++) {
						
						sendMsg ="#message%3"+ cv.get(j).getUser() + "%3" +cv.get(j).getTime() +"%3"+cv.get(j).getRoom()+"%3"+cv.get(j).getMessage();
					    send_Message(sendMsg);
					}
					String sendList="";
					Vector<String> users = rv.get(i).getUsers(); // 방의 유저 목록을 가져온다
					for(int k = 0 ; k < user.size(); k ++) {
						for(int p=0 ; p < users.size(); p++) {
							if(user.get(k).id.equals(users.get(p))) {
								sendList += user.get(k).getId() +"%3" + user.get(k).getIP() +"%3" +user.get(k).getPort()+"%3";
							}
						}
					}
					String sendMsg2 ="#userlist%3"+sendList;
					ChattingRoom check = null;
					Vector<ChattingRoom> cr = rm.getRoom();
					for (int q = 0; q < cr.size(); q++) {
						if (cmd[2].equals(cr.get(q).getRoomName())) {
							check = cr.get(q);
						}
					}
					send_BroadMsg(sendMsg2, check);
				}
			}
			return;
		}

		send_Message(sendMsg);
		System.out.println(sendMsg);

	}

	private void InMessage(String msg) {
		// TODO Auto-generated method stub
		String[] inMsg = msg.split(" ");
		System.out.println("In Message 함수 ");
		for (int i = 0; i < inMsg.length; i++) {
			System.out.println(inMsg[i]);
		}

	}

}
