package games.RailroadInk;

public enum ElementTypes {

	ROAD(0),
	ROAD_TURN(1),
	ROAD_TJUNCTION(2),
	RAIL(3),
	RAIL_TURN(4),
	RAIL_TJUNCTION(5),
	OVERPASS(6),
	STATION(7),
	STATION_TURN(8),
	ROAD_CROSSROAD(9),
	RAIL_CROSSROAD(10),
	STATION_1(11),
	STATION_2(12),
	STATION_3(13),
	STATION_4(14);
	
	private int value;
	
	ElementTypes(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public String toString() {
		switch(value) {
		case 0:
			return "straight road";
		case 1:
			return "turning road";
		case 2:
			return "road with a t-junction";
		case 3:
			return "straight rail";
		case 4:
			return "turning rail";
		case 5:
			return "rail with a t-junction";
		case 6:
			return "overpass";
		case 7:
			return "station";
		case 8:
			return "station with a turn";
		case 9:
			return "road crossroad";
		case 10:
			return "rail crossroad";
		case 11:
			return "special station 1";
		case 12:
			return "special station 2";
		case 13:
			return "special station 3";
		case 14:
			return "special station 4";
		default:
			return "undefined element type";
		}
	}
	
}
