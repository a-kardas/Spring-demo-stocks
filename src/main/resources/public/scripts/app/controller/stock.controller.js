'use strict';


angular.module('stockApp')
  .controller('StockCtrl', function ($scope, StockRsc, UserRsc) {

      var _timeout = [];
      var default_timeout = 10000;

      $scope.exchangeRate = {};

      function _getExchangeRate() {
          StockRsc.getAll({}, function(data){
              $scope.exchangeRate.stocks = data;
              $scope.exchangeRate.publicationDate = data[0].rate.publicationDate;

          }, function (error) {
              console.log(error);
          })
      }

      function _getUserWallet() {
          UserRsc.get({}, function (data) {
              $scope.user = data;
          }, function () {
              console.log(error);
          })
      }

      function _reloadData() {
          _getUserWallet();
          _getExchangeRate();
      }

      $scope.$on("$destroy", function() {
          for(var i = 0; i < _timeout.length; i++){
              if (_timeout[i]) {
                  clearInterval(_timeout[i]);
              }
          }
      });

      $scope.openBuyStockModal = function (selectedStock) {
          $scope.stock = selectedStock;

          $scope.userStock = {
              name : selectedStock.name,
              code : selectedStock.code,
              amount : selectedStock.rate.unit,
              rate : selectedStock.rate
          }

          $scope.buyStockModal = true;
      }

      $scope.openSellStockModal = function (selectedUserStock) {
          $scope.userStock = {
              name : selectedUserStock.stock.name,
              code : selectedUserStock.stock.code,
              ownAmount : selectedUserStock.amount,
              amount :  selectedUserStock.amount,
              rate :  selectedUserStock.stock.rate
          }
          $scope.sellStockModal = true;
      }

      $scope.sellStock = function () {
          StockRsc.sell($scope.userStock, function(){
              $scope.dismissModal();
              _reloadData();
              Materialize.toast("Success!.", 4000);
          }, function (error) {
              $scope.dismissModal();
              Materialize.toast(error.data.message, 8000);
          })
      }

      $scope.buyStock = function () {
          StockRsc.buy($scope.userStock, function(){
              $scope.dismissModal();
              _reloadData();
              Materialize.toast("Success!.", 4000);
          }, function (error) {
              $scope.dismissModal();
              Materialize.toast(error.data.message, 8000);
          })
      }

      $scope.dismissModal = function () {
          $scope.buyStockModal = false;
          $scope.sellStockModal = false;
      }

      _reloadData();
      _timeout[0] = setInterval(_getExchangeRate, default_timeout);
      _timeout[1] = setInterval(_getUserWallet, default_timeout);
  });
