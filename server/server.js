var firebase = require('firebase-admin');
var request = require('request');

var API_KEY = "AAAA8gvOiR0:APA91bGb4M0bj2wEddAITNSSxITGz3htfiCvscChUUK04e39v4AhJpDgNB1DKRt07YFpgxh-TKOAIsMBakcFUzGn1CiGrVcYXpeeXQ0KAUeVlbNwhy4sOV5rpwW6TeijvCwOMl37ipcD"; // Your Firebase Cloud Messaging Server API key

// Fetch the service account key JSON file contents
var serviceAccount = require('./key.json');

// Initialize the app with a service account, granting admin privileges
firebase.initializeApp({
  credential: firebase.credential.cert(serviceAccount),
  databaseURL: "https://blood-relations.firebaseio.com/"
});
ref = firebase.database().ref();

function listenForNotificationRequests() {
  console.error("Listening for requests");
  var requests = ref.child('notificationRequests');
  requests.on('child_added', function(requestSnapshot) {
	console.error("Received request");
    var request = requestSnapshot.val();
    sendNotificationToUser(
      request.username, 
      request.message,
      request.latitude,
      request.longitude,
      request.bloodgroup,
      request.quantity,
      function() {
        requestSnapshot.ref.remove();
      }
    );
  }, function(error) {
    console.error(error);
  });
};

function sendNotificationToUser(username, message, latitude, longitude, bloodgroup, quantity, onSuccess) {
  console.error("Message from " + username + " : " + message);
  request({
    url: 'https://fcm.googleapis.com/fcm/send',
    method: 'POST',
    headers: {
      'Content-Type' :' application/json',
      'Authorization': 'key='+API_KEY
    },
    body: JSON.stringify({
      data: {
        title: username,
        body: message,
        lat: latitude,
        lng: longitude,
        grp: bloodgroup,
        qty: quantity
      },
      to : '/topics/notifs'
    })
  }, function(error, response, body) {
    if (error) { console.error(error); }
    else if (response.statusCode >= 400) { 
      console.error('HTTP Error: '+response.statusCode+' - '+response.statusMessage); 
    }
    else {
      onSuccess();
    }
  });
}

// start listening
listenForNotificationRequests();