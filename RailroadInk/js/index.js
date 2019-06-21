function insertFieldnumber() {

	for (var i = 1; i <= 49; i++) {

		// document.getElementById("field_"+i).innerHTML = '<span>'+i+'</span>';
	}    
}

function toogleFieldnumber() {

	for (var i = 1; i <= 49; i++) {
		if (document.getElementById("field_"+i).innerHTML != '') {
			document.getElementById("field_"+i).innerHTML = '';
		}
		else {
			document.getElementById("field_"+i).innerHTML = i;
		}		
	}
}


function toogleMarker(id) {

	if (id<=49 && id>0) {
		document.getElementById("field_"+id).innerHTML = '<div>&#10003;</div>'; //aktuelle Rundenzahl muss eingetragen werden
	}
	else {
		alert("Feld existiert nicht!");
	}

}