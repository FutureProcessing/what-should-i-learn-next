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

    this.handleSuccess = this.handleSuccess.bind(this);

    setInterval(function (stats) {
        console.log(stats.toJSON());
    }, 1000, this.stats);
}

ElasticUploader.prototype.handleSuccess = function (response) {
    this.stats.mark();
    this.semaphore.leave();
};

ElasticUploader.prototype.handleError = function (error) {
    console.error('error:', error);
};


ElasticUploader.prototype.indexRelations = function (entry, isPositive) {
    this.semaphore.take(function () {
        var mappingScore = isPositive ? entry.value : 0 - entry.value;
        this.elasticClient.index({
            index: this.indexName,
            type: "relations",
            id: entry.key + isPositive,
            body: {
                t1: entry.t1,
                t2: entry.t2,
                plus: isPositive,
                v: mappingScore
            }
        }).then(this.handleSuccess, this.handleError);
    }.bind(this));
};

ElasticUploader.prototype.indexList = function (entry) {
    this.semaphore.take(function () {
        this.elasticClient.index({
            index: this.indexName,
            type: "list",
            id: entry.t1,
            body: {
                name: entry.t1
            }
        }).then(this.handleSuccess, this.handleError);
    }.bind(this));
};

ElasticUploader.prototype.indexPair = function (line) {

    //console.log(line);

    var entry = new MappingEntry(line);

    this.indexRelations(entry, true);
    this.indexRelations(entry, false);
    this.indexList(entry);
};

module.exports = ElasticUploader;
