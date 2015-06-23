whatToLearnNextApp.controller('mainController', ['$scope', 'technologyService', 'localStorageService', function ($scope, technologyService, localStorageService) {

    // Init application and restore its state.
    (function () {
        var knownTechnologies = localStorageService.getItem("knownTechnologies");
        var wantedTechnologies = localStorageService.getItem("wantedTechnologies");
        var avoidTechnologies = localStorageService.getItem("avoidTechnologies");
        var suggestedTechnologies = localStorageService.getItem("suggestedTechnologies");

        $scope.knownTechnologies = knownTechnologies ? knownTechnologies : [];
        $scope.wantedTechnologies = wantedTechnologies ? wantedTechnologies : [];
        $scope.avoidTechnologies = avoidTechnologies ? avoidTechnologies : [];
        $scope.suggestedTechnologies = suggestedTechnologies ? suggestedTechnologies : [];
    })();
           
    $scope.removeKnown = function (tech) {
        $scope.knownTechnologies = _($scope.knownTechnologies).without(tech);
        localStorageService.setItem("knownTechnologies", $scope.knownTechnologies);
    };

    $scope.removeWanted = function (tech) {
        $scope.wantedTechnologies = _($scope.wantedTechnologies).without(tech);
        localStorageService.setItem("wantedTechnologies", $scope.wantedTechnologies);
    };

    $scope.removeAvoid = function (tech) {
        $scope.avoidTechnologies = _($scope.avoidTechnologies).without(tech);
        localStorageService.setItem("avoidTechnologies", $scope.avoidTechnologies);
    };
    
    $scope.alreadyKnow = function (tech) {
        $scope.knownTechnologies.push(tech);
        localStorageService.setItem("knownTechnologies", $scope.knownTechnologies);

        $scope.suggestedTechnologies = _($scope.suggestedTechnologies).without(tech);
        localStorageService.setItem("suggestedTechnologies", $scope.suggestedTechnologies);
    };
    
    $scope.wanted = function (tech) {
        $scope.wantedTechnologies.push(tech);
        localStorageService.setItem("wantedTechnologies", $scope.wantedTechnologies);

        $scope.suggestedTechnologies = _($scope.suggestedTechnologies).without(tech);
        localStorageService.setItem("suggestedTechnologies", $scope.suggestedTechnologies);
    };

    $scope.avoid = function (tech) {
        $scope.avoidTechnologies.push(tech);
        localStorageService.setItem("avoidTechnologies", $scope.avoidTechnologies);

        $scope.suggestedTechnologies = _($scope.suggestedTechnologies).without(tech);
        localStorageService.setItem("suggestedTechnologies", $scope.suggestedTechnologies);
    };

    $scope.clearStorage = function () {
        localStorageService.clear();

        $scope.knownTechnologies = [];
        $scope.wantedTechnologies = [];
        $scope.avoidTechnologies = [];
        $scope.suggestedTechnologies = [];
    };
         
    $scope.$watch('query', function (query) {
        if (!query) {
            $scope.technologyPredictions = undefined;
            return;
        }
        
        technologyService.getTechnologyPredictions(query.toLowerCase()).then( function (technologies) {
            $scope.technologyPredictions = _(technologies).difference($scope.knownTechnologies.concat($scope.wantedTechnologies));
        });
    });

    $scope.$watch('selectedTechnology', function (selectedTechnology) {
        if (!$scope.selectedTechnology)
            return;

        if ($scope.technologyPredictions && $scope.technologyPredictions.length > 0) {
            var foundTechnology = _($scope.technologyPredictions).find(function (technologyName) {
                return technologyName === selectedTechnology.originalObject;
            });

            if (foundTechnology) {
                $scope.knownTechnologies.push(foundTechnology);
                localStorageService.setItem("knownTechnologies", $scope.knownTechnologies);
                $scope.query = null;
                $scope.selectedTechnology = null;
            }
        }
    });

    $scope.refreshTechnologies = function(){
        technologyService.getTechnologySuggestions($scope.knownTechnologies.concat($scope.wantedTechnologies), $scope.avoidTechnologies).then(function (technologies) {
            $scope.suggestedTechnologies = technologies;
        });
    };
    
    $scope.$watch('knownTechnologies', function (knownTechnologies) {
        if(knownTechnologies && knownTechnologies.length > 0) {
            $scope.refreshTechnologies();
        }
    }, true);

    $scope.$watch('avoidTechnologies', function (avoidTechnologies) {
        if(avoidTechnologies && avoidTechnologies.length > 0) {
            $scope.refreshTechnologies();
        }
    }, true);
    
    $scope.$watch('wantedTechnologies', function (wantedTechnologies) {
        if(wantedTechnologies && wantedTechnologies.length > 0) {
            $scope.refreshTechnologies();
        }
    }, true);
    
    console.log($scope);
}]);