var elasticSearch = require('elasticsearch');
var hostAdress = process.argv[2];

var client = new elasticSearch.Client({
    host: hostAdress
});


var createRelationsMapping = function () {
    return client.indices.putMapping({
        index: 'technologies',
        type: 'relations',
        body: {
            relations: {
                properties: {
                    t1: {type: 'string', index: 'not_analyzed'},
                    t2: {type: 'string', index: 'not_analyzed'}
                }
            }
        }
    });
};

var createListMapping = function () {
    return client.indices.putMapping({
        index: 'technologies',
        type: 'list',
        body: {
            list: {
                properties: {
                    name: {
                        type: 'string',
                        analyzer: 'autocomplete'
                    }
                }
            }
        }
    });
};

client.indices.create({
    index: 'technologies',
    body: {
        settings: {
            analysis: {
                filter: {
                    autocomplete_filter: {
                        type: 'edge_ngram',
                        min_gram: 1,
                        max_gram: 20,
                        token_chars: ['letter', 'digit', 'punctuation', 'symbol']
                    }
                },
                analyzer: {
                    autocomplete: {
                        type: 'custom',
                        tokenizer: 'standard',
                        filter: [
                            'lowercase',
                            'autocomplete_filter'
                        ]
                    }
                }
            }
        }
    }
})
    .then(createRelationsMapping)
    .then(createListMapping)
    .then(function () {
        console.log("finished");
        process.exit();
    });

