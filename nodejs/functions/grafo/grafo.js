// Prototypes - https://tableless.com.br/dominando-o-uso-de-prototype-em-javascript/

const Vertice = require('./vertice.js');

function Grafo(profundidadeMax, grafoDbRef)
{
    this.vertices = {};

    // Profundidade máxima de leitura do grafo
    this.profundidadeMax = profundidadeMax;

    this.grafoDbRef = grafoDbRef;
}

// Em construção
// Tratar p/ n enviar ou mostrar o usuário logado na recomendação
// https://www.youtube.com/watch?v=-he67EEM6z0&t=94s
Grafo.prototype.bfs = function(uid)
{
    let filaVertices = [];

    let verticeAtual = this.vertices[uid];
    verticeAtual.visitado = true;

    filaVertices.push(verticeAtual);

    while(filaVertices.length > 0)
    {
        verticeAtual = filaVertices.shift();
        
        verticeAtual.vizinhos.forEach(vizinhoid => 
        {
            verticeVizinho = this.vertices[vizinhoid];

            verticeVizinho.pontuacao++;

            if(!verticeVizinho.visitado)
            {
                verticeVizinho.visitado = true;
                filaVertices.push(verticeVizinho);
            }
        });
    }
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
        console.log(this.vertices[key]);
        this.vertices[key].vizinhos.forEach(vizinho => {
            console.log("    " + vizinho);
        });
    }
}

Grafo.prototype.imprimirPontuacao = function()
{    
    for(var key in this.vertices)
        console.log(key + " = " + this.vertices[key].pontuacao);
}

Grafo.prototype.addVertice = function(chave, vertice, vizinhos)
{
    vizinhos.forEach(vizinho => {
        vertice.addVizinho(vizinho.key);
    });

    this.vertices[chave] = vertice;
}

module.exports = Grafo;