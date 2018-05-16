// Prototypes - https://tableless.com.br/dominando-o-uso-de-prototype-em-javascript/

const Vertice = require('./vertice.js');
const admin   = require('firebase-admin');

function Grafo(profundidadeMax)
{
    this.vertices = {};
    this.profundidadeMax = profundidadeMax;
    this.profundidade = 0;

    this.db = admin.database();
}

Grafo.prototype.bfs = uid =>
{
    
}

Grafo.prototype.lerGrafo = uid =>
{
    if(this.profundidade > this.profundidadeMax)
    {
        this.profundidade = 0;
        return false;
    }

    this.profundidade++;

    console.log(this.profundidade);

    let vertice = new Vertice();
    let userSeguindoRef = this.db.ref("userSeguindo").child(uid);

    return userSeguindoRef.once('value').then(seguindoUids => 
    {
        if(seguindoUids)
        {
            seguindoUids.forEach(seguindo => 
            {
                let seguindoUid = seguindo.key;
                this.addVizinhoVertice(seguindoUids);

                if(this.vertices[uid] === undefined)
                    lerGrafo(seguindoUid);
            });
        }
            
        return false;
    });
}

/*Grafo.prototype.lerUsuarioSeguindo = uid =>
{
    let seguindo = [];

    const userSeguindoRef = this.db.ref("userSeguindo").child(uid);
    
    // https://firebase.googleblog.com/2016/01/keeping-our-promises-and-callbacks_76.html
    userSeguindoRef.once('value').then(snapshot => 
    {
        let seguindoUids = snapshot.keys;
        console.log(seguindoUids);

        return seguindoUids;
    }).catch(err =>{
        console.log(err);
    });
}*/

Grafo.prototype.imprimirAdjList = () =>
{
    console.log(vertices);
    /*this.vertices.forEach(vertice => {

    });*/
}

Grafo.prototype.addVertice = (chave, vertice) => {
    this.vertices[chave] = vertice;
}

Grafo.prototype.addVizinhoVertice = (chave, vizinhos) => {
    this.vertice[chave].addVizinhoVertice(vizinhos);
}

Grafo.prototype.helloWorldGrafo = () => {
    return "Hello World do Grafo";
}

module.exports = Grafo;