package musicplayerdesktopapp;

public class Song {
    private String play;
    private String title;
    private String artist;
    private String duration;
    private String filePath;

    public Song(String play, String title, String artist, String duration, String filePath) {
        this.play = play;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.filePath = filePath;
    }

    public String getPlay() {
        return play;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getDuration() {
        return duration;
    }

    public String getFilePath() {
        return filePath;
    }
}
