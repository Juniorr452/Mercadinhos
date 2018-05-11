// Getting started - https://www.youtube.com/watch?v=QP8sjZuOlFY
// Todas as funções do Firebase ficam nesse arquivo.

const functions = require('firebase-functions');
const Grafo     = require('./grafo/grafo.js');

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

exports.olaMundoGrafo = functions.https.onRequest((request, response) => {
    var grafo = new Grafo();
    response.send(grafo.helloWorldGrafo());
});

exports.olaMundo = functions.https.onRequest((request, response) => {
    response.send('Hello World!');
});