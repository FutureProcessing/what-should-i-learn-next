var elasticSearch = require('elasticsearch');

var create = function (host, index, callback) {
    var client = new elasticSearch.Client({
        host: host
    });


    var createRelationsMapping = function () {
        return client.indices.putMapping({
            index: index,
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
            index: index,
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
        index: index,
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
        .then(callback);

};

if (require.main === module) {
    var host = process.argv[2];
    var index = process.argv[3];
    create(host, index, function () {
        console.log("finished");
        process.exit();
    });
} else {
    module.exports = create;
}
