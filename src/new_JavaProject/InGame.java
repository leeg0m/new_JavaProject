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
//#책갈피(Ctrl f)
//=깃발탐색=
//=빈셀탐색=
//=첫클릭=
//=사운드=
//=마우스이벤트=
//=화면출력=
//=클릭중=
//=키보드이벤트=

	//    private final int NUM_IMAGES = 26;
	private final int NUM_IMAGES = 13;// \Java-Minesweeper-Game-master\Java-Minesweeper-Game-master\src\resources 이미지
	// 파일 12개
//    private final int CELL_SIZE = 15;//셀의 간격
	private static int CELL_SIZE = 30;

	private final int COVER_FOR_CELL = 10;// 커버(비활성화 칸)//커버 있는곳은 10만큼 더해짐
	private final int MARK_FOR_CELL = 10;// 깃발 꽃힌 칸//깃발꽂힌곳은 10만큼 더해짐
	private final int EMPTY_CELL = 0;// 빈칸(활성화된칸)
	private final int MINE_CELL = 9;// 지뢰 셀(칸)
	private final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;// 커버+지뢰 = 커버 씌운 지뢰
	private final int MARKED_MINE_CELL = COVERED_MINE_CELL + MARK_FOR_CELL;// 깃발 꽂힌 지뢰셀 //깃발+커버지뢰 = 깃발(10) + 지뢰(9+10)

	private final int DRAW_MINE = 9;// 지뢰 이미지?
	//  private final int DRAW_COVER = 1;//NUM_IMAGES와 연관있음. 위의 CELL도 NUM_IMAGES에 연관있음.
//  이미지순서 0:눌림 1:1 2:2 3:3 .. 8:8 9:지뢰 10:안눌림 11:깃 12:깃 취소
//	셀값 순서 0:눌림 1:1 2:2 3:3 .. 8:8 9:지뢰 10:안눌림 11:커버(1) 12:커버(2) .. 19:커버지뢰 29:깃발꽂힌지뢰커버 //20:빈셀에 꽂힌 깃발 21:1에꽂힌깃발...28:8에꽂깃
	private final int DRAW_COVER = 10;// 커버
	private final int DRAW_MARK = 11;// 깃발마크
	private final int DRAW_WRONG_MARK = 12;// 깃 취소

	//    private final int N_MINES = 40;//지뢰 개수
	private static int N_MINES = 40;// 지뢰 개수
	//    private final int N_ROWS = 16;//세로칸 수
	private static int N_ROWS = 16;// 세로칸 수
	//    private final int N_COLS = 32;//가로칸 수
//    private final int N_COLS = 16;//가로칸 수
	private static int N_COLS = 16;// 가로칸 수

	//    private final int BOARD_WIDTH = N_COLS * CELL_SIZE + 1;//프레임 넓이
//    private final int BOARD_HEIGHT = N_ROWS * CELL_SIZE + 1;//
	private int BOARD_WIDTH = N_COLS * CELL_SIZE + 1;// 프레임 넓이
	private int BOARD_HEIGHT = N_ROWS * CELL_SIZE + 1;//

	private static int[] field;// 모든 셀(칸) 넘버링
	private boolean inGame;// false:game lost
	private boolean win;
	private int minesLeft;// 좌측하단의 표시되는 현재 남은 지뢰개수를 카운트하는 변수
	private Image[] img;// 이미지 파일

	private static int allCells;// N_ROWS * N_COLS;// = field 개수
	private final JLabel statusbar = new JLabel("testMessage");// 좌측하단의 글씨. ex) 지뢰개수, 승리메시지,패배메시지 등
	FirstClick fc = new FirstClick();
	CreateGame game = new CreateGame();
	private boolean mousePressedCheck = false;
	List<Integer> pressedCellCover = new ArrayList<>();
	List<Integer> scanCellCover = new ArrayList<>();

	long beforeTime;// 시간 측정
	long afterTime;
	long secDiffTime;
	Timer m_timer;
	TimerTask m_task;
	JLabel testLabel;

	Sound sound;
	Sound.soundsEnum soundsEnum;

	static MinesAdapter ma;

	// 클래스 생성자
	public InGame(int n_cols, int n_rows, int n_mines, int cell_size) {
		this.N_COLS = n_cols;
		this.N_ROWS = n_rows;
		this.N_MINES = n_mines;
		this.CELL_SIZE = cell_size;
		BOARD_WIDTH = N_COLS * CELL_SIZE + 1;// 프레임 넓이
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
		setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));// 창 사이즈 지정

		sound = new Sound();
		sound.setSounds();// 사운드 추가

		img = new Image[NUM_IMAGES];// 이미지 로딩
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
//		addKeyListener(new Keys());//키입력받기는 window에 구현해야 동작할거같은데
//		this.requestFocus();
//		this.setFocusable(true);
		game.gameBoard();
//        game.newGame();//핵심코드
	}
//    static ImageIcon imageSetSize(ImageIcon icon, int i, int j) { // image Size Setting
//		Image ximg = icon.getImage();  //ImageIcon을 Image로 변환.
//		Image yimg = ximg.getScaledInstance(i, j, java.awt.Image.SCALE_SMOOTH);
//		ImageIcon xyimg = new ImageIcon(yimg);
//		return xyimg;
//	}

//    private void newGame() {
//
//        int cell;
//
//        var random = new Random();//java.util.Random //지뢰 위치설정할때 사용
//        inGame = true;//게임 상태. true:대기 false:gamelose
//        minesLeft = N_MINES;//남은 개수를 총 개수로 초기화
//
//        allCells = N_ROWS * N_COLS;
//        field = new int[allCells];
//
//        for (int i = 0; i < allCells; i++) {
//
//            field[i] = COVER_FOR_CELL;//모든 칸에 커버(비활성화 빈칸)를 씌움
//        }
//
//        statusbar.setText(Integer.toString(minesLeft));
//
//        int i = 0;
//
//        while (i < N_MINES) {
//
////            int position = (int) (50 * random.nextDouble());
//            int position = (int) (allCells * random.nextDouble());//전체 셀 중에 위치하는 좌표값. ex) position==0 일때 (0,0)에 지뢰가 있음
//
//            if ((position < allCells)
//                    && (field[position] != COVERED_MINE_CELL)) { //지뢰의 위치가 전체칸보다 크지 않으면서 지뢰가있는 필드의 상태가 커버가 씌어진 지뢰칸이 아니면
//
//                int current_col = position % N_COLS;//지뢰의 가로 위치. %연산
//
//                field[position] = COVERED_MINE_CELL;// 지뢰가 있는 필드에 커버(지뢰있음)를 씌움
//                i++;
//
//                if (current_col > 0) {//0보다 큼 = 가장 왼쪽에 위치하지 않음 = 왼쪽 셀이 범위안에 있는지 체크
//                    cell = position - 1 - N_COLS;//지뢰 위치의 왼쪽 위의 칸
////                    System.out.println(cell);
//                    if (cell >= 0) {//0보다 크거나 같음 = 왼쪽 위 셀이 범위 안에 있다.
//                        if (field[cell] != COVERED_MINE_CELL) {//지뢰칸 왼쪽위에 지뢰 없으면
//                            field[cell] += 1;//그자리에 1더함
//                        }
//                    }
//                    cell = position - 1;//지뢰 위치의 왼쪽
//                    if (cell >= 0) {
//                        if (field[cell] != COVERED_MINE_CELL) {
//                            field[cell] += 1;
//                        }
//                    }
//
//                    cell = position + N_COLS - 1;//왼쪽 아래
//                    if (cell < allCells) {
//                        if (field[cell] != COVERED_MINE_CELL) {
//                            field[cell] += 1;
//                        }
//                    }
//                }
//
//                cell = position - N_COLS;//위
//                if (cell >= 0) {
//                    if (field[cell] != COVERED_MINE_CELL) {
//                        field[cell] += 1;
//                    }
//                }
//
//                cell = position + N_COLS;//아래
//                if (cell < allCells) {
//                    if (field[cell] != COVERED_MINE_CELL) {
//                        field[cell] += 1;
//                    }
//                }
//
//                if (current_col < (N_COLS - 1)) {//오른쪽 셀이 범위안에있는지 체크
//                    cell = position - N_COLS + 1;//오른쪽 위
//                    if (cell >= 0) {
//                        if (field[cell] != COVERED_MINE_CELL) {
//                            field[cell] += 1;
//                        }
//                    }
//                    cell = position + N_COLS + 1;//오른쪽 아래
//                    if (cell < allCells) {
//                        if (field[cell] != COVERED_MINE_CELL) {
//                            field[cell] += 1;
//                        }
//                    }
//                    cell = position + 1;//오른쪽
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
//            		position = (int) (allCells * random.nextDouble());//전체 셀 중에 위치하는 좌표값. ex) position==0 일때 (0,0)에 지뢰가 있음

				boolean count = false;// 빈셀에 지뢰가 배치되면 다시 계산
				for (int empty : fc.emptyCell) {
					if (empty == position)
						count = true;
				}
				if (count)
					continue;

				if ((position < allCells) && (field[position] != COVERED_MINE_CELL)) { // 지뢰의 위치가 전체칸보다 크지 않으면서 지뢰가있는
					// 필드의 상태가 커버가 씌어진 지뢰칸이 아니면

					int current_col = position % N_COLS;// 지뢰의 가로 위치. %연산

					field[position] = COVERED_MINE_CELL;// 지뢰가 있는 필드에 커버(지뢰있음)를 씌움
					i++;

					if (current_col > 0) {// 0보다 큼 = 가장 왼쪽에 위치하지 않음 = 왼쪽 셀이 범위안에 있는지 체크
						cell = position - 1 - N_COLS;// 지뢰 위치의 왼쪽 위의 칸
						if (cell >= 0) {// 0보다 크거나 같음 = 왼쪽 위 셀이 범위 안에 있다.
							if (field[cell] != COVERED_MINE_CELL) {// 지뢰칸 왼쪽위에 지뢰 없으면
								field[cell] += 1;// 그자리에 1더함
							}
						}
						cell = position - 1;// 지뢰 위치의 왼쪽
						if (cell >= 0) {
							if (field[cell] != COVERED_MINE_CELL) {
								field[cell] += 1;
							}
						}

						cell = position + N_COLS - 1;// 왼쪽 아래
						if (cell < allCells) {
							if (field[cell] != COVERED_MINE_CELL) {
								field[cell] += 1;
							}
						}
					}

					cell = position - N_COLS;// 위
					if (cell >= 0) {
						if (field[cell] != COVERED_MINE_CELL) {
							field[cell] += 1;
						}
					}

					cell = position + N_COLS;// 아래
					if (cell < allCells) {
						if (field[cell] != COVERED_MINE_CELL) {
							field[cell] += 1;
						}
					}

					if (current_col < (N_COLS - 1)) {// 오른쪽 셀이 범위안에있는지 체크
						cell = position - N_COLS + 1;// 오른쪽 위
						if (cell >= 0) {
							if (field[cell] != COVERED_MINE_CELL) {
								field[cell] += 1;
							}
						}
						cell = position + N_COLS + 1;// 오른쪽 아래
						if (cell < allCells) {
							if (field[cell] != COVERED_MINE_CELL) {
								field[cell] += 1;
							}
						}
						cell = position + 1;// 오른쪽
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
			fc.firstCheck = true;// 첫클릭 상태 초기화

			inGame = true;// 게임 상태. true:대기 false:gamelose
			win = false;
			minesLeft = N_MINES;// 남은 개수를 총 개수로 초기화

			allCells = N_ROWS * N_COLS;
			field = new int[allCells];

			for (int i = 0; i < allCells; i++) {

				field[i] = COVER_FOR_CELL;// 모든 칸에 커버(비활성화 빈칸)를 씌움
			}

		}
	}

	//  =============================빈셀탐색======================
	private void find_empty_cells(int j) {// 빈칸찾기

		int current_col = j % N_COLS;// 넘겨받은 칸의 가로 위치
		int cell;

		if (current_col > 0) {// 왼쪽 셀이 범위에 있는지 체크
			cell = j - N_COLS - 1;// 왼쪽 위
			if (cell >= 0) {
				if (field[cell] > MINE_CELL) {// 왼쪽 위의 셀이 지뢰셀(9)보다 크면(비활성화 빈칸 / 깃발?)
					field[cell] -= COVER_FOR_CELL;// 커버값(10)만큼 뺌 //10-18 => 0-8
					if (field[cell] == EMPTY_CELL) {// 빈칸이면
						find_empty_cells(cell);// 재귀호출 = 왼쪽 위 기준으로 다시 빈셀 탐색
					}
				}
			}

			cell = j - 1;// 왼쪽
			if (cell >= 0) {
				if (field[cell] > MINE_CELL) {
					field[cell] -= COVER_FOR_CELL;
					if (field[cell] == EMPTY_CELL) {
						find_empty_cells(cell);
					}
				}
			}

			cell = j + N_COLS - 1;// 왼쪽 아래
			if (cell < allCells) {
				if (field[cell] > MINE_CELL) {
					field[cell] -= COVER_FOR_CELL;
					if (field[cell] == EMPTY_CELL) {
						find_empty_cells(cell);
					}
				}
			}
		}

		cell = j - N_COLS;// 위
		if (cell >= 0) {
			if (field[cell] > MINE_CELL) {
				field[cell] -= COVER_FOR_CELL;
				if (field[cell] == EMPTY_CELL) {
					find_empty_cells(cell);
				}
			}
		}

		cell = j + N_COLS;// 아래
		if (cell < allCells) {
			if (field[cell] > MINE_CELL) {
				field[cell] -= COVER_FOR_CELL;
				if (field[cell] == EMPTY_CELL) {
					find_empty_cells(cell);
				}
			}
		}

		if (current_col < (N_COLS - 1)) {// 오른쪽 셀이 범위내에 있는지 체크
			cell = j - N_COLS + 1;// 오른쪽 위
			if (cell >= 0) {
				if (field[cell] > MINE_CELL) {
					field[cell] -= COVER_FOR_CELL;
					if (field[cell] == EMPTY_CELL) {
						find_empty_cells(cell);
					}
				}
			}

			cell = j + N_COLS + 1;// 오른쪽 아래
			if (cell < allCells) {
				if (field[cell] > MINE_CELL) {
					field[cell] -= COVER_FOR_CELL;
					if (field[cell] == EMPTY_CELL) {
						find_empty_cells(cell);
					}
				}
			}

			cell = j + 1;// 오른쪽
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

	// ==================================화면출력=========================================
	static Rectangle scanBox = new Rectangle();
	@Override
	public void paintComponent(Graphics g){

		int uncover = 0;// 커버 활성화

		if (inGame) {
			for (int i = 0; i < N_ROWS; i++) {// 세로

				for (int j = 0; j < N_COLS; j++) {// 가로

					int cell = field[(i * N_COLS) + j];

					if (cell == MINE_CELL) {// 게임중이고, 지뢰셀을 누르면

						inGame = false;// 졌다
					}
				}
			}
		}

		for (int i = 0; i < N_ROWS; i++) {// 세로

			for (int j = 0; j < N_COLS; j++) {// 가로
				int cell = field[(i * N_COLS) + j];

				if (!inGame) {// 졌으면

					if (cell == COVERED_MINE_CELL) {// 커버 지뢰는
						cell = DRAW_MINE;// 지뢰를 표시
					} else if (cell == MARKED_MINE_CELL) {// 깃발꽂힌 지뢰 셀은
						cell = DRAW_MARK;// 깃발 이미지 유지
					} else if (cell > COVERED_MINE_CELL) {// 깃발꽃힌 지뢰아닌 셀은
						cell = DRAW_WRONG_MARK;// 깃발 취소 이미지
					} else if (cell > MINE_CELL) {// 지뢰(9)셀 보다 크면(10-18)
						cell = DRAW_COVER;// 커버 이미지
					}

				} else {// 아니면

					if (cell > COVERED_MINE_CELL) {// 커버 지뢰(19) 보다 크면
						cell = DRAW_MARK;// 깃발
					} else if (cell > MINE_CELL) {// 지뢰보다 크면
						cell = DRAW_COVER;// 일반커버
						uncover++;// 비활성화 셀 카운트 증가
					}
//                    if(mousePressedCheck) {
//	                    for(int pcc : pressedCellCover) {
//	                    	if((i*j) == pcc) cell = EMPTY_CELL;
//	                    	System.out.println("iijijijijii"+i*j);
//	                    }
//                    }
				}

				g.drawImage(img[cell], (j * CELL_SIZE), (i * CELL_SIZE), this);// 셀 이미지 갱신

			}
			if (mousePressedCheck) {
				for (int pcc : pressedCellCover) {
					int col = pcc % N_COLS;
					int row = pcc / N_COLS;
					g.drawImage(img[EMPTY_CELL], (col * CELL_SIZE), (row * CELL_SIZE), this);// 셀 이미지 갱신
				}
			}
			if(Items.scan.scan) {
				for(int pcc : scanCellCover) {
//					int col = pcc % N_COLS;
//					int row = pcc / N_COLS;
//					if (cell == COVERED_MINE_CELL) {// 커버 지뢰는
//						cell = DRAW_MINE;// 지뢰를 표시
//					} else if (cell == MARKED_MINE_CELL) {// 깃발꽂힌 지뢰 셀은
//						cell = DRAW_MARK;// 깃발 이미지 유지
//					} else if (cell > COVERED_MINE_CELL) {// 깃발꽃힌 지뢰아닌 셀은
//						cell = DRAW_WRONG_MARK;// 깃발 취소 이미지
//					} else if (cell > MINE_CELL) {// 지뢰(9)셀 보다 크면(10-18)
//						cell = DRAW_COVER;// 커버 이미지
//					}
//					g.drawImage(img[cell], (j * CELL_SIZE), (i * CELL_SIZE), this);// 셀 이미지 갱신
				}
			}
		}
		// 게임 승리
		if (uncover == 0 && inGame) {// 게임중이고 비활성화 칸 없으면
			afterTime = System.nanoTime();// 게임 종료시간
			secDiffTime = (afterTime - beforeTime) / 1_000_000;
			m_task.cancel();
			inGame = false;// 게임종료
			win = true;

//			statusbar.setText("Game won");// 승리
//            sound(soundsFilePath.get(soundsEnum.GAMEWIN.ordinal()));//승리소리

		} else if (!inGame) { // 게임끝
			afterTime = System.nanoTime();// 게임 종료시간
			secDiffTime = (afterTime - beforeTime) / 1_000_000;
			m_task.cancel();
			win = false;
//			statusbar.setText("Game lost");// 패배
//            sound(soundsFilePath.get(soundsEnum.GAMEOVER.ordinal()));//패배소리
		}
		if(ma.getUsingItem()){
			g.setColor(Color.red);  //사각형 테두리 색상을 먼저 설정
			g.drawRect(scanBox.x, scanBox.y, scanBox.width, scanBox.height); //생성된 box의 속성값을 가지고 그려줌
//			System.out.println("!");
		}
	}


	//	//=============================키보드이벤트======================
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
	// =============================마우스이벤트======================
	class MinesAdapter extends MouseAdapter implements MouseMotionListener{

		int x;// 마우스 x좌표
		int y;// 마우스 y좌표
		private boolean usingItem = false;
		void setUsingItem(boolean ui) {usingItem=ui;}
		boolean getUsingItem() {return usingItem;}

		Point point;
		MouseEvent mouseEvent;

		@Override
		public void mousePressed(MouseEvent e) {
			x = e.getX();// 마우스 x좌표
			y = e.getY();// 마우스 y좌표
			int cCol = x / CELL_SIZE;
			int cRow = y / CELL_SIZE;
			int currentField = (cRow * N_COLS) + cCol;// 현재 누르는 셀
			boolean doRepaint = false;
//            mousePressedCheck = true;

			if ((x < N_COLS * CELL_SIZE) && (y < N_ROWS * CELL_SIZE)) {// 15*16=240

				if (e.getButton() == MouseEvent.BUTTON3) {// 우클릭하면

				} else {// 좌클, 휠클 등등

					if ((field[currentField] > MINE_CELL) && (field[currentField] < MARKED_MINE_CELL)) {// 커버있는 셀이면

						if (field[currentField] == MINE_CELL) {// 지뢰였으면
						}

						if (field[currentField] == EMPTY_CELL) {// 빈셀이면
						}

					}
					if (field[currentField] < COVER_FOR_CELL) {// 활성화 셀이면
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

			x = e.getX();// 마우스 x좌표
			y = e.getY();// 마우스 y좌표
//          System.out.println(y);//범위0-240
//          System.out.println(x);//범위0-240

			int cCol = x / CELL_SIZE;
			int cRow = y / CELL_SIZE;
//          System.out.println(cCol);// 범위0-240/15(CELL_SIZE) = 0~15(CELL_SIZE)

			int currentField = (cRow * N_COLS) + cCol;// 현재 누르는 셀
			boolean doRepaint = false;

			if (!inGame) {

				game.gameBoard();// 새게임
				repaint();// 새로그림
			}
//            System.out.println(e.getButton());
//            System.out.println(MouseEvent.BUTTON3);

			if ((x < N_COLS * CELL_SIZE) && (y < N_ROWS * CELL_SIZE)) {// 15*16=240

				if (e.getButton() == MouseEvent.BUTTON3) {// 우클릭하면

					if (field[currentField] > MINE_CELL) {// 눌린 셀이 지뢰(9)보다 크면 = 10-29

						doRepaint = true;

						if (field[currentField] <= COVERED_MINE_CELL) {// 커버지뢰보다 작거나 같으면 = 10-19 = 깃발 안꽂혀있으면

							if (minesLeft > 0) {// 지뢰 카운터가 0보다 클때
								field[currentField] += MARK_FOR_CELL;// 깃발 꽂음
								minesLeft--;// 지뢰 카운터 줄임
								String msg = Integer.toString(minesLeft);
//								statusbar.setText(msg);// 카운터 줄임
//								Sound.SoundPlayer sp = new Sound.SoundPlayer(sound.soundsFilePath.get(soundsEnum.FLAG.ordinal()));
								sound.setPath(sound.soundsFilePath.get(soundsEnum.FLAG.ordinal()));//테스트중
								Thread t = new Thread(sound.sp);
								t.start();
//								sound(sound.soundsFilePath.get(soundsEnum.FLAG.ordinal()));// 깃발소리
							} else {
//                            	sound()//금지
								statusbar.setText("No marks left");// 카운터보다 깃발을 많이 꽂아서 더이상 깃발을 꽂을 수 없음
							}
						} else {// 깃발 꽂혀있으면

							field[currentField] -= MARK_FOR_CELL;// 깃발 제거
							minesLeft++;// 카운터 증가
							String msg = Integer.toString(minesLeft);
//							statusbar.setText(msg);// 증가
//							sound(sound.soundsFilePath.get(sound.soundsEnum.UNFLAG.ordinal()));// 깃발소리
						}
					}

				} else {// 좌클, 휠클 등등
					if (fc.firstCheck) {// 첫클릭이면
//                		beforeTime = System.currentTimeMillis();//게임 시작시간
						beforeTime = System.nanoTime();// 게임 시작시간

						setTimerTask();
						m_timer = new Timer();
						m_timer.schedule(m_task, 0, 1000);

						fc.openFirstCell(inGame, currentField);// 팔방향 빈셀 확보
						game.newGame();// 지뢰 셋팅
//						sound(soundsFilePath.get(soundsEnum.CLICK.ordinal()));// 클릭소리

//						repaint();
//						return;
					}

					if(Items.scan.scan) {
						scanCellCover = Items.scan.scanCell(currentField);
					}
					if (field[currentField] > COVERED_MINE_CELL) {// 깃발꽂혀있으면

//                    	sound()//금지?
						return;// 아무것도안함
					}

					if ((field[currentField] > MINE_CELL) && (field[currentField] < MARKED_MINE_CELL)) {// 커버있는 셀이면

						field[currentField] -= COVER_FOR_CELL;// 커버 제거
						doRepaint = true;

						if (field[currentField] == MINE_CELL) {// 지뢰였으면
//                        	sound(soundsFilePath.get(soundsEnum.GAMEOVER.ordinal()));//패배소리
//                        	sound(soundsFilePath.get(soundsEnum.CLICK.ordinal()));//패배소리
							inGame = false;// 패배
						}

						if (field[currentField] == EMPTY_CELL) {// 빈셀이면
							find_empty_cells(currentField);// 다른 빈셀 탐색
						}

//						if (field[currentField] < MINE_CELL)
//							sound(soundsFilePath.get(soundsEnum.CLICK.ordinal()));// 클릭소리

					}
					if (field[currentField] < COVER_FOR_CELL) {// 활성화 셀이면
						find_mark_cells(currentField);// 깃발비교후 팔방 활성화
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
			if(reverse.reverse) {//스태틱으로 설정되어있음//동작안될것임//테스트용
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

	private class FlagsCount {// 깃발찾아서 카운트 하는데 사용하는 클래스
		int fc = 0;
	}

	// =============================깃발탐색======================
	private void find_mark_cells(int j) {

		int current_col = j % N_COLS;// 넘겨받은 칸의 가로 위치
		int cell;
		FlagsCount flags_count = new FlagsCount();// 깃발 카운터
		int coverMark = COVER_FOR_CELL + MARK_FOR_CELL;// 깃발있음,커버있음

		for (int i = 0; i < 2; i++) {// 첫번째 반복: 깃발 카운팅, 두번째 반복: 팔방위 활성화
			if (current_col > 0) {// 왼쪽 셀이 범위에 있는지 체크
				cell = j - N_COLS - 1;// 왼쪽 위
				if (cell >= 0) {
//	            	if(i==0){//첫번째 반복 : 팔방의 깃발 수 카운트
//	            		if (field[cell] >= coverMark) {//왼쪽 위의 셀이 커버가 있으면서 깃발이 꽂혀있으면
//	                		flags_count++;
//	            		}
//	            	}
//                	else{//두번째 반복 : 현재 셀의 숫자와 깃발카운트가 같으면 팔방 활성화
//                		if(flags_count == field[j]) {
//                			if (field[cell] < coverMark && field[cell]>=COVER_FOR_CELL) {//왼쪽 위의 셀이 커버가 있으면서 깃발이 없으면
//	                			field[cell] -= COVER_FOR_CELL;//커버값(10)만큼 뺌 //10-19 => 0-9
//	                		}
//                		}
//                	}
					find_mark_cells_tool(cell, flags_count, coverMark, i, j);// 위 주석과 동일 내용
				}

				cell = j - 1;// 왼쪽
				if (cell >= 0) {
					find_mark_cells_tool(cell, flags_count, coverMark, i, j);

				}

				cell = j + N_COLS - 1;// 왼쪽 아래
				if (cell < allCells) {
					find_mark_cells_tool(cell, flags_count, coverMark, i, j);
				}
			}

			cell = j - N_COLS;// 위
			if (cell >= 0) {
				find_mark_cells_tool(cell, flags_count, coverMark, i, j);
			}

			cell = j + N_COLS;// 아래
			if (cell < allCells) {
				find_mark_cells_tool(cell, flags_count, coverMark, i, j);
			}

			if (current_col < (N_COLS - 1)) {// 오른쪽 셀이 범위내에 있는지 체크
				cell = j - N_COLS + 1;// 오른쪽 위
				if (cell >= 0) {
					find_mark_cells_tool(cell, flags_count, coverMark, i, j);
				}

				cell = j + N_COLS + 1;// 오른쪽 아래
				if (cell < allCells) {
					find_mark_cells_tool(cell, flags_count, coverMark, i, j);
				}

				cell = j + 1;// 오른쪽
				if (cell < allCells) {
					find_mark_cells_tool(cell, flags_count, coverMark, i, j);
				}
			}
			if (flags_count.fc != field[j])
				break;// 깃발수가 현재셀숫자와 다르면 다음동작없음
			if (i == 1) {
//				if (inGame)
//					sound(soundsFilePath.get(soundsEnum.CLICK.ordinal()));// 클릭소리
//	        	else
//	        		sound();//
			}
		}
	}

	private void find_mark_cells_tool(int cell, FlagsCount flags_count, int coverMark, int i, int j) {
		if (i == 0) {// 첫번째 반복 : 팔방의 깃발 수 카운트
			if (field[cell] >= coverMark) {// 셀이 커버가 있으면서 깃발이 꽂혀있으면
				flags_count.fc++;
			}
		} else {// 두번째 반복 : 현재 셀의 숫자와 깃발카운트가 같으면 팔방 활성화
			if (flags_count.fc == field[j]) {
				if (field[cell] < coverMark && field[cell] >= COVER_FOR_CELL) {// 왼쪽 위의 셀이 커버가 있으면서 깃발이 없으면
					field[cell] -= COVER_FOR_CELL;// 커버값(10)만큼 뺌 //10-19 => 0-9
				}
				if (field[cell] == EMPTY_CELL) {
					find_empty_cells(cell);
				}
			}
		}
	}

	// =============================첫클릭======================
	private class FirstClick {
		boolean firstCheck = true;
		List<Integer> emptyCell = new ArrayList<>();// 빈셀 좌표 저장

		void openFirstCell(boolean inGame, int currentCell) {
			if (inGame) {
				if (firstCheck) {
					firstCheck = false;
					int currentCol = currentCell % N_COLS;// 넘겨받은 칸의 가로 위치
					int cell;

					field[currentCell] = EMPTY_CELL;// 현재칸 빈셀
					emptyCell.add(currentCell);
					if (currentCol > 0) {// 왼쪽 셀이 범위에 있는지 체크
						cell = currentCell - N_COLS - 1;// 왼쪽 위
						if (cell >= 0) {
							field[cell] = EMPTY_CELL;
							emptyCell.add(cell);
						}
						cell = currentCell - 1;// 왼쪽
						if (cell >= 0) {
							field[cell] = EMPTY_CELL;
							emptyCell.add(cell);
						}
						cell = currentCell + N_COLS - 1;// 왼쪽 아래
						if (cell < allCells) {
							field[cell] = EMPTY_CELL;
							emptyCell.add(cell);
						}
					}

					cell = currentCell - N_COLS;// 위
					if (cell >= 0) {
						field[cell] = EMPTY_CELL;
						emptyCell.add(cell);
					}

					cell = currentCell + N_COLS;// 아래
					if (cell < allCells) {
						field[cell] = EMPTY_CELL;
						emptyCell.add(cell);
					}

					if (currentCol < (N_COLS - 1)) {// 오른쪽 셀이 범위내에 있는지 체크
						cell = currentCell - N_COLS + 1;// 오른쪽 위
						if (cell >= 0) {
							field[cell] = EMPTY_CELL;
							emptyCell.add(cell);
						}

						cell = currentCell + N_COLS + 1;// 오른쪽 아래
						if (cell < allCells) {
							field[cell] = EMPTY_CELL;
							emptyCell.add(cell);
						}

						cell = currentCell + 1;// 오른쪽
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

	// ==================================클릭중=========================================
	List<Integer> mousePressedCell(boolean inGame, int currentCell) {// 클릭중에 비활성셀이 있으면 임시로 활성표시
		List<Integer> list = new ArrayList<>();
		if (inGame) {
			int currentCol = currentCell % N_COLS;// 넘겨받은 칸의 가로 위치
			int cell;

			if (currentCol > 0) {// 왼쪽 셀이 범위에 있는지 체크
				cell = currentCell - N_COLS - 1;// 왼쪽 위
				if (cell >= 0) {
					if (field[cell] >= 10 && field[cell] < 20)
						list.add(cell);
				}
				cell = currentCell - 1;// 왼쪽
				if (cell >= 0) {
					if (field[cell] >= 10 && field[cell] < 20)
						list.add(cell);
				}
				cell = currentCell + N_COLS - 1;// 왼쪽 아래
				if (cell < allCells) {
					if (field[cell] >= 10 && field[cell] < 20)
						list.add(cell);
				}
			}

			cell = currentCell - N_COLS;// 위
			if (cell >= 0) {
				if (field[cell] >= 10 && field[cell] < 20)
					list.add(cell);
			}

			cell = currentCell + N_COLS;// 아래
			if (cell < allCells) {
				if (field[cell] >= 10 && field[cell] < 20)
					list.add(cell);
			}

			if (currentCol < (N_COLS - 1)) {// 오른쪽 셀이 범위내에 있는지 체크
				cell = currentCell - N_COLS + 1;// 오른쪽 위
				if (cell >= 0) {
					if (field[cell] >= 10 && field[cell] < 20)
						list.add(cell);
				}

				cell = currentCell + N_COLS + 1;// 오른쪽 아래
				if (cell < allCells) {
					if (field[cell] >= 10 && field[cell] < 20)
						list.add(cell);
				}

				cell = currentCell + 1;// 오른쪽
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
					secDiffTime = (afterTime - beforeTime) / 1_000_000;//정수? 실수?
					System.out.printf("%.2f\n", (afterTime - beforeTime) / 1_000_000.);//나노초 -> 초 변환
				}
			};
		} catch (Exception e) {

		}

	}




	//====UserInterface======
	public static class Player extends ItemBox{//생성된 게임이 관여할수있는 플레이어 설정. 어쩌면 새로운 프로젝트로 따로 관리해야할지도
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
		//멀티구현 공부 필요
		//일단 참조자료 : https://creativeprm.tistory.com/320
		//https://www.google.com/search?q=%EC%9E%90%EB%B0%94+%EA%B2%8C%EC%9E%84+%EB%A9%80%ED%8B%B0&ei=YIqMYaXADtHb2roPoKW_-AI&oq=%EC%9E%90%EB%B0%94+%EA%B2%8C%EC%9E%84+%EB%A9%80%ED%8B%B0&gs_lcp=Cgdnd3Mtd2l6EAMyBggAEAgQHjIGCAAQCBAeOggIABCABBCwAzoHCAAQsAMQHjoJCAAQsAMQCBAeOgkIABCwAxAHEB46BAgAEEM6BQgAEIAESgQIQRgBUJs6WJpFYKVHaANwAHgCgAHNAYgBjw-SAQYwLjExLjGYAQCgAQHIAQrAAQE&sclient=gws-wiz&ved=0ahUKEwil8MvTq4_0AhXRrVYBHaDSDy8Q4dUDCA4&uact=5
	}

	public static class ItemBox{//각 플레이어가 하나씩 가지는 아이템창 박스
		static int maxItem;//최대 아이템 개수
		static int itemIndex;//아이템 위치
		List<Items> item;//아이템 객체를 따로 만들필요있을듯. string으로 구현한다면 아이템명으로 사용하는걸까
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
			//클릭 우선순위 높게 설정
			//그래픽
//			int x=0;
//            int y=0;
//			public void mouseMoved(MouseEvent e) {
//				scanBox = new Rectangle(e.getX(),e.getY(),CELL_SIZE*3,CELL_SIZE*3);
//            }
			static boolean scan = false;
			static void using(int x, int y) {//탐색가능한 범위 세분화 필요//1,4,9..
				scan = true;
				ma.setUsingItem(true);
				int cCol = x/CELL_SIZE;
				int cRow = y/CELL_SIZE;
//            	System.out.println("px"+px);
//            	System.out.println("py"+py);
				scanBox.setBounds((cCol*CELL_SIZE-(CELL_SIZE)),(cRow*CELL_SIZE-(CELL_SIZE)),CELL_SIZE*3,CELL_SIZE*3);

				//if 클릭
				int currentField = (cRow * N_COLS) + cCol;// 현재 누르는 셀
				scanCell(currentField);
			}
			static List<Integer> scanCell(int currentCell) {
				List<Integer> list = new ArrayList<>();
				int currentCol = currentCell % N_COLS;// 넘겨받은 칸의 가로 위치
				int cell;

				if (currentCol > 0) {// 왼쪽 셀이 범위에 있는지 체크
					cell = currentCell - N_COLS - 1;// 왼쪽 위
					if (cell >= 0) {
						if (field[cell] >= 10 && field[cell] < 20)
							list.add(cell);
					}
					cell = currentCell - 1;// 왼쪽
					if (cell >= 0) {
						if (field[cell] >= 10 && field[cell] < 20)
							list.add(cell);
					}
					cell = currentCell + N_COLS - 1;// 왼쪽 아래
					if (cell < allCells) {
						if (field[cell] >= 10 && field[cell] < 20)
							list.add(cell);
					}
				}

				cell = currentCell - N_COLS;// 위
				if (cell >= 0) {
					if (field[cell] >= 10 && field[cell] < 20)
						list.add(cell);
				}

				cell = currentCell + N_COLS;// 아래
				if (cell < allCells) {
					if (field[cell] >= 10 && field[cell] < 20)
						list.add(cell);
				}

				if (currentCol < (N_COLS - 1)) {// 오른쪽 셀이 범위내에 있는지 체크
					cell = currentCell - N_COLS + 1;// 오른쪽 위
					if (cell >= 0) {
						if (field[cell] >= 10 && field[cell] < 20)
							list.add(cell);
					}

					cell = currentCell + N_COLS + 1;// 오른쪽 아래
					if (cell < allCells) {
						if (field[cell] >= 10 && field[cell] < 20)
							list.add(cell);
					}

					cell = currentCell + 1;// 오른쪽
					if (cell < allCells) {
						if (field[cell] >= 10 && field[cell] < 20)
							list.add(cell);
					}
				}
				return list;
			}

		}
		static class reverse{//마우스 이동 반대로 만들기
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
//ItemBox.setItem(itemCode, 유저?)
//
//테스트 끝나면 items static 제거

