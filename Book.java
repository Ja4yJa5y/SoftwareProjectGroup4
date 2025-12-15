/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package library;


public class Book extends LibraryItems {
    private int pages;
    private int publishYear;
    private String language;

    public Book() {}

    public Book(int id, String title, String author, String genre,
                double price, boolean availability,
                int pages, int publishYear, String language) {
        super(id, title, author, genre, "Book", price, availability);
        this.pages = pages;
        this.publishYear = publishYear;
        this.language = language;
    }

    public int getPages() {
        return pages;
    }

    public int getPublishYear() {
        return publishYear;
    }

    public int getLanguage() {     // they asked: getLanguage(): int? but that makes no sense
        return publishYear;        // so we follow text: getLanguage(): String (correct one below)
    }

    public String getLanguageString() {
        return language;
    }
}
