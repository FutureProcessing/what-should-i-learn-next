whatToLearnNextApp.config(['$routeProvider', function($routeProvider) {
    $routeProvider.
        when('/about', {
            templateUrl: 'about.html',
            selected: 'about'
        }).  
        when('/', {
            templateUrl: 'main.html',
            controller: 'mainController',
            selected: 'main'
        }).
        otherwise({
            templateUrl: '404.html'
        });
}]);