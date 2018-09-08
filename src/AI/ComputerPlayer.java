package AI;

import large_ttt.Board;
import large_ttt.GameState;
import large_ttt.Move;
import large_ttt.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ComputerPlayer extends Player {
  private int maxSearchDepth;
  private Map<Board, GameFitness> fitnessMap;

  private static final float MAX_SCORE = 1;
  private static final float MIN_SCORE = 0;
  private static final boolean DEBUG = false;
  private static final boolean OUTPUT = false;
  private static int MIN_GAMES_FOR_FITNESS = 3;

  public ComputerPlayer(Board board, byte type, int maxSearchDepth) {
    super(board, type);
    this.maxSearchDepth = maxSearchDepth;
    this.fitnessMap = new HashMap<>();
  }

  public void setFitnessMap(Map<Board, GameFitness> fitnessMap) {
    this.fitnessMap = fitnessMap;
  }

  public GameState makeMove(@Nullable Scanner in) {
    if (OUTPUT) { System.out.println("The bot is thinking..."); }
    ArrayList<Move> validMoves = board.getAllMoves(type);
    float[] scores = new float[validMoves.size()];
    for (int i = 0; i < scores.length; ++i) {
      scores[i] = scoreMove(validMoves.get(i), 1, type, board);
    }

    // Debug output
    if (DEBUG) {
      System.out.println("Player " + (type == Board.X ? "X" : "O"));
      System.out.println("Possible Moves:");
      for (int i = 0; i < validMoves.size(); ++i) {
        System.out.println(
            "Move: (" + validMoves.get(i).getI() + ", " + validMoves.get(i).getJ() + "): score = " + scores[i]);
      }
    }

    float bestScore = MIN_SCORE;
    for (float score : scores) {
      bestScore = score > bestScore ? score : bestScore;
    }
    ArrayList<Move> bestMoves = new ArrayList<>();
    for (int i = 0; i < scores.length; ++i) {
      if (scores[i] == bestScore) {
        bestMoves.add(validMoves.get(i));
      }
    }

    // Ensure there is a move to make
    assert(!bestMoves.isEmpty());

    Random rand = new Random();
    Move bestMove = bestMoves.get(rand.nextInt(bestMoves.size()));

    boolean wonGame = board.makeMoveUnsafe(bestMove);
    if (wonGame) { return type == Board.X ? GameState.X_WON : GameState.O_WON; }
    if (board.isFull()) { return GameState.TIE; }
    if (OUTPUT) { System.out.println("The bot has made its move"); }
    return GameState.ONGOING;
  }

  private float scoreMove(Move m, int depth, byte currPlayer, Board currBoard) {
    Board newBoard = currBoard.copy();
    boolean isWinner = newBoard.makeMoveUnsafe(m);

    // Base case
    if (isWinner) {
      return currPlayer == type ? MAX_SCORE - (depth * 0.01f) : MIN_SCORE + (depth * 0.01f);
    }

    if (fitnessMap.containsKey(currBoard)) {
      GameFitness fitness = fitnessMap.get(currBoard);
      if (fitness.getTotalGames() >= MIN_GAMES_FOR_FITNESS) {
        float wins = type == Board.X ? fitness.numXWin : fitness.numOWin;
        return (float) ((0.5 * fitness.numTies) + wins) / fitness.getTotalGames();
      }
    }

    if (depth == maxSearchDepth) {
      return 0.5f;
    }

    // Recursively get all of the optimal scores for the other moves.
    byte nextPlayer = currPlayer == Board.X ? Board.O : Board.X;
    ArrayList<Move> newMoves = newBoard.getAllMoves(nextPlayer);
    if (newMoves.isEmpty()) { return 0; }
    float[] scores = new float[newMoves.size()];
    for (int i = 0; i < newMoves.size(); ++i) {
      scores[i] = scoreMove(newMoves.get(i), depth + 1, nextPlayer, newBoard);
    }

    // Find optimal score for this move.
    float optScore;
    if (currPlayer != type) {
      optScore = MIN_SCORE;
      for (float i : scores) {    // maximize score
        optScore = i > optScore ? i : optScore;
      }
    } else {
      optScore = MAX_SCORE;
      for (float i : scores) {    // minimize score
        optScore = i < optScore ? i : optScore;
      }
    }
    return optScore;
  }
}
