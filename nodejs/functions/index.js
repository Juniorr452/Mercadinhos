// Getting started - https://www.youtube.com/watch?v=QP8sjZuOlFY
// Todas as funções do Firebase ficam nesse arquivo.

const functions = require('firebase-functions');
const Grafo     = require('./grafo/grafo.js');
const admin     = require('firebase-admin');

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

admin.initializeApp();
let userSeguindoRef = admin.database().ref("userSeguindo");

// Recomendações de usuários para seguir
exports.getRecomendacoesUsuarios = functions.https.onRequest((request, response) => {
    //let uid = request.query.uid;
    // Tô colocando só meu uid por enquanto
    let uid = "ECevzocmNxaERZq8KO3lwDiE79Y2";

    if(uid === undefined)
        return;

    let grafo = new Grafo(2, userSeguindoRef);

    grafo.lerGrafo(uid, 0).then(terminou => 
    {
        grafo.imprimirAdjList();
        return true;
    }).catch(err => {
        console.log(err);
    });

    response.send("uid = " + uid);
});