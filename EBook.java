package library;

public class EBook extends LibraryItems {
    private String fileFormat;
    private String downloadLink;

    public EBook() {}

    public EBook(int id, String title, String author, String genre,
                 double price, boolean availability,
                 String fileFormat, String downloadLink) {
        super(id, title, author, genre, "EBook", price, availability);
        this.fileFormat = fileFormat;
        this.downloadLink = downloadLink;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public String getDownloadLink() {
        return downloadLink;
    }
}
