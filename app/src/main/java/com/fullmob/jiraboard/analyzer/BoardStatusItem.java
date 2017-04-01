package com.fullmob.jiraboard.analyzer;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shehabic on 01/04/2017.
 */
public class BoardStatusItem {
    @SerializedName("name")
    public final String name;

    @SerializedName("id")
    public final String id;

    public BoardStatusItem() {
        this.name = null;
        this.id = null;
    }
}
