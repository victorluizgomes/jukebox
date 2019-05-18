package tests;

// Added to allow package tests to exist on GitHub
import static org.junit.Assert.*;
import java.time.LocalDate;
import org.junit.Test;
import javafx.embed.swing.JFXPanel;
import model.AllAccounts;
import model.Jukebox;
import model.Song;
import model.User;
import views.LoginView;

/**************************************************************************************************
 * @author Ashley Mains and Victor Gomes
 * 
 * CLASS: JukeboxTest
 * DESCRIPTION:
 * Provide JUnit testing for the Jukebox project and individual classes (in progress).
 **************************************************************************************************/

public class JukeboxTest {
	
	final JFXPanel fxPanel = new JFXPanel(); // Fix from stackoverflow says to add this
	
	@Test
	public void testAllAccountsInitialization() {
		AllAccounts accountList = new AllAccounts(false);
		assertTrue(accountList.verifyAccount("Chris", "1"));
		assertFalse(accountList.verifyAccount("Chris", "22"));
		assertFalse(accountList.isAdmin("Chris"));
	}
	
	@Test
	public void testAccountUserData() {
		AllAccounts accountList = new AllAccounts(false);
		User merlin = accountList.getUser("Merlin");
		assertEquals("Merlin", merlin.getUsername());
		assertEquals("7777777", merlin.getPassword());
		
		assertEquals(90000, merlin.getTimeRemaining());
		assertEquals(0, merlin.getSongCount());
		merlin.selectSong(10000);
		assertEquals(80000, merlin.getTimeRemaining());
		assertEquals(1, merlin.getSongCount());
		merlin.resetCount();
		assertEquals(0, merlin.getSongCount());
	}
	
	@Test
	public void testAddRemoveAccount() {
		AllAccounts accountList = new AllAccounts(false);
		accountList.addAccount("Victor", "hey", false);
		assertTrue(accountList.verifyAccount("Victor", "hey"));
		assertFalse(accountList.isAdmin("Victor"));
		assertTrue(accountList.removeAccount("Victor"));
		assertFalse(accountList.verifyAccount("Victor", "hey"));
		assertFalse(accountList.removeAccount("Victor"));
		assertFalse(accountList.removeAccount("Merlin"));
		assertFalse(accountList.removeAccount("Admin"));
	}
	
	@Test
	public void testAccountUpdate() {
		AllAccounts accountList = new AllAccounts(false);
		User merlin = accountList.getUser("Merlin");
		assertTrue(merlin.isAdmin());
		merlin.updateInfo("seven", false);
		assertEquals("seven", merlin.getPassword());
		assertFalse(merlin.isAdmin());
	}
	
	@Test
	public void testSongSelection() {
		Song song = new Song("Pokemon Capture", "0:05", "Pikachu", "Capture.mp3", "0");
		assertEquals(5, song.getSeconds());
		assertEquals(0, song.getTimesSelected());
		song.selectSong();
		assertEquals(1, song.getTimesSelected());
		song.resetCount();
		assertEquals(0, song.getTimesSelected());
		assertNotEquals(null, song.getSongMedia());
	}
	
	@Test
	public void testUserSongSelection() {
		Jukebox jukebox = new Jukebox(false);
		User victor = new User("Victor", "hey", false, 1500, 0);
		String message = jukebox.checkSongAvailability(victor, "Pokemon Capture", LocalDate.now());
		assertEquals("Confirm", message); // Failing if using persistence
	}
	
	@Test
	public void testTodayTomorrowReset() {
		LoginView login = new LoginView(false);
		login.setCurrentUser("Chris");
		Jukebox jukebox = new Jukebox(false);
		User chris = login.getCurrentUser();
		jukebox.checkSongAvailability(chris, "Pokemon Capture", LocalDate.now());
		jukebox.checkSongAvailability(chris, "Pokemon Capture", LocalDate.now());
		String message = jukebox.checkSongAvailability(chris, "Pokemon Capture", LocalDate.now());
		assertEquals("Confirm", message); // Failing if using persistence
		message = jukebox.checkSongAvailability(chris, "Pokemon Capture", LocalDate.now());
		assertEquals("You may only play a total of 3 songs per day.", message);
		message = jukebox.checkSongAvailability(chris, "Pokemon Capture", LocalDate.now().plusDays(1));
		assertEquals("Confirm", message); // Failing if using persistence
	}
	
	@Test
	public void testTwoUserSelectSameSong() {
		LoginView login = new LoginView(false);
		Jukebox jukebox = new Jukebox(false);
		login.setCurrentUser("Chris");
		User chris = login.getCurrentUser();
		jukebox.checkSongAvailability(chris, "Pokemon Capture", LocalDate.now());
		jukebox.checkSongAvailability(chris, "Pokemon Capture", LocalDate.now());
		
		login.setCurrentUser("Ryan");
		User ryan = login.getCurrentUser();
		String message = jukebox.checkSongAvailability(ryan, "Pokemon Capture", LocalDate.now());
		assertEquals("Confirm", message); // Failing if using persistence
		message = jukebox.checkSongAvailability(ryan, "Pokemon Capture", LocalDate.now());
		assertEquals("This song may not be selected more than 3 times per day.", message);
	}
	
	@Test
	public void testUserOutOfTime() {
		Jukebox jukebox = new Jukebox(false);
		AllAccounts accountList = new AllAccounts(false);
		User merlin = accountList.getUser("Merlin");
		merlin.selectSong(89996);
		assertEquals(4, merlin.getTimeRemaining());
		assertEquals(1, merlin.getSongCount());
		String message = jukebox.checkSongAvailability(merlin, "Pokemon Capture", LocalDate.now());
		assertEquals("Not enough time remaining in account.", message);
	}
	
}//END JukeboxTest class