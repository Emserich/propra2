function insertFieldnumber() {

	for (var i = 1; i <= 49; i++) {

		document.getElementById("field_"+i).innerHTML = ''+i;
	}    
}

function toogleFieldnumber() {

	for (var i = 1; i <= 49; i++) {
		if (document.getElementById("field_"+i).innerHTML != '') {
			document.getElementById("field_"+i).innerHTML = '';
		}
		else {
			document.getElementById("field_"+i).innerHTML = ''+i;
		}
		
	}    
}