
angular.module('stockApp').factory('StockRsc', function($resource) {
  return $resource('api/stock', {}, {

  })
});