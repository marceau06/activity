function autocomplete(code) {

	labels = {};
	var _lang = "fr";
	var item;
	var haveToShowDestiPopup = !jQuery(code).val().length > 0;

	jQuery(code).ws_autocomplete().on('start', function(){
		delete item;
	}).on('_select', function(event, item) {
		if (item) {
			selectItem(item, code, code, "#cat", "#latitude", "#longitude", "#zoom", "#radius");
		}
		jQuery(".ui-autocomplete-input").focusout();
		jQuery(this).blur();
	}).on('_close', function(event) {
		if (!item) {
			resetItem(code, code, "#cat", "#latitude", "#longitude", "#zoom", "#radius");
		}
		jQuery(".ui-autocomplete-input").focusout();
		jQuery(this).blur();
	});
}

function selectItem(item, nameId, codeId, catId, latId, lngId, zoomId, radiusId) {
	item = (item) ? getItemFromRequest(item) : hydrateItem(nameId, codeId, catId, latId, lngId, zoomId, radiusId);
	setItem(item, nameId, codeId, catId, latId, lngId, zoomId, radiusId, true);
}

function getItemFromRequest(item) {

	if (item.category === "POLYGON") {
		var center = item.center ||  getCenterOfPoints(item.bounds);
		return {
			value : item.name,
			points : item.bounds,
			latitude : center.latitude,
			longitude : center.longitude,
			code : encodeURIComponent(item.code),
			type : "POLYGON",
			categoryType : "POLYGON",
			radius : item.radius != null ? item.radius : 0
		};
	} else if (item.category === "HOTEL") {

	} else if (item.category === "CITY") {
		jQuery("#country").val(item.hobbes_country.name);
		return {
			value : item.name,
			code : item.source_id,
			latitude : item.location.latitude,
			longitude : item.location.longitude,
			zoom : 12,
			categoryType : "CITY",
			type : "PLACE",
			radius : item.radius != null ? item.radius : 0
		};
	} else if (item.category === "POI") {
		return {
			value : item.name,
			code : item.source_id,
			latitude : item.location.latitude,
			longitude : item.location.longitude,
			zoom : 5,
			categoryType : item.category,
			type : "PLACE",
			radius : item.radius != null ? item.radius : 0
		};
	} else if (item.category === 'ADDRESS') {
		return {
			value : item.name,
			code : item.name,
			latitude : item.location.latitude,
			longitude : item.location.longitude,
			zoom : 17,
			categoryType : item.category,
			type : 'PLACE',
			radius : item.radius != null ? item.radius : 0
		};
	} else {
		return item;
	}
}


function setItem(item, nameId, codeId, catId, latId, lngId, zoomId, radiusId, hasToEraseCityNameInput) {
	
	haveToShowDestiPopup = false;
	if (hasToEraseCityNameInput) {
		jQuery(nameId).val(item.value);
	} else {
		jQuery(nameId).attr("value", item.value);
	}

	jQuery(codeId).val(item.code);
	jQuery(catId).val(item.type);
	jQuery(latId).val(item.latitude);
	jQuery(lngId).val(item.longitude);
	jQuery(zoomId).val(item.zoom);
	jQuery(radiusId).val(item.radius);
	
	// Update google map only for address input
	if(item.categoryType === "ADDRESS") {
		initMap(jQuery(latId).val(), jQuery(lngId).val(), jQuery(zoom).val());
	} 
}

function hydrateItem(nameId, codeId, catId, latId, lngId, zoomId, radiusId, resetItem) {
	return {
		value: resetItem ? nameId : jQuery(nameId).val(),
				code: resetItem ? codeId : toInt(jQuery(codeId).val()),
						type: jQuery(catId).val(),
						categoryType : jQuery(catId).val(),
						latitude: toFloat(jQuery(latId).val()),
						longitude: toFloat(jQuery(lngId).val()),
						zoom: toInt(jQuery(zoomId).val()),
						radius: toInt(jQuery(radiusId).val()),
	};
}

function changeDestinationCityWithId(id){
	//vide les champs code et name
	document.getElementById(id).value = "";
}


//Reinitialise les donnees de la ville
function resetItem(nameId, codeId, catId, latId, lngId, zoomId, radiusId) {
	// TODO 

	reset(nameId);
	reset(codeId);
	reset(catId);
//	reset(latId);
//	reset(lngId);
//	reset(zoomId);
	reset(radiusId);
}

//Reinitialise la valeur d'un element
function reset(element) {
	jQuery(element).val("");
	return null;
}

(function ($) {
	$.widget("custom.catcomplete", $.ui.autocomplete, {
		_create: function () {
			this._super();
			this.widget().menu("option", "items", "> :not(.ui-menu-item-categ)");
		},
		_renderMenu: function (ul, items) {
			_settings.labels = {
					CITY : "Villes",
					POLYGON : "Polygone",
					POI : "Points d'intérêts",
					ADDRESS : "Adresses",
					HOTEL : "Etablissements",
					AIRPORT: "Aéroports",
					CAR_RENTAL_STATION: "Stations de location",
					COUNTRY: "Pays",
					AIR_COMPANY: "Compagnie aériennes",
					SUPPLIER: "Fournisseurs"
			};
			var that = this;
			var currentCategory = '';
			
			// On récupère le nom de l'input pour déterminer le type de réponse (Villes, adresses ...) que lon va afficher
			var inputName = that.element[0].id;
			var catToExclude;
			
			if(inputName ===  "address") {
				catToExclude = "CITY";
			} else {
				catToExclude = "ADDRESS";
			}
						
			$.each(items, function (index, item) {
				var cat;
				if(item.data.category != catToExclude && item.data.category != "HOTEL" && item.data.category != "CAR_RENTAL_STATION" && item.data.category != "AIRPORT") {
						cat = _settings.labels[item.data.category] || item.data.category;	
						if (cat !== currentCategory && !_settings.hide[cat]) {
							ul.append("<li class='ui-menu-item-categ'>" + cat + "</li>");
							currentCategory = cat;
						}
						that._renderItemData(ul, item);
					} 
			});
		},
		_renderItem: function (ul, item) {
			item.label = setClassItemSearch(item.label, this.term);
			return $('<li></li>').data('item.autocomplete', item).append("<a>" + item.label + "</a>").appendTo(ul);
		}
	});
	var getISO3Language = function (key) {
		var ISO3LanguagesMapping = {
				fr:"fre",
				en:"eng",
				es:"spa",
				nl:"nld",
				zh:"chi"
		}
		if(ISO3LanguagesMapping.hasOwnProperty(key)){
			return ISO3LanguagesMapping[key];
		}else{
			return "fre";
		}
	};

	var _lang = getISO3Language(jQuery('#search-form').data('lang'));
	var _settings = {
			key :"5e04a87aa83bfdb8",
			lang : "fre",
			url : 'https://webservice-autocomplete.koedia.com/complete.json',
			minLength : 3,

			hide : {
				"Polygone" : true,
				"Etablissements" : true,
				"Stations de location" : true,
				"Fournisseurs" : true,
				"Compagnie aériennes" : true,
				"Pays" : true,
				"Aéroports" : true
			},
			source : function(rows, searchVal) {
				rows = jQuery.grep(rows, function(row){
					return row.physical_airport !== false;
				});
				return jQuery.map(rows, function(row) {
					if (row) {
						return {
							data : row,
							label : _settings.showLabel(row)
						};
					}
				});
			},
			showLabel : function(row) {
				if(row.hobbes_country && row.category === 'CITY'){
					row.name += " (" + row.hobbes_country.name + ")";
				}
				labels[row.name] = row;
				return row.name;
			}
	};

	// Plug-in autocomplete
	$.fn.ws_autocomplete = function (labels) {
		var _that = $(this);

		_that.catcomplete({
			source: function (req, resp) {
				_that.trigger('_start');
				var strReq = _that.val().trim();
				var i = 0;
				var isNumber;

				// si les 3 premiers caractères sont des chiffres, alors on envoie le paramètre bounds
				do {
					isNumber = !isNaN(strReq.charAt(i)) ? true : false;
					i++;
				} while (i != 3 && isNumber == true);

				$.ajax({
					url: _settings.url,
					type: 'post',
					dataType: 'json',
					data: {
						key: _settings.key,
						req: window.encodeURI(_that.val().trim()),
						lang: _settings.lang,
						bounds: _settings.bounds
					},
					success: function (data) {
						_that.trigger('_success', data);
						resp(_settings.source(data, _that.val()));
					},
					complete: function (data) {
						_that.trigger('complete');
						_that.trigger('_complete');
					},
					error: function () {
						_that.trigger('_error');
					}
				});
			},
			select: function (event, ui) {
				if (ui.item.data) {
					_that.trigger('_select', ui.item.data);
				}
			},
			open: function () {
				if (navigator.userAgent.match(/(iPod|iPhone|iPad)/)) {
					jQuery('.ui-autocomplete').off('menufocus hover mouseover');
				}
				_that.trigger('_open');
				$(this).removeClass("ui-corner-all").addClass("ui-corner-top");
			},
			close: function () {
				_that.trigger('_close');
				$(this).removeClass("ui-corner-top").addClass("ui-corner-all");
			},
			delay: _settings.delay,
			minLength: _settings.minLength
		});
		return _that;
	};

	function setClassItemSearch(str, text) {
		if (navigator.appVersion.indexOf("MSIE 8.") === -1) {
			var toBold = str.split(" ");
			var searchStr = text.split(" ");
			for (var ii = 0; ii < toBold.length; ii++) {
				var bold = toBold[ii].split(",");
				for (var kk = 0; kk < bold.length; kk++) {
					for (var jj = 0; jj < searchStr.length; jj++) {
						if (bold[kk].indexOf("<span") === -1 && searchStr[jj].length) {
							var matcher = new RegExp("(" + $.ui.autocomplete.escapeRegex(searchStr[jj]) + "+)", "ig");
							bold[kk] = bold[kk].replace(matcher, "<span class='itemSearchAutocomplete'>$1</span>");
						}
					}
				}
				toBold[ii] = bold.join(",");
			}
			return toBold.join(" ");
		}
		return str;
	}

})(jQuery);
