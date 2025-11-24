package com.mygame.main;

import javax.swing.JFrame;

public class MainFrame extends JFrame {

    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 600;

    public MainFrame() {
        setTitle("Java Space Shooter");
        setSize(GAME_WIDTH, GAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // 創建並加入遊戲面板
        GamePanel gamePanel = new GamePanel();
        this.add(gamePanel);
        
        setVisible(true);
        
        // 確保在視窗顯示後請求焦點，讓鍵盤監聽生效
        gamePanel.requestFocusInWindow(); 
    }
}
