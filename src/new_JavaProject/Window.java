package new_JavaProject;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilderFactory;

import static java.lang.System.*;

public class Window {
	Socket socket = new Socket(); // ���� ����
	DataInputStream in; // Server�κ��� �����͸� �о����
	DataInputStream keyboard; // Ű����� �Է��ϴ� �� �о����
	DataOutputStream out; // Server�� �������� ���� ��½�Ʈ��
	private JFrame frame;
	private int mouseX, mouseY;

	static String Server_IP = "172.30.1.41"; //"192.168.1.90"
	static String login_data;
	static String signup_data;
	static String overcheck_data;
	static String IDvalue; // ID
	static String PWvalue; // PW
	static String NIvalue; // NickName
	static String login_possible;
	static String signup_possible;
	static String overcheckNick_possible;
	static String overcheckId_possible;

	// �±� ����

	static String sc = "//success";
	static String f = "//fail";
	static String login = "LOGIN";
	static String signup = "SIGNUP";
	static String overcheck = "OVER";

	//�����̰� �׽�Ʈ�� �κ�
	static String viewrank = "RANK";


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window window = new Window();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Window() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setUndecorated(true);

		// ���� ���� ��ư
		JButton exitButton = new JButton("");
		exitButton.setLocation(1249, 10);
		exitButton.setSize(21, 20);
		exitButton.setIcon(new ImageIcon(".\\images\\exitButton.png"));
		exitButton.setBorderPainted(false);
		exitButton.setContentAreaFilled(false);
		exitButton.setFocusPainted(false);
		exitButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				exitButton.setIcon(new ImageIcon(".\\images\\exitButtonEntered.png"));
				exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));//��ư�� ���콺 �ø��� �հ��� ��� Ŀ���� ����
			}

			@Override
			public void mouseExited(MouseEvent e) {
				exitButton.setIcon(new ImageIcon(".\\images\\exitButton.png"));
				exitButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));		//��ư�� ���콺 ���� ���� ��� Ŀ���� ����
			}

			@Override
			public void mousePressed(MouseEvent e) {

				exitButton.setIcon(new ImageIcon(".\\images\\exitButtonPressed.png"));
			//	exit(0);
			}
			public void mouseReleased(MouseEvent e) {
				//�۵��� ���⼭
				exit(0);
			}
		});
		frame.getContentPane().add(exitButton);


		// ���� �޴���(menuBar)
		JLabel menuBar = new JLabel("");
		menuBar.setIcon(new ImageIcon(".\\images\\menuBar.png"));
		menuBar.setBounds(6, 7, 1269, 26);
		menuBar.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}
		});

		menuBar.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				frame.setLocation(x - mouseX, y - mouseY);
			}
		});
		frame.getContentPane().add(menuBar);


		//���ȭ��
		JLabel background = new JLabel("");
		background.setIcon(new ImageIcon(".\\images\\mainMenuPage.png"));
		background.setBounds(0, 0, 1280, 720);

		frame.getContentPane().add(background,2);

		background.setVisible(false);



		// ���ӽ���ȭ��
		ImagePanel startGamePage = new ImagePanel(
				new ImageIcon(".\\images/startGamePage.png")
						.getImage());
		frame.setSize(startGamePage.getWidth(), startGamePage.getHeight());

		// �α���ȭ��
		ImagePanel loginPage = new ImagePanel(
				new ImageIcon("./images/loginPage.png").getImage());
		frame.setSize(loginPage.getWidth(), loginPage.getHeight());

		frame.getContentPane().add(loginPage);

		//ȸ������ȭ��
		ImagePanel signupPage = new ImagePanel(
				new ImageIcon(".\\images\\signupPage.png").getImage());
		frame.setSize(signupPage.getWidth(), signupPage.getHeight());
		frame.getContentPane().add(signupPage);

		//ȸ������ ȭ�� �ʵ��

		//ȸ������ ID�ʵ�
		JTextField signupPageTxtID = new JTextField();
		signupPageTxtID.setFont(new Font("���� ���", Font.PLAIN,20));
		signupPageTxtID.setBounds(551,327,178,33);
		signupPageTxtID.setBorder(null);
		signupPage.add(signupPageTxtID);

		//ȸ������ ��й�ȣ �ʵ�
		JPasswordField signupPagePass = new JPasswordField();
		signupPagePass.setFont(new Font("���� ���", Font.PLAIN,20));
		signupPagePass.setBounds(551,383,178,33);
		signupPagePass.setBorder(null);
		signupPage.add(signupPagePass);

		//ȸ������ �г��� �ʵ�
		JTextField signupPageNickname = new JTextField();
		signupPageNickname.setFont(new Font("���� ���", Font.PLAIN,20));
		signupPageNickname.setBounds(551,437,178,33);
		signupPageNickname.setBorder(null);
		signupPage.add(signupPageNickname);


		/* ȸ������ ȭ�� ��ư�� */
		// id �ߺ�üũ ��ư
		JButton signupPage_IDovercheckButton = new JButton("");
		signupPage_IDovercheckButton.setIcon(new ImageIcon(".\\images\\loginButton.png"));

		signupPage_IDovercheckButton.setBounds(755, 327, 91, 35);
		signupPage_IDovercheckButton.setBorderPainted(false);
		signupPage_IDovercheckButton.setContentAreaFilled(false);
		signupPage_IDovercheckButton.setFocusPainted(false);
		signupPage_IDovercheckButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {


				signupPage_IDovercheckButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				signupPage_IDovercheckButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {

			}
		});
		signupPage.add(signupPage_IDovercheckButton);
		// nickname �ߺ�üũ ��ư
		JButton signupPage_NicknameovercheckButton = new JButton("");
		signupPage_NicknameovercheckButton.setIcon(new ImageIcon(".\\images\\loginButton.png"));

		signupPage_NicknameovercheckButton.setBounds(755, 327, 91, 35);
		signupPage_NicknameovercheckButton.setBorderPainted(false);
		signupPage_NicknameovercheckButton.setContentAreaFilled(false);
		signupPage_NicknameovercheckButton.setFocusPainted(false);
		signupPage_NicknameovercheckButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				signupPage_NicknameovercheckButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				signupPage_NicknameovercheckButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {


			}
			public void mouseReleased(MouseEvent e) {
				//�۵��� ���⼭
				try	{
					System.out.println(signupPageNickname.getText());
					NIvalue = null;
					NIvalue = signupPageNickname.getText();

					if(IDvalue.equals(null))
						JOptionPane.showMessageDialog(null,"ID�� �Է����ּ���!");
					else {
						socket = new Socket(Server_IP, 5555);
						in = new DataInputStream(socket.getInputStream());
						keyboard = new DataInputStream(socket.getInputStream());
						out = new DataOutputStream(socket.getOutputStream());
						System.out.println(socket.toString());

						overcheck_data = overcheck + "//" + IDvalue; // Server���� "//"�� ���ؼ� ����
						out.writeUTF(overcheck_data);
						JOptionPane.showMessageDialog(null,"��밡��!");
						// id Ȥ�� pw Ȥ�� Nickname�� null ���� �� ȸ������ ��ư ������ �� "������ ���� �Է����ּ���!" ���
					}

				} catch(IOException ex) {

				}

				try {
					overcheckNick_possible = in.readUTF();
					if (overcheckNick_possible.equals(overcheck + sc)) {
						JOptionPane.showMessageDialog(null, "��밡��!");
						signupPage.setVisible(false);
						loginPage.setVisible(true);
					} else {
						JOptionPane.showMessageDialog(null, "��밡��! �ٸ� ID�� �Է����ּ���.");
						signupPage.setVisible(false);
						loginPage.setVisible(true);
					}
				} catch(IOException ex) {

				}

			}
		});
		signupPage.add(signupPage_NicknameovercheckButton);


		//�ڷΰ��� ��ư
		JButton signupPage_BackButton = new JButton("");
		signupPage_BackButton.setIcon(new ImageIcon(".\\images\\BackButton.png"));

		signupPage_BackButton.setBounds(543, 512, 91, 35);
		signupPage_BackButton.setBorderPainted(false);
		signupPage_BackButton.setContentAreaFilled(false);
		signupPage_BackButton.setFocusPainted(false);
		signupPage_BackButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				signupPage_BackButton.setIcon(new ImageIcon(".\\images\\BackButtonEntered.png"));
				signupPage_BackButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				signupPage_BackButton.setIcon(new ImageIcon(".\\images\\BackButton.png"));
				signupPage_BackButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				signupPage_BackButton.setIcon(new ImageIcon(".\\images\\BackButtonPressed.png"));

			}
			public void mouseReleased(MouseEvent e) {
				//�۵��� ���⼭
				signupPage.setVisible(false);
				loginPage.setVisible(true);
			}
		});
		signupPage.add(signupPage_BackButton);

		//ȸ������ ȭ���� ȸ������ ��ư
		JButton signupPage_signupButton = new JButton("");
		signupPage_signupButton.setIcon(new ImageIcon(".\\images\\loginButton.png"));

		signupPage_signupButton.setBounds(647, 512, 91, 35);
		signupPage_signupButton.setBorderPainted(false);
		signupPage_signupButton.setContentAreaFilled(false);
		signupPage_signupButton.setFocusPainted(false);
		signupPage_signupButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				signupPage_signupButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				signupPage_signupButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				try {
					IDvalue = null;
					PWvalue = null;
					NIvalue = null;
					IDvalue = signupPageTxtID.getText();
					PWvalue = signupPagePass.getText();
					NIvalue = signupPageNickname.getText();

					if(IDvalue.equals("") || PWvalue.equals("") ||NIvalue.equals(""))
						JOptionPane.showMessageDialog(null,"������ ���� �Է����ּ���!");
					else {
						socket = new Socket(Server_IP, 5555);
						in = new DataInputStream(socket.getInputStream());
						keyboard = new DataInputStream(socket.getInputStream());
						out = new DataOutputStream(socket.getOutputStream());
						System.out.println(socket.toString());

						signup_data = signup + "//" + NIvalue + "//" + IDvalue + "//" + PWvalue; // Server���� "//"�� ���ؼ� ����
						out.writeUTF(signup_data);
						// id Ȥ�� pw Ȥ�� Nickname�� null ���� �� ȸ������ ��ư ������ �� "������ ���� �Է����ּ���!" ���
					}
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(null,"��� ����!!!");
				}

				try {
					signup_possible = in.readUTF();
					if(signup_possible.equals(signup + sc)) {
						JOptionPane.showMessageDialog(null,"ȸ������ ����! ����ȭ������ ���ư��ϴ�.");
						signupPage.setVisible(false);
						loginPage.setVisible(true);
					}

				} catch(IOException ex) {

				}
			}
		});
		signupPage.add(signupPage_signupButton);




		//�α��� ȭ�� �ʵ��

		//�α��� ȭ�� ID�ʵ�
		JTextField loginPageTxtID = new JTextField();
		loginPageTxtID.setFont(new Font("���� ���", Font.PLAIN,20));
		loginPageTxtID.setBounds(527,537,178,33);
		loginPageTxtID.setBorder(null);
		loginPage.add(loginPageTxtID);

		//�α��� ȭ�� ��й�ȣ �ʵ�
		JPasswordField loginPagePass = new JPasswordField();
		loginPagePass.setFont(new Font("���� ���", Font.PLAIN,20));
		loginPagePass.setBounds(526,583,178,33);
		loginPagePass.setBorder(null);
		loginPage.add(loginPagePass);

		//�α���ȭ�� ��ư��

		//�α��� ��ư

		JButton loginButton = new JButton("");
		loginButton.setIcon(new ImageIcon(".\\images\\loginButton.png"));

		loginButton.setBounds(713, 537, 91, 80);
		loginButton.setBorderPainted(false);
		loginButton.setContentAreaFilled(false);
		loginButton.setFocusPainted(false);
		loginButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				loginButton.setIcon(new ImageIcon(".\\images\\loginButtonEntered.png"));
				loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

			}

			@Override
			public void mouseExited(MouseEvent e) {
				loginButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				loginButton.setIcon(new ImageIcon(".\\images\\loginButton.png"));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				loginButton.setIcon(new ImageIcon(".\\images\\loginButtonPressed.png"));
			}
			//
			public void mouseReleased(MouseEvent e) {
				//�۵��� ���⼭
					// �޼ҵ�� �α��� ��ư�� ������ �� ������ ID�� PW�� ��ȯ
					// ID, PW�� ��ȯ���� ������ ��ġ���� Ȯ���� ���� DB ��ȯ
					// DB�� Ȯ�� �� ������ true��, Ʋ���� false�� ������ ��ȯ
					// ������ true�� ��ȭ������ success��, false�� ��ȯ������ fail�� ��ȯ
					//
					//loginButton.setIcon(new ImageIcon(".\\images\\loginButtonPressed.png"));
					//loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

					//
					try {
						IDvalue = null;
						PWvalue = null;
						IDvalue = loginPageTxtID.getText();
						PWvalue = loginPagePass.getText();
						if(IDvalue.equals(""))
							IDvalue = "null";
						if(PWvalue.equals(""))
							PWvalue = "null";
						if(IDvalue.equals("null") || PWvalue.equals("null"))
							JOptionPane.showMessageDialog(null,"ID Ȥ�� PW�� �Էµ��� �ʾҽ��ϴ�.");
						else {
							socket = new Socket(Server_IP, 5555);
							in = new DataInputStream(socket.getInputStream());
							keyboard = new DataInputStream(socket.getInputStream());
							out = new DataOutputStream(socket.getOutputStream());
							System.out.println(socket.toString());

							login_data = login + "//" + IDvalue + "//" + PWvalue; // Server���� "//"�� ���ؼ� ����
							out.writeUTF(login_data);
						}

					} catch (IOException ex) {
						JOptionPane.showMessageDialog(null,"�ٽ� �Է�!!!!!");
					}

					try {
						login_possible = in.readUTF();
						if(login_possible.equals(login + sc)) {
							loginPage.setVisible(false);
							startGamePage.setVisible(true);
						}
						else
							JOptionPane.showMessageDialog(null,"ID�� PW�� �߸��Ǿ����ϴ�!");
					} catch(IOException ex) {
						//JOptionPane.showMessageDialog(null,"ID�� PW�� �߸��Ǿ����ϴ�!");
					}

				}

		});
		loginPage.add(loginButton);


		//�α��� ȭ���� ȸ������ ��ư
		JButton signupButton = new JButton("");
		signupButton.setBounds(722, 645, 70, 18);
		signupButton.setBorderPainted(false);
		signupButton.setContentAreaFilled(false);
		signupButton.setFocusPainted(false);
		signupButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				signupButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				signupButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				loginPage.setVisible(false);
				signupPage.setVisible(true);

			}
		});
		loginPage.add(signupButton);


		//���̵�/��й�ȣ ã��
		JButton find_ID_Pass_Button = new JButton("");
		find_ID_Pass_Button.setBounds(543, 645, 140, 18);
		find_ID_Pass_Button.setBorderPainted(false);
		find_ID_Pass_Button.setContentAreaFilled(false);
		find_ID_Pass_Button.setFocusPainted(false);
		find_ID_Pass_Button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				find_ID_Pass_Button.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				find_ID_Pass_Button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				JOptionPane.showMessageDialog(null,"ã�� �׽���");

			}
		});
		loginPage.add(find_ID_Pass_Button);

		frame.getContentPane().add(startGamePage);


		// ���Ӽ��� ��ư

		// ��ũ(���Ӽ���)ȭ�� 		//�ؽ�Ʈ�ʵ� �����ŷ� �ϴ� ��ŷ �� �޾ƿ��� �� ����
		ImagePanel rankPage = new ImagePanel(
				new ImageIcon(".\\images\\rankPage.png").getImage());
		frame.setSize(rankPage.getWidth(), rankPage.getHeight());
		frame.getContentPane().add(rankPage);
		/*
		JTextField rank_TextField = new JTextField("");
		rank_TextField.setBounds(100,120,1080,520);

		frame.add(rank_TextField);
		*/
///////////
		// Ʃ�丮��ȭ��
		ImagePanel htpPage = new ImagePanel(
				new ImageIcon(".\\images/htpPage.png").getImage());
		frame.setSize(htpPage.getWidth(), htpPage.getHeight());
		frame.getContentPane().add(htpPage);

		//�̱۰��Ӹ�弱��ȭ��
		ImagePanel singleModeSelectPage = new ImagePanel(new ImageIcon(".\\images/startGamePage.png")
				.getImage());
		frame.setSize(singleModeSelectPage.getWidth(), singleModeSelectPage.getHeight());
		frame.getContentPane().add(singleModeSelectPage);


		//ȭ�� �ʱ�ȭ
		signupPage.setVisible(false);
		htpPage.setVisible(false);
		rankPage.setVisible(false);
		startGamePage.setVisible(false);
		singleModeSelectPage.setVisible(false);
		background.setVisible(false);


		frame.getContentPane().setLayout(null);

		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);


		//���ӽ���ȭ�� ��ư��

		//�̱� ��ư
		JButton startGamePage_singleButton = new JButton("");
		startGamePage_singleButton.setIcon(new ImageIcon(".\\images\\singleButton.png"));

		startGamePage_singleButton.setBorderPainted(false);
		startGamePage_singleButton.setContentAreaFilled(false);
		startGamePage_singleButton.setFocusPainted(false);

		startGamePage_singleButton.setBounds(207, 257, 350, 280);

		startGamePage_singleButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				startGamePage_singleButton.setIcon(new ImageIcon(".\\images\\singleButtonEntered.png"));
				startGamePage_singleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				startGamePage_singleButton.setIcon(new ImageIcon(".\\images\\singleButton.png"));
				startGamePage_singleButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

			}

			@Override
			public void mousePressed(MouseEvent e) {
				startGamePage_singleButton.setIcon(new ImageIcon(".\\images\\singleButtonPressed.png"));

				//startGamePage_singleButton.setIcon(new ImageIcon(".\\images\\singleButtonPressed.png"));

			}
			public void mouseReleased(MouseEvent e) {
				//�۵��� ���⼭
				startGamePage.setVisible(false);
				singleModeSelectPage.setVisible(true);
			}
		});
		startGamePage.add(startGamePage_singleButton);


		//��Ƽ ��ư
		JButton startGamePage_multiButton = new JButton("");
		startGamePage_multiButton.setIcon(new ImageIcon(".\\images\\multiButton.png"));

		startGamePage_multiButton.setBorderPainted(false);
		startGamePage_multiButton.setContentAreaFilled(false);
		startGamePage_multiButton.setFocusPainted(false);

		startGamePage_multiButton.setBounds(729, 258, 350, 280);

		startGamePage_multiButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				startGamePage_multiButton.setIcon(new ImageIcon(".\\images\\multiButtonEntered.png"));
				startGamePage_multiButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				startGamePage_multiButton.setIcon(new ImageIcon(".\\images\\multiButton.png"));
				startGamePage_multiButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

			}

			@Override
			public void mousePressed(MouseEvent e) {
				startGamePage_multiButton.setIcon(new ImageIcon(".\\images\\multiButtonEntered.png"));
				startGamePage.setVisible(false);
				singleModeSelectPage.setVisible(true);
			}
			public void mouseReleased(MouseEvent e) {
				startGamePage.setVisible(false);
				singleModeSelectPage.setVisible(true);
			}
		});
		startGamePage.add(startGamePage_multiButton);


		//��ũ �޴� ��ư
		JButton startGamePage_rankButton = new JButton("");
		startGamePage_rankButton.setBounds(244, 59, 210, 40);
		startGamePage_rankButton.setBorderPainted(false);
		startGamePage_rankButton.setContentAreaFilled(false);
		startGamePage_rankButton.setFocusPainted(false);
		startGamePage_rankButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {
				startGamePage.setVisible(false);
				rankPage.setVisible(true);
			}
		});
		startGamePage.add(startGamePage_rankButton);

		//Ʃ�丮�� �޴� ��ư
		JButton startGamePage_htpButton = new JButton("");
		startGamePage_htpButton.setBounds(466, 59, 210, 40);
		startGamePage_htpButton.setBorderPainted(false);
		startGamePage_htpButton.setContentAreaFilled(false);
		startGamePage_htpButton.setFocusPainted(false);
		startGamePage_htpButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {
				//startGamePage.setVisible(false);
				//htpPage.setVisible(true);
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				startGamePage.setVisible(false);
				htpPage.setVisible(true);
			}

		});
		startGamePage.add(startGamePage_htpButton);

		//�α׾ƿ� ��ư
		JButton startGamePage_logoutButton = new JButton("");
		startGamePage_logoutButton.setIcon(new ImageIcon(".\\images\\logoutButton.png"));
		startGamePage_logoutButton.setBounds(1203, 43, 47, 47);
		startGamePage_logoutButton.setBorderPainted(false);
		startGamePage_logoutButton.setContentAreaFilled(false);
		startGamePage_logoutButton.setFocusPainted(false);
		startGamePage_logoutButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				startGamePage_logoutButton.setIcon(new ImageIcon(".\\images\\logoutButtonEntered.png"));
				startGamePage_logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				startGamePage_logoutButton.setIcon(new ImageIcon(".\\images\\logoutButton.png"));
				startGamePage_logoutButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				startGamePage_logoutButton.setIcon(new ImageIcon(".\\images\\logoutButtonPressed.png"));


			}

			public void mouseReleased(MouseEvent e) {
				startGamePage.setVisible(false);
				loginPage.setVisible(true);
			}
		});
		startGamePage.add(startGamePage_logoutButton);


		//��ũȭ�� ��ư��

		//��ũȭ�� �ؽ�Ʈ�ʵ�
		JTextField rank_TextField = new JTextField("���⿡ DB SELECT * FROM ���� ���ϴ�");
		rank_TextField.setBounds(300,120,520,480);

		rankPage.add(rank_TextField);

		//��ũȭ�� ���ΰ�ħ ��ư  entered ���� �ȸ���
		JButton rank_refreshButton = new JButton("");
		rank_refreshButton.setIcon(new ImageIcon(".\\images\\rankrefreshButton.png"));
		rank_refreshButton.setBounds(50, 600, 47, 47);
		rank_refreshButton.setBorderPainted(false);
		rank_refreshButton.setContentAreaFilled(false);
		rank_refreshButton.setFocusPainted(false);

		rankPage.add(rank_refreshButton);
		rank_refreshButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				rank_refreshButton.setIcon(new ImageIcon(".\\images\\rankrefreshButtonPressed.png")); //entered�� ��ȯ �ʿ�
				rank_refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				rank_refreshButton.setIcon(new ImageIcon(".\\images\\rankrefreshButton.png"));
				rank_refreshButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) { // select ���� �ٽ� �ҷ��´�.
				rank_refreshButton.setIcon(new ImageIcon(".\\images\\rankrefreshButtonPressed.png"));
			}

			public void mouseReleased(MouseEvent e) {
				//�۵��� ���⼭
			}
		});

		//���ӽ��� �޴� ��ư
		JButton rankPage_startGameButton = new JButton("");
		rankPage_startGameButton.setBounds(26, 57, 210, 40);
		rankPage_startGameButton.setBorderPainted(false);
		rankPage_startGameButton.setContentAreaFilled(false);
		rankPage_startGameButton.setFocusPainted(false);
		rankPage_startGameButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {
				rankPage.setVisible(false);
				startGamePage.setVisible(true);
			}
		});
		rankPage.add(rankPage_startGameButton);

		//Ʃ�丮�� �޴� ��ư
		JButton rankPage_htpButton = new JButton("");
		rankPage_htpButton.setBounds(466, 57, 210, 40);
		rankPage_htpButton.setBorderPainted(false);
		rankPage_htpButton.setContentAreaFilled(false);
		rankPage_htpButton.setFocusPainted(false);
		rankPage_htpButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {
				rankPage.setVisible(false);
				htpPage.setVisible(true);
			}
		});
		rankPage.add(rankPage_htpButton);


		//�α׾ƿ� ��ư
		JButton rankpage_logoutButton = new JButton("");
		rankpage_logoutButton.setIcon(new ImageIcon(".\\images\\logoutButton.png"));
		rankpage_logoutButton.setBounds(1203, 43, 47, 47);
		rankpage_logoutButton.setBorderPainted(false);
		rankpage_logoutButton.setContentAreaFilled(false);
		rankpage_logoutButton.setFocusPainted(false);
		rankpage_logoutButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				rankpage_logoutButton.setIcon(new ImageIcon(".\\images\\logoutButtonEntered.png"));
				rankpage_logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				rankpage_logoutButton.setIcon(new ImageIcon(".\\images\\logoutButton.png"));
				rankpage_logoutButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				rankpage_logoutButton.setIcon(new ImageIcon(".\\images\\logoutButtonPressed.png"));


			}

			public void mouseReleased(MouseEvent e) {
				rankPage.setVisible(false);
				loginPage.setVisible(true);
			}
		});
		rankPage.add(rankpage_logoutButton);

		//Ʃ�丮��ȭ�� ��ư��

		//Ʃ�丮�� ȭ�� �̹���
		//ImageIcon img1 = new ImageIcon(".\\images\\htpimage1.png");
		//ImageIcon img2 = new ImageIcon(".\\images\\htpimage2.png");
		JButton htpImageButton = new JButton("");
		htpImageButton.setIcon(new ImageIcon(".\\images\\htipimage1.png"));


		htpImageButton.setBorderPainted(false);
		htpImageButton.setBounds(100,120,1080,520);
		htpImageButton.setPreferredSize(new Dimension(1080,520));

		JButton htp_nextButton = new JButton("");
		htp_nextButton.setIcon(new ImageIcon(".\\images\\nextButton.png")); //�̹����ٲٱ�
		htp_nextButton.setBounds(50, 600, 47, 47);
		htp_nextButton.setBorderPainted(false);
		htp_nextButton.setContentAreaFilled(false);
		htp_nextButton.setFocusPainted(false);

		htp_nextButton.addMouseListener(new MouseAdapter() {

			public void mouseEntered(MouseEvent e) {
				htp_nextButton.setIcon(new ImageIcon(".\\images\\nextButtonEntered.png")); //�̹����ٲٱ�

				htp_nextButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			public void mouseExited(MouseEvent e) {
				htp_nextButton.setIcon(new ImageIcon(".\\images\\nextButton.png")); //�̹����ٲٱ�

				htp_nextButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

			}

			public void mousePressed(MouseEvent e) {
				htp_nextButton.setIcon(new ImageIcon(".\\images\\nextButtonPressed.png")); //�̹����ٲٱ�

			}
			public void mouseReleased(MouseEvent e) {

				htpImageButton.setIcon(new ImageIcon(".\\images\\htipimage2.png"));
			}
		});

		htpPage.add(htp_nextButton);
		htpPage.add(htpImageButton);

		//���ӽ��� �޴� ��ư
		JButton htpPage_startGameButton = new JButton("");
		htpPage_startGameButton.setBounds(29, 59, 210, 40);
		htpPage_startGameButton.setBorderPainted(false);
		htpPage_startGameButton.setContentAreaFilled(false);
		htpPage_startGameButton.setFocusPainted(false);
		htpPage_startGameButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {
				htpPage.setVisible(false);
				startGamePage.setVisible(true);
			}
		});
		htpPage.add(htpPage_startGameButton);

		//��ũ �޴� ��ư
		JButton htpPage_rankButton = new JButton("");
		htpPage_rankButton.setBounds(251, 59, 210, 40);
		htpPage_rankButton.setBorderPainted(false);
		htpPage_rankButton.setContentAreaFilled(false);
		htpPage_rankButton.setFocusPainted(false);
		htpPage_rankButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {
				htpPage.setVisible(false);
				rankPage.setVisible(true);
			}
		});
		htpPage.add(htpPage_rankButton);

		//�α׾ƿ� ��ư
		JButton htpPage_logoutButton = new JButton("");
		htpPage_logoutButton.setIcon(new ImageIcon(".\\images\\logoutButton.png"));
		htpPage_logoutButton.setBounds(1203, 43, 47, 47);
		htpPage_logoutButton.setBorderPainted(false);
		htpPage_logoutButton.setContentAreaFilled(false);
		htpPage_logoutButton.setFocusPainted(false);
		htpPage_logoutButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				htpPage_logoutButton.setIcon(new ImageIcon(".\\images\\logoutButtonEntered.png"));
				htpPage_logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				htpPage_logoutButton.setIcon(new ImageIcon(".\\images\\logoutButton.png"));
				htpPage_logoutButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				htpPage_logoutButton.setIcon(new ImageIcon(".\\images\\logoutButtonPressed.png"));

			}
			public void mouseReleased(MouseEvent e) {
				htpPage.setVisible(false);
				loginPage.setVisible(true);
			}
		});
		htpPage.add(htpPage_logoutButton);



		//�̱ۼ���ȭ�� ����

		//��ũ �޴� ��ư
		JButton singlemodeSelectPage_rankButton = new JButton("");
		singlemodeSelectPage_rankButton.setBounds(244, 59, 210, 40);
		singlemodeSelectPage_rankButton.setBorderPainted(false);
		singlemodeSelectPage_rankButton.setContentAreaFilled(false);
		singlemodeSelectPage_rankButton.setFocusPainted(false);
		singlemodeSelectPage_rankButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {
				singleModeSelectPage.setVisible(false);
				rankPage.setVisible(true);
			}
		});
		singleModeSelectPage.add(singlemodeSelectPage_rankButton);

		//Ʃ�丮�� �޴� ��ư

		JButton singleModeSelectPage_htpButton = new JButton("");
		singleModeSelectPage_htpButton.setBounds(466, 57, 210, 40);
		singleModeSelectPage_htpButton.setBorderPainted(false);
		singleModeSelectPage_htpButton.setContentAreaFilled(false);
		singleModeSelectPage_htpButton.setFocusPainted(false);
		singleModeSelectPage_htpButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {
				singleModeSelectPage.setVisible(false);
				htpPage.setVisible(true);
			}
		});
		singleModeSelectPage.add(singleModeSelectPage_htpButton);

		//�α׾ƿ� ��ư
		JButton singleModeSelectPage_logoutButton = new JButton("");
		singleModeSelectPage_logoutButton.setIcon(new ImageIcon(".\\images\\logoutButton.png"));
		singleModeSelectPage_logoutButton.setBounds(1203, 43, 47, 47);
		singleModeSelectPage_logoutButton.setBorderPainted(false);
		singleModeSelectPage_logoutButton.setContentAreaFilled(false);
		singleModeSelectPage_logoutButton.setFocusPainted(false);
		singleModeSelectPage_logoutButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				singleModeSelectPage_logoutButton.setIcon(new ImageIcon(".\\images\\logoutButtonEntered.png"));
				htpPage_logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				singleModeSelectPage_logoutButton.setIcon(new ImageIcon(".\\images\\logoutButton.png"));
				htpPage_logoutButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				singleModeSelectPage_logoutButton.setIcon(new ImageIcon(".\\images\\logoutButtonPressed.png"));

			}
			public void mouseReleased(MouseEvent e) {
				singleModeSelectPage.setVisible(false);
				loginPage.setVisible(true);
			}

		});
		singleModeSelectPage.add(singleModeSelectPage_logoutButton);

		//�ʱ� ��� ��ư
		JButton singleModeSelectPage_beginnerButton = new JButton("");
		singleModeSelectPage_beginnerButton.setIcon(new ImageIcon(".\\images\\beginner_Button.png"));

		singleModeSelectPage_beginnerButton.setBorderPainted(false);
		singleModeSelectPage_beginnerButton.setContentAreaFilled(false);
		singleModeSelectPage_beginnerButton.setFocusPainted(false);

		singleModeSelectPage_beginnerButton.setBounds(89, 301, 200, 200);

		singleModeSelectPage_beginnerButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				singleModeSelectPage_beginnerButton.setIcon(new ImageIcon(".\\images\\beginner_ButtonEntered.png"));
				singleModeSelectPage_beginnerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

			}

			@Override
			public void mouseExited(MouseEvent e) {
				singleModeSelectPage_beginnerButton.setIcon(new ImageIcon(".\\images\\beginner_Button.png"));
				singleModeSelectPage_beginnerButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				singleModeSelectPage_beginnerButton.setIcon(new ImageIcon(".\\images\\beginner_ButtonPressed.png"));


			}

			public void mouseReleased(MouseEvent e) {
				singleModeSelectPage.setVisible(false);
				background.setVisible(true);
				beginner_gameStart();
			}
		});
		singleModeSelectPage.add(singleModeSelectPage_beginnerButton);

		//�߱� ��� ��ư
		JButton singleModeSelectPage_intermediateButton = new JButton("");
		singleModeSelectPage_intermediateButton.setIcon(new ImageIcon(".\\images\\intermediate_Button.png"));

		singleModeSelectPage_intermediateButton.setBorderPainted(false);
		singleModeSelectPage_intermediateButton.setContentAreaFilled(false);
		singleModeSelectPage_intermediateButton.setFocusPainted(false);

		singleModeSelectPage_intermediateButton.setBounds(390, 301, 200, 200);

		singleModeSelectPage_intermediateButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				singleModeSelectPage_intermediateButton.setIcon(new ImageIcon(".\\images\\intermediate_ButtonEntered.png"));
				singleModeSelectPage_intermediateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				singleModeSelectPage_intermediateButton.setIcon(new ImageIcon(".\\images\\intermediate_Button.png"));
				singleModeSelectPage_intermediateButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				singleModeSelectPage_intermediateButton.setIcon(new ImageIcon(".\\images\\intermediate_ButtonPressed.png"));

			}
			public void mouseReleased(MouseEvent e) {
				//�۵��� ���⼭
			}
		});
		singleModeSelectPage.add(singleModeSelectPage_intermediateButton);

		//��� ��� ��ư
		JButton singleModeSelectPage_advancedButton = new JButton("");
		singleModeSelectPage_advancedButton.setIcon(new ImageIcon(".\\images\\advanced_Button.png"));

		singleModeSelectPage_advancedButton.setBorderPainted(false);
		singleModeSelectPage_advancedButton.setContentAreaFilled(false);
		singleModeSelectPage_advancedButton.setFocusPainted(false);

		singleModeSelectPage_advancedButton.setBounds(691, 301, 200, 200);

		singleModeSelectPage_advancedButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				singleModeSelectPage_advancedButton.setIcon(new ImageIcon(".\\images\\advanced_ButtonEntered.png"));
				singleModeSelectPage_advancedButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				singleModeSelectPage_advancedButton.setIcon(new ImageIcon(".\\images\\advanced_Button.png"));
				singleModeSelectPage_advancedButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

			}

			@Override
			public void mousePressed(MouseEvent e) {
				singleModeSelectPage_advancedButton.setIcon(new ImageIcon(".\\images\\advanced_ButtonPressed.png"));

			}
			public void mouseReleased(MouseEvent e) {
				//�۵��� ���⼭
			}
		});

		singleModeSelectPage.add(singleModeSelectPage_advancedButton);

		//���� ��� ��ư
		JButton singleModeSelectPage_challengeButton = new JButton("");
		singleModeSelectPage_challengeButton.setIcon(new ImageIcon(".\\images\\challenge_Button.png"));

		singleModeSelectPage_challengeButton.setBorderPainted(false);
		singleModeSelectPage_challengeButton.setContentAreaFilled(false);
		singleModeSelectPage_challengeButton.setFocusPainted(false);

		singleModeSelectPage_challengeButton.setBounds(992, 301, 200, 200);

		singleModeSelectPage_challengeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				singleModeSelectPage_challengeButton.setIcon(new ImageIcon(".\\images\\challenge_ButtonEntered.png"));
				singleModeSelectPage_challengeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				singleModeSelectPage_challengeButton.setIcon(new ImageIcon(".\\images\\challenge_Button.png"));
				singleModeSelectPage_challengeButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

				@Override
			public void mousePressed(MouseEvent e) {
					singleModeSelectPage_challengeButton.setIcon(new ImageIcon(".\\images\\challenge_ButtonPressed.png"));

				}
			public void mouseReleased(MouseEvent e) {
				//�۵��� ���⼭

			}
		});
		singleModeSelectPage.add(singleModeSelectPage_challengeButton);

		//�ڷΰ��� ��ư
		JButton singleModeSelectPage_backButton = new JButton("");
		singleModeSelectPage_backButton.setIcon(new ImageIcon(".\\images\\backButton.png"));
		singleModeSelectPage_backButton.setBounds(56, 598, 66, 66);
		singleModeSelectPage_backButton.setBorderPainted(false);
		singleModeSelectPage_backButton.setContentAreaFilled(false);
		singleModeSelectPage_backButton.setFocusPainted(false);
		singleModeSelectPage_backButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				singleModeSelectPage_backButton.setIcon(new ImageIcon(".\\images\\backButtonEntered.png"));
				singleModeSelectPage_backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				singleModeSelectPage_backButton.setIcon(new ImageIcon(".\\images\\backButton.png"));
				singleModeSelectPage_backButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				singleModeSelectPage_backButton.setIcon(new ImageIcon(".\\images\\backButtonPressed.png"));

			//	singleModeSelectPage.setVisible(false);
				//startGamePage.setVisible(true);
			}
			public void mouseReleased(MouseEvent e) {
				//�۵��� ���⼭
				singleModeSelectPage.setVisible(false);
				startGamePage.setVisible(true);
			}
		});

		singleModeSelectPage.add(singleModeSelectPage_backButton);



	}



	//public void gameStart(int nowSelected, String mode) {
	public void beginner_gameStart() {

//			isMainScreen = false;
//			singleButton.setVisible(false);
//			multipleButton.setVisible(false);
//			background = new ImageIcon(Main.class.getResource("../images/gameBackground.png")).getImage();

		int col = 9;
		int row = 9;
		int mine = 10;
		//�ʱ� : 9x9|10
		//�߱� : 16x16|40
		//��� : 30x16|99
		//���̵����� ����ü�� Ŭ������ [����x����|���ڼ�] ������ ����
//			private final int BOARD_WIDTH = N_COLS * CELL_SIZE + 1;//������ ����
//		    private final int BOARD_HEIGHT = N_ROWS * CELL_SIZE + 1;//
//			int size =
//			InGame IG = new InGame(new JLabel(""));
		InGame IG = new InGame(col,row,mine,50);//���ΰ���,���ΰ���,���ڼ�,��ĭ�� ũ��
//			IG.setLocation(100, 100);
		IG.setBounds(410, 150,IG.getWidth(), IG.getHeight());
		frame.add(IG,1);
//			InGame IG2 = new InGame(col,row,mine);
//			IG2.setBounds(100, 400,IG2.getWidth(), IG2.getHeight());
//			frame.add(IG2);
//			InGame IG3 = new InGame(col,row,mine);
//			IG3.setBounds(700, 100,IG3.getWidth(), IG3.getHeight());
//			frame.add(IG3);
//			InGame IG4 = new InGame(col,row,mine);
//			IG4.setBounds(700, 400,IG4.getWidth(), IG4.getHeight());
//			frame.add(IG4);
	}
}







class ImagePanel extends JPanel {
	private Image img;

	public ImagePanel(Image img) {
		this.img = img;
		setSize(new Dimension(img.getWidth(null), img.getHeight(null)));
		setPreferredSize(new Dimension(img.getWidth(null), img.getHeight(null)));
		setLayout(null);
	}

	public int getWidth() {
		return img.getWidth(null);
	}

	public int getHeight() {
		return img.getHeight(null);
	}

	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, null);
	}
}
