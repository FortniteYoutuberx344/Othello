/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mrjaffesclass.apcs.mvc.template;

import com.mrjaffesclass.apcs.messenger.MessageHandler;
import com.mrjaffesclass.apcs.messenger.Messenger;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author tonys
 */
public class DrawPanel extends JPanel implements MessageHandler {
    
    // created to paint on the jPanel using the graphics library (since we need to define functions to paint on it)
    // we cannot use the paint functions outside of this class because Graphics is an abstract library and we cannot instantiate it as an object, we can only subclass it
    
    // using messaging to keep painting the gui accurately
    private Messenger mvcMessaging;
    
    private int[][] board;
    
    public DrawPanel(Messenger messages) {
        this.mvcMessaging = messages;
        this.init();
    }
    
    public void init() {
        this.mvcMessaging.subscribe("model:boardChange", this);
    }

    // this is the main function to paint on the jPanel it updates board and draws the grid lines 
    // each tile is 50x50
    @Override
    public void paintComponent(Graphics g) {
        updateBoard(g);
        g.setColor(Color.gray);
        for (int y = 50; y <= 400; y += 50) {
            g.drawLine(0, y, 400, y);
        }
        for (int x = 50; x <= 400; x += 50) {
            g.drawLine(x, 0, x, 400);
        }
    }
    
    // paints on the gui in the corresponding coordinates to the index of the board 
    // each square is 50x50, so its coordinates will correspond to 50 * coordinates
    public void updateBoard(Graphics g) {
        for (int row = 0; row < Constants.SIZE; row++) {
            for (int col = 0; col < Constants.SIZE; col++) {
                // condition to change fill color here
                if (this.board[row][col] == Constants.BLACK) {
                    g.setColor(Color.black);
                    g.fillRect(50 * col + 1, 50 * row + 1, 49, 49);
                } else if (this.board[row][col] == Constants.WHITE) {
                    g.setColor(Color.white);
                    g.fillRect(50 * col + 1, 50 * row + 1, 49, 49);
                } else {
                    g.setColor(Color.green);
                    g.fillRect(50 * col + 1, 50 * row + 1, 49, 49);
                }
            }
        }
    }
    
    @Override
    public void messageHandler(String messageName, Object messagePayload) {
        if (messageName.equals("model:boardChange")) {
            this.board = (int[][]) messagePayload;
            this.repaint(); //calls the paint function 
        }
    }
    
}