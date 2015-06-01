var angular = require('angular');

angular.module('whatToLearnNext.technologyService', [])

.factory('technologyService', ['$http', function($http) {
    
    return {
        getTechnologyPredictions: function (query) {
            return $http.get('/technologyPredictions', {
                params: {
                    q: query
                }
            }).then(function (response) {
                return response.data.technologies;
            });
        },
        
        getTechnologySuggestions: function (knownTechnologies) {
            return $http.get('/technologySuggestions', {
                params: {
                    known: knownTechnologies
                }
            }).then(function (response) {
                return response.data.technologies;
            });
        }
    }
}]);