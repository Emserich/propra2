/*									*
 *  +++++++++ Variables +++++++++	*
 *									*/								

 /*
	@author: Raphael Dobesch
	
	Javascript file for the GUI
 */

let cardImgBasePath = "/Uno/images/";
let isInitiated = false

/*									*
 *  +++++++++ Listeners +++++++++	*
 *									*/	

 
 /*
	GUI javascript file
 
	@author: Raphael Dobesch

	Listener:
		- CLEAR_CENTER 			-> Clear center info after restart
		- SPECTATOR				-> for spectatormode
		- FINISHED				-> the end of the game
		- CHOOSE_COLOR			-> for the WILD Card choosing the color
		- UPDATE_PLAYERLIST		-> Like a start event for the lobby mode
		- ANNOUNCEMENT			-> sets the infos from server to the history or the center
		- DOWNLOAD_DATA			-> refresh the game
	
*/	
addListener('CLEAR_CENTER', function(event){
	function clearCenterInformation(){
		deleteFromContainer(centerGameInformation);
	}
});  
 
addListener('SPECTATOR', function(event){
	document.getElementById("spectatorClearButton").style.display="block";
	document.getElementById("spectatorClearButton").style.cssFloat="right";
}); 
 
addListener('FINISHED', function(event){
	// Set buttons
	document.getElementById("startButton").style.display="none";
	document.getElementById("buzzerButton").style.display="none";
	document.getElementById("startButton").style.display="inline-block";
	document.getElementById("aiButton").style.display="inline-block";
	
	let playerHand 			= document.getElementById("playerCardRow");
	let depositCardStack 	= document.getElementById("depositCardStack");
	
	// clear players hand (move this elsewhere!)
	deleteFromContainer(playerHand);
	// clear depositCardStack
	deleteFromContainer(depositCardStack);
	// clear players
	deleteFromContainer(otherPlayer);
	
	let info  ='<p class="informationElement">';
		info += event.data;
		info +='</p>';
	
	printToCenterGameInformation(info);
	
}); 
 
addListener('CHOOSE_COLOR', function(event){
	let	info ='<p class="informationElement"> Bitte waehlen Sie eine Karte..... </p>';
	
	let	colorButton ='<p class="informationElement">';
		colorButton	+='<button id="chooseColorBlue" class="chooseColorButton" type="button" onClick="chooseColorBlue()">Blau</button>';		
		colorButton +='<button id="chooseColorRed" class="chooseColorButton" type="button" onClick="chooseColorRed()">Rot</button>';	
		colorButton +='<button id="chooseColorYellow" class="chooseColorButton" type="button" onClick="chooseColorYellow()">Gelb</button>';
		colorButton +='<button id="chooseColorGreen" class="chooseColorButton" type="button" onClick="chooseColorGreen()">Gruen</button>';
		colorButton +='</p>';
		
		info += colorButton;
		
		printToCenterGameInformation(info);
		
});

addListener('UPDATE_PLAYERLIST', function(event){
	
	
	statusWait = true;
	console.log(document.getElementById("gameTableContent").firstChild);
	// show gameTableContent
	document.getElementById("gameTableContent").style.display="block";
	document.getElementById("gameTableContent").style.cssFloat="left";
	document.getElementById("gameTableHistory").style.cssFloat="right";
	
	// show buttons
	document.getElementById("startButton").style.display="inline-block";
	document.getElementById("aiButton").style.display="inline-block";
	document.getElementById("clearButton").style.display="inline-block";
	document.getElementById("clearButton").style.cssFloat="right";
	document.getElementById("clearButton").style.marginRight="0px";
	document.getElementById("spectatorClearButton").style.display="none";
	
	let playerList			= JSON.parse(event.data);
	// prepare/parse the fields into useful data
	let ownPlayer		= document.getElementById("ownPlayer");
	let otherPlayer		= document.getElementById("otherPlayer");

	// Delete players
	deleteFromContainer(ownPlayer);
	deleteFromContainer(otherPlayer);
	
	// Update players
	for(let i=0;i<playerList.length;i++) {
		if(i==0){
			addPlayerToContainer(ownPlayer, playerList[i]);
		}else
			addPlayerToContainer(otherPlayer, playerList[i]);
	}

	statusWait = false;
});

addListener('ANNOUNCEMENT', function(event){
	// Adds information to the history
	let announcementString = event.data;
	printToHistory(announcementString);
});

addListener('DOWNLOAD_DATA', function(event) {

	statusWait = true;
	// Parse server string
	let asJson = JSON.parse(event.data);
	// prepare/parse the fields into useful data
	let topPileCard = asJson.topPileCard;
	let currentPlayerCards 	= asJson.currentPlayerCards;
	let otherPlayerCards 	= Object.entries(asJson.otherPlayerCardCounts);
	let playerTurnName		= asJson.playerTurnName;
		playerTurnName	= "player-"+playerTurnName;
		console.log(playerTurnName);
		
	// get containers
	let playerHand 			= document.getElementById("playerCardRow");
	let depositCardStack 	= document.getElementById("depositCardStack");
	let ownPlayer 			= document.getElementById("ownPlayer");
	let otherPlayer 		= document.getElementById("otherPlayer");
	let playerList			= document.getElementsByClassName("player");

	// clear players hand (move this elsewhere!)
	deleteFromContainer(playerHand);
	// clear depositCardStack
	deleteFromContainer(depositCardStack);
	// clear players
	deleteFromContainer(otherPlayer);
	
	// add cards to players hand
	for(let card of currentPlayerCards) {
		addCardImageToContainer(playerHand, card, true);
	}
	// add topPileCard
	addCardImageToContainer(depositCardStack, topPileCard, false);
	// Add new list with CardCounts
	for(let i = 0;i<otherPlayerCards.length;i++){
		let playerName 					= otherPlayerCards[i][0];
		let playerCardCount 			= otherPlayerCards[i][1];
		
		addPlayerToContainer(otherPlayer, playerName);
		let playerContainer				= document.getElementById("player-"+playerName);
			playerContainer.innerHTML   += "<p>"+playerCardCount+"</p>";	
	}
	
	// Set the playerTurn
	for(let i = 0;i<playerList.length;i++){
		playerList[i].style.background="grey";
		
		if(playerList[i].id==playerTurnName){
			playerList[i].style.background="lightgreen";
		}
	}
	
	// Hide accept button
	document.getElementById("startButton").style.display="none";
	document.getElementById("aiButton").style.display="none";
	document.getElementById("buzzerButton").style.display="inline-block";	
	document.getElementById("spectatorClearButton").style.display="none";
	
	statusWait = false;
});

/*										*
 *  +++++++++ HelpFunctions +++++++++	*
 *										*/	

/*
	Functions for adding players to the gui or announcements to the HTML container
	or just to send input/data to the server

	@author Raphael Dobesch
*/

function printToHistory(info){
	var infoText                = document.createElement("p");
		infoText.className      = "informationElement";
		infoText.innerHTML		= info;
	
	var container				= document.getElementById("gameTableHistory");
	
	if(container.firstChild){
		container.insertBefore(infoText, container.firstChild);	
	}else{
		container.appendChild(infoText);
	}
	printToGameInformation(infoText.innerHTML);
}

function printToGameInformation(info){
	var container				= document.getElementById("gameInformation");
		container.innerHTML		= "<p class'informationElement'>"+info+"</p>";
}
function printToCenterGameInformation(info){
	var container				= document.getElementById("centerGameInformation");
	container.innerHTML			= info;	
}

function addPlayerToContainer(container, player){
	// Create new player
	var tempPlayer             	  	= 	document.createElement("div");
		tempPlayer.className        =   "player";
		tempPlayer.id       		 =   "player-"+player;
		tempPlayer.innerHTML		=	"<p>"+player+"</p>";
		
		
	// add player to panel
	container.appendChild(tempPlayer);
}

function addCardImageToContainer(container, card, clickable) {
	var img                 = document.createElement("img");
		img.src             = getCardAdress(card);
		img.className       = "card";
	
	// set id correctly, need for later
	if(card) img.id = card.id;
	else img.id = "hiddenCard";
	
	if(clickable) {
		img.onclick = function(){
			if(statusWait==false) {
				pickCard(this.id)
			}
		};
	}

	container.appendChild(img);
}

function getCardAdress(card) {
	// back of the card
	if(card === null)                  return cardImgBasePath + "card_back_large.png"
	// wild cards
	if(card.type === "WILD")           return cardImgBasePath + "wild_colora_changer_large.png"
	if(card.type === "WILD_DRAW_FOUR") return cardImgBasePath + "wild_pick_four_large.png"
	
	// colored action cards
	if(card.type === "SKIP")           return cardImgBasePath + card.color.toLowerCase() + "_skip_large.png"
	if(card.type === "DRAW_TWO")       return cardImgBasePath + card.color.toLowerCase() + "_picker_large.png"
	if(card.type === "REVERSE")        return cardImgBasePath + card.color.toLowerCase() + "_reverse_large.png"
	
	if(card.type === "NORMAL")         return cardImgBasePath + card.color.toLowerCase() + "_" + card.score + "_large.png"
	
	alert("Could not render card" + JSON.stringify(card))
	return cardImgBasePath + "card_back_large.png"
}


function deleteFromContainer(container){
	while(container.firstChild) {
		container.removeChild(container.firstChild);
	}
}
function clearCenterInformation(){
	deleteFromContainer(centerGameInformation);
}
function clearHistoryLog(){
	deleteFromContainer(gameTableHistory);
}
function buzzer(){
	console.log("You hit the buzzer");
	sendDataToServer("BUZZER");
}
function pickCard(cardId) {
	sendDataToServer(cardId);
}
function startGame() {
	sendDataToServer("RESTART");
}
function addAi() {
	sendDataToServer("ADD_AI");
}
function drawCard(){
	sendDataToServer("DRAW_CARD");
}

function chooseColorBlue(){
	sendDataToServer("CHOOSE_COLOR_BLUE");
	clearCenterInformation();
}

function chooseColorRed(){
	sendDataToServer("CHOOSE_COLOR_RED");
	clearCenterInformation();
}
function chooseColorYellow(){
	sendDataToServer("CHOOSE_COLOR_YELLOW");
	clearCenterInformation();
}
function chooseColorGreen(){
	sendDataToServer("CHOOSE_COLOR_GREEN");
	clearCenterInformation();

}

