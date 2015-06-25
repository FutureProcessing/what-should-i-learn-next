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
    res.writeHead(404, {'Content-Type': 'text/html'});
    res.write('<!DOCTYPE html><html><head><title>FP - Page Not Found</title></head><body>');
    res.write('<div style="position: fixed; top: 0; height: 90%; width: 100%; display: flex; justify-content: center; align-items: center;"><img src="images/404.png"></img></div>');
    res.write('</body></html>');
    res.end();
});

app.listen(config.http.port);
console.log("Listening on port: " + config.http.port)
