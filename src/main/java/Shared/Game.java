package Shared;

public class Game {

    private String title;
    private String genre;
    private String pathFile;
    private String developer;
    private int id;
    private int size;
    private int price;
    private int reviews;
    private int releaseYear;
    private boolean controllerSupport;

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", developer='" + developer + '\'' +
                ", genre='" + genre + '\'' +
                ", price=" + price +
                ", release_year=" + releaseYear +
                ", controller_support=" + controllerSupport +
                ", reviews=" + reviews +
                ", size=" + size +
                ", path_file='" + pathFile + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public boolean isControllerSupport() {
        return controllerSupport;
    }

    public void setControllerSupport(boolean controllerSupport) {
        this.controllerSupport = controllerSupport;
    }

    public int getReviews() {
        return reviews;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getPath() {
        return pathFile;
    }

    public void setPath(String path_file) {
        this.pathFile = path_file;
    }
}
