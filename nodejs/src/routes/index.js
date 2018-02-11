'use strict'

const express = require('express');
const router  = express.Router();

router.get('/', (request, response, next) => {
  response.status(200).send({
    title: "Node Store API",
    version: "0.1.0"
  });
});

module.exports = router;