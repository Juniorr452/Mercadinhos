package com.mobile.pid.pid.home.perfil;

import android.media.Image;

/**
 * Created by jonasramos on 19/03/18.
 */

public class FollowItem {

    private String iconUser_URL;
    private String user_name;
    private String user;

    public FollowItem(String iconUser_URL, String user_name, String user) {
        this.iconUser_URL = iconUser_URL;
        this.user_name = user_name;
        this.user = user;
    }

    public String getIconUser_URL() {
        return iconUser_URL;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser() {
        return user;
    }
}
