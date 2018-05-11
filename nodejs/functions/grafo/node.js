exports.Node = function(key)
{
    this.key = key;
    this.edges = [];
    this.visitado = false;
}