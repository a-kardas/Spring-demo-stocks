angular.module('stockApp')
    .factory('authExpiredInterceptor', function ($rootScope, $q, $injector, localStorageService) {
        return {
            responseError: function (response) {
                // session has expired
                if (response.status === 401 ||  response.status === 403 || response.status === 419) {
                    localStorageService.remove('isAuthenticated');
                    $rootScope.$broadcast('notAuthenticated'); //broadcast event
                }
                return $q.reject(response);
            }
        };
    });
