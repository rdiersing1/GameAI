package large_ttt;

import org.jetbrains.annotations.Nullable;

import java.util.Scanner;

public class HumanPlayer extends Player {
  HumanPlayer(Board board, byte type) {
    super(board, type);
  }

  public GameState makeMove(Scanner in) {
    char player = type == Board.X ? 'X' : 'O';
    System.out.println("Your Move Player " + player + ":");
    boolean choseValidMove = false;
    Move move = null;
    while(!choseValidMove) {
      System.out.println("Please enter a move by providing the character corresponding to the row then the column you " +
          "would like to place your move. Then press enter...");
      String moveStr = in.nextLine();
      move = stringToMove(moveStr, type);
      if (move == null) {
        System.out.println("Invalid Move Format");
        continue;
      }
      if (!board.isValid(move)) {
        System.out.println("Invalid Move Location");
        continue;
      }
      choseValidMove = true;
    }

    boolean wonGame = board.makeMoveUnsafe(move);
    if (wonGame) { return type == Board.X ? GameState.X_WON : GameState.O_WON; }
    if (board.isFull()) { return GameState.TIE; }
    return GameState.ONGOING;
  }

  @Nullable
  private Move stringToMove(String s, byte player) {
    if (s.length() != 2) { return null; }
    s = s.toLowerCase();
    int i = s.charAt(0) - 'a';
    int j = s.charAt(1) - 'a';
    return new Move(i, j, player);
  }
}
