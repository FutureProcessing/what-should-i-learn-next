var express = require('express');
var morgan = require('morgan');
var Technologies = require('./technologies.js');
var config = require("./config.json");


var app = express();
var technologies = new Technologies(config.elastic.address, config.elastic.indexName);

app.use(morgan('combined'));

app.use(express.static(__dirname + '/build/static'));
app.use(express.static(__dirname + '/../client/generated'));
app.use(express.static(__dirname + '/../client/html'));

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

app.listen(config.http.port);
console.log("Listening on port: " + config.http.port)
