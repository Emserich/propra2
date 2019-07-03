var img_index = 1; //ID des ins Spielfeld zu ziehenden Images
var angle = 0;  //Drehwinkel
var mirror = 0; //Spiegelung
var clickLeft = 0; // nach links drehen
var clickRight = 0; // nach rechts drehen
var rotation = 0; // Anzahl Drehung
var rotationSide = ""; //Drehrichtung
var statusWait = true;


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
	
		$('.dice_cube').removeClass("img_used");
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
	    $('#dice_rotated_'+img_index).css({'transform': 'rotate('+angle+'deg) scale(-1, 1)'});
	    mirror = 1;
	    break;
	  case 1:
	    $('#dice_rotated_'+img_index).css({'transform': 'rotate('+angle+'deg) scale(-1, -1)'});
	    mirror = 2;
	    break;
	  case 2:
	  	$('#dice_rotated_'+img_index).css({'transform': 'rotate('+angle+'deg) scale(1, -1)'});
	    mirror =3;
	    break;
	  case 3:
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
	  	return 'scale(-1, 1)';
	  case 2:
	  	return 'scale(-1, -1)';
	  case 3:
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
		
		event.target.style.border = "";
		$(".field").css("border","");
		event.target.appendChild(document.getElementById(data));
		document.getElementById("Player").innerHTML = document.getElementById(data).src;
		$('#dice_rotated_'+img_index).removeClass("unset");
		$('#dice_rotated_'+img_index).addClass("set");
		$('.set').attr('draggable', false);
		

		$('.rotation_field').hide();
		img_index++; //ID hochzählen

		$('#'+event.target.id).prepend('<div class="round_nr">5</div>'); //Runde eintragen
	
	}
});
// -----------------------------/DRAG & DROP-----------------------------


// ----------------------------EventListener---------------------
addListener('turnEnd', function(event) {
		var stringFromServer = event.data;
		var arr = stringFromServer.split(',');
		//console.log(arr);
		
		if(arr.length==11){
			for(var i=0; i<9; i++) { arrFields[i] = +arr[i]; }
			playerMessage = arr[9];
			var str = arr[10];
			if(str=="HOST"){
				console.log(arr[10]);
				setVisible();
			}
				
			
			
			
			document.getElementById("Player").innerHTML = playerMessage;
			redraw();
		}
		statusWait = false;
	});
//START wird ausgelöst wenn ein Spiel erstellt wird,
// aber noch nicht genügend Spieler da sind
addEventListener('START', function(event){
	var stringFromServer = event.data;
	var arr = stringFromServer.split(',');
	playerMessage = arr[9];
	document.getElementById("Player").innerHTML = playerMessage;
	if(arr[10]=="HOST") setVisible();
	statusWait = false;
});

//PLAYERLEFT wird ausgelöst wenn ein Spieler auf "Spiel verlassen" klickt
addEventListener('PLAYERLEFT', function(event){
	var stringFromServer = event.data;
	playerMessage = stringFromServer;
	document.getElementById("Player").innerHTML = playerMessage;
});
//CLOSE wird ausgelöst wenn der Host das Spiel per "Spiel schließen" Button beendet
addEventListener('CLOSE', function(event){
	document.getElementById("Player").innerHTML = "Spiel wurde vom Host beendet!";
});
// ---------------------------/EventListener---------------------




// -----------------------------Ergebnis-Modal-----------------------------
$(document).on("click","#ergebnis_button", function () {
	$('#modal_finish').modal('toggle')
});
// -----------------------------/Ergebnis-Modal-----------------------------





// ----------------------------- User Buttons -----------------------------
$(document).on("click","#button_finish_round", function () {

	//Fertigstellen des Spielzugs
	alert('Fertigstellen des Spielzugs');

});

$(document).on("click","#button_restart_game", function () {

	//Spiel neu starten
	alert('Spiel neu starten');

});

$(document).on("click","#button_leave_game", function () {

	//Spiel verlassen
	alert('Spiel verlassen');

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

	function updateGameState(){
		statusWait = true;
		sendDataToServer(sentFields);
	}
	
	function restart(){
			statusWait = true;
			sendDataToServer("RESTART");
	}

	function setVisible(){
		document.getElementById("restartButton").style.visibility ="visible";	
		document.getElementById("closeButton").style.visibility ="visible";
	}

	function closeGame(){
		sendDataToServer("CLOSE");
	}
	function countLeft(){
		clickLeft+=1;
	}
	function countRight(){
		clickRight+=1;
	}
	function getRotation(){
		if (clickRight > clickLeft){
			
				rotation = clickRight - clickLeft;
			 	}
				 else if (clickLeft > clickRight){
					
					 rotation = clickLeft - clickRight;
				 }
				 else {
					rotation = 0;
					};
					return rotation;
	}
	function getRotationSide(){
			if (clickRight > clickLeft){
				 rotationSide = "r";
				 	}
				 else if (clickLeft > clickRight){
					 rotationSide ="l";
				}
				 else {rotationSide ="";	
				 };
					return rotationSide; 
	}
// ---------------------------/Hilfsfunktionen-------------------
