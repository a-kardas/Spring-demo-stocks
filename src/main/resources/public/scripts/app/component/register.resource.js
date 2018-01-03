
angular.module('stockApp').factory('RegisterRsc', function($resource, $rootScope) {
    return $resource('api/users', {}, {
        'user' : {
            method: 'POST'
        }
    });
});