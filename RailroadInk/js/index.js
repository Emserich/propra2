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


// ----------------------------- Tooltips initialisieren -----------------------------
$(function () {
  $('[data-toggle="tooltip"]').tooltip()
})
// ----------------------------- /Tooltips initialisieren -----------------------------




// -----------------------------Rotation und Spiegelung-----------------------------

// Würfel in Rotation-Box kopieren
$(document).on("click",".dice_img", function () {
	// $('#rotation_image').html($(this));

	if (!$(this).hasClass('img_used')) {

		$(this).addClass('img_used');

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

	if ( event.target.className == "field" && $('#'+data).hasClass('unset') ) {
		// document.getElementById("demo").style.color = "";

		// $('#'+event.target.id).append('<div class="round_nr">&#10003;</div>');
		document.getElementById("Player").innerHTML = event.target.id //nur zum Test
		event.target.style.border = "";
		$(".field").css("border","");
		event.target.appendChild(document.getElementById(data));
		//Bild des benutzen Würfels
		currentDiceImg = document.getElementById(data).src.slice(-7); 
		currentFieldID = event.target.id;
		
		setTurnData();
		updateGameState();
		
		$('#dice_rotated_'+img_index).removeClass("unset");
		$('#dice_rotated_'+img_index).addClass("set");
		$('.set').attr('draggable', false);
		

		$('.rotation_field').hide();
		img_index++; //ID hochzählen
		diceCounter++; //Anzahl benutzter Würfel

		$('#'+event.target.id).prepend('<div class="round_nr">'+turnCounter+'</div>'); //Runde eintragen
	
	}
});
// -----------------------------/DRAG & DROP-----------------------------





// -----------------------------Ergebnis-Modal-----------------------------
$(document).on("click","#ergebnis_button", function () {
	$('#modal_finish').modal('toggle')
});

$('#modal_finish').on('shown.bs.modal', function () {
})

var field_winner = [
  	[
		"1", //Feldnummer
		"90", //Winkel
		"0", //Spiegelung
		"dice_1_2" // Bildname
	],

	[
		"5", //Feldnummer
		"-90", //Winkel
		"0", //Spiegelung
		"dice_1_1" // Bildname
	],

	[
		"49", //Feldnummer
		"-180", //Winkel
		"0", //Spiegelung
		"special_1" // Bildname
	]

];


function printFields (array) {

	var field_nr = 0;
	var image_angle = 0;
	var image_mirror = 0
	var image_name = "";


	for (var i = 0; i < array.length; i++) {

		field_nr = array[i][0];
		image_angle = array[i][1];
		image_mirror = array[i][2];
		image_name = array[i][3];

		var wfield_img = $('<img />').attr({	            
	            'class': 'dice_rotated',
	            'src': 'images/dice/' + image_name + '.png',
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
	//unten als onCLick-function  

});

$(document).on("click","#button_start_game_human", function () {

	//Online-Spiel starten
	startGame();
	turnCounter = 1;
	document.getElementById('gameround').innerHTML = 'Runde ' +  turnCounter;

});
$(document).on("click","#button_start_game_ki", function () {

	//Spiel gegen KI
	addKI();
	startGame();
	turnCounter = 1;
	document.getElementById('gameround').innerHTML = 'Runde ' +  turnCounter;

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
	statusWait = true;
	turnDataToString();
	sendDataToServer(stringData);
}
function turnDataToString(){
	stringData = turnData.join(",");
	}
function turnEnd(){
	turnCounter +=1;
	document.getElementById('gameround').innerHTML = 'Runde ' +  turnCounter;
	sendDataToServer("END_TURN");
}
/*
function restart(){
	statusWait = true;
	sendDataToServer("RESTART");
} */
function startGame(){
	statusWait = true;
	console.log("Game start");
	sendDataToServer("RESTART");
}
function getRoll(){
	var diceClass = document.getElementsByClassName("die-list even-roll");
	
		for (i=0;i<diceClass.length;i++){
				roleValue[i] = diceClass[i].getAttribute("data-roll");
			}	
		translateRoll();		
}
function hideButtons(){
	$('#col_restart_game').hide();
	$('#roll-button').hide();
	// $('#ergebnis_button').hide();
}
function setVisible(){
	$('#col_restart_game').show();
	$('#roll-button').show();

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
			case "1" : roleValue[3] ="l_1.png";
			break;
			case "2":	roleValue[3] ="l_2.png";
			break;
			case "3":	roleValue[3] ="l_3.png";
			break;
			case "4":	roleValue[3] ="l_4.png";
			break;
			case "5":	roleValue[3] ="l_5.png";
			break;
			case "6":	roleValue[3] ="l_6.png";
			break;
	}
	
	var roleValue2 = roleValue.join(",");
	sendDataToServer(roleValue2);
}

function closeGame(){
	sendDataToServer("CLOSE");
}

function addKi() {
	sendDataToServer("ADD_KI");
	
}
function drawWinnerBoard(){
	for (i=1; i<=49 ; i++){
	// do stuff
	}
}
// ---------------------------/Hilfsfunktionen-------------------




// ----------------------------EventListener---------------------
addListener('gameEnd', function(event) {
var stringFromServer = event.data;
	playerMessage = stringFromServer;
	document.getElementById("Player").innerHTML = playerMessage;
});

addListener('resultData', function(event){
	// do stuff
})
addListener('NEW_PLAYER', function(event){
	document.getElementById("Player").innerHTML = "Ein neuer Spieler ist beigetreten!";
});
addListener('standardEvent', function(event) {
		var stringFromServer = event.data;
		var arr = stringFromServer.split(',');
		console.log(arr);
		console.log("standardEvent angekommen");
		playerMessage = arr[2];

			diceCounter = 0;	
			turnData = [0, 0, 0, 0];
 			stringData = "";				
			rollButtonCounter = 0;
			roleValue = [0,0,0,0];
			document.getElementById("Player").innerHTML = playerMessage;
			
		statusWait = false;
	});
//START wird ausgelöst wenn ein Spiel erstellt wird,
// aber noch nicht genügend Spieler da sind
addListener('START', function(event){
	var stringFromServer = event.data;
	var arr = stringFromServer.split(',');
	console.log(arr.length);
	playerMessage = arr[1];
	document.getElementById("Player").innerHTML = playerMessage;
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
});
// ---------------------------/EventListener---------------------
