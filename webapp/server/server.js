var express = require('express');
var morgan = require('morgan');
var _ = require('underscore');
var config = require("./config.json");
var elasticSearch = require('elasticsearch');

var elasticAddress = config.elastic.address;

'use strict';
var app = express();
var client = new elasticSearch.Client({ host: elasticAddress });

app.use(morgan('combined'));

app.use(express.static(__dirname + '/build/static'));
app.use(express.static(__dirname + '/../client/generated'));
app.use(express.static(__dirname + '/../client/html'));

app.get('/technologyPredictions', function (req, res) {
    var query = req.query.q;

    client.search({
        index: 'technologies',
        type: 'list',
        body: {
            query: {
                bool: {
                    should: [
                        {
                            match: {
                                _id: query
                            }
                        },
                        {
                            match: {
                                name: query
                            }
                        }
                    ]
                }
            }
        }
    }).then(function (result) {
        res.send({
            technologies: _(result.hits.hits).pluck('_id')
        });
    });


});

app.get('/technologySuggestions', function (req, res) {
    var known = req.query.known;
    if(known && !Array.isArray(known)) {
        known = [known];
    }

    var avoid = req.query.avoid;
    if(avoid && !Array.isArray(avoid)) {
        avoid = [avoid];
    }

    client.search({
        index: 'technologies',
        type: 'relations',
        body: {
            query: {
                filtered: {
                    filter: {
                        and: [
                            {
                                terms: {
                                    t1: known
                                }
                            },
                            {
                                not: {
                                    terms: {
                                        t2: _.union(known, avoid)
                                    }
                                }
                            }
                        ]
                    }
                }
            },
            aggs: {
                rel: {
                    terms: {
                        field: 't2',
                        order: {
                            total: 'desc'
                        },
                        size: 5
                    },
                    aggs: {
                        total: {
                            sum: {
                                field: 'v'
                            }
                        }
                    }
                }
            }
        }
    }).then(function (results) {
        res.send({
            technologies: _(results.aggregations.rel.buckets).pluck('key')
        });
    });

});

app.listen(config.http.port);
console.log("Listening on port: " + config.http.port)
