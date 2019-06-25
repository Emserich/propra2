package applicationLogic.Uno;

import java.util.UUID;

/**
 * Represents a card of given COLOR, TYPE and score.<br>
 * @author Daniil pp013
 *
 */
public class Card {
	
	/**
	 * A Unique id for referencing.
	 */
	private UUID id;
	/**
	 * Type represents the type of the UNO card.<br>
	 * -> NORMAL: Colored and Number.<br>
	 * -> SKIP, DRAW_TWO, REVERSE: Action Cards.<br>
	 * -> WILD, WILD_DRAW_TWO: Wild Cards.<br>
	 */
	private Type type;
	/**
	 * The Color of the card. Depends on Card Type.
	 */
	private Color color;
	/**
	 * Score of the card. Should be between 0 and 9.
	 * Score is automatically assigned to WILD and action cards.
	 */
	private int score;
	
	
	/**
	 * The possible colors of a card.
	 * @author Daniil pp013
	 *
	 */
	public static enum Color { 
		RED, BLUE, GREEN, YELLOW
	}
	
	/**
	 * The possible types of a card.
	 * -> NORMAL: Colored and Number.<br>
	 * -> SKIP, DRAW_TWO, REVERSE: Action Cards.<br>
	 * -> WILD, WILD_DRAW_TWO: Wild Cards.<br>
	 * @author Daniil pp013
	 *
	 */
	public static enum Type {
		/**
		 * A normal card with number.
		 * Colored card.
		 */
		NORMAL,
		/**
		 * Next player in sequence misses a turn.
		 * Colored action card.
		 */
		SKIP,
		/**
		 * Next player in sequence draws two cards and misses a turn.
		 * Colored action card.
		 */
		DRAW_TWO, 
		/**
		 * Order of play switches directions.
		 * Colored action card.
		 */
		REVERSE, 
		/**
		 * Player declares next color to be matched. 
		 * (May be used on any turn even if the player has matching color).
		 */
		WILD,
		/**
		 * Player declares next color to be matched; next player in sequence draws four cards and misses a turn.
		 * Is legally played only if the player has no cards of the current color.
		 */
		WILD_DRAW_FOUR 
	}

	private Card(Type type, Color color, int number) {
		this.id = UUID.randomUUID();
		this.type = type;
		this.color = color;
		this.score = number;
	}
	
	public Card( UUID id, Type type, Color color, int number){
		
		this.id=id;
		this.type=type;
		this.color=color;
		this.score=number;
		
	}
	
	/**
	 * @param color of the Card.
	 * @param score should be between 0 and 9. (number represented on the card) 
	 * @return a simple, colored Card.
	 */
	public static Card createNormalCard(Color color, int number) {
		if(color == null) {
			throw new IllegalArgumentException("Color can not be null.");
		}
		if(number < 0 || number > 9) {
			throw new IllegalArgumentException("Number must be between 0 and 9");
		}
		
		return new Card(Type.NORMAL, color, number);
	}
	
	/**
	 * @param color of the Card.
	 * @param type should only be SKIP, DRAW_TWO or REVERSE.
	 * @return An action Card.
	 */
	public static Card createActionCard(Color color, Type type) {
		if(color == null) {
			throw new IllegalArgumentException("Color can not be null.");
		}
		if(type == Type.NORMAL) {
			throw new IllegalArgumentException("Type should not be normal, use createNumberedCard for that.");
		}
		
		return new Card(type, color, 20);
	}
	
	/**
	 * @param type Should only be WILD or WILD_DRAW_FOUR
	 * @return a Wild Card.
	 */
	public static Card createWildCard(Type type) {
		if(type != Type.WILD && type != Type.WILD_DRAW_FOUR) {
			throw new IllegalArgumentException("Type should be either WILD or WILD_DRAW_FOUR");
		}
		
		return new Card(type, null, 50);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other){
		
		if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof Card))return false;
	    
	    Card otherCard = (Card)other;
		
		return this.id.equals(otherCard.id);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		String cardColor = "SPECIAL";
		if(color != null) cardColor = color.toString();
		
		String cardNumber = ".";
		if(score != -1) cardNumber = " and number: " + score;
		
		return cardColor + " Card of type: " + type + cardNumber;
	}
	
	/**
	 * @return the value of the card.
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * @param score
	 * @return whether score matches card score.
	 */
	public boolean hasScore(int score) {
		return this.score == score;
	}

	/**
	 * @return the id
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * @param color
	 * @return whether color matches card color.
	 */
	public boolean isColor(Color color) {
		return this.color == color;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}
	
	/**
	 * @param type
	 * @return whether type matches card type.
	 */
	public boolean isType(Type type) {
		return this.type == type;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	
}
