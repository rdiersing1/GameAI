/** @author Robert Diersing */
package large_ttt;

import java.io.Serializable;

/** A class representing the move action of a player. */
public class Move implements Serializable {
  private final int i;
  private final int j;
  private final byte player;

  Move(int i, int j, byte player) {
    this.i = i;
    this.j = j;
    this.player = player;
  }

  @Override
  public boolean equals(Object other) {
    if (other.getClass() != Move.class) { return false; }
    Move otherMove = (Move) other;
    return i == otherMove.i && j == otherMove.j && player == otherMove.player;
  }

  @Override
  public int hashCode() {
    return Integer.hashCode(i + (j * 100) + (((int) player) * 100000));
  }

  @Override
  public String toString() {
    return "Move=[" + i + "," + j + "," + player + "]";
  }

  public int getI() {
    return i;
  }

  public int getJ() {
    return j;
  }

  public byte getPlayer() {
    return player;
  }
}
