package applicationLogic.Uno;

import applicationLogic.Uno.Card.Color;

/**
 * Colors Text for log
 * @author Jan
 *
 */
/**
 * @author Jan
 *
 */
/**
 * @author Jan
 *
 */
public class TextColor {
	
	//The Color for the Cards
	private static String blue = "#1E90FF";
	private static String red = "red";
	private static String green = "green";
	private static String yellow = "yellow";
	
	public static enum typeCardColor{
		RED,
		BLUE,
		GREEN,
		YELLOW,
		WILD,
		WILD_DRAW_FOUR
	}
	public static enum typeTextColor{
		ERROR,
		DRAW,
		WON,
		POINT_TABLE,
		CALL,
		FAIL,
		MESSAGE

	}
	
	/**
	 * Colors Events
	 * @param color
	 * @return
	 */
	public static String getColor(typeTextColor color){
		
		if(color.equals(typeTextColor .DRAW)){
			return "<font color='#ffa54f'>";
		}
		if(color.equals(typeTextColor .ERROR)){
			return "<font color='#ff0000'>";
		}
		if(color.equals(typeTextColor .WON)){
			return "<font color='#FFD700'>";
		}
		if(color.equals(typeTextColor .POINT_TABLE)){
			return "<font color='#ff34b3'>";
		}
		if(color.equals(typeTextColor .CALL)){
			return "<font color='#4169e1'>";
		}
		if(color.equals(typeTextColor .FAIL)){
			return "<font color='#ff4500'>";
		}
		if(color.equals(typeTextColor .MESSAGE)){
			return "<font color='cyan'>";
		}
		return "";
		
		
	}
	
	/**
	 * end line HTML
	 * @return endline HTML
	 */
	public static String end(){
		return "</font>";
	}
	
	/**
	 * Colorrised Text in Color of the Card
	 * @param card
	 * @return Colorrised Text in Color of the Card
	 */
	public static String getColorForCardType(Card card){
		
		if(card.getType().equals(Card.Type.NORMAL) || card.getType().equals(Card.Type.REVERSE) || card.getType().equals(Card.Type.SKIP) || card.getType().equals(Card.Type.DRAW_TWO)){
			if(card.getColor().equals(Card.Color.RED)){
				return "<font color='"+red+"'> RED </font>";
				
			}
			if(card.getColor().equals(Card.Color.BLUE)){
				return "<font color='"+blue+"'> BLUE </font>";
				
			}
			if(card.getColor().equals(Card.Color.GREEN)){
				return "<font color='"+green+"'> GREEN </font>";
				
			}
			if(card.getColor().equals(Card.Color.YELLOW)){
				return "<font color='"+yellow+"'> YELLOW </font>";
				
			}
		}
		if(card.getType().equals(Card.Type.WILD)){
			return "<font color='"+red+"'>"+"W"+"</font>"+
					"<font color='"+green+"'>"+"I"+"</font>"+
					"<font color='"+blue+"'>"+"L"+"</font>"+
					"<font color='"+yellow+"'>"+"D"+"</font>";
		}
		if(card.getType().equals(Card.Type.WILD_DRAW_FOUR)){
			return "<font color='"+red+"'>"+"W"+"</font>"+
					"<font color='"+green+"'>"+"I"+"</font>"+
					"<font color='"+blue+"'>"+"L"+"</font>"+
					"<font color='"+yellow+"'>"+"D_"+"</font>"+
					"<font color='"+red+"'>"+"D"+"</font>"+
					"<font color='"+green+"'>"+"R"+"</font>"+
					"<font color='"+blue+"'>"+"A"+"</font>"+
					"<font color='"+yellow+"'>"+"W_"+"</font>"+
					"<font color='"+red+"'>"+"F"+"</font>"+
					"<font color='"+green+"'>"+"O"+"</font>"+
					"<font color='"+blue+"'>"+"U"+"</font>"+
					"<font color='"+yellow+"'>"+"R"+"</font>";
		}
	
		return "";
	}

	/**
	 * @param color
	 * @return color as text
	 */
	public static String getColorAsText(Color color) {
		if(color.equals(Card.Color.RED)){
			return "<font color='"+red+"'> RED </font>";
			
		}
		if(color.equals(Card.Color.BLUE)){
			return "<font color='"+blue+"'> BLUE </font>";
			
		}
		if(color.equals(Card.Color.GREEN)){
			return "<font color='"+green+"'> GREEN </font>";
			
		}
		if(color.equals(Card.Color.YELLOW)){
			return "<font color='"+yellow+"'> YELLOW </font>";
			
		}
		return null;
	}


}
