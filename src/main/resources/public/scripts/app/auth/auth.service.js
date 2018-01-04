"use strict";

angular.module('stockApp').factory('AuthSrv', function(AuthRsc, localStorageService, $rootScope, $location) {

    var _login = function(){
        $location.url("/login");
    }

    var _isAuthenticated = function() {
        AuthRsc.isAuthenticate({}, function(user){
            localStorageService.set('isAuthenticated', true);
            $rootScope.$broadcast('isAuthenticated', user); //broadcast event
        }, function () {
            localStorageService.remove('isAuthenticated');
            $rootScope.$broadcast('notAuthenticated'); //broadcast event
            _login();
        });
    }

    var _logout = function () {
        AuthRsc.logout({}, function(){
            localStorageService.remove('isAuthenticated');
            $rootScope.$broadcast('notAuthenticated'); //broadcast event
            _login();
        }, function () {
            localStorageService.remove('isAuthenticated');
            $rootScope.$broadcast('notAuthenticated'); //broadcast event
            _login();
        })
    }

    return {
        isAuthenticated : _isAuthenticated,
        logout: _logout
    }

});