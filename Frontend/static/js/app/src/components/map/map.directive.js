/* beautify preserve:start */
	 import template from './map.tmpl.html';
	 import {MapController as controller} from './map.controller.js'; 
/* beautify preserve:end */
export const mapDirective=()=>{
	return{
		restrict:'E',
		scope:{

		},
		replace:true,
		controller:controller,
		controllerAs:'vm',
		template:template
	};
};