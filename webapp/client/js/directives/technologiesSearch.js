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
        link: function (scope, element) {
            var magnifier = angular.element(document.getElementById('magnifier'));
            var technologiesSearch = angular.element(document.getElementById('technologiesSearch')).find('input')[0];
            
            magnifier.bind("click",function() {
                console.log(technologiesSearch)
                technologiesSearch.focus();
            })
        }
    } 
})