/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mrjaffesclass.apcs.mvc.template;

/**
 *
 * @author tonys
 */
public class BoardInfo {
    // class created to store the info related to the game 
    
    private boolean turn;
    private int blackTiles;
    private int whiteTiles;
    private String gameStatus;
    
    public BoardInfo(boolean whoseTurn, int blackNum, int whiteNum, String status) {
        this.turn = whoseTurn;
        this.blackTiles = blackNum;
        this.whiteTiles = whiteNum;
        this.gameStatus = status;
    }
    
    public boolean getTurn() {
        return this.turn;
    }
    
    public int getBlackCount() {
        return this.blackTiles;
    }
    
    public int getWhiteCount() {
        return this.whiteTiles;
    }
    
    public String getStatus() {
        return this.gameStatus;
    }
}
