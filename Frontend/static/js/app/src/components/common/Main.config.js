class MainConfig{
	constructor(){
		this.defaultUrl='http://firebreathingrubberduckies.azurewebsites.net/';
		this.errors={
			tokenMissing:"Authorization token not found"
		};
		this.shared={
			teamsLoc:[],
			areaLoc:[],
			mapId:0,
			teamIds:[],
			userIds:[],
			userIdsTeam2:[],
			gameId:0
		};
	}
}


export {MainConfig};