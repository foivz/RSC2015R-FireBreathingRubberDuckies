class NavigationController{
	constructor(){
		this.navigation=[{
			state:'hackathon.login',
			label:'Login'
		},{
			state:'hackathon.register',
			label:'Register'
		},{
			state:'hackathon.dashboard',
			label:'Dashboard'
		}];
	}
}

export {NavigationController};
