package com.batchmates.android.whatsinmygithub;

import android.widget.TextView;

/**
 * Created by Android on 7/10/2017.
 */

public class GithubHolderPOJO {

    private String avatar;
    private String name;
    private String url;
    private String followers;

    public GithubHolderPOJO(String avatar, String name, String url, String followers) {
        this.avatar = avatar;
        this.name = name;
        this.url = url;
        this.followers = followers;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getFollowers() {
        return followers;
    }
}
