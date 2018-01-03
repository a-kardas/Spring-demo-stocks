"use strict";

angular.module('stockApp').factory('AuthSrv', function(AuthRsc, localStorageService, $rootScope, $location) {

    var _login = function(){
        $location.url("/login");
    }

    var _isAuthenticated = function() {
        AuthRsc.isAuthenticate({}, function(){
            localStorageService.set('isAuthenticated', true);
            $rootScope.$broadcast('notAuthenticated'); //broadcast event
            _login();
        }, function () {
            localStorageService.remove('isAuthenticated');
            $rootScope.$broadcast('authenticated'); //broadcast event
        });
    }

    return {
        isAuthenticated : _isAuthenticated
    }

});