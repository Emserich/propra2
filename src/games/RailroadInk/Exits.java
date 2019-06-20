package games.RailroadInk;

public enum Exits {

	TOP_EXIT_LEFT(-1, new RouteElement(Orientations.NINETY_DEGREES, ElementTypes.ROAD, false)),
	TOP_EXIT_MIDDLE(-3, new RouteElement(Orientations.NINETY_DEGREES, ElementTypes.RAIL, false)),
	TOP_EXIT_RIGHT(-5, new RouteElement(Orientations.NINETY_DEGREES, ElementTypes.ROAD, false)),
	
	LEFT_EXIT_TOP(-7, new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.RAIL, false)),
	LEFT_EXIT_MIDDLE(-21, new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.ROAD, false)),
	LEFT_EXIT_BOTTOM(-35, new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.RAIL, false)),
	
	RIGHT_EXIT_TOP(-13, new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.RAIL, false)),
	RIGHT_EXIT_MIDDLE(-27, new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.ROAD, false)),
	RIGHT_EXIT_BOTTOM(-41, new RouteElement(Orientations.ZERO_DEGREES, ElementTypes.RAIL, false)),
	
	BOTTOM_EXIT_LEFT(-43, new RouteElement(Orientations.NINETY_DEGREES, ElementTypes.ROAD, false)),
	BOTTOM_EXIT_MIDDLE(-45, new RouteElement(Orientations.NINETY_DEGREES, ElementTypes.RAIL, false)),
	BOTTOM_EXIT_RIGHT(-47, new RouteElement(Orientations.NINETY_DEGREES, ElementTypes.ROAD, false));
	
	private int position;
	private RouteElement element;
	
	Exits(int position, RouteElement element) {
		this.position = position;
		this.element = element;
	}
	
	public Field getField() {
		Field f = new Field(position, element);
		return f;
	}
	
}
