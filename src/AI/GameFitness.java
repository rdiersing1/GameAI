package AI;

import java.io.Serializable;

public class GameFitness implements Serializable {
  public int numXWin = 0;
  public int numOWin = 0;
  public int numTies = 0;

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Fit=[" + numXWin + "," + numOWin + "," + numTies + "]");
    return sb.toString();
  }

  public int getTotalGames() {
    return numXWin + numOWin + numTies;
  }
}
