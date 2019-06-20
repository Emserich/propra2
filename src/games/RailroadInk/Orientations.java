package games.RailroadInk;

public enum Orientations {

	ZERO_DEGREES(0),
	NINETY_DEGREES(1),
	ONEHUNDREDEIGHTY_DEGREES(2),
	TWOHUNDREDSEVENTY_DEGREES(3);
	
	private int value;
	
	Orientations(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public String toString() {
		switch(value) {
		case 0:
			return "zero degrees";
		case 1:
			 return "nintey degrees";
		case 2:
			return "one hundred and eighty degrees";
		case 3:
			return "two hundred and seventy degrees";
		default:
			return "undefined orientation";
		}
	}
	
}
