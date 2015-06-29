angular = require('angular');
require('angular-route');
_ = require('underscore');
require('./technologyService');
require('./localStorageService');
require('./lib/angucomplete');

whatToLearnNextApp = angular.module('whatToLearnNext', [
    'ngRoute',
    'whatToLearnNext.technologyService',
    'whatToLearnNext.localStorageService',
    'angucomplete'
]);