package musicplayerdesktopapp;

import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class MusicPlayerWindow {
    private MediaPlayer mediaPlayer;
    private List<Song> songList;
    private int currentIndex;
    private Label nowPlayingLabel; // Menambahkan label untuk menampilkan judul lagu saat ini

    public MusicPlayerWindow(MediaPlayer mediaPlayer, List<Song> songList, int currentIndex) {
        this.mediaPlayer = mediaPlayer;
        this.songList = songList;
        this.currentIndex = currentIndex;
    }

    public BorderPane getView() {
        BorderPane pane = new BorderPane();
        
        nowPlayingLabel = new Label(); // Inisialisasi label
        updateNowPlayingLabel(); // Setel judul lagu yang sedang diputar saat ini
        pane.setTop(nowPlayingLabel); // Tambahkan label ke atas BorderPane

        Button btnPlay = new Button("Play");
        btnPlay.setOnAction(e -> mediaPlayer.play());

        Button btnPause = new Button("Pause");
        btnPause.setOnAction(e -> mediaPlayer.pause());

        Button btnStop = new Button("Stop");
        btnStop.setOnAction(e -> mediaPlayer.stop());

        Button btnPrevious = new Button("Previous");
        btnPrevious.setOnAction(e -> playPreviousSong());

        Button btnNext = new Button("Next");
        btnNext.setOnAction(e -> playNextSong());

        Slider volumeSlider = new Slider(0, 100, 50);
        mediaPlayer.setVolume(0.5);
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> 
            mediaPlayer.setVolume(newVal.doubleValue() / 100));

        Button btnReset = new Button("Reset");
        btnReset.setOnAction(e -> mediaPlayer.seek(mediaPlayer.getStartTime()));

        HBox controls = new HBox(10, btnPrevious, btnPlay, btnPause, btnStop, btnNext, volumeSlider, btnReset);
        pane.setCenter(controls);
        
        // Menambahkan event handler untuk menutup pemutar musik saat jendela ditutup
        Stage stage = (Stage) pane.getScene().getWindow();
        stage.setOnCloseRequest(event -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
        });

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
        } else {
            nowPlayingLabel.setText("Now Playing: Unknown"); // Jika tidak ada lagu yang diputar, setel teks label menjadi "Unknown"
        }
    }

    private void playSongAtCurrentIndex() {
        // Hentikan pemutaran sebelumnya
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        Song song = songList.get(currentIndex);
        MediaPlayer newMediaPlayer = new MediaPlayer(new javafx.scene.media.Media(new java.io.File(song.getFilePath()).toURI().toString()));
        newMediaPlayer.setOnReady(() -> {
            newMediaPlayer.play();
            updateNowPlayingLabel(); // Perbarui label setelah memutar lagu baru
        });
        mediaPlayer = newMediaPlayer;
    }
}
