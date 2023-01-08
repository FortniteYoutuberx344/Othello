package com.mrjaffesclass.apcs.mvc.template;

import com.mrjaffesclass.apcs.messenger.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The model represents the data that the app uses.
 * @author Roger Jaffe
 * @version 1.0
 */
public class Model implements MessageHandler {

  // Messaging system for the MVC
  private final Messenger mvcMessaging;

  // Model's data variables
  private int[][] board;
  private boolean whoseTurn;

  /**
   * Model constructor: Create the data representation of the program
   * @param messages Messaging class instantiated by the Controller for 
   *   local messages between Model, View, and controller
   */
  public Model(Messenger messages) {
    this.mvcMessaging = messages;
    this.board = new int[Constants.SIZE][Constants.SIZE];
  }
  
  /**
   * Initialize the model here and subscribe to any required messages
   */
  public void init() {
    this.mvcMessaging.subscribe("view:boardChange", this);
    this.mvcMessaging.subscribe("view:newGame", this);
    this.newGame();
  }
  
  private void newGame() {
      whoseTurn = true;
      for (int row = 0; row < Constants.SIZE; row++) {
          for (int col = 0; col < Constants.SIZE; col++) {
              this.board[row][col] = Constants.EMPTY;
          }
      }
      this.board[3][3] = Constants.WHITE; // set the middle squares like how they are in othello
      this.board[4][4] = Constants.WHITE;
      this.board[3][4] = Constants.BLACK;
      this.board[4][3] = Constants.BLACK;
      
//      Check for tie game
//      for (int row = 0; row < Constants.SIZE; row++) {
//          for (int col = 0; col < Constants.SIZE; col++) {
//              if (row < 4) {
//                  this.board[row][col] = Constants.BLACK;
//              } else {
//                  this.board[row][col] = Constants.WHITE;
//              }
//          }
//      }
//      this.board[3][6] = Constants.WHITE;
//      this.board[3][7] = Constants.EMPTY;
      this.mvcMessaging.notify("model:boardChange", this.board); // set a message with the board to drawpanel to get the board displayed on the gui
  }
  
  @Override
  public void messageHandler(String messageName, Object messagePayload) {
    if (messagePayload != null) {
      System.out.println("MSG: received by model: "+messageName+" | "+messagePayload.toString());
    } else {
      System.out.println("MSG: received by model: "+messageName+" | No data sent");
    }
    if (messageName == "view:boardChange") {
        Position pos = (Position) messagePayload;
        if (this.makeMove(pos)) { // places the move and checks if the move has been made
            // the following will only execute if makeMove returns true, meaning that a move has been made
            this.mvcMessaging.notify("model:boardChange", this.board);
            whoseTurn = !whoseTurn; //switch turn
            BoardInfo info = new BoardInfo(whoseTurn, this.countTiles(Constants.BLACK),
                                        this.countTiles(Constants.WHITE), this.gameOverCheck()); // object that stores the turn, tile counts, and game status
            if (this.gameOverCheck() != "Continue") {
                this.mvcMessaging.notify("model:gameOver", info);
            } else {
                if (this.noMovesAvailable()) { // if the current player has no moves
                    whoseTurn = !whoseTurn; // switch turn back if the current player has no moves (skipping) their turn
                    this.mvcMessaging.notify("model:turnSkip", info);
                } else {
                    this.mvcMessaging.notify("model:newTurn", info);
                }
            }
        }
    }
   }      
  
  // checks for gameOver and returns the game's status as a string
  public String gameOverCheck() {
      int blackTiles = this.countTiles(Constants.BLACK);
      int whiteTiles = this.countTiles(Constants.WHITE);
      if (blackTiles == 0) {
          return "White wins!!";
      } else if (whiteTiles == 0) {
          return "Black wins!!";
      } else if (blackTiles + whiteTiles == 64) { // if both the player counts add up to 64, it means the board is full
          if (blackTiles > whiteTiles) {
              return "Black wins!!";
          } else if (whiteTiles > blackTiles) {
              return "White wins!!";
          } else {
              return "Tie game";
          }
      }
      return "Continue";
  }
  
  // returns the integer corresponding to the boolean whoseTurn
  public int getPlayer() {
        if (whoseTurn) {
            return Constants.BLACK;
        } else { 
            return Constants.WHITE;
        }  
  }
  
  // counts the amount of tiles a given player has by traversing board
  public int countTiles(int player) {
      int count = 0;
      for (int[] row : this.board) {
          for (int tile : row) {
              if (tile == player) {
                  count++;
              }
          }
      }
      return count;  
  }
  
  // places the current player's corresponding number on the board if the conditions are satisfied
  // checks if the move is valid (can change opponent's pieces into their own) and if place is not already taken
  public boolean makeMove(Position pos) {
        boolean moveMade = false;
        for (String compassPoint : Directions.getDirections()) {
            Position direction = Directions.getVector(compassPoint);
            boolean validMove = checkDirection(pos, direction, 0);            
            if (validMove && this.board[pos.getY()][pos.getX()] == Constants.EMPTY) {
                placeMove(pos, direction);
                moveMade = true;
            }
        }
        if (moveMade) {
            this.board[pos.getY()][pos.getX()] = this.getPlayer();
        }
        return moveMade;
  }
  
  //checks if the current player has no available moves (cannot make any legal moves)
  public boolean noMovesAvailable() {
      for (int row = 0; row < Constants.SIZE; row++) {
          for (int col = 0; col < Constants.SIZE; col++) {
              Position pos = new Position(col, row);
              if (this.isLegalMove(pos)) {
                  return false;
              }
          }
      }
      return true;
  }
  // checks if the given move is legal (it is not made on a taken spot and can change opponent's pieces)
  public boolean isLegalMove(Position pos) {
      if (this.board[pos.getY()][pos.getX()] != Constants.EMPTY) {
          return false;
      }
      
      for (String direction : Directions.getDirections()) {
          Position directionVector = Directions.getVector(direction);
          if (checkDirection(pos, directionVector, 0)) {
              return true;
          }      
      }
      return false;
  }
  
  // changes all of the opponents pieces in one direction (changes all of the pieces up to your piece)
  public void placeMove(Position move, Position direction) {
    Position newPosition = move.translate(direction);
    while (this.board[newPosition.getY()][newPosition.getX()] != this.getPlayer()) {
        this.board[newPosition.getY()][newPosition.getX()] = this.getPlayer();
        newPosition = newPosition.translate(direction);
    }
  }
  
  // main function idea from Mr Jaffe's Othello
  // checks if the direction is valid (the direction has no empty spaces between the current player's pieces and has opponent's pieces in the middle)
  public boolean checkDirection(Position move, Position direction, int count) {
    Position newPosition = move.translate(direction); // adds the direction (a position) to the current position
    if (newPosition.isOffBoard()) {
        // If off the board then move is not legal
        return false;
    } 
    
    int space = this.board[newPosition.getY()][newPosition.getX()];
    if (space == Constants.EMPTY && (count == 0)) {
        // If empty space AND adjacent to position then not legal
        return false;
    } else if (space != this.getPlayer() && space != Constants.EMPTY) {
        // If space has opposing player then move to next space in same direction (uses recursion to keep moving until we hit the current player again)
        return checkDirection(newPosition, direction, count + 1);
    } else if (space == this.getPlayer()) {
        // If space has this player and we've moved more than one space then it's legal,
        // otherwise it's not legal
        return count > 0;
    } else {
        // Didn't pass any other test, not legal move
        return false;
    }
  }  
  
}


 
