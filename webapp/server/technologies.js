var elasticSearch = require('elasticsearch');
var _ = require('underscore');

var Technologies = function (host, index) {
    this.client = new elasticSearch.Client({ host: host });
    this.index = index;
};

Technologies.prototype.getPredictionsRaw = function (query) {
    return this.client.search({
        index: this.index,
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
    });
};

Technologies.prototype.getSuggestionsRaw = function (known, avoid) {
    return this.client.search({
        index: this.index,
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
    });
};

Technologies.prototype.getPredictions = function (query) {
    return this.getPredictionsRaw(query).then(function (result) {
        return _(result.hits.hits).pluck('_id');
    });
};

Technologies.prototype.getSuggestions = function (known, avoid) {
    return this.getSuggestionsRaw(known, avoid).then(function (results) {
        return _(results.aggregations.rel.buckets).pluck('key');
    });
};

module.exports = Technologies;