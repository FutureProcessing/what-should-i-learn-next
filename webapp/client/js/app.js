angular = require('angular');
_ = require('underscore');
require('./technologyService');
require('./localStorageService');
require('./lib/angucomplete');

whatToLearnNextApp = angular.module('whatToLearnNext', [
    'whatToLearnNext.technologyService',
    'whatToLearnNext.localStorageService',
    'angucomplete'
]);