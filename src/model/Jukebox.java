package model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import views.LoginView;
import views.JukeboxView;

/**************************************************************************************************
 * @author Ashley Mains and Victor Gomes
 * 
 * CLASS: Jukebox
 * DESCRIPTION:
 * Models a Jukebox that keeps track of the queue of songs to play and determines whether or not
 * a song is allowed to be selected again based on date and song selection totals.
 **************************************************************************************************/

public class Jukebox {
	
	private ObservableList<Song> observeSongs;
	private ArrayList<Song> songData;
	private Map<String, Song> songList;
	private ArrayList<String> songQueue;
	private LocalDate startDate;
	private MediaPlayer mediaPlayer;
	
	/**********************************************************************************************
	 * METHOD: Jukebox()
	 * PURPOSE: Constructs a Jukebox object by reading all available songs from the file "SongList".
	 * Resets all values to default if 'false' is passed as a parameter.
	 * @param doReadFiles The boolean flaf to determine whether to read files or not.
	 **********************************************************************************************/
	@SuppressWarnings("unchecked")
	public Jukebox(boolean doReadFiles) {
		observeSongs = FXCollections.observableArrayList();
		songData = new ArrayList<Song>(0);
		songList = new HashMap<String, Song>();
		songQueue = new ArrayList<String>(0);
		startDate = LocalDate.now();
		
		if(doReadFiles) {
			try {
				//Read all song data
				FileInputStream fileStream = new FileInputStream("SongList");
				ObjectInputStream objectStream1 = new ObjectInputStream(fileStream);
				songData = (ArrayList<Song>) objectStream1.readObject();
				objectStream1.close();
				
				//Map song data
				for(int i=0; i<songData.size(); i++) {
					songList.put(songData.get(i).getSongTitle(), songData.get(i));
					observeSongs.add(songData.get(i));
				}
				
				//Read current queue
				fileStream = new FileInputStream("SongQueue");
				ObjectInputStream objectStream2 = new ObjectInputStream(fileStream);
				songQueue = (ArrayList<String>) objectStream2.readObject();
				objectStream2.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			resetToDefault();
		}
		
		playNextSong();
	}//END Jukebox() constructor
	
	/**********************************************************************************************
	 * METHOD: getAllSongInfo()
	 * PURPOSE: Returns the Observable list of all Songs to be displayed in the Jukebox of the GUI.
	 * @return The ObservableList of all song data.
	 **********************************************************************************************/
	public ObservableList<Song> getAllSongInfo() { 
		return observeSongs;
	}//END getSongInfo()
	
	/**********************************************************************************************
	 * METHOD: getSongQueue()
	 * PURPOSE: Returns the ArrayList of all Songs in the queue.
	 * @return The ArrayList of all songs in the queue.
	 **********************************************************************************************/
	public ArrayList<String> getSongQueue(){
		return songQueue;
	}//END getSongQueue()
	
	/**********************************************************************************************
	 * METHOD: checkSongAvailability()
	 * PURPOSE: Determines if the song selected is allowed to be played based on the current date,
	 *     user selection total, and particular song selection total.
	 * @param user The User who requested a song.
	 * @param song The Srtring name of the song being requested.
	 * @param checkDate The LocalDate of the current date to be checked.
	 * @return The return message String.
	 **********************************************************************************************/
	public String checkSongAvailability(User user, String song, LocalDate checkDate) {
		//Reset jukebox counts based on date if valid
		if(checkDate.isAfter(startDate)) {
			startDate = checkDate;
			Set<String> keySet = songList.keySet();
			for(String key : keySet) { songList.get(key).resetCount(); }
			LoginView.resetCounts();
		}
		
		//Must input a valid song name
		if(songList.get(song) == null) {
			return "The song selected was not found in this Jukebox.";
		}
		
		//User must have enough time remaining
		if(user.getTimeRemaining() < songList.get(song).getSeconds()) {
			return "Not enough time remaining in account.";
		}
		
		//User can only select three songs per day
		if(user.getSongCount() >= 3) {
			return "You may only play a total of 3 songs per day.";
		}
		
		//A song may only be selected 3 times per day
		if(songList.get(song).getTimesSelected() >= 3) {
			return "This song may not be selected more than 3 times per day.";
		}
		
		//The song is able to be selected
		boolean queueWasEmpty = songQueue.isEmpty();
		songList.get(song).selectSong();
		user.selectSong(songList.get(song).getSeconds());
		songQueue.add(song);
		if(queueWasEmpty) { playNextSong(); } 
		return "Confirm";
	}//END checkSongAvailability()
	
	/**********************************************************************************************
	 * METHOD: playNextSong()
	 * PURPOSE: Plays the next song in the song queue if the queue is not empty (FIFO order).
	 **********************************************************************************************/
	public void playNextSong() {
		if(songQueue.isEmpty() == false) {
			Media media = songList.get(songQueue.get(0)).getSongMedia();
			mediaPlayer = new MediaPlayer(media);
			mediaPlayer.setAutoPlay(true);
			mediaPlayer.play();
			mediaPlayer.setOnEndOfMedia(new EndOfSongHandler());
		}
	}//END playNextSong()
	
	/**********************************************************************************************
	 * CLASS: EndOfSongHandler()
	 * PURPOSE: After a song plays, it is removed from the queue and any other songs remaining
	 *     are played.
	 **********************************************************************************************/
	private class EndOfSongHandler implements Runnable {
		@Override
		public void run() {
			mediaPlayer.dispose();
			songQueue.remove(0);
			JukeboxView.refreshQueueView();
			playNextSong();
		}//END run()
	}//END EndOfSongHandler class
	
	/**********************************************************************************************
	 * METHOD: resetToDefault()
	 * PURPOSE: Completely resets to system to starting default values.
	 **********************************************************************************************/
	private void resetToDefault() { //TODO: reuse enum here?
		Song song1 = new Song("Pokemon Capture", "0:05", "Pikachu", "Capture.mp3", "0");
		Song song2 = new Song("Danse Macabre", "0:34", "Kevin MacLeod", "DanseMacabreViolinHook.mp3", "0");
		Song song3 = new Song("Determined Tumbao", "0:20", "FreePlay Music", "DeterminedTumbao.mp3", "0");
		Song song4 = new Song("Loping Sting", "0:05", "Kevin MacLeod", "LopingSting.mp3", "0");
		Song song5 = new Song("Swing Cheese", "0:15", "FreePlay Music", "SwingCheese.mp3", "0");
		Song song6 = new Song("The Curtain Rises", "0:28", "Kevin MacLeod", "TheCurtainRises.mp3", "0");
		Song song7 = new Song("Untameable Fire", "4:42", "Pierre Langer", "UntameableFire.mp3", "0");
		songData.add(song1); songData.add(song2); songData.add(song3); songData.add(song4);
		songData.add(song5); songData.add(song6); songData.add(song7);
		
		for(int i=0; i<songData.size(); i++) {
			songList.put(songData.get(i).getSongTitle(), songData.get(i));
			observeSongs.add(songData.get(i));
		}
		
		songQueue.clear();
	}//END resetToDefault()
	
	/**********************************************************************************************
	 * METHOD: writeQueueToFile()
	 * PURPOSE: Writes out the current queue of songs to the file "SongQueue".
	 **********************************************************************************************/
	public void writeQueueToFile() {
		try {
			FileOutputStream fileOutput = new FileOutputStream("SongQueue");
			ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
			objectOutput.writeObject(songQueue);
			objectOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}//END writeQueueToFile()
	
	/**********************************************************************************************
	 * METHOD: writeSongsToFile()
	 * PURPOSE: Writes out the all of song data to the file "SongList".
	 **********************************************************************************************/
	public void writeSongsToFile() {
		try {
			FileOutputStream fileOutput = new FileOutputStream("SongList");
			ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
			objectOutput.writeObject(songData);
			objectOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}//END writeSongsToFile()
	
}//END Jukebox class