var LineByLineReader = require('line-by-line');
var elasticSearch = require('elasticsearch');
var hostAdress = process.argv[2];

var client = new elasticSearch.Client({
    host: hostAdress,
    minSockets: 10,
    maxSockets: 50
});

var lr = new LineByLineReader('./part-r-00000');

var i = 0;
var previous = null;
var bulk = [];

lr.on('line', function (line) {

    var variables = line.split('\t');
    var t1 = variables[0];
    var t2 = variables[1];
    var v = parseInt(variables[2]);


    bulk.push({
        index: {
            _index: "technologies",
            _type: "relations",
            _id: t1 + "_" + t2
        }
    });
    bulk.push({
        t1: t1,
        t2: t2,
        v: v
    });

    if (previous !== t1) {
        previous = t1;

        bulk.push({
            index: {
                _index: "technologies",
                _type: "list",
                _id: t1
            }
        });

        bulk.push({
            name: t1
        });
    }


    if (bulk.length > 50) {
        client.bulk({
            body: bulk
        }, function (error, result, status) {
            if (status !== 200){
                console.log("" + i + " - " + status + " - " + line.toString());
            }
        });
        bulk = [];
    }

    i++;

});



