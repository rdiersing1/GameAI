package large_ttt;

import java.io.Serializable;
import java.util.ArrayList;

public class Board implements Serializable {
  public final static byte EMPTY = 0;
  public final static byte X = 1;
  public final static byte O = 2;

  private final int length;
  private int movesMade;
  private byte[][] boardMatrix;

  public Board(int length) {
    this.length = length;
    boardMatrix = new byte[length][length];
    movesMade = 0;
  }

  /**
   * Will print the board to System.out.
   * <b>WARNING:</b> Boards with length greater than 26 are NOT supported.
   */
  public void print() {
    StringBuilder boardSB = new StringBuilder("\n  ");
    for (int i = 0; i < length; ++i) {
      boardSB.append(" ");
      char c = (char) ('A' + ((char) i));
      boardSB.append(c);
      boardSB.append("  ");
    }
    boardSB.append('\n');
    for (int i = 0; i < length; ++i) {
      char c = (char) ('A' + ((char) i));
      boardSB.append(c);
      boardSB.append(' ');
      for (int j = 0; j < length; ++j) {
        boardSB.append(j == 0 ? "" : "|");
        boardSB.append(' ');
        switch (boardMatrix[i][j]) {
          case EMPTY: boardSB.append(' '); break;
          case X: boardSB.append('X'); break;
          case O: boardSB.append('O'); break;
        }
        boardSB.append(' ');
      }
      boardSB.append("\n  ");
      if (i != length - 1) {
        for (int j = 0; j < length; ++j) {
          boardSB.append(j == 0 ? "" : "|");
          boardSB.append("---");
        }
      }
      boardSB.append("\n");
    }
    System.out.println(boardSB.toString());
  }

  /** Returns an ArrayList of all valid moves */
  public ArrayList<Move> getAllMoves(byte player) {
    ArrayList<Move> validMoves = new ArrayList<>();
    for (int i = 0; i < length; ++i) {
      for (int j = 0; j < length; ++j) {
        if (boardMatrix[i][j] == EMPTY) {
          validMoves.add(new Move(i, j, player));
        }
      }
    }
    return validMoves;
  }

  /**
   * Will make a given move.
   * <b>WARNING:</b> Will throw null pointer exception if the move is OOB, always check moves with isValid.
   *
   * @param m the move to make
   * @return a boolean that is true if the game has just been won
   */
  public boolean makeMoveUnsafe(Move m) {
    ++movesMade;
    int i = m.getI();
    int j = m.getJ();
    boardMatrix[i][j] = m.getPlayer();
    for (int di = -1; di <= 1; ++di) {
      for (int dj = -1; dj <= 1; ++dj) {
        if (di + i < 0 || di + i >= length) { continue; }
        if (dj + j < 0 || dj + j >= length) { continue; }
        if (winner(di + i, dj + j, m.getPlayer())) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean isValid(Move m) {
    if (m.getI() < 0 || m.getI() >= length) { return false; }
    if (m.getJ() < 0 || m.getJ() >= length) { return false; }
    return boardMatrix[m.getI()][m.getJ()] == EMPTY;
  }

  public boolean isFull() {
    return movesMade == length * length;
  }

  public Board copy() {
    Board newBoard = new Board(length);
    for (int i = 0; i < length; ++i) {
      for (int j = 0; j < length; ++j) {
        newBoard.boardMatrix[i][j] = boardMatrix[i][j];
      }
    }
    newBoard.movesMade = movesMade;
    return newBoard;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    for (int i = 0; i < length; ++i) {
      for (int j = 0; j < length; ++j) {
        hash += ((int) boardMatrix[i][j]) * Math.pow(3, Math.pow(length, i) + j);
      }
    }
    return Integer.hashCode(hash);
  }

  @Override
  public boolean equals(Object other) {
    if (other.getClass() != Board.class) { return false; }
    Board otherBoard = (Board) other;
    if (otherBoard.length != length) { return false; }
    for (int i = 0; i < length; ++i) {
      for (int j = 0; j < length; ++j) {
        if (otherBoard.boardMatrix[i][j] != boardMatrix[i][j]) { return false; }
      }
    }
    return true;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Board [len=");
    sb.append(length);
    sb.append(", state=[");
    for (int i = 0; i < length; ++i) {
      for (int j = 0; j < length; ++j) {
        sb.append(boardMatrix[i][j]);
        sb.append(i == length-1 && j == length-1 ? "" : ",");
      }
    }
    sb.append("]]");
    return sb.toString();
  }

  boolean winner(int i, int j, byte player) {
    for (int di = -1; di <= 1; ++di) {
      for (int dj = -1; dj <= 1; ++dj) {
        if (di == 0 && dj == 0) { continue; }
        if (i + (di * 3) < 0 || i + (di * 3) >= length) { continue; }
        if (j + (dj * 3) < 0 || j + (dj * 3) >= length) { continue; }
        for (int len = 0; len < 4; ++len) {
          int newI = i + (di * len);
          int newJ = j + (dj * len);
          if (boardMatrix[newI][newJ] != player) {
            break;
          } else if (len == 3) {
            return true;
          }
        }
      }
    }
    return false;
  }
}
