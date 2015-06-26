var args = require('minimist')(process.argv);
var AWS = require('aws-sdk');
var fs = require('fs');
var awsS3Client = new AWS.S3();
var s3Stream = require('s3-upload-stream')(awsS3Client);


console.log("Arguments: ");
console.dir(args);
console.log("Uplading file: " + args.file);

AWS.config.update({region: 'us-west-2'});

var read = fs.createReadStream(args.file);
var upload = s3Stream.upload({
    Bucket: args.s3Bucket,
    Key: args.s3Key
});

upload.maxPartSize(20 * 1024 * 1024); // 20 MB
upload.concurrentParts(10);

upload.on('error', function (error) {
    console.log('error');
    console.error(error);
});

var megabate = 1024 * 1024;

upload.on('part', function (details) {
    var receivedSize = (details.receivedSize / megabate);
    var uploadedSize = (details.uploadedSize / megabate);

    console.log('Part ' + details.PartNumber + ', receivedSize: ' + receivedSize + 'MB, uploadedSize: ' + uploadedSize + 'MB');
});

upload.on('uploaded', function (details) {
    console.log(details);
});

read.pipe(upload);
