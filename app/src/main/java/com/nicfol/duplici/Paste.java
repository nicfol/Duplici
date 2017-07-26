package com.nicfol.duplici;

/**
 * Created by Nicolai on 26-07-2017.
 */

public class Paste {

    private String label;
    private String text;

    public Paste(String label, String text) {
        this.label = label;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setClip(String clip) {
        this.text = clip;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
