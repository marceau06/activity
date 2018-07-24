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
	if (tabToShow < 0 || tabToShow > 11) {
		return false;
	// Special case : Paxs step
	} else if (tabToShow != 9) {
		jQuery("#paxsCreated").hide();
	} else {
		jQuery("#paxsCreated").show();
	}
	// Hide the current tab
	tabs[currentTab].style.display = "none";
	// Set the current tab value
	currentTab = tabToShow;
	
	if(currentTab <= 11) {
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
