
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;


public class Server extends JFrame{
	private JPanel contentPane;
	final static int Port = 30000;
	private ServerSocket socket;
	private JButton start;
	static protected JTextArea textArea;
	private Socket soc;
	private Vector<User_Info> vc = new Vector<User_Info>(); // 사용자 소켓 정보
	private Vector<User> uv = new Vector<User>(); // 사용자 를 담은 벡터
	
	public static void main(String[] args) {
		Server frame = new Server();
		frame.setVisible(true);
	}
	
	
	public Server() {
		init();
	}
	private void init() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(600,300,400,400);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5,5,5,5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane js = new JScrollPane();
		contentPane.add(js);
		js.setBounds(0,0,382,254);
		
		textArea = new JTextArea();
		textArea.setColumns(20);
		textArea.setRows(5);
		js.setViewportView(textArea);
		textArea.setEditable(false);
		
		
		start = new JButton("Server Start");
		start.setBounds(70, 270, 240, 30);
		contentPane.add(start);
		Myaction action= new Myaction();
		start.addActionListener(action);
		
		User u = new User("ahntan","1234");
		uv.add(u);
		User u1 = new User("shbaek","1234");
		uv.add(u1);
		User u2 = new User("한경동","1234");
		uv.add(u2);
		User u3 = new User("송정은","1234");
		uv.add(u3);
		User u4 = new User("안형우","1234");
		uv.add(u4);
	}
	
	class Myaction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			server_start();
		}
		
	}
	private void server_start() {
		try {
			socket = new ServerSocket(Port);
			start.setEnabled(false);
			start.setText("Server started..");
			
			if(socket != null) {
				
				Connection();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			textArea.append("Socket is already used");
		}
	}
	
	private void Connection() {
		Thread th = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				while(true) {
					textArea.append("waiting Client..\n");
					try {
						System.out.println("server");
						soc = socket.accept();
						System.out.println("server");
						textArea.setText("Client join !!\n");
						
						User_Info user = new User_Info(soc,vc,uv);
						
						vc.add(user);
						user.start();
						
						
					} catch (IOException e) {
						textArea.append("Accept Error!!\n");
					}
				}
				
			}
			
		});
		th.start();
	}
}

