package applicationLogic.Uno;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import applicationLogic.Uno.Card.Color;
import applicationLogic.Uno.Card.Type;

/**
 * Represents a simple set of cards.
 * Remember: You can use all ArrayList operators and more!
 * @author Daniil pp013
 *
 */
public class CardSet extends ArrayList<Card> {
	private static final long serialVersionUID = 5737507897616929343L;

	public CardSet(ArrayList<Card> cards) {
		super(cards);
	}
	
	public CardSet() {
		super();
	}
	
	/**
	 * @param color
	 * @return whether CardSet contains a card of given color.
	 */
	public boolean containsColor(Color color) {
		for(Card c : this) {
			if(c.isColor(color)) return true;
		}
		return false;
	}
	
	/**
	 * @param type
	 * @return whether CardSet contains a card of given type.
	 */
	public boolean containsType(Type type) {
		for(Card c : this) {
			if(c.isType(type)) return true;
		}
		return false;
	}
	
	/**
	 * @param score to be checked against.
	 * @return whether CardSet contains a card of given score.
	 */
	public boolean containsScore(int score) {
		for(Card c : this) {
			if(c.hasScore(score)) return true;
		}
		return false;
	}
	
	/**
	 * @param cardId to look for.
	 * @return the first card which has the same ID.
	 */
	public Card getCard(String cardId) {
		for(Card c : this) {
			if(c.getId().toString().equals(cardId)) return c;
		}
		return null;
	}
	public Color pickBestColor(){
		HashMap <Card.Color,Integer>color = new HashMap<Card.Color,Integer>(4);
		color.put(Card.Color.BLUE, 0);
		color.put(Card.Color.RED, 0);
		color.put(Card.Color.GREEN, 0);
		color.put(Card.Color.YELLOW, 0);
		for(Card c : this){
			if(c.getColor()!=null)
				color.put(c.getColor(), color.get(c.getColor())+1);
		}
		int temp=0;
		Card.Color result=Card.Color.RED;
		for (Color key : color.keySet()){
			if(color.get(key)>temp)
				result=key;
				temp=color.get(key);
		}
		return result;
	}
	
	public void putCardOnTop(Card card) {
		this.add(0, card);
	}
	
	public Card getTopCard() {
		return this.get(0);
	}
	
	/**
	 * Draws and returns one card from the top of the set. Drawn Card is removed.
	 * Similar to Stack.pop().
	 * @return null or first card of the top of the CardSet.
	 */
	public Card drawOne() {
		if(this.size() > 0) {
			Card res = this.get(0);
			this.remove(0);
			return res;
		}
		return null;
	}
	
	/**
	 * Draws cards from the CardSet.
	 * Cards that are returned are removed from the original CardSet.
	 * Similar to .pop() of Stack.
	 * @param amount of cards to be drawn.
	 * @return Cards that were drawn.
	 */
	public CardSet draw(int amount) {
		CardSet res = new CardSet();
		
		Iterator<Card> it = this.iterator();
		while(it.hasNext() && amount > 0) {
			Card actual = it.next();
			res.add(actual);
			it.remove();
			amount--;
		}
		
		return res;
	}
	
	/**
	 * @return the total Value of the Cards contained in this CardSet.
	 */
	public int getTotalCardValue() {
		int total = 0;
		for (Card c : this) total += c.getScore();
		return total;
	}
	
	/**
	 * Generates a base deck of 108 cards.
	 * @return a baseDeck for UNO.
	 */
	public static CardSet generateBaseDeck() {
		CardSet res = new CardSet();
		
		// normal/colored cards
		for(Color color : Card.Color.values()) {
			
			// only one "0" card per color
			res.add(Card.createNormalCard(color, 0));
			// two sets of...
			for(int j = 0; j < 2; j++) {
				
				// .. colored 1 to 9 cards
				for(int i = 1; i < 10; i++) {
					res.add(Card.createNormalCard(color, i));
				}
				
				// .. colored action cards
				res.add(Card.createActionCard(color, Card.Type.SKIP));
				res.add(Card.createActionCard(color, Card.Type.REVERSE));
				res.add(Card.createActionCard(color, Card.Type.DRAW_TWO));
			}
		}
		
		// special cards
		res.add(Card.createWildCard(Card.Type.WILD));
		res.add(Card.createWildCard(Card.Type.WILD));
		res.add(Card.createWildCard(Card.Type.WILD));
		res.add(Card.createWildCard(Card.Type.WILD));
		res.add(Card.createWildCard(Card.Type.WILD_DRAW_FOUR));
		res.add(Card.createWildCard(Card.Type.WILD_DRAW_FOUR));
		res.add(Card.createWildCard(Card.Type.WILD_DRAW_FOUR));
		res.add(Card.createWildCard(Card.Type.WILD_DRAW_FOUR));
		
		return res;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Card Set: \n");
		for(Card c : this) {
			sb.append(c.toString());
			sb.append("\n");
		}
		
		return sb.toString();
	}
}
