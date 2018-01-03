
/*
angular.module('stockApp').factory('AuthRsc', function($resource, $rootScope) {
    return $resource('api/public/login', {}, {
        'login' : {
            method: 'POST'
        }
    });
});*/


angular.module('stockApp').factory('AuthRsc', function($resource) {
    return $resource('api/public/authenticate', {}, {
        'isAuthenticate' : {
            method: 'GET'
        }
    });
});
