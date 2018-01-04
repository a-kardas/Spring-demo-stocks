'use strict';

angular
  .module('stockApp', [
    'ngAnimate',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
    'LocalStorageModule'
  ])
    .run(function($rootScope, AuthSrv){
        AuthSrv.isAuthenticated();

        $rootScope.$on('notAuthenticated', function() { $rootScope.isAuthenticated = false; });

        $rootScope.$on('isAuthenticated', function(event, args) {
            $rootScope.isAuthenticated = true;
            $rootScope.username = args.email;
        });

        $rootScope.logout = function () {
            AuthSrv.logout();
        }


    })
    .config(function($httpProvider){
        //$httpProvider.defaults.withCredentials = true;
        $httpProvider.interceptors.push('authExpiredInterceptor');
    });
