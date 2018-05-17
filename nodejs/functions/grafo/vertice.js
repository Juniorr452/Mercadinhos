function Vertice()
{
    this.pontuacao = 0;
    this.vizinhos  = [];
    this.visitado  = false;
}

Vertice.prototype.setVizinhos = function(vizinhos) {
    this.vizinhos = vizinhos;
}

Vertice.prototype.addVizinho = function(vizinho) {
    this.vizinhos.push(vizinho);
}

module.exports = Vertice;