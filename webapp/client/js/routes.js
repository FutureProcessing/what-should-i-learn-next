whatToLearnNextApp.config(['$routeProvider', function($routeProvider) {
    $routeProvider.
        when('/about', {
            templateUrl: 'about.html'
        }).  
        when('/', {
            templateUrl: 'main.html',
            controller: 'mainController'
        }).
        otherwise({
            templateUrl: '404.html'
        });
}]);