package com.fullmob.multiqr.utils;

import com.fullmob.multiqr.data.Project;

public class Identifier {
    private final Project project;

    public Identifier(Project project) {
        this.project = project;
    }

    public boolean isColumn(String text) {
        return !text.toLowerCase().trim().startsWith(project.getPrefix().toLowerCase().trim());
    }
}
