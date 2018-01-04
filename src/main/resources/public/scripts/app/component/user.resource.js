'use strict';

angular.module('stockApp').factory('UserRsc', function($resource) {
    return $resource('api/user', {}, {
        'get' : {
            url: 'api/user/get',
            method: 'GET'
        }

    })
});