var angular = require('angular');

angular.module('whatToLearnNext.localStorageService', [])

.factory('localStorageService', [function () {
    return {
        setItem: function (key, value) {
            if (JSON && JSON.stringify && localStorage) {
                localStorage.setItem(key, JSON.stringify(value));
            }
        },

        getItem: function (key) {
            if (JSON && JSON.parse && localStorage) {
                var value = localStorage.getItem(key);
                return JSON.parse(value);
            }

            return null;
        },

        clear: function () {
            if (localStorage) {
                localStorage.clear();
            }
        }
    }
}]);