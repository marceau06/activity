
function initMap(latMeetingPoint, lngMeetingPoint, zoomSearch, radius) {

	// Check parameters
	if(!latMeetingPoint || !latMeetingPoint || !zoomSearch){
		// TODO
	} else {
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

			geonameByGeolocs(geocoder, map, marker, infowindow, event.latLng.lat(), event.latLng.lng());
			
			// Retrieve variables by reference
			marker = marker[0];
			map = map[0];
		});
	}
}

function geonameByGeolocs(geocoder, map, marker, infowindow, latStr, lngStr) {

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

function toggleBounce() {
	if (marker.getAnimation() !== null) {
		marker.setAnimation(null);
	} else {
		marker.setAnimation(google.maps.Animation.BOUNCE);
	}
}


