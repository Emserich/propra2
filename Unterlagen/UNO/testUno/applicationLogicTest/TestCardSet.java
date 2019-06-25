package applicationLogicTest;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;

import applicationLogic.Uno.Card;
import applicationLogic.Uno.CardSet;

/**
 * 
 */

/**
 * @author Daniil pp013
 *
 */
public class TestCardSet {

	CardSet testedCardSet;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		testedCardSet = new CardSet();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		testedCardSet = null;
	}
	
	/**
	 * Test method for {@link applicationLogic.Uno.CardSet#generateBaseDeck()}.
	 */
	@Test
	public void testGenerateBaseDeck() {
		// "every base deck should have 108 cards"
		testedCardSet.addAll(CardSet.generateBaseDeck());
		assertEquals(testedCardSet.size(), 108);
	}
	
	/**
	 * Test method for {@link applicationLogic.Uno.CardSet#draw(int)}.
	 */
	@Test
	public void testDraw() {
		testedCardSet.add(Card.createActionCard(Card.Color.GREEN, Card.Type.REVERSE));
		testedCardSet.add(Card.createActionCard(Card.Color.RED, Card.Type.SKIP));
		testedCardSet.add(Card.createActionCard(Card.Color.GREEN, Card.Type.DRAW_TWO));
		
		CardSet drawnCards = testedCardSet.draw(2);
		
		assertEquals(testedCardSet.size(), 1);
		assertEquals(drawnCards.size(), 2);
		
		assertTrue(testedCardSet.get(0).isColor(Card.Color.GREEN));
		assertTrue(testedCardSet.get(0).isType(Card.Type.DRAW_TWO));
		
		assertTrue(drawnCards.get(0).isColor(Card.Color.GREEN));
		assertTrue(drawnCards.get(0).isType(Card.Type.REVERSE));
		
		assertTrue(drawnCards.get(1).isColor(Card.Color.RED));
		assertTrue(drawnCards.get(1).isType(Card.Type.SKIP));
	}

	/**
	 * Test method for {@link applicationLogic.Uno.CardSet#containsColor(applicationLogic.Uno.Card.Color)}.
	 */
	@Test
	public void testContainsColor() {
		testedCardSet.add(Card.createActionCard(Card.Color.GREEN, Card.Type.REVERSE));
		assertTrue(testedCardSet.containsColor(Card.Color.GREEN));
		assertFalse(testedCardSet.containsColor(Card.Color.RED));
	}

	/**
	 * Test method for {@link applicationLogic.Uno.CardSet#containsType(applicationLogic.Uno.Card.Type)}.
	 */
	@Test
	public void testContainsType() {
		testedCardSet.add(Card.createActionCard(Card.Color.GREEN, Card.Type.REVERSE));
		assertTrue(testedCardSet.containsType(Card.Type.REVERSE));
		assertFalse(testedCardSet.containsType(Card.Type.SKIP));
	}

	/**
	 * Test method for {@link applicationLogic.Uno.CardSet#containsScore(int)}.
	 */
	@Test
	public void testContainsNumber() {
		testedCardSet.add(Card.createNormalCard(Card.Color.GREEN, 1));
		assertTrue(testedCardSet.containsScore(1));
		assertFalse(testedCardSet.containsScore(2));
	}

}
