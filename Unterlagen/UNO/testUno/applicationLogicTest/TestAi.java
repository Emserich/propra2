package applicationLogicTest;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import applicationLogic.Uno.Ai;
import applicationLogic.Uno.Card;
import applicationLogic.Uno.CardSet;
import games.Uno.Uno;
import userManagement.User;

public class TestAi {
	Ai testedAi;
	Uno gameMock;
	
	@Before
	public void setUp() throws Exception {
		gameMock = new Uno();
		
		User host = new User("host", "");
		User user2 = new User("user2", "");
		
		// initial setup
		this.gameMock.addUser(host);
		this.gameMock.addUser(user2);
		
		this.gameMock.execute(host, "RESTART");
		
		testedAi = new Ai("TestAI", Ai.DiffLevel.EASY, gameMock);
		testedAi.setAiCards(new CardSet());
	}

	@After
	public void tearDown() throws Exception {
		testedAi.getAiCards().clear();
		testedAi = null;
		
		gameMock.closeGame();
		gameMock = null;
	}

	@Test
	public void testPickCardSuccess() {
		// set up environment
		
		CardSet cardPile = new CardSet();
		
		// dirty backwards workaround
		gameMock.setCardPile(cardPile);
		
		cardPile.add(Card.createNormalCard(Card.Color.GREEN, 3));
		
		// these should not be picked by ai
		testedAi.getAiCards().add(Card.createNormalCard(Card.Color.BLUE, 0));
		testedAi.getAiCards().add(Card.createNormalCard(Card.Color.BLUE, 1));
		testedAi.getAiCards().add(Card.createNormalCard(Card.Color.BLUE, 2));
		
		// play green if available
		Card shouldBePlayed = Card.createNormalCard(Card.Color.GREEN, 0);
		testedAi.getAiCards().add(shouldBePlayed);
		assertEquals(shouldBePlayed, testedAi.pickCard());
		testedAi.getAiCards().remove(shouldBePlayed);
		
		// play same number if available
		shouldBePlayed = Card.createNormalCard(Card.Color.RED, 3);
		testedAi.getAiCards().add(shouldBePlayed);
		assertEquals(shouldBePlayed, testedAi.pickCard());
		testedAi.getAiCards().remove(shouldBePlayed);
		
		// play action if available
		shouldBePlayed = Card.createActionCard(Card.Color.GREEN, Card.Type.REVERSE);
		testedAi.getAiCards().add(shouldBePlayed);
		assertEquals(shouldBePlayed, testedAi.pickCard());
		testedAi.getAiCards().remove(shouldBePlayed);
		
		// play wild if available
		shouldBePlayed = Card.createWildCard(Card.Type.WILD);
		testedAi.getAiCards().add(shouldBePlayed);
		assertEquals(shouldBePlayed, testedAi.pickCard());
		testedAi.getAiCards().remove(shouldBePlayed);
	}
	
	@Test
	public void testPickCardNoPlayableCard() {
		// create unplayable situation
		testedAi.getAiCards().add(Card.createNormalCard(Card.Color.BLUE, 0));
		testedAi.getAiCards().add(Card.createNormalCard(Card.Color.BLUE, 1));
		testedAi.getAiCards().add(Card.createNormalCard(Card.Color.BLUE, 2));
		
		gameMock.getCardPile().putCardOnTop(Card.createNormalCard(Card.Color.RED, 3));
		
		// preset card to be drawn from cardPile for easier testing
		Card toBeDrawn = Card.createNormalCard(Card.Color.RED, 4);
		gameMock.getDrawDeck().putCardOnTop(toBeDrawn);
		
		// ai should draw if no card palyable and return it if that card is playable
		assertEquals(toBeDrawn, testedAi.pickCard());
	}
	
	@Test
	public void testPickCardNoPlayableCardDrawWild() {
		// create unplayable situation
		testedAi.getAiCards().add(Card.createNormalCard(Card.Color.BLUE, 0));
		testedAi.getAiCards().add(Card.createNormalCard(Card.Color.BLUE, 1));
		testedAi.getAiCards().add(Card.createNormalCard(Card.Color.BLUE, 2));
		
		gameMock.getCardPile().putCardOnTop(Card.createNormalCard(Card.Color.RED, 3));
		
		// preset card to be drawn from cardPile for easier testing
		Card toBeDrawn = Card.createWildCard(Card.Type.WILD);
		gameMock.getDrawDeck().putCardOnTop(toBeDrawn);
		
		// ai should draw if no card palyable and return it if that card is playable
		assertEquals(toBeDrawn, testedAi.pickCard());
	}
	
	@Test
	public void testPickCardNoPlayableCardCheat() {
		// create unplayable situation
		testedAi.getAiCards().add(Card.createNormalCard(Card.Color.BLUE, 0));
		testedAi.getAiCards().add(Card.createNormalCard(Card.Color.BLUE, 1));
		testedAi.getAiCards().add(Card.createNormalCard(Card.Color.BLUE, 2));
		
		gameMock.getCardPile().putCardOnTop(Card.createNormalCard(Card.Color.RED, 3));
		
		// preset card to be drawn from cardPile for easier testing
		Card toBeDrawn = Card.createNormalCard(Card.Color.YELLOW, 7);
		gameMock.getDrawDeck().putCardOnTop(toBeDrawn);
		
		// ai should skip turn by returning a null card
		Card nullCard = testedAi.pickCard();
		assertNull(nullCard);
	}
}
