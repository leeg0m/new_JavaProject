package new_JavaProject;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InGame extends JPanel {
//#책갈피(Ctrl f)
//=깃발탐색=
//=빈셀탐색=
//=첫클릭=
//=사운드=
//=마우스이벤트=
//=화면출력=
//=클릭중=
	
//    private final int NUM_IMAGES = 26;
    private final int NUM_IMAGES = 13;// \Java-Minesweeper-Game-master\Java-Minesweeper-Game-master\src\resources 이미지 파일 12개 
//    private final int CELL_SIZE = 15;//셀의 간격
    private int CELL_SIZE = 30;

    private final int COVER_FOR_CELL = 10;//커버(비활성화 칸)//커버 있는곳은 10만큼 더해짐
    private final int MARK_FOR_CELL = 10;//깃발 꽃힌 칸//깃발꽂힌곳은 10만큼 더해짐
    private final int EMPTY_CELL = 0;//빈칸(활성화된칸)
    private final int MINE_CELL = 9;//지뢰 셀(칸)
    private final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;//커버+지뢰 = 커버 씌운 지뢰
    private final int MARKED_MINE_CELL = COVERED_MINE_CELL + MARK_FOR_CELL;//깃발 꽂힌 지뢰셀 //깃발+커버지뢰 = 깃발(10) + 지뢰(9+10)

    private final int DRAW_MINE = 9;//지뢰 이미지?
//  private final int DRAW_COVER = 1;//NUM_IMAGES와 연관있음. 위의 CELL도 NUM_IMAGES에 연관있음.
//  이미지순서 0:눌림 1:1 2:2 3:3 .. 8:8 9:지뢰 10:안눌림 11:깃 12:깃 취소 
//	셀값 순서 0:눌림 1:1 2:2 3:3 .. 8:8 9:지뢰 10:안눌림 11:커버(1) 12:커버(2) .. 19:커버지뢰 29:깃발꽂힌지뢰커버 //20:빈셀에 꽂힌 깃발 21:1에꽂힌깃발...28:8에꽂깃
    private final int DRAW_COVER = 10;//커버
    private final int DRAW_MARK = 11;//깃발마크
    private final int DRAW_WRONG_MARK = 12;//깃 취소

//    private final int N_MINES = 40;//지뢰 개수
    private int N_MINES = 40;//지뢰 개수
//    private final int N_ROWS = 16;//세로칸 수
    private int N_ROWS = 16;//세로칸 수
//    private final int N_COLS = 32;//가로칸 수
//    private final int N_COLS = 16;//가로칸 수
    private int N_COLS = 16;//가로칸 수

//    private final int BOARD_WIDTH = N_COLS * CELL_SIZE + 1;//프레임 넓이
//    private final int BOARD_HEIGHT = N_ROWS * CELL_SIZE + 1;//
    private int BOARD_WIDTH = N_COLS * CELL_SIZE + 1;//프레임 넓이
    private int BOARD_HEIGHT = N_ROWS * CELL_SIZE + 1;//

    private int[] field;//모든 셀(칸) 넘버링
    private boolean inGame;//false:game lost
    private int minesLeft;//좌측하단의 표시되는 현재 남은 지뢰개수를 카운트하는 변수
    private Image[] img;//이미지 파일

    private int allCells;//N_ROWS * N_COLS;// = field 개수
    private final JLabel statusbar=new JLabel("testMessage");//좌측하단의 글씨. ex) 지뢰개수, 승리메시지,패배메시지 등 
	FirstClick fc = new FirstClick();
	CreateGame game = new CreateGame();
	private boolean mousePressedCheck=false;
	List<Integer> pressedCellCover = new ArrayList<>();

//    public InGame(JLabel statusbar) {
	public InGame(int n_cols, int n_rows, int n_mines, int cell_size) {
		this.N_COLS=n_cols;
		this.N_ROWS=n_rows;
		this.N_MINES=n_mines;
		this.CELL_SIZE=cell_size;
		BOARD_WIDTH = N_COLS * CELL_SIZE + 1;//프레임 넓이
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
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));//창 사이즈 지정

        setSounds();//사운드 추가
        
        img = new Image[NUM_IMAGES];//이미지 로딩
        for (int i = 0; i < NUM_IMAGES; i++) {

            var path = "./images/inGame/resources/" + i + ".png";
            ImageIcon icon = new ImageIcon(path);
//            img[i] = (new ImageIcon(path)).getImage();
            img[i] = icon.getImage();
            img[i] = img[i].getScaledInstance(CELL_SIZE,CELL_SIZE,Image.SCALE_SMOOTH);
            
            ImageIcon changeIcon = new ImageIcon(img[i]);
//            img[i] = imageSetSize(img[i],30,30);
            
        }

        addMouseListener(new MinesAdapter());
        game.gameBoard();
//        newGame();//핵심코드
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
    
    private class CreateGame{
    	int cell;

    	
    	void newGame() {
//    		gameBoard();
    		
    		int i = 0;
    		var random = new Random();
    		int position;

            while (i < N_MINES) {

            	position = (int) (allCells * random.nextDouble());
//            		position = (int) (allCells * random.nextDouble());//전체 셀 중에 위치하는 좌표값. ex) position==0 일때 (0,0)에 지뢰가 있음
            	
            	boolean count=false;//빈셀에 지뢰가 배치되면 다시 계산
            	for(int empty : fc.emptyCell) {
            		if(empty == position)
            			count=true;
            	}
            	if(count)
            		continue;
            	
                if ((position < allCells)
                        && (field[position] != COVERED_MINE_CELL)) { //지뢰의 위치가 전체칸보다 크지 않으면서 지뢰가있는 필드의 상태가 커버가 씌어진 지뢰칸이 아니면

                    int current_col = position % N_COLS;//지뢰의 가로 위치. %연산
                    
                    field[position] = COVERED_MINE_CELL;// 지뢰가 있는 필드에 커버(지뢰있음)를 씌움
                    i++;

                    if (current_col > 0) {//0보다 큼 = 가장 왼쪽에 위치하지 않음 = 왼쪽 셀이 범위안에 있는지 체크
                        cell = position - 1 - N_COLS;//지뢰 위치의 왼쪽 위의 칸
                        if (cell >= 0) {//0보다 크거나 같음 = 왼쪽 위 셀이 범위 안에 있다.
                            if (field[cell] != COVERED_MINE_CELL) {//지뢰칸 왼쪽위에 지뢰 없으면
                                field[cell] += 1;//그자리에 1더함
                            }
                        }
                        cell = position - 1;//지뢰 위치의 왼쪽
                        if (cell >= 0) {
                            if (field[cell] != COVERED_MINE_CELL) {
                                field[cell] += 1;
                            }
                        }

                        cell = position + N_COLS - 1;//왼쪽 아래
                        if (cell < allCells) {
                            if (field[cell] != COVERED_MINE_CELL) {
                                field[cell] += 1;
                            }
                        }
                    }

                    cell = position - N_COLS;//위
                    if (cell >= 0) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }

                    cell = position + N_COLS;//아래
                    if (cell < allCells) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }

                    if (current_col < (N_COLS - 1)) {//오른쪽 셀이 범위안에있는지 체크
                        cell = position - N_COLS + 1;//오른쪽 위
                        if (cell >= 0) {
                            if (field[cell] != COVERED_MINE_CELL) {
                                field[cell] += 1;
                            }
                        }
                        cell = position + N_COLS + 1;//오른쪽 아래
                        if (cell < allCells) {
                            if (field[cell] != COVERED_MINE_CELL) {
                                field[cell] += 1;
                            }
                        }
                        cell = position + 1;//오른쪽
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
    		fc.firstCheck=true;//첫클릭 상태 초기화
    		
            inGame = true;//게임 상태. true:대기 false:gamelose
            minesLeft = N_MINES;//남은 개수를 총 개수로 초기화

            allCells = N_ROWS * N_COLS;
            field = new int[allCells];

            for (int i = 0; i < allCells; i++) {

                field[i] = COVER_FOR_CELL;//모든 칸에 커버(비활성화 빈칸)를 씌움
            }

        }
    }

//  =============================빈셀탐색======================
    private void find_empty_cells(int j) {//빈칸찾기

        int current_col = j % N_COLS;//넘겨받은 칸의 가로 위치
        int cell;

        if (current_col > 0) {//왼쪽 셀이 범위에 있는지 체크
            cell = j - N_COLS - 1;//왼쪽 위
            if (cell >= 0) {
                if (field[cell] > MINE_CELL) {//왼쪽 위의 셀이 지뢰셀(9)보다 크면(비활성화 빈칸 / 깃발?)
                    field[cell] -= COVER_FOR_CELL;//커버값(10)만큼 뺌 //10-18 => 0-8
                    if (field[cell] == EMPTY_CELL) {//빈칸이면
                        find_empty_cells(cell);//재귀호출 = 왼쪽 위 기준으로 다시 빈셀 탐색
                    }
                }
            }

            cell = j - 1;//왼쪽
            if (cell >= 0) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j + N_COLS - 1;//왼쪽 아래
            if (cell < allCells) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }
        }

        cell = j - N_COLS;//위
        if (cell >= 0) {
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL) {
                    find_empty_cells(cell);
                }
            }
        }

        cell = j + N_COLS;//아래
        if (cell < allCells) {
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL) {
                    find_empty_cells(cell);
                }
            }
        }

        if (current_col < (N_COLS - 1)) {//오른쪽 셀이 범위내에 있는지 체크
            cell = j - N_COLS + 1;//오른쪽 위
            if (cell >= 0) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j + N_COLS + 1;//오른쪽 아래
            if (cell < allCells) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j + 1;//오른쪽
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

    //==================================화면출력=========================================
    @Override
    public void paintComponent(Graphics g) {

        int uncover = 0;//커버 활성화

        for (int i = 0; i < N_ROWS; i++) {//세로

            for (int j = 0; j < N_COLS; j++) {//가로

                int cell = field[(i * N_COLS) + j];

                if (inGame && cell == MINE_CELL) {//게임중이고, 지뢰셀을 누르면

                    inGame = false;//졌다
                }
            }
        }
        for (int i = 0; i < N_ROWS; i++) {//세로

            for (int j = 0; j < N_COLS; j++) {//가로
            	int cell = field[(i * N_COLS) + j];

                if (!inGame) {//졌으면

                    if (cell == COVERED_MINE_CELL) {//커버 지뢰는
                        cell = DRAW_MINE;//지뢰를 표시
                    } else if (cell == MARKED_MINE_CELL) {//깃발꽂힌 지뢰 셀은
                        cell = DRAW_MARK;//깃발 이미지 유지
                    } else if (cell > COVERED_MINE_CELL) {//깃발꽃힌 지뢰아닌 셀은
                        cell = DRAW_WRONG_MARK;//깃발 취소 이미지
                    } else if (cell > MINE_CELL) {//지뢰(9)셀 보다 크면(10-18)
                        cell = DRAW_COVER;//커버 이미지
                    }

                } else {//아니면

                    if (cell > COVERED_MINE_CELL) {//커버 지뢰(19) 보다 크면
                        cell = DRAW_MARK;//깃발
                    } else if (cell > MINE_CELL) {//지뢰보다 크면
                        cell = DRAW_COVER;//일반커버
                        uncover++;//비활성화 셀 카운트 증가
                    }
//                    if(mousePressedCheck) {
//	                    for(int pcc : pressedCellCover) {
//	                    	if((i*j) == pcc) cell = EMPTY_CELL;
//	                    	System.out.println("iijijijijii"+i*j);
//	                    }
//                    }
                }

                g.drawImage(img[cell], (j * CELL_SIZE),
                        (i * CELL_SIZE), this);//셀 이미지 갱신
                
            }
            if(mousePressedCheck) {
            	for(int pcc : pressedCellCover) {
            		int col = pcc % N_COLS;
            		int row = pcc / N_COLS;
            		g.drawImage(img[EMPTY_CELL], (col * CELL_SIZE),
            				(row * CELL_SIZE), this);//셀 이미지 갱신
            	}
        	}
        }

        if (uncover == 0 && inGame) {//게임중이고 비활성화 칸 없으면

            inGame = false;//게임종료
            statusbar.setText("Game won");//승리
//            sound(soundsFilePath.get(soundsEnum.GAMEWIN.ordinal()));//승리소리

        } else if (!inGame) {//게임끝
            statusbar.setText("Game lost");//졌다
//            sound(soundsFilePath.get(soundsEnum.GAMEOVER.ordinal()));//패배소리
        }
    }
  //=============================마우스이벤트======================
    private class MinesAdapter extends MouseAdapter {

    	 int x;//마우스 x좌표
         int y;//마우스 y좌표

    	
    	@Override
    	public void mousePressed(MouseEvent e) {
    		x = e.getX();//마우스 x좌표
    		y = e.getY();//마우스 y좌표
    		int cCol = x / CELL_SIZE;
            int cRow = y / CELL_SIZE;
            int currentField=(cRow * N_COLS) + cCol;//현재 누르는 셀
            boolean doRepaint = false;
//            mousePressedCheck = true;
            
    		if ((x < N_COLS * CELL_SIZE) && (y < N_ROWS * CELL_SIZE)) {//15*16=240

                if (e.getButton() == MouseEvent.BUTTON3) {//우클릭하면

                } else {//좌클, 휠클 등등


                    if ((field[currentField] > MINE_CELL)
                            && (field[currentField] < MARKED_MINE_CELL)) {//커버있는 셀이면


                        if (field[currentField] == MINE_CELL) {//지뢰였으면
                        }

                        if (field[currentField] == EMPTY_CELL) {//빈셀이면
                        }
                        
                        
                    }
                    if (field[currentField] < COVER_FOR_CELL) {//활성화 셀이면
                    	pressedCellCover=mousePressedCell(inGame, currentField);
                    	mousePressedCheck = true;
                    	doRepaint=true;
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

        	x = e.getX();//마우스 x좌표
    		y = e.getY();//마우스 y좌표
//          System.out.println(y);//범위0-240
//          System.out.println(x);//범위0-240

          int cCol = x / CELL_SIZE;
          int cRow = y / CELL_SIZE;
//          System.out.println(cCol);// 범위0-240/15(CELL_SIZE) = 0~15(CELL_SIZE)

          int currentField=(cRow * N_COLS) + cCol;//현재 누르는 셀
          boolean doRepaint = false;

            if (!inGame) {

                game.gameBoard();//새게임
                repaint();//새로그림
            }
//            System.out.println(e.getButton());
//            System.out.println(MouseEvent.BUTTON3);

            if ((x < N_COLS * CELL_SIZE) && (y < N_ROWS * CELL_SIZE)) {//15*16=240

                if (e.getButton() == MouseEvent.BUTTON3) {//우클릭하면

                    if (field[currentField] > MINE_CELL) {//눌린 셀이 지뢰(9)보다 크면 = 10-29

                        doRepaint = true;

                        if (field[currentField] <= COVERED_MINE_CELL) {//커버지뢰보다 작거나 같으면 = 10-19 = 깃발 안꽂혀있으면

                            if (minesLeft > 0) {//지뢰 카운터가 0보다 클때
                                field[currentField] += MARK_FOR_CELL;//깃발 꽂음
                                minesLeft--;//지뢰 카운터 줄임
                                String msg = Integer.toString(minesLeft);
                                statusbar.setText(msg);//카운터 줄임
                                sound(soundsFilePath.get(soundsEnum.FLAG.ordinal()));//깃발소리
                            } else {
//                            	sound()//금지
                                statusbar.setText("No marks left");//카운터보다 깃발을 많이 꽂아서 더이상 깃발을 꽂을 수 없음
                            }
                        } else {//깃발 꽂혀있으면

                            field[currentField] -= MARK_FOR_CELL;//깃발 제거
                            minesLeft++;//카운터 증가
                            String msg = Integer.toString(minesLeft);
                            statusbar.setText(msg);//증가
                            sound(soundsFilePath.get(soundsEnum.UNFLAG.ordinal()));//깃발소리
                        }
                    }

                } else {//좌클, 휠클 등등
                	if(fc.firstCheck) {//첫클릭이면
                		fc.openFirstCell(inGame, currentField);//팔방향 빈셀 확보
                		game.newGame();//지뢰 셋팅
                		sound(soundsFilePath.get(soundsEnum.CLICK.ordinal()));//클릭소리
                	}

                    if (field[currentField] > COVERED_MINE_CELL) {//깃발꽂혀있으면

//                    	sound()//금지?
                        return;//아무것도안함
                    }

                    if ((field[currentField] > MINE_CELL)
                            && (field[currentField] < MARKED_MINE_CELL)) {//커버있는 셀이면

                        field[currentField] -= COVER_FOR_CELL;//커버 제거
                        doRepaint = true;

                        if (field[currentField] == MINE_CELL) {//지뢰였으면
//                        	sound(soundsFilePath.get(soundsEnum.GAMEOVER.ordinal()));//패배소리
//                        	sound(soundsFilePath.get(soundsEnum.CLICK.ordinal()));//패배소리
                            inGame = false;//패배
                        }

                        if (field[currentField] == EMPTY_CELL) {//빈셀이면
                            find_empty_cells(currentField);//다른 빈셀 탐색
                        }
                        
                        if (field[currentField] < MINE_CELL) 
                        	sound(soundsFilePath.get(soundsEnum.CLICK.ordinal()));//클릭소리
                        
                    }
                    if (field[currentField] < COVER_FOR_CELL) {//활성화 셀이면
                        find_mark_cells(currentField);//깃발비교후 팔방 활성화
                        doRepaint = true;
                    }
                }

                if (doRepaint) {
                    repaint();
                }
            }
        }
    }
    
    private class FlagsCount{//깃발찾아서 카운트 하는데 사용하는 클래스
    	int fc=0;
    }
    //=============================깃발탐색======================
    private void find_mark_cells(int j) {

        int current_col = j % N_COLS;//넘겨받은 칸의 가로 위치
        int cell;
        FlagsCount  flags_count = new FlagsCount();//깃발 카운터
        int coverMark=COVER_FOR_CELL+MARK_FOR_CELL;//깃발있음,커버있음

        for(int i=0;i<2;i++) {//첫번째 반복: 깃발 카운팅, 두번째 반복: 팔방위 활성화
	        if (current_col > 0) {//왼쪽 셀이 범위에 있는지 체크
	            cell = j - N_COLS - 1;//왼쪽 위
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
	            	find_mark_cells_tool(cell,flags_count,coverMark,i,j);//위 주석과 동일 내용
	            }
	            
	
	            cell = j - 1;//왼쪽
	            if (cell >= 0) {
	            	find_mark_cells_tool(cell,flags_count,coverMark,i,j);
	                
	            }
	            
	            cell = j + N_COLS - 1;//왼쪽 아래
	            if (cell < allCells) {
	            	find_mark_cells_tool(cell,flags_count,coverMark,i,j);
	            }
	        }
	
	        cell = j - N_COLS;//위
	        if (cell >= 0) {
	        	find_mark_cells_tool(cell,flags_count,coverMark,i,j);
	        }
	
	        cell = j + N_COLS;//아래
	        if (cell < allCells) {
	        	find_mark_cells_tool(cell,flags_count,coverMark,i,j);
	        }
	
	        if (current_col < (N_COLS - 1)) {//오른쪽 셀이 범위내에 있는지 체크
	            cell = j - N_COLS + 1;//오른쪽 위
	            if (cell >= 0) {
	            	find_mark_cells_tool(cell,flags_count,coverMark,i,j);
	            }
	
	            cell = j + N_COLS + 1;//오른쪽 아래
	            if (cell < allCells) {
	            	find_mark_cells_tool(cell,flags_count,coverMark,i,j);
	            }
	
	            cell = j + 1;//오른쪽
	            if (cell < allCells) {
	            	find_mark_cells_tool(cell,flags_count,coverMark,i,j);
	            }
	        }
	        if(flags_count.fc != field[j]) break;//깃발수가 현재셀숫자와 다르면 다음동작없음
	        if(i==1) {
	        	if(inGame)
	        		sound(soundsFilePath.get(soundsEnum.CLICK.ordinal()));//클릭소리
//	        	else
//	        		sound();//
	        }
        }
    }
    
    private void find_mark_cells_tool(int cell, FlagsCount flags_count, int coverMark, int i, int j) {
    	if(i==0){//첫번째 반복 : 팔방의 깃발 수 카운트
    		if (field[cell] >= coverMark) {//셀이 커버가 있으면서 깃발이 꽂혀있으면
        		flags_count.fc++;
    		}
    	}
    	else{//두번째 반복 : 현재 셀의 숫자와 깃발카운트가 같으면 팔방 활성화
    		if(flags_count.fc == field[j]) {
    			if (field[cell] < coverMark && field[cell]>=COVER_FOR_CELL) {//왼쪽 위의 셀이 커버가 있으면서 깃발이 없으면
        			field[cell] -= COVER_FOR_CELL;//커버값(10)만큼 뺌 //10-19 => 0-9
        		}
    			if (field[cell] == EMPTY_CELL) {
                    find_empty_cells(cell);
                }
    		}
    	}
    }
    
    
  //=============================첫클릭======================
    private class FirstClick{
    	boolean firstCheck=true;
    	List<Integer> emptyCell = new ArrayList<>();//빈셀 좌표 저장
    	
    	void openFirstCell(boolean inGame, int currentCell){
    		if(inGame) {
    			if(firstCheck) {
    				firstCheck=false;
    				int currentCol = currentCell % N_COLS;//넘겨받은 칸의 가로 위치
    		        int cell;
    		        
    		        field[currentCell]=EMPTY_CELL;//현재칸 빈셀
	            	emptyCell.add(currentCell);
    		        if (currentCol > 0) {//왼쪽 셀이 범위에 있는지 체크
    		            cell = currentCell - N_COLS - 1;//왼쪽 위
    		            if (cell >= 0) {
    		            	field[cell]=EMPTY_CELL;
    		            	emptyCell.add(cell);
    		            }
    		            cell = currentCell - 1;//왼쪽
    		            if (cell >= 0) {
    		            	field[cell]=EMPTY_CELL;
    		            	emptyCell.add(cell);
    		            }
    		            cell = currentCell + N_COLS - 1;//왼쪽 아래
    		            if (cell < allCells) {
    		            	field[cell]=EMPTY_CELL;
    		            	emptyCell.add(cell);
    		            }
    		        }
    		
    		        cell = currentCell - N_COLS;//위
    		        if (cell >= 0) {
    		        	field[cell]=EMPTY_CELL;
    		        	emptyCell.add(cell);
    		        }
    		
    		        cell = currentCell + N_COLS;//아래
    		        if (cell < allCells) {
    		        	field[cell]=EMPTY_CELL;
    		        	emptyCell.add(cell);
    		        }
    		
    		        if (currentCol < (N_COLS - 1)) {//오른쪽 셀이 범위내에 있는지 체크
    		            cell = currentCell - N_COLS + 1;//오른쪽 위
    		            if (cell >= 0) {
    		            	field[cell]=EMPTY_CELL;
    		            	emptyCell.add(cell);
    		            }
    		
    		            cell = currentCell + N_COLS + 1;//오른쪽 아래
    		            if (cell < allCells) {
    		            	field[cell]=EMPTY_CELL;
    		            	emptyCell.add(cell);
    		            }
    		
    		            cell = currentCell + 1;//오른쪽
    		            if (cell < allCells) {
    		            	field[cell]=EMPTY_CELL;
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
    
    //==================================클릭중=========================================
    List<Integer> mousePressedCell(boolean inGame, int currentCell){//클릭중에 비활성셀이 있으면 임시로 활성표시
    	List<Integer> list = new ArrayList<>();
		if(inGame) {
			int currentCol = currentCell % N_COLS;//넘겨받은 칸의 가로 위치
	        int cell;
	        
	        if (currentCol > 0) {//왼쪽 셀이 범위에 있는지 체크
	            cell = currentCell - N_COLS - 1;//왼쪽 위
	            if (cell >= 0) {
	            	if(field[cell]>=10 && field[cell]<20)
	            		list.add(cell);
	            }
	            cell = currentCell - 1;//왼쪽
	            if (cell >= 0) {
	            	if(field[cell]>=10 && field[cell]<20)
	            		list.add(cell);
	            }
	            cell = currentCell + N_COLS - 1;//왼쪽 아래
	            if (cell < allCells) {
	            	if(field[cell]>=10 && field[cell]<20)
	            		list.add(cell);
	            }
	        }
	
	        cell = currentCell - N_COLS;//위
	        if (cell >= 0) {
	        	if(field[cell]>=10 && field[cell]<20)
	        		list.add(cell);
	        }
	
	        cell = currentCell + N_COLS;//아래
	        if (cell < allCells) {
	        	if(field[cell]>=10 && field[cell]<20)
	        		list.add(cell);
	        }
	
	        if (currentCol < (N_COLS - 1)) {//오른쪽 셀이 범위내에 있는지 체크
	            cell = currentCell - N_COLS + 1;//오른쪽 위
	            if (cell >= 0) {
	            	if(field[cell]>=10 && field[cell]<20)
	            		list.add(cell);
	            }
	
	            cell = currentCell + N_COLS + 1;//오른쪽 아래
	            if (cell < allCells) {
	            	if(field[cell]>=10 && field[cell]<20)
	            		list.add(cell);
	            }
	
	            cell = currentCell + 1;//오른쪽
	            if (cell < allCells) {
	            	if(field[cell]>=10 && field[cell]<20)
	            		list.add(cell);
	            }
	        }
		}
		return list;
	}
    
    
    //==================================사운드=========================================
    public void sound(String fileName)
    {
        try
        {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(fileName));
            Clip clip = AudioSystem.getClip();
            clip.stop();
            clip.open(ais);
            clip.start();
        }
        catch (Exception ex)
        {
        }
    }
//    "./images/inGame/resources/" + i + ".png";
    List<String> soundsFilePath = new ArrayList<>();
    void setSounds() {
//    	String fileName = "";
    	String soundsPath = "./sounds/";
	    File rw = new File(soundsPath);
	    File []fileList = rw.listFiles();
	    for(File file : fileList) {
	          if(file.isFile()) {
//	             fileName = file.getName();
//	             System.out.println("fileName : " + fileName);
	        	  soundsFilePath.add(soundsPath+file.getName());
	          }
	    }
    }
    enum soundsEnum{
    	CLICK(0),
    	FLAG(1),
    	GAMEOVER(2),
    	GAMEWIN(3),
    	x(4),
    	xx(5),
    	UNFLAG(6),
    	WRONG(7);

		soundsEnum(int i) {
			// TODO Auto-generated constructor stub
		}
    }
//    soundsFilePath 순서(임시)
//    fileName : sounds_click.wav
//    fileName : sounds_flag.wav
//    fileName : sounds_gameOver.wav
//    fileName : sounds_gameWin.wav
//    fileName : sounds_mainmenu.ogg
//    fileName : sounds_music.ogg
//    fileName : sounds_unflag.wav
//    fileName : sounds_wrong.wav
    
}
