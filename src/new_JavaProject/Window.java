package new_JavaProject;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Window {

	private JFrame frame;
	private int mouseX, mouseY;

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
				exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));		//��ư�� ���콺 �ø��� �հ��� ��� Ŀ���� ����
			}

			@Override
			public void mouseExited(MouseEvent e) {
				exitButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));		//��ư�� ���콺 ���� ���� ��� Ŀ���� ����
			}

			@Override
			public void mousePressed(MouseEvent e) {
				System.exit(0);
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

		

		//�α���ȭ�� ��ư��
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
				loginPage.setVisible(false);
				startGamePage.setVisible(true);

			}
		});
		loginPage.add(loginButton);

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
				JOptionPane.showMessageDialog(null,"ȸ������ �׽���");

			}
		});
		loginPage.add(signupButton);
		
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

		// ��ũȭ��
		ImagePanel rankPage = new ImagePanel(
				new ImageIcon(".\\images\\rankPage.png").getImage());
		frame.setSize(rankPage.getWidth(), rankPage.getHeight());
		frame.getContentPane().add(rankPage);

		// Ʃ�丮��ȭ��
		ImagePanel htpPage = new ImagePanel(
				new ImageIcon(".\\images/htpPage.png").getImage());
		frame.setSize(htpPage.getWidth(), htpPage.getHeight());
		frame.getContentPane().add(htpPage);
		htpPage.setVisible(false);
		rankPage.setVisible(false);
		startGamePage.setVisible(false);
		frame.getContentPane().setLayout(null);

		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		
		//���ӽ���ȭ�� ��ư��
		JButton startGamePage_singleButton = new JButton("");
		startGamePage_singleButton.setIcon(new ImageIcon(".\\images\\singleButton.png"));

		startGamePage_singleButton.setBorderPainted(false);
		startGamePage_singleButton.setContentAreaFilled(false);
		startGamePage_singleButton.setFocusPainted(false);

		startGamePage_singleButton.setBounds(207, 257, 350, 280);
		startGamePage.add(startGamePage_singleButton);

		JButton startGamePage_multiButton = new JButton("");
		startGamePage_multiButton.setIcon(new ImageIcon(".\\images\\multiButton.png"));

		startGamePage_multiButton.setBorderPainted(false);
		startGamePage_multiButton.setContentAreaFilled(false);
		startGamePage_multiButton.setFocusPainted(false);

		startGamePage_multiButton.setBounds(729, 258, 350, 280);
		startGamePage.add(startGamePage_multiButton);

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
		
		
		//��ũȭ�� ��ư��
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

		//Ʃ�丮��ȭ�� ��ư��
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