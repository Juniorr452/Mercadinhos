// Prototypes - https://tableless.com.br/dominando-o-uso-de-prototype-em-javascript/

const node = require('./node.js');

function Grafo(){
    this.nodes = []; 
}

Grafo.prototype.helloWorldGrafo = function(){
    return "Hello World do Grafo";
}

/*Grafo.prototype.addNode(n)
{
    this.nodes.push(n);
}*/

module.exports = Grafo;