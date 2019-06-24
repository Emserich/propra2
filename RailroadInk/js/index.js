function insertFieldnumber() {

	for (var i = 1; i <= 49; i++) {

		document.getElementById("field_"+i).innerHTML = '<span>'+i+'</span>';
	}    
}

function toogleFieldnumber() {

	for (var i = 1; i <= 49; i++) {


		// if( $('#field_'+i).is(':empty') ) {

		// 	$('#field_'+i).append('<span>'+i+'</span>');
		// } 
		// else {
		// 	$('#field_'+i).empty();
		// }


		if (document.getElementById("field_"+i).innerHTML != '<div></div>') {
			document.getElementById("field_"+i).innerHTML = '<div></div>';
		}
		else {
			document.getElementById("field_"+i).innerHTML = '<span>'+i+'</span>';
		}
	}
}


function toogleMarker(id) {

	// if (id<=49 && id>0) {
	// 	$('#field_'+id).html('<div>&#10003;</div>'); 
	// }
	// else {
	// 	alert("Feld existiert nicht!");
	// }


	if (id<=49 && id>0) {
		document.getElementById("field_"+id).innerHTML = '<div>&#10003;</div>'; //aktuelle Rundenzahl muss eingetragen werden
	}
	else {
		alert("Feld existiert nicht!");
	}

}


// -----------------------------DRAG&DROP-----------------------------

function allowDrop(ev) {
  ev.preventDefault();
}

function drag(ev) {
  ev.dataTransfer.setData("text", ev.target.id);
}

function drop(ev) {
  ev.preventDefault();
  var data = ev.dataTransfer.getData("text");
  ev.target.appendChild(document.getElementById(data));
}