whatToLearnNextApp.directive('menu', ['$route', '$window', function($route, $window){
    return {
        restrict: 'A',
        replace: true,
        templateUrl: 'menu.html',        
        link: function(scope, element) {
            var clickHandler = function () {
                if(scope.isMenuVisible) {
                    scope.isMenuVisible = false;
                    scope.$apply();
                }
            };
            
            angular.element($window).bind('click', clickHandler);
            
            scope.$on('$destroy', function() {
                angular.element($window).unbind('click', clickHandler);
            });
        },
        controller: function ($scope, $element) {
            $scope.isMenuVisible = false;
            $scope.selected = $route.current.selected;
            
            $scope.$on('$locationChangeSuccess', function(next, current) { 
                $scope.selected = $route.current.selected;
            });
            
            $scope.toggleMenu = function($event) {
                $scope.isMenuVisible = !$scope.isMenuVisible;
                $event.stopPropagation();
            };
        }
    } 
}])