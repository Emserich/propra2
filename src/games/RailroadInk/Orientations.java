package games.RailroadInk;

/**
 * This enumeration stores the possible orientations a {@link RouteElement} can have. If the degree is anything but {@link #ZERO_DEGREES}, the element is turned.
 * It should be noted that the rotating is done counter-clockwise.
 */
public enum Orientations {

	/**
	 * A value indicating that a route element is placed in its standard orientation.
	 */
	ZERO_DEGREES(0),
	
	/**
	 * A value indicating that a route element is rotated ninety degrees counter-clockwise from its standard orientation, i.e. {@link #ZERO_DEGREES}.
	 */
	NINETY_DEGREES(1),
	
	/**
	 * A value indicating that a route element is rotated one hundred and eighty degrees counter-clockwise from its standard orientation, i.e. {@link #ZERO_DEGREES}.
	 */
	ONEHUNDREDEIGHTY_DEGREES(2),
	
	/**
	 * A value indicating that a route element is rotated two hundred and seventy degrees counter-clockwise from its standard orientation, i.e. {@link #ZERO_DEGREES}.
	 */
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
			 return "ninety degrees";
		case 2:
			return "one hundred and eighty degrees";
		case 3:
			return "two hundred and seventy degrees";
		default:
			return "undefined orientation";
		}
	}
	
	public static Orientations addNinetyDegrees(Orientations oldOrien) {
		switch(oldOrien) {
		case ZERO_DEGREES:
			return NINETY_DEGREES;
		case NINETY_DEGREES:
			return ONEHUNDREDEIGHTY_DEGREES;
		case ONEHUNDREDEIGHTY_DEGREES:
			return TWOHUNDREDSEVENTY_DEGREES;
		case TWOHUNDREDSEVENTY_DEGREES:
			return ZERO_DEGREES;
		default:
			return null;
		}
	}
	
	public static Orientations addOnehundredeightyDegrees(Orientations oldOrien) {
		switch(oldOrien) {
		case ZERO_DEGREES:
			return ONEHUNDREDEIGHTY_DEGREES;
		case NINETY_DEGREES:
			return TWOHUNDREDSEVENTY_DEGREES;
		case ONEHUNDREDEIGHTY_DEGREES:
			return ZERO_DEGREES;
		case TWOHUNDREDSEVENTY_DEGREES:
			return NINETY_DEGREES;
		default:
			return null;
		}
	}
	
}
