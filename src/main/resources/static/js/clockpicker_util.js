$(document).ready(function() {
  var start = new Date();
  var end = new Date();
  var difference;

  console.log("clockpicker_util");
  
// Clockpicker bootstrap
  jQuery('.clockpicker').clockpicker();

  $('#start').clockpicker({
    donetext: "Done",

    afterShow: function() {
      start = $('#start').clockpicker('getTime');
      console.log('Start after show: ' + start);
    },
    afterDone: function() {
      start = $('#start').clockpicker('getTime');
      console.log('Start after done: ' + start);
      $('#end').prop('disabled', false);
    }
  });

  $('#end').clockpicker({
    donetext: "Done",

    afterShow: function() {
      end = $('#end').clockpicker('getTime');
      console.log('End after show: ' + end);
    },
    afterDone: function() {
      end = $('#end').clockpicker('getTime');
      console.log('End after done: ' + end);
      difference = (end - start);
      getdiff(difference);
    }
  });

function getdiff(s) {
    var secs = Math.round(s/ 1000);
    var modsecs = ((Math.round(s/1000))%60); //remaining secs if not even
    var mins = Math.round(s/1000/60);
    var modmins = ((Math.round(s/1000/60))%60); //mins remaining if not even
    var modhrs = ((Math.round(s/1000/60/60))%24); //mins remaining if not even
    
    var hrs = Math.round(s/1000/60/60);
    if (modmins >=30){
        modhrs = modhrs-1;
    }
    
    var enddiff = [
      modhrs
    ];
    var arr = jQuery.map(enddiff, function(modhrs) {
      return modhrs + ":" + modmins;
    });
    $('#diff').text("Difference is " +arr);
  }
});

function changeEndHour(startTimeInput) {
	var endTimeValue;
	// Start time input
	var beginHourInput = jQuery(startTimeInput);
	// Id of start input session ex : begin42
	var beginHourSessionId = beginHourInput.data('session-id');
	
	// Id of end input session
	var endHourSessionId = "end" + beginHourSessionId.replace("begin", "");
	// End time input
	var endHourInput = jQuery('*[data-session-id='+ endHourSessionId +']');
	
	// Change end time with start time + one hour
    var hour = parseInt(beginHourInput.val().split(':')[0]) + 1;
    var minute = beginHourInput.val().split(':')[1];
    if (hour >= 10) {
    	endTimeValue = hour + ":" + minute;
    } else {
    	endTimeValue = "0" + hour + ":" + minute;
    }
    endHourInput.val(endTimeValue);
    
    // Update the session duration
    getSessionDuration(endTimeValue, beginHourInput.val());
}

function updateDifferenceHour(endTimeInput) {
	var sessionDuration = 0;
	var beginTimeValue;
	// Start time input
	var endHourInput = jQuery(endTimeInput);
	var endTimeValue = endHourInput.val();
	// Id of start input session ex : begin42
	var endHourSessionId = endHourInput.data('session-id');
	// Id of end input session
	var beginHourSessionId = "begin" + endHourSessionId.replace("end", "");
	// End time input
	var beginHourInput = jQuery('*[data-session-id='+ beginHourSessionId +']');
	beginTimeValue = beginHourInput.val();
	
	getSessionDuration(endTimeValue, beginTimeValue);
}


function getSessionDuration(endTime, startTime) {
	
	var t1 = moment(endTime, "HH:mm");
	var t2 = moment(startTime, "HH:mm");
	var difference = moment(t1.diff(t2)).format("HH:mm");
  
    console.log(jQuery("#sessionDurationInfo"));
    	jQuery("#sessionDurationInfo").val("test text changing");
}