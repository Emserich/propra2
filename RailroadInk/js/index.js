var img_index = 1; //ID des ins Spielfeld zu ziehenden Images
var angle = 0;  //Drehwinkel
var mirror = 0; //Spiegelung




// -----------------------------Rotation und Spiegelung-----------------------------

// Würfel in Rotation-Box kopieren
$(document).on("click",".dice_img", function () {
	// $('#rotation_image').html($(this));

	angle = 0;
  	mirror = 0;

	var img = $('<img />').attr({
            'id': 'dice_rotated_'+img_index,
            'src': $(this).attr('src'),
            'draggable':'true',
            'ondragstart': 'drag(event)'
        });

	$('#rotation_image').html(img);
	$('.rotation_field').fadeIn( "fast" );
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



// -----------------------------DRAG & DROP-----------------------------
function allowDrop(ev) {
  ev.preventDefault();
  // $(ev.target).hover().css("background", 'rgba(0,0,0,0.1');
  // $(ev.target).mouseleave().css("background", 'rgba(0,0,0,1');
}

function drag(ev) {
  ev.dataTransfer.setData("text", ev.target.id);
}

function drop(ev) {
  ev.preventDefault();
  var data = ev.dataTransfer.getData("text");
  ev.target.appendChild(document.getElementById(data));
  $('.rotation_field').hide();

  img_index++;
}










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


function setMarker(id) {

	if (id<=49 && id>0) {
		document.getElementById("field_"+id).innerHTML = '<div>&#10003;</div>'; //aktuelle Rundenzahl muss eingetragen werden
	}
	else {
		alert("Feld existiert nicht!");
	}

}