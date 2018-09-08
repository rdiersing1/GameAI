package AI;

import large_ttt.Board;
import large_ttt.GameState;
import large_ttt.Player;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AITrain {
  public static long NANOSEC_PER_SEC = 1_000_000_000;

  public Map<Board, GameFitness> train(int numGames, int searchDepth) {
    Map<Board, GameFitness> fitnessMap = new HashMap<>();

    long totalStartTime = System.nanoTime();
    for (int i = 0; i < numGames; ++i) {
      long startTime = System.nanoTime();
      fitnessMap = playGame(fitnessMap, searchDepth);
      long endTime = System.nanoTime();
      double runtimeSec = ((double) (endTime - startTime)) / NANOSEC_PER_SEC;
      System.out.format("Played %d/%d games. Runtime: %.4fsec\n", i + 1, numGames, runtimeSec);
    }
    long totalEndTime = System.nanoTime();
    double totalRuntimeSec = ((double) (totalEndTime - totalStartTime)) / NANOSEC_PER_SEC;
    System.out.format("MapSize: %d. Total Runtime: %.4fsec\n", fitnessMap.size(), totalRuntimeSec);

    return fitnessMap;
  }

  public Map<Board, GameFitness> playGame(Map<Board, GameFitness> fitnessMap, int searchDepth) {
    Board board = new Board(6);
    Player x = new ComputerPlayer(board, Board.X, searchDepth);
    Player o = new ComputerPlayer(board, Board.O, searchDepth);
    GameState state = GameState.ONGOING;
    boolean xTurn = true;
    Set<Board> newStates = new HashSet<>();

    while (state == GameState.ONGOING) {
      if (xTurn) {
        state = x.makeMove(null);
      } else {
        state = o.makeMove(null);
      }
      newStates.add(board.copy());
      xTurn = !xTurn;
    }

    // Update the games played database
    for (Board b : newStates) {
      if (fitnessMap.containsKey(b)) {
        switch (state) {
          case X_WON: fitnessMap.get(b).numXWin++; break;
          case O_WON: fitnessMap.get(b).numOWin++; break;
          default: fitnessMap.get(b).numTies++;
        }
      } else {
        GameFitness newFitness = new GameFitness();
        switch (state) {
          case X_WON: newFitness.numXWin++; break;
          case O_WON: newFitness.numOWin++; break;
          default: newFitness.numTies++;
        }
        fitnessMap.put(b, newFitness);
      }
    }
    return fitnessMap;
  }

  public static void main(String[] args) {
    AITrain trainer = new AITrain();
    Map<Board, GameFitness> fitnessMap = trainer.train(500, 4);
    System.out.println("Map size: " + fitnessMap.size());

    try {
      ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File("fitnessMap.txt")));
      out.writeObject(fitnessMap);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
