function Vertice(id)
{
    this.id = id;
    this.pontuacao = 1;
    this.vizinhos  = [];
    this.visitado  = false;
    this.ignorar   = false;
}

Vertice.prototype.setVizinhos = function(vizinhos) {
    this.vizinhos = vizinhos;
}

Vertice.prototype.addVizinho = function(vizinho) {
    this.vizinhos.push(vizinho);
}

module.exports = Vertice;