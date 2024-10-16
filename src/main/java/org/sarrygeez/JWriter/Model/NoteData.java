package org.sarrygeez.JWriter.Model;

@SuppressWarnings("unused")
public class NoteData {
    private final int id;
    private String title;
    private String dateCreated;
    private String content;

    public NoteData(int id, String title, String dateCreated, String content) {
        this.id = id;
        this.title = title;
        this.dateCreated = dateCreated;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
