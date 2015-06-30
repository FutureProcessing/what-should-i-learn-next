var express = require('express');
var morgan = require('morgan');
var Technologies = require('./technologies.js');
var config = require("./config.json");
var path = require('path');

var app = express();
var technologies = new Technologies(config.elastic.address, config.elastic.indexName);

app.use(morgan('combined'));

app.use(express.static(__dirname + '/build/static'));
app.use(express.static(__dirname + '/../client/generated'));
app.use(express.static(__dirname + '/../client/html'));
app.use('/fonts', express.static(__dirname + '/../client/fonts'));
app.use('/images', express.static(__dirname + '/../client/images'));


app.get('/technologyPredictions', function (req, res) {
    var query = req.query.q;

    technologies.getPredictions(query).then(function (predictions) {
        res.send({
            technologies: predictions
        });
    });


});

app.get('/technologySuggestions', function (req, res) {
    var known = req.query.known;
    if(!known) {
        known = [];
    }
    if(!Array.isArray(known)) {
        known = [known];
    }

    var avoid = req.query.avoid;
    if(!avoid) {
        avoid = [];
    }
    if(!Array.isArray(avoid)) {
        avoid = [avoid];
    }


    technologies.getSuggestions(known, avoid).then(function (suggestions) {
        res.send({
            technologies: suggestions
        });
    });

});

// Error handler for 404
app.use(function(req, res, next) {
  res.status(404);

  // respond with html page
  if (req.accepts('html')) {
    res.sendFile(path.resolve(__dirname + '/../client/html/server-404.html'));
    return;
  }

  // respond with json
  if (req.accepts('json')) {
    res.send({ error: 'Not found' });
    return;
  }

  // default to plain-text. send()
  res.type('txt').send('Not found');
});

app.listen(config.http.port);
console.log("Listening on port: " + config.http.port)
