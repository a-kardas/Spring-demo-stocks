'use strict';


angular.module('stockApp')
    .controller('RegisterCtrl', function ($scope, RegisterRsc, $location, StockRsc) {

        $scope.newUser = {
            stocks : []
        };

        $scope.register = function () {
            if($scope.newUser.password !== $scope.newUser.repeatedPassword){
                Materialize.toast('Hey, hey, hey! Passwords are not the same', 4000);
                return;
            } else {
                register($scope.newUser);
            }
        }

        var register = function (data) {
            RegisterRsc.user(data, function(){
                Materialize.toast('Thanks! Now you can log in!', 4000);
                $location.path("/email");
            }, function(){
                Materialize.toast('Error! Probably entered e-mail address is in use.', 4000);
                return;
            })
        }

        var _getStocks = function(){
            StockRsc.publicList({}, function(data){
                for(var i = 0; i < data.length; i++){
                   // $scope.newUser.stocks[i] = { stock : data.items[i], amount : undefined}
                    $scope.newUser.stocks[i] = data[i];
                }

            }, function () {
                Materialize.toast('Error! Cannot load stocks. You can fill it later in settings section.', 4000);
            })
        }
        _getStocks();
    });
