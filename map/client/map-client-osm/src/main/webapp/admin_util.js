function createMap(emailAddress, mapDescription) {
	if (!emailAddress) {
		alert('Kérlek, add meg az e-mail címedet!');
		return false;
	}

	fetch(mapServerUrlBase, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({ emailAddress, mapDescription })
	})
		.then(response => response.text())
		.then(data => {
			document.getElementById('mapLinks').innerHTML = data;
			return true;
		})
		.catch(error => {
			console.error('Hiba a térkép létrehozásakor:', error);
		});
	return false;
}

function getMyMapLinks(emailAddress) {
	if (!emailAddress) {
		alert('Kérlek, add meg az e-mail címedet!');
		return;
	}

	fetch(mapServerUrlBase + '/mylinks?emailAddress=' + emailAddress)
		.then(response => response.text())
		.then(data => {
			document.getElementById('mapLinks').innerHTML = data;
		})
		.catch(error => {
			console.error('Hiba a térképek listázásakor:', error);
		});
}

function getMapLinks() {
	fetch(mapServerUrlBase + '/links')
		.then(response => response.text())
		.then(data => {
			document.getElementById('mapLinks').innerHTML = data;
		})
		.catch(error => {
			console.error('Hiba a térképek listázásakor:', error);
		});
}

function deleteMap(mapId, emailAddress) {
	fetch(mapServerUrlBase + '/' + mapId + '/' + emailAddress, {
		method: 'DELETE'
	})
		.then(response => response.text())
		.then(data => {
			document.getElementById('mapLinks').innerHTML = data;
		})
		.catch(error => {
			console.error('Hiba a térkép törléskor:', error);
		});
}

