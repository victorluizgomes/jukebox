package demoMediaPlayer;

/**
 * This code will play "LopingSting.mp3" assuming that file is in
 * a folder named songfiles. You were asked to import this folder 
 * containing mp3 files when accepting the team assignment.
 */
import java.io.File;
import java.net.URI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class PlayAnMP3 extends Application {
	
	private static MediaPlayer mediaPlayer;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private int songsPlayed = 1;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		String path = "songfiles/UntameableFire.mp3";
		File file = new File(path);
		URI uri = file.toURI();
		BorderPane all = new BorderPane();
		all.setTop(new Label("Play one song"));
		all.setCenter(new Label(uri.toString()));
		
		Media media = new Media(uri.toString());
		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setAutoPlay(true);
		mediaPlayer.play();
		
		mediaPlayer.setOnEndOfMedia(new EndOfSongHandler());
		
		Scene scene = new Scene(all, 600, 80);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private class EndOfSongHandler implements Runnable {
		@Override
		public void run() {
			songsPlayed++;
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Message");
			alert.setHeaderText("Song ended, can now play song #" + songsPlayed);
			alert.showAndWait();
			
		}
	}
}