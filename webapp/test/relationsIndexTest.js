var expect = require('chai').expect;
var elasticSearch = require('elasticsearch');
var initializeIndices = require('../distribution/initializeIndices');


var host = 'localhost:9200';
var index = 'relationsTest';

var client = new elasticSearch.Client({
    host: host
});

describe('Relations Index', function () {

    beforeEach(function (done) {
        initializeIndices(host, index, function () {
            done();
        });
    });

    afterEach(function (done) {
        client.indices.delete({ index: index }, function () {
            done();
        });
    });

    it('should find all related technologies', function () {
        // given

    });

});
