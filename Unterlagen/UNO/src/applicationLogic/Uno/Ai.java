package applicationLogic.Uno;

import java.util.Iterator;

import applicationLogic.Uno.Card.Color;
import games.Uno.Uno;

/**
 * @author Daniil pp013
 *
 */
public class Ai {
	
	public enum DiffLevel {
		EASY
	}
	
	private CardSet aiCards;
	private DiffLevel difficultyLevel;
	private String aiName;
	
	private Uno gameRef;
	
	private int score;
	

	/**
	 * @param aiName
	 * @param difficultyLevel
	 */
	public Ai(String aiName, DiffLevel difficultyLevel, Uno gameRef) {
		this.difficultyLevel = difficultyLevel;
		this.aiName = aiName;
		this.gameRef = gameRef;
	}

	public Card pickCard() {
		Card topCardOnPile = gameRef.getCardPile().getTopCard();
		CardSet validCards = new CardSet(aiCards);
		Card pickedCard = null;
		
		
		// filter out valid cards
		Iterator<Card> it = validCards.iterator();
		while(it.hasNext()) {
			Card card = it.next();
			
			if(!Rules.validMove(card, topCardOnPile)) {
				it.remove();
			}
		}
		
		// return first best
		
		if(validCards.size() == 0) {
			this.gameRef.drawCard(aiCards);
			
			Card newCard = aiCards.get(aiCards.size() - 1);
			
			if(!Rules.validMove(newCard, topCardOnPile)) {
				pickedCard = null; // skip playing card
			}
			else {
				pickedCard = newCard;
			}
		}
		else {
			pickedCard = validCards.get(0);
		}
		
		// set color if about to play wild cards
		if(pickedCard != null && (pickedCard.getType() == Card.Type.WILD || 
		   pickedCard.getType() == Card.Type.WILD_DRAW_FOUR)) {
			pickedCard.setColor(aiCards.pickBestColor());
		}
		
		return pickedCard;
	}

	/**
	 * @return the aiCards
	 */
	public CardSet getAiCards() {
		return aiCards;
	}

	/**
	 * @param aiCards the aiCards to set
	 */
	public void setAiCards(CardSet aiCards) {
		this.aiCards = aiCards;
	}

	/**
	 * @return the difficultyLevel
	 */
	public DiffLevel getDifficultyLevel() {
		return difficultyLevel;
	}

	/**
	 * @param difficultyLevel the difficultyLevel to set
	 */
	public void setDifficultyLevel(DiffLevel difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
	}

	/**
	 * @return the aiName
	 */
	public String getAiName() {
		return aiName;
	}

	/**
	 * @param aiName the aiName to set
	 */
	public void setAiName(String aiName) {
		this.aiName = aiName;
	}

	/**
	 * @return the gameRef
	 */
	public Uno getGameRef() {
		return gameRef;
	}
	
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
}
