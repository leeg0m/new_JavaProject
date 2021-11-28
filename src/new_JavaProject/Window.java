package new_JavaProject;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilderFactory;

public class Window {
	Socket socket = new Socket(); // 소켓 생성
	BufferedReader in = null; // Server로부터 데이터를 읽어들임
	BufferedReader keyboard = null; // 키보드로 입력하는 값 읽어들임
	PrintWriter out = null; // Server로 내보내기 위한 출력스트림
	private JFrame frame;
	private int mouseX, mouseY;

	static String data;
	static String IDvalue; // ID
	static String PWvalue; // PW
	static String possible;

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

		// 게임 종료 버튼
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
				exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));		//버튼에 마우스 올리면 손가락 모양 커서로 변경
			}

			@Override
			public void mouseExited(MouseEvent e) {
				exitButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));		//버튼에 마우스 때면 원래 모양 커서로 변경
			}

			@Override
			public void mousePressed(MouseEvent e) {
				System.exit(0);
			}
		});
		frame.getContentPane().add(exitButton);


		// 게임 메뉴바(menuBar)
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


		//배경화면
		JLabel background = new JLabel("");
		background.setIcon(new ImageIcon(".\\images\\mainMenuPage.png"));
		background.setBounds(0, 0, 1280, 720);

		frame.getContentPane().add(background,2);

		background.setVisible(false);



		// 게임시작화면
		ImagePanel startGamePage = new ImagePanel(
				new ImageIcon(".\\images/startGamePage.png")
						.getImage());
		frame.setSize(startGamePage.getWidth(), startGamePage.getHeight());

		// 로그인화면
		ImagePanel loginPage = new ImagePanel(
				new ImageIcon("./images/loginPage.png").getImage());
		frame.setSize(loginPage.getWidth(), loginPage.getHeight());

		frame.getContentPane().add(loginPage);

		//로그인 화면 필드들

		//ID필드
		JTextField loginPageTxtID = new JTextField();
		loginPageTxtID.setFont(new Font("맑은 고딕", Font.PLAIN,20));
		loginPageTxtID.setBounds(527,537,178,33);
		loginPageTxtID.setBorder(null);
		loginPage.add(loginPageTxtID);

		//비밀번호 필드
		JPasswordField loginPagePass = new JPasswordField();
		loginPagePass.setFont(new Font("맑은 고딕", Font.PLAIN,20));
		loginPagePass.setBounds(526,583,178,33);
		loginPagePass.setBorder(null);
		loginPage.add(loginPagePass);

		//로그인화면 버튼들

		//로그인 버튼
		JButton loginButton = new JButton("");
		loginButton.setIcon(new ImageIcon(".\\images\\loginButton.png"));

		loginButton.setBounds(713, 537, 91, 80);
		loginButton.setBorderPainted(false);
		loginButton.setContentAreaFilled(false);
		loginButton.setFocusPainted(false);
		loginButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {
			// 메소드로 로그인 버튼을 눌렀을 때 서버에 ID와 PW를 반환
			// ID, PW를 반환받은 서버는 일치여부 확인을 위해 DB 반환
			// DB는 확인 후 맞으면 true를, 틀리면 false를 서버에 반환
			// 서버는 true를 반화받으면 success를, false를 반환받으면 fail을 반환
			//

				IDvalue = loginPageTxtID.getText();
				PWvalue = loginPagePass.getText();

				/*
				try {
					socket = new Socket("172.30.1.51", 1234);
					in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					keyboard = new BufferedReader(new InputStreamReader(System.in));
					out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
					System.out.println(socket.toString());

					data = IDvalue + " " + PWvalue;
					out.write(data);


				}*/
				try {
					socket = new Socket("127.0.0.1", 1234);
					in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					keyboard = new BufferedReader(new InputStreamReader(System.in));
					out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
					System.out.println(socket.toString());

					data = IDvalue + " " + PWvalue;
					out.write(data);

				} catch (IOException ex) {

				}

				try {
					possible = in.readLine();
					if(possible.equals("success")) {
						loginPage.setVisible(false);
						startGamePage.setVisible(true);
					}
				} catch(IOException ex) {
					JOptionPane.showMessageDialog(null,"ID나 PW가 잘못되었습니다!");
				}



				loginPage.setVisible(false); // 화면 바뀌게 하는거 (id,pw 맞을 떄 조건으로 변경)
				startGamePage.setVisible(true);
			}
		});
		loginPage.add(loginButton);


		//회원가입 버튼
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
				//JOptionPane.showMessageDialog(null,"회원가입 테슷흐");

			}
		});
		loginPage.add(signupButton);


		//아이디/비밀번호 찾기
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
				JOptionPane.showMessageDialog(null,"찾기 테슷흐");

			}
		});
		loginPage.add(find_ID_Pass_Button);

		frame.getContentPane().add(startGamePage);

		// 랭크화면
		ImagePanel rankPage = new ImagePanel(
				new ImageIcon(".\\images\\rankPage.png").getImage());
		frame.setSize(rankPage.getWidth(), rankPage.getHeight());
		frame.getContentPane().add(rankPage);

		// 튜토리얼화면
		ImagePanel htpPage = new ImagePanel(
				new ImageIcon(".\\images/htpPage.png").getImage());
		frame.setSize(htpPage.getWidth(), htpPage.getHeight());
		frame.getContentPane().add(htpPage);

		//싱글게임모드선택화면
		ImagePanel singleModeSelectPage = new ImagePanel(new ImageIcon(".\\images/startGamePage.png")
				.getImage());
		frame.setSize(singleModeSelectPage.getWidth(), singleModeSelectPage.getHeight());
		frame.getContentPane().add(singleModeSelectPage);


		//화면 초기화
		htpPage.setVisible(false);
		rankPage.setVisible(false);
		startGamePage.setVisible(false);
		singleModeSelectPage.setVisible(false);
		background.setVisible(false);


		frame.getContentPane().setLayout(null);

		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);




		//게임시작화면 버튼들

		//싱글 버튼
		JButton startGamePage_singleButton = new JButton("");
		startGamePage_singleButton.setIcon(new ImageIcon(".\\images\\singleButton.png"));

		startGamePage_singleButton.setBorderPainted(false);
		startGamePage_singleButton.setContentAreaFilled(false);
		startGamePage_singleButton.setFocusPainted(false);

		startGamePage_singleButton.setBounds(207, 257, 350, 280);

		startGamePage_singleButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {
				startGamePage.setVisible(false);
				singleModeSelectPage.setVisible(true);
			}
		});
		startGamePage.add(startGamePage_singleButton);


		//멀티 버튼
		JButton startGamePage_multiButton = new JButton("");
		startGamePage_multiButton.setIcon(new ImageIcon(".\\images\\multiButton.png"));

		startGamePage_multiButton.setBorderPainted(false);
		startGamePage_multiButton.setContentAreaFilled(false);
		startGamePage_multiButton.setFocusPainted(false);

		startGamePage_multiButton.setBounds(729, 258, 350, 280);
		startGamePage.add(startGamePage_multiButton);


		//랭크 메뉴 버튼
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

		//튜토리얼 메뉴 버튼
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

		//로그아웃 버튼
		JButton startGamePage_logoutButton = new JButton("");
		startGamePage_logoutButton.setIcon(new ImageIcon(".\\images\\logoutButton.png"));
		startGamePage_logoutButton.setBounds(1203, 43, 47, 47);
		startGamePage_logoutButton.setBorderPainted(false);
		startGamePage_logoutButton.setContentAreaFilled(false);
		startGamePage_logoutButton.setFocusPainted(false);
		startGamePage_logoutButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				startGamePage_logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				startGamePage_logoutButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				startGamePage.setVisible(false);
				loginPage.setVisible(true);
			}
		});
		startGamePage.add(startGamePage_logoutButton);


		//랭크화면 버튼들

		//게임시작 메뉴 버튼
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

		//튜토리얼 메뉴 버튼
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


		//로그아웃 버튼
		JButton rankpage_logoutButton = new JButton("");
		rankpage_logoutButton.setIcon(new ImageIcon(".\\images\\logoutButton.png"));
		rankpage_logoutButton.setBounds(1203, 43, 47, 47);
		rankpage_logoutButton.setBorderPainted(false);
		rankpage_logoutButton.setContentAreaFilled(false);
		rankpage_logoutButton.setFocusPainted(false);
		rankpage_logoutButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				rankpage_logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				rankpage_logoutButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				rankPage.setVisible(false);
				loginPage.setVisible(true);
			}
		});
		rankPage.add(rankpage_logoutButton);

		//튜토리얼화면 버튼들

		//게임시작 메뉴 버튼
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

		//랭크 메뉴 버튼
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

		//로그아웃 버튼
		JButton htpPage_logoutButton = new JButton("");
		htpPage_logoutButton.setIcon(new ImageIcon(".\\images\\logoutButton.png"));
		htpPage_logoutButton.setBounds(1203, 43, 47, 47);
		htpPage_logoutButton.setBorderPainted(false);
		htpPage_logoutButton.setContentAreaFilled(false);
		htpPage_logoutButton.setFocusPainted(false);
		htpPage_logoutButton.addMouseListener(new MouseAdapter() {
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
				htpPage.setVisible(false);
				loginPage.setVisible(true);
			}
		});
		htpPage.add(htpPage_logoutButton);



		//싱글선택화면 모드들

		//랭크 메뉴 버튼
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

		//튜토리얼 메뉴 버튼

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

		//로그아웃 버튼
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

		//초급 모드 버튼
		JButton singleModeSelectPage_beginnerButton = new JButton("");
		singleModeSelectPage_beginnerButton.setIcon(new ImageIcon(".\\images\\beginner_Button.png"));

		singleModeSelectPage_beginnerButton.setBorderPainted(false);
		singleModeSelectPage_beginnerButton.setContentAreaFilled(false);
		singleModeSelectPage_beginnerButton.setFocusPainted(false);

		singleModeSelectPage_beginnerButton.setBounds(89, 301, 200, 200);

		singleModeSelectPage_beginnerButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {
				singleModeSelectPage.setVisible(false);
				background.setVisible(true);
				beginner_gameStart();

			}
		});
		singleModeSelectPage.add(singleModeSelectPage_beginnerButton);

		//중급 모드 버튼
		JButton singleModeSelectPage_intermediateButton = new JButton("");
		singleModeSelectPage_intermediateButton.setIcon(new ImageIcon(".\\images\\intermediate_Button.png"));

		singleModeSelectPage_intermediateButton.setBorderPainted(false);
		singleModeSelectPage_intermediateButton.setContentAreaFilled(false);
		singleModeSelectPage_intermediateButton.setFocusPainted(false);

		singleModeSelectPage_intermediateButton.setBounds(390, 301, 200, 200);

		singleModeSelectPage_intermediateButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}
		});
		singleModeSelectPage.add(singleModeSelectPage_intermediateButton);

		//고급 모드 버튼
		JButton singleModeSelectPage_advancedButton = new JButton("");
		singleModeSelectPage_advancedButton.setIcon(new ImageIcon(".\\images\\advanced_Button.png"));

		singleModeSelectPage_advancedButton.setBorderPainted(false);
		singleModeSelectPage_advancedButton.setContentAreaFilled(false);
		singleModeSelectPage_advancedButton.setFocusPainted(false);

		singleModeSelectPage_advancedButton.setBounds(691, 301, 200, 200);

		singleModeSelectPage_advancedButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}
		});

		singleModeSelectPage.add(singleModeSelectPage_advancedButton);

		//도전 모드 버튼
		JButton singleModeSelectPage_challengeButton = new JButton("");
		singleModeSelectPage_challengeButton.setIcon(new ImageIcon(".\\images\\challenge_Button.png"));

		singleModeSelectPage_challengeButton.setBorderPainted(false);
		singleModeSelectPage_challengeButton.setContentAreaFilled(false);
		singleModeSelectPage_challengeButton.setFocusPainted(false);

		singleModeSelectPage_challengeButton.setBounds(992, 301, 200, 200);

		singleModeSelectPage_challengeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}
		});
		singleModeSelectPage.add(singleModeSelectPage_challengeButton);

		//뒤로가기 버튼
		JButton singleModeSelectPage_backButton = new JButton("");
		singleModeSelectPage_backButton.setIcon(new ImageIcon(".\\images\\backButton.png"));
		singleModeSelectPage_backButton.setBounds(56, 598, 66, 66);
		singleModeSelectPage_backButton.setBorderPainted(false);
		singleModeSelectPage_backButton.setContentAreaFilled(false);
		singleModeSelectPage_backButton.setFocusPainted(false);
		singleModeSelectPage_backButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				singleModeSelectPage_backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				singleModeSelectPage_backButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
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
		//초급 : 9x9|10
		//중급 : 16x16|40
		//고급 : 30x16|99
		//난이도별로 구조체나 클래스로 [가로x세로|지뢰수] 데이터 저장
//			private final int BOARD_WIDTH = N_COLS * CELL_SIZE + 1;//프레임 넓이
//		    private final int BOARD_HEIGHT = N_ROWS * CELL_SIZE + 1;//
//			int size =
//			InGame IG = new InGame(new JLabel(""));
		InGame IG = new InGame(col,row,mine,50);//가로개수,세로개수,지뢰수,한칸당 크기
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
