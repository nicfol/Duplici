package com.nicfol.duplici;

/**
 * Created by Nicolai on 26-07-2017.
 */

public class Paste {

    private String label;
    private String clip;

    public Paste(String label, String clip) {
        this.label = label;
        this.clip = clip;
    }

    public String getClip() {
        return clip;
    }

    public void setClip(String clip) {
        this.clip = clip;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
