'use strict';


angular.module('stockApp')
    .controller('BuyStockCtrl', function ($scope, $uibModalInstance, stock) {

        $scope.stock = stock;

        $scope.userStock = {
            amount : 0
        }

        $scope.buy = function () {
            $uibModalInstance.close($scope.stock);
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    });
