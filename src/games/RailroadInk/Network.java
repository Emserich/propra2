package games.RailroadInk;

import java.util.ArrayList;

public class Network {

	/* -- ATTRIBUTES -- */
	
	private ArrayList<Field> fields;
	private ArrayList<Exits> exits;
	private ArrayList<Field> longestRoad;
	private ArrayList<Field> longestTrack;
	private int errors;
	
	/* -- CONSTRUCTOR -- */
	public Network() {
		//initiate the lists
		this.fields = new ArrayList<Field>();
		this.exits = new ArrayList<Exits>();
		this.longestRoad = new ArrayList<Field>();
		this.longestTrack = new ArrayList<Field>();
		
		//initiate the error counter
		this.errors = 0;
	}

	/* -- GETTERS AND SETTERS */
	
	public ArrayList<Field> getFields() {
		return fields;
	}

	public ArrayList<Exits> getExits() {
		return exits;
	}

	public ArrayList<Field> getLongestRoad() {
		return longestRoad;
	}

	public ArrayList<Field> getLongestTrack() {
		return longestTrack;
	}

	public int getErrors() {
		return errors;
	}

}
