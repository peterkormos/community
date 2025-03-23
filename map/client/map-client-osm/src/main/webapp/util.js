
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

function addMarkerToMap(marker, markerClusterGroup, emailAddress) {
	markerClusterGroup.addLayer(marker);

	/*	
	marker
		.addTo(map)
//		.openPopup()
		;
*/

	if (emailAddress) {
		marker.on('click', function() {
			markerClusterGroup.removeLayer(marker); // A jelölő eltávolítása a térképről
			deleteMarkerFromServer(marker, mapId, emailAddress);
			return marker;
		});
	}

	return marker;
}

function onMapClick(e, mapId, content, emailAddress, markerClusterGroup) {
	if (content && emailAddress) {
		var marker = addMarkerToMap(createMarker(e.latlng, content), markerClusterGroup, emailAddress);
		saveMarkerToServer(marker, mapId, emailAddress);
	}
}

function saveMarkerToServer(marker, mapId, emailAddress) {
	var latlng = marker.getLatLng();

	// Marker adatok mentése a szerverre REST API-n keresztül
	fetch(markerServerUrlBase, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({ mapId: mapId, lat: latlng.lat, lng: latlng.lng, content: marker.getTooltip().getContent(), emailAddress: emailAddress })
	})
		//		.then(response => response.json())
		//		.then(data => {
		//			console.log('Marker mentve:', data);
		//		})
		.catch(error => {
			console.error('Hiba a marker mentésekor:', error);
			marker.bindPopup('Hiba a marker mentésekor!').openPopup();
		});
}

function deleteMarkerFromServer(marker, mapId, emailAddress) {
	var latlng = marker.getLatLng();

	// Marker adatok mentése a szerverre REST API-n keresztül
	fetch(markerServerUrlBase, {
		method: 'DELETE',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({ mapId: mapId, lat: latlng.lat, lng: latlng.lng, emailAddress: emailAddress })
	})
		//		.then(response => response.json())
		//		.then(data => {
		//			console.log('Marker törölve:', data);
		//		})
		.catch(error => {
			console.error('Hiba a marker törlésekor:', error);
		});
}

function addMarkersFromServer(markerClusterGroup, mapId, emailAddress, addEmail) {
	// Markerek betöltése a szerverről
	fetch(markerServerUrlBase + '/' + mapId + '/' + emailAddress)
		.then(response => response.json())
		.then(markers => {
			markers.forEach(markerData => {
				addMarkerToMap(createMarker(L.latLng(markerData.lat, markerData.lng),
					markerData.content), markerClusterGroup, markerData.emailAddress);
			});
		})
		.catch(error => {
			console.error('Hiba a markerek betöltésekor:', error);
		});
}

function clickToCopyText(element) {
	element.addEventListener('click', () => {
		copyTextToClipborard(element)
	});
}

function copyTextToClipborard(textToCopy) {
	//	const textToCopy = element.textContent;

	if (navigator.clipboard) {
		navigator.clipboard.writeText(textToCopy)
			.then(() => {
				alert('[' + textToCopy + '] vágólapra másolva...');
			})
			.catch(err => {
				console.error('Másolás nem ment: ', err);
				alert('Másolás nem ment. ;)');
			});
	};
}

function copyURLToClipborard(url) {
	copyTextToClipborard(getBaseUrl() + '/' + url);
}

function getBaseUrl() {
	const url = window.location.href;
	const parts = url.split('/');

	// Remove the last part (the page name)
	parts.pop();

	// Join the remaining parts
	const baseUrl = parts.join('/');

	return baseUrl;
}


var emailAddress;
function getEmailAddress() {
	if(emailAddress)
		return emailAddress;
	else
		emailAddress = prompt("Pont szerkesztéséhez megerősítő email ide érkezzen:");
	
	return emailAddress;
	}
