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
let usuariosRef     = admin.database().ref("usuarios");
let userSeguindoRef = admin.database().ref("userSeguindo");

// Recomendações de usuários para seguir
exports.getRecomendacoesUsuarios = functions.https.onRequest((request, response) => {
    let uid = request.query.uid;

    //let uid = "ECevzocmNxaERZq8KO3lwDiE79Y2";

    if(uid === undefined)
        return;

    let grafo = new Grafo(2, userSeguindoRef);

    let listaVertices = [];

    grafo.lerGrafo(uid, 0).then(terminou => 
    {
        //grafo.imprimirAdjList();
        grafo.bfs(uid);
        grafo.imprimirPontuacao();

        let listaVertices = ordenarLista(Object.values(grafo.vertices));

        return pegarUsuarios(listaVertices);

    }).then(lista => {
        return response.send(lista);

    }).catch(err => {
        console.log(err);
        response.send(err);
    });
});

function ordenarLista(lista)
{
    lista.sort((a, b) => {
        return b.pontuacao - a.pontuacao;
    });

    return lista;
}

function pegarUsuarios(listaVertices)
{
    let promises = [];

    listaVertices.forEach(usuario => 
    {
        let promise = usuariosRef.child(usuario.id).once('value', snapshot => {
            usuario.perfil = snapshot.val();
        });

        promises.push(promise);
    });
    
    return Promise.all(promises).then(() => {
        return listaVertices;
    });
}