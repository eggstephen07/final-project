package com.mygame.main;

import com.mygame.entities.Bullet;
import com.mygame.entities.Enemy;
import com.mygame.entities.Player;
import com.mygame.managers.ScoreManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {
    
    private final int DELAY = 16; // 約 60 FPS
    private Timer timer;
    
    private Player player; 
    private ArrayList<Bullet> bullets; 
    private ArrayList<Enemy> enemies; // 敵機列表
    private ScoreManager scoreManager;
    
    // 敵機生成間隔控制變數
    private long lastEnemySpawnTime = 0;
    private final long ENEMY_SPAWN_INTERVAL = 1000; // 每 1 秒生成一架敵機
    
    public GamePanel() {
        setPreferredSize(new Dimension(MainFrame.GAME_WIDTH, MainFrame.GAME_HEIGHT));
        setBackground(Color.BLACK);
        
        addKeyListener(new TAdapter());
        setFocusable(true);
        
        initGame();
    }
    
    private void initGame() {
        player = new Player(MainFrame.GAME_WIDTH / 2 - 15, MainFrame.GAME_HEIGHT - 50); 
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        scoreManager = new ScoreManager(); // <--- 初始化分數管理器
        
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame(); 
        repaint();
    }
    
    private void updateGame() {
        // 1. 更新玩家
        player.update();
        
        // 2. 處理玩家射擊
        if (player.isShooting()) {
            bullets.add(new Bullet(player.getBulletStartX(), player.getBulletStartY()));
        }

        // 3. 敵機生成邏輯
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastEnemySpawnTime > ENEMY_SPAWN_INTERVAL) {
            enemies.add(new Enemy());
            lastEnemySpawnTime = currentTime;
        }

        // 4. 更新子彈位置並移除
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            bullet.update();
            if (!bullet.isVisible()) {
                bulletIterator.remove();
            }
        }
        
        // 5. 更新敵機位置並移除
        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            enemy.update();
            
            if (!enemy.isVisible()) { 
                enemyIterator.remove();
            }
        }
        
        // 6. 碰撞檢測 <--- 新增核心邏輯
        checkCollisions();
    }

    // 實作碰撞檢測方法
    private void checkCollisions() {
        // 迭代所有子彈 (外部迴圈)
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            
            // 迭代所有敵機 (內部迴圈)
            Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
                
            // 判斷子彈和敵機的矩形區域是否相交
            if (bullet.getBounds().intersects(enemy.getBounds())) {
                    
                // 發生碰撞！
                    
                // A. 處理敵機銷毀：設定敵機為非活躍，等待在 updateGame() 中被移除
                enemy.setVisible(false);
                    
                // B. 處理子彈銷毀：設定子彈為非活躍，等待在 updateGame() 中被移除
                bullet.setVisible(false);
                    
                // 【重要】由於敵機已經被擊中，不需要再與其他子彈進行比較
                // 可以直接跳出內部迴圈，檢查下一發子彈。
                scoreManager.addScore();
                break; 
            }
        }
    }
        
        // 【TODO: 接下來將加入分數管理和玩家與敵機的碰撞】
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // 1. 繪製玩家
        player.draw(g);
        
        // 2. 繪製子彈
        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }

        // 3. 繪製敵機
        for (Enemy enemy : enemies) {
            enemy.draw(g);
        }
        
        g.setColor(Color.WHITE);
        g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 18));
        String scoreText = "Score: " + scoreManager.getScore();
        
        // 繪製在右上角 (寬度 - 100, 頂部 + 20)
        g.drawString(scoreText, MainFrame.GAME_WIDTH - 100, 20); 
        
        java.awt.Toolkit.getDefaultToolkit().sync();
    }

    // 鍵盤輸入處理類別
    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e); 
        }

        @Override
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }
    }
}
