// Prototypes - https://tableless.com.br/dominando-o-uso-de-prototype-em-javascript/

const Vertice = require('./vertice.js');

function Grafo(profundidadeMax, grafoDbRef)
{
    this.vertices = {};

    // Profundidade máxima de leitura do grafo
    this.profundidadeMax = profundidadeMax;

    // Referência do db de onde estão os vértices
    this.grafoDbRef = grafoDbRef;
}

Grafo.prototype.bfs = function(uid)
{
    
}

// Ler o grafo recursivamente do db até uma certa profundidade
// Retorna uma promise ou falso, se a profundidade já for máxima
// Promises - https://www.youtube.com/watch?v=726GW5bg3OY 
Grafo.prototype.lerGrafo = function(uid, profundidade)
{
    // Talvez depois eu tire esse vertices.hasOwnProperty, mas vou deixar pra não estragar o que já tá funcionando
    if((profundidade >= this.profundidadeMax) || this.vertices.hasOwnProperty(uid))
        return false;

    let userSeguindoRef = this.grafoDbRef.child(uid);

    return userSeguindoRef.once('value').then(seguindoUids => 
    {
        // Lista de promises,
        let promises = [];

        if(seguindoUids)
        {
            this.addVertice(uid, new Vertice(), seguindoUids);

            seguindoUids.forEach(seguindo => 
            {
                if(!this.vertices.hasOwnProperty(seguindo.key))          
                    promises.push(this.lerGrafo(seguindo.key, profundidade + 1));
            });
        }
        
        // Só vai retornar quando terminar de executar todas as promises da lista.
        return Promise.all(promises);
    });
}

Grafo.prototype.imprimirAdjList = function()
{    
    for(var key in this.vertices)
    {
        console.log(key);
        this.vertices[key].vizinhos.forEach(vizinho => {
            console.log("    " + vizinho);
        });
    }
}

Grafo.prototype.addVertice = function(chave, vertice, vizinhos)
{
    vizinhos.forEach(vizinho => {
        vertice.addVizinho(vizinho.key);
    });

    this.vertices[chave] = vertice;
}

module.exports = Grafo;