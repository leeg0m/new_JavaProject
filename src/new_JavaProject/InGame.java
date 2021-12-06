package new_JavaProject;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import new_JavaProject.InGame.Items.reverse;

public class InGame extends JPanel {
//#å����(Ctrl f)
//=���Ž��=
//=��Ž��=
//=ùŬ��=
//=����=
//=���콺�̺�Ʈ=
//=ȭ�����=
//=Ŭ����=
//=Ű�����̺�Ʈ=

	//    private final int NUM_IMAGES = 26;
	private final int NUM_IMAGES = 13;// \Java-Minesweeper-Game-master\Java-Minesweeper-Game-master\src\resources �̹���
	// ���� 12��
//    private final int CELL_SIZE = 15;//���� ����
	private static int CELL_SIZE = 30;

	private final int COVER_FOR_CELL = 10;// Ŀ��(��Ȱ��ȭ ĭ)//Ŀ�� �ִ°��� 10��ŭ ������
	private final int MARK_FOR_CELL = 10;// ��� ���� ĭ//��߲������� 10��ŭ ������
	private final int EMPTY_CELL = 0;// ��ĭ(Ȱ��ȭ��ĭ)
	private final int MINE_CELL = 9;// ���� ��(ĭ)
	private final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;// Ŀ��+���� = Ŀ�� ���� ����
	private final int MARKED_MINE_CELL = COVERED_MINE_CELL + MARK_FOR_CELL;// ��� ���� ���ڼ� //���+Ŀ������ = ���(10) + ����(9+10)

	private final int DRAW_MINE = 9;// ���� �̹���?
	//  private final int DRAW_COVER = 1;//NUM_IMAGES�� ��������. ���� CELL�� NUM_IMAGES�� ��������.
//  �̹������� 0:���� 1:1 2:2 3:3 .. 8:8 9:���� 10:�ȴ��� 11:�� 12:�� ���
//	���� ���� 0:���� 1:1 2:2 3:3 .. 8:8 9:���� 10:�ȴ��� 11:Ŀ��(1) 12:Ŀ��(2) .. 19:Ŀ������ 29:��߲�������Ŀ�� //20:�󼿿� ���� ��� 21:1���������...28:8���ȱ�
	private final int DRAW_COVER = 10;// Ŀ��
	private final int DRAW_MARK = 11;// ��߸�ũ
	private final int DRAW_WRONG_MARK = 12;// �� ���

	//    private final int N_MINES = 40;//���� ����
	private static int N_MINES = 40;// ���� ����
	//    private final int N_ROWS = 16;//����ĭ ��
	private static int N_ROWS = 16;// ����ĭ ��
	//    private final int N_COLS = 32;//����ĭ ��
//    private final int N_COLS = 16;//����ĭ ��
	private static int N_COLS = 16;// ����ĭ ��

	//    private final int BOARD_WIDTH = N_COLS * CELL_SIZE + 1;//������ ����
//    private final int BOARD_HEIGHT = N_ROWS * CELL_SIZE + 1;//
	private int BOARD_WIDTH = N_COLS * CELL_SIZE + 1;// ������ ����
	private int BOARD_HEIGHT = N_ROWS * CELL_SIZE + 1;//

	private static int[] field;// ��� ��(ĭ) �ѹ���
	private boolean inGame;// false:game lost
	private boolean win;
	private int minesLeft;// �����ϴ��� ǥ�õǴ� ���� ���� ���ڰ����� ī��Ʈ�ϴ� ����
	private Image[] img;// �̹��� ����

	private static int allCells;// N_ROWS * N_COLS;// = field ����
	private final JLabel statusbar = new JLabel("testMessage");// �����ϴ��� �۾�. ex) ���ڰ���, �¸��޽���,�й�޽��� ��
	FirstClick fc = new FirstClick();
	CreateGame game = new CreateGame();
	private boolean mousePressedCheck = false;
	List<Integer> pressedCellCover = new ArrayList<>();
	List<Integer> scanCellCover = new ArrayList<>();

	long beforeTime;// �ð� ����
	long afterTime;
	long secDiffTime;
	Timer m_timer;
	TimerTask m_task;
	JLabel testLabel;

	Sound sound;
	Sound.soundsEnum soundsEnum;

	static MinesAdapter ma;

	// Ŭ���� ������
	public InGame(int n_cols, int n_rows, int n_mines, int cell_size) {
		this.N_COLS = n_cols;
		this.N_ROWS = n_rows;
		this.N_MINES = n_mines;
		this.CELL_SIZE = cell_size;
		BOARD_WIDTH = N_COLS * CELL_SIZE + 1;// ������ ����
		BOARD_HEIGHT = N_ROWS * CELL_SIZE + 1;//

//        this.statusbar = statusbar;
		initBoard();
	}

	public int getWidth() {
		return BOARD_WIDTH;
	}

	public int getHeight() {
		return BOARD_HEIGHT;
	}

	private void initBoard() {
		setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));// â ������ ����

		sound = new Sound();
		sound.setSounds();// ���� �߰�

		img = new Image[NUM_IMAGES];// �̹��� �ε�
		for (int i = 0; i < NUM_IMAGES; i++) {

			var path = "./images/inGame/resources/" + i + ".png";
			ImageIcon icon = new ImageIcon(path);
//            img[i] = (new ImageIcon(path)).getImage();
			img[i] = icon.getImage();
			img[i] = img[i].getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH);

			ImageIcon changeIcon = new ImageIcon(img[i]);
//            img[i] = imageSetSize(img[i],30,30);

		}

		ma = new MinesAdapter();
		addMouseListener(ma);
		addMouseMotionListener(ma);
//		addKeyListener(new Keys());//Ű�Է¹ޱ�� window�� �����ؾ� �����ҰŰ�����
//		this.requestFocus();
//		this.setFocusable(true);
		game.gameBoard();
//        game.newGame();//�ٽ��ڵ�
	}
//    static ImageIcon imageSetSize(ImageIcon icon, int i, int j) { // image Size Setting
//		Image ximg = icon.getImage();  //ImageIcon�� Image�� ��ȯ.
//		Image yimg = ximg.getScaledInstance(i, j, java.awt.Image.SCALE_SMOOTH);
//		ImageIcon xyimg = new ImageIcon(yimg);
//		return xyimg;
//	}

//    private void newGame() {
//
//        int cell;
//
//        var random = new Random();//java.util.Random //���� ��ġ�����Ҷ� ���
//        inGame = true;//���� ����. true:��� false:gamelose
//        minesLeft = N_MINES;//���� ������ �� ������ �ʱ�ȭ
//
//        allCells = N_ROWS * N_COLS;
//        field = new int[allCells];
//
//        for (int i = 0; i < allCells; i++) {
//
//            field[i] = COVER_FOR_CELL;//��� ĭ�� Ŀ��(��Ȱ��ȭ ��ĭ)�� ����
//        }
//
//        statusbar.setText(Integer.toString(minesLeft));
//
//        int i = 0;
//
//        while (i < N_MINES) {
//
////            int position = (int) (50 * random.nextDouble());
//            int position = (int) (allCells * random.nextDouble());//��ü �� �߿� ��ġ�ϴ� ��ǥ��. ex) position==0 �϶� (0,0)�� ���ڰ� ����
//
//            if ((position < allCells)
//                    && (field[position] != COVERED_MINE_CELL)) { //������ ��ġ�� ��üĭ���� ũ�� �����鼭 ���ڰ��ִ� �ʵ��� ���°� Ŀ���� ������ ����ĭ�� �ƴϸ�
//
//                int current_col = position % N_COLS;//������ ���� ��ġ. %����
//
//                field[position] = COVERED_MINE_CELL;// ���ڰ� �ִ� �ʵ忡 Ŀ��(��������)�� ����
//                i++;
//
//                if (current_col > 0) {//0���� ŭ = ���� ���ʿ� ��ġ���� ���� = ���� ���� �����ȿ� �ִ��� üũ
//                    cell = position - 1 - N_COLS;//���� ��ġ�� ���� ���� ĭ
////                    System.out.println(cell);
//                    if (cell >= 0) {//0���� ũ�ų� ���� = ���� �� ���� ���� �ȿ� �ִ�.
//                        if (field[cell] != COVERED_MINE_CELL) {//����ĭ �������� ���� ������
//                            field[cell] += 1;//���ڸ��� 1����
//                        }
//                    }
//                    cell = position - 1;//���� ��ġ�� ����
//                    if (cell >= 0) {
//                        if (field[cell] != COVERED_MINE_CELL) {
//                            field[cell] += 1;
//                        }
//                    }
//
//                    cell = position + N_COLS - 1;//���� �Ʒ�
//                    if (cell < allCells) {
//                        if (field[cell] != COVERED_MINE_CELL) {
//                            field[cell] += 1;
//                        }
//                    }
//                }
//
//                cell = position - N_COLS;//��
//                if (cell >= 0) {
//                    if (field[cell] != COVERED_MINE_CELL) {
//                        field[cell] += 1;
//                    }
//                }
//
//                cell = position + N_COLS;//�Ʒ�
//                if (cell < allCells) {
//                    if (field[cell] != COVERED_MINE_CELL) {
//                        field[cell] += 1;
//                    }
//                }
//
//                if (current_col < (N_COLS - 1)) {//������ ���� �����ȿ��ִ��� üũ
//                    cell = position - N_COLS + 1;//������ ��
//                    if (cell >= 0) {
//                        if (field[cell] != COVERED_MINE_CELL) {
//                            field[cell] += 1;
//                        }
//                    }
//                    cell = position + N_COLS + 1;//������ �Ʒ�
//                    if (cell < allCells) {
//                        if (field[cell] != COVERED_MINE_CELL) {
//                            field[cell] += 1;
//                        }
//                    }
//                    cell = position + 1;//������
//                    if (cell < allCells) {
//                        if (field[cell] != COVERED_MINE_CELL) {
//                            field[cell] += 1;
//                        }
//                    }
//                }
//            }
//        }
//    }

	private class CreateGame {
		int cell;

		void newGame() {
//    		gameBoard();

			int i = 0;
			var random = new Random();
			int position;

			while (i < N_MINES) {

				position = (int) (allCells * random.nextDouble());
//            		position = (int) (allCells * random.nextDouble());//��ü �� �߿� ��ġ�ϴ� ��ǥ��. ex) position==0 �϶� (0,0)�� ���ڰ� ����

				boolean count = false;// �󼿿� ���ڰ� ��ġ�Ǹ� �ٽ� ���
				for (int empty : fc.emptyCell) {
					if (empty == position)
						count = true;
				}
				if (count)
					continue;

				if ((position < allCells) && (field[position] != COVERED_MINE_CELL)) { // ������ ��ġ�� ��üĭ���� ũ�� �����鼭 ���ڰ��ִ�
					// �ʵ��� ���°� Ŀ���� ������ ����ĭ�� �ƴϸ�

					int current_col = position % N_COLS;// ������ ���� ��ġ. %����

					field[position] = COVERED_MINE_CELL;// ���ڰ� �ִ� �ʵ忡 Ŀ��(��������)�� ����
					i++;

					if (current_col > 0) {// 0���� ŭ = ���� ���ʿ� ��ġ���� ���� = ���� ���� �����ȿ� �ִ��� üũ
						cell = position - 1 - N_COLS;// ���� ��ġ�� ���� ���� ĭ
						if (cell >= 0) {// 0���� ũ�ų� ���� = ���� �� ���� ���� �ȿ� �ִ�.
							if (field[cell] != COVERED_MINE_CELL) {// ����ĭ �������� ���� ������
								field[cell] += 1;// ���ڸ��� 1����
							}
						}
						cell = position - 1;// ���� ��ġ�� ����
						if (cell >= 0) {
							if (field[cell] != COVERED_MINE_CELL) {
								field[cell] += 1;
							}
						}

						cell = position + N_COLS - 1;// ���� �Ʒ�
						if (cell < allCells) {
							if (field[cell] != COVERED_MINE_CELL) {
								field[cell] += 1;
							}
						}
					}

					cell = position - N_COLS;// ��
					if (cell >= 0) {
						if (field[cell] != COVERED_MINE_CELL) {
							field[cell] += 1;
						}
					}

					cell = position + N_COLS;// �Ʒ�
					if (cell < allCells) {
						if (field[cell] != COVERED_MINE_CELL) {
							field[cell] += 1;
						}
					}

					if (current_col < (N_COLS - 1)) {// ������ ���� �����ȿ��ִ��� üũ
						cell = position - N_COLS + 1;// ������ ��
						if (cell >= 0) {
							if (field[cell] != COVERED_MINE_CELL) {
								field[cell] += 1;
							}
						}
						cell = position + N_COLS + 1;// ������ �Ʒ�
						if (cell < allCells) {
							if (field[cell] != COVERED_MINE_CELL) {
								field[cell] += 1;
							}
						}
						cell = position + 1;// ������
						if (cell < allCells) {
							if (field[cell] != COVERED_MINE_CELL) {
								field[cell] += 1;
							}
						}
					}
				}
			}
		}

		void gameBoard() {
			fc.firstCheck = true;// ùŬ�� ���� �ʱ�ȭ

			inGame = true;// ���� ����. true:��� false:gamelose
			win = false;
			minesLeft = N_MINES;// ���� ������ �� ������ �ʱ�ȭ

			allCells = N_ROWS * N_COLS;
			field = new int[allCells];

			for (int i = 0; i < allCells; i++) {

				field[i] = COVER_FOR_CELL;// ��� ĭ�� Ŀ��(��Ȱ��ȭ ��ĭ)�� ����
			}

		}
	}

	//  =============================��Ž��======================
	private void find_empty_cells(int j) {// ��ĭã��

		int current_col = j % N_COLS;// �Ѱܹ��� ĭ�� ���� ��ġ
		int cell;

		if (current_col > 0) {// ���� ���� ������ �ִ��� üũ
			cell = j - N_COLS - 1;// ���� ��
			if (cell >= 0) {
				if (field[cell] > MINE_CELL) {// ���� ���� ���� ���ڼ�(9)���� ũ��(��Ȱ��ȭ ��ĭ / ���?)
					field[cell] -= COVER_FOR_CELL;// Ŀ����(10)��ŭ �� //10-18 => 0-8
					if (field[cell] == EMPTY_CELL) {// ��ĭ�̸�
						find_empty_cells(cell);// ���ȣ�� = ���� �� �������� �ٽ� �� Ž��
					}
				}
			}

			cell = j - 1;// ����
			if (cell >= 0) {
				if (field[cell] > MINE_CELL) {
					field[cell] -= COVER_FOR_CELL;
					if (field[cell] == EMPTY_CELL) {
						find_empty_cells(cell);
					}
				}
			}

			cell = j + N_COLS - 1;// ���� �Ʒ�
			if (cell < allCells) {
				if (field[cell] > MINE_CELL) {
					field[cell] -= COVER_FOR_CELL;
					if (field[cell] == EMPTY_CELL) {
						find_empty_cells(cell);
					}
				}
			}
		}

		cell = j - N_COLS;// ��
		if (cell >= 0) {
			if (field[cell] > MINE_CELL) {
				field[cell] -= COVER_FOR_CELL;
				if (field[cell] == EMPTY_CELL) {
					find_empty_cells(cell);
				}
			}
		}

		cell = j + N_COLS;// �Ʒ�
		if (cell < allCells) {
			if (field[cell] > MINE_CELL) {
				field[cell] -= COVER_FOR_CELL;
				if (field[cell] == EMPTY_CELL) {
					find_empty_cells(cell);
				}
			}
		}

		if (current_col < (N_COLS - 1)) {// ������ ���� �������� �ִ��� üũ
			cell = j - N_COLS + 1;// ������ ��
			if (cell >= 0) {
				if (field[cell] > MINE_CELL) {
					field[cell] -= COVER_FOR_CELL;
					if (field[cell] == EMPTY_CELL) {
						find_empty_cells(cell);
					}
				}
			}

			cell = j + N_COLS + 1;// ������ �Ʒ�
			if (cell < allCells) {
				if (field[cell] > MINE_CELL) {
					field[cell] -= COVER_FOR_CELL;
					if (field[cell] == EMPTY_CELL) {
						find_empty_cells(cell);
					}
				}
			}

			cell = j + 1;// ������
			if (cell < allCells) {
				if (field[cell] > MINE_CELL) {
					field[cell] -= COVER_FOR_CELL;
					if (field[cell] == EMPTY_CELL) {
						find_empty_cells(cell);
					}
				}
			}
		}

	}

	// ==================================ȭ�����=========================================
	static Rectangle scanBox = new Rectangle();
	@Override
	public void paintComponent(Graphics g){

		int uncover = 0;// Ŀ�� Ȱ��ȭ

		if (inGame) {
			for (int i = 0; i < N_ROWS; i++) {// ����

				for (int j = 0; j < N_COLS; j++) {// ����

					int cell = field[(i * N_COLS) + j];

					if (cell == MINE_CELL) {// �������̰�, ���ڼ��� ������

						inGame = false;// ����
					}
				}
			}
		}

		for (int i = 0; i < N_ROWS; i++) {// ����

			for (int j = 0; j < N_COLS; j++) {// ����
				int cell = field[(i * N_COLS) + j];

				if (!inGame) {// ������

					if (cell == COVERED_MINE_CELL) {// Ŀ�� ���ڴ�
						cell = DRAW_MINE;// ���ڸ� ǥ��
					} else if (cell == MARKED_MINE_CELL) {// ��߲��� ���� ����
						cell = DRAW_MARK;// ��� �̹��� ����
					} else if (cell > COVERED_MINE_CELL) {// ��߲��� ���ھƴ� ����
						cell = DRAW_WRONG_MARK;// ��� ��� �̹���
					} else if (cell > MINE_CELL) {// ����(9)�� ���� ũ��(10-18)
						cell = DRAW_COVER;// Ŀ�� �̹���
					}

				} else {// �ƴϸ�

					if (cell > COVERED_MINE_CELL) {// Ŀ�� ����(19) ���� ũ��
						cell = DRAW_MARK;// ���
					} else if (cell > MINE_CELL) {// ���ں��� ũ��
						cell = DRAW_COVER;// �Ϲ�Ŀ��
						uncover++;// ��Ȱ��ȭ �� ī��Ʈ ����
					}
//                    if(mousePressedCheck) {
//	                    for(int pcc : pressedCellCover) {
//	                    	if((i*j) == pcc) cell = EMPTY_CELL;
//	                    	System.out.println("iijijijijii"+i*j);
//	                    }
//                    }
				}

				g.drawImage(img[cell], (j * CELL_SIZE), (i * CELL_SIZE), this);// �� �̹��� ����

			}
			if (mousePressedCheck) {
				for (int pcc : pressedCellCover) {
					int col = pcc % N_COLS;
					int row = pcc / N_COLS;
					g.drawImage(img[EMPTY_CELL], (col * CELL_SIZE), (row * CELL_SIZE), this);// �� �̹��� ����
				}
			}
			if(Items.scan.scan) {
				for(int pcc : scanCellCover) {
//					int col = pcc % N_COLS;
//					int row = pcc / N_COLS;
//					if (cell == COVERED_MINE_CELL) {// Ŀ�� ���ڴ�
//						cell = DRAW_MINE;// ���ڸ� ǥ��
//					} else if (cell == MARKED_MINE_CELL) {// ��߲��� ���� ����
//						cell = DRAW_MARK;// ��� �̹��� ����
//					} else if (cell > COVERED_MINE_CELL) {// ��߲��� ���ھƴ� ����
//						cell = DRAW_WRONG_MARK;// ��� ��� �̹���
//					} else if (cell > MINE_CELL) {// ����(9)�� ���� ũ��(10-18)
//						cell = DRAW_COVER;// Ŀ�� �̹���
//					}
//					g.drawImage(img[cell], (j * CELL_SIZE), (i * CELL_SIZE), this);// �� �̹��� ����
				}
			}
		}
		// ���� �¸�
		if (uncover == 0 && inGame) {// �������̰� ��Ȱ��ȭ ĭ ������
			afterTime = System.nanoTime();// ���� ����ð�
			secDiffTime = (afterTime - beforeTime) / 1_000_000;
			m_task.cancel();
			inGame = false;// ��������
			win = true;

//			statusbar.setText("Game won");// �¸�
//            sound(soundsFilePath.get(soundsEnum.GAMEWIN.ordinal()));//�¸��Ҹ�

		} else if (!inGame) { // ���ӳ�
			afterTime = System.nanoTime();// ���� ����ð�
			secDiffTime = (afterTime - beforeTime) / 1_000_000;
			m_task.cancel();
			win = false;
//			statusbar.setText("Game lost");// �й�
//            sound(soundsFilePath.get(soundsEnum.GAMEOVER.ordinal()));//�й�Ҹ�
		}
		if(ma.getUsingItem()){
			g.setColor(Color.red);  //�簢�� �׵θ� ������ ���� ����
			g.drawRect(scanBox.x, scanBox.y, scanBox.width, scanBox.height); //������ box�� �Ӽ����� ������ �׷���
//			System.out.println("!");
		}
	}


	//	//=============================Ű�����̺�Ʈ======================
//	class Keys implements KeyListener{
//
//		@Override
//		public void keyTyped(KeyEvent e) {
//			// TODO Auto-generated method stub
//
//		}
//
//		@Override
//		public void keyPressed(KeyEvent e) {
//			// TODO Auto-generated method stub
//			System.out.println(e.getKeyCode());
//			if(e.getKeyCode()==KeyEvent.VK_1) {
//				Items.scan.using(ma.x,ma.y);
//				System.out.println(ma.x);
//				System.out.println(ma.y);
//			}
//		}
//
//		@Override
//		public void keyReleased(KeyEvent e) {
//			// TODO Auto-generated method stub
//
//		}
//
//	}
	// =============================���콺�̺�Ʈ======================
	class MinesAdapter extends MouseAdapter implements MouseMotionListener{

		int x;// ���콺 x��ǥ
		int y;// ���콺 y��ǥ
		private boolean usingItem = false;
		void setUsingItem(boolean ui) {usingItem=ui;}
		boolean getUsingItem() {return usingItem;}

		Point point;
		MouseEvent mouseEvent;

		@Override
		public void mousePressed(MouseEvent e) {
			x = e.getX();// ���콺 x��ǥ
			y = e.getY();// ���콺 y��ǥ
			int cCol = x / CELL_SIZE;
			int cRow = y / CELL_SIZE;
			int currentField = (cRow * N_COLS) + cCol;// ���� ������ ��
			boolean doRepaint = false;
//            mousePressedCheck = true;

			if ((x < N_COLS * CELL_SIZE) && (y < N_ROWS * CELL_SIZE)) {// 15*16=240

				if (e.getButton() == MouseEvent.BUTTON3) {// ��Ŭ���ϸ�

				} else {// ��Ŭ, ��Ŭ ���

					if ((field[currentField] > MINE_CELL) && (field[currentField] < MARKED_MINE_CELL)) {// Ŀ���ִ� ���̸�

						if (field[currentField] == MINE_CELL) {// ���ڿ�����
						}

						if (field[currentField] == EMPTY_CELL) {// ���̸�
						}

					}
					if (field[currentField] < COVER_FOR_CELL) {// Ȱ��ȭ ���̸�
						pressedCellCover = mousePressedCell(inGame, currentField);
						mousePressedCheck = true;
						doRepaint = true;
//                    	System.out.println();
					}
				}

				if (doRepaint) {
					repaint();
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			mousePressedCheck = false;

			x = e.getX();// ���콺 x��ǥ
			y = e.getY();// ���콺 y��ǥ
//          System.out.println(y);//����0-240
//          System.out.println(x);//����0-240

			int cCol = x / CELL_SIZE;
			int cRow = y / CELL_SIZE;
//          System.out.println(cCol);// ����0-240/15(CELL_SIZE) = 0~15(CELL_SIZE)

			int currentField = (cRow * N_COLS) + cCol;// ���� ������ ��
			boolean doRepaint = false;

			if (!inGame) {

				game.gameBoard();// ������
				repaint();// ���α׸�
			}
//            System.out.println(e.getButton());
//            System.out.println(MouseEvent.BUTTON3);

			if ((x < N_COLS * CELL_SIZE) && (y < N_ROWS * CELL_SIZE)) {// 15*16=240

				if (e.getButton() == MouseEvent.BUTTON3) {// ��Ŭ���ϸ�

					if (field[currentField] > MINE_CELL) {// ���� ���� ����(9)���� ũ�� = 10-29

						doRepaint = true;

						if (field[currentField] <= COVERED_MINE_CELL) {// Ŀ�����ں��� �۰ų� ������ = 10-19 = ��� �Ȳ���������

							if (minesLeft > 0) {// ���� ī���Ͱ� 0���� Ŭ��
								field[currentField] += MARK_FOR_CELL;// ��� ����
								minesLeft--;// ���� ī���� ����
								String msg = Integer.toString(minesLeft);
//								statusbar.setText(msg);// ī���� ����
//								Sound.SoundPlayer sp = new Sound.SoundPlayer(sound.soundsFilePath.get(soundsEnum.FLAG.ordinal()));
								sound.setPath(sound.soundsFilePath.get(soundsEnum.FLAG.ordinal()));//�׽�Ʈ��
								Thread t = new Thread(sound.sp);
								t.start();
//								sound(sound.soundsFilePath.get(soundsEnum.FLAG.ordinal()));// ��߼Ҹ�
							} else {
//                            	sound()//����
								statusbar.setText("No marks left");// ī���ͺ��� ����� ���� �ȾƼ� ���̻� ����� ���� �� ����
							}
						} else {// ��� ����������

							field[currentField] -= MARK_FOR_CELL;// ��� ����
							minesLeft++;// ī���� ����
							String msg = Integer.toString(minesLeft);
//							statusbar.setText(msg);// ����
//							sound(sound.soundsFilePath.get(sound.soundsEnum.UNFLAG.ordinal()));// ��߼Ҹ�
						}
					}

				} else {// ��Ŭ, ��Ŭ ���
					if (fc.firstCheck) {// ùŬ���̸�
//                		beforeTime = System.currentTimeMillis();//���� ���۽ð�
						beforeTime = System.nanoTime();// ���� ���۽ð�

						setTimerTask();
						m_timer = new Timer();
						m_timer.schedule(m_task, 0, 1000);

						fc.openFirstCell(inGame, currentField);// �ȹ��� �� Ȯ��
						game.newGame();// ���� ����
//						sound(soundsFilePath.get(soundsEnum.CLICK.ordinal()));// Ŭ���Ҹ�

//						repaint();
//						return;
					}

					if(Items.scan.scan) {
						scanCellCover = Items.scan.scanCell(currentField);
					}
					if (field[currentField] > COVERED_MINE_CELL) {// ��߲���������

//                    	sound()//����?
						return;// �ƹ��͵�����
					}

					if ((field[currentField] > MINE_CELL) && (field[currentField] < MARKED_MINE_CELL)) {// Ŀ���ִ� ���̸�

						field[currentField] -= COVER_FOR_CELL;// Ŀ�� ����
						doRepaint = true;

						if (field[currentField] == MINE_CELL) {// ���ڿ�����
//                        	sound(soundsFilePath.get(soundsEnum.GAMEOVER.ordinal()));//�й�Ҹ�
//                        	sound(soundsFilePath.get(soundsEnum.CLICK.ordinal()));//�й�Ҹ�
							inGame = false;// �й�
						}

						if (field[currentField] == EMPTY_CELL) {// ���̸�
							find_empty_cells(currentField);// �ٸ� �� Ž��
						}

//						if (field[currentField] < MINE_CELL)
//							sound(soundsFilePath.get(soundsEnum.CLICK.ordinal()));// Ŭ���Ҹ�

					}
					if (field[currentField] < COVER_FOR_CELL) {// Ȱ��ȭ ���̸�
						find_mark_cells(currentField);// ��ߺ��� �ȹ� Ȱ��ȭ
						doRepaint = true;
					}
				}

				if (doRepaint) {
					repaint();
				}
			}
		}
		@Override
		public void mouseMoved(MouseEvent e) {
			x=e.getX();
			y=e.getY();
			setMousePoint(e);
			setMouseEvent(e);
//			System.out.println(getUsingItem());
			if(ma.getUsingItem()) {repaint();
				Items.scan.using(e.getX(),e.getY());
//				System.out.println("live");
			}
			if(reverse.reverse) {//����ƽ���� �����Ǿ�����//���۾ȵɰ���//�׽�Ʈ��
				reverse.using();
			}
		}

		public void setMousePoint(MouseEvent e) {
			point = e.getPoint();
		}
		public Point getMousePoint() {
			return point;
		}
		public void setMouseEvent(MouseEvent e) {
			mouseEvent = e;
		}
		public MouseEvent getMouseEvent() {
			return mouseEvent;
		}
	}

	private class FlagsCount {// ���ã�Ƽ� ī��Ʈ �ϴµ� ����ϴ� Ŭ����
		int fc = 0;
	}

	// =============================���Ž��======================
	private void find_mark_cells(int j) {

		int current_col = j % N_COLS;// �Ѱܹ��� ĭ�� ���� ��ġ
		int cell;
		FlagsCount flags_count = new FlagsCount();// ��� ī����
		int coverMark = COVER_FOR_CELL + MARK_FOR_CELL;// �������,Ŀ������

		for (int i = 0; i < 2; i++) {// ù��° �ݺ�: ��� ī����, �ι�° �ݺ�: �ȹ��� Ȱ��ȭ
			if (current_col > 0) {// ���� ���� ������ �ִ��� üũ
				cell = j - N_COLS - 1;// ���� ��
				if (cell >= 0) {
//	            	if(i==0){//ù��° �ݺ� : �ȹ��� ��� �� ī��Ʈ
//	            		if (field[cell] >= coverMark) {//���� ���� ���� Ŀ���� �����鼭 ����� ����������
//	                		flags_count++;
//	            		}
//	            	}
//                	else{//�ι�° �ݺ� : ���� ���� ���ڿ� ���ī��Ʈ�� ������ �ȹ� Ȱ��ȭ
//                		if(flags_count == field[j]) {
//                			if (field[cell] < coverMark && field[cell]>=COVER_FOR_CELL) {//���� ���� ���� Ŀ���� �����鼭 ����� ������
//	                			field[cell] -= COVER_FOR_CELL;//Ŀ����(10)��ŭ �� //10-19 => 0-9
//	                		}
//                		}
//                	}
					find_mark_cells_tool(cell, flags_count, coverMark, i, j);// �� �ּ��� ���� ����
				}

				cell = j - 1;// ����
				if (cell >= 0) {
					find_mark_cells_tool(cell, flags_count, coverMark, i, j);

				}

				cell = j + N_COLS - 1;// ���� �Ʒ�
				if (cell < allCells) {
					find_mark_cells_tool(cell, flags_count, coverMark, i, j);
				}
			}

			cell = j - N_COLS;// ��
			if (cell >= 0) {
				find_mark_cells_tool(cell, flags_count, coverMark, i, j);
			}

			cell = j + N_COLS;// �Ʒ�
			if (cell < allCells) {
				find_mark_cells_tool(cell, flags_count, coverMark, i, j);
			}

			if (current_col < (N_COLS - 1)) {// ������ ���� �������� �ִ��� üũ
				cell = j - N_COLS + 1;// ������ ��
				if (cell >= 0) {
					find_mark_cells_tool(cell, flags_count, coverMark, i, j);
				}

				cell = j + N_COLS + 1;// ������ �Ʒ�
				if (cell < allCells) {
					find_mark_cells_tool(cell, flags_count, coverMark, i, j);
				}

				cell = j + 1;// ������
				if (cell < allCells) {
					find_mark_cells_tool(cell, flags_count, coverMark, i, j);
				}
			}
			if (flags_count.fc != field[j])
				break;// ��߼��� ���缿���ڿ� �ٸ��� �������۾���
			if (i == 1) {
//				if (inGame)
//					sound(soundsFilePath.get(soundsEnum.CLICK.ordinal()));// Ŭ���Ҹ�
//	        	else
//	        		sound();//
			}
		}
	}

	private void find_mark_cells_tool(int cell, FlagsCount flags_count, int coverMark, int i, int j) {
		if (i == 0) {// ù��° �ݺ� : �ȹ��� ��� �� ī��Ʈ
			if (field[cell] >= coverMark) {// ���� Ŀ���� �����鼭 ����� ����������
				flags_count.fc++;
			}
		} else {// �ι�° �ݺ� : ���� ���� ���ڿ� ���ī��Ʈ�� ������ �ȹ� Ȱ��ȭ
			if (flags_count.fc == field[j]) {
				if (field[cell] < coverMark && field[cell] >= COVER_FOR_CELL) {// ���� ���� ���� Ŀ���� �����鼭 ����� ������
					field[cell] -= COVER_FOR_CELL;// Ŀ����(10)��ŭ �� //10-19 => 0-9
				}
				if (field[cell] == EMPTY_CELL) {
					find_empty_cells(cell);
				}
			}
		}
	}

	// =============================ùŬ��======================
	private class FirstClick {
		boolean firstCheck = true;
		List<Integer> emptyCell = new ArrayList<>();// �� ��ǥ ����

		void openFirstCell(boolean inGame, int currentCell) {
			if (inGame) {
				if (firstCheck) {
					firstCheck = false;
					int currentCol = currentCell % N_COLS;// �Ѱܹ��� ĭ�� ���� ��ġ
					int cell;

					field[currentCell] = EMPTY_CELL;// ����ĭ ��
					emptyCell.add(currentCell);
					if (currentCol > 0) {// ���� ���� ������ �ִ��� üũ
						cell = currentCell - N_COLS - 1;// ���� ��
						if (cell >= 0) {
							field[cell] = EMPTY_CELL;
							emptyCell.add(cell);
						}
						cell = currentCell - 1;// ����
						if (cell >= 0) {
							field[cell] = EMPTY_CELL;
							emptyCell.add(cell);
						}
						cell = currentCell + N_COLS - 1;// ���� �Ʒ�
						if (cell < allCells) {
							field[cell] = EMPTY_CELL;
							emptyCell.add(cell);
						}
					}

					cell = currentCell - N_COLS;// ��
					if (cell >= 0) {
						field[cell] = EMPTY_CELL;
						emptyCell.add(cell);
					}

					cell = currentCell + N_COLS;// �Ʒ�
					if (cell < allCells) {
						field[cell] = EMPTY_CELL;
						emptyCell.add(cell);
					}

					if (currentCol < (N_COLS - 1)) {// ������ ���� �������� �ִ��� üũ
						cell = currentCell - N_COLS + 1;// ������ ��
						if (cell >= 0) {
							field[cell] = EMPTY_CELL;
							emptyCell.add(cell);
						}

						cell = currentCell + N_COLS + 1;// ������ �Ʒ�
						if (cell < allCells) {
							field[cell] = EMPTY_CELL;
							emptyCell.add(cell);
						}

						cell = currentCell + 1;// ������
						if (cell < allCells) {
							field[cell] = EMPTY_CELL;
							emptyCell.add(cell);
						}
					}
				}
			}
//    		else {
//    			firstCheck=true;
//    		}
		}
	}

	// ==================================Ŭ����=========================================
	List<Integer> mousePressedCell(boolean inGame, int currentCell) {// Ŭ���߿� ��Ȱ������ ������ �ӽ÷� Ȱ��ǥ��
		List<Integer> list = new ArrayList<>();
		if (inGame) {
			int currentCol = currentCell % N_COLS;// �Ѱܹ��� ĭ�� ���� ��ġ
			int cell;

			if (currentCol > 0) {// ���� ���� ������ �ִ��� üũ
				cell = currentCell - N_COLS - 1;// ���� ��
				if (cell >= 0) {
					if (field[cell] >= 10 && field[cell] < 20)
						list.add(cell);
				}
				cell = currentCell - 1;// ����
				if (cell >= 0) {
					if (field[cell] >= 10 && field[cell] < 20)
						list.add(cell);
				}
				cell = currentCell + N_COLS - 1;// ���� �Ʒ�
				if (cell < allCells) {
					if (field[cell] >= 10 && field[cell] < 20)
						list.add(cell);
				}
			}

			cell = currentCell - N_COLS;// ��
			if (cell >= 0) {
				if (field[cell] >= 10 && field[cell] < 20)
					list.add(cell);
			}

			cell = currentCell + N_COLS;// �Ʒ�
			if (cell < allCells) {
				if (field[cell] >= 10 && field[cell] < 20)
					list.add(cell);
			}

			if (currentCol < (N_COLS - 1)) {// ������ ���� �������� �ִ��� üũ
				cell = currentCell - N_COLS + 1;// ������ ��
				if (cell >= 0) {
					if (field[cell] >= 10 && field[cell] < 20)
						list.add(cell);
				}

				cell = currentCell + N_COLS + 1;// ������ �Ʒ�
				if (cell < allCells) {
					if (field[cell] >= 10 && field[cell] < 20)
						list.add(cell);
				}

				cell = currentCell + 1;// ������
				if (cell < allCells) {
					if (field[cell] >= 10 && field[cell] < 20)
						list.add(cell);
				}
			}
		}
		return list;
	}

	private void setTimerTask() {
		try {
			m_task = new TimerTask() {
				@Override
				public void run() {
					afterTime = System.nanoTime();
					secDiffTime = (afterTime - beforeTime) / 1_000_000;//����? �Ǽ�?
					System.out.printf("%.2f\n", (afterTime - beforeTime) / 1_000_000.);//������ -> �� ��ȯ
				}
			};
		} catch (Exception e) {

		}

	}




	//====UserInterface======
	public static class Player extends ItemBox{//������ ������ �����Ҽ��ִ� �÷��̾� ����. ��¼�� ���ο� ������Ʈ�� ���� �����ؾ�������
		int life;
		ItemBox itemBox;
		Player(){
			int life=1;
		}
		Player(int life){
			this.life = life;
		}

		void lifeIncrease() {life += 1;}
		void lifeDecrease() {life -= 1;}
		//��Ƽ���� ���� �ʿ�
		//�ϴ� �����ڷ� : https://creativeprm.tistory.com/320
		//https://www.google.com/search?q=%EC%9E%90%EB%B0%94+%EA%B2%8C%EC%9E%84+%EB%A9%80%ED%8B%B0&ei=YIqMYaXADtHb2roPoKW_-AI&oq=%EC%9E%90%EB%B0%94+%EA%B2%8C%EC%9E%84+%EB%A9%80%ED%8B%B0&gs_lcp=Cgdnd3Mtd2l6EAMyBggAEAgQHjIGCAAQCBAeOggIABCABBCwAzoHCAAQsAMQHjoJCAAQsAMQCBAeOgkIABCwAxAHEB46BAgAEEM6BQgAEIAESgQIQRgBUJs6WJpFYKVHaANwAHgCgAHNAYgBjw-SAQYwLjExLjGYAQCgAQHIAQrAAQE&sclient=gws-wiz&ved=0ahUKEwil8MvTq4_0AhXRrVYBHaDSDy8Q4dUDCA4&uact=5
	}

	public static class ItemBox{//�� �÷��̾ �ϳ��� ������ ������â �ڽ�
		static int maxItem;//�ִ� ������ ����
		static int itemIndex;//������ ��ġ
		List<Items> item;//������ ��ü�� ���� �����ʿ�������. string���� �����Ѵٸ� �����۸����� ����ϴ°ɱ�
		ItemBox(){
			maxItem=3;
			itemIndex=0;
			item = new LinkedList<>();
		}
		ItemBox(int i){
			maxItem=i;
			itemIndex=0;
			item = new LinkedList<>();
		}
		void setmaxItem(int i) {maxItem = i;}
		int getmaxItem() {return maxItem;}
		void setItemIndex(int i) {itemIndex = i;}
		int getItemIndex() {return itemIndex;}

		void useItem(int itemIndex){
//			if(itemIndex==0) {
//
//			}
			item.get(itemIndex).useItem();
		}
		void setItem(itemCode code) {
			item.add(new Items(code));
		}
		void itemType(int itemIndex) {
//			if(item.get(itemIndex).s.scan);
		}
	}


	static enum itemCode{
		LIFE(0),
		SCAN(1),
		REVERSE(2),
		RELOCATION(3);

		private final int value;
		itemCode(int value) {
			// TODO Auto-generated constructor stub
			this.value = value;
		}

		public int getValue() { return value; }
	}
	void test() {
		Items i = new Items(itemCode.SCAN);
	}
	static Robot robot;
	public static class Items{
		int selectItem;
		String selectItemString;
		life l;
		scan s;
		reverse rv;
		relocation rl;

		Items(itemCode code){
			switch(code) {
				case LIFE:
					l = new life();
					break;
				case SCAN:
					s = new scan();
					break;
				case REVERSE:
					rv = new reverse();
					break;
				case RELOCATION:
					rl = new relocation();
					break;
				default:
					selectItem = code.ordinal();
					selectItemString = code.name();
			}
		}
		void useItem() {
			itemCode code = itemCode.valueOf(selectItemString);
			switch(code) {
				case LIFE:
//					l.increase(null);
					break;
				case SCAN:
					s.using(ma.x, ma.y);
					break;
				case REVERSE:
					rv.using();
					break;
				case RELOCATION:
//					rl.using();
					break;
//				default:
			}
		}
		class life {
			void increase(Player p) {
				p.lifeIncrease();
			}
		}
//		class scan extends MouseAdapter {//implements MouseListener, MouseMotionListener

		static class scan {
			//			InGame.MinesAdapter.
			//Ŭ�� �켱���� ���� ����
			//�׷���
//			int x=0;
//            int y=0;
//			public void mouseMoved(MouseEvent e) {
//				scanBox = new Rectangle(e.getX(),e.getY(),CELL_SIZE*3,CELL_SIZE*3);
//            }
			static boolean scan = false;
			static void using(int x, int y) {//Ž�������� ���� ����ȭ �ʿ�//1,4,9..
				scan = true;
				ma.setUsingItem(true);
				int cCol = x/CELL_SIZE;
				int cRow = y/CELL_SIZE;
//            	System.out.println("px"+px);
//            	System.out.println("py"+py);
				scanBox.setBounds((cCol*CELL_SIZE-(CELL_SIZE)),(cRow*CELL_SIZE-(CELL_SIZE)),CELL_SIZE*3,CELL_SIZE*3);

				//if Ŭ��
				int currentField = (cRow * N_COLS) + cCol;// ���� ������ ��
				scanCell(currentField);
			}
			static List<Integer> scanCell(int currentCell) {
				List<Integer> list = new ArrayList<>();
				int currentCol = currentCell % N_COLS;// �Ѱܹ��� ĭ�� ���� ��ġ
				int cell;

				if (currentCol > 0) {// ���� ���� ������ �ִ��� üũ
					cell = currentCell - N_COLS - 1;// ���� ��
					if (cell >= 0) {
						if (field[cell] >= 10 && field[cell] < 20)
							list.add(cell);
					}
					cell = currentCell - 1;// ����
					if (cell >= 0) {
						if (field[cell] >= 10 && field[cell] < 20)
							list.add(cell);
					}
					cell = currentCell + N_COLS - 1;// ���� �Ʒ�
					if (cell < allCells) {
						if (field[cell] >= 10 && field[cell] < 20)
							list.add(cell);
					}
				}

				cell = currentCell - N_COLS;// ��
				if (cell >= 0) {
					if (field[cell] >= 10 && field[cell] < 20)
						list.add(cell);
				}

				cell = currentCell + N_COLS;// �Ʒ�
				if (cell < allCells) {
					if (field[cell] >= 10 && field[cell] < 20)
						list.add(cell);
				}

				if (currentCol < (N_COLS - 1)) {// ������ ���� �������� �ִ��� üũ
					cell = currentCell - N_COLS + 1;// ������ ��
					if (cell >= 0) {
						if (field[cell] >= 10 && field[cell] < 20)
							list.add(cell);
					}

					cell = currentCell + N_COLS + 1;// ������ �Ʒ�
					if (cell < allCells) {
						if (field[cell] >= 10 && field[cell] < 20)
							list.add(cell);
					}

					cell = currentCell + 1;// ������
					if (cell < allCells) {
						if (field[cell] >= 10 && field[cell] < 20)
							list.add(cell);
					}
				}
				return list;
			}

		}
		static class reverse{//���콺 �̵� �ݴ�� �����
			static boolean reverse=false;
			//            Thread time;
//            void setTime() {
//            	time.start();
//            }
			static Point p;
			static boolean firstPoint=false;
			static Timer reverseTime;
			static void using() {
				if(!firstPoint) {
					PointerInfo mouseInfo = MouseInfo.getPointerInfo();
					p = mouseInfo.getLocation();
					firstPoint=true;
					reverse=true;
					reverseTime = new Timer();
					reverseTime.schedule(new TimerTask() {
						@Override
						public void run() {
							reverse=false;
							firstPoint=false;
						}
					}, 10000);
				}
				PointerInfo mouseInfo = MouseInfo.getPointerInfo();
				Point newp = mouseInfo.getLocation();
				newp = new Point(newp.x-p.x,newp.y-p.y);
				try {
					robot = new Robot();

					robot.mouseMove(p.x-newp.x, p.y-newp.y);
					p.x = p.x - newp.x;
					p.y = p.y - newp.y;
					Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
					if(p.x>res.width-5) p.x=res.width-5;
					else if(p.x<5) p.x=5;
					if(p.y>res.height-5) p.y=res.height-5;
					else if(p.y<5) p.y=5;

//					System.out.println("]]"+p.x);
				} catch (AWTException e1) {
					// TODO Auto-generated catch blockW
					e1.printStackTrace();
				}

			}
		}
		class relocation {
			//reverse, random
		}
	}
	int getTimer(){
		return (int)secDiffTime;
	}

}








//scancell 1095
//
//paint 700
//
//item code(enum)
//
//ingame -> player -> itembox - items -> item
//
//ItemBox.setItem(itemCode, ����?)
//
//�׽�Ʈ ������ items static ����

