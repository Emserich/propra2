var img_index = 1; //ID des ins Spielfeld zu ziehenden Images
var angle = 0;  //Drehwinkel
var mirror = 0; //Spiegelung
var clickLeft = 0; // nach links drehen
var clickRight = 0; // nach rechts drehen
var rotation = 0; // Anzahl Drehung
var rotationSide = ""; //Drehrichtung
var statusWait = true;
var turnCounter = 0;
var diceCounter = 0;
var currentFieldID =0;
var currentDiceImg = "";
var turnData = 	[0, 0, 0, 0];
var stringData = ""; 	
var rollButtonCounter = 0;
var roleValue=[];
var resetImage = false;
var imageDropped = false;
var winnerString = ''; //String des Gewinners, wird vom Server übergeben
var activeImageID = ''; //Hilfsvariable, speichert die in die Rotationsbox geladene Strecke


// ----------------------------- Tooltips initialisieren -----------------------------
$(function () {
  $('[data-toggle="tooltip"]').tooltip()
})
// ----------------------------- /Tooltips initialisieren -----------------------------



// -----------------------------Rotation und Spiegelung-----------------------------

// Würfel in Rotation-Box kopieren
$(document).on("click",".dice_img", function () {

	activeImageID = $(this).attr('id');
	alert(activeImageID);

	if (!$(this).hasClass('img_used')) {

		// $(this).addClass('img_used');

		if ($(this).is('#d2_2') || $(this).is('#d2_5')) {
			$('#mirror_row').show();
		}
		else {
			$('#mirror_row').hide();
		}

		angle = 0;
	  	mirror = 0;

		var img = $('<img />').attr({
	            'id': 'dice_rotated_'+img_index,
	            'class': 'dice_rotated unset',
	            'src': $(this).attr('src'),
	            'draggable':'true'
	        });

		$('#rotation_image').html(img);
		$('.rotation_field').fadeIn( "fast" );
	}
});

//Würfel wieder wählbar machen wenn neu gewürfelt
$(document).on("click","#roll-button", function () {
	if (rollButtonCounter%2==0){
		$('.dice_cube').removeClass("img_used");
	}
	else {
		getRoll();
		
		translateRoll();
		$('#roll-button').hide();
		}
	rollButtonCounter++;	
	
});



// Rotation
$('#rotate_left').on('click', function () {
	angle -= 90;
	$('#dice_rotated_'+img_index).css({'transform': 'rotate('+angle+'deg) '+getMirror()});
});
$('#rotate_right').on('click', function () {
	angle += 90;
	$('#dice_rotated_'+img_index).css({'transform': 'rotate('+angle+'deg) '+getMirror()});
});


// Spiegelung
$('#mirror').on('click', function () {

	switch (mirror) {
	  case 0:
	    $('#dice_rotated_'+img_index).css({'transform': 'rotate('+angle+'deg) scale(1, -1)'});
	    mirror = 1;
	    break;
	  
	  case 1:
	  	$('#dice_rotated_'+img_index).css({'transform': 'rotate('+angle+'deg) scale(1, 1)'});
	    mirror = 0;
	    break;
	}
});

function getMirror() { //Gibt die aktuelle Spiegelung an die Rotation 
	switch (mirror) {
	  case 0:
	  	return 'scale(1, 1)';
	  case 1:
	  	return 'scale(1, -1)';
	}
};
// -----------------------------/Rotation und Spiegelung-----------------------------




// -----------------------------DRAG & DROP-----------------------------
//Kein anderes Image darf draggable sein
$('img').attr('draggable', false);


/* Events fired on the drag target */

document.addEventListener("dragstart", function(event) {
	// The dataTransfer.setData() method sets the data type and the value of the dragged data

	if ( $('#'+event.target.id).hasClass('unset')) {

		event.dataTransfer.setData("Text", event.target.id);
		// Output some text when starting to drag the p element
		// document.getElementById("demo").innerHTML = "Started to drag the p element.";

		// Change the opacity of the draggable element
		event.target.style.opacity = "0.7";
			
	}
	
});
// While dragging the p element, change the color of the output text
document.addEventListener("drag", function(event) {
	// document.getElementById("demo").style.color = "red";
});
// Output some text when finished dragging the p element and reset the opacity
document.addEventListener("dragend", function(event) {
	// document.getElementById("demo").innerHTML = "Finished dragging the p element.";
	event.target.style.opacity = "1";
	$(".field").css("border","");
});

/* Events fired on the drop target */

// When the draggable p element enters the field, change the DIVS's border style
document.addEventListener("dragenter", function(event) {
	if ( event.target.className == "field" ) {
		event.target.style.border = "3px dotted #e85a4d";
		// alert(event.dataTransfer.getData("Text"));
	}
});
// By default, data/elements cannot be dropped in other elements. To allow a drop, we must prevent the default handling of the element
document.addEventListener("dragover", function(event) {
	event.preventDefault();
});
// When the draggable p element leaves the field, reset the DIVS's border style
document.addEventListener("dragleave", function(event) {
	if ( event.target.className == "field" ) {
		event.target.style.border = "";		
	}
});
/* On drop - Prevent the browser default handling of the data (default is open as link on drop)
   Reset the color of the output text and DIV's border color
   Get the dragged data with the dataTransfer.getData() method
   The dragged data is the id of the dragged element ("drag1")
   Append the dragged element into the drop element
*/
document.addEventListener("drop", function(event) {
	event.preventDefault();
	var data = event.dataTransfer.getData("Text");
		currentDiceImg = document.getElementById(data).src.slice(-7); 
		currentFieldID = event.target.id;
		setTurnData();
		updateGameState();
	if ( event.target.className == "field" && $('#'+data).hasClass('unset') ) {
		//checken ob das Element gesetzt werden darf
		
		//validateDrop();
		
		//Validierung findet immer erst nach drop statt
		if (resetImage==false){
			console.log("warum");
		event.target.style.border = "";
		$(".field").css("border","");
		event.target.appendChild(document.getElementById(data));

		$('#'+activeImageID).addClass('img_used'); //Macht die zuletzt in die Rotationsbox geladene Strecke unselectable
		activeImageID = '';
				
		$('#dice_rotated_'+img_index).removeClass("unset");
		$('#dice_rotated_'+img_index).addClass("set");
		$('.set').attr('draggable', false);
		

		$('.rotation_field').hide();
		img_index++; //ID hochzählen
		diceCounter++; //Anzahl benutzter Würfel

		$('#'+event.target.id).prepend('<div class="round_nr">'+turnCounter+'</div>'); //Runde eintragen
		
		//else packe Bild zurück in die Imagebox
		};
	};	
});
// -----------------------------/DRAG & DROP-----------------------------





// -----------------------------Ergebnis-Modal-----------------------------
$(document).on("click","#ergebnis_button", function () {
	$('#modal_finish').modal('toggle')
		
});

$('#modal_finish').on('shown.bs.modal', function () {
	getScore();
	getWinner();	
})



winnerString = "Florian;980;1_2.png,1,-90,1;1_1.png,2,-90,0;1_3.png,3,0,0;l_6.png,9,0,1"; //NUR ZUM TESTEN 


function printWinner() {

	var winnerArray = winnerString.split(';');

	var winnerName = winnerArray[0];
	var winnerPoints = winnerArray[1];

	$('#headingWinner').html('Der Sieger ist <b>'+winnerName+'</b> mit '+winnerPoints+' Punkten:');


	for (var i = 2; i < winnerArray.length; i++) {

		var fieldArray = winnerArray[i].split(',');		

		var image_name = '';
		if (fieldArray[0].charAt(0) == '1' || fieldArray[0].charAt(0) == '2') {
			image_name = 'dice_'+fieldArray[0];
		}
		else if ((fieldArray[0].charAt(0) == 'l')) {
			image_name = 'specia'+fieldArray[0];
		} 
		else {
			alert('Bilddatei mit der Endung '+fieldArray[0]+' nicht gefunden!');
		}

		var field_nr = fieldArray[1];
		var image_angle = fieldArray[2];
		var image_mirror = fieldArray[3];

		// alert('Bild: '+image_name+', Feld: '+field_nr+', Winkel: '+image_angle+', Spiegelung: '+image_mirror);

		var wfield_img = $('<img />').attr({	            
	            'class': 'dice_rotated',
	            'src': 'images/dice/' + image_name,
	            'draggable':'false'
	        });

		if (image_mirror == 1) {
			$(wfield_img).css({'transform': 'rotate('+image_angle+'deg) scale(1, -1)'});
		}
		else {
			$(wfield_img).css({'transform': 'rotate('+image_angle+'deg) scale(1, 1)'});
		}

		$('#result_field_'+field_nr).html(wfield_img);
	}
}
// -----------------------------/Ergebnis-Modal-----------------------------





// ----------------------------- User Buttons -----------------------------
$(document).on("click","#button_finish_round", function () {

	//Fertigstellen des Spielzugs
	turnEnd();
	
});

$(document).on("click","#button_start_game_human", function () {

	//Online-Spiel starten
	startGame();
	$('#button_finish_round').show();

	
});
$(document).on("click","#button_start_game_ki", function () {

	//Spiel gegen KI
	addKI();
	turnCounter = 1;
	document.getElementById('gameround').innerHTML = 'Runde ' +  turnCounter;
	$('#button_finish_round').show();

});

$(document).on("click","#button_restart_game", function () {

	//Spiel neu starten
	alert('Spiel neu starten');
	startGame();

});

$(document).on("click","#button_leave_game", function () {

	//Spiel verlassen
	alert('Spiel verlassen');
	closeGame();
	

});
// ----------------------------- /User Buttons -----------------------------








// -----------------------------BLA-----------------------------
function insertFieldnumber() {

	for (var i = 1; i <= 49; i++) {

		document.getElementById("field_"+i).innerHTML = '<span>'+i+'</span>';
	}    
}

function toggleFieldnumber() {

	for (var i = 1; i <= 49; i++) {

		if (document.getElementById("field_"+i).innerHTML != '<div></div>') {
			document.getElementById("field_"+i).innerHTML = '<div></div>';
		}
		else {
			document.getElementById("field_"+i).innerHTML = '<span>'+i+'</span>';
		}
	}
}
// -----------------------------/BLA-----------------------------




// ----------------------------Hilfsfunktionen-------------------
function setTurnData(){
	turnData[0] = currentDiceImg;
	if (currentFieldID.length == 7){
		turnData[1] = currentFieldID.slice(-1);
	} else {turnData[1] = currentFieldID.slice(-2)};		
	turnData[2] = angle;
	turnData[3] = mirror;
	}
function updateGameState(){
	
	turnDataToString();
	sendDataToServer(stringData);
	console.log(stringData);
}
function turnDataToString(){
	stringData = turnData.join(",");
	stringData = (stringData + ", validateDrop");
	}
function turnEnd(){
	document.getElementById('gameround').innerHTML = 'Runde ' +  turnCounter;
	if (turnCounter > 7) {
		$('#button_finish_round').hide();
	}
	sendDataToServer("END_TURN");
	statusWait = true;
}

function startGame(){
	statusWait = true;
	sendDataToServer("RESTART");
	$('.dice_cube').removeClass("img_used"); //Strecken wieder wählbar machen
	$('.special .dice_image').removeClass("img_used");
}
function getRoll(){
	var diceClass2 = document.getElementsByClassName("die-list even-roll");
	
		for (i=0;i<diceClass2.length;i++){
				roleValue[i] = diceClass2[i].getAttribute("data-roll");
			}	
				
}
function hideButtons(){
	$('#dice_row').hide();	
	$('#roll-button').hide();
	$('#button_finish_round').hide();
	$('#col_restart_game').hide();
	$('#ergebnis_button').hide();
}
function setVisible(){
	$('#col_restart_game').show();
	$('#roll-button').show();
	$('#dice_row').show();
	$('#button_start_game_human').show();
	$('#button_start_game_ki').show();
}
function translateRoll() {
	
	for(i=0;i<3;i++){
		switch (roleValue[i]){
			case "1": roleValue[i] ="1_1.png";
			break;
			case "2":	roleValue[i] ="1_2.png";
			break;
			case "3":	roleValue[i] ="1_3.png";
			break;
			case "4":	roleValue[i] ="1_4.png";
			break;
			case "5":	roleValue[i] ="1_5.png";
			break;
			case "6":	roleValue[i] ="1_6.png";
			break;
		}
	}
		switch (roleValue[3]){
			case "1" : roleValue[3] ="2_1.png";
			break;
			case "2":	roleValue[3] ="2_2.png";
			break;
			case "3":	roleValue[3] ="2_3.png";
			break;
			case "4" : roleValue[3] ="2_3.png";
			break;
			case "5":	roleValue[3] ="2_2.png";
			break;
			case "6":	roleValue[3] ="2_1.png";
			break;
			
	}
	
	var roleValue2 = roleValue.join(",");
	roleValue2 = "ROLL," + roleValue2;
	console.log("translateRoll");
	sendDataToServer(roleValue2);
}

function closeGame(){
	sendDataToServer("CLOSE");
}

function addKI() {
	sendDataToServer("ADD_KI");
	document.getElementById("Player").innerHTML = "KI wurde hinzugefügt";
}
function getScore(){
	console.log("score");
	sendDataToServer("myScore");	
}
function getWinner(){
	console.log("winner");
	sendDataToServer("winnerData");
}
/*
function validateDrop(){
	sendDataToServer("validateDrop");
	imageDropped = false;
	imageReset = false;		
}
*/
// ---------------------------/Hilfsfunktionen-------------------


// ----------------------------EventListener---------------------
addListener('EndOfGame', function(event) {
var stringFromServer = event.data;
	playerMessage = stringFromServer;
	document.getElementById("Player").innerHTML = playerMessage;
	$('#ergebnis_button').show;
	$('#modal_finish').modal('toggle');
});

addListener('winnerData', function(event){
	var myArray = [];
	var dummyString = "result_field_";
	var stringFromServer = event.data;
	var score = stringFromServer.split(';'); 
	for (i=2; i<51; i++){  //weil die ersten beiden elemente user + score sind
		myArray = score[i].split(",");
		dummyString += i;
		/*
		img = myArray[0];
		field = myArray[1];
		rotation = myArray[2];
		mirror = myArray[3];
		*/
		//draw board
		dummyString -= i;
	}
});
addListener('myScore', function(event) {
	var stringFromServer = event.data;
	var score = stringFromServer.split(',');
	document.getElementById("score_1").innerHTML = score[0];
	document.getElementById("score_2").innerHTML = score[1];
	document.getElementById("score_3").innerHTML = score[2];
	document.getElementById("score_4").innerHTML = score[3];
	document.getElementById("score_5").innerHTML = score[4];
	var scoreFinal = score[5];
	document.getElementById("score_final").innerHTML = scoreFinal;
	
});
addListener('NEW_PLAYER', function(event){
	document.getElementById("Player").innerHTML = "Ein neuer Spieler ist beigetreten!";
});

addListener('EndOfTurn', function(event) {
		turnCounter +=1;
		if (turnCounter > 7) {
			$('#button_finish_round').hide();
		}
		$('.dice_cube').removeClass("img_used"); //Strecken wieder wählbar machen
		activeImageID = '';
		var stringFromServer = event.data;
		arr = stringFromServer.split(",");
		console.log("standardEvent angekommen");
		console.log(arr[1]);
		if (arr[1] == "HOST"){setVisible()};

			diceCounter = 0;	
			turnData = [0, 0, 0, 0];
 			stringData = "";				
			rollButtonCounter = 0;
			roleValue = [0,0,0,0];		
			document.getElementById("Player").innerHTML = "Runde " + turnCounter + " wurde gestartet!";
			
		statusWait = false;
	});

//START wird ausgelöst wenn ein Spiel erstellt wird,
// aber noch nicht genügend Spieler da sind
addListener('START', function(event){
	var stringFromServer = event.data;
	var arr = stringFromServer.split(',');
	playerMessage = arr[1];
	document.getElementById("Player").innerHTML = playerMessage;
	turnCounter++;
	document.getElementById('gameround').innerHTML = 'Runde ' +  turnCounter;
	$('.dice_cube').removeClass("img_used"); //Strecken wieder wählbar machen
	$('.special .dice_image').removeClass("img_used");
	if(arr[2]=="HOST") setVisible();
	statusWait = false;	
});
addListener('START_KI', function(event){
	var stringFromServer = event.data;
	var arr = stringFromServer.split(',');
	playerMessage = arr[1];
	document.getElementById("Player").innerHTML = playerMessage;
	$('.dice_cube').removeClass("img_used"); //Strecken wieder wählbar machen
	$('.special .dice_image').removeClass("img_used");
	if(arr[2]=="HOST") setVisible();
	console.log(arr[2]);
	statusWait = false;	
});

//PLAYERLEFT wird ausgelöst wenn ein Spieler auf "Spiel verlassen" klickt
addListener('PLAYERLEFT', function(event){
	var stringFromServer = event.data;
	playerMessage = stringFromServer;
	document.getElementById("Player").innerHTML = playerMessage;
});
//CLOSE wird ausgelöst wenn der Host das Spiel per "Spiel schließen" Button beendet
addListener('CLOSE', function(event){
	document.getElementById("Player").innerHTML = "Spiel wurde vom Host beendet!";
	window.location = "/index"; //hier muss man zum eig Start weitergeleitet werden.
	eventSource.close();
});
addListener('thisRoll', function(event){
	var arrarr = [];
	var stringFromServer = event.data;
	var arr = stringFromServer.split(",");
	console.log(arr);
	for (i=1; i<(arr.length-1); i++){
		arrarr[i] = arr[i].charAt(2);
	}
	var diceClass = document.getElementsByClassName("die-list even-roll");
	console.log(diceClass.length);
	
		for (i=0;i<4;i++){
				diceClass[i].setAttribute("data-roll", arrarr[i]);
			}	
	if (arr[5] == "NOTTHEHOST"){
	$('#dice_row').show();
	}
});
addListener('WrongField',function(event){
	alert('Das Element darf hier nicht platziert werden');
	resetImage = true;
	console.log("in WrongField" + resetImage);
});

addListener('CANT_END_TURN',function(event){
	console.log("cant end");
	alert('Es müssen weitere Elemente gesetzt werden!');
});
addListener('dropped',function(event){
	document.getElementById("Player").innerHTML = "Element gesetzt!"
	imageDropped = true;
})
// ---------------------------/EventListener---------------------
