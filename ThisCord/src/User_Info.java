import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
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
	private String ID = "";
	private String PWD = "";

	public User_Info(Socket soc, Vector<User_Info> vc, Vector<User> uv) {
		this.user_socket = soc;
		this.user_vc = vc;
		this.user = uv;
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

			Server.textArea.append(ID + "\n");
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
		} catch (IOException e) {
			System.out.print("msg send Error \n");
		}

	}

	int CheckValid(String id, String pwd) {

		String user_id = "";
		for (int i = 0; i < user_vc.size(); i++) {
			user_id = user_vc.get(i).ID;
			if (user_id.equals(id)) {
				return 4;
			}
		}
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
				msg = msg.trim();
				String[] msg1 = msg.split(" ");
				String cmd = msg1[1];
				if (cmd.equals("refresh") || cmd.equals("create")) {
					MsgControll(msg1);
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

	private void MsgControll(String[] cmd) { // 사용자 전송 "refresh" ,"create"
		String sendMsg = "#" + cmd[1] + " ";
		if (cmd[1].equals("refresh")) {
			for (int i = 0; i < user_vc.size(); i++) {
				sendMsg += user_vc.get(i).ID + " ";
			}
			
			
		} else if (cmd[1].equals("create")) {
	
			sendMsg += cmd[2]; // 방이름
			
			System.out.println(sendMsg + " 보낸다");
		}
		send_Message(sendMsg);

	}

	private void InMessage(String msg) {
		// TODO Auto-generated method stub
		String[] inMsg = msg.split(" ");

		for (int i = 0; i < inMsg.length; i++) {
			System.out.println(inMsg[i]);
		}
		

	}

}
