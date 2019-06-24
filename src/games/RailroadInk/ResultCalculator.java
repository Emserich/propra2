package games.RailroadInk;

import java.util.ArrayList;

public class ResultCalculator {
	
	/* -- THE MOST IMPORTANT METHOD OF THIS CLASS -- */
	
	public int calculateResult(Board board) {
		//the result is a score
		int score = 0;
		
		//find the networks
		ArrayList<Network> networks = findNetworks(board);
		
		//go through each network
		for(Network n : networks) {
			//get the error counter
			int errors = n.getErrors();
			//for every error, the player loses a point
			score -= errors;
			
			//for every linked exit, the player gets points
			//TODO change depending on how the result is actually calculated
			ArrayList<Exits> exits = n.getExits();
			score += exits.size() * 3;
			
			if(exits.size() > 5) {
				score += exits.size() - 5;
			}
			
			System.out.println(n);
			
		}
		
		//get the longest road 
		int longestRoad = longestRoad(networks);
		//for the every field in the longest road, the player gets a point
		score += longestRoad;
		
		//get the longest track
		int longestTrack = longestTrack(networks);
		//for every field in the longest track, the player gets a point
		score += longestTrack;
		
		//find out, how many of the central fields have been used
		ArrayList<Field> usedCentralFields = usedCentralFields(networks);
		//for every used central field, the player gets a point
		score += usedCentralFields.size();
		
		//return the score
		return score;
	}
	
	/* -- METHODS THAT ARE USED TO CALCULATE THE SCORE --*/
	
	private int longestRoad(ArrayList<Network> networks) {
		//the result is an integer value
		int longestRoadLength = 0;
		
		//go through each network
		for(Network n : networks) {
			//get the length of the longest road
			try {
				int currentLength = n.getLongestRoad().size();
				//if it is longer than the current longest road, change the value
				if(longestRoadLength < currentLength) {
					longestRoadLength = currentLength;
				}
			} catch (NullPointerException e) {
				//there could be the case that the network does not have any roads
				//but that does not really matter
				e.printStackTrace();
			}
		}
		
		//return the result
		return longestRoadLength;
	}
	
	private int longestTrack(ArrayList<Network> networks) {
		//the result is an integer value
		int longestTrackLength = 0;
		
		//go through each network
		for(Network n : networks) {
			//get the length of the longest track
			try {
				int currentLength = n.getLongestTrack().size();
				//if it is longer than the current longest track, change the value
				if(longestTrackLength < currentLength) {
					longestTrackLength = currentLength;
				}
			} catch (NullPointerException e) {
				//there could be the case that the network does not have any roads
				//but that does not really matter
				e.printStackTrace();
			}
		}
		
		//return the result 
		return longestTrackLength;
	}
	
	private ArrayList<Field> usedCentralFields(ArrayList<Network> networks) {
		//the result is an ArrayList of Fields
		ArrayList<Field> result = new ArrayList<Field>();
		
		//go through each network
		for(Network n : networks) {
			//go through each field in the network
			for(Field f : n.getFields()) {
				//check whether it is one of the central fields
				if(!isCentralField(f)) {
					//if it is not, go on with the next one
					continue;
				}
				//if it is, check whether it is already in the result
				if(containsField(result, f)) {
					//if it is, go on with the next one
					continue;
				}
				//if not, add it to the result
				result.add(f);
			}
		}
		
		//TODO for testing, remove later
		System.out.println("\nUsed Central Fields:\n");
		for(Field f : result) {
			System.out.println(f);
		}
		System.out.println("\n");
		
		//return the result
		return result;
	}
	
	private ArrayList<Network> findNetworks(Board board) {
		//the result is an ArrayList of networks
		ArrayList<Network> networks = new ArrayList<Network>();
		
		//we have to keep looking until there are no more fields that don't belong to any network
		boolean fieldsLeft = true;
		
		while(fieldsLeft) {
		
			//find a starting point
			Field startingPoint = findStartingPoint(board, networks);
		
			//if the result is null, then there are no more networks to be found
			if(startingPoint == null) {
				fieldsLeft = false;
				continue;
			}
			
			//if it is not null, create a new network and add it to the result
			Network network = new Network();
			networks.add(network);
			
			//then, grow it beginning from the starting point
			growNetwork(board, network, startingPoint);
	
		}

		
		//return the result
		return networks;
	}
	
	/* -- HELPING METHODS -- */
	
	/**
	 * This method checks whether a field is part of an ArrayList containing fields. For example, it can be used in order to check whether a field is 
	 * already part of a network.
	 * @param fields The list of fields which the evaluated field could already be part of.
	 * @param f The field that is examined.
	 * @return {@code true} if the field is already in the list, {@code false} if not or either the field or the list is null, or the list is empty.
	 */
	private boolean containsField(ArrayList<Field> fields, Field f) {
		//the result is a boolean value
		boolean alreadyContainsField = false;
		
		//check whether either the field or the list is null, and if so, return false
		if(fields == null || fields.isEmpty()) {
			return false;
		}
		
		if(f == null) {
			return false;
		}
		
		//go through each field in the list
		for(Field currField : fields) {
			//compare the position of the current field with the one we're looking at
			//the position is relative to the whole board, so we can find out whether it's the same field
			if(f.getPosition() == currField.getPosition()) {
				//if they are equal, the field is already in the list
				alreadyContainsField = true;
			}
		}
		
		//return the result
		return alreadyContainsField;
	}
	
	private boolean isCentralField(Field f) {
		if(f.getPosition() == 16 || f.getPosition() == 17 || f.getPosition() == 18) {
			return true;
		}
		
		if(f.getPosition() == 23 || f.getPosition() == 24 || f.getPosition() == 25) {
			return true;
		}
		
		if(f.getPosition() == 30 || f.getPosition() == 31 || f.getPosition() == 32) {
			return true;
		}
		
		return false;
	}
	
	private Field findStartingPoint(Board board, ArrayList<Network> networks) {
		//the result is a field
		Field startingPoint = null;
		
		//you have to go trough the fields of the board
		ArrayList<Field> fields = board.getFields();
		
		//go through each field of the board
		for(Field f : fields) {
			//check whether the current field is empty or not
			if(f.isEmpty()) {
				//it is empty, it is not interesting, so just go on with the next one
				continue;
			}
			//if it is not empty, you also have to check whether the field is already part of another network
			boolean isPartOfAnotherNetwork = false;
			//therefore, go through each network, if there are any
			if(networks == null || networks.isEmpty()) {
				//in this case, we've found a non-empty field but there are no networks, so we can just set the result and return
				startingPoint = f;
				return startingPoint;
			}
			//if we end up here, there are networks to go through
			for(Network n : networks) {
				//get the fields of the network
				ArrayList<Field> fieldsOfTheNetwork = n.getFields();
				//check whether the field is part of it
				if(containsField(fieldsOfTheNetwork, f)) {
					isPartOfAnotherNetwork = true;
					//if it is, we don't have to go through the other networks
					break;
				}
			}
			
			//if it is part of another network, just go on with the next field
			if(isPartOfAnotherNetwork) {
				continue;
			}
			
			//if it is not part of another network, just return the field as the result
			startingPoint = f;
			return startingPoint;
		}

		
		//return the result (if we end up here it is null)
		return startingPoint;
	}
	
	public void growNetwork(Board board, Network network, Field field) {
		//check whether the field is already part of the network
		if(containsField(network.getFields(), field)) {
			//in this case, the field is already part of the network, therefore return
			return;
		}
		
		//if not, add the field to the network
		network.getFields().add(field);
		
		//get the element
		RouteElement element = field.getElement();
		
		//get the matching road and rail connections of the field
		Directions[] roadConnections = field.getElement().getRoadConnections();
		Directions[] railConnections = field.getElement().getRailConnections();
		
		//go through each road connection, as long as the array is not null
		if(roadConnections != null) {
			for(int i = 0; i < roadConnections.length; i++) {
				//depending on the direction, we have to get the neighbouring field
				Field neighbour = null;
				switch(roadConnections[i]) {
				case NORTH:
					neighbour = board.getTopNeighbour(field);
					break;
				case EAST:
					neighbour = board.getRightNeighbour(field);
					break;
				case SOUTH:
					neighbour = board.getBottomNeighbour(field);
					break;
				case WEST:
					neighbour = board.getLeftNeighbour(field);
					break;
				default:
					//undefined behaviour, just do nothing
				}
				
				//if the neighbour is null, then the network obviously cannot grow any further into this direction
				if(neighbour == null) {
					//therefore, go on with the next one
					continue;
				}
				
				//if the neighbour is empty, then this counts as an error
				if(neighbour.isEmpty()) {
					network.addError();
					//TODO for testing, remove later
					System.out.println(field + ", Direction: " + roadConnections[i]);
					//and obviously, the network cannot grow any further
					continue;
				}
				
				//if the neighbour is not empty, check whether it has a connection to the current field
				Directions[] neighbourConnections = neighbour.getElement().getRoadConnections();
				boolean matchingConnection = false;
				if(neighbourConnections != null) {
					//therefore, go through each of the connections
					for(int k = 0; k < neighbourConnections.length; k++) {
						//if we have already found a matching connection, stop
						if(matchingConnection) {
							break;
						}
						
						//check whether the direction of the neighbour matches the one of the field we're looking at
						switch(neighbourConnections[k]) {
						case NORTH:
							if(roadConnections[i] == Directions.SOUTH) {
								matchingConnection = true;
							}
							break;
						case EAST:
							if(roadConnections[i] == Directions.WEST) {
								matchingConnection = true;
							}
							break;
						case SOUTH:
							if(roadConnections[i] == Directions.NORTH) {
								matchingConnection = true;
							}
							break;
						case WEST:
							if(roadConnections[i] == Directions.EAST) {
								matchingConnection = true;
							}
							break;
						default:
							//undefined behaviour, just do nothing and hope it helps
						}
					}
				}
				//if there is no matching connection, the network can obviously not grow any further
				if(!matchingConnection) {
					//moreover, it counts as an error
					network.addError();
					//TODO for testing, remove later
					System.out.println(field + ", Direction: " + roadConnections[i]);
					continue;
				}
				
				//if the neighbour has a matching connection, check whether it is one of the exits
				if(neighbour.getPosition() < 0) {
					int position = neighbour.getPosition();
					switch(position) {
					case -1:
						network.getExits().add(Exits.TOP_EXIT_LEFT);
						break;
					case -3:
						network.getExits().add(Exits.TOP_EXIT_MIDDLE);
						break;
					case -5:
						network.getExits().add(Exits.TOP_EXIT_RIGHT);
						break;
					case -7:
						network.getExits().add(Exits.LEFT_EXIT_TOP);
						break;
					case -13:
						network.getExits().add(Exits.RIGHT_EXIT_TOP);
						break;
					case -21:
						network.getExits().add(Exits.LEFT_EXIT_MIDDLE);
						break;
					case -27:
						network.getExits().add(Exits.RIGHT_EXIT_MIDDLE);
						break;
					case -35:
						network.getExits().add(Exits.LEFT_EXIT_BOTTOM);
						break;
					case -41:
						network.getExits().add(Exits.RIGHT_EXIT_BOTTOM);
						break;
					case -43:
						network.getExits().add(Exits.BOTTOM_EXIT_LEFT);
						break;
					case -45:
						network.getExits().add(Exits.BOTTOM_EXIT_MIDDLE);
						break;
					case -47:
						network.getExits().add(Exits.BOTTOM_EXIT_RIGHT);
						break;
					default:
						//undefined behaviour, just do nothing
					}
					//and obviously, the network cannot grow any further from this
					continue;
				}
				
				//in all other cases, the network can grow further from here
				growNetwork(board, network, neighbour);
			}
		}
		
		//go through each rail connection, as long as the array is not null
		if(railConnections != null) {
			for(int i = 0; i < railConnections.length; i++) {
				//depending on the direction, we have to get the neighbouring field
				Field neighbour = null;
				switch(railConnections[i]) {
				case NORTH:
					neighbour = board.getTopNeighbour(field);
					break;
				case EAST:
					neighbour = board.getRightNeighbour(field);
					break;
				case SOUTH:
					neighbour = board.getBottomNeighbour(field);
					break;
				case WEST:
					neighbour = board.getLeftNeighbour(field);
					break;
				default:
					//undefined behaviour, just do nothing
				}				
				
				//if the neighbour is null, then the network obviously cannot grow any further into this direction
				if(neighbour == null) {
					//therefore, return
					continue;
				}
				
				//if the neighbour is empty, then this counts as an error
				if(neighbour.isEmpty()) {
					network.addError();
					//TODO for testing, remove later
					System.out.println(field + ", Direction: " + railConnections[i]);
					//and obviously, the network cannot grow any further
					continue;
				}
				
				//if the neighbour is not empty, check whether it has a connection to the current field
				Directions[] neighbourConnections = neighbour.getElement().getRailConnections();
				boolean matchingConnection = false;
				if(neighbourConnections != null) {
					//therefore, go through each of the connections
					for(int k = 0; k < neighbourConnections.length; k++) {
						//if we have already found a matching connection, stop
						if(matchingConnection) {
							break;
						}
						
						//check whether the direction of the neighbour matches the one of the field we're looking at
						switch(neighbourConnections[k]) {
						case NORTH:
							if(railConnections[i] == Directions.SOUTH) {
								matchingConnection = true;
							}
							break;
						case EAST:
							if(railConnections[i] == Directions.WEST) {
								matchingConnection = true;
							}
							break;
						case SOUTH:
							if(railConnections[i] == Directions.NORTH) {
								matchingConnection = true;
							}
							break;
						case WEST:
							if(railConnections[i] == Directions.EAST) {
								matchingConnection = true;
							}
							break;
						default:
							//undefined behaviour, just do nothing and hope it helps
						}
					}
				}
				//if there is no matching connection, the network can obviously not grow any further
				if(!matchingConnection) {
					//moreover, it counts as an error
					network.addError();
					//TODO for testing, remove later
					System.out.println(field + ", Direction: " + railConnections[i]);
					continue;
				}
				
				//if the neighbour has a matching connection, check whether it is one of the exits
				if(neighbour.getPosition() < 0) {
					int position = neighbour.getPosition();
					switch(position) {
					case -1:
						network.getExits().add(Exits.TOP_EXIT_LEFT);
						break;
					case -3:
						network.getExits().add(Exits.TOP_EXIT_MIDDLE);
						break;
					case -5:
						network.getExits().add(Exits.TOP_EXIT_RIGHT);
						break;
					case -7:
						network.getExits().add(Exits.LEFT_EXIT_TOP);
						break;
					case -13:
						network.getExits().add(Exits.RIGHT_EXIT_TOP);
						break;
					case -21:
						network.getExits().add(Exits.LEFT_EXIT_MIDDLE);
						break;
					case -27:
						network.getExits().add(Exits.RIGHT_EXIT_MIDDLE);
						break;
					case -35:
						network.getExits().add(Exits.LEFT_EXIT_BOTTOM);
						break;
					case -41:
						network.getExits().add(Exits.RIGHT_EXIT_BOTTOM);
						break;
					case -43:
						network.getExits().add(Exits.BOTTOM_EXIT_LEFT);
						break;
					case -45:
						network.getExits().add(Exits.BOTTOM_EXIT_MIDDLE);
						break;
					case -47:
						network.getExits().add(Exits.BOTTOM_EXIT_RIGHT);
						break;
					default:
						//undefined behaviour, just do nothing
					}
					//and obviously, the network cannot grow any further from this
					continue;
				}
				
				//in all other cases, the network can grow further from here
				growNetwork(board, network, neighbour);
				
			}
		}
	}
	
	public void findLongestRoad(Network network) {
		//copy the fields of the network into a new ArrayList (to not influence the fields of the network)
		ArrayList<Field> fieldsCopy = new ArrayList<Field>();
		
		//only do that if the network has fields (but that should be the case)
		if(network.getFields() == null || network.getFields().isEmpty()) {
			return;
		}
		
		//copy the list
		for(Field f : network.getFields()) {
			fieldsCopy.add(f);
		}

		
	}
	/*
	public static void main(String[] args) throws IllegalPlayerMoveException {
		
		userManagement.User user = new userManagement.User("hallo", "hehe");
		Board board = new Board(user);
		
		Field one = board.getFields().get(1);
		one.addElement(new RouteElement(Orientations.NINETY_DEGREES, ElementTypes.ROAD, false));
		
		Field three = board.getFields().get(3);
		three.addElement(new RouteElement(Orientations.NINETY_DEGREES, ElementTypes.RAIL, false));
		
		Field four = board.getFields().get(4);
		four.addElement(new RouteElement(Orientations.NINETY_DEGREES, ElementTypes.OVERPASS, false));
		
		Field seven = board.getFields().get(7);
		seven.addElement(new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.RAIL, false));
		
		Field eight = board.getFields().get(8);
		eight.addElement(new RouteElement(Orientations.NINETY_DEGREES, ElementTypes.OVERPASS, false));
		
		Field nine = board.getFields().get(9);
		nine.addElement(new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.RAIL, false));
		
		Field ten = board.getFields().get(10);
		ten.addElement(new RouteElement(Orientations.ONEHUNDREDEIGHTY_DEGREES, ElementTypes.STATION_2, false));
		
		Field eleven = board.getFields().get(11);
		eleven.addElement(new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.STATION_TURN, true));
		
		Field fifteen = board.getFields().get(15);
		fifteen.addElement(new RouteElement(Orientations.NINETY_DEGREES, ElementTypes.ROAD, false));
		
		Field seventeen = board.getFields().get(17);
		seventeen.addElement(new RouteElement(Orientations.NINETY_DEGREES, ElementTypes.ROAD_TURN, false));
		
		Field eighteen = board.getFields().get(18);
		eighteen.addElement(new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.ROAD, false));
		
		Field nineteen = board.getFields().get(19);
		nineteen.addElement(new RouteElement(Orientations.TWOHUNDREDSEVENTY_DEGREES, ElementTypes.ROAD_TURN, false));
		
		Field twentyone = board.getFields().get(21);
		twentyone.addElement(new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.ROAD, false));
		
		Field twentytwo = board.getFields().get(22);
		twentytwo.addElement(new RouteElement(Orientations.ONEHUNDREDEIGHTY_DEGREES, ElementTypes.ROAD_TJUNCTION, false));
		
		Field twentythree = board.getFields().get(23);
		twentythree.addElement(new RouteElement(Orientations.TWOHUNDREDSEVENTY_DEGREES, ElementTypes.ROAD_TURN, false));
		
		Field twentysix = board.getFields().get(26);
		twentysix.addElement(new RouteElement(Orientations.NINETY_DEGREES, ElementTypes.ROAD_TJUNCTION, false));
		
		Field twentyseven = board.getFields().get(27);
		twentyseven.addElement(new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.ROAD, false));
		
		Field thirty = board.getFields().get(30);
		thirty.addElement(new RouteElement(Orientations.NINETY_DEGREES, ElementTypes.ROAD, false));
		
		Field thirtyone = board.getFields().get(31);
		thirtyone.addElement(new RouteElement(Orientations.NINETY_DEGREES, ElementTypes.STATION_TURN, false));
		
		Field thirtytwo = board.getFields().get(32);
		thirtytwo.addElement(new RouteElement(Orientations.NINETY_DEGREES, ElementTypes.ROAD_CROSSROAD, false));
		
		Field thirtythree = board.getFields().get(33);
		thirtythree.addElement(new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.STATION_3, false));
		
		Field thirtyfive = board.getFields().get(35);
		thirtyfive.addElement(new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.RAIL, false));
		
		Field thirtysix = board.getFields().get(36);
		thirtysix.addElement(new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.RAIL_TJUNCTION, false));
		
		Field thirtyseven = board.getFields().get(37);
		thirtyseven.addElement(new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.STATION_TURN, true));
		
		Field thirtyeight = board.getFields().get(38);
		thirtyeight.addElement(new RouteElement(Orientations.NINETY_DEGREES, ElementTypes.RAIL, false));
		
		Field forty = board.getFields().get(40);
		forty.addElement(new RouteElement(Orientations.NINETY_DEGREES, ElementTypes.RAIL_TURN, false));
		
		Field fortyone = board.getFields().get(41);
		fortyone.addElement(new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.RAIL, false));
		
		Field fortythree = board.getFields().get(43);
		fortythree.addElement(new RouteElement(Orientations.TWOHUNDREDSEVENTY_DEGREES, ElementTypes.STATION, false));
		
		Field fortyfive = board.getFields().get(45);
		fortyfive.addElement(new RouteElement(Orientations.NINETY_DEGREES, ElementTypes.RAIL_TJUNCTION, false));
		
		Field fortysix = board.getFields().get(46);
		fortysix.addElement(new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.RAIL, false));
		
		Field fortyseven = board.getFields().get(47);
		fortyseven.addElement(new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.STATION_TURN, false));
		
		ResultCalculator calc = new ResultCalculator();
		int result = calc.calculateResult(board);
		System.out.println(result);
	}
	*/
}
