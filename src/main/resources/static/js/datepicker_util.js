//Initialise les datePickers
function initDatePicker() {
	// EVENEMENTS
	jQuery("#startDateIcon").click(function() {
		jQuery("#beginDate").datepicker("show");
	});

	jQuery("#endDateIcon").click(function() {
		jQuery("#endDate").datepicker("show");
	});

	$("#beginDate").datepicker({
		maxDate : '+48M',
		dateFormat : "dd/mm/yy",
		minDate : 1,
		numberOfMonths : 2,
		firstDay : 1,
		afterShow : function(input, inst, cells) {
			// Initialisation
			var startDate = $("#beginDate").datepicker("getDate");
			var endDate = $("#endDate").datepicker("getDate");

			// Pré-sélection
			$.each(cells, function(index, cell) {
				var currentDate = getDateFromDatepickerCell(cell);
				if (endDate && currentDate && (currentDate.getTime() <= endDate.getTime()) && (currentDate.getTime() >= startDate.getTime())) {
					$(cell).addClass("highlight");

					if ((currentDate.getTime() == endDate.getTime()) || (currentDate.getTime() == startDate.getTime())) {
						$(cell).children('a').addClass("ui-state-active");
					}
				}
			});
		},

		onSelect : function() {
			var date = $("#beginDate").datepicker("getDate");
			date.setDate(date.getDate() + 1);
			var endDate = $("#endDate");
			endDate.datepicker('option', 'minDate', date).datepicker('setDate', date);
			$("#beginDate").trigger('change');
			setTimeout(function() {
				endDate.datepicker('show');
			}, 0);
		}
	});

	$("#endDate").datepicker({
		maxDate : '+48M',
		dateFormat : "dd/mm/yy",
		minDate : 2,
		numberOfMonths : 2,
		firstDay : 1,
		afterShow : function(input, inst, cells) {
			// Initialisation
			var startDate = $("#beginDate").datepicker("getDate");
			var endDate = $("#endDate").datepicker("getDate");
			var currentDate = null;

			// Pré-sélection
			$.each(cells, function(index, cell) {
				var currentDate = getDateFromDatepickerCell(cell);

				if (endDate && currentDate && (currentDate.getTime() <= endDate.getTime()) && (currentDate.getTime() >= startDate.getTime())) {
					$(cell).addClass("highlight");

					if ((currentDate.getTime() == endDate.getTime()) || (currentDate.getTime() == startDate.getTime())) {
						$(cell).children('a').addClass("ui-state-active");
					}
				}
			});

			// Evènement au survol
			$.each(cells, function(index, cell) {
				$(cell).on('mouseenter', function() {
					currentDate = getDateFromDatepickerCell(cell);

					$.each(cells, function(indexBis, cellBis) {
						var dateInRange = getDateFromDatepickerCell(cellBis);

						if (startDate && (dateInRange.getTime() <= currentDate.getTime()) && (dateInRange.getTime() >= startDate.getTime())) {
							$(cellBis).addClass("highlight");
						} else {
							$(cellBis).removeClass("highlight");
						}
					});
				});
			});
		}
	});

	// Initialisation des dates
	changeReturnDate("#startDate", "#endDate");
};


function getDateFromDatepickerCell(cell) {
	var day = $(cell).children('a').text();
	if (day.length < 2) {
		day = "0" + day;
	}

	var month = $(cell).data('month');
	month += 1;

	if (month.toString().length < 2) {
		month = "0" + month;
	}

	var year = $(cell).data('year');

	// si date en Fr
	if ($.datepicker._defaults.dateFormat == "dd/mm/yy") {
		return $.datepicker.parseDate($.datepicker._defaults.dateFormat, day + "/" + month + "/" + year);

	} else if ($.datepicker._defaults.dateFormat == "mm/dd/yy") {
		// si date en En
		return $.datepicker.parseDate($.datepicker._defaults.dateFormat, month + "/" + day + "/" + year);
	}
};


function changeReturnDate() {
	console.log("test");
	var date = jQuery("#beginDate").datepicker('getDate');
	console.log(date);
	if (date && date.getDate) {
		date.setDate(date.getDate() + 1);
		jQuery("#endDate").datepicker('option', 'minDate', date);
		jQuery("#endDate").datepicker('setDate', date);
		console.log(jQuery("#endDate"));
	}
};