package org.avmrus.fopds;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Book {
    private String id;
    private String title;
    private ArrayList<Author> authors;
    private ArrayList<String> categories;
    private String language;
    private String content;
    private String coverUrl;
    private ArrayList<DownloadRecord> downloadUrls;
    private Bitmap cover;

    public Book() {
        super();
        this.authors = new ArrayList<>();
        this.categories = new ArrayList<>();
        downloadUrls = new ArrayList<>();
        this.cover = null;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Author> getAuthors() {
        return this.authors;
    }


    public ArrayList<String> getCategories() {
        return this.categories;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getCoverUrl() {
        return this.coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public ArrayList<DownloadRecord> getDownloadUrls() {
        return this.downloadUrls;
    }

    public Bitmap getCover() {
        return cover;
    }

    public void setCover(Bitmap cover) {
        this.cover = cover;
    }

    public Author getFirstAuthor() {
        Author author;
        if (this.authors.size() > 0) {
            author = this.authors.get(0);
        } else {
            author = new Author("Unknown author");
        }
        return author;
    }
}
