package cl.alfa.alfalab.models;

import androidx.annotation.NonNull;

public class Message {

    private String title, content, created_at;
    private User author;

    public Message() {
    }

    public Message(@NonNull String title, String content, @NonNull String created_at, User author) {
        this.title = title;
        this.content = content;
        this.created_at = created_at;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return content;
    }

    public void setMessage(String content) {
        this.content = content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
