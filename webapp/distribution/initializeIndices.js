var elasticSearch = require('elasticsearch');
var hostAddress = process.argv[2];
var indexName = process.argv[3];

var client = new elasticSearch.Client({
    host: hostAddress
});


var createRelationsMapping = function () {
    return client.indices.putMapping({
        index: indexName,
        type: 'relations',
        body: {
            relations: {
                properties: {
                    t1: {type: 'string', index: 'not_analyzed'},
                    t2: {type: 'string', index: 'not_analyzed'},
                    plus: {type: 'boolean'}
                }
            }
        }
    });
};

var createListMapping = function () {
    return client.indices.putMapping({
        index: indexName,
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
    index: indexName,
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

