'use strict';


angular.module('stockApp')
    .controller('LoginCtrl', function ($scope, AuthRsc, $location) {

        $scope.user = {
            rememberMe : true
        };

        $scope.login = function () {
            var credentials = {
                email: $scope.user.email,
                password: $scope.user.password
            }
            AuthRsc.login(credentials, function(){
                $location.url("/");
            }, function(){
                Materialize.toast('Error! Try again.', 4000);
                return;
            })
        }

    });
