package new_JavaProject;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InGame extends JPanel {

//    private final int NUM_IMAGES = 26;
    private final int NUM_IMAGES = 13;// \Java-Minesweeper-Game-master\Java-Minesweeper-Game-master\src\resources 이미지 파일 12개 
    private final int CELL_SIZE = 15;//셀의 간격
//    private final int CELL_SIZE = 30;

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

//    public InGame(JLabel statusbar) {
	public InGame(int n_cols, int n_rows, int n_mines) {
		this.N_COLS=n_cols;
		this.N_ROWS=n_rows;
		this.N_MINES=n_mines;
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

        img = new Image[NUM_IMAGES];//이미지 로딩
        for (int i = 0; i < NUM_IMAGES; i++) {

            var path = "./images/inGame/resources/" + i + ".png";
            img[i] = (new ImageIcon(path)).getImage();
        }

        addMouseListener(new MinesAdapter());
        newGame();//핵심코드
    }

    private void newGame() {

        int cell;

        var random = new Random();//java.util.Random //지뢰 위치설정할때 사용
        inGame = true;//게임 상태. true:대기 false:gamelose
        minesLeft = N_MINES;//남은 개수를 총 개수로 초기화

        allCells = N_ROWS * N_COLS;
        field = new int[allCells];

        for (int i = 0; i < allCells; i++) {

            field[i] = COVER_FOR_CELL;//모든 칸에 커버(비활성화 빈칸)를 씌움
        }

        statusbar.setText(Integer.toString(minesLeft));

        int i = 0;

        while (i < N_MINES) {

//            int position = (int) (50 * random.nextDouble());
            int position = (int) (allCells * random.nextDouble());//전체 셀 중에 위치하는 좌표값. ex) position==0 일때 (0,0)에 지뢰가 있음

            if ((position < allCells)
                    && (field[position] != COVERED_MINE_CELL)) { //지뢰의 위치가 전체칸보다 크지 않으면서 지뢰가있는 필드의 상태가 커버가 씌어진 지뢰칸이 아니면

                int current_col = position % N_COLS;//지뢰의 가로 위치. %연산
                
                field[position] = COVERED_MINE_CELL;// 지뢰가 있는 필드에 커버(지뢰있음)를 씌움
                i++;

                if (current_col > 0) {//0보다 큼 = 가장 왼쪽에 위치하지 않음 = 왼쪽 셀이 범위안에 있는지 체크
                    cell = position - 1 - N_COLS;//지뢰 위치의 왼쪽 위의 칸
//                    System.out.println(cell);
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

    @Override
    public void paintComponent(Graphics g) {

        int uncover = 0;//커버 활성화

        for (int i = 0; i < N_ROWS; i++) {//세로

            for (int j = 0; j < N_COLS; j++) {//가로

                int cell = field[(i * N_COLS) + j];

                if (inGame && cell == MINE_CELL) {//게임중이고, 지뢰셀을 누르면

                    inGame = false;//졌다
                }

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
                }

                g.drawImage(img[cell], (j * CELL_SIZE),
                        (i * CELL_SIZE), this);//셀 이미지 갱신
            }
        }

        if (uncover == 0 && inGame) {//게임중이고 비활성화 칸 없으면

            inGame = false;//게임종료
            statusbar.setText("Game won");//승리

        } else if (!inGame) {//게임끝
            statusbar.setText("Game lost");//졌다
        }
    }

    private class MinesAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {

            int x = e.getX();//마우스 x좌표
            int y = e.getY();//마우스 y좌표
//            System.out.println(y);//범위0-240
//            System.out.println(x);//범위0-240

            int cCol = x / CELL_SIZE;
            int cRow = y / CELL_SIZE;
//            System.out.println(cCol);// 범위0-240/15(CELL_SIZE) = 0~15(CELL_SIZE)

            boolean doRepaint = false;

            if (!inGame) {

                newGame();//새게임
                repaint();//새로그림
            }
//            System.out.println(e.getButton());
//            System.out.println(MouseEvent.BUTTON3);

            if ((x < N_COLS * CELL_SIZE) && (y < N_ROWS * CELL_SIZE)) {//15*16=240

                if (e.getButton() == MouseEvent.BUTTON3) {//우클릭하면

                    if (field[(cRow * N_COLS) + cCol] > MINE_CELL) {//눌린 셀이 지뢰(9)보다 크면 = 10-29

                        doRepaint = true;

                        if (field[(cRow * N_COLS) + cCol] <= COVERED_MINE_CELL) {//커버지뢰보다 작거나 같으면 = 10-19 = 깃발 안꽂혀있으면

                            if (minesLeft > 0) {//지뢰 카운터가 0보다 클때
                                field[(cRow * N_COLS) + cCol] += MARK_FOR_CELL;//깃발 꽂음
                                minesLeft--;//지뢰 카운터 줄임
                                String msg = Integer.toString(minesLeft);
                                statusbar.setText(msg);//카운터 줄임
                            } else {
                                statusbar.setText("No marks left");//카운터보다 깃발을 많이 꽂아서 더이상 깃발을 꽂을 수 없음
                            }
                        } else {//깃발 꽂혀있으면

                            field[(cRow * N_COLS) + cCol] -= MARK_FOR_CELL;//깃발 제거
                            minesLeft++;//카운터 증가
                            String msg = Integer.toString(minesLeft);
                            statusbar.setText(msg);//증가
                        }
                    }

                } else {//좌클, 휠클 등등

                    if (field[(cRow * N_COLS) + cCol] > COVERED_MINE_CELL) {//깃발꽂혀있으면

                        return;//아무것도안함
                    }

                    if ((field[(cRow * N_COLS) + cCol] > MINE_CELL)
                            && (field[(cRow * N_COLS) + cCol] < MARKED_MINE_CELL)) {//커버있는 셀이면

                        field[(cRow * N_COLS) + cCol] -= COVER_FOR_CELL;//커버 제거
                        doRepaint = true;

                        if (field[(cRow * N_COLS) + cCol] == MINE_CELL) {//지뢰였으면
                            inGame = false;//패배
                        }

                        if (field[(cRow * N_COLS) + cCol] == EMPTY_CELL) {//빈셀이면
                            find_empty_cells((cRow * N_COLS) + cCol);//다른 빈셀 탐색
                        }
                    }
                }

                if (doRepaint) {
                    repaint();
                }
            }
        }
    }
}
