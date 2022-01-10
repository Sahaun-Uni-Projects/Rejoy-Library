package com.example.rejoylibrary;

import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;

public class Author implements Serializable {
    String name, description, dbKey;

    public Author(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static Author build(DataSnapshot snapshot) {
        String name = snapshot.child("name").getValue().toString();
        String description = snapshot.child("description").getValue().toString();

        Author author = new Author(name, description);
        author.setDbKey(snapshot.getKey());

        return author;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
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
        return "Author{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
