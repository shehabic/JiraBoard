package com.fullmob.jiraboard.transitions;

/**
 * Created by shehabic on 27/03/2017.
 */
public class TransitionStep {
    public final String viaId;
    public final String viaName;
    public final String toId;
    public final String toName;
    public final String fromColor;
    public final String toColor;

    public TransitionStep(String viaId, String viaName, String toId, String toName, String fromColor, String toColor) {
        this.viaId = viaId;
        this.viaName = viaName;
        this.toName = toName;
        this.toId = toId;
        this.fromColor = fromColor;
        this.toColor = toColor;
    }
}
