// Getting started - https://www.youtube.com/watch?v=QP8sjZuOlFY

const functions = require('firebase-functions');

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

exports.olaMundo = functions.https.onRequest((request, response) => {
    response.send('<object style="width:100%;height:100%;width: 820px; height: 461.25px; float: none; clear: both; margin: 2px auto;" data="https://www.youtube.com/watch?v=_XEON2Tf4PA?autoplay=1"></object>');
});