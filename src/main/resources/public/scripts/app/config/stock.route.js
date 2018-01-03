

angular.module('stockApp')
    .config(function ($routeProvider) {

        $routeProvider
            .when('/', {
                templateUrl: 'views/stock.html',
                controller: 'StockCtrl'
            });
    });