import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;

public class User_Info extends Thread{

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
			byte[] b = new byte[1024];
			dis.read(b);
			
			String IDPW = new String(b);
			String[] ID_info= IDPW.split(" ");
			this.ID = ID_info[0].trim();
			this.PWD = ID_info[1].trim();
			
			
			
			Server.textArea.append(ID+"\n");
			int chk = CheckValid(ID,PWD);
			if(chk==1) {
				//�� ���� ���
				String user_names = "#OK ";
				for(int i=0; i< user_vc.size(); i++) {
					user_names  += user_vc.get(i).ID +" ";
				}
				send_Message(user_names);
			//	dos.writeUTF(str);;
			}
			else if (chk==2) {
				send_Message("#PNOK "); // ��й�ȣ Ʋ�����
			}
			else
				send_Message("#NOK ");
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void send_Message(String string) {
		
		try {
			byte[] bb ;
			bb = string.getBytes();
			dos.write(bb);
		} catch (IOException e) {
			System.out.print("msg send Error \n");
		}
		
	}

	int CheckValid(String id, String pwd) {
	
		for(int i=0; i<user.size(); i ++) {
			if(user.get(i).getId().equals(id)) {
				if(user.get(i).getPwd().equals(pwd)) {
					return 1; // �� ���� ���
				}
				else
					return 2;// ��й�ȣ�� Ʋ�����
			}
		}
		return 0; // ���̵� ���� ���
	}
	public void run() // ������ ����
	{

		while (true) {
			try {
				
				// ����ڿ��� �޴� �޼���
				byte[] b = new byte[128];
				dis.read(b);
				String msg = new String(b);
				msg = msg.trim();
				InMessage(msg);
				System.out.println(msg);
			} 
			catch (IOException e) 
			{
				
				try {
					dos.close();
					dis.close();
					user_socket.close();
					user_vc.removeElement( this ); // �������� ���� ��ü�� ���Ϳ��� �����
					
					
					break;
				
				} catch (Exception ee) {
				
				}// catch�� ��
			}// �ٱ� catch����

		}
		
		
		
	}// run�޼ҵ� ��

	private void InMessage(String msg) {
		// TODO Auto-generated method stub
		if(msg.equals("1")) {
			for(int i =0; i < user_vc.size(); i++) {
				System.out.println(user_vc.get(i).ID);
			}
		}
	}

}
