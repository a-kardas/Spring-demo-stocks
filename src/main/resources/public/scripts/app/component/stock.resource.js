'use strict';

angular.module('stockApp').factory('StockRsc', function($resource) {
  return $resource('api/stock', {}, {
      'getAll' : {
          url: 'api/stock/all',
          method: 'GET'
      },
      'publicList' : {
          url: 'api/stock/public/list',
          method: 'GET'
      }

  })
});