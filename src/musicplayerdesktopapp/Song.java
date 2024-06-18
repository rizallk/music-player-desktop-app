package musicplayerdesktopapp;

public class Song {
    private String title;
    private String artist;
    private String album;
    private String genre;
    private String duration;
    private String filePath;

    public Song(String title, String artist, String album, String genre, String duration, String filePath) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.genre= genre;
        this.duration = duration;
        this.filePath = filePath;
    }

    // Getters for the fields
    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getGenre() {
        return genre;
    }

    public String getDuration() {
        return duration;
    }

    public String getFilePath() {
        return filePath;
    }
}

