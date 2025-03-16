function initMap() {
	var map = L.map('map');

	// Magyarország határai (kb.)
	var bounds = L.latLngBounds([
		[45.788212, 16.072998], // Délnyugat
		[48.668542, 23.049316]  // Északkelet
	]);

	map.fitBounds(bounds);
	map.setZoom(8);

	L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
		maxZoom: 19,
		attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
	}).addTo(map);
	// show the scale bar on the lower left corner
	L.control.scale({ imperial: true, metric: true }).addTo(map);

	return map;
}

function createMarker(latlng, content) {
	return L.marker(latlng)
		.bindPopup(content)
		.bindTooltip(content);
}

function addMarkerToMap(marker, map) {
	marker
		.addTo(map)
		.openPopup();
	return marker;
}

function onMapClick(e, mapId, content, emailAddress) {
	if (content && emailAddress) {
		var marker = addMarkerToMap(createMarker(e.latlng, content), map);
		saveMarkerToServer(marker, mapId, emailAddress);

		marker.on('click', function() {
			map.removeLayer(marker); // A jelölő eltávolítása a térképről
			deleteMarkerFromServer(marker, mapId, emailAddress);
		});
	}
}

function deleteMarkerFromServer(marker, mapId, emailAddress) {
	var latlng = marker.getLatLng();

	// Marker adatok mentése a szerverre REST API-n keresztül
	fetch('/api/markers', {
		method: 'DELETE',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({ mapId: mapId, lat: latlng.lat, lng: latlng.lng, emailAddress: emailAddress })
	})
		.then(response => response.json())
		.then(data => {
			console.log('Marker törölve:', data);
		})
		.catch(error => {
			console.error('Hiba a marker törlésekor:', error);
		});
}

function saveMarkerToServer(marker, mapId, emailAddress) {
	var latlng = marker.getLatLng();

	// Marker adatok mentése a szerverre REST API-n keresztül
	fetch('/api/marker', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({ mapId: mapId, lat: latlng.lat, lng: latlng.lng, content: marker.getTooltip().getContent(), emailAddress: emailAddress })
	})
		.then(response => response.json())
		.then(data => {
			console.log('Marker mentve:', data);
			//			marker.bindPopup('Marker mentve! ID: ' + data.id).openPopup();
		})
		.catch(error => {
			console.error('Hiba a marker mentésekor:', error);
			marker.bindPopup('Hiba a marker mentésekor!').openPopup();
		});
}

function addMarkersFromServer(map, mapId, addEmail) {
	// Markerek betöltése a szerverről
	fetch('/api/markers/' + mapId)
		.then(response => response.json())
		.then(markers => {
			markers.forEach(markerData => {
				addMarkerToMap(createMarker(L.latLng(markerData.lat, markerData.lng),
					markerData.content + (addEmail ? "<p>" + markerData.emailAddress : "")), map);
				console.error('Marker ID: ' + markerData.id);
			});
		})
		.catch(error => {
			console.error('Hiba a markerek betöltésekor:', error);
		});
}
