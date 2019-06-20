package games.RailroadInk;

public class Field {

	private int position;
	private RouteElement element;
	private boolean isEmpty;
	
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
	
	public void addElement(RouteElement element) throws IllegalPlayerMoveException {
		if(isEmpty) {
			this.element = element;
			this.isEmpty = false;
		} else {
			throw new IllegalPlayerMoveException("Das Feld ist bereits belegt.");
		}
	}
	
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
