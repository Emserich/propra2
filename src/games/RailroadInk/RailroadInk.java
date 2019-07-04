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
import java.util.Iterator;

public class RailroadInk extends Game {

	/* -- ATTRIBUTES -- */
	
	//a user object is used to denote the user whose turn it is (might be useless)
	
	//two ArrayLists are used to store the players and the spectators
	private ArrayList<User> playerList = new ArrayList<User>();
	private ArrayList<User> spectatorList = new ArrayList<User>();
	
	//an ArrayList to store the boards of the players
	private ArrayList<Board> boardList = new ArrayList<Board>();
	
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
		//if(this.gState==GameState.CLOSED) return;
		
		if(gsonString.equals("CLOSE")){
			sendGameDataToClients("CLOSE");
			closeGame();
			return;
		}
		
		if (gsonString.equals("RESTART")) {
			
			boardList.clear();
			for(User u : playerList)
			{
				Board board = new Board(u);
				board.initializeFields();
				boardList.add(board);
			}
			
			
			//if there are as many players as allowed, start the game
			if(playerList.size() == getMaxPlayerAmount() || playerList.size() >1) {
				this.gState = GameState.RUNNING;
				sendGameDataToClients("standardEvent");
			}
			
		}
		
		if (gState != GameState.RUNNING)
			return;
		
		String[] strArray = gsonString.split(",");
		String [] receivedArray = new String[5];
		for (int i = 0; i < 5; i++) {
			receivedArray[i] = strArray[i];
		}
		int imageNr = Integer.parseInt(receivedArray[0]);
		String Drehung = receivedArray[1];
		int Drehanzahl = Integer.parseInt(receivedArray[2]);
		boolean spiegelung = Boolean.parseBoolean(receivedArray[3]);
		int feldnr = Integer.parseInt(receivedArray[4]);
		
		
		int GesamtDrehung=0;
		if (Drehung.equals("r"))
		{
			GesamtDrehung= (90 * Drehanzahl) % 360 ;
		}
		else if (Drehung.equals("l"))
		{
			Drehanzahl = Drehanzahl % 4;
			GesamtDrehung = 360 - (Drehanzahl * 90);
		}
		
		Orientations orientation = Orientations.ZERO_DEGREES;
		
		switch (GesamtDrehung)
		{
			case 0: 
				orientation = Orientations.ZERO_DEGREES;
			case 90:
				orientation = Orientations.NINETY_DEGREES;
			case 180:
				orientation = Orientations.ONEHUNDREDEIGHTY_DEGREES;
			case 270:
				orientation = Orientations.TWOHUNDREDSEVENTY_DEGREES;
		
		}
		
		RouteElement RouteElem=null;
		switch (imageNr)
		{
			case 1:
				RouteElem = new RouteElement(orientation, ElementTypes.ROAD, spiegelung);
			case 2: 
				RouteElem = new RouteElement(orientation, ElementTypes.ROAD_TURN, spiegelung);
			case 3:
				RouteElem = new RouteElement(orientation, ElementTypes.ROAD_TJUNCTION, spiegelung);
			case 4:
				RouteElem = new RouteElement(orientation, ElementTypes.RAIL, spiegelung);
			case 5:
				RouteElem = new RouteElement(orientation, ElementTypes.RAIL_TURN, spiegelung);
			case 6:
				RouteElem = new RouteElement(orientation, ElementTypes.RAIL_TJUNCTION, spiegelung);
			case 7:
				RouteElem = new RouteElement(orientation, ElementTypes.OVERPASS, spiegelung);
			case 8:
				RouteElem = new RouteElement(orientation, ElementTypes.STATION, spiegelung);
			case 9:
				RouteElem = new RouteElement(orientation, ElementTypes.STATION_TURN, spiegelung);
			case 10:
				RouteElem = new RouteElement(orientation, ElementTypes.ROAD_CROSSROAD, spiegelung);
			case 11:
				RouteElem = new RouteElement(orientation, ElementTypes.RAIL_CROSSROAD, spiegelung);
			case 12:
				RouteElem = new RouteElement(orientation, ElementTypes.STATION_1, spiegelung);
			case 13:
				RouteElem = new RouteElement(orientation, ElementTypes.STATION_2, spiegelung);
			case 14:
				RouteElem = new RouteElement(orientation, ElementTypes.STATION_3, spiegelung);
			case 15:
				RouteElem = new RouteElement(orientation, ElementTypes.STATION_4, spiegelung);
		}
		
		Board userboard = null;;
		
		Iterator<Board> itB = boardList.iterator();
		while (itB.hasNext())
		{
			Board userboardit = itB.next();
			if (userboardit.getUser() == user) userboard = userboardit;
		}
		
		Field field = new Field(feldnr);
		
		//falls SPezialelement platziert wurde in dieser runde kehre zurück
		
		if(imageNr>9 && !userboard.isSpecialElementPlacedInThisRound())
			{
			sendGameDataToUser(user, "SpecialElementalreadyPlaced");
			return;
			}
		
		if(userboard.canElementBePlaced(field, RouteElem))
		{
			Iterator<Field> itF = userboard.getFields().iterator();
			while(itF.hasNext())
			{
				Field f = itF.next();
				if(f == field)
				{
					f = new Field(feldnr, RouteElem);
				}
			}
		}
		else 
		{
			sendGameDataToUser(user, "WrongField");
			return;
		}
			
		
	}

	private String isHost(User user) 
	{
		if(user==creator)
			
			return 	",HOST";
		else
			
		return ",NOTTHEHOST";
	}
	
	@Override
	
	public String getGameData(String eventName, User user) {
		
		String gameData = "";
		if(eventName.equals("PLAYERLEFT")){
			return playerLeft + " hat das Spiel verlassen!";
		}
		if(eventName.equals("CLOSE")){
			return "CLOSE";
		}
		
		ArrayList<Board> boardList  = getBoardList();

		for (int i = 0; i < boardList.size(); i++) {
			Board board = boardList.get(i);
			if (board.getUser().getName() == user.getName())
			{
				//TODO Format der Daten ist noch zu definieren
				//gameData += String.valueOf(board.getFields());
				gameData += ',';
			}
		}
		
		if(playerList.size() < 2){
			gameData += "Warte Auf 2ten Spieler...";
			gameData += isHost(user);
			return gameData;
		}

		if (this.gState == GameState.FINISHED) {
			if (turnCounter == 7){
				gameData += "Unentschieden!";
				gameData += isHost(user);
				return gameData;
			}
			
			//TODO Fall betrachten, wenn es nicht unentschieden ist
		}


		if (playerList.indexOf(user) == 0)
			gameData += " (x)";
		else
			gameData += " (o)";
		
		gameData += isHost(user);

		return gameData;
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
			//create a new board for the player
			Board board = new Board(user);
			//add it to the list
			boardList.add(board);
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
		//get the index
		int index = playerList.indexOf(user);
		//try to remove the player
		if(playerList.remove(user)) {
			//if he was removed, inform the other players about this
			playerLeft = user.getName();
			//also remove the board from the list
			if(index != -1) {
				boardList.remove(index);
			}
			//TODO inform the other players about this
		}
	}

	public ArrayList<Board> getBoardList() {
		return boardList;
	}	
	
}
