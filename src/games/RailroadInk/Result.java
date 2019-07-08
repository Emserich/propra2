package games.RailroadInk;

import userManagement.User;

public class Result {

	private userManagement.User user;
	private int[] results;
	private int score;
	private String board;
	
	public Result(User user, int[] results, String board) {
		this.user = user;
		this.results = results;
		this.score = results[5];
		this.board = board;
	}

	public userManagement.User getUser() {
		return user;
	}

	public int[] getResults() {
		return results;
	}

	public int getScore() {
		return score;
	}

	public String getBoard() {
		return board;
	}
	
}
