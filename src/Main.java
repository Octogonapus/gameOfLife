import processing.core.PApplet;

public class Main extends PApplet {

  private static final int SIZE_X = 100;
  private static final int SIZE_Y = 100;
  private static final int WINDOW_X = 1000;
  private static final int WINDOW_Y = 1000;
  private static final int DIV_X = WINDOW_X / SIZE_X;
  private static final int DIV_Y = WINDOW_Y / SIZE_Y;
  private final State[][] board1, board2;
  private State[][] readBoard, writeBoard;

  public Main() {
    board1 = new State[SIZE_X][SIZE_Y];
    board2 = new State[SIZE_X][SIZE_Y];

    clearBoard(board1);
    clearBoard(board2);

    board1[4][5] = State.alive;
    board1[4][6] = State.alive;
    board1[4][7] = State.alive;

    board1[5][4] = State.alive;
    board1[5][5] = State.alive;
    board1[5][6] = State.alive;

    readBoard = board1;
    writeBoard = board2;
  }

  public static void main(String[] args) {
    PApplet.main("Main");
  }

  @Override
  public void settings() {
    size(WINDOW_X, WINDOW_Y);
  }

  @Override
  public void draw() {
    for (int i = 0; i < SIZE_X; i++) {
      for (int j = 0; j < SIZE_Y; j++) {
        setFillForCell(readBoard, i, j);
        rect(i * DIV_X, j * DIV_Y, DIV_X, DIV_Y);
      }
    }

    delay(250);
    stepGame(readBoard, writeBoard);

    //Flip buffer
    State[][] temp = readBoard;
    readBoard = writeBoard;
    writeBoard = temp;
  }

  private void stepGame(final State[][] readBoard, final State[][] writeBoard) {
    clearBoard(writeBoard);

    for (int i = 0; i < SIZE_X; i++) {
      for (int j = 0; j < SIZE_Y; j++) {
        final State current = readBoard[i][j];
        final int neighbors = getAliveNeighborCount(readBoard, i, j);

        if (current == State.alive) {
          //Less than two and more than three neighbors, cell dies
          if (neighbors == 2 || neighbors == 3) {
            writeBoard[i][j] = State.alive;
          }
        } else if (current == State.dead) {
          //Three neighbors, cell lives again
          if (neighbors == 3)  {
            writeBoard[i][j] = State.alive;
          }
        }
      }
    }
  }

  private int getAliveNeighborCount(final State[][] board, final int x, final int y) {
    int total = 0;

    for (int i = x - 1; i <= x + 1; i++) {
      if (i >= 0 && i < SIZE_X) {
        for (int j = y - 1; j <= y + 1; j++) {
          if (j >= 0 && j < SIZE_Y) {
            if (!(i == x && j == y) && board[i][j] == State.alive) {
              total++;
            }
          }
        }
      }
    }

    return total;
  }

  private void setFillForCell(final State[][] board, final int x, final int y) {
    switch (board[x][y]) {
      case alive:
        fill(0, 255, 0);
        break;
      case dead:
        fill(255, 0, 0);
        break;
    }
  }

  private void clearBoard(State[][] board) {
    for (int i = 0; i < SIZE_X; i++) {
      for (int j = 0; j < SIZE_Y; j++) {
        board[i][j] = State.dead;
      }
    }
  }

  private enum State {
    alive, dead
  }

}
