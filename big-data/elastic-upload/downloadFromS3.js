var args = require('minimist')(process.argv);
var AWS = require('aws-sdk');
var Semaphore = require('semaphore');
var ElasticUploader = require('./ElasticUploaderBulk');


var uploader = new ElasticUploader('vm-wsiln-02', 9200, 'sstech03');


var s3 = new AWS.S3();
var s3Bucket = 'ss-hadoop';

var byline = require('byline');

//ensures max two files being processed at a time from S3
var s3Semaphore = Semaphore(1);
var elasticSemaphore = Semaphore(20);

var params = {
    Bucket: s3Bucket,
    Prefix: "output/output04/part"
};

s3.listObjects(params, function (err, data) {
    if (err) {
        console.log(err, err.stack);
    } else {
        console.log("objects to download from S3: " + data.Contents.length);

        data.Contents.forEach(processPartFile);
    }
});

var processPartFile = function (s3ObjectSummary) {
    s3Semaphore.take(function () {

        var count = 0;

        console.log('Downloading content of ' + s3ObjectSummary.Key + ' from ' + s3Bucket);
        var readStream = s3.getObject({Bucket: s3Bucket, Key: s3ObjectSummary.Key}).createReadStream();

        var streamByLine = byline(readStream);

        streamByLine.on('data', function (line) {
            elasticSemaphore.take(function () {
                uploader.indexPair(line.toString('utf-8'), function () {
                    elasticSemaphore.leave();
                });
                if (count == 0) {
                    console.log('Start stream');
                }
                count++;
            });

        });

        streamByLine.on('end', function () {
            console.log('END - no. of lines read: ' + count);
            s3Semaphore.leave();
        });

    });

};

