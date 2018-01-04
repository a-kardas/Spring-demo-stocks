'use strict';

angular.module('stockApp').factory('AuthRsc', function($resource) {
    return $resource('api/public/authenticate', {}, {
        'isAuthenticate' : {
            method: 'GET'
        },
        'login' : {
            method: 'POST'
        },
        'logout' : {
            url: 'api/public/logout',
            method: 'GET'
        }
    });
});
