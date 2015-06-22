angular = require('angular');
_ = require('underscore');
require('./technologyService');
require('./lib/angucomplete');

whatToLearnNextApp = angular.module('whatToLearnNext', [
    'whatToLearnNext.technologyService',
    'angucomplete'
]);