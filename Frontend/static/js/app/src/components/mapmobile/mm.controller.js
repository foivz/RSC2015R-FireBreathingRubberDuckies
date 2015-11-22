class MapMobileController {
    constructor($stateParams, $http, MainConfig, $interval) {
        var mm = this;
        this.$stateParams = $stateParams;
        this.teamId = $stateParams.teamId;
        this.gameId = MainConfig.shared.gameId;
        this.$http = $http;
        this.MainConfig = MainConfig;
        this.$interval = $interval;
        this.spectatorOneMarker = 0;
        this.spectatorTwoMarker = 0;
        this.markersArr = [];
        this.spectatorOneMarkerLng = 0;
        this.spectatorTwoMarkerLng = 0;
        var mapmobile = L.map('mapmobile', {
            drawControl: true
        }).setView([46.454468, 16.420155], 17);
        L.tileLayer(
            'http://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}', {
                maxZoom: 18,
            }).addTo(mapmobile);
        if ($stateParams.gameId != 0) {

            console.log("Game id", $stateParams.gameId);
            mm.$interval(function() {
                this.getSpectatorMap(mapmobile);
            }.bind(this), 2000);
        } else {

            this.getLocation(mapmobile);

        }
    }
    getLocation(map) {
        var mm = this;
        mm.$http.get(mm.MainConfig.defaultUrl + 'api/1/maps/teammates/' + mm.teamId + '/0/').then(function(coords) {
            console.log(coords);
            for (var i = 0; i < coords.data.length; i++) {

                var marker = new L.marker([coords.data[i].lat, coords.data[i].lng])
                    .addTo(map);
                console.log(marker);
            }
        }, function(err) {
            console.log(err);
        });
    }
    getSpectatorMap(map) {
        var mm = this;
        console.log(mm.$stateParams.gameId);

        mm.$http.get(mm.MainConfig.defaultUrl + 'api/1/maps/teammates/0/' + mm.$stateParams.gameId).then(function(coords) {
            console.log(coords);
            if(mm.markersArr.length>0){
                for(var k=0; k<mm.markersArr.length;k++){
                    map.removeLayer(mm.markersArr[k]);
                }
                mm.markersArr=[];
            }
            for (var i = 0; i < coords.data.teamOne.length; i++) {
                if (mm.spectatorOneMarker == 0) {
                    mm.spectatorOneMarker = coords.data.teamOne[i].lat;
                }
                if (mm.spectatorOneMarkerLng == 0) {
                    mm.spectatorOneMarkerLng = coords.data.teamOne[i].lng;
                }

                if (mm.markersArr.length > 0) {
                    map.removeLayer(mm.markersArr[0]);
                    mm.markersArr.shift();
                }
                var marker = new L.marker([mm.spectatorOneMarker + (Math.random() * (0.0020 - 0.0030) + 0.00200), mm.spectatorTwoMarkerLng + (Math.random() * (0.0020 - 0.00300) + 0.00200)])
                    .addTo(map);
                mm.markersArr.push(marker);
            }
            for (var j = 0; j < coords.data.teamOne.length; j++) {
                if (mm.spectatorTwoMarker == 0) {
                    mm.spectatorTwoMarker = coords.data.teamTwo[j].lat;
                }
                if (mm.spectatorTwoMarkerLng == 0) {
                    mm.spectatorTwoMarkerLng = coords.data.teamTwo[j].lng;
                }
                var marker2 = new L.marker([mm.spectatorTwoMarker + (Math.random() * (0.0020 - 0.00300) + 0.00200), coords.data.teamTwo[j].lng + (Math.random() * (0.0020 - 0.00300) + 0.00200)])
                    .addTo(map);
                mm.markersArr.push(marker2);
            }
        }, function(err) {
            console.log(err);
        });
    }
}
MapMobileController.$inject = ['$stateParams', '$http', 'MainConfig', '$interval'];
export {
    MapMobileController
};
