package com.phacsin.entreprenia.main.speeker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Bineesh P Babu on 20-08-2016.
 */
public class Friend {
    private String avatar;
    private String nickname,designation,event;
    private int background;
    public Friend(String avatar, String nickname, int background, String designation,String event) {
        this.avatar = avatar;
        this.nickname = nickname;
        this.background = background;
        this.designation = designation;
        this.event = event;

    }

    public String getAvatar() {
        return avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public int getBackground() {
        return background;
    }

    public String getDesignation() {
        return designation;
    }

    public String getEvent() {
        return event;
    }
}
