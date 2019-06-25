package games.RailroadInk;

/* -- IMPORTS -- */

//classes that are necessary for the server
import gameClasses.Game;
import gameClasses.GameState;
import global.FileHelper;

//classes that are necessary for the user management
import userManagement.User;

//java classes
import java.io.IOException;
import java.util.ArrayList;

public class RailroadInk extends Game {

	/* -- ATTRIBUTES -- */
	
	//a user object is used to denote the user whose turn it is (might be useless)
	private User playerTurn = null;
	
	//two ArrayLists are used to store the players and the spectators
	private ArrayList<User> playerList = new ArrayList<User>();
	private ArrayList<User> spectatorList = new ArrayList<User>();
	
	//an integer value counts the turns
	private int turnCounter = 0;
	
	//a String is used to indicate if a player has left
	private String playerLeft = null;

	
	/* -- METHODS NECESSARY FOR THE SERVER -- */
	
	@Override
	public String getSite() {
		try {
			return FileHelper.getFile("RailroadInk/index.html");
		} catch (IOException e) {
			System.err.println("Loading of file RailroadInk/index.html failed");
		}
		return null;
	}

	@Override
	public String getCSS() {
		try {
			return FileHelper.getFile("RailroadInk/css/index_style.css");
		} catch (IOException e) {
			System.err.println("Loading of file RailroadInk/css/index_style.css failed");
		}
		return null;
	}

	@Override
	public String getJavaScript() {
		return "<script src =\"js/index.js\"></script>";
	}

	@Override
	public int getMaxPlayerAmount() {
		//the game is supposed to be played by a maximum number of 6 players
		return 6;
	}

	@Override
	public int getCurrentPlayerAmount() {
		//return the amount of users currently stored in the list of players
		return playerList.size();
	}

	@Override
	public void execute(User user, String gsonString) {
		// TODO implement what should happen if the user interacts with the game
		
	}

	@Override
	public ArrayList<User> getPlayerList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<User> getSpectatorList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getGameData(String eventName, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addUser(User user) {
		//TODO this is just a first version of the method, it is not finished yet
		
		//the user is only allowed to join the game, if there are less than 6 players and he is not already in the list
		if (playerList.size() < getMaxPlayerAmount() && !playerList.contains(user)) {
			playerList.add(user);
		}
		
		
		//if there are six players, start the game 
		if(playerList.size() == 6) {
			this.gState = GameState.RUNNING;
		}
	}

	@Override
	public void addSpectator(User user) {
		// TODO Auto-generated method stub
		this.spectatorList.add(user);
	}

	@Override
	public boolean isJoinable() {
		//TODO this is just a first version of the method, it is not finished yet
		
		//if there are 6 or more players, you cannot join
		if(playerList.size() >= 6) {
			return false;
		} 
		
		//if the game is running, you can only join as a spectator
		if(this.gState == GameState.RUNNING) {
			//TODO add the player as a spectator
			return false;
		}
		
		//if the game is finished or closed, you cannot join
		if(this.gState == GameState.CLOSED || this.gState == GameState.FINISHED) {
			return false;
		}
		
		//in all other cases, the game is joinable
		return true;
	}

	@Override
	public void playerLeft(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public GameState getGameState() {
		// TODO Auto-generated method stub
		return null;
	}
	
/*		@Override
	public  void sendGameDataToClients(String eventName, String id)
	{
		this.createServerSentEvent(e, ed) = new Event
	} */

	
	
}
