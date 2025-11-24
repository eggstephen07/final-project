package com.mygame.managers;

public class ScoreManager {
    
    private int score = 0;
    private final int ENEMY_SCORE_VALUE = 10; // 擊毀一架敵機獲得 10 分

    public void addScore() {
        score += ENEMY_SCORE_VALUE;
    }

    public int getScore() {
        return score;
    }

    public void resetScore() {
        score = 0;
    }
}
