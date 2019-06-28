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
			ArrayList<Exits> exits = n.getExits();

			//however, the player only gets points if they connected more than one exit
			int numberOfExits = exits.size();
			if(numberOfExits > 1) {
				numberOfExits--;
				score += numberOfExits * 4;
			}
			
			//if the network connected all 12 exits, you get a bonus point
			if(numberOfExits == 11) {
				score++;
			}
			
			//get the longest road and the longest track of the network
			findLongestRoad(n);
			findLongestTrack(n);
			
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
		
		//for testing purposes, also save the position of the network
		int position = -1;
		
		//go through each network
		for(Network n : networks) {
			//get the length of the longest road
			try {
				int currentLength = n.getLongestRoad().size();
				//if it is longer than the current longest road, change the value
				if(longestRoadLength < currentLength) {
					longestRoadLength = currentLength;
					
					//for testing purposes, also get the position of the network
					position = networks.indexOf(n);
				}
			} catch (NullPointerException e) {
				//there could be the case that the network does not have any roads
				//but that does not really matter
				e.printStackTrace();
			}
		}
		
		//TODO for testing, remove later
		if(position != -1) {
			System.out.println("\nLongest Road:\n");
			Network longestRoad = networks.get(position);
			for(Field f : longestRoad.getLongestRoad()) {
				System.out.println(f);
			}
			System.out.println("\n----------\n");
		}
			
		//return the result
		return longestRoadLength;
	}
	
	private int longestTrack(ArrayList<Network> networks) {
		//the result is an integer value
		int longestTrackLength = 0;
		
		//for testing purposes, also save the position of the network
		int position = -1;
		
		//go through each network
		for(Network n : networks) {
			//get the length of the longest track
			try {
				int currentLength = n.getLongestTrack().size();
				//if it is longer than the current longest track, change the value
				if(longestTrackLength < currentLength) {
					longestTrackLength = currentLength;
					
					//for testing purposes, also get the position of the network
					position = networks.indexOf(n);
				}
			} catch (NullPointerException e) {
				//there could be the case that the network does not have any roads
				//but that does not really matter
				e.printStackTrace();
			}
		}
		
		//TODO for testing, remove later
		if(position != -1) {
			System.out.println("\nLongest Track:\n");
			Network longestTrack = networks.get(position);
			for(Field f : longestTrack.getLongestTrack()) {
				System.out.println(f);
			}
			System.out.println("\n----------\n");
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
			Field startingPoint = findStartingPoint(board, networks, true);
		
			//if the result is null, then there are no more networks to be found
			if(startingPoint == null) {
				fieldsLeft = false;
				continue;
			}
			
			//if it is not null, create a new network and add it to the result
			Network network = new Network();
			networks.add(network);
			
			//TODO for testing, remove later
			System.out.println("Errors:\n");
			
			//a variable used to check whether we are going for roads or rails (only interesting for overpasses)
			boolean road = true;
			
			//get the element of the starting point and check whether it is an overpass
			RouteElement element = startingPoint.getElement();
			if(element.getType() == ElementTypes.OVERPASS) {
				//if it is an overpass, check whether the roads have been used or the rails
				if(!element.isRoadsUsed()) {
					road = true;
				} 
				if(!element.isRailsUsed()) {
					road = false;
				}
			}
			
			//then, grow it beginning from the starting point
			//if we start the network from here, it does not matter whether we came from a road or a rail
			growNetwork(board, network, startingPoint, road);
	
			//TODO remove later
			System.out.println("\n----------\n");
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
	
	private Field findStartingPoint(Board board, ArrayList<Network> networks, boolean growingNetworks) {
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
			
			//if it is part of another network, just go on with the next field, except if it is an overpass
			if(isPartOfAnotherNetwork) {
				//check whether the element is an overpass
				RouteElement element = f.getElement();
				//the special treatment should only be done if we're growing networks, as this behaviour might be bad for 
				//finding the longest road and the longest track
				if(element.getType() == ElementTypes.OVERPASS && growingNetworks) {
					//if it is an overpass, check whether both the rail and the road connections have already been used
					if(!element.isRailsUsed()) {
						//if the rails have not been used, return this field as the starting point
						return f;
					} 
					if(!element.isRoadsUsed()) {
						//if the roads have not been used, return this field as the starting point
						return f;
					}
					//if they have both been used, the overpass is alredy part of two networks and can be discarded 
					continue;
				} else {
					//if it is not an overpass, just go on with the next field
					continue;
				}
			}
			
			//if it is not part of another network, just return the field as the result
			startingPoint = f;
			return startingPoint;
		}

		
		//return the result (if we end up here it is null)
		return startingPoint;
	}
	
	public void growNetwork(Board board, Network network, Field field, boolean road) {
		//get the element
		RouteElement element = field.getElement();
		
		//check whether it is an overpass
		boolean overpass = false;
		if(element.getType() == ElementTypes.OVERPASS) {
			overpass = true;
		}
		
		//check whether the field is already part of the network
		if(containsField(network.getFields(), field)) {
			//in this case, the field is already part of the network, therefore return
			return;
		}
		
		//if not, add the field to the network
		network.getFields().add(field);
		
		//get the matching road and rail connections of the field
		Directions[] roadConnections = field.getElement().getRoadConnections();
		Directions[] railConnections = field.getElement().getRailConnections();
		
		//now we have to do some special checking in case we're looking at an overpass
		//because you can only choose either the road or the rail connections of an overpass
		if(overpass) {
			//in this case, we have to consider whether we were on a road or on a rail before
			if(road) {
				//if we were on a road before, get rid of the rail connections
				railConnections = null;
				//set the corresponding attribute in the element
				element.setRoadsUsed();
			} else {
				//if we were on a track before, get rid of all the road connections
				roadConnections = null;
				//set the corresponding attribute in the element
				element.setRailsUsed();
			}
		}
		
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
				growNetwork(board, network, neighbour, true);
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
				growNetwork(board, network, neighbour, false);
				
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

		//now, we're gonna find and store every field in the network that does not have a road on it
		ArrayList<Field> fieldsWithoutRoads = new ArrayList<Field>();
		for(Field f: fieldsCopy) {
			switch(f.getElement().getType()) {
			case ROAD:
			case ROAD_TURN:
			case ROAD_TJUNCTION:
			case ROAD_CROSSROAD:
			case OVERPASS:
			case STATION:
			case STATION_TURN:
			case STATION_1:
			case STATION_2:
			case STATION_3:
			case STATION_4:
				//in all of the above cases, don't do anything, since they do contain roads
				break;
			case RAIL:
			case RAIL_TURN:
			case RAIL_TJUNCTION:
			case RAIL_CROSSROAD:
				//in all of the above cases, add the element to the newly defined list
				fieldsWithoutRoads.add(f);
				break;
			default:
				//undefined behaviour, just don't do anything
			}
		}
		
		//now, remove every element that does not have a road in it from the list containing the fields
		if(!fieldsWithoutRoads.isEmpty()) {
			//of course you can only do that if there are fields that do not contain roads
			for(Field f : fieldsWithoutRoads) {
				fieldsWithoutRoads.indexOf(f);
				boolean removed = fieldsCopy.remove(f);
				//if the element has not been removed, at least inform about this, as it is very weird behaviour
				if(!removed) {
					System.out.println(f + "could not be removed from the list of fields");
				}
			}
		}
		
		//it is possible that the list of fields we're examining is now empty
		//in this case, return, because there are no roads in this network
		if(fieldsCopy.isEmpty()) {
			return;
		}
		
		//create a copy of the board that only contains the fields with roads
		userManagement.User user = new userManagement.User("resultCalculationUser", "resultCalculationUserPassword");
		Board roadsOnlyBoard = new Board(user);
		
		//fill the board
		for(Field f : fieldsCopy) {
			int position = f.getPosition();
			Field currField = roadsOnlyBoard.getFields().get(position);
			RouteElement currElement = new RouteElement(f.getElement().getOrientation(), f.getElement().getType(), f.getElement().isMirrored());
			try {
				currField.addElement(currElement);
			} catch (IllegalPlayerMoveException e) {
				//shouldn't happen, so don't do anything
				e.printStackTrace();
			}
		}
		
		//get the every road on the board
		ArrayList<Network> roads = findRoads(roadsOnlyBoard);
		
		//check if there are any
		if(roads == null || roads.isEmpty()) {
			//return, because we can't do anything in this case
			return;
		}
		
		//go through each road and find the longest one
		int longestRoad = 0;
		int longestRoadPosition = -1;
		for(Network road : roads) {
			if(road.getFields().size() > longestRoad) {
				longestRoad = road.getFields().size();
				longestRoadPosition = roads.indexOf(road);
			}
		}
		
		//check if we have found anything productive
		if(longestRoadPosition == -1) {
			return;
		}
		
		//finally, set the longest road of the network
		Network longestRoadNetwork = roads.get(longestRoadPosition);
		network.setLongestRoad(longestRoadNetwork.getFields());
		
	}
	
	private ArrayList<Network> findRoads(Board board) {
		//the result is an ArrayList of networks
		ArrayList<Network> networks = new ArrayList<Network>();
		
		//we have to keep looking until there are no more fields that don't belong to any network
		boolean fieldsLeft = true;
		
		while(fieldsLeft) {
		
			//find a starting point
			Field startingPoint = findStartingPoint(board, networks, false);
		
			//if the result is null, then there are no more networks to be found
			if(startingPoint == null) {
				fieldsLeft = false;
				continue;
			}
			
			//create a new network
			Network network = new Network();
			
			//then, find every road that starts from this point
			//this already influences the result
			startRoad(board, network, startingPoint, networks);
			
		}

		
		//return the result
		return networks;
	}
	
	public void startRoad(Board board, Network network, Field field, ArrayList<Network> result) {
		//get the matching road connections of the field
		Directions[] roadConnections = field.getElement().getRoadConnections();
		
		//check whether there are any road connections
		if(roadConnections == null || roadConnections.length <= 0) {
			//if not, you cannot start a road from here
			return;
		}
		
		//go through each connection
		for(int i = 0; i < roadConnections.length; i++) {
			//copy the fields of the network that was given
			Network copyOfNetwork = new Network();
			for(Field f : network.getFields()) {
				Field copyField = new Field(f.getPosition(), f.getElement());
				copyOfNetwork.getFields().add(copyField);
			}
			
			//add it to the result
			result.add(copyOfNetwork);
			
			//now, grow the road from here
			growRoad(board, copyOfNetwork, field, roadConnections[i], result);
		}
	}
	
	public void growRoad(Board board, Network network, Field field, Directions direction, ArrayList<Network> result) {
		//get the matching road connections of the field
		Directions[] roadConnections = field.getElement().getRoadConnections();
		
		//check whether there are any road connections
		if(roadConnections == null || roadConnections.length <= 0) {
			//if not, you cannot go any further into this direction
			return;
		}
		
		//now if we haven't been given a direction, we have to check whether this field has more than one road connection
		if(direction == null) {
			if(roadConnections.length == 1) {
				//in this case, we can simply set the direction to the only one this element has
				direction = roadConnections[0];
			} else {
				//in this case, there is more than one direction and we have to start anew
				startRoad(board, network, field, result);
				//and, very importantly, we have to stop this method
				return;
			}
		}
		
		//check whether the field is already part of the network
		if(containsField(network.getFields(), field)) {
			//in this case, the field is already part of the network, therefore return
			return;
		}
		
		//if not, add the field to the network
		network.getFields().add(field);
		
		//depending on the direction, we have to get the neighbouring field
		Field neighbour = null;
		switch(direction) {
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
			return;
		}
				
		//if the neighbour is empty, then the network obviously cannot grow any further into this direction
		if(neighbour.isEmpty()) {
			//therefore, return
			return;
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
					if(direction == Directions.SOUTH) {
						matchingConnection = true;
					}
					break;
				case EAST:
					if(direction == Directions.WEST) {
						matchingConnection = true;
					}
					break;
				case SOUTH:
					if(direction == Directions.NORTH) {
						matchingConnection = true;
					}
					break;
				case WEST:
					if(direction == Directions.EAST) {
						matchingConnection = true;
					}
					break;
				default:
					//undefined behaviour, just do nothing and hope it helps
				}
			}
				
			//if there is no matching connection, the network can obviously not grow any further
			if(!matchingConnection) {
				//therefore, return
				return;
			}
				
			//if the neighbour has a matching connection, check whether it is one of the exits
			if(neighbour.getPosition() < 0) {
				//obviously, the network cannot grow any further from this
				return;
			}
				
			//in all other cases, the network can grow further from here
			growRoad(board, network, neighbour, null, result);
		}
	}
	
	public void findLongestTrack(Network network) {
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

		//now, we're gonna find and store every field in the network that does not have a rails on it
		ArrayList<Field> fieldsWithoutRails = new ArrayList<Field>();
		for(Field f: fieldsCopy) {
			switch(f.getElement().getType()) {
			case ROAD:
			case ROAD_TURN:
			case ROAD_TJUNCTION:
			case ROAD_CROSSROAD:
			case OVERPASS:
				//in all of the above cases, add the field to the newly defined list
				fieldsWithoutRails.add(f);
				break;
			case STATION:
			case STATION_TURN:
			case STATION_1:
			case STATION_2:
			case STATION_3:
			case STATION_4:
			case RAIL:
			case RAIL_TURN:
			case RAIL_TJUNCTION:
			case RAIL_CROSSROAD:
				//in all of the above cases, don't do anything, since the elements do contain rails
				break;
			default:
				//undefined behaviour, just don't do anything
			}
		}
		
		//now, remove every element that does not have rails in it from the list containing the fields
		if(!fieldsWithoutRails.isEmpty()) {
			//of course you can only do that if there are fields that do not contain rails
			for(Field f : fieldsWithoutRails) {
				fieldsWithoutRails.indexOf(f);
				boolean removed = fieldsCopy.remove(f);
				//if the element has not been removed, at least inform about this, as it is very weird behaviour
				if(!removed) {
					System.out.println(f + "could not be removed from the list of fields");
				}
			}
		}
		
		//it is possible that the list of fields we're examining is now empty
		//in this case, return, because there are no rails in this network
		if(fieldsCopy.isEmpty()) {
			return;
		}
		
		//create a copy of the board that only contains the fields with rails
		userManagement.User user = new userManagement.User("resultCalculationUser", "resultCalculationUserPassword");
		Board railsOnlyBoard = new Board(user);
		
		//fill the board
		for(Field f : fieldsCopy) {
			int position = f.getPosition();
			Field currField = railsOnlyBoard.getFields().get(position);
			RouteElement currElement = new RouteElement(f.getElement().getOrientation(), f.getElement().getType(), f.getElement().isMirrored());
			try {
				currField.addElement(currElement);
			} catch (IllegalPlayerMoveException e) {
				//shouldn't happen, so don't do anything
				e.printStackTrace();
			}
		}
		
		//get the every road on the board
		ArrayList<Network> rails = findRails(railsOnlyBoard);
		
		//check if there are any
		if(rails == null || rails.isEmpty()) {
			//return, because we can't do anything in this case
			return;
		}
		
		//go through each rail and find the longest one
		int longestRailLength = 0;
		int longestRailPosition = -1;
		for(Network rail : rails) {
			if(rail.getFields().size() > longestRailLength) {
				longestRailLength = rail.getFields().size();
				longestRailPosition = rails.indexOf(rail);
			}
		}
		
		//check if we have found anything productive
		if(longestRailPosition == -1) {
			return;
		}
		
		//finally, set the longest road of the network
		Network longestRail = rails.get(longestRailPosition);
		network.setLongestTrack(longestRail.getFields());
		
	}
	
	private ArrayList<Network> findRails(Board board) {
		//the result is an ArrayList of networks
		ArrayList<Network> networks = new ArrayList<Network>();
		
		//we have to keep looking until there are no more fields that don't belong to any network
		boolean fieldsLeft = true;
		
		while(fieldsLeft) {
		
			//find a starting point
			Field startingPoint = findStartingPoint(board, networks, false);
		
			//if the result is null, then there are no more networks to be found
			if(startingPoint == null) {
				fieldsLeft = false;
				continue;
			}
			
			//create a new network
			Network network = new Network();
			
			//then, find every track that starts from this point
			//this already influences the result
			startRail(board, network, startingPoint, networks);
			
		}

		
		//return the result
		return networks;
	}
	
	public void startRail(Board board, Network network, Field field, ArrayList<Network> result) {
		//get the matching rail connections of the field
		Directions[] railConnections = field.getElement().getRailConnections();
		
		//check whether there are any road connections
		if(railConnections == null || railConnections.length <= 0) {
			//if not, you cannot start a road from here
			return;
		}
		
		//go through each connection
		for(int i = 0; i < railConnections.length; i++) {
			//copy the fields of the network that was given
			Network copyOfNetwork = new Network();
			for(Field f : network.getFields()) {
				Field copyField = new Field(f.getPosition(), f.getElement());
				copyOfNetwork.getFields().add(copyField);
			}
			
			//add it to the result
			result.add(copyOfNetwork);
			
			//now, grow the road from here
			growRail(board, copyOfNetwork, field, railConnections[i], result);
		}
	}
	
	public void growRail(Board board, Network network, Field field, Directions direction, ArrayList<Network> result) {
		//get the matching rail connections of the field
		Directions[] railConnections = field.getElement().getRailConnections();
		
		//check whether there are any rail connections
		if(railConnections == null || railConnections.length <= 0) {
			//if not, you cannot go any further into this direction
			return;
		}
		
		//now if we haven't been given a direction, we have to check whether this field has more than one rail connection
		if(direction == null) {
			if(railConnections.length == 1) {
				//in this case, we can simply set the direction to the only one this element has
				direction = railConnections[0];
			} else {
				//in this case, there is more than one direction and we have to start anew
				startRail(board, network, field, result);
				//and, very importantly, we have to stop this method
				return;
			}
		}
		
		//check whether the field is already part of the network
		if(containsField(network.getFields(), field)) {
			//in this case, the field is already part of the network, therefore return
			return;
		}
		
		//if not, add the field to the network
		network.getFields().add(field);
		
		//depending on the direction, we have to get the neighbouring field
		Field neighbour = null;
		switch(direction) {
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
			return;
		}
				
		//if the neighbour is empty, then the network obviously cannot grow any further into this direction
		if(neighbour.isEmpty()) {
			//therefore, return
			return;
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
					if(direction == Directions.SOUTH) {
						matchingConnection = true;
					}
					break;
				case EAST:
					if(direction == Directions.WEST) {
						matchingConnection = true;
					}
					break;
				case SOUTH:
					if(direction == Directions.NORTH) {
						matchingConnection = true;
					}
					break;
				case WEST:
					if(direction == Directions.EAST) {
						matchingConnection = true;
					}
					break;
				default:
					//undefined behaviour, just do nothing and hope it helps
				}
			}
				
			//if there is no matching connection, the network can obviously not grow any further
			if(!matchingConnection) {
				//therefore, return
				return;
			}
				
			//if the neighbour has a matching connection, check whether it is one of the exits
			if(neighbour.getPosition() < 0) {
				//obviously, the network cannot grow any further from this
				return;
			}
				
			//in all other cases, the network can grow further from here
			growRail(board, network, neighbour, null, result);
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
		System.out.println(result + "\n\n--------------------\n");
		
		//second board
		userManagement.User user2 = new userManagement.User("hey", "shamona");
		Board board2 = new Board(user2);
		
		//fill the second board
		Field three2 = board2.getFields().get(3);
		three2.addElement(new RouteElement(Orientations.NINETY_DEGREES, ElementTypes.RAIL, false));
		
		Field ten2 = board2.getFields().get(10);
		ten2.addElement(new RouteElement(Orientations.NINETY_DEGREES, ElementTypes.RAIL, false));
		
		Field seventeen2 = board2.getFields().get(17);
		seventeen2.addElement(new RouteElement(Orientations.NINETY_DEGREES, ElementTypes.RAIL, false));
		
		Field twentyfour2 = board2.getFields().get(24);
		twentyfour2.addElement(new RouteElement(Orientations.NINETY_DEGREES, ElementTypes.RAIL, false));
		
		Field thirtyone2 = board2.getFields().get(31);
		thirtyone2.addElement(new RouteElement(Orientations.NINETY_DEGREES, ElementTypes.RAIL, false));
		
		Field thirtyeight2 = board2.getFields().get(38);
		thirtyeight2.addElement(new RouteElement(Orientations.NINETY_DEGREES, ElementTypes.RAIL, false));
		
		Field fourtyfive2 = board2.getFields().get(45);
		fourtyfive2.addElement(new RouteElement(Orientations.NINETY_DEGREES, ElementTypes.RAIL, false));
		
		int result2 = calc.calculateResult(board2);
		System.out.println(result2  + "\n\n--------------------\n");
		
		//testing the methods designed for the AI
		RouteElement[] remainingElements = new RouteElement[3];
		RouteElement straightRoad = new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.ROAD, false);
		RouteElement stationTurn = new RouteElement(Orientations.NINETY_DEGREES, ElementTypes.STATION_TURN, true);
		RouteElement railTJunction = new RouteElement(Orientations.TWOHUNDREDSEVENTY_DEGREES, ElementTypes.RAIL_TJUNCTION, false);
		remainingElements[0] = straightRoad;
		remainingElements[1] = stationTurn;
		remainingElements[2] = railTJunction;
		RouteElement[] remainingElements2 = new RouteElement[1];
		RouteElement element = new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.RAIL_CROSSROAD, false);
		remainingElements2[0] = element;
		System.out.println("\nBoard 1:\n");
		System.out.println("Moves Left?");
		System.out.println(board.movesLeft(remainingElements2) + "\n");
		System.out.println("Proposed move:");
		System.out.println(board.proposeMove(remainingElements2) + "\n");
		System.out.println("\nBoard 2:\n");
		System.out.println("Moves Left?");
		System.out.println(board2.movesLeft(remainingElements) + "\n");
		System.out.println("Proposed move:");
		System.out.println(board2.proposeMove(remainingElements) + "\n");
		
		System.out.println(board.canElementBePlaced(board.getFields().get(34), element));
	}
	*/
}
