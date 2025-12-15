package library;

import java.util.List;

public class Search {

    public List<LibraryItems> byTitle(String title) {
        return LibraryItems.findByTitle(title);
    }

    public List<LibraryItems> byAuthor(String author) {
        return LibraryItems.findByAuthor(author);
    }

    public List<LibraryItems> byGenre(String genre) {
        return LibraryItems.findByGenre(genre);
    }

    public LibraryItems byId(int id) {
        return LibraryItems.findById(id);
    }
}
