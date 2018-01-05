'use strict';


angular.module('stockApp')
  .controller('StockCtrl', function ($scope, StockRsc, UserRsc, $location, $route) {

      var _timeout = [];

      $scope.exchangeRate = {};

      var _getExchangeRate = function() {
          StockRsc.getAll({}, function(data){
              $scope.exchangeRate.stocks = data;
              $scope.exchangeRate.publicationDate = data[0].rate.publicationDate;

          }, function (error) {
              console.log(error);
          })
          _timeout[0] = setTimeout(_getExchangeRate, 10000);
      }

      var _getUserWallet = function () {
          UserRsc.get({}, function (data) {
              $scope.user = data;
          }, function () {
              console.log(error);
          })
          _timeout[1] = setTimeout(_getUserWallet, 10000);
      }

      $scope.$on("$destroy", function() {
          for(var i = 0; i < _timeout.length; i++){
              if (_timeout[i]) {
                  clearTimeout(_timeout[i]);
              }
          }
      });


      $scope.openBuyStockModal = function (selectedStock) {
          $scope.stock = selectedStock;

          $scope.userStock = {
              name : selectedStock.name,
              code : selectedStock.code,
              amount : 0,
              rate : selectedStock.rate
          }

          $scope.buyStockModal = true;
      }

      $scope.openSellStockModal = function (selectedUserStock) {
          $scope.userStock = selectedUserStock;

          /*$scope.userStock = {
              name : selectedStock.name,
              code : selectedStock.code,
              amount : 0,
              rate : selectedStock.rate
          }*/

          $scope.sellStockModal = true;
      }

      $scope.sellStock = function () {
          StockRsc.sell($scope.userStock, function(){
              $scope.dismissModal();
              $route.reload();
              Materialize.toast("Success!.", 4000);
          }, function (error) {
              Materialize.toast(error.message, 4000);
          })
      }

      $scope.buyStock = function () {
          StockRsc.buy($scope.userStock, function(){
              $scope.dismissModal();
              $route.reload();
              Materialize.toast("Success!.", 4000);
          }, function (error) {
              Materialize.toast(error.message, 4000);
          })
      }

      $scope.dismissModal = function () {
          $scope.buyStockModal = false;
      }

      _getExchangeRate();
      _getUserWallet();
  });
