// Prototypes - https://tableless.com.br/dominando-o-uso-de-prototype-em-javascript/

const vertice = require('./vertice.js');
const admin   = require('firebase-admin');

function Grafo(profundidade)
{
    this.db = admin.database();

    this.vertices     = [];
    this.profundidade = profundidade;
}

Grafo.prototype.lerGrafo = function(uid)
{
    let seguindo = [];

    const userSeguindoRef = this.db.ref("userSeguindo").child(uid);
    
    userSeguindoRef.once('value', snasphot => 
    {
        snasphot.forEach(usuario => {
            console.log(usuario);
        });
    });
}

Grafo.prototype.helloWorldGrafo = function(){
    return "Hello World do Grafo";
}

/*Grafo.prototype.addVertice(n)
{
    this.vertices.push(n);
}*/

module.exports = Grafo;