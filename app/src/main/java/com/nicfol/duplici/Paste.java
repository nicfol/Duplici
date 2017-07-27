package com.nicfol.duplici;

public class Paste {

    private String label;
    private String text;
    private int icon;

    public Paste(String label, String text, int icon) {
        this.label = label;
        this.text = text;
        this.icon = icon;
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

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

}
