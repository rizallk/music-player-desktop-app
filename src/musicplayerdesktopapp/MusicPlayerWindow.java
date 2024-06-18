package musicplayerdesktopapp;

import java.io.File;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicPlayerWindow {
    private MediaPlayer mediaPlayer;
    private final List<Song> songList;
    private int currentIndex;
    private Label nowPlayingLabel; 
    private Label artistLabel;
    private Label albumLabel;
    private Label genreLabel;
    private Slider progressSlider; 
    private Label totalTimeLabel;
    private Label currentTimeLabel;

    public MusicPlayerWindow(MediaPlayer mediaPlayer, List<Song> songList, int currentIndex) {
        this.mediaPlayer = mediaPlayer;
        this.songList = songList;
        this.currentIndex = currentIndex;
    }

    public BorderPane getView() {
        BorderPane pane = new BorderPane();
        
        int iconHeight = 16;
        boolean iconRatio = true;
        
        nowPlayingLabel = new Label(); // Inisialisasi label
        artistLabel = new Label(); // Inisialisasi label artist
        albumLabel = new Label(); // Inisialisasi label album
        genreLabel = new Label(); // Inisialisasi label genre
        
        nowPlayingLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        nowPlayingLabel.setPadding(new Insets(0, 0, 10, 0));
        
        updateNowPlayingLabel(); // Setel judul lagu yang sedang diputar saat ini
        pane.setTop(nowPlayingLabel); // Tambahkan label ke atas BorderPane

        Button btnPlay = new Button();
        ImageView playIcon2 = new ImageView(new Image("file:src/images/play-icon.png"));
        playIcon2.setFitHeight(iconHeight);
        playIcon2.setPreserveRatio(iconRatio);
        btnPlay.setGraphic(playIcon2);
        btnPlay.setOnAction(e -> mediaPlayer.play());

        Button btnPause = new Button();
        ImageView pauseIcon = new ImageView(new Image("file:src/images/pause-icon.png"));
        pauseIcon.setFitHeight(iconHeight);
        pauseIcon.setPreserveRatio(iconRatio);
        btnPause.setGraphic(pauseIcon);
        btnPause.setOnAction(e -> mediaPlayer.pause());

        Button btnStop = new Button();
        ImageView stopIcon = new ImageView(new Image("file:src/images/stop-icon.png"));
        stopIcon.setFitHeight(iconHeight);
        stopIcon.setPreserveRatio(iconRatio);
        btnStop.setGraphic(stopIcon);
        btnStop.setOnAction(e -> mediaPlayer.stop());

        Button btnPrevious = new Button();
        ImageView prevIcon = new ImageView(new Image("file:src/images/previous-icon.png"));
        prevIcon.setFitHeight(iconHeight);
        prevIcon.setPreserveRatio(iconRatio);
        btnPrevious.setGraphic(prevIcon);
        btnPrevious.setOnAction(e -> playPreviousSong());

        Button btnNext = new Button();
        ImageView nextIcon = new ImageView(new Image("file:src/images/next-icon.png"));
        nextIcon.setFitHeight(iconHeight);
        nextIcon.setPreserveRatio(iconRatio);
        btnNext.setGraphic(nextIcon);
        btnNext.setOnAction(e -> playNextSong());
        
        Label volumeLabel = new Label();
        ImageView volumeIcon = new ImageView(new Image("file:src/images/volume-icon.png"));
        volumeIcon.setFitHeight(iconHeight);
        volumeIcon.setPreserveRatio(iconRatio);
        volumeLabel.setGraphic(volumeIcon);

        Slider volumeSlider = new Slider(0, 100, 50);
        mediaPlayer.setVolume(0.5);
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> 
            mediaPlayer.setVolume(newVal.doubleValue() / 100));

        HBox controls = new HBox(10, btnPrevious, btnPlay, btnPause, btnStop, btnNext, volumeLabel, volumeSlider);
        controls.setAlignment(Pos.CENTER);
        
        VBox metadata = new VBox(10, artistLabel, albumLabel, genreLabel);
        pane.setCenter(metadata);
        
        progressSlider = new Slider();
        progressSlider.setMin(0);
        progressSlider.setMax(100);
        progressSlider.setPrefWidth(300);
        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            progressSlider.setValue(newValue.toSeconds());
            updateCurrentTimeLabel(newValue);
        });
        progressSlider.setOnMousePressed(event -> {
            mediaPlayer.seek(javafx.util.Duration.seconds(progressSlider.getValue()));
        });
        progressSlider.setOnMouseDragged(event -> {
            mediaPlayer.seek(javafx.util.Duration.seconds(progressSlider.getValue()));
        });

        currentTimeLabel = new Label("0:00");
        totalTimeLabel = new Label(formatDuration(mediaPlayer.getTotalDuration()));
        
        HBox timeAndProgressBar = new HBox(10, currentTimeLabel, progressSlider, totalTimeLabel);
        timeAndProgressBar.setAlignment(Pos.CENTER);
        
        VBox bottom = new VBox(10, timeAndProgressBar, controls);
        bottom.setAlignment(Pos.CENTER);
        pane.setBottom(bottom);
        
        BorderPane.setAlignment(bottom, Pos.CENTER);

        mediaPlayer.setOnReady(() -> {
            totalTimeLabel.setText(formatDuration(mediaPlayer.getTotalDuration()));
            progressSlider.setMax(mediaPlayer.getTotalDuration().toSeconds());
        });
        
        pane.setPadding(new Insets(10));

        return pane;
    }

    private void playPreviousSong() {
        if (currentIndex > 0) {
            currentIndex--;
            playSongAtCurrentIndex();
        }
    }

    private void playNextSong() {
        if (currentIndex < songList.size() - 1) {
            currentIndex++;
            playSongAtCurrentIndex();
        }
    }
    
     private void updateNowPlayingLabel() {
        if (currentIndex >= 0 && currentIndex < songList.size()) {
            Song currentSong = songList.get(currentIndex);
            nowPlayingLabel.setText("Now Playing: " + currentSong.getTitle()); // Setel teks label dengan judul lagu
        
            // Menampilkan metadata 
            if (currentSong.getArtist() != null) {
               artistLabel.setText("Artist: " + currentSong.getArtist());
            } else {
               artistLabel.setText("Artist: Unknown");
            }
            if (currentSong.getAlbum()!= null) {
               albumLabel.setText("Album: " + currentSong.getAlbum());
            } else {
               albumLabel.setText("Album: Unknown");
            }
            if (currentSong.getGenre()!= null) {
               genreLabel.setText("Genre: " + currentSong.getGenre());
            } else {
               genreLabel.setText("Genre: Unknown");
            }
        } else {
            nowPlayingLabel.setText("Now Playing: Unknown"); // Jika tidak ada lagu yang diputar, setel teks label menjadi "Unknown"
        }
    }

    private void playSongAtCurrentIndex() {
        // Hentikan pemutaran sebelumnya
        mediaPlayer.stop();
            
        Song song = songList.get(currentIndex);
        MediaPlayer newMediaPlayer = new MediaPlayer(new Media(new File(song.getFilePath()).toURI().toString()));
        newMediaPlayer.setOnReady(() -> {
            newMediaPlayer.play();
            updateNowPlayingLabel(); // Perbarui label setelah memutar lagu baru
            updateCurrentTimeLabel(newMediaPlayer.currentTimeProperty().get()); // Perbarui label waktu saat ini
            progressSlider.setMax(newMediaPlayer.getTotalDuration().toSeconds());
            progressSlider.setValue(0); // Set nilai awal ke 0
            newMediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                progressSlider.setValue(newValue.toSeconds());
                updateCurrentTimeLabel(newValue);
            });
            // Perbarui label total durasi
            totalTimeLabel.setText(formatDuration(newMediaPlayer.getTotalDuration()));
        });
        mediaPlayer = newMediaPlayer;
    }
    
    // ===================================================================
    
    private void updateCurrentTimeLabel(javafx.util.Duration currentTime) {
    int currentSeconds = (int) currentTime.toSeconds();
    int minutes = currentSeconds / 60;
    int seconds = currentSeconds % 60;
    currentTimeLabel.setText(String.format("%d:%02d", minutes, seconds));
}
    
    private String formatDuration(javafx.util.Duration duration) {
        int totalSeconds = (int) duration.toSeconds();
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }
}