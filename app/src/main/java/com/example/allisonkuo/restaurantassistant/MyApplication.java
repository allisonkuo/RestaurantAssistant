package com.example.allisonkuo.restaurantassistant;
import android.app.Application;

public class MyApplication extends Application {

    private int playerId;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int id) {
        this.playerId = id;
    }
}
