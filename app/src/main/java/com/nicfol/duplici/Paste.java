package com.nicfol.duplici;

public class Paste {

    private String label;
    private String text;
    private int icon;
    private int dbID;

    public Paste(int dbID, String label, String text, int icon) {
        this.label = label;
        this.text = text;
        this.icon = icon;
        this.dbID = dbID;
    }

    public String getText() { return text; }

    public void setText(String text) { this.text = text; }

    public String getLabel() { return label; }

    public void setLabel(String label) { this.label = label; }

    public int getIcon() { return icon; }

    public void setIcon(int icon) { this.icon = icon; }

    public int getDbID() { return dbID; }

}
