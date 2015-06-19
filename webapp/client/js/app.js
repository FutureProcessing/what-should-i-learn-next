angular = require('angular');
_ = require('underscore');
require('./technologyService');
require('./lib/angucomplete');
require('./lib/angularTooltips');

whatToLearnNextApp = angular.module('whatToLearnNext', [
    'whatToLearnNext.technologyService',
    'angucomplete',
    '720kb.tooltips'
]);