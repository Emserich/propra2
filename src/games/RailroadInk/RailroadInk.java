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
	
	//two ArrayLists are used to store the players and the spectators
	private ArrayList<User> playerList = new ArrayList<User>();
	private ArrayList<User> spectatorList = new ArrayList<User>();
	
	//an ArrayList to store the boards of the players
	private ArrayList<Board> boardList = new ArrayList<Board>();
	
	//an ArrayList to store the players that have already finished their turn
	private ArrayList<User> finishedPlayers = new ArrayList<User>();
	
	//an Arraylist to store the route elements of the turn for each player
	private ArrayList<ArrayList<RouteElement>> elementsPerTurn = new ArrayList<ArrayList<RouteElement>>();
	
	//an object of the AI-class
	private ArtificialIntelligence AI;
	
	//an ArrayList for the results
	private ArrayList<Result> results = new ArrayList<Result>();
	
	//an integer value counts the turns
	private int turnCounter = 0;
	
	//a String is used to indicate if a player has left
	private String playerLeft = null;
	
	//a String to pass on the dices for the current roll
	private String roll;
	
	private int allTurnCounters;
	
	
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
		
		//TODO for testing, remove later
		System.out.println("");
		System.out.println("Execute-Methode aufgerufen mit Daten:");
		System.out.println(user.getName() + "-" + gsonString);
		
		
		if(this.gState==GameState.CLOSED) {
			return;
		}
		
		if(gsonString.equals("CLOSE") && isHost(user).equals(",HOST")){
			sendGameDataToClients("CLOSE");
			closeGame();
			return;
		}
		
		if(gsonString.equals("CLOSE") && isHost(user).equals(",NOTTHEHOST")) {
			sendGameDataToClients("PLAYERLEFT");
			return;
		}
		if(gsonString.equals("myScore")) {
			sendGameDataToUser(user, "myScore");
			return;
		}
		if(gsonString.equals("winnerData")) {
			sendGameDataToUser(user, "winnerData");
			return;
		}
		if(gsonString.contains("ROLL")) {
			roll(gsonString);
			
			//store the route elements in an Array for each player
			ArrayList<RouteElement> elements = new ArrayList<RouteElement>();
			String[] info = gsonString.split(",");
			for(int i = 1; i < info.length; i++) {
				String typeInfo = info[i];
				ElementTypes type = getTypeFromJsInput(typeInfo);
				RouteElement element = new RouteElement(Orientations.ZERO_DEGREES, type, false);
				elements.add(element);
			}
			
			elementsPerTurn.clear();
			for(int j = 0; j < playerList.size(); j++) {
				elementsPerTurn.add(elements);
			}
			
			//go through each board and set some variable false again
			for(Board b : boardList) {
				b.setSpecialElementPlacedInThisRound(false);
			}
			
			return;
		}
		if(spectatorList.contains(user)) {
			return;
		}
		if(gsonString.equals("ADD_KI")) {
			this.gState = GameState.RUNNING;
			sendGameDataToUser(user, "START_KI");
			return;
		}
		if(gsonString.equals("RESTART")) {
			if(gState == GameState.RUNNING) {
				sendGameDataToClients("Cannot restart game once it was started.");
				return;
			}
		}
		
		if (gsonString.equals("RESTART") && isHost(user).equals(",HOST")) {
			if(gState == GameState.RUNNING) 
			{
				sendGameDataToClients("Cannot restart game once it was started.");
				return;
			}
			boardList.clear();
			elementsPerTurn.clear();
			results.clear();
			for(User u : playerList)
			{
				//create boards for each player
				Board board = new Board(u);
				board.initializeFields();
				boardList.add(board);
				
				//create an array of elements for each player
				ArrayList<RouteElement> elements = new ArrayList<RouteElement>();
				elementsPerTurn.add(elements);
			}
			
			turnCounter = 0;
			allTurnCounters = 0;
			
			
			//if there are as many players as allowed, start the game
			if(playerList.size() == getMaxPlayerAmount() || playerList.size() >1) {
				this.gState = GameState.RUNNING;
				sendGameDataToClients("START");
			}
			
			//return because otherwise we'll get exceptions
			return;
		}
		
		if (gState != GameState.RUNNING) {
			return;
		}
		
		/* -- acquiring information about the player -- */
		
		//get the board of the user
		Board userboard = null;
		
		Iterator<Board> itB = boardList.iterator();
		while (itB.hasNext())
		{
			Board userboardit = itB.next();
			if (userboardit.getUser() == user) userboard = userboardit;
		}
		
		//get the elements they can place in this turn
		int index = playerList.indexOf(user);
		ArrayList<RouteElement> remainingElements = elementsPerTurn.get(index);
		
		/* -- this is the standard case: the player wants to add an element -- */
		
		
		
		//only go on with this if the Array also has four elements and is not null and so on
		if(gsonString.contains("validateDrop")) {
			boolean validate = true;
			System.out.println(validate + " aus validateDrop");
			String [] receivedArray = new String[5];
			String[] strArray = gsonString.split(",");
		if(strArray != null && strArray.length == 5) {
		
			for (int i = 0; i < 4; i++) {
				receivedArray[i] = strArray[i];
				}
			
			int fieldNr = Integer.parseInt(receivedArray[1]);
			fieldNr--;
		
			RouteElement routeElem = getElementFromJS(gsonString);
		
			//TODO for testing, remove later
			System.out.println(routeElem);
		
			Field field = userboard.getFields().get(fieldNr);
		
			//falls SPezialelement platziert wurde in dieser runde kehre zurück
		
			if(routeElem.isSpecialElement() && userboard.isSpecialElementPlacedInThisRound())
				{
				sendGameDataToUser(user, "SpecialElementalreadyPlaced");
				//TODO for testing, remove later
				System.out.println("Es wurde bereits ein Spezialelement in dieser Runde gesetzt.");
				validate = false;
				return;
				}
		
			if(userboard.getNumberOfSpecialElements()==3)
			{
				sendGameDataToUser(user,"AlREADY 3 SPECIAL ELEMENTS");
				validate = false;
				return;
			}
			
			if(userboard.canElementBePlaced(field, routeElem))
			{
				try{
					field.addElement(routeElem);
					if(routeElem.isSpecialElement()) {
						userboard.setSpecialElementPlacedInThisRound(true);
					}
					//if we end up here, the element has been added
					int indexOfElem = -1;
					for(RouteElement other : remainingElements) {
						if(routeElem.sameType(other)) {
							indexOfElem = remainingElements.indexOf(other);
						}
					}
					remainingElements.remove(indexOfElem);
				} catch (IllegalPlayerMoveException e) {
					e.printStackTrace();
					sendGameDataToUser(user, "WrongField");
					validate = false;
					return;
				}
			}
			else 
			{
				sendGameDataToUser(user, "WrongField");
				//TODO for testing, remove later
				System.out.println("Das Element darf hier nicht platziert werden");
				validate = false;
				return;
			}
				if (validate == true) {
					sendGameDataToUser(user, "dropped");
					return;
				}
		  }
		}
		/* -- in this case, the player wants to end their turn -- */
		
		if(gsonString.equals("END_TURN"))
		{
					
			
			//check whether the player is allowed to end their turn
			if(remainingElements == null || remainingElements.size() == 0) {
				
				if(!finishedPlayers.contains(user)) {
					finishedPlayers.add(user);
				}
			} else if(!userboard.movesLeft((RouteElement[])remainingElements.toArray())) {
				
				if(!finishedPlayers.contains(user)) {
					finishedPlayers.add(user);
				}
			} else {
				
				sendGameDataToUser(user, "CANT_END_TURN");
				
				
			}
			
			
			//check whether all players have ended their turns
			if(finishedPlayers.size() == playerList.size()) {
				//increment the turn counter
				turnCounter++;
				sendGameDataToClients("EndOfTurn" + isHost(user));
				if(turnCounter == 7) {
					//end the game
					this.gState = GameState.FINISHED;
					sendGameDataToClients("EndOfGame");
					return;
				}
				
				return;
			}
				
		}
			
	}

	private String isHost(User user) 
	{
		if(user == creator)
			
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
		if(eventName.equals("Restart")){
			return "Cannot restart game once it was started.";
		}
		if(eventName.equals("START")) {
			return "START" + ",Spiel gestartet!"+ isHost(user);
		}
		if(eventName.equals("HOST")) {
			return "CLOSE";
		}
		if(eventName.equals("CANT_END_TURN")) {
			return "CANT_END_TURN";
		}
		if(eventName.equals("START_KI")) {
			//to do: packe KI ins Spiel!
			return "START" + ",Spiel gegen KI gestartet!" + isHost(user);
		}
		if(eventName.equals("NOTTHEHOST")) {
			return "PLAYERLEFT" + user.getName();
		}
		if(eventName.equals("EndOfTurn")) {
			return "EndOfTurn";
		}
		if(eventName.equals("dropped")) {
			return "dropped";
		}
		if(eventName.equals("thisRoll")) {
			return "thisRoll," + roll + isHost(user);
		}
				
		if(eventName.equals("NEW_PLAYER")) {
			return "NEW_PLAYER" + user.getName();
		}
		if(eventName.equals("WrongField")) {
			return "Das Element darf hier nicht platziert werden";
		}
		if(eventName.equals("SpecialElementalreadyPlaced")) {
			return "Es wurde bereits ein Spezialelement in dieser Runde gesetzt.";
		}
		if(eventName.equals("SpecialElementalreadyPlaced")) {
			return "Es wurde bereits ein Spezialelement in dieser Runde gesetzt.";
		}
		
		if(eventName.equals("AlREADY 3 SPECIAL ELEMENTS")) {
			return " 3 spezielle Elemente wurden schon gesetzt";
		}
		if(eventName.equals("myScore")) {
			return "myScore" + calculateSingleResults(user);
		}
		if(eventName.equals("winnerData")) {
			int index = -1;
			int highestScore = -1000;
			Result highestResult = null;
			for(Result r : results) {
				int score = r.getScore();
				if(score > highestScore) {
					highestScore = score;
					index = results.indexOf(r);
					highestResult = r;
				}
			}
			return "winnerData;" + highestResult.getUser().getName() + ";" + highestResult.getScore() + "," + highestResult.getBoard();
		}
		if(eventName.equals("EndOfGame")) {
			return "EndOfGame" + ",Das Spiel ist beendet.";
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
				gameData += "Spiel beendet!";
				gameData += isHost(user);
				return gameData;
			}
			
			//TODO Fall betrachten, wenn es nicht unentschieden ist
		}
		
		//gameData += isHost(user);
		//gameData += calculateResults();

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
			sendGameDataToClients("NEW_PLAYER");
			//create a new board for the player
			Board board = new Board(user);
			//add it to the list
			boardList.add(board);
		}
		
		
		//if there are as many players as allowed, start the game
		if(playerList.size() == getMaxPlayerAmount()) {
			this.gState = GameState.RUNNING;
			sendGameDataToClients("START");
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
			sendGameDataToClients("PLAYERLEFT");
		}
	}

	public ArrayList<Board> getBoardList() {
		return boardList;
	}	
	
	/* -- HELPFUL METHODS FOR THE IMPLEMENTATION -- */
	
	private RouteElement getElementFromJS(String jsInput) {
		
		//split the String every time there is a comma
		String[] info = jsInput.split(",");
		
		//the first part yields information about the element type
		String typeInfo = info[0];
		//the third part stores the rotation
		int degrees = Integer.parseInt(info[2]);
		//the last part tells you whether it is mirrored or not
		boolean isMirrored = false;
		int mirrorInfo = Integer.parseInt(info[3]);
		if(mirrorInfo > 0) {
			isMirrored = true;
		}
		
		//now, process the information
		//the orientation
		Orientations orientation = null;
		degrees = restoreOrientation(degrees);
		switch(degrees) {
		case 0:
			orientation = Orientations.ZERO_DEGREES;
			break;
		case 1:
			orientation = Orientations.NINETY_DEGREES;
			break;
		case 2:
			orientation = Orientations.ONEHUNDREDEIGHTY_DEGREES;
			break;
		case 3:
			orientation = Orientations.TWOHUNDREDSEVENTY_DEGREES;
			break;
		default:
			//undefined behaviour
		}
		
		//the element type
		ElementTypes type = getTypeFromJsInput(typeInfo);
		//since the default orientation is sometimes different in JS and game logic representation, we have to add some degrees sometimes
		switch(type) {
		case STATION_1:
		case STATION_2:
		case STATION_3:
		case STATION_4:
		case ROAD_CROSSROAD:
		case RAIL_CROSSROAD:
			//in all of the above cases, we don't have to do anything
			break;
		case ROAD:
		case RAIL:
		case STATION_TURN:
		case OVERPASS:
			//in the above cases, we have to add ninety degrees
			orientation = Orientations.addNinetyDegrees(orientation);
			break;
		case STATION:
			orientation = Orientations.addNinetyDegrees(orientation);
			orientation = Orientations.addNinetyDegrees(orientation);
			orientation = Orientations.addNinetyDegrees(orientation);
			break;
		case ROAD_TURN:
		case ROAD_TJUNCTION:
		case RAIL_TURN:
		case RAIL_TJUNCTION:
			//in the above cases, we have to add one hundred and eighty degrees
			orientation = Orientations.addOnehundredeightyDegrees(orientation);
			break;
		}
		
		//now we can create the element and return it
		RouteElement element = new RouteElement(orientation, type, isMirrored);
		return element;
	}
	
	private int restoreOrientation(int degrees) {		
		//we can divide the number by 90 because the scaling does not give any more information
		degrees /= 90;
		
		//now, do something depending on whether the number is positive or negative
		if(degrees > 0) {
			//put it in a range of 0 to 3 as more does not make any sense in the context of the game logic
			degrees %= 4;
			//put it in counter-clockwise representation
			//0 or 180 degrees stay the same
			if(degrees == 1) {
				//90 degrees clockwise correspond to 270 degrees counter-clockwise
				degrees = 3;
			}
			if(degrees == 3) {
				//270 degrees clockwise correspond to 90 degrees counter-clockwise
				degrees = 1;
			}
		} else {
			//if it is below zero, the rotation is counter-clockwise, which is what we want
			degrees *= -1;
			//put it in a range of 0 to 3 as more does not make any sense in the context of the game logic
			degrees %= 4;
		}
		
		//return the result
		return degrees;
	}

	private ElementTypes getTypeFromJsInput(String jsInput) {
		//the result is of type ElementTypes
		ElementTypes type = null;
		
		//get rid of the leading white space
		jsInput.trim();
		//we only need the first three characters
		String typeInfo = jsInput.substring(0, 3);
		
		//get the corresponding type depending on the string
		switch(typeInfo) {
		case "1_1":
			type = ElementTypes.RAIL;
			break;
		case "1_2":
			type = ElementTypes.RAIL_TURN;
			break;
		case "1_3":
			type = ElementTypes.RAIL_TJUNCTION;
			break;
		case "1_4":
			type = ElementTypes.ROAD;
			break;	
		case "1_5":
			type = ElementTypes.ROAD_TURN;
			break;
		case "1_6":
			type = ElementTypes.ROAD_TJUNCTION;
			break;
		case "2_1":
			type = ElementTypes.STATION;
			break;
		case "2_2":
			type = ElementTypes.STATION_TURN;
			break;
		case "2_3":
			type = ElementTypes.OVERPASS;
			break;
		case "l_1":
			type = ElementTypes.STATION_1;
			break;
		case "l_2":
			type = ElementTypes.STATION_2;
			break;
		case "l_3":
			type = ElementTypes.ROAD_CROSSROAD;
			break;
		case "l_4":
			type = ElementTypes.RAIL_CROSSROAD;
			break;
		case "l_5":
			type = ElementTypes.STATION_3;
			break;
		case "l_6":
			type = ElementTypes.STATION_4;
			break;
		}
		
		//return the result
		return type;
	}
	
	private String boardToJsRepresentation(Board board) {
		String result = "";
		
		for(Field f : board.getFields()) {
			int pos = f.getPosition();
			pos++;
			if(f.isEmpty()) {
				result += "empty," + pos + ",empty,empty";
			} else {
				RouteElement e = f.getElement();
				boolean isMirrored = e.isMirrored();
				int mirrored = 0;
				if(isMirrored) {
					mirrored = 1;
				}
				Orientations orien = e.getOrientation();
				ElementTypes elemType = e.getType();
				String type = "";
				switch(elemType) {
				case ROAD:
					type = "1_4.png";
					orien = Orientations.subtractNinetyDegrees(orien);
					break;
				case ROAD_TURN:
					type = "1_5.png";
					orien = Orientations.addOnehundredeightyDegrees(orien);
					break;
				case ROAD_TJUNCTION:
					type = "1_6.png";
					orien = Orientations.addOnehundredeightyDegrees(orien);
					break;
				case RAIL:
					type = "1_1.png";
					orien = Orientations.subtractNinetyDegrees(orien);
					break;
				case RAIL_TURN:
					type = "1_2.png";
					orien = Orientations.addOnehundredeightyDegrees(orien);
					break;
				case RAIL_TJUNCTION:
					type = "1_3.png";
					orien = Orientations.addOnehundredeightyDegrees(orien);
					break;
				case STATION:
					type = "2_1.png";
					orien = Orientations.subtractNinetyDegrees(orien);
					break;
				case STATION_TURN:
					type = "2_2.png";
					orien = Orientations.subtractNinetyDegrees(orien);
					break;
				case OVERPASS:
					type = "2_3.png";
					orien = Orientations.subtractNinetyDegrees(orien);
					break;
				case STATION_1:
					type = "l_1.png";
					break;
				case STATION_2:
					type = "l_1.png";
					break;
				case STATION_3:
					type = "l_5.png";
					break;
				case STATION_4:
					type = "l_6.png";
					break;
				case ROAD_CROSSROAD:
					type = "1_3.png";
					break;
				case RAIL_CROSSROAD:
					type = "1_4.png";
					break;
				}
				int orientation = -1;
				switch(orien) {
				case ZERO_DEGREES:
					orientation = 0;
					break;
				case NINETY_DEGREES:
					orientation = -90;
					break;
				case ONEHUNDREDEIGHTY_DEGREES:
					orientation = -180;
					break;
				case TWOHUNDREDSEVENTY_DEGREES:
					orientation = -270;
					break;
				}
				result += type + "," + pos + "," + orientation + "," + mirrored;
			}
			result += ";";
		}
		
		return result;
	}
	
	private String calculateResults() {
		String result = "";
		
		ResultCalculator calc = new ResultCalculator();
		
		for(Board board : boardList) {
			String username = board.getUser().getName();
			int[] res = calc.calculateResult(board);
			result += username + "," + res[0] + "," + res[1] + "," + res[2] + "," + res[3] + "," + res[4] + "," + res[5] + ";";
		}
		
		return result;
	}
	
	private void roll(String gsonString) {
		String dices = "";
		String[] info = gsonString.split(",");
		for(int i = 1; i < info.length; i++) {
			String typeInfo = info[i];
			dices += typeInfo;
			if(i != 4) {
				dices += ",";
			}
		}
		roll = dices;
		sendGameDataToClients("thisRoll");
	}
	
	private String calculateSingleResults(User user) {
		String result = "";
		
		ResultCalculator calc = new ResultCalculator();
		
		int index = playerList.indexOf(user);
		Board board = boardList.get(index);
		
		String username = board.getUser().getName();
		int[] res = calc.calculateResult(board);
		result += username + "," + res[0] + "," + res[1] + "," + res[2] + "," + res[3] + "," + res[4] + "," + res[5] + ";";
		
		String boardRepresentation = boardToJsRepresentation(board);
		Result resClass = new Result(user, res, boardRepresentation);
		results.add(resClass);
		
		return result;
	}
	
}
