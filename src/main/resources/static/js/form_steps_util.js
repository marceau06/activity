/***
 * Code from w3schools.com 
 * How TO - Form with Multiple Steps
 * source : https://www.w3schools.com/howto/howto_js_form_steps.asp
 ***/

var currentTab = 0; // Current tab is set to be the first tab (0)

function showTab(n) {
	
	// This function will display the specified tab of the form ...
	var x = document.getElementsByClassName("tab");
	x[n].style.display = "flex";
	x[n].style.flexDirection = "row";
	x[n].style.justifyContent = "space-around";

	// ... and fix the Previous/Next buttons:
	if (n == 0) {
		document.getElementById("prevBtn").style.display = "none";
		x[n].style.flexDirection = "column";
	} else {
		document.getElementById("prevBtn").style.display = "inline";
		x[n].style.flexDirection = "row";
	}
	if (n == (x.length - 1)) {
		document.getElementById("nextBtn").innerHTML = "Envoyer";
	} else {
		document.getElementById("nextBtn").innerHTML = "Next";
	}
	// ... and run a function that displays the correct step indicator:
	fixStepIndicator(n)
}

//Efface les inputs renseignées et affiche les suivantes
function nextPrev(n) {
	// This function will figure out which tab to display
	var x = document.getElementsByClassName("tab");
	// Exit the function if any field in the current tab is invalid:
	if (n == 1 && !validateForm()) {
		return false;
	}
	// Hide the current tab:
	x[currentTab].style.display = "none";
	// Increase or decrease the current tab by 1:
	currentTab = currentTab + n;
	// if you have reached the end of the form... :
	if (currentTab >= x.length) {
		//...the form gets submitted:
		document.getElementById("nextBtn").type = "submit";
		return false;
	}
	// Otherwise, display the correct tab:
	showTab(currentTab);
}


// Add conditions to validate activitie's attributes dynamically
function validateForm() {
	// This function deals with validation of the form fields
	var x, y, z, i, valid = true;
	x = document.getElementsByClassName("tab");
	y = x[currentTab].getElementsByTagName("input");
	z = x[currentTab].getElementsByTagName("textarea");
	// A loop that checks every input field in the current tab:
	for (i = 0; i < y.length; i++) {
		
		// If a field is empty...
		if (y[i].value == "" || y[i].value.length < 2) {
			// add an "invalid" class to the field:
			y[i].className += " invalid";
			// and set the current valid status to false:
			valid = false;
		}
		if (z[i] != null) {
			if (z[i].value == "" || z[i].value.length < 2 || z[i].value.length > 1000) {
				// add an "invalid" class to the field:
				z[i].className += " invalid";
				z[i].placeholder += "Vous devez "
				// and set the current valid status to false:
				valid = false;
			}
		}
	}
	// If the valid status is true, mark the step as finished and valid:
	if (valid) {
		document.getElementsByClassName("step")[currentTab].className += " finish";
	}
	return valid; // return the valid status
}

function fixStepIndicator(n) {
	// This function removes the "active" class of all steps...
	var i, x = document.getElementsByClassName("step");
	for (i = 0; i < x.length; i++) {
		x[i].className = x[i].className.replace(" active", "");
	}
	//... and adds the "active" class to the current step:
	x[n].className += " active";
}

function displayImg() {
	
	// Afficher l'image que l'on vient d'upload
	var preview = document.getElementById("imgDisplay");
	var file    = document.querySelector('input[type=file]').files[0]; 
	var reader  = new FileReader();

	reader.onloadend = function () {
		preview.src = reader.result;
	}
	if (file) {
		reader.readAsDataURL(file); 
	} else {
		preview.src = "";
	}
}

// Retourne le nombre de nuits entre 2 dates
function getNbNightsBetweenTwoDates(startDate, endDate) {
	return Math.round(Math.abs((endDate - startDate) / (24 * 60 * 60 * 1000)));
}

// Change la date de retour
function changeReturnDate() {
	var date = jQuery("#beginDateText").datepicker('getDate');
	if (date && date.getDate) {
		date.setDate(date.getDate() + 1);
		jQuery("#endDateText").datepicker('option', 'minDate', date);
		jQuery("#endDateText").datepicker('setDate', date);
	}
}

//Change l'heure de retour
function changeReturnTime() {
	var beginHour = jQuery("#beginHour").val();
	if (beginHour) {
		jQuery("#endHour").val(parseInt(beginHour) + 60);
	}
}

// Remplit un select avec des heures
function populate(selector) {
	var select = jQuery(selector);
	var hours, minutes, ampm;
	for(var i = 0; i <= 1320; i += 30){
		hours = Math.floor(i / 60);
		minutes = i % 60;
		if (minutes < 10){
			minutes = '0' + minutes; // adding leading zero
		}
		ampm = hours % 24 < 12 ? 'AM' : 'PM';
		if (hours === 0 && ampm === "PM"){
			hours = 12;
		}
		select.append($('<option></option>')
				.attr('value', i)
				.text(hours + ':' + minutes + ' ' + ampm)); 
	}
}


function onChangeDate(){	
	
	var formattedBeginDate = jQuery("#beginDateText").datepicker("option", "dateFormat", "yy-mm-dd").val();
	var formattedEndDate = jQuery("#endDateText").datepicker("option", "dateFormat", "yy-mm-dd").val();
	
	jQuery("#beginDate").val(formattedBeginDate);
	jQuery("#endDate").val(formattedEndDate);
}

function countChars(inputId){	
}