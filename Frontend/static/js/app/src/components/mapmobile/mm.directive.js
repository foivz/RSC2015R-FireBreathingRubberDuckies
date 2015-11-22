import template from './mm.tmpl.html';
import {MapMobileController as controller} from './mm.controller.js';


export const mmDirective=()=>{
	return {
		restrict:'E',
		scope:{},
		template:template,
		controller:controller,
		controllerAs:'vm',
		replace:true
	};
};	