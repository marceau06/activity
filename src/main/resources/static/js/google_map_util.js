
function initMap(latMeetingPoint, lngMeetingPoint, zoomSearch, radius) {
		
	// Options
	var uluru = {
			lat: parseFloat(latMeetingPoint), 
			lng: parseFloat(lngMeetingPoint)
	};

		var map = new google.maps.Map(document.getElementById('googleMap'), {
			zoom: parseFloat(zoomSearch),
			center: uluru
		});

		var marker = new google.maps.Marker({
			position: uluru,
			map: map,
			draggable:true,
			animation: google.maps.Animation.DROP
		});

		// Events
		var geocoder = new google.maps.Geocoder;
		var infowindow = new google.maps.InfoWindow;

		marker.addListener('click', toggleBounce);

		google.maps.event.addListener(marker, "dragend", function(event) {

			// Pass variables by reference
			marker = [marker];
			map = [map];

			updateMarkerWithGeolocs(geocoder, map, marker, infowindow, event.latLng.lat(), event.latLng.lng());
			
			// Retrieve variables by reference
			marker = marker[0];
			map = map[0];
		});
}

function updateMarkerWithGeolocs(geocoder, map, marker, infowindow, latStr, lngStr) {

	console.log(">updateMarkerWithGeolocs : lat = " + latStr + " lng = " + lngStr);
	
	jQuery("#latitude").val(parseFloat(latStr));
	jQuery("#longitude").val(parseFloat(lngStr));
	
	console.log(jQuery("#latitude").val());
	console.log(jQuery("#longitude").val());
	
	var latlng = {lat: parseFloat(latStr), lng: parseFloat(lngStr)};

	geocoder.geocode({'location': latlng}, function(results, status) {
		if (status === 'OK') {
			if (results[1]) {
			
				marker[0].setOptions({
					position: latlng,
					map: map[0],
					draggable:true,
				});
				infowindow.setContent(results[1].formatted_address);
				infowindow.open(map[0], marker);

			} else {
				window.alert('No results found');
			}
		} else {
			window.alert('Geocoder failed due to: ' + status);
		}
	});
}

// TODO Externaliser remplissage des inputs dans un autre fichier
function geoNameByGeolocs(lat, lng) {
	
	var url = 'https://maps.googleapis.com/maps/api/geocode/json?latlng=' + lat + ',' + lng + '&key=AIzaSyDMVRF60y0zxhEqVF1vArJvTyWk5d81kJ4&sensor=false';
	var city;
	var area;
	var country;
	
	var test = jQuery.getJSON(url, function(data) {
		
		var results = data.results;
		for (var ac = 0; ac < results[0].address_components.length; ac++) {
            var component = results[0].address_components[ac];

            switch(component.types[0]) {
                case 'locality':
                    city = component.long_name;
                    break;
                case 'postal_code':
                    zipCode = component.short_name;
                    break;
                case 'country':
                    country = component.long_name;
                    break;
            }
        };

	}).always(function() {
		jQuery("#city").val(city);
		jQuery("#country").val(country);
		jQuery("#zipCode").val(zipCode);
	});
}

function toggleBounce() {
	if (marker.getAnimation() !== null) {
		marker.setAnimation(null);
	} else {
		marker.setAnimation(google.maps.Animation.BOUNCE);
 }
	
}


