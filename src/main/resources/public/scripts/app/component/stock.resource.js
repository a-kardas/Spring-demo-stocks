
angular.module('stockApp').factory('StockRsc', function($resource) {
  return $resource('api/stock', {}, {
      'getAll' : {
          url: 'api/stock/all',
          method: 'GET'
      }

  })
});