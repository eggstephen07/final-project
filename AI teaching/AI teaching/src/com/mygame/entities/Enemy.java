package com.mygame.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

import com.mygame.main.MainFrame;

public class Enemy {

    private int x, y;
    private final int SPEED = 2;
    private final int SIZE = 40;
    private boolean visible;
    
    private static final Random random = new Random(); 

    public Enemy() {
        this.visible = true;
        this.y = -SIZE; 
        
        // 隨機生成 X 座標
        this.x = random.nextInt(MainFrame.GAME_WIDTH - SIZE); 
    }

    public void update() {
        y += SPEED;

        // 檢查是否飛出螢幕底部
        if (y > MainFrame.GAME_HEIGHT) {
            visible = false;
        }
    }

    public void draw(Graphics g) {
        if (visible) {
            g.setColor(Color.RED);
            g.fillRect(x, y, SIZE, SIZE);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, SIZE, SIZE);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
