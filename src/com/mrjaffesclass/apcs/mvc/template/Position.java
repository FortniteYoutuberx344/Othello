/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mrjaffesclass.apcs.mvc.template;

/**
 *
 * @author tonys
 */
public class Position {
    //created to keep track of pieces
    private int x;
    private int y;
    
    public Position(int xPos, int yPos) {
        x = xPos;
        y = yPos;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public boolean isOffBoard() {
        return (this.x < 0 || this.x >= Constants.SIZE ||
          this.y < 0 || this.y >= Constants.SIZE);
    }
    
    public Position translate(Position vector) {
        return new Position(this.x + vector.getX(), this.y + vector.getY());
    }
}
