package games.RailroadInk;

import java.util.ArrayList;

public class ResultCalculator {
	
	public int calculateResult(Board board) {
		//the result is a score
		int score = 0;
		
		//find the networks
		//TODO
		ArrayList<Network> networks = null;
		
		//go through each network
		for(Network n : networks) {
			//get the error counter
			int errors = n.getErrors();
			//for every error, the player loses a point
			score -= errors;
		}
		
		//get the longest road 
		int longestRoad = longestRoad(networks);
		//for the every field in the longest road, the player gets a point
		score += longestRoad;
		
		//get the longest track
		int longestTrack = longestTrack(networks);
		//for every field in the longest track, the player gets a point
		score += longestTrack;
		
		//find out, how many of the central fields have been used
		ArrayList<Field> usedCentralFields = usedCentralFields(networks);
		//for every used central field, the player gets a point
		score += usedCentralFields.size();
		
		//return the score
		return score;
	}
	
	private int longestRoad(ArrayList<Network> networks) {
		//the result is an integer value
		int longestRoadLength = 0;
		
		//go through each network
		for(Network n : networks) {
			//get the length of the longest road
			try {
				int currentLength = n.getLongestRoad().size();
				//if it is longer than the current longest road, change the value
				if(longestRoadLength < currentLength) {
					longestRoadLength = currentLength;
				}
			} catch (NullPointerException e) {
				//there could be the case that the network does not have any roads
				//but that does not really matter
				e.printStackTrace();
			}
		}
		
		//return the result
		return longestRoadLength;
	}
	
	private int longestTrack(ArrayList<Network> networks) {
		//the result is an integer value
		int longestTrackLength = 0;
		
		//go through each network
		for(Network n : networks) {
			//get the length of the longest track
			try {
				int currentLength = n.getLongestTrack().size();
				//if it is longer than the current longest track, change the value
				if(longestTrackLength < currentLength) {
					longestTrackLength = currentLength;
				}
			} catch (NullPointerException e) {
				//there could be the case that the network does not have any roads
				//but that does not really matter
				e.printStackTrace();
			}
		}
		
		//return the result 
		return longestTrackLength;
	}
	
	private ArrayList<Field> usedCentralFields(ArrayList<Network> networks) {
		//the result is an ArrayList of Fields
		ArrayList<Field> result = new ArrayList<Field>();
		
		//go through each network
		for(Network n : networks) {
			//go through each field in the network
			for(Field f : n.getFields()) {
				//check whether it is one of the central fields
				if(!isCentralField(f)) {
					//if it is not, go on with the next one
					continue;
				}
				//if it is, check whether it is already in the result
				if(!containsField(result, f)) {
					//if it is, go on with the next one
					continue;
				}
				//if not, add it to the result
				result.add(f);
			}
		}
		
		//return the result
		return result;
	}
	
	private boolean containsField(ArrayList<Field> fields, Field f) {
		//the result is a boolean value
		boolean alreadyContainsField = false;
		
		//go through each field in the list
		for(Field currField : fields) {
			//compare the position of the current field with the one we're looking at
			//the position is relative to the whole board, so we can find out whether it's the same field
			if(f.getPosition() == currField.getPosition()) {
				//if they are equal, the field is already in the list
				alreadyContainsField = true;
			}
		}
		
		//return the result
		return alreadyContainsField;
	}
	
	private boolean isCentralField(Field f) {
		if(f.getPosition() == 16 || f.getPosition() == 17 || f.getPosition() == 18) {
			return true;
		}
		
		if(f.getPosition() == 23 || f.getPosition() == 24 || f.getPosition() == 25) {
			return true;
		}
		
		if(f.getPosition() == 30 || f.getPosition() == 31 || f.getPosition() == 32) {
			return true;
		}
		
		return false;
	}
}
