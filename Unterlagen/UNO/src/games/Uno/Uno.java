package games.Uno;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import applicationLogic.Uno.Ai;
import applicationLogic.Uno.Card;
import applicationLogic.Uno.CardSet;
import applicationLogic.Uno.ClientData;
import applicationLogic.Uno.Rules;
import applicationLogic.Uno.TextColor;
import applicationLogic.Uno.Card.Color;
import applicationLogic.Uno.Card.Type;
import applicationLogic.Uno.TextColor.typeTextColor;
import gameClasses.Game;
import gameClasses.GameState;
import global.FileHelper;
import userManagement.User;

public class Uno extends Game {
	
	private int MAX_PLAYERS = 5;
	private int finish =200;
	
	ArrayList<User> playerList = new ArrayList<User>();
	ArrayList<Ai>   aiList = new ArrayList<Ai>();
	
	ArrayList<User> spectatorList = new ArrayList<User>();
	
	private int turnCounter = 0;
	private String playerLeft = null;
	
	private User playerTurn = null;
	private boolean playerTurnValid = false;
	
	private int playDirection = 1;
	
	public HashMap<User, CardSet> playersHand = new HashMap<User, CardSet>(MAX_PLAYERS);
	HashMap<User, Integer> playerScore = new HashMap<User, Integer>(MAX_PLAYERS);
	private HashMap<User, Boolean> buzzerList = new HashMap<User, Boolean>(MAX_PLAYERS);
	private HashMap<String, Boolean> lockTurn = new HashMap<String, Boolean>(1);
	
	private CardSet drawDeck = new CardSet();
	private CardSet cardPile = new CardSet();
	
	private boolean playerPickingWildColor = false;
	/**
	 * wild card to be played after color selection.
	 */
	private Card wildCardToBePlayed;
	private boolean drawFour=false;
	private String tempAnnouncementString = "";
	private String winner="";
	
	/**
	 * funktionen aller karten 
	 */
	
	//Karte spielen (aus playershand l�schen und dem stapel hinzuf�gen)
	public void playCard(Card playerCard, CardSet playersCards){
		this.cardPile.putCardOnTop(playerCard);
		playersCards.remove(playerCard);
		
		this.turnCounter += this.playDirection;
		
	}

	
	public void playCardMove(String userName, Card card, CardSet playersCards) {
		
		
		
		if(!Rules.validMove(card, cardPile.getTopCard())) {
			this.makeAnnouncementToAllPlayers(userName+" verucht die falsche Karte zu legen.",TextColor.typeTextColor.FAIL);
			return;
		// ai sets Color automatically
		} else if((card.getType()==Card.Type.WILD || card.getType()==Card.Type.WILD_DRAW_FOUR) && card.getColor() != null) {
			if(card.getType() == Type.WILD_DRAW_FOUR){ 
				if (drawDeck.size()<4)
					refillDrawCards();
				this.getNextPlayerCards().addAll(drawDeck.draw(4));
				
			}
			printCard(userName, card);
			this.makeAnnouncementToAllPlayers("Player "+userName+" set color to: " +TextColor.getColorAsText(card.getColor()));
			this.turnCounter += this.playDirection;
			playCard(card, playersCards);
			return;

		// no color -> wait for player to chooseCard
		} else if((card.getType()==Card.Type.WILD || card.getType()==Card.Type.WILD_DRAW_FOUR) && card.getColor() == null){
			//Farbwunschkartehis.sendGameDataToUser(this.playerTurn, "CHOOSE_COLOR");
			
			if(card.getType() == Type.WILD_DRAW_FOUR){ 
				if (drawDeck.size()<4)
					refillDrawCards();
				this.getNextPlayerCards().addAll(drawDeck.draw(4));
				drawFour=true;
			}
			printCard(userName, card);
			this.playerPickingWildColor = true;
			this.wildCardToBePlayed     = card;
			// sent event to choose color
			this.sendGameDataToUser(this.playerTurn, "CHOOSE_COLOR");
			lockTurn.put(getCurrentPlayerName(), true);
			
			return;
		
		} else if(card.getType() == Card.Type.DRAW_TWO) {
			//zwei Karten ziehen
			this.drawTwo(card, playersCards);
		
			
		} else if(card.getType() == Card.Type.SKIP) {
			//Zug �berspringen
			this.skipCardPlayed(card, playersCards);
			
		}else if (card.getType() == Card.Type.REVERSE){
			//Richtungswechsel
			this.reverseCard(card, playersCards);
			
		}else if (card.getType() == Card.Type.NORMAL){
			//normale Karten
			playNormalCard(card, playersCards);
		}
		printCard(userName, card);
	}
	
	//Alle Normalen Karte
	public boolean playNormalCard (Card playerCard, CardSet playersCards){
		if (playerCard.getType() == Card.Type.NORMAL &&
			playerCard.getColor() == cardPile.getTopCard().getColor() || 
			playerCard.getScore() == cardPile.getTopCard().getScore()){
			playCard(playerCard, playersCards);
		}
		return true;
		
	}
	
	//Farbwunschkarte
	public void chooseColor(Card playerCard, Color color, CardSet playersCards, String userName){
			
			this.playerPickingWildColor = false;
			this.wildCardToBePlayed     = null;
			playerCard.setColor(color);
			playCard(playerCard, playersCards);
			String newColor = TextColor.getColorAsText(color);
			this.makeAnnouncementToAllPlayers("Player " + userName +  " set color to: " + newColor);  
	}
	
	//Zwei Ziehen   //hier CardSet.drawTwo aufrufen
	public void drawTwo(Card playerCard, CardSet playersCards){
		
		if(drawDeck.size()<2)
			refillDrawCards();
		
		this.getNextPlayerCards().addAll(drawDeck.draw(2));
		
		playCard(playerCard, playersCards);
		turnCounter += playDirection;
	}
	
			
	//Richtungswechselkarte
	public void reverseCard (Card playerCard, CardSet playersCards){
		
		//richtungswechsel anweisen
		if(playDirection == 1){
			playDirection = -1;
		}else{
			playDirection = 1;
		}
		//Karte spielen
		 playCard(playerCard, playersCards);
	
	}

	
	//Aussetzen Karte
	public void skipCardPlayed (Card playerCard, CardSet playerCards){
			
		// skip next player
		turnCounter += playDirection;
		// Karte spielen
		playCard(playerCard, playerCards);
	}
	
	
	
	/**
	 * Draws Card and put it in players Hand
	 * @param playersCards
	 * @author Jan
	 */
	public void drawCard (CardSet playersCards){
		
		if(drawDeck.size()<1)
			refillDrawCards();
		
		if (cardPile.getTopCard().getType() != Card.Type.DRAW_TWO || 
			cardPile.getTopCard().getType() != Card.Type.WILD_DRAW_FOUR) {
			
			playersCards.add(drawDeck.drawOne());
			
		
		} else this.makeAnnouncementToAllPlayers("cannot draw for this event, computer adds cards to your hand");
	}
	
	
	/**
	 * Checks CardDeck, if Empty refill Draw Cards
	 * @author Jan
	 */
	public void refillDrawCards(){
		Card saveTop = cardPile.get(cardPile.size()-1);
		cardPile.remove(cardPile.size()-1);
		Collections.shuffle(cardPile);
		for (Card card : cardPile){
			if(card.isType(Card.Type.WILD_DRAW_FOUR)||card.isType(Card.Type.WILD)){
				card.setColor(null);
			}
		}
		drawDeck.addAll(cardPile);
		cardPile.clear();
		cardPile.add(saveTop);	
	}

	
	/**Set score of winner
	 * @param winner
	 * @author Jan
	 */
	public void setScoreOfWinner (User winner){
		int score=0;
		for (int i =0;i<playerList.size();i++ ){
			score += playersHand.get(playerList.get(i)).getTotalCardValue();
			}
		for (Ai ai: aiList){
			score += ai.getAiCards().getTotalCardValue();
		}
		playerScore.put(winner, playerScore.get(winner)+score);
		this.makeAnnouncementToAllPlayers("Player "+winner.getName()+" won in this Round " + score+" points", TextColor.typeTextColor.WON);
		
	}
	
		
	//Spielerrotation
	public void nextPlayer(ArrayList<User> playerList, ArrayList<Ai> aiList){
		
		int totalParticipants = this.playerList.size() + this.aiList.size();
		
		this.turnCounter %= totalParticipants;
		
		if(this.turnCounter < 0) 
			this.turnCounter += totalParticipants;
		else if(this.turnCounter >= totalParticipants) 
			this.turnCounter -= totalParticipants;
		
		// ais move
		if(this.turnCounter >= this.playerList.size()) {
			
			//Send Data to server and give Ai time to such a Card
			sendGameDataToClients("DOWNLOAD_DATA");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			int aiTurnCounter = this.turnCounter - this.playerList.size();
			
			Ai ai = aiList.get(aiTurnCounter);
			Card aiCard = ai.pickCard();
			
			
			
			// if ai didnt skip turn
			if(aiCard != null) {
				this.playCardMove(ai.getAiName(), aiCard, ai.getAiCards());
				//last Card of Ai?
				if (newRoundAi(ai))
					return;
			}
				
			else {
				this.makeAnnouncementToAllPlayers(ai.getAiName()+" drew a card.",TextColor.typeTextColor .DRAW);
				this.turnCounter += this.playDirection;
			}
			this.nextPlayer(playerList, aiList);
		}
		else {
			this.makeAnnouncementToAllPlayers("Current Player: " + this.getCurrentPlayerName());
		}
	}
	
	
	/**
	 * Sets score of Ai and starts new Round
	 * @param ai
	 * @return if Ai Cards = null true
	 */
	private boolean newRoundAi(Ai ai) {
		if (ai.getAiCards().isEmpty()){
			int score = 0;
			for(User user : playerList){
				score+=playersHand.get(user).getTotalCardValue();
			}
			ai.setScore(ai.getScore()+score);
			this.makeAnnouncementToAllPlayers("<font color='gold'>Player "+ai.getAiName()+" won in this Round " + score+" points</font>");
			printScore();
			newRound();
			
			this.makeAnnouncementToAllPlayers("Current Player: " + this.getCurrentPlayerName());
		
			return true;
		}
		return false;
	}

	/**
	 * @return A CardSet of the current player (determined by turnCounter)
	 */
	public CardSet getCurrentPlayerCards() {
		int tempTurnCounter = this.turnCounter;
		
		int totalParticipants = this.playerList.size() + this.aiList.size();
		
		tempTurnCounter %= totalParticipants;
		
		if(tempTurnCounter < 0) 
			tempTurnCounter += totalParticipants;
		else if(tempTurnCounter >= totalParticipants) 
			tempTurnCounter -= totalParticipants;
		
		if(tempTurnCounter < this.playerList.size()) {
			User player = this.playerList.get(tempTurnCounter);
			
			return this.playersHand.get(player);
		}
		else {
			tempTurnCounter -= this.playerList.size();
			
			return this.aiList.get(tempTurnCounter).getAiCards();
		}
	}
	
	/**
	 * @return the name of the current player (determined by turnCounter)
	 */
	public String getCurrentPlayerName() {
		int tempTurnCounter = this.turnCounter;
		
		int totalParticipants = this.playerList.size() + this.aiList.size();
		
		tempTurnCounter %= totalParticipants;
		
		if(tempTurnCounter < 0) 
			tempTurnCounter += totalParticipants;
		else if(tempTurnCounter >= totalParticipants) 
			tempTurnCounter -= totalParticipants;
		
		if(tempTurnCounter < this.playerList.size()) {
			User player = this.playerList.get(tempTurnCounter);
			
			return player.getName();
		}
		else {
			tempTurnCounter -= this.playerList.size();
			
			return this.aiList.get(tempTurnCounter).getAiName();
		}
	}
	
	/**
	 * @return A CardSet of the next player (determined by turnCounter)
	 */
	public CardSet getNextPlayerCards() {
		int tempTurnCounter = this.turnCounter + this.playDirection;
		
		int totalParticipants = this.playerList.size() + this.aiList.size();
		
		tempTurnCounter %= totalParticipants;
		
		if(tempTurnCounter < 0) 
			tempTurnCounter += totalParticipants;
		else if(tempTurnCounter >= totalParticipants) 
			tempTurnCounter -= totalParticipants;
		
		if(tempTurnCounter < this.playerList.size()) {
			User player = this.playerList.get(tempTurnCounter);
			
			return this.playersHand.get(player);
		}
		else {
			tempTurnCounter -= this.playerList.size();
			
			return this.aiList.get(tempTurnCounter).getAiCards();
		}
	}
	
	/**
	 * @return the name of the next player (determined by turnCounter)
	 */
	public String getNextPlayerName() {
		int tempTurnCounter = this.turnCounter + this.playDirection;
		
		int totalParticipants = this.playerList.size() + this.aiList.size();
		
		tempTurnCounter %= totalParticipants;
		
		if(tempTurnCounter < 0) 
			tempTurnCounter += totalParticipants;
		else if(tempTurnCounter >= totalParticipants) 
			tempTurnCounter -= totalParticipants;
		
		
		if(tempTurnCounter < this.playerList.size()) {
			User player = this.playerList.get(tempTurnCounter);
			
			return player.getName();
		}
		else {
			tempTurnCounter -= this.playerList.size();
			
			return this.aiList.get(tempTurnCounter).getAiName();
		}
	}
	
	/**
	 * 	@author Raphael Dobesch
	 *  @param user, gsonString
	 */
	
	@Override
	public void execute(User user, String gsonString) {
		
		try {
			// the order of operations is important here!
			// first handle Game specific "Meta Events" ... 
			//  (represented by plaintext gsonStrings)
			// game closed or needs to be closed 
			if(this.gState==GameState.CLOSED) return;
			
			if(spectatorList.contains(user)) return;
			
			if(gsonString.equals("CLOSE")){
				sendGameDataToClients("CLOSE");
				closeGame();
				return;
			}
			
			if(spectatorList.contains(user)){
				this.makeAnnouncementToPlayer(user,"You are only spectator!",TextColor.typeTextColor.MESSAGE);
				return;
			}
			
			if(gsonString.equals("RESTART")) {
				if(gState == GameState.RUNNING) {
					this.makeAnnouncementToAllPlayers("Cannot restart game once it was started.");
					sendGameDataToClients("CLEAR_CENTER");
					return;
				}
				
//				if(!creator.equals(user)) {
//					this.makeAnnouncementToAllPlayers("Only host can restart game.");
//					return;
//				}
				
				if (playerList.size() + aiList.size() < 2) {
					this.makeAnnouncementToAllPlayers("Cannot play alone.", TextColor.typeTextColor.ERROR);
					return;
				}
				
				this.restartGame();
				setTurnCounter(0);
				this.gState = GameState.RUNNING;
				// send (initial) Data to players
				sendGameDataToClients("DOWNLOAD_DATA");
				return;
				
			}
			
			if(gsonString.equals("UPDATE_PLAYERLIST")){
				sendGameDataToClients("UPDATE_PLAYERLIST");
				return;
			}
			
			if(gsonString.equals("ADD_AI")) {
				if((this.playerList.size() + this.aiList.size()) < MAX_PLAYERS) {
					
					String aiName = "Easy AI " + this.aiList.size()+1;
					
					this.aiList.add(new Ai(aiName, Ai.DiffLevel.EASY, this));
					
					this.makeAnnouncementToAllPlayers("Ai "+ aiName +" was Added Correctly!", TextColor.typeTextColor.MESSAGE);
				}
				else {
					this.makeAnnouncementToPlayer(user, "Ai could not be added, max player amount reached!", TextColor.typeTextColor.ERROR);
				}
				// Refreshing the AI in the playerList
				sendGameDataToClients("UPDATE_PLAYERLIST");
				return;
			}
			
			/*
			 * 			 ... secondly handle player input
			 *			 forbid any other gState than RUNNING (players can not be playing the game otherwise)
			 */
			if(gState != GameState.RUNNING)	return;			
			
			if(user.getName() != getCurrentPlayerName()){
				makeAnnouncementToPlayer(user, "This is not your turn!", TextColor.typeTextColor.FAIL);
				ArrayList<User> others=new ArrayList<User>();
				others.addAll(playerList);
				others.remove(user);
				for (User u : others){
					makeAnnouncementToPlayer(u,user.getName()+" ist ungeduldig", TextColor.typeTextColor.MESSAGE);
				}
				return;
			}
			
			if(gsonString.equals("CHOOSE_COLOR_GREEN")) {
				chooseColor(this.wildCardToBePlayed, Card.Color.GREEN, this.playersHand.get(user), user.getName());
				if(zeroCards(this.playersHand.get(user), user)) return;
				if(this.drawFour){
					this.turnCounter += this.playDirection;
					drawFour=false;
				}
				this.nextPlayer(playerList, aiList);
				sendGameDataToClients("DOWNLOAD_DATA");
				lockTurn.clear();
				return;
			}			
			if(gsonString.equals("CHOOSE_COLOR_YELLOW")) {
				chooseColor(this.wildCardToBePlayed, Card.Color.YELLOW, this.playersHand.get(user), user.getName());
				if(zeroCards(this.playersHand.get(user), user)) return;
				if(this.drawFour){
					this.turnCounter += this.playDirection;
					drawFour=false;
				}
				this.nextPlayer(playerList, aiList);
				sendGameDataToClients("DOWNLOAD_DATA");
				lockTurn.clear();
				return;
			}
			if(gsonString.equals("CHOOSE_COLOR_BLUE")) {
				chooseColor(this.wildCardToBePlayed, Card.Color.BLUE, this.playersHand.get(user), user.getName());
				if(zeroCards(this.playersHand.get(user), user)) return;
				if(this.drawFour){
					this.turnCounter += this.playDirection;
					drawFour=false;
				}
				this.nextPlayer(playerList, aiList);
				sendGameDataToClients("DOWNLOAD_DATA");
				lockTurn.clear();
				return;
			}
			if(gsonString.equals("CHOOSE_COLOR_RED")) {
				chooseColor(this.wildCardToBePlayed, Card.Color.RED, this.playersHand.get(user), user.getName());
				if(zeroCards(this.playersHand.get(user), user)) return;
				if(this.drawFour){
					this.turnCounter += this.playDirection;
					drawFour=false;
				}
				this.nextPlayer(playerList, aiList);
				sendGameDataToClients("DOWNLOAD_DATA");
				lockTurn.clear();
				return;
			}
			
			if(!lockTurn.isEmpty()){
				if(lockTurn.get(user.getName())){
					this.makeAnnouncementToPlayer(user, "You have to choose a card!!", TextColor.typeTextColor.FAIL);
					return;
				}				
			}

			
			
			if(this.playerPickingWildColor == true) {
				this.makeAnnouncementToPlayer(user, "You must select a color for your WILD card first!", TextColor.typeTextColor.ERROR);
			}
				
			//if(!user.equals(playerTurn)) return;
			
			// -------------------- handle players turn ---------------------------------
			// ---------------- (gsonString is a card id of the players hand) -----------
			
			this.playerTurn = user;
			CardSet playersCards = playersHand.get(user);
			Card card = playersCards.getCard(gsonString);
			
			// hier den zug ausführen
			if(gsonString.equals("DRAW_CARD")) { 
				if(user.getName().equals(playerTurn.getName()))
					drawCard(playersCards);
					this.turnCounter += this.playDirection;
					this.makeAnnouncementToAllPlayers(user.getName() +" drew a card.",TextColor.typeTextColor .DRAW);
					this.makeAnnouncementToAllPlayers("Anzahl der Karten im DrawDeck: "+drawDeck.size());
					this.nextPlayer(playerList, aiList);
					buzzerList.put(user, false);
					sendGameDataToClients("DOWNLOAD_DATA");
				return;
			}
			
			// Regulate Buzzer
			if(gsonString.equals("BUZZER")){
				if(playersCards.size()>2){
					drawCard(playersCards);
					this.turnCounter += this.playDirection;
					this.nextPlayer(playerList, aiList);
					this.makeAnnouncementToAllPlayers("Player "+ user.getName() +" war etwas voreilig und wurde bestraft!",TextColor.typeTextColor .FAIL);
					sendGameDataToClients("DOWNLOAD_DATA");
					return;
				}else if(playersCards.size()==2){
					buzzerList.put(user, true);
					this.makeAnnouncementToAllPlayers("Player "+ user.getName() +" schreit UUUUNNNNNOOOOO!!!",TextColor.typeTextColor.CALL);
					return;
				}else{
					this.makeAnnouncementToPlayer(user, "Jetzt brauchst du auch nicht mehr buzzern!");
					return;
				}
			}
			
			if(playersCards.size()==2){
				if(buzzerList.get(user)==false){
					drawCard(playersCards);
					this.turnCounter += this.playDirection;
					this.nextPlayer(playerList, aiList);
					this.makeAnnouncementToAllPlayers("Player "+ user.getName() +" hat vergessen UNO zu sagen...VOLLHONK!",TextColor.typeTextColor.FAIL);
					sendGameDataToClients("DOWNLOAD_DATA");		
					return;
				}else{
					buzzerList.put(user, false);
				}
				
			}

			// Core
			this.playCardMove(user.getName(), card, playersCards);
			
			// wait until player picks color
			if(this.playerPickingWildColor) return;
			
			if(zeroCards(playersCards, user)) return;
			
			this.nextPlayer(playerList, aiList);
			
				 

			
			// refresh the game grid
			sendGameDataToClients("DOWNLOAD_DATA");
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private boolean zeroCards(CardSet cards, User user){
		if(cards.size()==0){
			setScoreOfWinner(user);
			printScore();
			
			//Prüft auch den punktestand 
			this.newRound();
			return true;
		}
		return false;
	}
	
	
	/**
	 * Prind Card to Log
	 * @param user
	 * @param card
	 * @author Jan
	 */
	public void printCard(String user, Card card){
		String color= TextColor.getColorForCardType(card);
		makeAnnouncementToAllPlayers("Player "+ user + " played "+ color +" Card of type: "+card.getType()+" and number: "+card.getScore());
	}
	
	/**
	 * Prints HighScore
	 * @author Jan
	 */
	private void printScore() {
		String print = "-----Score Table-----";
		for(User player : playerList){
			if(!print.isEmpty())
				print+="<br>";
			print += "Player "+player.getName() + " has "+playerScore.get(player)+" Points ";
		}
		for(Ai ai : aiList){
			if(!print.isEmpty())
				print+="<br>";
			print += "Ai"+ ai.getAiName()+ " has "+ai.getScore()+" Points ";
		}
		
		makeAnnouncementToAllPlayers(print, TextColor.typeTextColor.POINT_TABLE);
	}

	/**
	 *   @author Raphael Dobesch
	 *   @param eventName, user
	 *   @return String
	 */
	@Override
	public String getGameData(String eventName, User user) {
		Gson gson = new GsonBuilder().create();
		
		if(eventName.equals("CHOOSE_COLOR")) {
			return eventName;
		}
		
		if(eventName.equals("UPDATE_PLAYERLIST")) {
			ArrayList<String> playerNames = new ArrayList<String>();
			playerNames.add(user.getName());
			for(User u : this.playerList){
				if(!u.equals(user)){
					playerNames.add(u.getName());
				}
			}
			if(!this.aiList.isEmpty()){
				for(Ai ai : this.aiList){
					playerNames.add(ai.getAiName());
				}				
			}
			
			return gson.toJson(playerNames);
		}
		
		if(eventName.equals("ANNOUNCEMENT")) {
			return this.tempAnnouncementString;
		}
		
		if(eventName.equals("DOWNLOAD_DATA")) {
			
			// determine other players to determine their card count
			ArrayList<User> otherPlayers = new ArrayList<User>();
			otherPlayers.addAll(playerList);
			otherPlayers.remove(user);
			
			HashMap<String, Integer> otherPlayersCards = new HashMap<String, Integer>();
			
			for (User otherPlayer : otherPlayers) {
				otherPlayersCards.put(otherPlayer.getName(), playersHand.get(otherPlayer).size());
			}
			
			for (Ai ai : this.aiList) {
				otherPlayersCards.put(ai.getAiName(), ai.getAiCards().size());
			}
			
			
			
			ClientData playerTurn = new ClientData(
					getCurrentPlayerName(),
					playersHand.get(user),
					cardPile.get(0),
					otherPlayersCards, 
					null, 
					playDirection
					);
			
			return gson.toJson(playerTurn);
		}
		if(eventName.equals("FINISHED")) {
			return winner+" hat das Spiel gewonnen!";
		}
		
		return null;
	}
	
	public void makeAnnouncementToAllPlayers(String announcement) {
		if(super.getUserMap() != null && super.getUserMap().size() != 0) {
			this.tempAnnouncementString = announcement;
			sendGameDataToClients("ANNOUNCEMENT");
		}
	}
	
	public void makeAnnouncementToAllPlayers(String announcement, TextColor.typeTextColor  type) {
		if(super.getUserMap() != null && super.getUserMap().size() != 0) {
			this.tempAnnouncementString = TextColor.getColor(type)+announcement+TextColor.end();
			sendGameDataToClients("ANNOUNCEMENT");
		}
	}
	
	public void makeAnnouncementToPlayer(User user, String announcement) {
		this.tempAnnouncementString = announcement;
		this.sendGameDataToUser(user, "ANNOUNCEMENT");
	}
	
	public void makeAnnouncementToPlayer(User user, String announcement, TextColor.typeTextColor type) {
		this.tempAnnouncementString = TextColor.getColor(type)+announcement+TextColor.end();
		this.sendGameDataToUser(user, "ANNOUNCEMENT");
	}
	
	public void checkPlayerQuantity(){
		int totalPlayers = this.playerList.size() + this.aiList.size();
		if(totalPlayers==1){
			this.makeAnnouncementToAllPlayers("Waiting for atleast one more player...");
		}
		else if(totalPlayers>1 && this.playerList.size()<this.MAX_PLAYERS){
			this.makeAnnouncementToAllPlayers("Enough players for starting the game...");
		}
		else if(totalPlayers> this.MAX_PLAYERS){
			this.makeAnnouncementToAllPlayers("More players are not possible, please start the game !");
		}
	}

	public void restartGame() {
		// FIXME assign host as current player
		this.setPlayerTurn(creator);
		
		// create new drawdeck
		this.drawDeck = CardSet.generateBaseDeck();
		Collections.shuffle(drawDeck);
		
		// put one starting card onto the card pile
		Card topCard = drawDeck.drawOne();
		if(topCard.getType() == Type.WILD || topCard.getType() == Type.WILD_DRAW_FOUR) {
			this.makeAnnouncementToAllPlayers("Since wild card is first on pile color is set to green" , TextColor.typeTextColor.MESSAGE);
			topCard.setColor(Color.GREEN);
		}
		this.cardPile.putCardOnTop(topCard);
				
		
		// fill players hands (with 6 cards from the draw deck)
		playersHand.clear();
		for(User currentPlayer : this.playerList) {
			playersHand.put(currentPlayer, drawDeck.draw(7));
		}
		
		for(User currentPlayer : this.playerList) {
			playerScore.put(currentPlayer, 0);
		}
		
		for(Ai ai : this.aiList) {
			ai.setAiCards(drawDeck.draw(7));
		}
		
		this.playerTurn = this.playerList.get(0);
	
		this.makeAnnouncementToAllPlayers("Game started...", TextColor.typeTextColor.MESSAGE);
		this.makeAnnouncementToAllPlayers("First player turn: " + getCurrentPlayerName(), TextColor.typeTextColor.MESSAGE);
	}
	
	/**
	 * Starts new Round and Checks if Player has more Points as Max
	 */
	public void newRound() {
		for(User player : playerList){
			if(playerScore.get(player)>finish){
				winner=player.getName();
				this.makeAnnouncementToAllPlayers("Player "+player.getName()+" won the game", TextColor.typeTextColor.WON);
				sendGameDataToClients("FINISHED");
				this.gState = GameState.FINISHED;
				return;
			}
		}
		
		for(Ai ai : aiList){
			if(ai.getScore()>finish){
				winner=ai.getAiName();
				this.makeAnnouncementToAllPlayers("Player "+ai.getAiName()+" won the game", TextColor.typeTextColor.WON);
				sendGameDataToClients("FINISHED");
				this.gState = GameState.FINISHED;
				return;
			}
		}
		// FIXME assign host as current player
		this.setPlayerTurn(creator);
		
		// create new drawdeck
		this.drawDeck = CardSet.generateBaseDeck();
		Collections.shuffle(drawDeck);
		
		// put one starting card onto the card pile
		Card topCard = drawDeck.drawOne();
		if(topCard.getType() == Type.WILD || topCard.getType() == Type.WILD_DRAW_FOUR) {
			this.makeAnnouncementToAllPlayers("Since wild card is first on pile color is set to green", TextColor.typeTextColor.MESSAGE);
			topCard.setColor(Color.GREEN);
		}
		this.cardPile.putCardOnTop(topCard);
		
		
		// fill players hands (with 6 cards from the draw deck)
		playersHand.clear();
		for(User currentPlayer : this.playerList) {
			playersHand.put(currentPlayer, drawDeck.draw(7));
		}
		
		for(Ai ai : this.aiList) {
			ai.setAiCards(drawDeck.draw(7));
		}
		
		//If Ai play with the Player, Player 1 starts always
		if(!aiList.isEmpty()){
			this.playerTurn = this.playerList.get(0);
			setTurnCounter(0);
		}
		
		this.makeAnnouncementToAllPlayers("New Round started",TextColor.typeTextColor.MESSAGE);
		this.makeAnnouncementToAllPlayers("First player turn: " + getCurrentPlayerName(),TextColor.typeTextColor.MESSAGE);
		lockTurn.clear();
		// send (initial) Data to players
		sendGameDataToClients("DOWNLOAD_DATA");
	}
	
	
	@Override
	public String getSite() {
		try {
			return FileHelper.getFile("Uno/Uno.html");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "<p> HTML File could not be loaded! </p>";
	}

	@Override
	public String getCSS() {
		try {
			return global.FileHelper.getFile("Uno/css/Uno.css");
		} catch (IOException e) {
			System.err.println("Loading of file TicTacToe/css/Uno.css failed");
		}
		return "";
	}

	@Override
	public String getJavaScript() {
		return "<script src='js/Uno.js'></script>";
	}

	@Override
	public int getCurrentPlayerAmount() {
		return playerList.size();
	}
	
	@Override
	public int getMaxPlayerAmount() {
		return MAX_PLAYERS;
	}

	@Override
	public ArrayList<User> getPlayerList() {
		return this.playerList;
	}

	@Override
	public ArrayList<User> getSpectatorList() {
		return this.spectatorList;
	}

	@Override
	public void addUser(User user) {
		try {
			if ((playerList.size()+aiList.size()) < MAX_PLAYERS && !playerList.contains(user) && !spectatorList.contains(user) && gState != GameState.RUNNING) {
				playerList.add(user);
				this.makeAnnouncementToAllPlayers("Player " + user.getName() + " joined game.");
				checkPlayerQuantity();
				buzzerList.put(user, false);
				sendGameDataToClients("UPDATE_PLAYERLIST");
				if(playerList.size() == 1) {
					this.makeAnnouncementToAllPlayers("Waiting for atleast one more player...");
				} else {
					this.makeAnnouncementToAllPlayers("Player " + user.getName() + " joined game.");
				}
			} else {
				if(!playerList.contains(user) && !spectatorList.contains(user)){
					this.addSpectator(user);
				}	
			}
		} catch (Exception e) {
			System.out.println("Error in addUser");
			e.printStackTrace();
		}
	}

	@Override
	public void addSpectator(User user) {
		try {
			this.spectatorList.add(user);
			this.makeAnnouncementToAllPlayers("Spectator " + user.getName() + " joined game.");
			sendGameDataToUser(user, "SPECTATOR");
		} catch (Exception e) {
			System.out.println("Error in addSpectator");
			e.printStackTrace();
		}
	}

	@Override
	public boolean isJoinable() {
		try {
			if (this.getCurrentPlayerAmount() < MAX_PLAYERS && this.gState != GameState.RUNNING) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			
		}
		return false;
	}

	@Override
	public void playerLeft(User user) {
		try {
			if(playerList.contains(user)){
				playerList.remove(user);
				playerLeft = user.getName();
				checkPlayerQuantity();
				this.makeAnnouncementToAllPlayers("Player " + user.getName() + " has left the game.");			
				sendGameDataToClients("UPDATE_PLAYERLIST");	
				sendGameDataToClients("DOWNLOAD_DATA");	
			}else{
				spectatorList.remove(user);
				this.makeAnnouncementToAllPlayers("Spectator " + user.getName() + " has left the game.");
			}
		} catch (Exception e) {
			System.out.println("Error in playerLeft");
			e.printStackTrace();
		}
	}

	@Override
	public GameState getGameState() {
		return this.gState;
	}

	public int getTurnCounter() {
		return turnCounter;
	}

	public void setTurnCounter(int turnCounter) {
		this.turnCounter = turnCounter;
	}

	public void setPlayerTurn(User playerTurn) {
		this.playerTurn = playerTurn;
	}
	
	/**
	 * @return the playerTurn
	 */
	public User getPlayerTurn() {
		return playerTurn;
	}

	/**
	 * @return the aiList
	 */
	public ArrayList<Ai> getAiList() {
		return aiList;
	}

	/**
	 * @param aiList the aiList to set
	 */
	public void setAiList(ArrayList<Ai> aiList) {
		this.aiList = aiList;
	}

	/**
	 * @return the playDirection
	 */
	public int getPlayDirection() {
		return playDirection;
	}

	/**
	 * @param playDirection the playDirection to set
	 */
	public void setPlayDirection(int playDirection) {
		this.playDirection = playDirection;
	}

	/**
	 * @return the cardPile
	 */
	public CardSet getCardPile() {
		return cardPile;
	}

	/**
	 * @param cardPile the cardPile to set
	 */
	public void setCardPile(CardSet cardPile) {
		this.cardPile = cardPile;
	}

	/**
	 * @return the drawDeck
	 */
	public CardSet getDrawDeck() {
		return drawDeck;
	}

	/**
	 * @param drawDeck the drawDeck to set
	 */
	public void setDrawDeck(CardSet drawDeck) {
		this.drawDeck = drawDeck;
	}

	/**
	 * @return the playerPickingWildColor
	 */
	public boolean isPlayerPickingWildColor() {
		return playerPickingWildColor;
	}

	/**
	 * @param playerPickingWildColor the playerPickingWildColor to set
	 */
	public void setPlayerPickingWildColor(boolean playerPickingWildColor) {
		this.playerPickingWildColor = playerPickingWildColor;
	}

	/**
	 * @return the wildCardToBePlayed
	 */
	public Card getWildCardToBePlayed() {
		return wildCardToBePlayed;
	}

	/**
	 * @param wildCardToBePlayed the wildCardToBePlayed to set
	 */
	public void setWildCardToBePlayed(Card wildCardToBePlayed) {
		this.wildCardToBePlayed = wildCardToBePlayed;
	}
}
