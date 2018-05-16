function Vertice()
{
    this.pontuacao = 0;
    this.vizinhos  = [];
    this.visitado  = false;
}

Vertice.prototype.setVizinhos = (vizinhos) => {
    this.vizinhos = vizinhos;
}

Vertice.prototype.addVizinho = (vizinho) => {
    this.vizinhos.push(vizinho);
}

module.exports = Vertice;