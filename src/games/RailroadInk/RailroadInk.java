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
	
	/* -- LOADING THE HTML, CSS AND JS FILE -- */
	
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

	/* -- SIMPLE GETTER METHODS -- */
	
	@Override
	public int getMaxPlayerAmount() {
		//the game is supposed to be played by a maximum number of 4 players
		return 4;
	}

	@Override
	public int getCurrentPlayerAmount() {
		//return the amount of users currently stored in the list of players
		return playerList.size();
	}

	@Override
	public ArrayList<User> getPlayerList() {
		return this.playerList;
	}

	@Override
	public ArrayList<User> getSpectatorList() {
		return this.spectatorList;
	}
	
	@Override
	public GameState getGameState() {
		return this.gState;
	}
	
	/* -- METHODS FOR THE SERVER COMMUNICATION -- */
	
	@Override
	public void execute(User user, String gsonString) {
		// TODO implement what should happen if the user interacts with the game
		
	}

	@Override
	public String getGameData(String eventName, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	/* -- METHODS FOR MANAGING THE PLAYERS -- */
	
	@Override
	public void addSpectator(User user) {
		this.spectatorList.add(user);
	}
	
	@Override
	public void addUser(User user) {
		
		//the user is only allowed to join the game, if there are less than the max amount of players and they aren't already in the list
		if (playerList.size() < getMaxPlayerAmount() && !playerList.contains(user)) {
			playerList.add(user);
		}
		
		
		//if there are as many players as allowed, start the game
		if(playerList.size() == getMaxPlayerAmount()) {
			this.gState = GameState.RUNNING;
			//TODO inform the players about this
		}
		
	}

	@Override
	public boolean isJoinable() {
		
		//if there are more or just as many players as allowed, you cannot join
		if(playerList.size() >= getMaxPlayerAmount()) {
			return false;
		} 
		
		//if the game is finished, closed or running, you cannot join
		if(this.gState == GameState.CLOSED || this.gState == GameState.FINISHED || this.gState == GameState.RUNNING) {
			return false;
		}
		
		//in all other cases, the game is joinable
		return true;
		
	}

	@Override
	public void playerLeft(User user) {
		//try to remove the player
		if(playerList.remove(user)) {
			//if he was removed, inform the other players about this
			playerLeft = user.getName();
			//TODO inform the other players about this
		}
	}	
	
}
