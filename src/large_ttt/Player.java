package large_ttt;

import org.jetbrains.annotations.Nullable;

import java.util.Scanner;

public abstract class Player {
  protected final Board board;
  protected final byte type;

  public Player(Board board, byte type) {
    this.board = board;
    this.type = type;
  }
  public abstract GameState makeMove(@Nullable Scanner in);
}
