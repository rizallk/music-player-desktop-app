package musicplayerdesktopapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        MusicPlayer musicPlayer = new MusicPlayer();
        
        BorderPane root = new BorderPane();
        root.setCenter(musicPlayer.getView());
        
        Scene scene = new Scene(root, 800, 400);
        primaryStage.setTitle("Music Player");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
