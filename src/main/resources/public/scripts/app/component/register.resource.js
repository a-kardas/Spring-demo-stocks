
angular.module('stockApp').factory('RegisterRsc', function($resource, $rootScope) {
    return $resource('api/public', {}, {
        'user' : {
            url: 'api/public/register',
            method: 'POST'
        }
    });
});