'use strict';


angular.module('stockApp')
  .controller('StockCtrl', function ($scope, StockRsc) {
      var _timeout = null;
      $scope.exchangeRate = {};

      var _getExchangeRate = function() {
          StockRsc.getAll({}, function(data){
              $scope.exchangeRate.stocks = data.items;
              $scope.exchangeRate.publicationDate = data.publicationDate;

          }, function (error) {
              console.log(error);
          })
          _timeout = setTimeout(_getExchangeRate, 10000);
      }

      $scope.$on("$destroy", function() {
          if (_timeout) {
              clearTimeout(_timeout);
          }
      });

      _getExchangeRate();
  });
