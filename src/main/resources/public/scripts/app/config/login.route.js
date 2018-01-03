

angular.module('stockApp')
    .config(function ($routeProvider) {

        $routeProvider
            .when('/login', {
                templateUrl: 'views/login.html',
                controller: 'LoginCtrl'
            })
            .otherwise({
                redirectTo: '/login'
            });
    });