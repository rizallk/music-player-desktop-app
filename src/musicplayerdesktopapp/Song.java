package musicplayerdesktopapp;

public class Song {
    private final String title;
    private final String artist;
    private final String album;
    private final String genre;
    private final String duration;
    private final String filePath;

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

