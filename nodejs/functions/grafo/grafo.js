// Prototypes - https://tableless.com.br/dominando-o-uso-de-prototype-em-javascript/

const Vertice = require('./vertice.js');

function Grafo(profundidadeMax, grafoDbRef)
{
    this.vertices = {};

    // Profundidade máxima de leitura do grafo
    this.profundidadeMax = profundidadeMax;

    this.grafoDbRef = grafoDbRef;
}

Grafo.prototype.bfs = function(uid)
{
    let filaVertices = [];

    // Ignorar vértice atual (você) e seus vizinhos (seus seguidores)
    // para recomendar apenas quem você não conhece
    let verticeAtual     = this.vertices[uid];
    verticeAtual.ignorar = true;

    verticeAtual.vizinhos.forEach(vizinhoid => {
        verticeVizinho = this.vertices[vizinhoid];

        verticeVizinho.ignorar   = true;
        verticeVizinho.pontuacao = 1;

        filaVertices.push(verticeVizinho);
    });

    //filaVertices.push(verticeAtual);

    while(filaVertices.length > 0)
    {
        verticeAtual = filaVertices.shift();
        
        verticeAtual.vizinhos.forEach(vizinhoid => 
        {
            verticeVizinho = this.vertices[vizinhoid];

            if(!verticeVizinho.ignorar)
            {
                verticeVizinho.pontuacao += verticeAtual.pontuacao;

                if(!verticeVizinho.visitado)
                {
                    verticeVizinho.visitado = true;
                    filaVertices.push(verticeVizinho);
                }
            }
        });
    }
}

// Função de início do DFS
// 
Grafo.prototype.dfs = function(uid)
{
    let verticeAtual     = this.vertices[uid];
    verticeAtual.ignorar = true;

    verticeAtual.vizinhos.forEach(vizinhoid => 
    {
        verticeVizinho = this.vertices[vizinhoid];

        verticeVizinho.ignorar   = true;
        verticeVizinho.pontuacao = 1;

        this.dfsRecursivo(verticeVizinho);
    });
}

Grafo.prototype.dfsRecursivo = function(verticeAtual)
{
    verticeAtual.vizinhos.forEach(vizinhoid => 
    {
        verticeVizinho = this.vertices[vizinhoid];

        if(!verticeVizinho.ignorar)
        {
            verticeVizinho.pontuacao += verticeAtual.pontuacao;

            if(!verticeVizinho.visitado)
            {
                verticeVizinho.visitado = true;
                this.dfsRecursivo(verticeVizinho);
            }
        }
    });
}

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
            this.addVertice(new Vertice(uid), seguindoUids);

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
    let adjs;
    for(var key in this.vertices)
    {
        msg = this.vertices[key].id + " => ";

        this.vertices[key].vizinhos.forEach(v => {
            vizinho = this.vertices[v];
            msg += vizinho.id + " (" + vizinho.pontuacao + ") ";
        });

        console.log(msg);
    }
}

Grafo.prototype.imprimirPontuacao = function()
{    
    for(var key in this.vertices)
        console.log(key + " = " + this.vertices[key].pontuacao);
}

Grafo.prototype.addVertice = function(vertice, vizinhos)
{
    vizinhos.forEach(vizinho => {
        vertice.addVizinho(vizinho.key);
    });

    this.vertices[vertice.id] = vertice;
}

module.exports = Grafo;