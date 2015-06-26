whatToLearnNextApp.controller('menuController', ['$scope', '$route', '$rootScope', function ($scope, $route, $rootScope) {
    
    $scope.$on('$locationChangeSuccess', function(next, current) { 
        $scope.selected = $route.current.selected;
    });
    
}]);