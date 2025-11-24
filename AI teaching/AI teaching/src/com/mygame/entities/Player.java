package com.mygame.entities;

import com.mygame.main.MainFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Player {

    private int x, y;
    private int dx, dy;
    private final int SPEED = 5; 
    private final int SIZE = 30; // 飛機寬高
    
    // 射擊邏輯
    private boolean isShooting = false;
    private long lastShotTime = -2000;
    private final long SHOOT_COOLDOWN = 200;
    
    // 垂直移動上限
    private final int MAX_Y_TOP = 400; 

    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.dx = 0;
        this.dy = 0;
    }

    // 遊戲邏輯更新 (已包含邊界限制)
    public void update() {
        x += dx * SPEED;
        y += dy * SPEED;
        
        // --- 水平邊界檢查 ---
        if (x < 0) x = 0;
        
        final int MAX_X = MainFrame.GAME_WIDTH - SIZE;
        if (x > MAX_X) x = MAX_X;
        
        // --- 垂直邊界檢查 ---
        if (y < MAX_Y_TOP) y = MAX_Y_TOP; 
        
        final int MAX_Y_BOTTOM = MainFrame.GAME_HEIGHT - SIZE;
        if (y > MAX_Y_BOTTOM) y = MAX_Y_BOTTOM;
    }

    public void draw(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillRect(x, y, SIZE, SIZE);
    }

    // 鍵盤按下事件 (修正後的版本)
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        // --- A. 移動邏輯 (水平) ---
        // 必須使用獨立的 if 判斷，確保左右鍵同時按下時能正確設定 dx
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            dx = -1; 
        } 
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            dx = 1; 
        }
        
        // --- B. 移動邏輯 (垂直) ---
        // 必須使用獨立的 if 判斷
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
            dy = -1;
        } 
        if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
            dy = 1;
        }
        
        // --- C. 射擊邏輯 ---
        // 必須與移動邏輯完全獨立，使用獨立的 if 判斷
        if (key == KeyEvent.VK_SPACE) {
            isShooting = true;
        }
    }
    // 鍵盤釋放事件 (修正後的版本)
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        // --- A. 停止水平移動 ---
        // 檢查釋放的鍵是否與當前移動方向一致，如果是，則停止該方向的移動。
        // 注意：這裡不使用 else if，但用邏輯判斷避免衝突。
        if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) && dx < 0) {
            dx = 0;
        } 
        if ((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) && dx > 0) {
            dx = 0;
        }
        
        // --- B. 停止垂直移動 ---
        if ((key == KeyEvent.VK_UP || key == KeyEvent.VK_W) && dy < 0) {
            dy = 0;
        } 
        if ((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) && dy > 0) {
            dy = 0;
        }

        // --- C. 停止射擊 ---
        if (key == KeyEvent.VK_SPACE) {
            isShooting = false;
        }
    }
    
    public boolean isShooting() {
        // 獲取當前時間
        long currentTime = System.currentTimeMillis();
        
        // 判斷是否正在按住空格鍵 (isShooting) 且冷卻時間已過
        if (isShooting && (currentTime - lastShotTime > SHOOT_COOLDOWN)) {
            // 更新上次射擊時間
            lastShotTime = currentTime; 
            return true; // 允許發射
        }
        return false; // 不發射
    }
    
    public int getBulletStartX() {
        return x + (SIZE / 2);
    }
    public int getBulletStartY() {
        return y;
    }
    
    // [Getter for Collision]
    public java.awt.Rectangle getBounds() {
        return new java.awt.Rectangle(x, y, SIZE, SIZE);
    }
}
