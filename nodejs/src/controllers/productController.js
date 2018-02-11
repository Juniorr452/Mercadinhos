'use strict'

var oracledb = require('oracledb');
var dbConfig = require('../dbconfig');

oracledb.autoCommit = true;
oracledb.outFormat  = oracledb.OBJECT; // Já codifica o retorno das consultas como objetos Javascript

// Listar todos os produtos
exports.get = (request, response, next) =>
{
  oracledb.getConnection(
    dbConfig,
    function(err, connection)
    {
      if (err)
      {
        console.log(err.message);
        response.status(400).send({message: "Erro ao conectar com o banco de dados"});
      }
      else
      {
        connection.execute
        (
          "SELECT * FROM PRODUTOS", [],
          function(err, result)
          {
            if (err) 
            {
              console.error(err.message);
              response.status(400).send({mensagem: "Erro ao pegar os produtos", erro: err.message});
            }
            else
            {
              response.status(201).send(result.rows);
              console.log(result.rows);
            }
              
          }
        );

        doRelease(connection);
      }
    }
  );
};

// Inserir um produto
exports.post = (request, response, next) => 
{
  var produto = request.body;

  oracledb.getConnection
  (
    dbConfig,
    function(err, connection)
    {
      if (err)
      {
        console.error(err.message);
        response.status(400).send({message: "Erro ao conectar com o banco de dados"});
      } 
      else
      {
        connection.execute
        (
          "INSERT INTO produtos VALUES (:titulo, :preco)",
          [produto.nome, parseFloat(produto.preco)],
          function(err, result)
          {
            if (err) 
            {
              console.error(err.message);
              response.status(400).send({mensagem: "Erro ao cadastrar o produto", erro: err.message, json: produto});
            }
            else
            {
              response.status(201).send({mensagem: "Produto cadastrado com sucesso", resposta: result});
              console.log(result);
            }
          }
        );

        doRelease(connection);
      }
    }
  );
};

exports.put = (request, response, next) => 
{
  const id = request.params.id;
  response.status(201).send
  ({
    id: id,
    item: request.body
  });
};

exports.delete = (request, response, next) => {
  response.status(200).send(request.body);
}

function doRelease(connection)
{
  connection.close(
    function(err) 
    {
      if (err) 
        console.error(err.message);
    });
}