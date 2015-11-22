/* beautify preserve:start */
import angular from 'angular';
import {mmDirective} from './mm.directive.js';
/* beautify preserve:end */
var config = ($stateProvider) => {
    $stateProvider.state('hackathon.mm', {
        url: '/mapmobile/:teamId/:gameId?',
        views: {
            'main@': {
                template: '<mm></mm>'
            }
        },
        params: {
            gameId: {
                squash: true,
                value: null
            }
        }
    });
};
config.$inject = ['$stateProvider'];
export const mapMobile = angular.module('hackathon.mm', [

    ])
    .config(config)
    .directive('mm', mmDirective);
