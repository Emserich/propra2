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

	/* -- METHODS -- */
	
	@Override
	public String toString() {
		String description = "Network:\n";
		description += "\nFields:\n";
		for(Field f : fields) {
			description += f + "\n";
		}
		description += "\nExits:\n";
		for(Exits e : exits) {
			description += e + "\n";
		}
		description += "\nLength of the longest road: " + longestRoad.size(); 
		description += "\nLength 0f the longest track: " + longestTrack.size();
		description += "\nNumber of errors: " + errors + "\n";
		description += "\n------------";
		return description;
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
	
	public void addError() {
		this.errors++;
	}

	public void setLongestRoad(ArrayList<Field> longestRoad) {
		this.longestRoad = longestRoad;
	}

	public void setLongestTrack(ArrayList<Field> longestTrack) {
		this.longestTrack = longestTrack;
	}
}
