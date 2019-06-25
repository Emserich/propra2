package applicationLogic.Uno;



import java.util.ArrayList;
import java.util.HashMap;

import applicationLogic.Uno.Card.Type;
import games.Uno.Uno;
import userManagement.User;

/**
 * @author Daniil pp013
 *
 */
public abstract class Rules {
	
	
	//prüfen ob Spielzug gültig ist
	public static boolean validMove(Card playerCard, Card topCardOnPile){

		if(playerCard.getType() == Card.Type.WILD || playerCard.getType() == Card.Type.WILD_DRAW_FOUR){
			return true;
		}
		// remember wild cards on topPile should match on color
		else if(topCardOnPile.getType() == Type.WILD || topCardOnPile.getType() == Type.WILD_DRAW_FOUR) {
			if(playerCard.getColor() == topCardOnPile.getColor()) return true;
			else return false;
		}else if (playerCard.getType() == Card.Type.REVERSE && (playerCard.getColor()==topCardOnPile.getColor() || playerCard.getType() == topCardOnPile.getType())){
			return true;
			
		}else if (playerCard.getType() == Card.Type.SKIP && (playerCard.getColor()==topCardOnPile.getColor() || playerCard.getType() == topCardOnPile.getType())){
			return true;
		}else if (playerCard.getType() == Card.Type.DRAW_TWO && (playerCard.getColor()==topCardOnPile.getColor() || playerCard.getType() == topCardOnPile.getType())){
			return true;
			
		}else if (playerCard.getColor() == topCardOnPile.getColor()){
			// color match
			return true;	
		
		// number match
		}else if (playerCard.getType()    == Card.Type.NORMAL &&
			topCardOnPile.getType() == Card.Type.NORMAL &&
			playerCard.getScore()   == topCardOnPile.getScore()){
			return true;
		
		
		//auf jede normale Karte darf eine Sonderkarte gelegt werden
//		if (playerCard.getType() != Card.Type.NORMAL && topCardOnPile.getType() == Card.Type.NORMAL){
//			return true;
		}
		
		return false;
	}
	
	/**
	 * Checks whether cardSet has a valid card to be played.
	 * @author Daniil pp013
	 * @param cardSet
	 * @param topCardOnPile
	 * @return
	 */
	public static boolean hasValidCard(CardSet cardSet, Card topCardOnPile) {
		for (Card card : cardSet) {
			if(Rules.validMove(card, topCardOnPile)) return true;
		}
		return false;
	}
	
	/**
	 * Checks whether a players Hand has a valid card to be played.
	 * @author Daniil pp013
	 * @param playerHand
	 * @param topCardOnPile
	 * @return
	 */
	public static boolean playerHasValidCard(CardSet playerHand, Card topCardOnPile) {
		return hasValidCard(playerHand, topCardOnPile);
	}
	
	/*
	//für KI - enthält alle Funktionen 
	public static boolean checkTurn(Card playerCard, Card topCardOnPile, CardSet playerHand) {
		boolean check = false;
		boolean valid = validMove(playerCard, topCardOnPile);
		
		if (valid == true){
			if(playerCard.getType() == Card.Type.WILD_DRAW_FOUR) {
				//funktion für wilddrawfour in uno schreiben
				check = true;
				
			}else if(playerCard.getType() == Card.Type.WILD){
				//Uno.chooseColore(playerCard);
				check = true;
				
			}else if(playerCard.getType() == Card.Type.DRAW_TWO){
				//Uno.drawTwo(turnCounter, topCardOnPile, playerCard, playerHand);
				check = true;
			
			}else if(playerCard.getType() == Card.Type.SKIP){
				//Uno.skipCardPlayed(playerCard, topCardOnPile);
				check = true;
				
			}else if (playerCard.getType() == Card.Type.REVERSE){
				//Uno.reverseCard(playerCard, topCardOnPile);
				check = true;
				
			}else if(playerCard.getType() == Card.Type.NORMAL){
				//funktion für normalen spielzug in uno schreiben
				check = true;
			}
			
		
		//...
		}
		else {
			//falls keine karte passt - karte vom stapel ziehen 
		//	CardSet.draw(1);
			check = false;
		}
		return check;
	}
	
	*/
	

	
	
	

	
	
	
	
}
