package applicationLogic.Uno;

import java.util.HashMap;

import applicationLogic.Uno.Card.Color;

/**
 * Contains all necessary information about a players passed turn. 
 * This should contain all the information to visualize the result in Client.
 * @author Daniil pp013
 *
 */
public class ClientData {
	/**
	 * Current cards in players hand.
	 */
	private CardSet currentPlayerCards;
	/**
	 * Card at the top of the pile.
	 */
	private Card topPileCard;
	/**
	 * A Mapping of player name to his card counts.
	 * No more information is needed?
	 */
	private HashMap<String, Integer> otherPlayerCardCounts;
	
	private Color currentColor;
	
	private int playDirection;
	
	private String playerTurnName;

	/**
	 * @param status
	 * @param currentPlayerCards
	 * @param topPileCard
	 * @param otherPlayerCardCounts
	 * @param currentColor
	 * @param playDirection
	 */
	public ClientData(String playerTurnName, CardSet currentPlayerCards, Card topPileCard,
			HashMap<String, Integer> otherPlayerCardCounts, Color currentColor, int playDirection) {
		this.playerTurnName = playerTurnName;
		this.currentPlayerCards = currentPlayerCards;
		this.topPileCard = topPileCard;
		this.otherPlayerCardCounts = otherPlayerCardCounts;
		this.currentColor = currentColor;
		this.playDirection = playDirection;
	}

	/**
	 * @return the currentPlayerCards
	 */
	public CardSet getCurrentCards() {
		return currentPlayerCards;
	}

	/**
	 * @param currentPlayerCards the currentPlayerCards to set
	 */
	public void setCurrentCards(CardSet currentPlayerCards) {
		this.currentPlayerCards = currentPlayerCards;
	}

	/**
	 * @return the topPileCard
	 */
	public Card getTopPileCard() {
		return topPileCard;
	}

	/**
	 * @param topPileCard the topPileCard to set
	 */
	public void setTopPileCard(Card topPileCard) {
		this.topPileCard = topPileCard;
	}

	/**
	 * @return the otherPlayerCardCounts
	 */
	public HashMap<String, Integer> getOtherPlayerCardCount() {
		return otherPlayerCardCounts;
	}

	/**
	 * @param otherPlayerCardCounts the otherPlayerCardCounts to set
	 */
	public void setOtherPlayerCardCount(HashMap<String, Integer> otherPlayerCardCounts) {
		this.otherPlayerCardCounts = otherPlayerCardCounts;
	}
}
