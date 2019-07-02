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
	
	public static ElementTypes getTypeViaString(String type) {
		//the result is an element type
		ElementTypes result = null;
		
		//depending on the string, set the corresponding type
		switch(type) {
		case "straight road":
			result = ROAD;
			break;
		case "turning road":
			result = ROAD_TURN;
			break;
		case "road with a t-junction":
			result = ROAD_TJUNCTION;
			break;
		case "straight rail":
			result = RAIL;
			break;
		case "turning rail":
			result = RAIL_TURN;
			break;
		case "rail with a t-junction":
			result = RAIL_TJUNCTION;
			break;
		case "overpass":
			result = OVERPASS;
			break;
		case "station":
			result = STATION;
			break;
		case "station with a turn":
			result = STATION_TURN;
			break;
		case "road crossroad":
			result = ROAD_CROSSROAD;
			break;
		case "rail crossroad":
			result = RAIL_CROSSROAD;
			break;
		case "special station 1":
			result = STATION_1;
			break;
		case "special station 2":
			result = STATION_2;
			break;
		case "special station 3":
			result = STATION_3;
			break;
		case "special station 4":
			result = STATION_4;
			break;
		default:
			System.out.println(type);
			break;
		}
		
		//return the result
		return result;
	}
	
}
