package games.RailroadInk;

public class RouteElement {

	private Orientations orientation;
	private ElementTypes type;
	private boolean isMirrored;

	//attributes necessary for overpass-handling
	private boolean roadsUsed;
	private boolean railsUsed;
	
	public RouteElement(Orientations orientation, ElementTypes type, boolean isMirrored) {
		this.orientation = orientation;
		this.type = type;
		this.isMirrored = isMirrored;
		
		//for overpass-handling
		this.roadsUsed = false;
		this.railsUsed = false;
	}

	public Directions[] getRoadConnections() {
		
		Directions[] result = null;
		
		switch(type) {
		case ROAD:
			//it does not matter if this element is mirrored
			
			//it always has two outgoing connections
			result = new Directions[2];
			
			if(orientation == Orientations.ZERO_DEGREES || orientation == Orientations.ONEHUNDREDEIGHTY_DEGREES) {
				result[0] = Directions.EAST;
				result[1] = Directions.WEST;
				return result;
			} else {
				result[0] = Directions.NORTH;
				result[1] = Directions.SOUTH;
				return result;
			}
		case ROAD_TURN:
			//it does not matter if this element is mirrored
			
			//in any case, this element has two outgoing connections
			result = new Directions[2];
			
			//the directions are dependent on the orientation
			switch(orientation) {
			case ZERO_DEGREES:
				result[0] = Directions.EAST;
				result[1] = Directions.SOUTH;
				break;
			case NINETY_DEGREES:
				result[0] = Directions.NORTH;
				result[1] = Directions.EAST;
				break;
			case ONEHUNDREDEIGHTY_DEGREES:
				result[0] = Directions.NORTH;
				result[1] = Directions.WEST;
				break;
			case TWOHUNDREDSEVENTY_DEGREES:
				result[0] = Directions.SOUTH;
				result[1] = Directions.WEST;
				break;
			default:
				//undefined behaviour, just return null in this case and pray nothing bad happens
				return null;
			}
			return result;
		case ROAD_TJUNCTION:
			//it does not matter if this element is mirrored
			
			//in any case, this element has three outgoing connections
			result = new Directions[3];
			
			switch(orientation) {
			case ZERO_DEGREES:
				result[0] = Directions.EAST;
				result[1] = Directions.SOUTH;
				result[2] = Directions.WEST;
				break;
			case NINETY_DEGREES:
				result[0] = Directions.NORTH;
				result[1] = Directions.EAST;
				result[2] = Directions.SOUTH;
				break;
			case ONEHUNDREDEIGHTY_DEGREES:
				result[0] = Directions.NORTH;
				result[1] = Directions.EAST;
				result[2] = Directions.WEST;
				break;
			case TWOHUNDREDSEVENTY_DEGREES:
				result[0] = Directions.NORTH;
				result[1] = Directions.SOUTH;
				result[2] = Directions.WEST;
				break;
			default:
				//undefined behaviour, just return null in this case and pray nothing bad happens
				return null;
			}
			return result;
		case RAIL:
			//this element does not have any outgoing road-connections
		case RAIL_TURN:
			//this element does not have any outgoing road-connections
		case RAIL_TJUNCTION:
			//this element does not have any outgoing road-connections
			return null;
		case OVERPASS:
			//it does not matter if this element is mirrored
			
			//it always has two outgoing connections
			result = new Directions[2];
			
			if(orientation == Orientations.ZERO_DEGREES || orientation == Orientations.ONEHUNDREDEIGHTY_DEGREES) {
				result[0] = Directions.EAST;
				result[1] = Directions.WEST;
				return result;
			} else {
				result[0] = Directions.NORTH;
				result[1] = Directions.SOUTH;
				return result;
			}
		case STATION:
			//it does not matter if this element is mirrored
			
			//in any case, this element has one outgoing connection
			result = new Directions[1];
			
			switch(orientation) {
			case ZERO_DEGREES:
				result[0] = Directions.EAST;
				break;
			case NINETY_DEGREES:
				result[0] = Directions.NORTH;
				break;
			case ONEHUNDREDEIGHTY_DEGREES:
				result[0] = Directions.WEST;
				break;
			case TWOHUNDREDSEVENTY_DEGREES:
				result[0] = Directions.SOUTH;
				break;
			default:
				//undefined behaviour, just return null in this case and pray nothing bad happens
				return null;
			}
			return result;
		case STATION_TURN:
			//the element always has one connection
			result = new Directions[1];
			
			//unfortunately, it is important whether the element is mirrored or not
			if(isMirrored()) {
				switch(orientation) {
				case ZERO_DEGREES:
					result[0] = Directions.NORTH;
					break;
				case NINETY_DEGREES:
					result[0] = Directions.WEST;
					break;
				case ONEHUNDREDEIGHTY_DEGREES:
					result[0] = Directions.SOUTH;
					break;
				case TWOHUNDREDSEVENTY_DEGREES:
					result[0] = Directions.EAST;
					break;
				default:
					//undefined behaviour, just return null in this case and pray nothing bad happens
					return null;
				}
			} else {
				switch(orientation) {
				case ZERO_DEGREES:
					result[0] = Directions.SOUTH;
					break;
				case NINETY_DEGREES:
					result[0] = Directions.EAST;
					break;
				case ONEHUNDREDEIGHTY_DEGREES:
					result[0] = Directions.NORTH;
					break;
				case TWOHUNDREDSEVENTY_DEGREES:
					result[0] = Directions.WEST;
					break;
				default:
					//undefined behaviour, just return null in this case and pray nothing bad happens
					return null;
				}
			}
			
			return result;
		case ROAD_CROSSROAD:
			//neither orientation nor whether the element is mirrored matter in this case
			//it always has connections in every direction
			result = new Directions[4];
			result[0] = Directions.NORTH;
			result[1] = Directions.EAST;
			result[2] = Directions.SOUTH;
			result[3] = Directions.WEST;
			return result;
		case RAIL_CROSSROAD:
			//this element does not have any outgoing road-connections
			return null;
		case STATION_1:
			//it does not matter if this element is mirrored
			
			//in any case, this element has three outgoing connections
			result = new Directions[3];
			
			switch(orientation) {
			case ZERO_DEGREES:
				result[0] = Directions.NORTH;
				result[1] = Directions.EAST;
				result[2] = Directions.WEST;
				break;
			case NINETY_DEGREES:
				result[0] = Directions.NORTH;
				result[1] = Directions.SOUTH;
				result[2] = Directions.WEST;
				break;
			case ONEHUNDREDEIGHTY_DEGREES:
				result[0] = Directions.EAST;
				result[1] = Directions.SOUTH;
				result[2] = Directions.WEST;
				break;
			case TWOHUNDREDSEVENTY_DEGREES:
				result[0] = Directions.NORTH;
				result[1] = Directions.EAST;
				result[2] = Directions.SOUTH;
				break;
			default:
				//undefined behaviour, just return null in this case and pray nothing bad happens
				return null;
			}
			return result;
		case STATION_2:
			//it does not matter if this element is mirrored
			
			//in any case, this element has one outgoing connection
			result = new Directions[1];
			
			switch(orientation) {
			case ZERO_DEGREES:
				result[0] = Directions.NORTH;
				break;
			case NINETY_DEGREES:
				result[0] = Directions.WEST;
				break;
			case ONEHUNDREDEIGHTY_DEGREES:
				result[0] = Directions.SOUTH;
				break;
			case TWOHUNDREDSEVENTY_DEGREES:
				result[0] = Directions.EAST;
				break;
			default:
				//undefined behaviour, just return null in this case and pray nothing bad happens
				return null;
			}
			return result;
		case STATION_3:
			//it does not matter if this element is mirrored
			
			//in any case, this element has two outgoing connections
			result = new Directions[2];
			
			//the directions are dependent on the orientation
			switch(orientation) {
			case ZERO_DEGREES:
				result[0] = Directions.NORTH;
				result[1] = Directions.WEST;
				break;
			case NINETY_DEGREES:
				result[0] = Directions.SOUTH;
				result[1] = Directions.WEST;
				break;
			case ONEHUNDREDEIGHTY_DEGREES:
				result[0] = Directions.EAST;
				result[1] = Directions.SOUTH;
				break;
			case TWOHUNDREDSEVENTY_DEGREES:
				result[0] = Directions.NORTH;
				result[1] = Directions.EAST;
				break;
			default:
				//undefined behaviour, just return null in this case and pray nothing bad happens
				return null;
			}
			return result;
		case STATION_4:
			//it does not matter if this element is mirrored
			
			//it always has two outgoing connections
			result = new Directions[2];
			
			if(orientation == Orientations.ZERO_DEGREES || orientation == Orientations.ONEHUNDREDEIGHTY_DEGREES) {
				result[0] = Directions.NORTH;
				result[1] = Directions.SOUTH;
				return result;
			} else {
				result[0] = Directions.EAST;
				result[1] = Directions.WEST;
				return result;
			}
		default:
			//this should not happen, but just return null in this case
			return null;
		}
		
	}
	
	public Directions[] getRailConnections() {
		
		Directions[] result = null;
		
		switch(type) {
		case ROAD:
			//this element does not have any outgoing rail-connections
		case ROAD_TURN:
			//this element does not have any outgoing rail-connections
		case ROAD_TJUNCTION:
			//this element does not have any outgoing rail-connections
			return null;
		case RAIL:
			//it does not matter if this element is mirrored
			
			//it always has two outgoing connections
			result = new Directions[2];
			
			if(orientation == Orientations.ZERO_DEGREES || orientation == Orientations.ONEHUNDREDEIGHTY_DEGREES) {
				result[0] = Directions.EAST;
				result[1] = Directions.WEST;
				return result;
			} else {
				result[0] = Directions.NORTH;
				result[1] = Directions.SOUTH;
				return result;
			}
		case RAIL_TURN:
			//it does not matter if this element is mirrored
			
			//in any case, this element has two outgoing connections
			result = new Directions[2];
			
			//the directions are dependent on the orientation
			switch(orientation) {
			case ZERO_DEGREES:
				result[0] = Directions.EAST;
				result[1] = Directions.SOUTH;
				break;
			case NINETY_DEGREES:
				result[0] = Directions.NORTH;
				result[1] = Directions.EAST;
				break;
			case ONEHUNDREDEIGHTY_DEGREES:
				result[0] = Directions.NORTH;
				result[1] = Directions.WEST;
				break;
			case TWOHUNDREDSEVENTY_DEGREES:
				result[0] = Directions.SOUTH;
				result[1] = Directions.WEST;
				break;
			default:
				//undefined behaviour, just return null in this case and pray nothing bad happens
				return null;
			}
			return result;
		case RAIL_TJUNCTION:
			//it does not matter if this element is mirrored
			
			//in any case, this element has three outgoing connections
			result = new Directions[3];
			
			switch(orientation) {
			case ZERO_DEGREES:
				result[0] = Directions.EAST;
				result[1] = Directions.SOUTH;
				result[2] = Directions.WEST;
				break;
			case NINETY_DEGREES:
				result[0] = Directions.NORTH;
				result[1] = Directions.EAST;
				result[2] = Directions.SOUTH;
				break;
			case ONEHUNDREDEIGHTY_DEGREES:
				result[0] = Directions.NORTH;
				result[1] = Directions.EAST;
				result[2] = Directions.WEST;
				break;
			case TWOHUNDREDSEVENTY_DEGREES:
				result[0] = Directions.NORTH;
				result[1] = Directions.SOUTH;
				result[2] = Directions.WEST;
				break;
			default:
				//undefined behaviour, just return null in this case and pray nothing bad happens
				return null;
			}
			return result;
		case OVERPASS:
			//it does not matter if this element is mirrored
			
			//it always has two outgoing connections
			result = new Directions[2];
			
			if(orientation == Orientations.ZERO_DEGREES || orientation == Orientations.ONEHUNDREDEIGHTY_DEGREES) {
				result[0] = Directions.NORTH;
				result[1] = Directions.SOUTH;
				return result;
			} else {
				result[0] = Directions.EAST;
				result[1] = Directions.WEST;
				return result;
			}
		case STATION:
			//it does not matter if this element is mirrored
			
			//in any case, this element has one outgoing connection
			result = new Directions[1];
			
			switch(orientation) {
			case ZERO_DEGREES:
				result[0] = Directions.WEST;
				break;
			case NINETY_DEGREES:
				result[0] = Directions.SOUTH;
				break;
			case ONEHUNDREDEIGHTY_DEGREES:
				result[0] = Directions.EAST;
				break;
			case TWOHUNDREDSEVENTY_DEGREES:
				result[0] = Directions.NORTH;
				break;
			default:
				//undefined behaviour, just return null in this case and pray nothing bad happens
				return null;
			}
			return result;
		case STATION_TURN:
			//the element always has one connection
			result = new Directions[1];
			
			//unfortunately, it is important whether the element is mirrored or not
			if(isMirrored()) {
				switch(orientation) {
				case ZERO_DEGREES:
					result[0] = Directions.WEST;
					break;
				case NINETY_DEGREES:
					result[0] = Directions.SOUTH;
					break;
				case ONEHUNDREDEIGHTY_DEGREES:
					result[0] = Directions.EAST;
					break;
				case TWOHUNDREDSEVENTY_DEGREES:
					result[0] = Directions.NORTH;
					break;
				default:
					//undefined behaviour, just return null in this case and pray nothing bad happens
					return null;
				}
			} else {
				switch(orientation) {
				case ZERO_DEGREES:
					result[0] = Directions.WEST;
					break;
				case NINETY_DEGREES:
					result[0] = Directions.SOUTH;
					break;
				case ONEHUNDREDEIGHTY_DEGREES:
					result[0] = Directions.EAST;
					break;
				case TWOHUNDREDSEVENTY_DEGREES:
					result[0] = Directions.NORTH;
					break;
				default:
					//undefined behaviour, just return null in this case and pray nothing bad happens
					return null;
				}
			}
			
			return result;
		case ROAD_CROSSROAD:
			//this element does not have any outgoing rail-connections
			return null;
		case RAIL_CROSSROAD:
			//neither orientation nor whether the element is mirrored matter in this case
			//it always has connections in every direction
			result = new Directions[4];
			result[0] = Directions.NORTH;
			result[1] = Directions.EAST;
			result[2] = Directions.SOUTH;
			result[3] = Directions.WEST;
			return result;
		case STATION_1:
			//it does not matter if this element is mirrored
			
			//in any case, this element has one outgoing connection
			result = new Directions[1];
			
			switch(orientation) {
			case ZERO_DEGREES:
				result[0] = Directions.SOUTH;
				break;
			case NINETY_DEGREES:
				result[0] = Directions.EAST;
				break;
			case ONEHUNDREDEIGHTY_DEGREES:
				result[0] = Directions.NORTH;
				break;
			case TWOHUNDREDSEVENTY_DEGREES:
				result[0] = Directions.WEST;
				break;
			default:
				//undefined behaviour, just return null in this case and pray nothing bad happens
				return null;
			}
			return result;
		case STATION_2:
			//it does not matter if this element is mirrored
			
			//in any case, this element has three outgoing connections
			result = new Directions[3];
			
			switch(orientation) {
			case ZERO_DEGREES:
				result[0] = Directions.EAST;
				result[1] = Directions.SOUTH;
				result[2] = Directions.WEST;
				break;
			case NINETY_DEGREES:
				result[0] = Directions.NORTH;
				result[1] = Directions.EAST;
				result[2] = Directions.SOUTH;
				break;
			case ONEHUNDREDEIGHTY_DEGREES:
				result[0] = Directions.NORTH;
				result[1] = Directions.EAST;
				result[2] = Directions.WEST;
				break;
			case TWOHUNDREDSEVENTY_DEGREES:
				result[0] = Directions.NORTH;
				result[1] = Directions.SOUTH;
				result[2] = Directions.WEST;
				break;
			default:
				//undefined behaviour, just return null in this case and pray nothing bad happens
				return null;
			}
			return result;
		case STATION_3:
			//it does not matter if this element is mirrored
			
			//in any case, this element has two outgoing connections
			result = new Directions[2];
			
			//the directions are dependent on the orientation
			switch(orientation) {
			case ZERO_DEGREES:
				result[0] = Directions.EAST;
				result[1] = Directions.SOUTH;
				break;
			case NINETY_DEGREES:
				result[0] = Directions.NORTH;
				result[1] = Directions.EAST;
				break;
			case ONEHUNDREDEIGHTY_DEGREES:
				result[0] = Directions.NORTH;
				result[1] = Directions.WEST;
				break;
			case TWOHUNDREDSEVENTY_DEGREES:
				result[0] = Directions.SOUTH;
				result[1] = Directions.WEST;
				break;
			default:
				//undefined behaviour, just return null in this case and pray nothing bad happens
				return null;
			}
			return result;
		case STATION_4:
			//it does not matter if this element is mirrored
			
			//it always has two outgoing connections
			result = new Directions[2];
			
			if(orientation == Orientations.ZERO_DEGREES || orientation == Orientations.ONEHUNDREDEIGHTY_DEGREES) {
				result[0] = Directions.EAST;
				result[1] = Directions.WEST;
				return result;
			} else {
				result[0] = Directions.NORTH;
				result[1] = Directions.SOUTH;
				return result;
			}
		default:
			//this should not happen, but just return null in this case
			return null;
		}
		
	}
	
	@Override 
	public String toString() {
		String description = "";
		description += getType() + ", " + getOrientation() + ", " + isMirrored();
		return description;
	}
	
	public Orientations getOrientation() {
		return orientation;
	}

	/**
	 * This method can be used to change the orientation of a route element. However, it should not be used in a standard setting. What should be done instead is
	 * to set the orientation directly when creating the object representing the element.
	 * This method was created in order to alter the orientation of a route element in order to try out every combination in the method {@link Board#movesLeft(RouteElement[])}.
	 * @param orientation The orientation to which this element's orientation should be changed.
	 */
	public void setOrientation(Orientations orientation) {
		this.orientation = orientation;
	}
	
	public ElementTypes getType() {
		return type;
	}

	public boolean isMirrored() {
		return isMirrored;
	}

	/**
	 * This method can be used to change whether a route element is mirrored. However, it should not be used in a standard setting. What should be done instead is
	 * to set the property directly when creating the object representing the element.
	 * This method was created in order to alter whether the route element is mirrored in order to try out every combination in the method {@link Board#movesLeft(RouteElement[])}.
	 * @param isMirrored A boolean value indicating whether the element is mirrored or not.
	 */
	public void setMirrored(boolean isMirrored) {
		this.isMirrored = isMirrored;
	}

	public boolean isRoadsUsed() {
		return roadsUsed;
	}

	public void setRoadsUsed() {
		this.roadsUsed = true;
	}

	public boolean isRailsUsed() {
		return railsUsed;
	}

	public void setRailsUsed() {
		this.railsUsed = true;
	}
	
}
