package games.RailroadInk;

import java.util.ArrayList;

/**
 * Objects of this class represent the AI. They can be used to let a single player have atleast one opponent.
 */
public class ArtificialIntelligence {

	//the AI has a board
	private Board board;
	
	//constructor
	/**
	 * This constructor creates an instance of the artificial intelligence. Therefore, a user has to be created, which has the user name
	 * "Computer-Spieler". The user is necessary in order to create a board on which the AI can place elements. The board is instantiated as well.
	 */
	public ArtificialIntelligence() {
		//create a user just for the sake of being able to create a board
		userManagement.User user = new userManagement.User("Computer-Spieler", "asdfasdfasdfasdfasdf");
		
		//create the board of the AI
		board = new Board(user);
	}

	//a method that simulates the turn of the AI
	/**
	 * Given a number of route elements, this method places these elements on the board of the AI opponent. Thus, it can be used to simulate the turn
	 * of the AI if it is given the route elements that are the result of rolling the dice at the beginning of a round. Moreover this means, that this
	 * is the only method that needs to be employed in order to have a working AI.
	 * @param elements The route elements the AI is supposed to use.
	 * @return {@code true} if the AI was able to place as much elements as possible on the board without any problems. {@code false} if no route elements
	 * were given, i.e. the Array {@code elements} is {@code null} or empty, or if any kind of problem occurred during the simulation of the round.
	 */
	public boolean finishTurn(RouteElement[] elements) {
		//a boolean value is used to indicate whether or not the AI was able to finish their turn
		boolean finished = false;
		
		//first, check whether there actually are any elements in the array
		if(elements == null || elements.length == 0) {
			//if there are none, the AI obviously cannot finish the turn
			return false;
		}
		
		//transfer the elements into an ArrayList in order to go through them easier
		ArrayList<RouteElement> routeElements = new ArrayList<RouteElement>();
		for(int i = 0; i < elements.length; i++) {
			routeElements.add(elements[i]);
		}
		
		//find out whether the AI can make a move or not
		boolean movesLeft = board.movesLeft(elements);
		while(movesLeft) {
			//create a new Array that can be used to get possible moves
			RouteElement[] remainingElements = new RouteElement[routeElements.size()];
			for(int i = 0; i < remainingElements.length; i++) {
				remainingElements[i] = routeElements.get(i);
			}
			
			//get a possible move
			String move = board.proposeMove(remainingElements);
			
			//check whether the move is valid
			if(move == null || move.isEmpty()) {
				//in this case, something went wrong, so let's return false as well
				return false;
			}
			
			//split the string
			String[] info = move.split(",");
			
			//check whether the information is useful or not
			if(info == null || info.length != 5) {
				//something went wrong again, return false
				return false;
			}
			
			//if we end up here, we can actually make the proposed move
			//get the corresponding field
			int position = Integer.parseInt(info[1]);
			Field f = board.getFields().get(position);
			//get the orientation
			String orientation = info[3];
			Orientations direction = null;
			if(orientation.equals("zero degrees")) {
				direction = Orientations.ZERO_DEGREES;
			}
			if(orientation.equals("ninety degrees")) {
				direction = Orientations.NINETY_DEGREES;
			}
			if(orientation.equals("one hundred and eighty degrees")) {
				direction = Orientations.ONEHUNDREDEIGHTY_DEGREES;
			}
			if(orientation.equals("two hundred and seventy degrees")) {
				direction = Orientations.TWOHUNDREDSEVENTY_DEGREES;
			}
			//get the information on whether it is mirrored or not
			boolean isMirrored = false;
			String mirrored = info[4];
			if(mirrored.equals("true")) {
				isMirrored = true;
			}
			//get the type
			String type = info[2];
			ElementTypes elementType = ElementTypes.getTypeViaString(type);
			
			//create the corresponding route element
			RouteElement element = new RouteElement(direction, elementType, isMirrored);
			
			//add it to the field
			try {
				f.addElement(element);
			} catch (IllegalPlayerMoveException e) {
				//something went wrong, return false
				return false;
			}
			
			//remove the corresponding element from the list of elements
			int index = -1;
			for(RouteElement r : routeElements) {
				if(r.sameType(element)) {
					index = routeElements.indexOf(r);
				}
			}
			if(index == -1) {
				System.out.println("hilfe");
				return false;
			}
			routeElements.remove(index);
			
			//create the remaining elements anew
			remainingElements = new RouteElement[routeElements.size()];
			for(int i = 0; i < remainingElements.length; i++) {
				remainingElements[i] = routeElements.get(i);
			}
			
			movesLeft = board.movesLeft(remainingElements);
		}
		
		//if we end up here and nothing went wrong, we can say that the turn is finished
		finished = true;
		
		//return whether it was successful or not
		return finished;
	}
	
	//getter for the board
	/**
	 * This method returns the board of the AI opponent. Thus, it can also be used to access for instance the name of the AI opponent by using the 
	 * method {@code getUser()} in Board.
	 * @return The board of the AI opponent.
	 */
	public Board getBoard() {
		return board;
	}
	
	/*
	public static void main(String[] args) {
		ArtificialIntelligence AI = new ArtificialIntelligence();
		
		RouteElement[] turn1 = new RouteElement[4];
		turn1[0] = new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.RAIL_TJUNCTION, false);
		turn1[1] = new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.ROAD, false);
		turn1[2] = new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.ROAD_TURN, false);
		turn1[3] = new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.STATION, false);
		
		AI.finishTurn(turn1);
		
		RouteElement[] turn2 = new RouteElement[4];
		turn2[0] = new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.RAIL_TJUNCTION, false);
		turn2[1] = new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.RAIL_TJUNCTION, false);
		turn2[2] = new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.ROAD_TURN, false);
		turn2[3] = new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.STATION, false);
		
		AI.finishTurn(turn2);
		
		RouteElement[] turn3 = new RouteElement[4];
		turn3[0] = new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.RAIL, false);
		turn3[1] = new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.OVERPASS, false);
		turn3[2] = new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.ROAD, false);
		turn3[3] = new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.ROAD_TURN, false);
		
		AI.finishTurn(turn3);
		
		RouteElement[] turn4 = new RouteElement[4];
		turn4[0] = new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.RAIL, false);
		turn4[1] = new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.OVERPASS, false);
		turn4[2] = new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.ROAD, false);
		turn4[3] = new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.ROAD_TURN, false);
		
		AI.finishTurn(turn4);
		
		RouteElement[] turn5 = new RouteElement[4];
		turn5[0] = new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.RAIL_TURN, false);
		turn5[1] = new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.STATION_TURN, false);
		turn5[2] = new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.ROAD, false);
		turn5[3] = new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.ROAD_TJUNCTION, false);
		
		AI.finishTurn(turn5);
		
		RouteElement[] turn6 = new RouteElement[4];
		turn6[0] = new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.RAIL_TJUNCTION, false);
		turn6[1] = new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.STATION, false);
		turn6[2] = new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.ROAD, false);
		turn6[3] = new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.ROAD_TURN, false);
		
		AI.finishTurn(turn6);
		
		RouteElement[] turn7 = new RouteElement[4];
		turn7[0] = new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.RAIL_TURN, false);
		turn7[1] = new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.RAIL_TURN, false);
		turn7[2] = new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.RAIL_TURN, false);
		turn7[3] = new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.STATION, false);
		
		AI.finishTurn(turn7);
		
		for(Field f : AI.getBoard().getFields()) {
			if(!f.isEmpty()) {
				System.out.println(f);
				System.out.println(f.getElement());
				System.out.println("");
			}
		}
		
		ResultCalculator calc = new ResultCalculator();
		System.out.println(calc.resultToString(AI.getBoard()));
	}
	*/
}
