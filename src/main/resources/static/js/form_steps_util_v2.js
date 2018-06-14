var currentTab = 0; // Current tab is set to be the first tab (0)
var countPaxCreation = 1; // Number of created paxs, starts from 0, the first one is the default pax 
var tabs = document.getElementsByClassName("tab"); // List of all tabs of creation form

// Go to the next tab 
function showTab(tabToShow) {
	
	// This function will display the specified tab of the form ...
	tabs[tabToShow].style.display = "flex";
	tabs[tabToShow].style.flexDirection = "row";
	tabs[tabToShow].style.justifyContent = "space-around";

	// If datas already are valids, we show modify button
//	displayRightButton(tabs[tabToShow].id);
	

	// Display the correct step indicator:
	fixStepIndicator(tabToShow)
}

// Switch between tabs from vertical left navbar
function showTabFromNavBar(tabToShow) {

	// Exit the function if any field in the current tab is invalid:
	if (tabToShow < 0 || tabToShow > 10) {
		return false;
	// Special case : Paxs step
	} else if (tabToShow != 8) {
		jQuery("#paxsCreated").hide();
	} else {
		jQuery("#paxsCreated").show();
	}
	
	// Hide the current tab
	tabs[currentTab].style.display = "none";
	
	// Set the current tab value
	currentTab = tabToShow;
	
	changeStepToValidate();
	
	if(currentTab <= 10) {
		showTab(currentTab);
	}
}


// Removes the "active" class of all steps
function fixStepIndicator(tabToShow) {
	var i, steps = document.getElementsByClassName("nav-item");
	// Remove "active" class to all steps
	for (i = 0; i < steps.length; i++) {
		steps[i].className = steps[i].className.replace(" selected", "");
	}
	// Adds the "active" class to the current step:
	steps[tabToShow].className += " selected";
}


function onChangeDate(inputDate) {	
	console.log(inputDate);
	
}

//Change la date de retour
function changeReturnDate() {
	var date = jQuery("#beginDate").datepicker('getDate');
	if (date && date.getDate) {
		date.setDate(date.getDate() + 1);
		jQuery("#endDate").datepicker('option', 'minDate', date);
		jQuery("#endDate").datepicker('setDate', date);
	}
	
}

function onChangeTime() {
}


//Removes the "active" class of all steps
//function fixStepIndicator(tabToShow) {
//	
//	var i, steps = document.getElementsByClassName("step");
//	
//	for (i = 0; i < steps.length; i++) {
//		steps[i].className = steps[i].className.replace(" active", "");
//	}
//	
//	// Adds the "active" class to the current step:
//	steps[tabToShow].className += "active";
//}




//function displayRightButton(currentTabId) {
//	var areDatasValid = jQuery("#" + currentTabId).data('isvalid');
//	
//	if (areDatasValid) {
//		// Show modify button
//		toggleButtonAndSwitchInput(true);
//	} else {
//		// Show validate button
//		toggleButtonAndSwitchInput(false);
//	}
//}
//
//function toggleButtonAndSwitchInput(onValidate) {
//	
//	// Retrieve current tab
//	inputsOfCurrentTab = tabs[currentTab].getElementsByTagName("input");
//	
//	// If datas are valid
//	if(onValidate) {
//		
//		// Hide validate button
//		jQuery("#nextBtn").hide();
//		
//		// Show modify button
//		jQuery("#modifyBtn").show();
//		
//		// Desactivate inputs
//		for (i = 0; i < y.length; i++) {
//			jQuery("#"+inputsOfCurrentTab[i].name).prop('readonly', true);
//		}
//		
//		// notify navbar
//		jQuery("#go-to-" + tabs[currentTab].id).css("background-color", "green");
//		
//		// Desactivate add button of pax creation tab
//		if (tabs[currentTab].id === 'stepPaxs') {
//			jQuery("#creationPaxBtn").hide();
//			jQuery(".btnDeletePaxCreated").hide();
//		}
//	} else {
//		
//		// Hide modify button
//		jQuery("#modifyBtn").hide();
//		
//		// Show validate button
//		jQuery("#nextBtn").show();
//		
//		// Activate inputs
//		for (i = 0; i < y.length; i++) {
//			jQuery("#"+inputsOfCurrentTab[i].name).prop('readonly', false);
//		}
//		
//		// notify navbar
//		jQuery("#go-to-" + tabs[currentTab].id).css("background-color", "");
//		
//		// Cas particulier : création de paxs
//		// Activate add button of pax creation tab
//		if (tabs[currentTab].id === 'stepPaxs') {
//			jQuery("#creationPaxBtn").show();
//			jQuery(".btnDeletePaxCreated").show();
//		}
//	}
	
//}