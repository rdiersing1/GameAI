package large_ttt;

import AI.ComputerPlayer;
import AI.GameFitness;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;
import java.util.Scanner;

public class Game {
  Game() {
    Map<Board, GameFitness> fitnessMap = null;
    try {
      ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File("fitnessMap.txt")));
      fitnessMap = (Map) in.readObject();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

    Board board = new Board(6);
    Player playerX = new ComputerPlayer(board, Board.X, 4);
    Player playerO = new HumanPlayer(board, Board.O);
    GameState state = GameState.ONGOING;
    Scanner in = new Scanner(System.in);
    boolean playerXTurn = true;
    ((ComputerPlayer) playerX).setFitnessMap(fitnessMap);

    while(state == GameState.ONGOING) {
      board.print();
      if (playerXTurn) {
        state = playerX.makeMove(in);
      } else {
        state = playerO.makeMove(in);
      }
      playerXTurn = (!playerXTurn);
    }

    board.print();
    switch (state) {
      case X_WON: System.out.println("Congratulations player X, you Won!"); break;
      case O_WON: System.out.println("Congratulations player O, you Won!"); break;
      case TIE: System.out.println("The game was a tie!");
    }
    board.print();
    in.close();
  }

  public static void main(String[] args) {
    new Game();
  }
}
