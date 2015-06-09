var expect = require('chai').expect;
var elasticSearch = require('elasticsearch');
var Technologies = require('../server/technologies');
var initializeIndices = require('../distribution/initializeIndices');


var host = 'localhost:9200';
var index = 'relations_test';

var client = new elasticSearch.Client({
    host: host
});

var technologies = new Technologies(host, index);

var createRelation = function (t1, t2, v) {
    var data = function (t1, t2, v, plus) {
        return {
            index: index,
            type: "relations",
            id: t1 + "_" + t2 + "_" + plus,
            body: {
                t1: t1,
                t2: t2,
                v: v,
                plus: plus
            }
        };
    };

    return client.index(data(t1, t2, v, true))
        .then( function () { return client.index(data(t1, t2, v, false)); })
        .then( function () { return client.indices.refresh( { index: index, force: true }); });
};

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

    it('should find only positive values when no avoid specified', function (done) {
        // given
        createRelation('java', 'spring', 1000)

            // when
            .then(function () { return technologies.getSuggestionsRaw(['java'], []) })

            // then
            .then(function (results) {
                console.log(results.aggregations.rel.buckets);
                var buckets = results.aggregations.rel.buckets;

                expect(buckets).to.have.length(1);
                expect(buckets[0].key).to.eql('spring');
                expect(buckets[0].doc_count).to.eql(1);
                expect(buckets[0].total.value).to.eql(1000);

                done();
            });
    });

});
