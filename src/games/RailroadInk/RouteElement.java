package games.RailroadInk;

public class RouteElement {

	private Orientations orientation;
	private ElementTypes type;
	private boolean isMirrored;
	
	public RouteElement(Orientations orientation, ElementTypes type, boolean isMirrored) {
		this.orientation = orientation;
		this.type = type;
		this.isMirrored = isMirrored;
	}

	public Directions[] getRoadConnections() {
		
		switch(type) {
		case ROAD:
			//it does not matter if this element is mirrored
			if(orientation == Orientations.ZERO_DEGREES || orientation == Orientations.ONEHUNDREDEIGHTY_DEGREES) {
				Directions[] result = {Directions.EAST, Directions.WEST};
				return result;
			} else {
				Directions[] result = {Directions.NORTH, Directions.SOUTH};
				return result;
			}
		case ROAD_TURN:
			//TODO
		case ROAD_TJUNCTION:
			//TODO
		case RAIL:
			//this element does not have any outgoing road-connections
		case RAIL_TURN:
			//this element does not have any outgoing road-connections
		case RAIL_TJUNCTION:
			//this element does not have any outgoing road-connections
			return null;
		case OVERPASS:
			//TODO
		case STATION:
			//TODO
		case STATION_TURN:
			//TODO
		case ROAD_CROSSROAD:
			//neither orientation nor whether the element is mirrored matter in this case
			//it always has connections in every direction
			Directions[] result = {Directions.NORTH, Directions.EAST, Directions.SOUTH, Directions.WEST};
			return result;
		case RAIL_CROSSROAD:
			//this element does not have any outgoing road-connections
			return null;
		case STATION_1:
			//TODO
		case STATION_2:
			//TODO
		case STATION_3:
			//TODO
		case STATION_4:
			//TODO
		default:
			//this should not happen, but just return null in this case
			return null;
		}
		
	}
	
	public Directions[] getRailConnections() {
		
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
			if(orientation == Orientations.ZERO_DEGREES || orientation == Orientations.ONEHUNDREDEIGHTY_DEGREES) {
				Directions[] result = {Directions.EAST, Directions.WEST};
				return result;
			} else {
				Directions[] result = {Directions.NORTH, Directions.SOUTH};
				return result;
			}
		case RAIL_TURN:
			//TODO
		case RAIL_TJUNCTION:
			//TODO
		case OVERPASS:
			//TODO
		case STATION:
			//TODO
		case STATION_TURN:
			//TODO
		case ROAD_CROSSROAD:
			//this element does not have any outgoing rail-connections
			return null;
		case RAIL_CROSSROAD:
			//neither orientation nor whether the element is mirrored matter in this case
			//it always has connections in every direction
			Directions[] result = {Directions.NORTH, Directions.EAST, Directions.SOUTH, Directions.WEST};
			return result;
		case STATION_1:
			//TODO
		case STATION_2:
			//TODO
		case STATION_3:
			//TODO
		case STATION_4:
			//TODO
		default:
			//this should not happen, but just return null in this case
			return null;
		}
		
	}
	
	public Orientations getOrientation() {
		return orientation;
	}

	public ElementTypes getType() {
		return type;
	}

	public boolean isMirrored() {
		return isMirrored;
	}

}
