class MapController {
    constructor($http, MainConfig, $localStorage) {
        var mapController = this;
        this.MainConfig = MainConfig;
        this.$localStorage = $localStorage;
        this.$http = $http;
        var map = L.map('map', {
            drawControl: true
        }).setView([46.454468, 16.420155], 17);
        L.tileLayer(
            'http://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}', {
                maxZoom: 18,
            }).addTo(map);
        var drawnItems = new L.FeatureGroup();
        map.addLayer(drawnItems);

        // Initialise the draw control and pass it the FeatureGroup of editable layers
        var drawControl = new L.Control.Draw({
            edit: {
                featureGroup: drawnItems
            },
            draw: {
                polygon: false,
                circle: false,

            }
        });

        map.addControl(drawControl);
        map.on('draw:created', function(e) {
            var type = e.layerType,
                layer = e.layer;
            if (type === 'marker') {
                mapController.MainConfig.shared.teamsLoc.push(layer.getLatLng());
            } else {
                mapController.MainConfig.shared.areaLoc.push(layer.getLatLngs());
                mapController.$http.post(mapController.MainConfig.defaultUrl + 'api/1/maps', mapController.MainConfig.shared.areaLoc[0]).then(function(resp) {
                   mapController.MainConfig.shared.mapId=resp.data.data[0].id;

                }, function(err) {
                    console.log(err);
                });
            }

            // Do whatever else you need to. (save to db, add to map etc)
            map.addLayer(layer);
        });
    }
}
MapController.$inject = ['$http', 'MainConfig', '$localStorage'];
/* beautify preserve:start */
    export {MapController};
/* beautify preserve:end */
