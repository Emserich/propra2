/**
 * 
 */
package applicationLogicTest;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import applicationLogic.Uno.Ai;
import applicationLogic.Uno.Card;
import applicationLogic.Uno.ClientData;
import applicationLogic.Uno.Card.Color;
import gameClasses.GameState;
import games.Uno.Uno;
import userManagement.User;

/**
 * @author Daniil pp013
 *
 */
public class TestUnoGame {
	
	Uno testedGame;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.testedGame = new Uno();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.testedGame.closeGame();
		
		this.testedGame = null;
	}

	/**
	 * Game should not start by itself.
	 * Users should be added correctly.
	 * Test method for {@link games.Uno.Uno#addUser(userManagement.User)}.
	 */
	@Test
	public void testAddUserUserGameSetup() {
		User user1 = new User("user1", "");
		User user2 = new User("user2", "");
		User user3 = new User("user3", "");
		User user4 = new User("user4", "");
		User user5 = new User("user5", "");
		
		this.testedGame.addUser(user1);
		
		// game should be in setup
		assertTrue(this.testedGame.getGameState() == GameState.SETUP);
		
		this.testedGame.addUser(user2);
		// game should still be in setup
		assertTrue(this.testedGame.getGameState() == GameState.SETUP);
		
		this.testedGame.addUser(user3);
		// game should still be in setup
		assertTrue(this.testedGame.getGameState() == GameState.SETUP);
		
		assertTrue(this.testedGame.isJoinable());
		this.testedGame.addUser(user4);
		// game should still be in setup
		assertTrue(this.testedGame.getGameState() == GameState.SETUP);
		
		// playerList should contain all players
		assertEquals(this.testedGame.getMaxPlayerAmount(), this.testedGame.getCurrentPlayerAmount());
		
		// fifth player should join as spectator
		
		this.testedGame.addUser(user5);
		assertFalse(this.testedGame.isJoinable());
		assertEquals(this.testedGame.getMaxPlayerAmount(), this.testedGame.getCurrentPlayerAmount());
	}
	
	@Test
	public void testIsJoinable() {
		User user1 = new User("user1", "");
		User user2 = new User("user2", "");
		User user3 = new User("user3", "");
		User user4 = new User("user4", "");
		User user5 = new User("user5", "");
		
		this.testedGame.addUser(user1);
		this.testedGame.addUser(user2);
		this.testedGame.addUser(user3);
		assertTrue(this.testedGame.isJoinable());
		this.testedGame.addUser(user4);
		assertFalse(this.testedGame.isJoinable());
		this.testedGame.addUser(user5);
		assertFalse(this.testedGame.isJoinable());
		assertEquals(1, this.testedGame.getSpectatorList().size());
	}
	
	/**
	 * Test method for {@link games.Uno.Uno#execute(userManagement.User, java.lang.String)}.
	 */
	@Test
	public void testExecuteDownloadData() {
		
		Gson gson = new GsonBuilder().create();
		
		User host = new User("host", "");
		User user2 = new User("user2", "");
		
		this.testedGame.addUser(host);
		this.testedGame.addUser(user2);

		this.testedGame.execute(host, "RESTART");
		
		// should parse back into data
		ClientData testedClientData = gson.fromJson(this.testedGame.getGameData("DOWNLOAD_DATA", host), ClientData.class);
		
		// should contain cards of the player
		assertArrayEquals(
				testedClientData.getCurrentCards().toArray(), 
				this.testedGame.playersHand.get(host).toArray()
				);
	}
	
	/**
	 * Test method for {@link games.Uno.Uno#execute(userManagement.User, java.lang.String)}.
	 */
	@Ignore("unsertain whether it should even be implemented...")
	@Test
	public void testOnlyHostShouldExecuteGameRestart() {
		
		fail("not yet implemented.");
		
		User host = new User("host", "");
		User user2 = new User("user2", "");
		
		this.testedGame.addUser(host);
		this.testedGame.addUser(user2);
		
		// only host should be able to start game
		assertEquals(GameState.SETUP, this.testedGame.getGameState());
		this.testedGame.execute(user2, "RESTART");
		assertEquals(GameState.SETUP, this.testedGame.getGameState());
		this.testedGame.execute(host, "RESTART");
		assertEquals(GameState.RUNNING, this.testedGame.getGameState());
	}
	
	/**
	 * Test method for {@link games.Uno.Uno#getCurrentPlayerCards()}.
	 */
	@Test
	public void testGetCurrentPlayerCards() {
		User host = new User("host", "");
		User user2 = new User("user2", "");
		
		Ai ai1 = new Ai("ai1", Ai.DiffLevel.EASY, this.testedGame);
		Ai ai2 = new Ai("ai2", Ai.DiffLevel.EASY, this.testedGame);
		
		// initial setup
		this.testedGame.addUser(host);
		this.testedGame.addUser(user2);
		
		this.testedGame.getAiList().add(ai1);
		this.testedGame.getAiList().add(ai2);
		
		this.testedGame.execute(host, "RESTART");
		
		// check for negative, should get ai
		this.testedGame.setTurnCounter(-1);
		assertEquals(ai2.getAiCards(), this.testedGame.getCurrentPlayerCards());
		
		// 0 => first player cards
		this.testedGame.setTurnCounter(0);
		assertEquals(
				this.testedGame.playersHand.get(host), 
				this.testedGame.getCurrentPlayerCards());
		
		// 1 => second player cards
		this.testedGame.setTurnCounter(1);
		assertEquals(
				this.testedGame.playersHand.get(user2), 
				this.testedGame.getCurrentPlayerCards());
		
		// 2 => 1 ai cards
		this.testedGame.setTurnCounter(2);
		assertEquals(ai1.getAiCards(), this.testedGame.getCurrentPlayerCards());
		
		// 3 => 2 ai cards
		this.testedGame.setTurnCounter(3);
		assertEquals(ai2.getAiCards(), this.testedGame.getCurrentPlayerCards());
	}
	
	/**
	 * Test method for {@link games.Uno.Uno#getCurrentPlayerCards()}.
	 */
	@Test
	public void testGetCurrentPlayerCardsNegativeVarsWithoutAI() {
		User host = new User("host", "");
		User user2 = new User("user2", "");
		User user3 = new User("user2", "");
		User user4 = new User("user2", "");
		
		// initial setup
		this.testedGame.addUser(host);
		this.testedGame.addUser(user2);
		this.testedGame.addUser(user3);
		this.testedGame.addUser(user4);
		
		this.testedGame.execute(host, "RESTART");
		
		// check for negative, should loop around and get ai
		this.testedGame.setTurnCounter(-1);
		assertEquals(
				this.testedGame.playersHand.get(user4), 
				this.testedGame.getCurrentPlayerCards());
		
		this.testedGame.setTurnCounter(-2);
		assertEquals(
				this.testedGame.playersHand.get(user3), 
				this.testedGame.getCurrentPlayerCards());
		
		this.testedGame.setTurnCounter(-3);
		assertEquals(
				this.testedGame.playersHand.get(user2), 
				this.testedGame.getCurrentPlayerCards());
		
		this.testedGame.setTurnCounter(-4);
		assertEquals(
				this.testedGame.playersHand.get(host), 
				this.testedGame.getCurrentPlayerCards());
		
		this.testedGame.setTurnCounter(-5);
		assertEquals(
				this.testedGame.playersHand.get(user4), 
				this.testedGame.getCurrentPlayerCards());
	}
	
	/**
	 * Test method for {@link games.Uno.Uno#getCurrentPlayerCards()}.
	 */
	@Test
	public void testGetCurrentPlayerCardsNegativeVarsWithAI() {
		User host = new User("host", "");
		User user2 = new User("user2", "");
		
		Ai ai1 = new Ai("ai1", Ai.DiffLevel.EASY, this.testedGame);
		Ai ai2 = new Ai("ai2", Ai.DiffLevel.EASY, this.testedGame);
		
		// initial setup
		this.testedGame.addUser(host);
		this.testedGame.addUser(user2);
		
		this.testedGame.getAiList().add(ai1);
		this.testedGame.getAiList().add(ai2);
		
		this.testedGame.execute(host, "RESTART");
		
		// check for negative, should loop around and get ai
		this.testedGame.setTurnCounter(-1);
		assertEquals(ai2.getAiCards(), this.testedGame.getCurrentPlayerCards());
		
		this.testedGame.setTurnCounter(-2);
		assertEquals(ai1.getAiCards(), this.testedGame.getCurrentPlayerCards());
		
		this.testedGame.setTurnCounter(-3);
		assertEquals(
				this.testedGame.playersHand.get(user2), 
				this.testedGame.getCurrentPlayerCards());
		
		this.testedGame.setTurnCounter(-4);
		assertEquals(
				this.testedGame.playersHand.get(host), 
				this.testedGame.getCurrentPlayerCards());
		
		this.testedGame.setTurnCounter(-5);
		assertEquals(ai2.getAiCards(), this.testedGame.getCurrentPlayerCards());
		
		
		
	}
	
	/**
	 * Test method for {@link games.Uno.Uno#getNextPlayerCards()}.
	 */
	@Test
	public void testGetNextPlayerCards() {
		User host = new User("host", "");
		User user2 = new User("user2", "");
		
		Ai ai1 = new Ai("ai1", Ai.DiffLevel.EASY, this.testedGame);
		Ai ai2 = new Ai("ai2", Ai.DiffLevel.EASY, this.testedGame);
		
		// initial setup
		this.testedGame.addUser(host);
		this.testedGame.addUser(user2);
		
		this.testedGame.getAiList().add(ai1);
		this.testedGame.getAiList().add(ai2);
		
		this.testedGame.execute(host, "RESTART");
		
		// check for negative, should get ai
		this.testedGame.setTurnCounter(-1);
		assertEquals(
				this.testedGame.playersHand.get(host), 
				this.testedGame.getNextPlayerCards());
		
		// 0 => second player cards
		this.testedGame.setTurnCounter(0);
		assertEquals(
				this.testedGame.playersHand.get(user2), 
				this.testedGame.getNextPlayerCards());
		
		// 1 => first ai cards
		this.testedGame.setTurnCounter(1);
		assertEquals(ai1.getAiCards(), this.testedGame.getNextPlayerCards());
		
		// 3 => 2 ai cards
		this.testedGame.setTurnCounter(2);
		assertEquals(ai2.getAiCards(), this.testedGame.getNextPlayerCards());
		
		// 4 => first player cards
		this.testedGame.setTurnCounter(3);
		assertEquals(
				this.testedGame.playersHand.get(host), 
				this.testedGame.getNextPlayerCards());
	}
	
	/**
	 * Test method for {@link games.Uno.Uno#getNextPlayerCards()}.
	 */
	@Test
	public void testGetNextPlayerCardsNegativeTurnCountPositiveRotationWithAI() {
		User host = new User("host", "");
		User user2 = new User("user2", "");
		
		Ai ai1 = new Ai("ai1", Ai.DiffLevel.EASY, this.testedGame);
		Ai ai2 = new Ai("ai2", Ai.DiffLevel.EASY, this.testedGame);
		
		// initial setup
		this.testedGame.addUser(host);
		this.testedGame.addUser(user2);
		
		this.testedGame.getAiList().add(ai1);
		this.testedGame.getAiList().add(ai2);
		
		this.testedGame.execute(host, "RESTART");
		
		// for playdirection positive
		this.testedGame.setPlayDirection(1);
		// check for negative, should loop around and get ai
		this.testedGame.setTurnCounter(-1);
		assertEquals(
				this.testedGame.playersHand.get(host), 
				this.testedGame.getNextPlayerCards());
		
		this.testedGame.setTurnCounter(-2);
		assertEquals(ai2.getAiCards(), this.testedGame.getNextPlayerCards());
		
		this.testedGame.setTurnCounter(-3);
		assertEquals(ai1.getAiCards(), this.testedGame.getNextPlayerCards());
		
		this.testedGame.setTurnCounter(-4);
		assertEquals(
				this.testedGame.playersHand.get(user2), 
				this.testedGame.getNextPlayerCards());
		
		this.testedGame.setTurnCounter(-5);
		assertEquals(
				this.testedGame.playersHand.get(host), 
				this.testedGame.getNextPlayerCards());
	}
	
	/**
	 * Test method for {@link games.Uno.Uno#getNextPlayerCards()}.
	 */
	@Test
	public void testGetNextPlayerCardsNegativeTurnCountNegativeRotationWithAI() {
		User host = new User("host", "");
		User user2 = new User("user2", "");
		
		Ai ai1 = new Ai("ai1", Ai.DiffLevel.EASY, this.testedGame);
		Ai ai2 = new Ai("ai2", Ai.DiffLevel.EASY, this.testedGame);
		
		// initial setup
		this.testedGame.addUser(host);
		this.testedGame.addUser(user2);
		
		this.testedGame.getAiList().add(ai1);
		this.testedGame.getAiList().add(ai2);
		
		this.testedGame.execute(host, "RESTART");
		
		// for playdirection positive
		this.testedGame.setPlayDirection(-1);
		// check for negative, should loop around and get ai
		this.testedGame.setTurnCounter(-1);
		assertEquals(ai1.getAiCards(), this.testedGame.getNextPlayerCards());
		
		this.testedGame.setTurnCounter(-2);
		assertEquals(
				this.testedGame.playersHand.get(user2), 
				this.testedGame.getNextPlayerCards());
		
		this.testedGame.setTurnCounter(-3);
		assertEquals(
				this.testedGame.playersHand.get(host), 
				this.testedGame.getNextPlayerCards());
		
		this.testedGame.setTurnCounter(-4);
		assertEquals(ai2.getAiCards(), this.testedGame.getNextPlayerCards());
		
		this.testedGame.setTurnCounter(-5);
		assertEquals(ai1.getAiCards(), this.testedGame.getNextPlayerCards());
	}
	
	/**
	 * Test method for {@link games.Uno.Uno#getNextPlayerCards()}.
	 */
	@Test
	public void testGetNextPlayerCardsNegativeTurnCountPositiveRotationWithoutAI() {
		User host = new User("host", "");
		User user2 = new User("user2", "");
		User user3 = new User("user2", "");
		User user4 = new User("user2", "");
		
		// initial setup
		this.testedGame.addUser(host);
		this.testedGame.addUser(user2);
		this.testedGame.addUser(user3);
		this.testedGame.addUser(user4);
		
		this.testedGame.execute(host, "RESTART");
		
		// for playdirection positive
		this.testedGame.setPlayDirection(1);
		// check for negative, should loop around and get ai
		this.testedGame.setTurnCounter(-1);
		assertEquals(
				this.testedGame.playersHand.get(host), 
				this.testedGame.getNextPlayerCards());
		
		this.testedGame.setTurnCounter(-2);
		assertEquals(
				this.testedGame.playersHand.get(user4), 
				this.testedGame.getNextPlayerCards());
		
		this.testedGame.setTurnCounter(-3);
		assertEquals(
				this.testedGame.playersHand.get(user3), 
				this.testedGame.getNextPlayerCards());
		
		this.testedGame.setTurnCounter(-4);
		assertEquals(
				this.testedGame.playersHand.get(user2), 
				this.testedGame.getNextPlayerCards());
		
		this.testedGame.setTurnCounter(-5);
		assertEquals(
				this.testedGame.playersHand.get(host), 
				this.testedGame.getNextPlayerCards());
	}
	
	/**
	 * Test method for {@link games.Uno.Uno#getNextPlayerCards()}.
	 */
	@Test
	public void testGetNextPlayerCardsNegativeTurnCountNegativeRotationWithoutAI() {
		User host = new User("host", "");
		User user2 = new User("user2", "");
		User user3 = new User("user2", "");
		User user4 = new User("user2", "");
		
		// initial setup
		this.testedGame.addUser(host);
		this.testedGame.addUser(user2);
		this.testedGame.addUser(user3);
		this.testedGame.addUser(user4);
		
		this.testedGame.execute(host, "RESTART");
		
		// for playdirection positive
		this.testedGame.setPlayDirection(-1);
		// check for negative, should loop around and get ai
		this.testedGame.setTurnCounter(-1);
		assertEquals(
				this.testedGame.playersHand.get(user3), 
				this.testedGame.getNextPlayerCards());
		
		this.testedGame.setTurnCounter(-2);
		assertEquals(
				this.testedGame.playersHand.get(user2), 
				this.testedGame.getNextPlayerCards());
		
		this.testedGame.setTurnCounter(-3);
		assertEquals(
				this.testedGame.playersHand.get(host), 
				this.testedGame.getNextPlayerCards());
		
		this.testedGame.setTurnCounter(-4);
		assertEquals(
				this.testedGame.playersHand.get(user4), 
				this.testedGame.getNextPlayerCards());
		
		this.testedGame.setTurnCounter(-5);
		assertEquals(
				this.testedGame.playersHand.get(user3), 
				this.testedGame.getNextPlayerCards());
	}
	
	/**
	 * Test method for {@link games.Uno.Uno#execute(userManagement.User, java.lang.String)}.
	 */
	@Test
	public void testSelectNextPlayerWithTwoPlayers() {
		
		User host = new User("host", "");
		User user2 = new User("user2", "");
		
		this.testedGame.addUser(host);
		this.testedGame.addUser(user2);
				
		// initial setup
		this.testedGame.execute(host, "RESTART");
		
		// after restart should be 0
		assertEquals(this.testedGame.getTurnCounter(), 0);
		
		// player plays normal card
		this.testedGame.setTurnCounter(1);
		this.testedGame.nextPlayer(this.testedGame.getPlayerList(), this.testedGame.getAiList());
		
		//should still be 1
		assertEquals(this.testedGame.getTurnCounter(), 1);
		
		// player plays normal card
		this.testedGame.setTurnCounter(2);
		this.testedGame.nextPlayer(this.testedGame.getPlayerList(), this.testedGame.getAiList());
		
		//should move to 0
		assertEquals(this.testedGame.getTurnCounter(), 0);
		
		// player plays reverse card
		this.testedGame.setTurnCounter(-1);
		this.testedGame.nextPlayer(this.testedGame.getPlayerList(), this.testedGame.getAiList());
		
		//should move to 1 ("loop around")
		assertEquals(this.testedGame.getTurnCounter(), 1);
	}
	
	/**
	 * Test method for {@link games.Uno.Uno#playCardMove(userManagement.User, applicationLogic.Uno.Card, applicationLogic.Uno.CardSet)}.
	 */
	@Test
	public void testPlayCardMoveWithReverseCard() {
		// setup
		User host = new User("host", "");
		User user2 = new User("user2", "");
		
		this.testedGame.addUser(host);
		this.testedGame.addUser(user2);
		
		this.testedGame.execute(host, "RESTART");
		
		// actual move
		Card reverseCard = Card.createActionCard(Card.Color.GREEN, Card.Type.REVERSE); 
		
		this.testedGame.getCardPile().putCardOnTop(Card.createNormalCard(Card.Color.GREEN, 0));
		
		// host plays green reverse
		this.testedGame.playersHand.get(host).add(reverseCard);
		this.testedGame.playCardMove(host.getName(), reverseCard, this.testedGame.playersHand.get(host));
		
		assertEquals(-1, this.testedGame.getPlayDirection());
		
		// before nextPlayer call in execute
		assertEquals(-1, this.testedGame.getTurnCounter());
		
		// after nextPlayer call 
		this.testedGame.nextPlayer(this.testedGame.getPlayerList(), this.testedGame.getAiList());
		assertEquals(1, this.testedGame.getTurnCounter());
	}
	
	/**
	 * Test method for {@link games.Uno.Uno#playCardMove(userManagement.User, applicationLogic.Uno.Card, applicationLogic.Uno.CardSet)}.
	 */
	@Test
	public void testPlayCardMoveWithDrawTwoCard() {
		// setup
		User host = new User("host", "");
		User user2 = new User("user2", "");
		Ai ai1 = new Ai("ai1", Ai.DiffLevel.EASY, this.testedGame);
		Ai ai2 = new Ai("ai2", Ai.DiffLevel.EASY, this.testedGame);
		
		this.testedGame.addUser(host);
		this.testedGame.addUser(user2);
		
		this.testedGame.getAiList().add(ai1);
		this.testedGame.getAiList().add(ai2);
		
		this.testedGame.execute(host, "RESTART");
		
		assertEquals(0, this.testedGame.getTurnCounter());
				
		// actual move
		Card drawTwo1 = Card.createActionCard(Card.Color.GREEN, Card.Type.DRAW_TWO);
		Card drawTwo2 = Card.createActionCard(Card.Color.RED, Card.Type.DRAW_TWO);
		Card drawTwo3 = Card.createActionCard(Card.Color.BLUE, Card.Type.DRAW_TWO);
		Card drawTwo4 = Card.createActionCard(Card.Color.YELLOW, Card.Type.DRAW_TWO);
		
		this.testedGame.getCardPile().putCardOnTop(Card.createNormalCard(Card.Color.GREEN, 0));
		
		// player 1 plays green draw2 card
		this.testedGame.playersHand.get(host).add(drawTwo1);
		this.testedGame.playCardMove(host.getName(), drawTwo1, this.testedGame.playersHand.get(host));
		// card was removed from players hand
		
		assertEquals(1, this.testedGame.getPlayDirection());
		assertEquals(1, this.testedGame.getTurnCounter());
		// player 2 should have more cards, then the starting amount
		assertEquals(7 + 2, 
				this.testedGame.playersHand.get(user2).size());
		
		// player 2 plays draw2 card aswell
		this.testedGame.playersHand.get(user2).add(drawTwo2);
		this.testedGame.playCardMove(user2.getName(), drawTwo2, this.testedGame.playersHand.get(user2));
		
		assertEquals(2, this.testedGame.getTurnCounter());
		// 1. ai should have 2 more cards
		assertEquals(7 + 2, 
				this.testedGame.getAiList().get(0).getAiCards().size());
		
		// 1. ai plays draw2 card
		this.testedGame.getAiList().get(0).getAiCards().add(drawTwo3);
		
		this.testedGame.playCardMove(ai1.getAiName(), drawTwo3, ai1.getAiCards());
		
		assertEquals(3, this.testedGame.getTurnCounter());
		// 2. ai should have 2 more cards
		assertEquals(7 + 2, 
				this.testedGame.getAiList().get(1).getAiCards().size());
				
		// 2. ai plays draw2 card
		this.testedGame.getAiList().get(1).getAiCards().add(drawTwo4);
		
		this.testedGame.playCardMove(ai2.getAiName(), drawTwo4, ai2.getAiCards());
		
		// note turn counter out of bounds, but it's k alson as nextPlayer is called afterwards
		assertEquals(4, this.testedGame.getTurnCounter());
		// player 1 should have more cards, then the amount of last turn 
		// (remember I cheated and added the reverse card)
		assertEquals(8 - 1 + 2, 
				this.testedGame.playersHand.get(host).size());
	}
	
	/**
	 * Test method for {@link games.Uno.Uno#playCardMove(userManagement.User, applicationLogic.Uno.Card, applicationLogic.Uno.CardSet)}.
	 */
	@Test
	public void testPlayCardMoveWithSkipMoveCardOnlyPlayers() {
		// setup
		User host  = new User("host", "");
		User user2 = new User("user2", "");
		User user3 = new User("user2", "");
		User user4 = new User("user2", "");
		
		// initial setup
		this.testedGame.addUser(host);
		this.testedGame.addUser(user2);
		this.testedGame.addUser(user3);
		this.testedGame.addUser(user4);
		
		this.testedGame.execute(host, "RESTART");
		
		// game setup
		this.testedGame.getCardPile().putCardOnTop(Card.createNormalCard(Card.Color.GREEN, 0));
		// some game tests
		assertEquals(0,  this.testedGame.getTurnCounter());
		assertEquals(1, this.testedGame.getPlayDirection());
				
		// actual move
		Card skip1 = Card.createActionCard(Card.Color.GREEN, Card.Type.SKIP);
		Card skip2 = Card.createActionCard(Card.Color.RED, Card.Type.SKIP);
		Card skip3 = Card.createActionCard(Card.Color.BLUE, Card.Type.SKIP);
		Card skip4 = Card.createActionCard(Card.Color.YELLOW, Card.Type.SKIP);
		
		// player 1 plays green skip card
		this.testedGame.playersHand.get(host).add(skip1);
		this.testedGame.playCardMove(host.getName(), skip1, this.testedGame.playersHand.get(host));
		
		// player 2's turn should be skipped, player 3 should be en turn
		assertEquals(2, this.testedGame.getTurnCounter());
		assertEquals(
				this.testedGame.playersHand.get(user3), 
				this.testedGame.getCurrentPlayerCards());
		
		// player 3 plays red skip card
		this.testedGame.playersHand.get(user3).add(skip2);
		this.testedGame.playCardMove(user3.getName(), skip2,
				this.testedGame.playersHand.get(user3));
		
		// player 4's turn should be skipped, player 1 should be en turn
		assertEquals(4, this.testedGame.getTurnCounter());
		assertEquals(
				this.testedGame.playersHand.get(host), 
				this.testedGame.getCurrentPlayerCards());
	}
	
	/**
	 * Test method for {@link games.Uno.Uno#playCardMove(userManagement.User, applicationLogic.Uno.Card, applicationLogic.Uno.CardSet)}.
	 */
	@Test
	public void testPlayCardMoveWithSkipMoveCardWithAI() {
		// setup
		User host = new User("host", "");
		User user2 = new User("user2", "");
		Ai ai1 = new Ai("ai1", Ai.DiffLevel.EASY, this.testedGame);
		Ai ai2 = new Ai("ai2", Ai.DiffLevel.EASY, this.testedGame);
		
		this.testedGame.addUser(host);
		this.testedGame.addUser(user2);
		
		this.testedGame.getAiList().add(ai1);
		this.testedGame.getAiList().add(ai2);
		
		this.testedGame.execute(host, "RESTART");
		
		// game setup
		this.testedGame.getCardPile().putCardOnTop(Card.createNormalCard(Card.Color.GREEN, 0));
		// some game tests
		assertEquals(0,  this.testedGame.getTurnCounter());
		assertEquals(1, this.testedGame.getPlayDirection());
				
		// actual move
		Card skip1 = Card.createActionCard(Card.Color.GREEN, Card.Type.SKIP);
		Card skip2 = Card.createActionCard(Card.Color.RED, Card.Type.SKIP);
		Card skip3 = Card.createActionCard(Card.Color.BLUE, Card.Type.SKIP);
		Card skip4 = Card.createActionCard(Card.Color.YELLOW, Card.Type.SKIP);
		
		// player 1 plays green skip card
		this.testedGame.playersHand.get(host).add(skip1);
		this.testedGame.playCardMove(host.getName(), skip1, this.testedGame.playersHand.get(host));
		
		// player 2's turn should be skipped, player 3 (1. ai) should be en turn
		assertEquals(2, this.testedGame.getTurnCounter());
		assertEquals(ai1.getAiCards(), this.testedGame.getCurrentPlayerCards());
		
		// 1. ai plays red skip card
		this.testedGame.getAiList().get(0).getAiCards().add(skip2);
		this.testedGame.playCardMove(this.testedGame.getAiList().get(0).getAiName(), skip2,
				this.testedGame.getAiList().get(0).getAiCards());
		
		// player 4's turn should be skipped, player 1 should be en turn
		assertEquals(4, this.testedGame.getTurnCounter());
		assertEquals(
				this.testedGame.playersHand.get(host), 
				this.testedGame.getCurrentPlayerCards());
	}
	
	
	/**
	 * Test method for {@link games.Uno.Uno#playCardMove(userManagement.User, applicationLogic.Uno.Card, applicationLogic.Uno.CardSet)}.
	 */
	@Test
	public void testPlayCardMoveWithWildCardOnlyPlayers() {
		// setup
		User host  = new User("host", "");
		User user2 = new User("user2", "");
		User user3 = new User("user2", "");
		User user4 = new User("user2", "");
		
		// initial setup
		this.testedGame.addUser(host);
		this.testedGame.addUser(user2);
		this.testedGame.addUser(user3);
		this.testedGame.addUser(user4);
		
		this.testedGame.execute(host, "RESTART");
		
		// game setup
		this.testedGame.getCardPile().putCardOnTop(Card.createNormalCard(Card.Color.GREEN, 0));
		// some game tests
		assertEquals(0,  this.testedGame.getTurnCounter());
		assertEquals(1, this.testedGame.getPlayDirection());
				
		// actual move
		Card skip1 = Card.createActionCard(Card.Color.GREEN, Card.Type.SKIP);
		Card skip2 = Card.createActionCard(Card.Color.RED, Card.Type.SKIP);
		Card skip3 = Card.createActionCard(Card.Color.BLUE, Card.Type.SKIP);
		Card skip4 = Card.createActionCard(Card.Color.YELLOW, Card.Type.SKIP);
		
		// player 1 plays green skip card
		this.testedGame.playersHand.get(host).add(skip1);
		this.testedGame.playCardMove(host.getName(), skip1, this.testedGame.playersHand.get(host));
		
		// player 2's turn should be skipped, player 3 should be en turn
		assertEquals(2, this.testedGame.getTurnCounter());
		assertEquals(
				this.testedGame.playersHand.get(user3), 
				this.testedGame.getCurrentPlayerCards());
		
		// player 3 plays red skip card
		this.testedGame.playersHand.get(user3).add(skip2);
		this.testedGame.playCardMove(user3.getName(), skip2,
				this.testedGame.playersHand.get(user3));
		
		// player 4's turn should be skipped, player 1 should be en turn
		assertEquals(4, this.testedGame.getTurnCounter());
		assertEquals(
				this.testedGame.playersHand.get(host), 
				this.testedGame.getCurrentPlayerCards());
	}
	
	/**
	 * Test method for {@link games.Uno.Uno#playCardMove(userManagement.User, applicationLogic.Uno.Card, applicationLogic.Uno.CardSet)}.
	 */
	@Test
	public void testPlayCardMoveWithWildCardWithAI() {
		// setup
		User host = new User("host", "");
		User user2 = new User("user2", "");
		Ai ai1 = new Ai("ai1", Ai.DiffLevel.EASY, this.testedGame);
		Ai ai2 = new Ai("ai2", Ai.DiffLevel.EASY, this.testedGame);
		
		this.testedGame.addUser(host);
		this.testedGame.addUser(user2);
		
		this.testedGame.getAiList().add(ai1);
		this.testedGame.getAiList().add(ai2);
		
		this.testedGame.execute(host, "RESTART");
		
		// game setup
		this.testedGame.getCardPile().putCardOnTop(Card.createNormalCard(Card.Color.GREEN, 0));
		// some game tests
		assertEquals(0,  this.testedGame.getTurnCounter());
		assertEquals(1, this.testedGame.getPlayDirection());
				
		// actual move
		Card wild1 = Card.createWildCard(Card.Type.WILD);
		Card wild2 = Card.createWildCard(Card.Type.WILD);
		Card wild3 = Card.createWildCard(Card.Type.WILD);
		Card wild4 = Card.createWildCard(Card.Type.WILD);
		
		// player 1 plays green skip card
		this.testedGame.playersHand.get(host).add(wild1);
		this.testedGame.playCardMove(host.getName(), wild1, this.testedGame.playersHand.get(host));
		
		// turn counter should not increase, since we are awaiting a color pick on host
		assertTrue(this.testedGame.isPlayerPickingWildColor());
		assertEquals(0, this.testedGame.getTurnCounter());
		
		// should "save" wild card for color selection 
		assertEquals(wild1, this.testedGame.getWildCardToBePlayed());
		
		// user 1 picks a color
		// NOTE: tecnically done with event "CHOOLE_COLOR_*"
//		this.testedGame.chooseColor(
//				this.testedGame.getWildCardToBePlayed(), 
//				Color.RED, 
//				this.testedGame.playersHand.get(host), );
		
		// should set card to null for savety
		assertNull(this.testedGame.getWildCardToBePlayed());
		
		assertFalse(this.testedGame.isPlayerPickingWildColor());
		assertEquals(1, this.testedGame.getTurnCounter());
		
//		// 1. ai plays red skip card
//		this.testedGame.getAiList().get(0).getAiCards().add(skip2);
//		
//		// player 4's turn should be skipped, player 1 should be en turn
//		assertEquals(4, this.testedGame.getTurnCounter());
	}
}
