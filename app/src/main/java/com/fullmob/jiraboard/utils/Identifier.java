package com.fullmob.jiraboard.utils;

import com.fullmob.jiraboard.data.Board;

public class Identifier {
    private final Board project;

    public Identifier(Board project) {
        this.project = project;
    }

    public boolean isColumn(String text) {
        return !text.toLowerCase().trim().startsWith(project.getPrefix().toLowerCase().trim());
    }
}
