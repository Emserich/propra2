package games.RailroadInk;

public enum Directions {

	NORTH(0),
	EAST(1),
	SOUTH(2),
	WEST(3);
	
	private int value;
	
	Directions(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public String toString() {
		switch(value) {
		case 0:
			return "north";
		case 1:
			 return "east";
		case 2:
			return "south";
		case 3:
			return "west";
		default:
			return "undefined direction";
		}
	}
	
}
