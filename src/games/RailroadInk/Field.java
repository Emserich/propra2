package games.RailroadInk;

public class Field {

	/* -- ATTRIBUTES -- */
	
	private int position;
	private RouteElement element;
	private boolean isEmpty;
	
	/* -- CONSTRUCTORS --*/
	
	public Field(int position) {
		this.position = position;
		this.element = null;
		this.isEmpty = true;
	}

	public Field(int position, RouteElement element) {
		this.position = position;
		this.element = element;
		this.isEmpty = false;
	}
	
	/* -- METHODS -- */
	
	public void addElement(RouteElement element) throws IllegalPlayerMoveException {
		if(isEmpty) {
			this.element = element;
			this.isEmpty = false;
		} else {
			throw new IllegalPlayerMoveException("Das Feld ist bereits belegt.");
		}
	}
	
	@Override
	public String toString() {
		String description = "Position: " + this.position + ", Element: ";
		if(isEmpty()) {
			description += "none";
		} else {
			description += this.element.getType();
		}
		return description;
	}
	
	public boolean equals(Field otherField) {
		if(this.position == otherField.getPosition()) {
			return true;
		} else {
			return false;
		}
	}
	
	/* -- GETTERS -- */
	
	public int getPosition() {
		return position;
	}

	public RouteElement getElement() {
		return element;
	}

	public boolean isEmpty() {
		return isEmpty;
	}
	
}
