package com.fullmob.jiraboard.data;

public class Ticket extends Column {
    private String columnName;

    public Ticket(String txt) {
        this.text = txt;
        columnName = "";
    }

    public Ticket() {
        this(null);
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
}
