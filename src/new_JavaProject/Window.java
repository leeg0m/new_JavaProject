package new_JavaProject;

import static java.lang.System.exit;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import new_JavaProject.InGame.Items;

public class Window {
	Socket socket = new Socket(); // ���� ����
	DataInputStream in; // Server�κ��� �����͸� �о����
	DataInputStream keyboard; // Ű����� �Է��ϴ� �� �о����
	DataOutputStream out; // Server�� �������� ���� ��½�Ʈ��
	private JFrame frame;
	private int mouseX, mouseY;

	static String Server_IP = "192.168.219.118";
	static int Server_Port = 5555;
	static String login_data;
	static String signup_data;
	static String overcheck_data;
	static String findcheck_data;
	static String IDvalue; // ID
	static String PWvalue; // PW
	static String NIvalue; // NickName
	static String EMvalue; // email
	static String login_possible;
	static String signup_possible;
	static String overcheckNick_possible;
	static String overcheckId_possible;
	static String findcheck_possible;

	static int IDovercheck_count = 0; // �ߺ�üũ ���� ���� üũ
	static int NIovercheck_count = 0; // �� �� 1 �̻� �� �� ȸ������ ����
	static String mynick;

	// �±� ����

	static String sc = "//success";
	static String f = "//fail";
	static String login = "LOGIN";
	static String signup = "SIGNUP";
	static String overcheck = "OVER";
	static String logout = "LOGOUT";
	static String rank = "RANK";
	static String singlewin = "SINGLEWIN";
	static String multiwin = "MULTIWIN";
	static String easymode = "EASYMODE";
	static String normalmode = "NORMALMODE";
	static String hardmode = "HARDMODE";
	static String find_id = "FINDID";
	static String find_pw = "FINDPW";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws Exception {
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

	ImagePanel easyModePage;
	ImagePanel nomalModePage;
	ImagePanel hardModePage;
	ImagePanel multiModePage;

	ImagePanel signupPage;
	ImagePanel htpPage;
	ImagePanel rankPage;
	ImagePanel startGamePage;
	ImagePanel singleModeSelectPage;
	ImagePanel syModePage;
	ImagePanel background;

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
				exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ��ư�� ���콺 �ø��� �հ��� ��� Ŀ���� ����
			}

			@Override
			public void mouseExited(MouseEvent e) {
				exitButton.setIcon(new ImageIcon(".\\images\\exitButton.png"));
				exitButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ��ư�� ���콺 ���� ���� ��� Ŀ���� ����
			}

			public void mousePressed(MouseEvent e) {
				exitButton.setIcon(new ImageIcon(".\\images\\exitButtonPressed.png"));

			}

			@Override
			public void mouseReleased(MouseEvent e) {

				if (socket.isConnected()) {
					try {

						out.writeUTF(logout + "//" + mynick);
						socket.close();
					} catch (IOException ex) {

					}
				}
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

		// ���ȭ��
		JLabel background = new JLabel("");
		background.setIcon(new ImageIcon(".\\images\\mainMenuPage.png"));
		background.setBounds(0, 0, 1280, 720);

		frame.getContentPane().add(background, 2);

		background.setVisible(false);

		// ���ӽ���ȭ��
		ImagePanel startGamePage = new ImagePanel(new ImageIcon(".\\images/startGamePage.png").getImage());
		frame.setSize(startGamePage.getWidth(), startGamePage.getHeight());

		// �α���ȭ��
		ImagePanel loginPage = new ImagePanel(new ImageIcon("./images/loginPage.png").getImage());
		frame.setSize(loginPage.getWidth(), loginPage.getHeight());

		frame.getContentPane().add(loginPage);

		// ȸ������ȭ��
		ImagePanel signupPage = new ImagePanel(new ImageIcon(".\\images\\signupPage.png").getImage());
		frame.setSize(signupPage.getWidth(), signupPage.getHeight());
		frame.getContentPane().add(signupPage);

		//���̵�_��й�ȣ ã�� ȭ��
		ImagePanel findPage = new ImagePanel(new ImageIcon(".\\images\\findPage.png").getImage());
		frame.setSize(findPage.getWidth(), findPage.getHeight());
		frame.getContentPane().add(findPage);



		//���̵�_��й�ȣ ã�� ȭ�� �ʵ���

		//���̵� ã�� ���̵� �ʵ�
		JTextField findPageTxtID1 = new JTextField();
		findPageTxtID1.setFont(new Font("���� ���", Font.PLAIN, 20));
		findPageTxtID1.setBounds(590, 244, 178, 33);
		findPageTxtID1.setBorder(null);
		signupPage.add(findPageTxtID1);
		findPage.add(findPageTxtID1);

		//��й�ȣ ã�� �̸��� �ʵ�
		JTextField findPageEmail = new JTextField();
		findPageEmail.setFont(new Font("���� ���", Font.PLAIN, 20));
		findPageEmail.setBounds(594, 384, 178, 33);
		findPageEmail.setBorder(null);
		signupPage.add(findPageEmail);
		findPage.add(findPageEmail);

		//��й�ȣ ã�� ���̵� �ʵ�
		JTextField findPageTxtID2 = new JTextField();
		findPageTxtID2.setFont(new Font("���� ���", Font.PLAIN, 20));
		findPageTxtID2.setBounds(595, 436, 178, 33);
		findPageTxtID2.setBorder(null);
		signupPage.add(findPageTxtID2);
		findPage.add(findPageTxtID2);


		//���̵�_��й�ȣ ã�� ��ư��

		//ã��ȭ��_���̵� ã�� ��ư
		JButton findPage_findIdButton = new JButton("");
		findPage_findIdButton.setBounds(675,302,92,19);
		findPage_findIdButton.setBorderPainted(false);
		findPage_findIdButton.setContentAreaFilled(false);
		findPage_findIdButton.setFocusPainted(false);
		findPage_findIdButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				findPage_findIdButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				findPage_findIdButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// �޼ҵ�� �α��� ��ư�� ������ �� ������ ID�� PW�� ��ȯ
				// ID, PW�� ��ȯ���� ������ ��ġ���� Ȯ���� ���� DB ��ȯ
				// DB�� Ȯ�� �� ������ true��, Ʋ���� false�� ������ ��ȯ
				// ������ true�� ��ȭ������ success��, false�� ��ȯ������ fail�� ��ȯ
				//

				try {

					IDvalue = null;
					IDvalue = findPageTxtID1.getText();


					if(IDvalue.equals(""))
						IDvalue = "null";

					if(IDvalue.equals("null"))
						JOptionPane.showMessageDialog(null,"�̸����� �Է����ּ���!");
					else {
						socket = new Socket(Server_IP, Server_Port);
						in = new DataInputStream(socket.getInputStream());
						keyboard = new DataInputStream(socket.getInputStream());
						out = new DataOutputStream(socket.getOutputStream());
						System.out.println(socket.toString());

						findcheck_data = find_id + "//" + IDvalue; // Server���� "//"�� ���ؼ� ����
						out.writeUTF(findcheck_data);
					}

				} catch (IOException ex) {
					JOptionPane.showMessageDialog(null,"���� ������(���� ����)");
				}

				try {
					findcheck_possible = in.readUTF(); //    login//success//mynick
					String s[] = findcheck_possible.split("//");
					if(s[0].equals(find_id) && s[1].equals("success")) {
						String myid = s[2];
						JOptionPane.showMessageDialog(null, "ã���ô� ID�� [" + myid + "]�Դϴ�." );
					}
					else
						JOptionPane.showMessageDialog(null,"�̸����� �߸��Ǿ����ϴ�!");
				} catch(IOException ex) {
				}
			}

		});

		findPage.add(findPage_findIdButton);

		//ã��ȭ��_��й�ȣ ã�� ��ư
		JButton findPage_findPwButton = new JButton("");
		findPage_findPwButton.setBounds(660,490,114,26);
		findPage_findPwButton.setBorderPainted(false);
		findPage_findPwButton.setContentAreaFilled(false);
		findPage_findPwButton.setFocusPainted(false);
		findPage_findPwButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				findPage_findPwButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				findPage_findPwButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// �޼ҵ�� �α��� ��ư�� ������ �� ������ ID�� PW�� ��ȯ
				// ID, PW�� ��ȯ���� ������ ��ġ���� Ȯ���� ���� DB ��ȯ
				// DB�� Ȯ�� �� ������ true��, Ʋ���� false�� ������ ��ȯ
				// ������ true�� ��ȭ������ success��, false�� ��ȯ������ fail�� ��ȯ
				//

				try {

					IDvalue = null;
					EMvalue = null;
					IDvalue = findPageTxtID2.getText();
					EMvalue = findPageEmail.getText();


					if(IDvalue.equals(""))
						IDvalue = "null";
					if(EMvalue.equals(""))
						EMvalue = "null";


					if(IDvalue.equals("null") || EMvalue.equals("null"))
						JOptionPane.showMessageDialog(null,"�̸��� �Ǵ� ���̵� �Է����ּ���!");
					else {
						socket = new Socket(Server_IP, Server_Port);
						in = new DataInputStream(socket.getInputStream());
						keyboard = new DataInputStream(socket.getInputStream());
						out = new DataOutputStream(socket.getOutputStream());
						System.out.println(socket.toString());

						findcheck_data = find_pw + "//" + EMvalue + "//" + IDvalue; // Server���� "//"�� ���ؼ� ����
						out.writeUTF(findcheck_data);
					}

				} catch (IOException ex) {
					JOptionPane.showMessageDialog(null,"���� ������(���� ����)");
				}

				try {
					findcheck_possible = in.readUTF(); //    login//success//mynick
					String s[] = findcheck_possible.split("//");
					if(s[0].equals(find_pw) && s[1].equals("success")) {
						String mypw = s[2];
						JOptionPane.showMessageDialog(null, "ã���ô� ��й�ȣ��  [" + mypw + "]�Դϴ�." );
					}
					else
						JOptionPane.showMessageDialog(null,"�̸��� �Ǵ� ���̵� �߸��Ǿ����ϴ�!");
				} catch(IOException ex) {
				}
			}

		});

		findPage.add(findPage_findPwButton);



		//ã��ȭ��_�ڷΰ��� ��ư
		JButton findPage_BackButton = new JButton("");

		findPage_BackButton.setBounds(702, 561, 68, 18);
		findPage_BackButton.setBorderPainted(false);
		findPage_BackButton.setContentAreaFilled(false);
		findPage_BackButton.setFocusPainted(false);
		findPage_BackButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				findPage_BackButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				findPage_BackButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {


				findPage.setVisible(false);
				loginPage.setVisible(true);

				findPageTxtID1.setText("");
				findPageTxtID2.setText("");
				findPageEmail.setText("");

			}
		});
		findPage.add(findPage_BackButton);


		// ȸ������ ȭ�� �ʵ��

		// ȸ������ ID�ʵ�
		JTextField signupPageTxtID = new JTextField();
		signupPageTxtID.setFont(new Font("���� ���", Font.PLAIN, 20));
		signupPageTxtID.setBounds(551, 270, 178, 33);
		signupPageTxtID.setBorder(null);
		signupPage.add(signupPageTxtID);

		// ȸ������ �г��� �ʵ�
		JTextField signupPageNickname = new JTextField();
		signupPageNickname.setFont(new Font("���� ���", Font.PLAIN, 20));
		signupPageNickname.setBounds(551, 326, 178, 33);
		signupPageNickname.setBorder(null);
		signupPage.add(signupPageNickname);

		// ȸ������ �̸��� �ʵ�
		JTextField signupPageEmail = new JTextField();
		signupPageEmail.setFont(new Font("���� ���", Font.PLAIN, 20));
		signupPageEmail.setBounds(551, 382, 178, 33);
		signupPageEmail.setBorder(null);
		signupPage.add(signupPageEmail);

		// ȸ������ ��й�ȣ �ʵ�
		JPasswordField signupPagePass = new JPasswordField();
		signupPagePass.setFont(new Font("���� ���", Font.PLAIN, 20));
		signupPagePass.setBounds(551, 437, 178, 33);
		signupPagePass.setBorder(null);
		signupPage.add(signupPagePass);

//=============================================================================
		/* ȸ������ ȭ�� ��ư�� */
		// id �ߺ�üũ ��ư
		JButton signupPage_IDovercheckButton = new JButton("");
		signupPage_IDovercheckButton.setIcon(new ImageIcon(".\\images\\overcheckButton.png"));

		signupPage_IDovercheckButton.setBounds(755, 270, 91, 35);
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

				IDvalue = null;
				IDvalue = signupPageTxtID.getText();

				if (IDvalue.equals(""))
					JOptionPane.showMessageDialog(null, "ID�� �Է����ּ���");
				else {
					try {
						socket = new Socket(Server_IP, Server_Port);
						in = new DataInputStream(socket.getInputStream());
						keyboard = new DataInputStream(socket.getInputStream());
						out = new DataOutputStream(socket.getOutputStream());
						System.out.println(socket.toString());

						overcheck_data = overcheck + "//" + "id" + "//" + IDvalue; // Server���� "//"�� ���ؼ� ����
						out.writeUTF(overcheck_data);

					} catch (IOException ex) {

					}

					try {
						overcheckId_possible = in.readUTF();
						// System.out.println(overcheckId_possible);
						if (overcheckId_possible.equals(overcheck + sc)) {
							JOptionPane.showMessageDialog(null, "��밡��!");
							IDovercheck_count++;
						} else
							JOptionPane.showMessageDialog(null, "���Ұ�! �ٸ� ID�� �Է����ּ���.");

					} catch (IOException ex) {

					}
				}
			}
		});
		signupPage.add(signupPage_IDovercheckButton);

		// �г��� �ߺ�üũ ��ư
		JButton signupPage_NicknameovercheckButton = new JButton("");
		signupPage_NicknameovercheckButton.setIcon(new ImageIcon(".\\images\\overcheckButton.png"));

		signupPage_NicknameovercheckButton.setBounds(755, 326, 91, 35);
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

				NIvalue = null;
				NIvalue = signupPageNickname.getText();

				if (NIvalue.equals(""))
					JOptionPane.showMessageDialog(null, "�г����� �Է����ּ���");
				else {
					try {
						socket = new Socket(Server_IP, Server_Port);
						in = new DataInputStream(socket.getInputStream());
						keyboard = new DataInputStream(socket.getInputStream());
						out = new DataOutputStream(socket.getOutputStream());
						System.out.println(socket.toString());

						overcheck_data = overcheck + "//" + "nickname" + "//" + NIvalue; // Server���� "//"�� ���ؼ� ����
						out.writeUTF(overcheck_data);

					} catch (IOException ex) {

					}

					try {
						overcheckNick_possible = in.readUTF();
						// System.out.println(overcheckId_possible);
						if (overcheckNick_possible.equals(overcheck + sc)) {
							JOptionPane.showMessageDialog(null, "��밡��!");
							NIovercheck_count++;
						} else
							JOptionPane.showMessageDialog(null, "���Ұ�! �ٸ� �г����� �Է����ּ���.");

					} catch (IOException ex) {

					}
				}
			}
		});
		signupPage.add(signupPage_NicknameovercheckButton);

		// ȸ������ ȭ���� �ڷΰ��� ��ư
		JButton signupPage_BackButton = new JButton("");
		signupPage_BackButton.setIcon(new ImageIcon(".\\images\\signupBackButton.png"));

		signupPage_BackButton.setBounds(543, 512, 91, 35);
		signupPage_BackButton.setBorderPainted(false);
		signupPage_BackButton.setContentAreaFilled(false);
		signupPage_BackButton.setFocusPainted(false);
		signupPage_BackButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				signupPage_BackButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				signupPage_BackButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				IDovercheck_count = 0; // �ڷΰ��� ��ư ���� �� ���̵�, �г��� ī���� �ʱ�ȭ
				NIovercheck_count = 0;

				signupPage.setVisible(false);
				loginPage.setVisible(true);

				signupPageTxtID.setText("");
				signupPagePass.setText("");
				signupPageEmail.setText("");
				signupPageNickname.setText("");
			}
		});
		signupPage.add(signupPage_BackButton);

		// ȸ������ ȭ���� ȸ������ ��ư
		JButton signupPage_signupButton = new JButton("");
		signupPage_signupButton.setIcon(new ImageIcon(".\\images\\signupButton.png"));

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
				IDvalue = null;
				PWvalue = null;
				NIvalue = null;
				EMvalue = null;
				IDvalue = signupPageTxtID.getText();
				PWvalue = signupPagePass.getText();
				NIvalue = signupPageNickname.getText();
				EMvalue = signupPageEmail.getText();

				if (IDvalue.equals("") || PWvalue.equals("") || NIvalue.equals("") || EMvalue.equals("")) {
					JOptionPane.showMessageDialog(null, "������ ���� ä���ּ���!");
				} else if (IDovercheck_count == 0 || NIovercheck_count == 0) {
					JOptionPane.showMessageDialog(null, "�ߺ�üũ ��ư�� �����ּ���");
				} else {
					try {
						socket = new Socket(Server_IP, Server_Port);
						in = new DataInputStream(socket.getInputStream());
						keyboard = new DataInputStream(socket.getInputStream());
						out = new DataOutputStream(socket.getOutputStream());
						System.out.println(socket.toString());

						signup_data = signup + "//" + NIvalue + "//" + IDvalue + "//" + PWvalue + "//" + EMvalue; // Server����
						// "//"��
						// ���ؼ�
						// ����
						out.writeUTF(signup_data);
						// id Ȥ�� pw Ȥ�� Nickname�� null ���� �� ȸ������ ��ư ������ �� "������ ���� �Է����ּ���!" ���
					} catch (IOException ex) {
						JOptionPane.showMessageDialog(null, "��� ����!!!");
					}

					try {
						signup_possible = in.readUTF();
						if (signup_possible.equals(signup + sc)) {
							JOptionPane.showMessageDialog(null, "ȸ������ ����! ����ȭ������ ���ư��ϴ�.");
							signupPage.setVisible(false);
							loginPage.setVisible(true);
							IDovercheck_count = 0; // ȸ������ ������ ���̵�, �г��� ī���� �ʱ�ȭ
							NIovercheck_count = 0;

							signupPageTxtID.setText("");
							signupPagePass.setText("");
							signupPageNickname.setText("");
							signupPageEmail.setText("");
						}
					} catch (IOException ex) {

					}
				}

			}
		});
		signupPage.add(signupPage_signupButton);

//=========================================================================================
		// �α��� ȭ�� �ʵ��

		// �α��� ȭ�� ID�ʵ�
		JTextField loginPageTxtID = new JTextField();
		JPasswordField loginPagePass = new JPasswordField();
		loginPageTxtID.setFont(new Font("���� ���", Font.PLAIN, 20));
		loginPageTxtID.setBounds(527, 537, 178, 33);
		loginPageTxtID.setBorder(null);
		loginPageTxtID.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.getKeyCode() == 10) {
					loginPagePass.requestFocus();
				}
			}
		});
		loginPage.add(loginPageTxtID);

		// �α��� ȭ�� ��й�ȣ �ʵ�

		loginPagePass.setFont(new Font("���� ���", Font.PLAIN, 20));
		loginPagePass.setBounds(526, 583, 178, 33);
		loginPagePass.setBorder(null);
		loginPagePass.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.getKeyCode() == 10) {
					loginPage.setVisible(false);
					startGamePage.setVisible(true);
				}
			}
		});
		loginPage.add(loginPagePass);


		// ================================================================
		/* �α���ȭ�� ��ư�� */

		// �α��� ��ư

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
				loginButton.setIcon(new ImageIcon(".\\images\\loginButton.png"));
				loginButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

			}

			public void mousePressed(MouseEvent e) {
				loginButton.setIcon(new ImageIcon(".\\images\\loginButton.png"));
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// �޼ҵ�� �α��� ��ư�� ������ �� ������ ID�� PW�� ��ȯ
				// ID, PW�� ��ȯ���� ������ ��ġ���� Ȯ���� ���� DB ��ȯ
				// DB�� Ȯ�� �� ������ true��, Ʋ���� false�� ������ ��ȯ
				// ������ true�� ��ȭ������ success��, false�� ��ȯ������ fail�� ��ȯ
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
						socket = new Socket(Server_IP, Server_Port);
						in = new DataInputStream(socket.getInputStream());
						keyboard = new DataInputStream(socket.getInputStream());
						out = new DataOutputStream(socket.getOutputStream());
						System.out.println(socket.toString());

						login_data = login + "//" + IDvalue + "//" + PWvalue; // Server���� "//"�� ���ؼ� ����
						out.writeUTF(login_data);
					}

				} catch (IOException ex) {
					JOptionPane.showMessageDialog(null,"���� ������(���� ����)");
				}

				try {
					login_possible = in.readUTF(); //    login//success//mynick
					String s[] = login_possible.split("//");
					if(s[0].equals(login) && s[1].equals("success")) {
						mynick = s[2];
						JOptionPane.showMessageDialog(null, mynick + "�� ȯ���մϴ�!");
						loginPage.setVisible(false);
						startGamePage.setVisible(true);
					}
					else
						JOptionPane.showMessageDialog(null,"ID�� PW�� �߸��Ǿ����ϴ�!");
				} catch(IOException ex) {

				}
//				loginPage.setVisible(false);
//				startGamePage.setVisible(true);

			}
		});

		loginPage.add(loginButton);

		// �α��� ȭ���� ȸ������ ��ư
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

		// ���̵�/��й�ȣ ã��
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
				loginPage.setVisible(false);
				findPage.setVisible(true);

			}
		});
		loginPage.add(find_ID_Pass_Button);

		frame.getContentPane().add(startGamePage);

		// =====================================================================
		// ���Ӽ��� ��ư

		// ��ũ(���Ӽ���)ȭ��
		ImagePanel rankPage = new ImagePanel(new ImageIcon(".\\images\\rankPage.png").getImage());
		frame.setSize(rankPage.getWidth(), rankPage.getHeight());
		frame.getContentPane().add(rankPage);

		// Ʃ�丮��ȭ��
		ImagePanel htpPage = new ImagePanel(new ImageIcon(".\\images/htpPage.png").getImage());
		frame.setSize(htpPage.getWidth(), htpPage.getHeight());
		frame.getContentPane().add(htpPage);

		// �̱۰��Ӹ�弱��ȭ��
		ImagePanel singleModeSelectPage = new ImagePanel(new ImageIcon(".\\images/startGamePage.png").getImage());
		frame.setSize(singleModeSelectPage.getWidth(), singleModeSelectPage.getHeight());
		frame.getContentPane().add(singleModeSelectPage);

		// �̱۰���_����_ȭ��
		easyModePage = new ImagePanel(new ImageIcon(".\\images/mainMenuPage.png").getImage());
		frame.setSize(easyModePage.getWidth(), easyModePage.getHeight());
		frame.getContentPane().add(easyModePage);

		nomalModePage = new ImagePanel(new ImageIcon(".\\images/mainMenuPage.png").getImage());
		frame.setSize(nomalModePage.getWidth(), nomalModePage.getHeight());
		frame.getContentPane().add(nomalModePage);

		hardModePage = new ImagePanel(new ImageIcon(".\\images/mainMenuPage.png").getImage());
		frame.setSize(hardModePage.getWidth(), hardModePage.getHeight());
		frame.getContentPane().add(hardModePage);

		// �����ϱ�_ȭ��
		multiModePage = new ImagePanel(new ImageIcon(".\\images/multiPage1.png").getImage());
		frame.setSize(multiModePage.getWidth(), multiModePage.getHeight());
		frame.getContentPane().add(multiModePage);

		// ȭ�� �ʱ�ȭ
		signupPage.setVisible(false);
		htpPage.setVisible(false);
		rankPage.setVisible(false);
		startGamePage.setVisible(false);
		singleModeSelectPage.setVisible(false);
		easyModePage.setVisible(false);
		nomalModePage.setVisible(false);
		hardModePage.setVisible(false);
		background.setVisible(false);
		findPage.setVisible(false);
		multiModePage.setVisible(false);

		frame.getContentPane().setLayout(null);

		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		// �ʱ� ��� ȭ�� ��ư��
		// ============================================================
		JButton easygamebackButton = new JButton("");
		easygamebackButton.setIcon(new ImageIcon(".\\images\\backButton.png"));
		easygamebackButton.setBounds(56, 598, 66, 66);
		easygamebackButton.setBorderPainted(false);
		easygamebackButton.setContentAreaFilled(false);
		easygamebackButton.setFocusPainted(false);
		easygamebackButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				easygamebackButton.setIcon(new ImageIcon(".\\images\\backButtonEntered.png"));
				easygamebackButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			public void mouseExited(MouseEvent e) {
				easygamebackButton.setIcon(new ImageIcon(".\\images\\backButton.png"));
				easygamebackButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

			}

			public void mousePressed(MouseEvent e) {
				easygamebackButton.setIcon(new ImageIcon(".\\images\\backButtonEnteredPressed.png"));
			}

			public void mouseReleased(MouseEvent e) {
				// exit
//				try {
//					IG.exit();
//
//				}catch(Exception ex){
//
//				}
				easyModePage.setVisible(false);
				singleModeSelectPage.setVisible(true);
			}
		});

		easyModePage.add(easygamebackButton);
		// ============================================================
		// �߱� ��� ȭ�� ��ư��
		// ============================================================
		JButton nomalgamebackButton = new JButton("");
		nomalgamebackButton.setIcon(new ImageIcon(".\\images\\backButton.png"));
		nomalgamebackButton.setBounds(56, 598, 66, 66);
		nomalgamebackButton.setBorderPainted(false);
		nomalgamebackButton.setContentAreaFilled(false);
		nomalgamebackButton.setFocusPainted(false);
		nomalgamebackButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				nomalgamebackButton.setIcon(new ImageIcon(".\\images\\backButtonEntered.png"));
				nomalgamebackButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			public void mouseExited(MouseEvent e) {
				nomalgamebackButton.setIcon(new ImageIcon(".\\images\\backButton.png"));
				nomalgamebackButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

			}

			public void mousePressed(MouseEvent e) {
				nomalgamebackButton.setIcon(new ImageIcon(".\\images\\backButtonEnteredPressed.png"));
			}

			public void mouseReleased(MouseEvent e) {
				nomalModePage.setVisible(false);

				singleModeSelectPage.setVisible(true);
			}
		});

		nomalModePage.add(nomalgamebackButton);
//============================================================
		// ��� ��� ȭ�� ��ư��
		// ============================================================
		JButton hardgamebackButton = new JButton("");
		hardgamebackButton.setIcon(new ImageIcon(".\\images\\backButton.png"));
		hardgamebackButton.setBounds(56, 598, 66, 66);
		hardgamebackButton.setBorderPainted(false);
		hardgamebackButton.setContentAreaFilled(false);
		hardgamebackButton.setFocusPainted(false);
		hardgamebackButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				hardgamebackButton.setIcon(new ImageIcon(".\\images\\backButtonEntered.png"));
				hardgamebackButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			public void mouseExited(MouseEvent e) {
				hardgamebackButton.setIcon(new ImageIcon(".\\images\\backButton.png"));
				hardgamebackButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

			}

			public void mousePressed(MouseEvent e) {
				hardgamebackButton.setIcon(new ImageIcon(".\\images\\backButtonEnteredPressed.png"));
			}

			public void mouseReleased(MouseEvent e) {
				hardModePage.setVisible(false);

				singleModeSelectPage.setVisible(true);
			}
		});

		hardModePage.add(hardgamebackButton);
//============================================================

		// ���ӽ���ȭ�� ��ư��

		// �̱� ��ư
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

			public void mousePressed(MouseEvent e) {
				startGamePage_singleButton.setIcon(new ImageIcon(".\\images\\singleButtonPressed.png"));
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				startGamePage.setVisible(false);
				singleModeSelectPage.setVisible(true);
			}
		});
		startGamePage.add(startGamePage_singleButton);

		// ��Ƽ ��ư
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
			}

			@Override
			public void mouseExited(MouseEvent e) {
				startGamePage_multiButton.setIcon(new ImageIcon(".\\images\\multiButton.png"));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				startGamePage_multiButton.setIcon(new ImageIcon(".\\images\\multiButtonPressed.png"));
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// ��Ƽ���� ����
				startGamePage.setVisible(false);
				multiModePage.setVisible(true);
			}
		});
		startGamePage.add(startGamePage_multiButton);

		// ���Ӽ��� �޴� ��ư
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

		// Ʃ�丮�� �޴� ��ư
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
				startGamePage.setVisible(false);
				htpPage.setVisible(true);
			}
		});
		startGamePage.add(startGamePage_htpButton);

		// �α׾ƿ� ��ư
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

			public void mousePressed(MouseEvent e) {
				startGamePage_logoutButton.setIcon(new ImageIcon(".\\images\\logoutButtonPressed.png"));
			}

			@Override
			public void mouseReleased(MouseEvent e) {

				try {
					out.writeUTF(logout + "//" + mynick);
					socket.close();
				} catch (IOException ex) {

				}

				startGamePage.setVisible(false);
				loginPage.setVisible(true);
			}
		});
		startGamePage.add(startGamePage_logoutButton);

		// =======================================================================
		/* ��ũȭ�� ��ư�� */

		// ���ӽ��� �޴� ��ư
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

		// Ʃ�丮�� �޴� ��ư
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

		// ���Ӽ��� ������ ���� �α׾ƿ� ��ư
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

			public void mousePressed(MouseEvent e) {
				rankpage_logoutButton.setIcon(new ImageIcon(".\\images\\logoutButtonPressed.png"));
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				rankPage.setVisible(false);
				loginPage.setVisible(true);
			}
		});
		rankPage.add(rankpage_logoutButton);

		JButton rankrefreshButton = new JButton("");
		rankrefreshButton.setIcon(new ImageIcon(".\\images\\rankrefreshButton.png"));
		rankrefreshButton.setBounds(70, 600, 47, 47);

		rankPage.add(rankrefreshButton);

		// ====================================================================
		// Ʃ�丮��ȭ�� ��ư��

		// Ʃ�丮�� ȭ�� �̹���
		// ImageIcon img1 = new ImageIcon(".\\images\\htpimage1.png");
		// ImageIcon img2 = new ImageIcon(".\\images\\htpimage2.png");
		JButton htpImageButton = new JButton("");
		htpImageButton.setIcon(new ImageIcon(".\\images\\htpimage1.png"));
		htpImageButton.setBorderPainted(false);
		htpImageButton.setBounds(120, 120, 1080, 520);
		htpImageButton.setPreferredSize(new Dimension(1080, 520));

		htpPage.add(htpImageButton);

		// Ʃ�丮�� ȭ�� �ؽ�Ʈ��ư
		JButton htpnextButton = new JButton("");
		htpnextButton.setIcon(new ImageIcon(".\\images\\nextButton.png"));
		htpnextButton.setBounds(50, 600, 66, 66);
		htpnextButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				htpnextButton.setIcon(new ImageIcon(".\\images\\nextButtonPressed.png"));
				htpnextButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				htpImageButton.setIcon(new ImageIcon(".\\images\\htpimage2.png"));
				htpnextButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				htpnextButton.setIcon(new ImageIcon(".\\images\\nextButtonEntered.png"));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				htpnextButton.setIcon(new ImageIcon(".\\images\\nextButton.png"));
			}
		});
		htpPage.add(htpnextButton);

		// ���ӽ��� �޴� ��ư
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

		// ��ũ �޴� ��ư
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

		// �α׾ƿ� ��ư
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

			public void mousePressed(MouseEvent e) {
				htpPage_logoutButton.setIcon(new ImageIcon(".\\images\\logoutButton.png"));
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				htpPage.setVisible(false);
				loginPage.setVisible(true);
			}
		});
		htpPage.add(htpPage_logoutButton);

//========================================
		// �̱ۼ���ȭ�� ����

		// ��ũ �޴� ��ư
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

		// Ʃ�丮�� �޴� ��ư

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

		// �α׾ƿ� ��ư
		JButton singleModeSelectPage_logoutButton = new JButton("");
		singleModeSelectPage_logoutButton.setIcon(new ImageIcon(".\\images\\logoutButton.png"));
		singleModeSelectPage_logoutButton.setBounds(1203, 43, 47, 47);
		singleModeSelectPage_logoutButton.setBorderPainted(false);
		singleModeSelectPage_logoutButton.setContentAreaFilled(false);
		singleModeSelectPage_logoutButton.setFocusPainted(false);
		singleModeSelectPage_logoutButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				htpPage_logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				htpPage_logoutButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				singleModeSelectPage.setVisible(false);
				loginPage.setVisible(true);
			}
		});
		singleModeSelectPage.add(singleModeSelectPage_logoutButton);

		// �ʱ� ��ư
		JButton singleModeSelectPage_easyButton = new JButton("");
		singleModeSelectPage_easyButton.setIcon(new ImageIcon(".\\images\\easy_Button.png"));

		singleModeSelectPage_easyButton.setBorderPainted(false);
		singleModeSelectPage_easyButton.setContentAreaFilled(false);
		singleModeSelectPage_easyButton.setFocusPainted(false);

		singleModeSelectPage_easyButton.setBounds(89, 301, 200, 200);

		singleModeSelectPage_easyButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				singleModeSelectPage_easyButton.setIcon(new ImageIcon(".\\images\\easy_ButtonEntered.png"));
				singleModeSelectPage_easyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				singleModeSelectPage_easyButton.setIcon(new ImageIcon(".\\images\\easy_Button.png"));
				singleModeSelectPage_easyButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			public void mousePressed(MouseEvent e) {
				singleModeSelectPage_easyButton.setIcon(new ImageIcon(".\\images\\easy_ButtonPressed.png"));
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				singleModeSelectPage.setVisible(false);
				easyModePage.setVisible(true);
				easy_gameStart();

			}
		});
		singleModeSelectPage.add(singleModeSelectPage_easyButton);

		// �߱� ��ư
		JButton singleModeSelectPage_normalButton = new JButton("");
		singleModeSelectPage_normalButton.setIcon(new ImageIcon(".\\images\\normal_Button.png"));

		singleModeSelectPage_normalButton.setBorderPainted(false);
		singleModeSelectPage_normalButton.setContentAreaFilled(false);
		singleModeSelectPage_normalButton.setFocusPainted(false);

		singleModeSelectPage_normalButton.setBounds(390, 301, 200, 200);

		singleModeSelectPage_normalButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				singleModeSelectPage_normalButton.setIcon(new ImageIcon(".\\images\\normal_ButtonEntered.png"));
				singleModeSelectPage_normalButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				singleModeSelectPage_normalButton.setIcon(new ImageIcon(".\\images\\normal_Button.png"));
				singleModeSelectPage_normalButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				singleModeSelectPage_normalButton.setIcon(new ImageIcon(".\\images\\normal_ButtonPressed.png"));
			}

			public void mouseReleased(MouseEvent e) {
				singleModeSelectPage.setVisible(false);
				nomalModePage.setVisible(true);
				normal_gameStart();
			}
		});
		singleModeSelectPage.add(singleModeSelectPage_normalButton);

		// ��� ��ư
		JButton singleModeSelectPage_hardButton = new JButton("");
		singleModeSelectPage_hardButton.setIcon(new ImageIcon(".\\images\\hard_Button.png"));

		singleModeSelectPage_hardButton.setBorderPainted(false);
		singleModeSelectPage_hardButton.setContentAreaFilled(false);
		singleModeSelectPage_hardButton.setFocusPainted(false);

		singleModeSelectPage_hardButton.setBounds(691, 301, 200, 200);

		singleModeSelectPage_hardButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				singleModeSelectPage_hardButton.setIcon(new ImageIcon(".\\images\\hard_ButtonEntered.png"));
				singleModeSelectPage_hardButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				singleModeSelectPage_hardButton.setIcon(new ImageIcon(".\\images\\hard_Button.png"));
				singleModeSelectPage_hardButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				singleModeSelectPage_hardButton.setIcon(new ImageIcon(".\\images\\hard_ButtonPressed.png"));
			}

			public void mouseReleased(MouseEvent e) {
				// ���� ����
				singleModeSelectPage.setVisible(false);
				hardModePage.setVisible(true);
				hard_gameStart();
			}
		});

		singleModeSelectPage.add(singleModeSelectPage_hardButton);

		// ���� ��ư
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
				// ���� ����
			}
		});
		singleModeSelectPage.add(singleModeSelectPage_challengeButton);

		// �ڷΰ��� ��ư
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

			public void mousePressed(MouseEvent e) {
				singleModeSelectPage_backButton.setIcon(new ImageIcon(".\\images\\backButtonPressed.png"));
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				singleModeSelectPage.setVisible(false);
				startGamePage.setVisible(true);
			}
		});

		singleModeSelectPage.add(singleModeSelectPage_backButton);
	}

	//======================================================================
	/* ���� ���� */
//	public void gameStart(int nowSelected, String mode) {
	InGame IG;

	public void easy_gameStart() {

		// ���̵����� ����ü�� Ŭ������ [����x����|���ڼ�] ������ ����
		// �ʱ� : 9x9|10
		// �߱� : 16x16|40
		// ��� : 30x16|99
		int col = 9;
		int row = 9;
		int mine = 10;

		IG = new InGame(col, row, mine, 50);// ���ΰ���,���ΰ���,���ڼ�,��ĭ�� ũ��
		IG = new InGame(col, row, mine, 50);// ���ΰ���,���ΰ���,���ڼ�,��ĭ�� ũ��
		IG.setBounds(410, 150, IG.getWidth(), IG.getHeight());
		easyModePage.add(IG);
//			InGame IG2 = new InGame(col,row,mine);
//			IG2.setBounds(100, 400,IG2.getWidth(), IG2.getHeight());
//			frame.add(IG2);
//			InGame IG3 = new InGame(col,row,mine);
//			IG3.setBounds(700, 100,IG3.getWidth(), IG3.getHeight());
//			frame.add(IG3);
//			InGame IG4 = new InGame(col,row,mine);
//			IG4.setBounds(700, 400,IG4.getWidth(), IG4.getHeight());
//			frame.add(IG4);

//		KeyListener kl = new inGameKeyListener(IG);
//		easyModePage.addKeyListener(kl);

		easyModePage.setFocusable(true);
		easyModePage.requestFocus();

		// �۾��Ұ� : �¸� Ʈ����, �й� Ʈ����
		// ����� ���ؼ� �ʿ��Ѱ� : ���� ���¸� �˱� ���� ���� �����带 ���� ���ӻ��� ����.
		// ���ӻ��°� win�� �� �ʿ��� �۾� : �����ͺ��̽��� ��ŷ���ھ� ����. �¸� �޽���? gui ����
		// ���ӻ��°� lose�� �� �ʿ��� �۾� : �й� �޽���? gui ����
		var path = "./images/inGame/resources/11.png";
		JButton eee = new JButton("");
		eee.setIcon(new ImageIcon(".\\images\\backButton.png"));
		eee.setIcon(new ImageIcon(".\\images\\backButtonEntered.png"));

		if(IG.win) {
			try {
				String singlewin_data;
				singlewin_data = singlewin + "//" + easymode + "//" + IG.getTimer(); // Server���� "//"�� ���ؼ� ����
				out.writeUTF(singlewin_data);
			} catch(IOException ex) {

			}
		}
	}
//�߱޸��

	public void normal_gameStart() {

		int col = 16;
		int row = 16;
		int mine = 40;

		IG = new InGame(col, row, mine, 30);// ���ΰ���,���ΰ���,���ڼ�,��ĭ�� ũ��
		IG.setBounds(410, 150, IG.getWidth(), IG.getHeight());
		nomalModePage.add(IG);

//		KeyListener kl = new inGameKeyListener(IG);
//		easyModePage.addKeyListener(kl);

		nomalModePage.setFocusable(true);
		nomalModePage.requestFocus();

		var path = "./images/inGame/resources/11.png";
		JButton eee = new JButton("");
		eee.setIcon(new ImageIcon(".\\images\\backButton.png"));
		eee.setIcon(new ImageIcon(".\\images\\backButtonEntered.png"));

	}

	// ��޸��
	public void hard_gameStart() {

		int col = 30;
		int row = 16;
		int mine = 99;

		IG = new InGame(col, row, mine, 30);// ���ΰ���,���ΰ���,���ڼ�,��ĭ�� ũ��
		IG.setBounds(150, 150, IG.getWidth(), IG.getHeight());
		hardModePage.add(IG);

//		KeyListener kl = new inGameKeyListener(IG);
//		easyModePage.addKeyListener(kl);

		hardModePage.setFocusable(true);
		hardModePage.requestFocus();

		var path = "./images/inGame/resources/11.png";
		JButton eee = new JButton("");
		eee.setIcon(new ImageIcon(".\\images\\backButton.png"));
		eee.setIcon(new ImageIcon(".\\images\\backButtonEntered.png"));

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

class inGameKeyListener implements KeyListener {
	InGame IG;

	inGameKeyListener(InGame ig) {
		this.IG = ig;
	}

	void setIG(InGame ig) {
		this.IG = ig;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println(e.getKeyCode());
		if (e.getKeyCode() == KeyEvent.VK_1) {
			Items.scan.using(IG.ma.x, IG.ma.y);
//					System.out.println(IG.ma.x);
//					System.out.println(IG.ma.y);
		}
		if (e.getKeyCode() == KeyEvent.VK_2) {
			Items.reverse.using();

		}
	}
}