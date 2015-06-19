whatToLearnNextApp.directive('technologiesSearch', function(){
    return {
        restrict: 'A',
        replace: true,
        templateUrl: 'technologiesSearch.html',
        scope: {
            selectedTechnology: '=',
            technologyPredictions: '=',
            query: '='
        },
        link: function (scope) {
        }
    } 
})