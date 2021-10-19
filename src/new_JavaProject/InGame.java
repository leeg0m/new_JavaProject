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
    private final int NUM_IMAGES = 13;// \Java-Minesweeper-Game-master\Java-Minesweeper-Game-master\src\resources �̹��� ���� 12�� 
    private final int CELL_SIZE = 15;//���� ����
//    private final int CELL_SIZE = 30;

    private final int COVER_FOR_CELL = 10;//Ŀ��(��Ȱ��ȭ ĭ)//Ŀ�� �ִ°��� 10��ŭ ������
    private final int MARK_FOR_CELL = 10;//��� ���� ĭ//��߲������� 10��ŭ ������
    private final int EMPTY_CELL = 0;//��ĭ(Ȱ��ȭ��ĭ)
    private final int MINE_CELL = 9;//���� ��(ĭ)
    private final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;//Ŀ��+���� = Ŀ�� ���� ����
    private final int MARKED_MINE_CELL = COVERED_MINE_CELL + MARK_FOR_CELL;//��� ���� ���ڼ� //���+Ŀ������ = ���(10) + ����(9+10)

    private final int DRAW_MINE = 9;//���� �̹���?
//  private final int DRAW_COVER = 1;//NUM_IMAGES�� ��������. ���� CELL�� NUM_IMAGES�� ��������.
//  �̹������� 0:���� 1:1 2:2 3:3 .. 8:8 9:���� 10:�ȴ��� 11:�� 12:�� ��� 
//	���� ���� 0:���� 1:1 2:2 3:3 .. 8:8 9:���� 10:�ȴ��� 11:Ŀ��(1) 12:Ŀ��(2) .. 19:Ŀ������ 29:��߲�������Ŀ�� //20:�󼿿� ���� ��� 21:1���������...28:8���ȱ�
    private final int DRAW_COVER = 10;//Ŀ��
    private final int DRAW_MARK = 11;//��߸�ũ
    private final int DRAW_WRONG_MARK = 12;//�� ���

//    private final int N_MINES = 40;//���� ����
    private int N_MINES = 40;//���� ����
//    private final int N_ROWS = 16;//����ĭ ��
    private int N_ROWS = 16;//����ĭ ��
//    private final int N_COLS = 32;//����ĭ ��
//    private final int N_COLS = 16;//����ĭ ��
    private int N_COLS = 16;//����ĭ ��

//    private final int BOARD_WIDTH = N_COLS * CELL_SIZE + 1;//������ ����
//    private final int BOARD_HEIGHT = N_ROWS * CELL_SIZE + 1;//
    private int BOARD_WIDTH = N_COLS * CELL_SIZE + 1;//������ ����
    private int BOARD_HEIGHT = N_ROWS * CELL_SIZE + 1;//

    private int[] field;//��� ��(ĭ) �ѹ���
    private boolean inGame;//false:game lost
    private int minesLeft;//�����ϴ��� ǥ�õǴ� ���� ���� ���ڰ����� ī��Ʈ�ϴ� ����
    private Image[] img;//�̹��� ����

    private int allCells;//N_ROWS * N_COLS;// = field ����
    private final JLabel statusbar=new JLabel("testMessage");//�����ϴ��� �۾�. ex) ���ڰ���, �¸��޽���,�й�޽��� �� 

//    public InGame(JLabel statusbar) {
	public InGame(int n_cols, int n_rows, int n_mines) {
		this.N_COLS=n_cols;
		this.N_ROWS=n_rows;
		this.N_MINES=n_mines;
		BOARD_WIDTH = N_COLS * CELL_SIZE + 1;//������ ����
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

        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));//â ������ ����

        img = new Image[NUM_IMAGES];//�̹��� �ε�
        for (int i = 0; i < NUM_IMAGES; i++) {

            var path = "./images/inGame/resources/" + i + ".png";
            img[i] = (new ImageIcon(path)).getImage();
        }

        addMouseListener(new MinesAdapter());
        newGame();//�ٽ��ڵ�
    }

    private void newGame() {

        int cell;

        var random = new Random();//java.util.Random //���� ��ġ�����Ҷ� ���
        inGame = true;//���� ����. true:��� false:gamelose
        minesLeft = N_MINES;//���� ������ �� ������ �ʱ�ȭ

        allCells = N_ROWS * N_COLS;
        field = new int[allCells];

        for (int i = 0; i < allCells; i++) {

            field[i] = COVER_FOR_CELL;//��� ĭ�� Ŀ��(��Ȱ��ȭ ��ĭ)�� ����
        }

        statusbar.setText(Integer.toString(minesLeft));

        int i = 0;

        while (i < N_MINES) {

//            int position = (int) (50 * random.nextDouble());
            int position = (int) (allCells * random.nextDouble());//��ü �� �߿� ��ġ�ϴ� ��ǥ��. ex) position==0 �϶� (0,0)�� ���ڰ� ����

            if ((position < allCells)
                    && (field[position] != COVERED_MINE_CELL)) { //������ ��ġ�� ��üĭ���� ũ�� �����鼭 ���ڰ��ִ� �ʵ��� ���°� Ŀ���� ������ ����ĭ�� �ƴϸ�

                int current_col = position % N_COLS;//������ ���� ��ġ. %����
                
                field[position] = COVERED_MINE_CELL;// ���ڰ� �ִ� �ʵ忡 Ŀ��(��������)�� ����
                i++;

                if (current_col > 0) {//0���� ŭ = ���� ���ʿ� ��ġ���� ���� = ���� ���� �����ȿ� �ִ��� üũ
                    cell = position - 1 - N_COLS;//���� ��ġ�� ���� ���� ĭ
//                    System.out.println(cell);
                    if (cell >= 0) {//0���� ũ�ų� ���� = ���� �� ���� ���� �ȿ� �ִ�.
                        if (field[cell] != COVERED_MINE_CELL) {//����ĭ �������� ���� ������
                            field[cell] += 1;//���ڸ��� 1����
                        }
                    }
                    cell = position - 1;//���� ��ġ�� ����
                    if (cell >= 0) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }

                    cell = position + N_COLS - 1;//���� �Ʒ�
                    if (cell < allCells) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                }

                cell = position - N_COLS;//��
                if (cell >= 0) {
                    if (field[cell] != COVERED_MINE_CELL) {
                        field[cell] += 1;
                    }
                }

                cell = position + N_COLS;//�Ʒ�
                if (cell < allCells) {
                    if (field[cell] != COVERED_MINE_CELL) {
                        field[cell] += 1;
                    }
                }

                if (current_col < (N_COLS - 1)) {//������ ���� �����ȿ��ִ��� üũ
                    cell = position - N_COLS + 1;//������ ��
                    if (cell >= 0) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                    cell = position + N_COLS + 1;//������ �Ʒ�
                    if (cell < allCells) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                    cell = position + 1;//������
                    if (cell < allCells) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                }
            }
        }
    }

    private void find_empty_cells(int j) {//��ĭã��

        int current_col = j % N_COLS;//�Ѱܹ��� ĭ�� ���� ��ġ
        int cell;

        if (current_col > 0) {//���� ���� ������ �ִ��� üũ
            cell = j - N_COLS - 1;//���� ��
            if (cell >= 0) {
                if (field[cell] > MINE_CELL) {//���� ���� ���� ���ڼ�(9)���� ũ��(��Ȱ��ȭ ��ĭ / ���?)
                    field[cell] -= COVER_FOR_CELL;//Ŀ����(10)��ŭ �� //10-18 => 0-8
                    if (field[cell] == EMPTY_CELL) {//��ĭ�̸�
                        find_empty_cells(cell);//���ȣ�� = ���� �� �������� �ٽ� �� Ž��
                    }
                }
            }

            cell = j - 1;//����
            if (cell >= 0) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j + N_COLS - 1;//���� �Ʒ�
            if (cell < allCells) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }
        }

        cell = j - N_COLS;//��
        if (cell >= 0) {
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL) {
                    find_empty_cells(cell);
                }
            }
        }

        cell = j + N_COLS;//�Ʒ�
        if (cell < allCells) {
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL) {
                    find_empty_cells(cell);
                }
            }
        }

        if (current_col < (N_COLS - 1)) {//������ ���� �������� �ִ��� üũ
            cell = j - N_COLS + 1;//������ ��
            if (cell >= 0) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j + N_COLS + 1;//������ �Ʒ�
            if (cell < allCells) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j + 1;//������
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

        int uncover = 0;//Ŀ�� Ȱ��ȭ

        for (int i = 0; i < N_ROWS; i++) {//����

            for (int j = 0; j < N_COLS; j++) {//����

                int cell = field[(i * N_COLS) + j];

                if (inGame && cell == MINE_CELL) {//�������̰�, ���ڼ��� ������

                    inGame = false;//����
                }

                if (!inGame) {//������

                    if (cell == COVERED_MINE_CELL) {//Ŀ�� ���ڴ�
                        cell = DRAW_MINE;//���ڸ� ǥ��
                    } else if (cell == MARKED_MINE_CELL) {//��߲��� ���� ����
                        cell = DRAW_MARK;//��� �̹��� ����
                    } else if (cell > COVERED_MINE_CELL) {//��߲��� ���ھƴ� ����
                        cell = DRAW_WRONG_MARK;//��� ��� �̹���
                    } else if (cell > MINE_CELL) {//����(9)�� ���� ũ��(10-18)
                        cell = DRAW_COVER;//Ŀ�� �̹���
                    }

                } else {//�ƴϸ�

                    if (cell > COVERED_MINE_CELL) {//Ŀ�� ����(19) ���� ũ��
                        cell = DRAW_MARK;//���
                    } else if (cell > MINE_CELL) {//���ں��� ũ��
                        cell = DRAW_COVER;//�Ϲ�Ŀ��
                        uncover++;//��Ȱ��ȭ �� ī��Ʈ ����
                    }
                }

                g.drawImage(img[cell], (j * CELL_SIZE),
                        (i * CELL_SIZE), this);//�� �̹��� ����
            }
        }

        if (uncover == 0 && inGame) {//�������̰� ��Ȱ��ȭ ĭ ������

            inGame = false;//��������
            statusbar.setText("Game won");//�¸�

        } else if (!inGame) {//���ӳ�
            statusbar.setText("Game lost");//����
        }
    }

    private class MinesAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {

            int x = e.getX();//���콺 x��ǥ
            int y = e.getY();//���콺 y��ǥ
//            System.out.println(y);//����0-240
//            System.out.println(x);//����0-240

            int cCol = x / CELL_SIZE;
            int cRow = y / CELL_SIZE;
//            System.out.println(cCol);// ����0-240/15(CELL_SIZE) = 0~15(CELL_SIZE)

            boolean doRepaint = false;

            if (!inGame) {

                newGame();//������
                repaint();//���α׸�
            }
//            System.out.println(e.getButton());
//            System.out.println(MouseEvent.BUTTON3);

            if ((x < N_COLS * CELL_SIZE) && (y < N_ROWS * CELL_SIZE)) {//15*16=240

                if (e.getButton() == MouseEvent.BUTTON3) {//��Ŭ���ϸ�

                    if (field[(cRow * N_COLS) + cCol] > MINE_CELL) {//���� ���� ����(9)���� ũ�� = 10-29

                        doRepaint = true;

                        if (field[(cRow * N_COLS) + cCol] <= COVERED_MINE_CELL) {//Ŀ�����ں��� �۰ų� ������ = 10-19 = ��� �Ȳ���������

                            if (minesLeft > 0) {//���� ī���Ͱ� 0���� Ŭ��
                                field[(cRow * N_COLS) + cCol] += MARK_FOR_CELL;//��� ����
                                minesLeft--;//���� ī���� ����
                                String msg = Integer.toString(minesLeft);
                                statusbar.setText(msg);//ī���� ����
                            } else {
                                statusbar.setText("No marks left");//ī���ͺ��� ����� ���� �ȾƼ� ���̻� ����� ���� �� ����
                            }
                        } else {//��� ����������

                            field[(cRow * N_COLS) + cCol] -= MARK_FOR_CELL;//��� ����
                            minesLeft++;//ī���� ����
                            String msg = Integer.toString(minesLeft);
                            statusbar.setText(msg);//����
                        }
                    }

                } else {//��Ŭ, ��Ŭ ���

                    if (field[(cRow * N_COLS) + cCol] > COVERED_MINE_CELL) {//��߲���������

                        return;//�ƹ��͵�����
                    }

                    if ((field[(cRow * N_COLS) + cCol] > MINE_CELL)
                            && (field[(cRow * N_COLS) + cCol] < MARKED_MINE_CELL)) {//Ŀ���ִ� ���̸�

                        field[(cRow * N_COLS) + cCol] -= COVER_FOR_CELL;//Ŀ�� ����
                        doRepaint = true;

                        if (field[(cRow * N_COLS) + cCol] == MINE_CELL) {//���ڿ�����
                            inGame = false;//�й�
                        }

                        if (field[(cRow * N_COLS) + cCol] == EMPTY_CELL) {//���̸�
                            find_empty_cells((cRow * N_COLS) + cCol);//�ٸ� �� Ž��
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
