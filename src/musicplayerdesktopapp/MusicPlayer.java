package musicplayerdesktopapp;

import javafx.geometry.Insets;
import java.io.File;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MusicPlayer {
    private TableView<Song> songTable;
    private ObservableList<Song> songList;
    private MediaPlayer mediaPlayer;

    public MusicPlayer() {
        songList = FXCollections.observableArrayList();
        songTable = new TableView<>(songList);

        TableColumn<Song, String> colPlay = new TableColumn<>("Play");
        colPlay.setCellFactory((TableColumn<Song, String> param) -> new TableCell<Song, String>() {
            final Button btn = new Button();
            final ImageView playIcon = new ImageView(new Image("file:src/images/play-icon.png"));
            
            {
                // Atur lebar gambar ikon volume dan pertahankan rasio aspeknya
                playIcon.setFitHeight(14);
                playIcon.setPreserveRatio(true);
                
                // Atur volumeIcon sebagai grafis tombol
                btn.setGraphic(playIcon);
            }
            
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    btn.setOnAction(event -> {
                        Song song = getTableView().getItems().get(getIndex());
                        handlePlaySong(song.getFilePath(), getIndex());
                    });
                    setGraphic(btn);
                    setText(null);
                }
            }
        });
        colPlay.setPrefWidth(75);

        TableColumn<Song, String> colTitle = new TableColumn<>("Title");
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colTitle.setPrefWidth(300);

        TableColumn<Song, String> colArtist = new TableColumn<>("Artist");
        colArtist.setCellValueFactory(new PropertyValueFactory<>("artist"));
        colArtist.setPrefWidth(300);

        TableColumn<Song, String> colDuration = new TableColumn<>("Duration");
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colDuration.setPrefWidth(100);

        songTable.getColumns().addAll(colPlay, colTitle, colArtist, colDuration);

        // Load songs dari default music folder
        loadSongsFromDefaultMusicFolder();
    }

    public BorderPane getView() {
        BorderPane pane = new BorderPane();
        // HBox untuk teks dan tombol di bagian atas
        HBox topControls = new HBox(10);
        topControls.setAlignment(Pos.CENTER);
        topControls.setPadding(new Insets(10));

        // Teks "Home"
        Label lblHome = new Label("Music Player");
        lblHome.setStyle("-fx-font-size: 32px; -fx-font-weight: bold");
        topControls.getChildren().add(lblHome);
        
        // Spacer agar tombol "Open File" berada di kanan
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Tombol "Open File"
        Button btnOpenFile = new Button("Open file");
        btnOpenFile.setOnAction(e -> handleOpenFile());
        topControls.getChildren().addAll(spacer, btnOpenFile);
        
         // Tombol "Open Folder"
        Button btnOpenFolder = new Button("Open Folder");
        btnOpenFolder.setOnAction(e -> handleOpenFolder());
        topControls.getChildren().add(btnOpenFolder);

        // Menempatkan HBox teks dan tombol di atas BorderPane
        pane.setTop(topControls);

        // Menambahkan daftar lagu ke dalam BorderPane di bagian bawah
        pane.setCenter(songTable);

        // Setting padding untuk pane
        pane.setPadding(new Insets(10));

        return pane;
    }

    private void handleOpenFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav"));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            handlePlaySong(file.getPath(), -1); // -1 sebagai tanda lagu tersebut tidak masuk dalam songList
        }
    }
    
    private void handleOpenFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(new Stage());
        if (selectedDirectory != null) {
            songList.clear();
            loadSongsFromFolder(selectedDirectory);
        }
    }

    private void handlePlaySong(String filePath, int index) {
        Stage stage = new Stage();
        Media media = new Media(new File(filePath).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        mediaPlayer.setOnReady(() -> {
            ObservableList<Song> tempSongList = FXCollections.observableArrayList();
            Map<String, Object> metadata = media.getMetadata();
            String title = (String) metadata.getOrDefault("title", new File(filePath).getName());
            String artist = (String) metadata.getOrDefault("artist", "Unknown Artist");
            String album = (String) metadata.getOrDefault("album", "Unknown Album");
            String genre = (String) metadata.getOrDefault("genre", "Unknown Genre");
            String duration = String.format("%d:%02d",
                    (int) media.getDuration().toMinutes(),
                    (int) (media.getDuration().toSeconds() % 60));

            if (index == -1) {
                tempSongList.add(new Song(title, artist, album, genre, duration, filePath));
            }

            MusicPlayerWindow playerWindow = new MusicPlayerWindow(mediaPlayer, index == -1 ? tempSongList : songList, index == -1 ? 0 : index);

            Scene scene = new Scene(playerWindow.getView(), 400, 250);
            stage.setTitle("Playing Music");
            stage.setScene(scene);

            stage.setOnCloseRequest(event -> mediaPlayer.stop());

            stage.show();
            mediaPlayer.play();
        });
    }


    private void loadSongsFromDefaultMusicFolder() {
        String userHome = System.getProperty("user.home");
        File musicFolder = new File(userHome, "Music");
        loadSongsFromFolder(musicFolder);
    }

    private void loadSongsFromFolder(File folder) {
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".mp3") || name.endsWith(".wav"));
        if (files != null) {
            for (File file : files) {
                Media media = new Media(file.toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setOnReady(() -> {
                    Map<String, Object> metadata = media.getMetadata();
                    String title = (String) metadata.getOrDefault("title", file.getName());
                    String artist = (String) metadata.getOrDefault("artist", "Unknown Artist");
                    String album = (String) metadata.getOrDefault("album", "Unknown Album");
                    String genre = (String) metadata.getOrDefault("genre", "Unknown Genre");
                    String duration = String.format("%d:%02d",
                            (int) media.getDuration().toMinutes(),
                            (int) (media.getDuration().toSeconds() % 60));
                    songList.add(new Song(title, artist, album, genre, duration, file.getPath()));
                });
            }
        }
    }
}
