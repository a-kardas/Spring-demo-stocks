'use strict';

angular.module('stockApp').factory('StockRsc', function($resource) {
  return $resource('api/stock', {}, {
      'getAll' : {
          url: 'api/stock/all',
          method: 'GET',
          isArray: true
      },
      'publicList' : {
          url: 'api/stock/public/list',
          method: 'GET',
          isArray: true
      },
      'buy' : {
          url: 'api/stock/buy',
          method: 'POST'
      },
      'sell' : {
          url: 'api/stock/sell',
          method: 'POST'
      }

  })
});