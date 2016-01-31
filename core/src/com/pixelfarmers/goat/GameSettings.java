package com.pixelfarmers.goat;


public class GameSettings {
    public enum Difficulty {
        NORMAL,
        HARDCORE
    }

    public enum BloodLevel {
        NO_BLOOD,
        NORMAL
    }

    private static GameSettings instance;

    public static GameSettings getInstance() {
        if (instance == null) {
            instance = new GameSettings();
        }
        return instance;
    }

    private BloodLevel bloodLevel = BloodLevel.NORMAL;
    private Difficulty difficulty = Difficulty.NORMAL;

    public BloodLevel getBloodLevel() {
        return bloodLevel;
    }

    public void setBloodLevel(BloodLevel bloodLevel) {
        this.bloodLevel = bloodLevel;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }
}
