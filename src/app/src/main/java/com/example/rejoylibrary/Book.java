package com.example.rejoylibrary;

import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;

public class Book implements Serializable {
    private String name, coverImage, description, dbKey;
    private Author author;

    public Book(String name, Author author, String coverImage, String description) {
        this.name = name;
        this.author = author;
        this.coverImage = coverImage;
        this.description = description;
    }

    public static Book build(DataSnapshot snapshot) {
        String name = snapshot.child("name").getValue().toString();
        Author author = Data.getAuthor(snapshot.child("authorKey").getValue().toString());
        String coverImage = snapshot.child("coverImage").getValue().toString();
        String description = snapshot.child("description").getValue().toString();

        Book book = new Book(name, author, coverImage, description);
        book.setDbKey(snapshot.getKey());

        return book;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDbKey() {
        return this.dbKey;
    }

    public void setDbKey(String dbKey) {
        this.dbKey = dbKey;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", coverImage='" + coverImage + '\'' +
                '}';
    }
}
