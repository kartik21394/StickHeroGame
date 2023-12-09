package com.example.apgame;

import java.io.Serializable;


public class SavedGames implements Serializable {
    public String getHighestscore() {
        return highestscore;
    }

    public void setHighestscore(String highestscore) {
        this.highestscore = highestscore;
    }

    private String highestscore;

    public SavedGames() {
        highestscore="0";
    }
}
