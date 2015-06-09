var elasticSearch = require('elasticsearch');
var _ = require('underscore');
var config = require("./config.json");

var elasticAddress = config.elastic.address;
var indexName = config.elastic.indexName;

var client = new elasticSearch.Client({ host: elasticAddress });

module.exports = {

    getPredictions: function (query) {
        return client.search({
            index: indexName,
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
             return _(result.hits.hits).pluck('_id');
        });
    },

    getSuggestions: function (known, avoid) {
        return client.search({
            index: indexName,
            type: 'relations',
            body: {
                query: {
                    filtered: {
                        filter: {
                            or: [
                                {
                                    and: [
                                        {
                                            term: {
                                                plus: true
                                            }
                                        },
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
                                },
                                {
                                    and: [
                                        {
                                            term: {
                                                plus: false
                                            }
                                        },
                                        {
                                            terms: {
                                                t1: avoid
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
                            size: 10
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
            return _(results.aggregations.rel.buckets).pluck('key');
        });
    }
};