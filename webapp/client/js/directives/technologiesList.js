whatToLearnNextApp.directive('technologiesList', function(){
    return {
        restrict: 'A',
        templateUrl: 'technologiesList.html',
        scope: {
            title: '=',
            titleStyle: '=',
            technologies: '=',
            remove: '&'
        },
        link: function (scope) {
        }
    } 
})