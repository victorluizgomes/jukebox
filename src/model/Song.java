package model;

import java.io.File;
import java.io.Serializable;
import javafx.scene.media.Media;

/**************************************************************************************************
 * @author Ashley Mains and Victor Gomes
 * 
 * CLASS: Song
 * DESCRIPTION:
 * Models a Song object that keeps track of how long it plays for in seconds, how many times this
 * song has been selected, and the Media object used to play this song in a MediaPlayer. Currently
 * extraneous information such as song title and artist are not used.
 **************************************************************************************************/

public class Song implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String songTitle;
	private String songTime;
	private String songArtist;
	private String songPath;
	private int songSeconds;
	private int timesSelected;
	
	/**********************************************************************************************
	 * METHOD: Song()
	 * PURPOSE: Constructs a Song object with play time in seconds
	 * @param title The String song title.
	 * @param time The String song play time in "0:00" format.
	 * @param artist The String song artist.
	 * @param path The String song file path in "FILE.mp3" format.
	 * @param selected The String integer count of how many times this song has been selected.
	 **********************************************************************************************/
	public Song(String title, String time, String artist, String path, String selected) {
		songTitle = title;
		songTime = time;
		songArtist = artist;
		songPath = path;
		String[] timeSplit = time.split(":");
		songSeconds = 60*Integer.parseInt(timeSplit[0]) + Integer.parseInt(timeSplit[1]);
		timesSelected = Integer.parseInt(selected);
	}//END Song() constructor
	
	/**********************************************************************************************
	 * METHOD: getSongTitle()
	 * PURPOSE: Returns this song title.
	 * @return This song's title
	 **********************************************************************************************/
	public String getSongTitle() {
		return songTitle;
	}//END getSongTitle()
	
	/**********************************************************************************************
	 * METHOD: getSongTime()
	 * PURPOSE: Returns this song's play time as a String "0:00".
	 * @return This song's play time as a "0:00" String.
	 **********************************************************************************************/
	public String getSongTime() {
		return songTime;
	}//END getSongTime()
	
	/**********************************************************************************************
	 * METHOD: getSongArtist()
	 * PURPOSE: Returns this song artist.
	 * @return This song's artist.
	 **********************************************************************************************/
	public String getSongArtist() {
		return songArtist;
	}//END getSongArtist()
	
	/**********************************************************************************************
	 * METHOD: getSeconds()
	 * PURPOSE: Returns how long this song plays for in seconds.
	 * @return This song's play time is seconds.
	 **********************************************************************************************/
	public int getSeconds() {
		return songSeconds;
	}//END getSeconds()
	
	/**********************************************************************************************
	 * METHOD: selectSong()
	 * PURPOSE: This song is "selected" and the selection count is incremented.
	 **********************************************************************************************/
	public void selectSong() {
		timesSelected++;
	}//END selectSong()
	
	/**********************************************************************************************
	 * METHOD: getTimesSelected()
	 * PURPOSE: Returns the number of times this song has be selected.
	 * @return The number of times this song has been selected.
	 **********************************************************************************************/
	public int getTimesSelected() {
		return timesSelected;
	}//END getTimesSelected()
	
	/**********************************************************************************************
	 * METHOD: resetCount()
	 * PURPOSE: Resets the number of times this song has been selected to 0.
	 **********************************************************************************************/
	public void resetCount() {
		timesSelected = 0;
	}//END resetCount()
	
	/**********************************************************************************************
	 * METHOD: getSongMedia()
	 * PURPOSE: Returns the Media object for this song.
	 * @return The song Media object to be played.
	 **********************************************************************************************/
	public Media getSongMedia() {
		return new Media(((new File("songfiles\\" + songPath)).toURI()).toString());
	}//END getSongMedia()
	
	/**********************************************************************************************
	 * METHOD: toString()
	 * PURPOSE: Returns this song as a string in file format for the SongList file.
	 **********************************************************************************************/
	//public String toString() {
	//	return songTitle + '\t' + songTime + '\t' + songArtist + '\t' 
	//			+ songPath + '\t' + timesSelected;
	//}//END toString()
	
}//END Song class