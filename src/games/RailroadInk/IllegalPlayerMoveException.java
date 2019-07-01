package games.RailroadInk;

public class IllegalPlayerMoveException extends Exception {
	
	
	private static final long serialVersionUID = 8501698285395704754L;

	public IllegalPlayerMoveException(String description) {
		super(description);
	}
	
}
