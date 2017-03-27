package com.fullmob.jiraboard.transitions;

/**
 * Created by shehabic on 27/03/2017.
 */
class TransitionStep {
    private final String viaId;
    private final String viaName;
    private final String toId;
    private final String toName;
    private final String fromColor;
    private final String toColor;

    public TransitionStep(String viaId, String viaName, String toId, String toName, String fromColor, String toColor) {
        this.viaId = viaId;
        this.viaName = viaName;
        this.toName = toName;
        this.toId = toId;
        this.fromColor = fromColor;
        this.toColor = toColor;
    }
}
