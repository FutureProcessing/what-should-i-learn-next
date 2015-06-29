var elasticSearch = require('elasticsearch');
var Semaphore = require('semaphore');
var MappingEntry = require('./MappingEntry');
var Measured = require('measured');

function ElasticUploader(host, port, indexName) {
    this.elasticClient = new elasticSearch.Client({
        host: host + ':' + port
    });
    this.indexName = indexName;
    this.semaphore = Semaphore(100);

    this.stats = new Measured.Meter();

    this.bulk = [];

    this.handleSuccess = this.handleSuccess.bind(this);
    this.handleError = this.handleError.bind(this);
    this.doRequest = this.doRequest.bind(this);

    setInterval(this.doRequest, 1000);

    setInterval(function (stats) {
        console.log(stats.toJSON());
    }, 1000, this.stats);
}

ElasticUploader.prototype.handleSuccess = function (response) {
    this.semaphore.leave();
};

ElasticUploader.prototype.handleError = function (error) {
    console.error('error:', error);
    this.semaphore.leave();
};


ElasticUploader.prototype.indexRelations = function (entry, isPositive) {

    var mappingScore = isPositive ? entry.value : 0 - entry.value;
    this.bulk.push({
        index: {
            _index: this.indexName,
            _type: "relations",
            _id: entry.key + isPositive
        }
    });

    this.bulk.push({
        t1: entry.t1,
        t2: entry.t2,
        plus: isPositive,
        v: mappingScore
    });
};

ElasticUploader.prototype.indexList = function (entry) {
    this.bulk.push({
        index: {
            _index: this.indexName,
            _type: "list",
            _id: entry.t1
        }
    });

    this.bulk.push({
        name: entry.t1
    });
};


ElasticUploader.prototype.doRequest = function () {
    this.semaphore.take(function () {
        if (this.bulk.length > 0) {
            this.elasticClient.bulk({
                body: this.bulk
            }).then(this.handleSuccess, this.handleError);
            this.bulk = [];
        }
    }.bind(this));
};

ElasticUploader.prototype.initializeRequest = function () {
    if (this.bulk.length > 100) {
        this.doRequest();
    }
};

ElasticUploader.prototype.indexPair = function (line, callback) {

    //console.log(line);

    var entry = new MappingEntry(line);

    this.indexRelations(entry, true);
    this.indexRelations(entry, false);
    this.indexList(entry);

    this.initializeRequest();
    this.stats.mark();
    callback();
};

module.exports = ElasticUploader;
