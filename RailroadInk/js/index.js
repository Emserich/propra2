function insertFieldnumber() {

	for (var i = 1; i <= 49; i++) {
		document.getElementById(i).innerHTML = i;		
	}    
}

function removeFieldnumber() {

	for (var i = 1; i <= 49; i++) {
		document.getElementById(i).innerHTML = '';		
	}    
}