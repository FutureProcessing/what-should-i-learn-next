var expect = require('chai').expect;
var elasticSearch = require('elasticsearch');
var Technologies = require('../server/technologies');
var initializeIndices = require('../../big-data/elastic-initialization/initializeIndices');


var host = 'localhost:9200';
var index = 'lists_test';

var client = new elasticSearch.Client({
    host: host
});

var technologies = new Technologies(host, index);

var createEntry = function (tech) {
    return client.index({
        index: index,
        type: 'list',
        id: tech,
        body: {
            name: tech
        }
    }).then(function () { return client.indices.refresh( { index: index, force: true }); });
};

describe('List Index', function () {

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

    it('should find only technologies starting with', function (done) {
        // given
        createEntry('spring')
            .then(createEntry('java'))

            // when
            .then(function () { return technologies.getPredictionsRaw('j') })

            // then
            .then(function (results) {
                var hits = results.hits.hits;

                expect(hits).to.have.length(1);
                expect(hits[0]._id).to.eql('java');

                done();
            });
    });

    it('should put exact match on first position', function (done) {
        // given
        createEntry('java-ee')
            .then(createEntry('java'))
            .then(createEntry('javascript'))

            // when
            .then(function () { return technologies.getPredictionsRaw('java') })

            // then
            .then(function (results) {
                var hits = results.hits.hits;

                expect(hits).to.have.length(3);
                expect(hits[0]._id).to.eql('java');

                done();
            });
    });

});
