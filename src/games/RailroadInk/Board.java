package games.RailroadInk;

import java.util.ArrayList;
import userManagement.User;

public class Board {

	private User user;
	private ArrayList<Field> fields;
	private int numberOfSpecialElements;
	private boolean specialElementPlacedInThisRound;
	
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
	}

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
	
	public void specialElementAdded() {
		this.numberOfSpecialElements++;
	}
	
	public boolean isSpecialElementPlacedInThisRound() {
		return specialElementPlacedInThisRound;
	}

	public void setSpecialElementPlacedInThisRound(boolean specialElementPlacedInThisRound) {
		this.specialElementPlacedInThisRound = specialElementPlacedInThisRound;
	}

	public User getUser() {
		return user;
	}

	public ArrayList<Field> getFields() {
		return fields;
	}

	public int getNumberOfSpecialElements() {
		return numberOfSpecialElements;
	}
	
	/*
	public static void main(String[] args) {
		User user = new User("hallo", "hehe");
		Board board = new Board(user);
		
		Field rightNeighbour = board.getFields().get(31);
		Field leftNeighbour = board.getFields().get(29);
		try {
			leftNeighbour.addElement(new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.ROAD, false));
			rightNeighbour.addElement(new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.RAIL, false));
		} catch (IllegalPlayerMoveException e) {
			e.printStackTrace();
		}
			
		Field f = board.getFields().get(30);
		RouteElement e = new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.ROAD, false);
		
		boolean canBePlaced = board.canElementBePlaced(f, e);
		
		System.out.println(canBePlaced);
	}
	*/
}
