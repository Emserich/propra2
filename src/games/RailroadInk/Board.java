package games.RailroadInk;

import java.util.ArrayList;
import userManagement.User;

public class Board {

	/* -- ATTRIBUTES -- */
	
	private User user;
	private ArrayList<Field> fields;
	private int numberOfSpecialElements;
	private boolean specialElementPlacedInThisRound;
	
	private boolean roadCrossroadPlaced;
	private boolean railCrossroadPlaced;
	private boolean stationOnePlaced;
	private boolean stationTwoPlaced;
	private boolean stationThreePlaced;
	private boolean stationFourPlaced;
	
	/* -- CONSTRUCTOR -- */
	
	public Board(User user) {
		//a board is always linked to one user
		this.user = user;
		//one counter is necessary to count how many special elements have been placed --> you are only allowed to place three per match
		this.numberOfSpecialElements = 0;
		//a boolean value is used to store whether a special element has been placed this round --> you are only allowed to place one each round
		this.specialElementPlacedInThisRound = false;
		
		//the fields have to be initiated
		this.fields = new ArrayList<Field>();
		//there are 49 elements
		for(int i = 0; i < 49; i++) {
			Field field = new Field(i);
			fields.add(field);
		}
		
		//the variables that check whether the special elements have already been placed have to be initialized
		//of course, at the beginning, they are not placed
		this.railCrossroadPlaced = false;
		this.roadCrossroadPlaced = false;
		this.stationOnePlaced = false;
		this.stationTwoPlaced = false;
		this.stationThreePlaced = false;
		this.stationFourPlaced = false;
	}

	/* -- METHODS TO ACCESS NEIGHBOURING FIELDS -- */
	
	public Field getRightNeighbour(Field f) {
		int position = f.getPosition();
		
		if(position == 13) {
			//in this case, the right neighbour is an exit with a rail connection
			return Exits.RIGHT_EXIT_TOP.getField();
		}
		
		if(position == 41) {
			//in this case, the right neighbour is an exit with a rail connection
			return Exits.RIGHT_EXIT_BOTTOM.getField();
		}
		
		if(position == 27) {
			//in this case, the right neighbour is an exit with a road connection
			return Exits.RIGHT_EXIT_MIDDLE.getField();
		}
		
		if(position == 6 || position == 20 || position == 34 || position == 48) {
			//in this case, the right neighbour is non-existent 
			return null;
		}
		
		//in all other cases, the right neighbour is just the field with a position one higher than the one of the field we're looking at
		return fields.get(position + 1);
	}
	
	public Field getTopNeighbour(Field f) {
		int position = f.getPosition();

		if(position == 3) {
			//in this case, the top neighbour is an exit with a rail connection
			return Exits.TOP_EXIT_MIDDLE.getField();
		}
		
		if(position == 1) {
			//in this case, the top neighbour is an exit with a road connection
			return Exits.TOP_EXIT_LEFT.getField();
		}
		
		if(position == 5) {
			//in this case, the top neighbour is an exit with a road connection
			return Exits.TOP_EXIT_RIGHT.getField();
		}
		
		if(position == 0 || position == 2 || position == 4 || position == 6) {
			//in this case, the top neighbour is non-existent 
			return null;
		}
		
		//in all other cases, the top neighbour is just the field with a position seven lower than the one of the field we're looking at
		return fields.get(position - 7);
		
	}
	
	public Field getLeftNeighbour(Field f) {
		int position = f.getPosition();

		if(position == 7) {
			//in this case, the left neighbour is an exit with a rail connection
			return Exits.LEFT_EXIT_TOP.getField();
		}
		
		if(position == 35) {
			//in this case, the left neighbour is an exit with a rail connection
			return Exits.LEFT_EXIT_BOTTOM.getField();
		}
		
		if(position == 21) {
			//in this case, the left neighbour is an exit with a road connection
			return Exits.LEFT_EXIT_MIDDLE.getField();
		}
		
		if(position == 0 || position == 14 || position == 28 || position == 42) {
			//in this case, the left neighbour is non-existent 
			return null;
		}
		
		//in all other cases, the left neighbour is just the field with a position one lower than the one of the field we're looking at
		return fields.get(position - 1);
		
	}
	
	public Field getBottomNeighbour(Field f) {
		int position = f.getPosition();
		
		if(position == 45) {
			//in this case, the bottom neighbour is an exit with a rail connection
			return Exits.BOTTOM_EXIT_MIDDLE.getField();
		}
		
		if(position == 43) {
			//in this case, the bottom neighbour is an exit with a road connection
			return Exits.BOTTOM_EXIT_LEFT.getField();
		}
		
		if(position == 47) {
			//in this case, the bottom neighbour is an exit with a road connection
			return Exits.BOTTOM_EXIT_RIGHT.getField();
		}
		
		if(position == 42 || position == 44 || position == 46 || position == 48) {
			//in this case, the bottom neighbour is non-existent 
			return null;
		}
		
		//in all other cases, the bottom neighbour is just the field with a position seven higher than the one of the field we're looking at
		return fields.get(position + 7);

	}
	
	/**
	 * This method returns the neighbouring fields of a given field. The neighbours are arranged like this: The first element of the array is the right neighbour,
	 * the second element is the top neighbour, the third element is the left neighbour and the fourth and final element is the bottom neighbour of the field.
	 * @param f The field that you want to get the neighbours of.
	 * @return An array of fields containing the neighbours in the order explained above.
	 */
	public Field[] getNeighbours(Field f) {
		Field[] neighbours = new Field[4];
		
		neighbours[0] = getRightNeighbour(f);
		neighbours[1] = getTopNeighbour(f);
		neighbours[2] = getLeftNeighbour(f);
		neighbours[3] = getBottomNeighbour(f);
		
		return neighbours;
	}
	
	/* -- METHODS THAT CHECK USER INPUTS -- */ 
	
	/**
	 * For a given field and a given route element, this method checks whether the element can be placed on this field. Of course, the element cannot be placed
	 * if there already is one on the field. However, if the field is empty, two things have to be checked: First, if there are any existing connections that the
	 * new element can be connected to (which is necessary according to the rules of the game). Second, it is necessary to check whether any existing connections 
	 * are causing conflicts: For example, you are not allowed to connect a road to a rail.
	 * @param field The field on which the element is supposed to be placed.
	 * @param element The route element that is supposed to be placed on the field.
	 * @return {@code true}, if the element can be placed on this field, {@code false} if not.
	 */
	public boolean canElementBePlaced(Field field, RouteElement element) {
		if(!field.isEmpty()) {
			//if the field is not empty, of course you cannot put an element on it
			return false;
		}
		
		//in order to place an element, there has to be atleast one matching connection that the new element can connect itself with
		boolean connectionExists = false;
		
		//we have to look at the outgoing connections of the neighbouring fields
		//they are initialized as empty arrays to not have to deal with nullpointer exceptions later on
		Directions[] topNeighbourRoadConnections = new Directions[0];
		Directions[] topNeighbourRailConnections = new Directions[0];
		Directions[] rightNeighbourRoadConnections = new Directions[0];
		Directions[] rightNeighbourRailConnections = new Directions[0];
		Directions[] bottomNeighbourRoadConnections = new Directions[0];
		Directions[] bottomNeighbourRailConnections = new Directions[0];
		Directions[] leftNeighbourRoadConnections = new Directions[0];
		Directions[] leftNeighbourRailConnections = new Directions[0];
		
		//get their actual values (if possible)
		Field topNeighbourField = getTopNeighbour(field);
		Field rightNeighbourField = getRightNeighbour(field);
		Field bottomNeighbourField = getBottomNeighbour(field);
		Field leftNeighbourField = getLeftNeighbour(field);
		
		//top neighbour
		try {
			topNeighbourRoadConnections = topNeighbourField.getElement().getRoadConnections();
		} catch (NullPointerException e) {
			//ending up here could mean that the neighbouring field is null (if we're on the border), the element on the field is null or the road connections are null
			//no matter what the reason is, it is not important 
		}
		try {
			topNeighbourRailConnections = topNeighbourField.getElement().getRailConnections();
		} catch (NullPointerException e) {
			//just go on, it is not important
		}
		
		//right neighbour
		try {
			rightNeighbourRoadConnections = rightNeighbourField.getElement().getRoadConnections();
		} catch (NullPointerException e) {
			//just go on, it is not important
		}
		try {
			rightNeighbourRailConnections = rightNeighbourField.getElement().getRailConnections();
		} catch (NullPointerException e) {
			//just go on, it is not important
		}
		
		//bottom neighbour
		try {
			bottomNeighbourRoadConnections = bottomNeighbourField.getElement().getRoadConnections();
		} catch (NullPointerException e) {
			//just go on, it is not important
		}
		try {
			bottomNeighbourRailConnections = bottomNeighbourField.getElement().getRailConnections();
		} catch (NullPointerException e) {
			//just go on, it is not important
		}
		
		//left neighbour
		try {
			leftNeighbourRoadConnections = leftNeighbourField.getElement().getRoadConnections();
		} catch (NullPointerException e) {
			//just go on, it is not important
		}
		try {
			leftNeighbourRailConnections = leftNeighbourField.getElement().getRailConnections();
		} catch (NullPointerException e) {
			//just go on, it is not important
		}

		
		//check the road connections of the element that is supposed to be placed
		Directions[] roadConnections = element.getRoadConnections();
		
		//if there are any, go through them one by one
		try {
			if(roadConnections.length != 0) {
				for(int i = 0; i < roadConnections.length; i++) {
					//get the current direction
					Directions direction = roadConnections[i];
					//depending on the direction, check whether the connections of the corresponding neighbours are problematic or not
					switch(direction) {
					case NORTH:
						//go through the connections of the top neighbour
						//first the road connections
						for(int j = 0; j < topNeighbourRoadConnections.length; j++) {
							//fetch the current direction
							Directions topNeighbourRoadConnection = topNeighbourRoadConnections[j];
							//we only care about a connection to the bottom
							if(topNeighbourRoadConnection == Directions.SOUTH) {
								//in this case, the element has a connection that it can be connected to
								connectionExists = true;
							}
						}
						//then the rail connections
						for(int k = 0; k < topNeighbourRailConnections.length; k++) {
							//fetch the current direction
							Directions topNeighbourRailConnection = topNeighbourRailConnections[k];
							//we only care about a connection to the bottom
							if(topNeighbourRailConnection == Directions.SOUTH) {
								//in this case, the element has a connection that does not allow us to place the element
								//therefore, return false
								return false;
							}
						}
						break;
					case EAST:
						//go through the connections of the right neighbour
						//first the road connections
						for(int j = 0; j < rightNeighbourRoadConnections.length; j++) {
							//fetch the current direction
							Directions rightNeighbourRoadConnection = rightNeighbourRoadConnections[j];
							//we only care about a connection to the left
							if(rightNeighbourRoadConnection == Directions.WEST) {
								//in this case, the element has a connection that it can be connected to
								connectionExists = true;
							}
						}
						//then the rail connections
						for(int k = 0; k < rightNeighbourRailConnections.length; k++) {
							//fetch the current direction
							Directions rightNeighbourRailConnection = rightNeighbourRailConnections[k];
							//we only care about a connection to the left
							if(rightNeighbourRailConnection == Directions.WEST) {
								//in this case, the element has a connection that does not allow us to place the element
								//therefore, return false
								System.out.println("schemona");
								return false;
							}
						}
						break;
					case SOUTH:
						//go through the connections of the bottom neighbour
						//first the road connections
						for(int j = 0; j < bottomNeighbourRoadConnections.length; j++) {
							//fetch the current direction
							Directions bottomNeighbourRoadConnection = bottomNeighbourRoadConnections[j];
							//we only care about a connection to the top
							if(bottomNeighbourRoadConnection == Directions.NORTH) {
								//in this case, the element has a connection that it can be connected to
								connectionExists = true;
							}
						}
						//then the rail connections
						for(int k = 0; k < bottomNeighbourRailConnections.length; k++) {
							//fetch the current direction
							Directions bottomNeighbourRailConnection = bottomNeighbourRailConnections[k];
							//we only care about a connection to the top
							if(bottomNeighbourRailConnection == Directions.NORTH) {
								//in this case, the element has a connection that does not allow us to place the element
								//therefore, return false
								return false;
							}
						}
						break;
					case WEST:
						//go through the connections of the left neighbour
						//first the road connections
						for(int j = 0; j < leftNeighbourRoadConnections.length; j++) {
							//fetch the current direction
							Directions leftNeighbourRoadConnection = leftNeighbourRoadConnections[j];
							//we only care about a connection to the right
							if(leftNeighbourRoadConnection == Directions.EAST) {
								//in this case, the element has a connection that it can be connected to
								connectionExists = true;
							}
						}
						//then the rail connections
						for(int k = 0; k < leftNeighbourRailConnections.length; k++) {
							//fetch the current direction
							Directions leftNeighbourRailConnection = leftNeighbourRailConnections[k];
							//we only care about a connection to the right
							if(leftNeighbourRailConnection == Directions.EAST) {
								//in this case, the element has a connection that does not allow us to place the element
								//therefore, return false
								return false;
							}
						}
						break;
					default:
						//undefined behaviour, just hope that it does not happen
					}
				}
			}
		} catch(NullPointerException e) {
			//in this case, there are no road connections, but that is okay, just don't do anything
		}
		
		//check the rail connections of the element that is supposed to be placed
		Directions[] railConnections = element.getRailConnections();
		
		//if there are any, go through them one by one
		try {
			if(railConnections.length != 0) {
				for(int i = 0; i < roadConnections.length; i++) {
					//get the current direction
					Directions direction = railConnections[i];
					//depending on the direction, check whether the connections of the corresponding neighbours are problematic or not
					switch(direction) {
					case NORTH:
						//go through the connections of the top neighbour
						//first the road connections
						for(int j = 0; j < topNeighbourRoadConnections.length; j++) {
							//fetch the current direction
							Directions topNeighbourRoadConnection = topNeighbourRoadConnections[j];
							//we only care about a connection to the bottom
							if(topNeighbourRoadConnection == Directions.SOUTH) {
								//in this case, the element has a connection that does not allow us to place the element 
								//therefore, return false
								return false;
							}
						}
						//then the rail connections
						for(int k = 0; k < topNeighbourRailConnections.length; k++) {
							//fetch the current direction
							Directions topNeighbourRailConnection = topNeighbourRailConnections[k];
							//we only care about a connection to the bottom
							if(topNeighbourRailConnection == Directions.SOUTH) {
								//in this case, the element has a connection that it can be connected to
								connectionExists = true;
							}
						}
						break;
					case EAST:
						//go through the connections of the right neighbour
						//first the road connections
						for(int j = 0; j < rightNeighbourRoadConnections.length; j++) {
							//fetch the current direction
							Directions rightNeighbourRoadConnection = rightNeighbourRoadConnections[j];
							//we only care about a connection to the left
							if(rightNeighbourRoadConnection == Directions.WEST) {
								//in this case, the element has a connection that does not allow us to place the element
								//therefore, return false
								return false;
							}
						}
						//then the rail connections
						for(int k = 0; k < rightNeighbourRailConnections.length; k++) {
							//fetch the current direction
							Directions rightNeighbourRailConnection = rightNeighbourRailConnections[k];
							//we only care about a connection to the left
							if(rightNeighbourRailConnection == Directions.WEST) {
								//in this case, the element has a connection that it can be connected to
								connectionExists = true;
							}
						}
						break;
					case SOUTH:
						//go through the connections of the bottom neighbour
						//first the road connections
						for(int j = 0; j < bottomNeighbourRoadConnections.length; j++) {
							//fetch the current direction
							Directions bottomNeighbourRoadConnection = bottomNeighbourRoadConnections[j];
							//we only care about a connection to the top
							if(bottomNeighbourRoadConnection == Directions.NORTH) {
								//in this case, the element has a connection that does not allow us to place the element
								//therefore, return false
								return false;
							}
						}
						//then the rail connections
						for(int k = 0; k < bottomNeighbourRailConnections.length; k++) {
							//fetch the current direction
							Directions bottomNeighbourRailConnection = bottomNeighbourRailConnections[k];
							//we only care about a connection to the top
							if(bottomNeighbourRailConnection == Directions.NORTH) {
								//in this case, the element has a connection that it can be connected to
								connectionExists = true;
							}
						}
						break;
					case WEST:
						//go through the connections of the left neighbour
						//first the road connections
						for(int j = 0; j < leftNeighbourRoadConnections.length; j++) {
							//fetch the current direction
							Directions leftNeighbourRoadConnection = leftNeighbourRoadConnections[j];
							//we only care about a connection to the right
							if(leftNeighbourRoadConnection == Directions.EAST) {
								//in this case, the element has a connection that does not allow us to place the element
								//therefore, return false
								return false;
							}
						}
						//then the rail connections
						for(int k = 0; k < leftNeighbourRailConnections.length; k++) {
							//fetch the current direction
							Directions leftNeighbourRailConnection = leftNeighbourRailConnections[k];
							//we only care about a connection to the right
							if(leftNeighbourRailConnection == Directions.EAST) {
								//in this case, the element has a connection that it can be connected to 
								connectionExists = true;
							}
						}
						break;
					default:
						//undefined behaviour, just hope that it does not happen
					}
				}
			}
		} catch(NullPointerException e) {
			//in this case, there are no rail connections, but that is okay, just don't do anything
		}
		
		//if we ended up here, no complications showed up
		//so check if the new element can be connected to anything
		if(connectionExists) {
			//if so, return true
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * This method can be used in order to check whether the player can still do anything in a round. In order to find out whether he can do that, the remaining 
	 * route elements have to be given to this method. Then, this method goes through each remaining element and checks whether the element can be placed anywhere
	 * on the board. In order to do this, every possible combination of rotation and mirroring is checked. 
	 * The method will continue with the next field if the current field is already occupied, and it will instantly terminate once it has found the first possible
	 * move the player can do. This is done in order to save computation time.
	 * The method also works if there are no remaining elements.
	 * @param elements The remaining route elements the user can potentially place on the board.
	 * @return {@code true} if the user can still make any moves, {@code false} if not
	 */
	public boolean movesLeft(RouteElement[] elements) {
		//the player can still make a move as long as a single element can be placed on the board
		boolean movesLeft = false;
		
		//if there are no remaining elements, of course there are no moves left
		if(elements == null || elements.length == 0) {
			return false;
		}
		
		//if there are remaining elements, go through them one by one
		for(int i = 0;  i < elements.length; i++) {
			//if movesLeft is true, stop
			if(movesLeft) {
				break;
			}
			
			//get the current element
			RouteElement element = elements[i];
			
			//check whether the element is null
			if(element == null) {
				//in this case, go on with the next one
				continue;
			}
			
			//create a copy of the element in order to not change the properties of the actual element!
			RouteElement copy = new RouteElement(element.getOrientation(), element.getType(), element.isMirrored());
			
			//now, go through each field that is on the board
			for(Field f : fields) {
				//if the current field is already occupied, just go on with the next one in order to save computation time
				if(!f.isEmpty()) {
					continue;
				}
				
				//check whether the element can be placed on the field in every rotation and every rotation if it is mirrored
				//if a single way to place the element has been found, just stop already and return true
				
				//zero degrees
				copy.setOrientation(Orientations.ZERO_DEGREES);
				copy.setMirrored(false);
				if(canElementBePlaced(f, copy)) {
					movesLeft = true;
					break;
				}
				copy.setMirrored(true);
				if(canElementBePlaced(f, copy)) {
					movesLeft = true;
					break;
				}
				
				//ninety degrees
				copy.setOrientation(Orientations.NINETY_DEGREES);
				copy.setMirrored(false);
				if(canElementBePlaced(f, copy)) {
					movesLeft = true;
					break;
				}
				copy.setMirrored(true);
				if(canElementBePlaced(f, copy)) {
					movesLeft = true;
					break;
				}
				
				//one hundred and eighty degrees
				copy.setOrientation(Orientations.ONEHUNDREDEIGHTY_DEGREES);
				copy.setMirrored(false);
				if(canElementBePlaced(f, copy)) {
					movesLeft = true;
					break;
				}
				copy.setMirrored(true);
				if(canElementBePlaced(f, copy)) {
					movesLeft = true;
					break;
				}
				
				//two hundred and seventy degrees
				copy.setOrientation(Orientations.TWOHUNDREDSEVENTY_DEGREES);
				copy.setMirrored(false);
				if(canElementBePlaced(f, copy)) {
					movesLeft = true;
					break;
				}
				copy.setMirrored(true);
				if(canElementBePlaced(f, copy)) {
					movesLeft = true;
					break;
				}
			}
		}
		
		//return the result
		return movesLeft;
	}
	
	/**
	 * This method can be used in order to check whether the player can still do anything in a round. Moreover, it will return 
	 * a String that contains the information about a possible move, if there are any.
	 * In order to find out whether the player can still do anything, the remaining route elements have to be given to this 
	 * method. Then, this method goes through each remaining element and checks whether the element can be placed anywhere
	 * on the board. In order to do this, every possible combination of rotation and mirroring is checked. 
	 * The method will continue with the next field if the current field is already occupied, and it will instantly terminate once it has found the first possible
	 * move the player can do. This is done in order to save computation time.
	 * The method also works if there are no remaining elements.
	 * @param elements The remaining route elements the user can potentially place on the board.
	 * @return A string formatted like this username,position,element,rotation,mirrored" or {@code null} if there are no 
	 * possible moves left.
	 */
	public String proposeMove(RouteElement[] elements) {
		//the player can still make a move as long as a single element can be placed on the board
		boolean movesLeft = false;
		
		//the String that should contain the result
		String move = null;
		
		//if there are no remaining elements, of course there are no moves left
		if(elements == null || elements.length == 0) {
			return null;
		}
		
		//if there are remaining elements, go through them one by one
		for(int i = 0;  i < elements.length; i++) {
			//if movesLeft is true, stop
			if(movesLeft) {
				break;
			}
			
			//get the current element
			RouteElement element = elements[i];
			
			//check whether the element is null
			if(element == null) {
				//go on with the next one
				continue;
			}
			
			//create a copy of the element in order to not change the properties of the actual element!
			RouteElement copy = new RouteElement(element.getOrientation(), element.getType(), element.isMirrored());
			
			//now, go through each field that is on the board
			for(Field f : fields) {
				//if the current field is already occupied, just go on with the next one in order to save computation time
				if(!f.isEmpty()) {
					continue;
				}
				
				//check whether the element can be placed on the field in every rotation and every rotation if it is mirrored
				//if a single way to place the element has been found, just stop already and return true
				
				//zero degrees
				copy.setOrientation(Orientations.ZERO_DEGREES);
				copy.setMirrored(false);
				if(canElementBePlaced(f, copy)) {
					movesLeft = true;
					//create the string containing the move
					move += getUser().getName() + "," + f.getPosition() + "," + copy.getType() + "," + copy.getOrientation() + "," + copy.isMirrored();
					break;
				}
				copy.setMirrored(true);
				if(canElementBePlaced(f, copy)) {
					movesLeft = true;
					//create the string containing the move
					move += getUser().getName() + "," + f.getPosition() + "," + copy.getType() + "," + copy.getOrientation() + "," + copy.isMirrored();
					break;
				}
				
				//ninety degrees
				copy.setOrientation(Orientations.NINETY_DEGREES);
				copy.setMirrored(false);
				if(canElementBePlaced(f, copy)) {
					movesLeft = true;
					//create the string containing the move
					move += getUser().getName() + "," + f.getPosition() + "," + copy.getType() + "," + copy.getOrientation() + "," + copy.isMirrored();
					break;
				}
				copy.setMirrored(true);
				if(canElementBePlaced(f, copy)) {
					movesLeft = true;
					//create the string containing the move
					move += getUser().getName() + "," + f.getPosition() + "," + copy.getType() + "," + copy.getOrientation() + "," + copy.isMirrored();
					break;
				}
				
				//one hundred and eighty degrees
				copy.setOrientation(Orientations.ONEHUNDREDEIGHTY_DEGREES);
				copy.setMirrored(false);
				if(canElementBePlaced(f, copy)) {
					movesLeft = true;
					//create the string containing the move
					move += getUser().getName() + "," + f.getPosition() + "," + copy.getType() + "," + copy.getOrientation() + "," + copy.isMirrored();
					break;
				}
				copy.setMirrored(true);
				if(canElementBePlaced(f, copy)) {
					movesLeft = true;
					//create the string containing the move
					move += getUser().getName() + "," + f.getPosition() + "," + copy.getType() + "," + copy.getOrientation() + "," + copy.isMirrored();
					break;
				}
				
				//two hundred and seventy degrees
				copy.setOrientation(Orientations.TWOHUNDREDSEVENTY_DEGREES);
				copy.setMirrored(false);
				if(canElementBePlaced(f, copy)) {
					movesLeft = true;
					//create the string containing the move
					move += getUser().getName() + "," + f.getPosition() + "," + copy.getType() + "," + copy.getOrientation() + "," + copy.isMirrored();
					break;
				}
				copy.setMirrored(true);
				if(canElementBePlaced(f, copy)) {
					movesLeft = true;
					//create the string containing the move
					move += getUser().getName() + "," + f.getPosition() + "," + copy.getType() + "," + copy.getOrientation() + "," + copy.isMirrored();
					break;
				}
			}
		}
		
		//return the result
		return move;
	}
	
	/* -- GETTERS AND SETTERS */
	
	/**
	 * This method can be called to increment the amount of special elements placed on this board.
	 */
	public void specialElementAdded() {
		this.numberOfSpecialElements++;
	}
	
	/**
	 * This method can be used in order to check whether a special element has been placed in this round. In order to set the corresponding property, 
	 * the method {@link #setSpecialElementPlacedInThisRound(boolean)} has to be used. 
	 * @return A boolean value indicating whether a special element has been placed in this round.
	 */
	public boolean isSpecialElementPlacedInThisRound() {
		return specialElementPlacedInThisRound;
	}

	/**
	 * This method can be used in order to set whether a special element has been placed on this board in this round. To check whether it has been done or not,
	 * the method {@link #isSpecialElementPlacedInThisRound()} should be used.
	 * @param specialElementPlacedInThisRound The boolean value indicating whether a special element has been placed or not.
	 */
	public void setSpecialElementPlacedInThisRound(boolean specialElementPlacedInThisRound) {
		this.specialElementPlacedInThisRound = specialElementPlacedInThisRound;
	}

	/**
	 * This method returns the user to whom this board belongs.
	 * @return The user playing on this board.
	 */
	public User getUser() {
		return user;
	}

	/**
	 * This method returns every field on this board. The elements in the list also contain information about whether or not the field is empty and about the 
	 * element that has been placed on the field (if there is one).
	 * @return An ArrayList containing every field on the board.
	 */
	public ArrayList<Field> getFields() {
		return fields;
	}

	/**
	 * This method can be used to check how many special elements have been placed on this board. It should be noted that the maximum amount of special elements
	 * that the player is allowed to place is 3.
	 * @return The amount of special elements on this board.
	 */
	public int getNumberOfSpecialElements() {
		return numberOfSpecialElements;
	}

	/**
	 * Players are only allowed to place each special element only once. Therefore, this method can be used to check whether one of these elements has already
	 * been placed.
	 * @return Whether or not the special element 'Road Crossroad' has been placed.
	 */
	public boolean isRoadCrossroadPlaced() {
		return roadCrossroadPlaced;
	}

	/**
	 * Players are only allowed to place each special element only once. Therefore, this method can be used to check whether one of these elements has already
	 * been placed.
	 * @return Whether or not the special element 'Rail Crossroad' has been placed.
	 */
	public boolean isRailCrossroadPlaced() {
		return railCrossroadPlaced;
	}

	/**
	 * Players are only allowed to place each special element only once. Therefore, this method can be used to check whether one of these elements has already
	 * been placed.
	 * @return Whether or not the special element 'Station_One' has been placed, i.e. the first special element from the left if you look at the manual.
	 */
	public boolean isStationOnePlaced() {
		return stationOnePlaced;
	}

	/**
	 * Players are only allowed to place each special element only once. Therefore, this method can be used to check whether one of these elements has already
	 * been placed.
	 * @return Whether or not the special element 'Station_Two' has been placed , i.e. the second special element from the left if you look at the manual.
	 */
	public boolean isStationTwoPlaced() {
		return stationTwoPlaced;
	}

	/**
	 * Players are only allowed to place each special element only once. Therefore, this method can be used to check whether one of these elements has already
	 * been placed.
	 * @return Whether or not the special element 'Station_Three' has been placed, i.e. the fifth special element from the left if you look at the manual.
	 */
	public boolean isStationThreePlaced() {
		return stationThreePlaced;
	}

	/**
	 * Players are only allowed to place each special element only once. Therefore, this method can be used to check whether one of these elements has already
	 * been placed.
	 * @return Whether or not the special element 'Station_Four' has been placed, i.e. the sixth special element from the left if you look at the manual.
	 */
	public boolean isStationFourPlaced() {
		return stationFourPlaced;
	}
	
	/**
	 * This method should be called to store the information that the special element 'Road Crossroad' has been placed. This is important since the player is 
	 * only allowed to place it once. 
	 * To check whether it has already been placed, please use the method {@link #isRoadCrossroadPlaced()}.
	 */
	public void roadCrossroadPlaced() {
		this.roadCrossroadPlaced = true;
	}
	
	/**
	 * This method should be called to store the information that the special element 'Rail Crossroad' has been placed. This is important since the player is 
	 * only allowed to place it once. 
	 * To check whether it has already been placed, please use the method {@link #isRailCrossroadPlaced()}.
	 */
	public void railCrossroadPlaced() {
		this.railCrossroadPlaced = true;
	}
	
	/**
	 * This method should be called to store the information that the special element 'Station_One' has been set. This is important since the player is 
	 * only allowed to place it once. 
	 * To check whether it has already been placed, please use the method {@link #isStationOnePlaced()}.
	 */
	public void stationOnePlaced() {
		this.stationOnePlaced = true;
	}
	
	/**
	 * This method should be called to store the information that the special element 'Station_Two' has been set. This is important since the player is 
	 * only allowed to place it once. 
	 * To check whether it has already been placed, please use the method {@link #isStationTwoPlaced()}.
	 */
	public void stationTwoPlaced() {
		this.stationTwoPlaced = true;
	}
	
	/**
	 * This method should be called to store the information that the special element 'Station_Three' has been set. This is important since the player is 
	 * only allowed to place it once. 
	 * To check whether it has already been placed, please use the method {@link #isStationThreePlaced()}.
	 */
	public void stationThreePlaced() {
		this.stationThreePlaced = true;
	}
	
	/**
	 * This method should be called to store the information that the special element 'Station_Four' has been set. This is important since the player is 
	 * only allowed to place it once. 
	 * To check whether it has already been placed, please use the method {@link #isStationFourPlaced()}.
	 */
	public void stationFourPlaced() {
		this.stationFourPlaced = true;
	}
	
}
