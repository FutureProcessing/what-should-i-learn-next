var angular = require('angular');
var _ = require('underscore');
require('./technologyService');
require('./lib/angucomplete');

angular.module('whatToLearnNext', [
    'whatToLearnNext.technologyService',
    "angucomplete"
])

.controller('mainController', ['$scope', 'technologyService', function ($scope, technologyService) {
    
    $scope.knownTechnologies = [];
    $scope.wantedTechnologies = [];
    $scope.avoidTechnologies = [];
    $scope.suggestedTechnologies = [];
    
    $scope.removeKnown = function (tech) {
        $scope.knownTechnologies = _($scope.knownTechnologies).without(tech);
    };

    $scope.removeWanted = function (tech) {
        $scope.wantedTechnologies = _($scope.wantedTechnologies).without(tech);
    };

    $scope.removeAvoid = function (tech) {
        $scope.avoidTechnologies = _($scope.avoidTechnologies).without(tech);
    };
    
    $scope.alreadyKnow = function (tech) {
        $scope.knownTechnologies.push(tech);
        $scope.suggestedTechnologies = _($scope.suggestedTechnologies).without(tech);
    };
    
    $scope.wanted = function (tech) {
        $scope.wantedTechnologies.push(tech);
        $scope.suggestedTechnologies = _($scope.suggestedTechnologies).without(tech);
    };

    $scope.avoid = function (tech) {
        $scope.avoidTechnologies.push(tech);
        $scope.suggestedTechnologies = _($scope.suggestedTechnologies).without(tech);
    };
    
    $scope.keyPress = function (event) {
        if (event.which === 13) {
            // TODO find better selection;
            if ($scope.technologyPredictions && $scope.technologyPredictions.length > 0) {
                $scope.query = undefined;
                $scope.knownTechnologies.push($scope.technologyPredictions[0]);
            }
        }
    };
    
    $scope.$watch('query', function (query) {
        if (!query) {
            $scope.technologyPredictions = undefined;
            return;
        }
        
        technologyService.getTechnologyPredictions(query).then( function (technologies) {
            $scope.technologyPredictions = _(technologies).difference($scope.knownTechnologies.concat($scope.wantedTechnologies));
        });
    });


    $scope.$watch('knownTechnologies', function (knownTechnologies) {
        if(knownTechnologies && knownTechnologies.length > 0) {
            technologyService.getTechnologySuggestions(knownTechnologies.concat($scope.wantedTechnologies), $scope.avoidTechnologies).then(function (technologies) {
                $scope.suggestedTechnologies = technologies;
            });
        }
    }, true);

    $scope.$watch('avoidTechnologies', function (avoidTechnologies) {
        if(avoidTechnologies && avoidTechnologies.length > 0) {
            technologyService.getTechnologySuggestions($scope.knownTechnologies.concat($scope.wantedTechnologies), avoidTechnologies).then(function (technologies) {
                $scope.suggestedTechnologies = technologies;
            });
        }
    }, true);
    
    console.log($scope);
    $scope.ddd = "Welcome Angular";
}]);
