/***
 * Code from w3schools.com 
 * source : https://www.w3schools.com/howto/howto_js_form_steps.asp
 ***/

var currentTab = 0; // Current tab is set to be the first tab (0)
var countPaxCreation = 1; // Number of created paxs, starts from 0, the first one is the default pax 


function showTab(n) {
	
	// This function will display the specified tab of the form ...
	var x = document.getElementsByClassName("tab");
	x[n].style.display = "flex";
	x[n].style.flexDirection = "row";
	x[n].style.justifyContent = "space-around";

	// If datas already are valids, we show modify button
	displayRightButton(x[n].id);
	
	if (n == (x.length - 1)) {
		document.getElementById("nextBtn").innerHTML = "Envoyer";
	} else {
		document.getElementById("nextBtn").innerHTML = "Valider";
	}
	// ... and run a function that displays the correct step indicator:
	fixStepIndicator(n)
}


function showTabFromNavBar(n) {
	
	var y = document.getElementsByClassName("tab");

	// Exit the function if any field in the current tab is invalid:
	if (n < 0 || n > 10) {
		return false;
	// Cas particulier : Type de voyageurs
	} else if (n != 8) {
		jQuery("#paxsCreated").hide();
	} else {
		jQuery("#paxsCreated").show();
	}
	
	// Hide the current tab
	y[currentTab].style.display = "none";
	currentTab = n;
	
	if(currentTab <= 10) {
		showTab(currentTab);
	}
}


function nextPrev(n) {
	
	var action = "tab";
	
	allTabsList = document.getElementsByClassName("tab");
	currentTabId = allTabsList[currentTab].id;
	
	// Cas particulier : Etape "types de voyageurs"
	if (currentTabId === "stepPaxs") {
		action = "pax";
	}
	
	// Exit the function if any field in the current tab is invalid
	if (n == 1 && !validateForm(action)) {
		return false;
	}
	
	// Hide the current tab
	allTabsList[currentTab].style.display = "none";
	
	// Increase or decrease the current tab by 1
	currentTab = parseInt(currentTab) + parseInt(n);
	
	// if you have reached the end of the form
	if (currentTab >= allTabsList.length) {
		//...the form gets submitted:
		document.getElementById("nextBtn").type = "submit";
		return false;
	}
	// Otherwise, display the correct tab
	showTab(currentTab);
	
}


function validateForm(action) {
	
	console.log("==> validateForm()");
	
	// This function deals with validation of the form fields
	var x, y, z, i, valid = true;

	x = document.getElementsByClassName("tab");
	y = x[currentTab].getElementsByTagName("input");
	z = x[currentTab].getElementsByTagName("textarea");
	w = x[currentTab].getElementsByTagName("select");
	
	console.log("==> validateForm() ==> Nombre d'inputs : " + y.length);
	console.log("==> validateForm() ==> Nombre textarea : " + z.length);
	console.log("==> validateForm() ==> Nombre select : " + w.length);
	
		if (y.length > 0) {
			
			console.log("==> validateForm() ==> inputs");
			// Cas particuliers : création de paxs BOUTON VALIDER
			// Ne pas tester les inputs
			// Notify sur navabr
			// Changement sur bouton valider
			if (action === "pax") {
				// On ne teste que le nombre de paxs créés car la validation des inputs a déjà été réalisée, lors du clic sur le bouton d'ajout
				if (countPaxCreation <= 4) {
					console.log("VALID FOR TYPES VOYAGEURS");
					valid = true;
					// notify navbar
					jQuery("#go-to-" + x[currentTab].id).css("background-color", "green");
					// Set div custom attribute
					jQuery("#" + x[currentTab].id).data('isvalid', 'true');
					// Desactivate input
					toggleButtonAndSwitchInput(true, y);
				} else {
					valid = false
					// notify navbar
					jQuery("#go-to-" + x[currentTab].id).css("background-color", "");
					// Set div custom attribute
					jQuery("#" + x[currentTab].id).data('isvalid', 'false');
				}
			} else {
				// This tab is only composed by inputs 
				// This loop checks every input field in the current tab
				for (i = 0; i < y.length; i++) {
					console.log("==> validateForm() ==> inputs ==> current input value : " + y[i].value);
					// If a field is empty...
					if (y[i].value == "" || y[i].value.length < 1) {
						// INVALID
						console.log("==> validateForm() ==> inputs ==> INVALIDS");
						// add an "invalid" class to the field
						y[i].className += " invalid";
						// and set the current valid status to false
						valid = false;
						// show an error message 
						showErrorMessage(y[i].id);
						if (action === "tab") {
							// Set div custom attribute
							jQuery("#" + x[currentTab].id).data('isvalid', 'false');
							// notify navbar
							jQuery("#go-to-" + x[currentTab].id).css("background-color", "");
						} 
					} else {
						// VALID
						valid = true;
						// hide error message
						hideErrorMessage(y[i].id);
						
						if (action === "tab") {
							// Set div custom attribute
							jQuery("#" + x[currentTab].id).data('isvalid', 'true');
							// notify navbar
							jQuery("#go-to-" + x[currentTab].id).css("background-color", "green");
							// Desactivate input
							toggleButtonAndSwitchInput(true, y);
						}
					}
				}
			}
		
		} else if (z.length > 0) {
			console.log("==> validateForm() ==> textarea");
			// This tab is only composed by textarea or selects 
			// This loop checks every field in the current tab
			for (i = 0; i < z.length; i++) {
				// On ne valide que la description par défaut
				// Si on ajoute des textareas dans le formulaire, retirer la condition suivante
				if (z[i].name === "descriptionFre") {
					if (typeof z[i] === 'undefined' || z[i].value.length < 10 || z[i].value.length > 1500) {
						// VALID
						console.log("==> validateForm() ==> textarea / select ==> INVALIDS");
						// add an "invalid" class to the field
						z[i].className += " invalid";
						// add a indication message in the placeholder
						z[i].placeholder += "Message dans le placeholder ??";
						// and set the current valid status to false
						valid = false;
						// show an error message
						showErrorMessage(z[i].id);
						// notify navbar
						jQuery("#go-to-" + x[currentTab].id).css("background-color", "");
						// Set custom div attribute
						jQuery("#" + x[currentTab].id).data('isvalid', 'true');
					} else {
						// INVALID
						// Cacher message d'erreur & notifier navbar
						hideErrorMessage(z[i].id);
						// notify navbar
						jQuery("#go-to-" + x[currentTab].id).css("background-color", "green");
						// Set custom div attribute
						jQuery("#" + x[currentTab].id).data('isvalid', 'false');
					}
				}
			}
			
		} else if (w.length > 0) { 
			console.log("==> validateForm() ==> select");
			for (i = 0; i < w.length; i++) {
				if (typeof w[i] !== 'undefined' || w[i].value.length > 0) {
					// VALID
					// notify navbar
					jQuery("#go-to-" + x[currentTab].id).css("background-color", "green");
					// Set custom div attribute
					jQuery("#" + x[currentTab].id).data('isvalid', 'true');
				} else {
					// INVALID
					// notify navbar
					jQuery("#go-to-" + x[currentTab].id).css("background-color", "");
					// Set custom div attribute
					jQuery("#" + x[currentTab].id).data('isvalid', 'false');
				}
			}
			
		} else {
			console.log("==> validateForm() ==> SUBMIT");
			// If the valid status is true, mark the step as finished and valid:
			if (valid) {
				document.getElementsByClassName("step")[currentTab].className += " finish";
			} 
		}
//	}
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

function changeEndHour() { // Remove
	var hour = jQuery("#beginDateText").datepicker('getDate');
	if (date && date.getDate) {
		date.setDate(date.getDate() + 1);
		jQuery("#endDateText").datepicker('option', 'minDate', date);
		jQuery("#endDateText").datepicker('setDate', date);
	}
	jQuery("#beginHour").val(jQuery("#beginHour").val() + ":00");
	jQuer("#").val()
	jQuery("#endHour").val(jQuery("#endHour").val() + ":00");
	jQuery("#").val();
}


function onChangeDate(){	
	
//	var formattedBeginDate = jQuery("#beginDateText").datepicker("option", "dateFormat", "yy-mm-dd").val();
//	var formattedEndDate = jQuery("#endDateText").datepicker("option", "dateFormat", "yy-mm-dd").val();
//	
//	jQuery("#beginDate").val(formattedBeginDate);
//	jQuery("#endDate").val(formattedEndDate);
}


function showErrorMessage(errorStep) {
	
	var classname = ".error-message-" + errorStep;
	if(errorStep != null) {
		jQuery(classname).show();
		
	}
}


function hideErrorMessage(errorStep) {
	
	var classname = ".error-message-" + errorStep;
	if(errorStep != null) {
		jQuery(classname).hide();
		
	}
}


function createPax() {
	
	// Retrieve pax datas
	var name = jQuery("#paxName").val();
	var ageMin = jQuery("#paxAgeMin").val();
	var ageMax = jQuery("#paxAgeMax").val();
	var price = jQuery("#paxPrice").val();
	
	// Button labels
	var labelBtnModify = "Modifier";
	var labelBtnDelete = "Supprimer";
	
	// Check if datas are valids
	if (validateForm()) {
		// Create div
		jQuery("#paxsCreated").append(
				'<div class="pax-item" id="' + countPaxCreation + '">'
				+'<p> <span class="pax-infos">' + name + '</span> </p>'
				+'<input type="hidden" id="paxs[' + countPaxCreation + '].name" name="paxs[' + countPaxCreation + '].name" value="' + name + '" />'
				+'<p> <span class="pax-infos">' + ageMin + '</span> </p>'
				+ '<input type="hidden" id="paxs[' + countPaxCreation + '].ageMin" name="paxs[' + countPaxCreation + '].ageMin" value="' + ageMin + '" />'
				+'<p> <span class="pax-infos">' + ageMax + '</span> </p>'
				+ '<input type="hidden" id="paxs[' + countPaxCreation + '].ageMax" name="paxs[' + countPaxCreation + '].ageMax" value="' + ageMax + '" />'
				+'<p> <span class="pax-infos">' + price + '</span> </p>'
				+ '<input type="hidden" id="paxs[' + countPaxCreation + '].price" name="paxs[' + countPaxCreation + '].price" value="' + price + '" />'
//				+'<p> <button class="btnModifyPaxCreated" id="btnModifyPaxCreated' + countPaxCreation + '" onclick="modifyPax(' + countPaxCreation + ');">' + labelBtnModify + '</button> </p>'
				+'<p> <button class="btnDeletePaxCreated" id="btnDeletePaxCreated' + countPaxCreation + '" onclick="deletePax(' + countPaxCreation + ');">' + labelBtnDelete + '</button> </p>'
				+ '</div>');
		
		countPaxCreation ++;
		hasCreatedPax = true;
		
		// Remove inputs values
		jQuery("#paxName").val("");
		jQuery("#paxAgeMin").val("");
		jQuery("#paxAgeMax").val("");
		jQuery("#paxPrice").val("");
	}
	
}

//function modifyPax() {
	// TODO
//}

function createSchedule() {
	if(validateSchedules()) {
		console.log("ok");
	} else {
		console.log("ko");
	}
	
}

//function validateSchedules() {
//	
//	var schedulesBloc, schedulesInputs, schedulesBlocCreation;
//	var isSet = false;
//	schedulesBloc = document.getElementById("stepSchedules");
//	schedulesBlocCreation = schedulesBloc.getElementsByClassName("sessionBloc");
//	
//	if (schedulesBlocCreation.length > 0) {
//		
//		for(var i = 0; i < schedulesBlocCreation.length; i++) {
//			
//			// Retrieve sessions input
//			schedulesInputs = schedulesBlocCreation[i].getElementsByTagName("input");
//			
//			for (var j = 0; j <= schedulesInputs.length; j++) {
//			
//				// Check if there is at least one imput which have been setted
//				if(schedulesInputs[j] != null) {
//					if(schedulesInputs[j].type == "time") {
//						
//						if (schedulesInputs[j].value) {
//							if (schedulesInputs[j].getAttribute("data-settable")  === true) {
//								isSet = true;
//							}
//						}
//					}
//				}
//			}
//		}
//		
//		if (!isSet) {
//			return false;
//		} else {
//			return true;
//		}
//	}
//	return isSet;
//}

function deletePax(paxId) {

	jQuery("#" + paxId).remove();
	
	if (countPaxCreation >= 0) {
		countPaxCreation --;
	} else {
		countPaxCreation = 0;
		hasCreatedPax = false;
	}
}


function increaseTimeByOneHour(timeInput, inputToSet) {
	
	var timeStr = timeInput.value;
    var splitedTimeStr = timeStr.split(':');
    var hours = parseInt(splitedTimeStr[0]);
    var minutes = splitedTimeStr[1].split(" ")[0];
    var nextHours = (hours + 1);

    jQuery("#" + inputToSet.name).val(nextHours + ":" + minutes);
  
}

function displayRightButton(currentTabId) {
	
	var areDatasValid = jQuery("#" + currentTabId).data('isvalid');
	
	if (areDatasValid) {
		// Show modify button
		toggleButtonAndSwitchInput(true);
	} else {
		// Show validate button
		toggleButtonAndSwitchInput(false);
	}
}

// TODO Ne marche que pour les inputs
function toggleButtonAndSwitchInput(onValidate) {
	
	console.log("toggleButtonAndSwitchInput");
	
	// Retrieve tab
	x = document.getElementsByClassName("tab");
	// Retrieve current tab
	y = x[currentTab].getElementsByTagName("input");
	
	if(onValidate) {
		// Hide validate button
		jQuery("#nextBtn").hide();
		// Show modify button
		jQuery("#modifyBtn").show();
		// Desactivate inputs
		for (i = 0; i < y.length; i++) {
			jQuery("#"+y[i].name).prop('readonly', true);
		}
		// notify navbar
		jQuery("#go-to-" + x[currentTab].id).css("background-color", "green");
		
		// Cas particulier : création de paxs
		// Desactivate add button of pax creation tab
		if (x[currentTab].id === 'stepPaxs') {
			jQuery("#creationPaxBtn").hide();
//			jQuery(".btnModifyPaxCreated").hide();
			jQuery(".btnDeletePaxCreated").hide();
		}
	} else {
		// Hide modify button
		jQuery("#modifyBtn").hide();
		// Show validate button
		jQuery("#nextBtn").show();
		// Activate inputs
		for (i = 0; i < y.length; i++) {
			jQuery("#"+y[i].name).prop('readonly', false);
		}
		// notify navbar
		jQuery("#go-to-" + x[currentTab].id).css("background-color", "");
		
		// Cas particulier : création de paxs
		// Activate add button of pax creation tab
		if (x[currentTab].id === 'stepPaxs') {
			jQuery("#creationPaxBtn").show();
//			jQuery(".btnModifyPaxCreated").show();
			jQuery(".btnDeletePaxCreated").show();
		}
	}
}