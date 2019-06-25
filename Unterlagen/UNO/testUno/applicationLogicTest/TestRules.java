package applicationLogicTest;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import applicationLogic.Uno.Card;
import applicationLogic.Uno.CardSet;
import applicationLogic.Uno.Rules;

public class TestRules {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testValidMoveColorMatch() {
		
		Card green1 = Card.createNormalCard(Card.Color.GREEN, 1);
		Card green2 = Card.createNormalCard(Card.Color.GREEN, 2);

		Card red3   = Card.createNormalCard(Card.Color.RED, 3);
		
		// color match
		assertTrue(Rules.validMove(green1, green2));
		assertFalse(Rules.validMove(green1, red3));
	}
	
	@Test
	public void testValidMoveNumberMatch() {
		Card green1 = Card.createNormalCard(Card.Color.GREEN, 1);
		
		Card red1   = Card.createNormalCard(Card.Color.RED, 1);
		
		// number match
		assertTrue(Rules.validMove(green1, red1));
	}
	
	@Test
	public void testValidMoveActionCard() {
		Card green1 = Card.createNormalCard(Card.Color.GREEN, 1);
		Card red1 = Card.createNormalCard(Card.Color.RED, 1);
		
		Card actionGreen = Card.createActionCard(Card.Color.GREEN, Card.Type.DRAW_TWO);
		Card actionRed = Card.createActionCard(Card.Color.RED, Card.Type.DRAW_TWO);
		Card actionRedREV = Card.createActionCard(Card.Color.RED, Card.Type.REVERSE);
		
		Card actionGreenSkip = Card.createActionCard(Card.Color.GREEN, Card.Type.SKIP);
		Card actionRedSkip = Card.createActionCard(Card.Color.RED, Card.Type.SKIP);
		
		// action card
		assertTrue(Rules.validMove(green1, actionGreen));
		assertFalse(Rules.validMove(green1, actionRed));
		
		// same action
		assertTrue(Rules.validMove(actionGreen, actionRed));
		
		// same color
		assertTrue(Rules.validMove(actionRed, actionRedREV));
		
		// not same action
		assertFalse(Rules.validMove(actionGreen, actionRedREV));
		
		assertTrue(Rules.validMove(actionGreenSkip, actionRedSkip));
		assertTrue(Rules.validMove(actionGreen,     actionGreenSkip));
		assertTrue(Rules.validMove(green1,          actionGreenSkip));
		
		assertFalse(Rules.validMove(red1,            actionGreenSkip));
		
		
	}
	
	@Test
	public void testValidMoveWildCard() {
		Card green1 = Card.createNormalCard(Card.Color.GREEN, 1);
		
		Card red1   = Card.createNormalCard(Card.Color.RED, 1);
		
		Card actionGreen = Card.createActionCard(Card.Color.GREEN, Card.Type.DRAW_TWO);
		Card actionRed   = Card.createActionCard(Card.Color.RED, Card.Type.DRAW_TWO);
		Card wild        = Card.createWildCard(Card.Type.WILD);
		
		// wild
		assertTrue(Rules.validMove(wild, green1));
		assertTrue(Rules.validMove(wild, red1));
		assertTrue(Rules.validMove(wild, actionGreen));
		assertTrue(Rules.validMove(wild, actionRed));
		assertTrue(Rules.validMove(wild, wild));
	}
	
	@Test
	public void testHasValidCard() {
		CardSet playersHand = new CardSet();
		Card green1 = Card.createNormalCard(Card.Color.GREEN, 1);
		Card green2 = Card.createNormalCard(Card.Color.GREEN, 2);
		
		Card red1   = Card.createNormalCard(Card.Color.RED, 1);
		Card red3   = Card.createNormalCard(Card.Color.RED, 3);
		
		Card actionGreen = Card.createActionCard(Card.Color.GREEN, Card.Type.DRAW_TWO);
		Card actionRed   = Card.createActionCard(Card.Color.RED, Card.Type.REVERSE);
		Card wild        = Card.createWildCard(Card.Type.WILD);
		
		playersHand.add(green1);
		playersHand.add(green2);
		assertFalse(Rules.hasValidCard(playersHand, red3));
		assertTrue(Rules.hasValidCard(playersHand, red1)); // match on (1)
		
		playersHand.add(actionRed);
		assertTrue(Rules.hasValidCard(playersHand, red3)); // match on red
		
		playersHand.add(wild);
		assertTrue(Rules.hasValidCard(playersHand, actionGreen)); // wild matches all
	}
	
}
