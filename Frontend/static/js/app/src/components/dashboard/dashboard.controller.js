class DashboardController {
    constructor($http, MainConfig, $interval) {
        var dashController = this;
        this.$http = $http;
        this.MainConfig = MainConfig;
        this.teams = [];
        this.iUrl = 'http://95.85.26.58:6767?team=' + MainConfig.shared.teamIds[0];
        this.teamIds = MainConfig.shared.teamIds[0];
        this.$interval = $interval;
        this.isGameActive = false;
        this.navigation = [{
            url: '/dashboard/add/',
            label: 'Add'
        }, {
            url: '/dashboard/users/',
            label: 'Users'
        }, {
            url: '/dashboard/stats',
            label: 'Statistics'
        }];

        $(document).on('click', '#details-info__next--first', function(e) {
            e.preventDefault();
            $('.map-wrapper').fadeOut(400);
            $('.team-wrapper').fadeIn(400);
            $('#details-info__next--inactive').removeClass('step-current');
            $('#details-info__next--first').addClass('step-current');
        });
        $(document).on('click', "#details-info__next--second", function(e) {
            e.preventDefault();
            $('.team-wrapper').fadeOut(400).css({
                display: 'none'
            });
            $('.users-wrapper').fadeIn(400);
            $('#details-info__next--first').removeClass('step-current');
            $('#details-info__next--second').addClass('step-current');
            dashController.$http.get(dashController.MainConfig.defaultUrl + 'api/1/users').then(function(users) {
                dashController.users = users.data.data;
            });
        });
        $(document).on('click', "#details-info__next--third", function(e) {
            e.preventDefault();
            $('.users-wrapper').fadeOut(400);
            $('.users-wrapper-team2').fadeIn(400);
            var firstTeam = [];
            for (var i = 0; i < dashController.MainConfig.shared.userIds.length; i++) {
                firstTeam.push({
                    userid: dashController.MainConfig.shared.userIds[i],
                    teamid: dashController.MainConfig.shared.teamIds[0]
                });
            }
            $('#details-info__next--second').removeClass('step-current');
            $('#details-info__next--third').addClass('step-current');
            dashController.$http.post(dashController.MainConfig.defaultUrl + 'api/1/teams/addmultiple', firstTeam).then(function(resp) {
                console.log(resp);
            }, function(err) {

            });

            dashController.$http.get(dashController.MainConfig.defaultUrl + 'api/1/users').then(function(users) {
                dashController.users = users.data.data;
            });
        });
        $(document).on('click', "#details-info__next--forth", function(e) {
            e.preventDefault();
            $('.users-wrapper-team2').fadeOut(400);
            $('.define-game').fadeIn(400);
            $('#details-info__next--third').removeClass('step-current');
            $('#details-info__next--forth').addClass('step-current');
            var secondTeam = [];
            for (var i = 0; i < dashController.MainConfig.shared.userIdsTeam2.length; i++) {
                secondTeam.push({
                    userid: dashController.MainConfig.shared.userIdsTeam2[i],
                    teamid: dashController.MainConfig.shared.teamIds[1]
                });
            }
            console.log("SEC TEAM");
            console.log(secondTeam);
            dashController.$http.post(dashController.MainConfig.defaultUrl + 'api/1/teams/addmultiple', secondTeam).then(function(resp) {
                console.log(resp);
            }, function(err) {

            });
        });

        $(document).on('click', "#details-info__next--fifth", function(e) {
            e.preventDefault();
            $('.define-game').fadeOut(400);
            $('.start-game').fadeIn(400);
                $('#details-info__next--forth').removeClass('step-current');
            $('#details-info__next--fifth').addClass('step-current');
        });

        $(document).on('click', '.start-game', function(e) {
            e.preventDefault();
            $('.start-game').fadeOut(400);
            $('.rtc-wrapper').fadeIn(400);
            $('.rtc-wrapper').find('iframe').attr('src', 'http://95.85.26.58:6767?team=' + MainConfig.shared.teamIds[0]);
            $('.rtc-wrapper-second').find('iframe').attr('src', 'http://95.85.26.58:6767?team=' + MainConfig.shared.teamIds[1]);
        });

    }
    
    createTeams(teams) {
        var dCtrl = this;
        this.$http.post(dCtrl.MainConfig.defaultUrl + 'api/1/teams', {
            name: teams.team1,
            longitude: dCtrl.MainConfig.shared.teamsLoc[0].lng,
            latitude: dCtrl.MainConfig.shared.teamsLoc[0].lat,
        }).then(function(resp) {
            dCtrl.MainConfig.shared.teamIds.push(resp.data.data[0].id);
            dCtrl.$http.post(dCtrl.MainConfig.defaultUrl + 'api/1/teams', {
                name: teams.team2,
                longitude: dCtrl.MainConfig.shared.teamsLoc[1].lng,
                latitude: dCtrl.MainConfig.shared.teamsLoc[1].lat,
            }).then(function(resp) {
                console.log(resp);
                 $('.teams-next').fadeIn(400);
                dCtrl.MainConfig.shared.teamIds.push(resp.data.data[0].id);
            }, function(err) {
                console.log(err);
            });
        }, function(err) {
            console.log(err);
        });

    }
    selectTeam(id) {
        var dCtrl = this;
        if (this.MainConfig.shared.teamIds.length == 2) {
            this.MainConfig.shared.teamIds.shift();
            this.MainConfig.shared.teamIds.push(id);
        } else {
            this.MainConfig.shared.teamIds.push(id);
        }
    }
    selectUser(id) {
        var dCtrl = this;
        this.MainConfig.shared.userIds.push(id);
        console.log(this.MainConfig.shared.userIds);
    }
    selectUserTeam2(id) {
        var dCtrl = this;
        this.MainConfig.shared.userIdsTeam2.push(id);
        console.log(this.MainConfig.shared.userIdsTeam2);
    }

    defineGame(game) {
        var defGame = {},
            dCtrl = this;
        console.log(defGame);
        defGame.length = game.length;
        defGame.killPoints = game.killPoints;

        defGame.challengerOne = dCtrl.MainConfig.shared.teamIds[0];
        defGame.challengerTwo = dCtrl.MainConfig.shared.teamIds[1];
        defGame.locations = dCtrl.MainConfig.shared.teamsLoc;
        defGame.mapId = dCtrl.MainConfig.shared.mapId;
        console.log(defGame);
        dCtrl.$http.post(dCtrl.MainConfig.defaultUrl + 'api/1/games', defGame).then(function(resp) {
            dCtrl.MainConfig.shared.gameId = resp.data.data[0].id;

        }, function(err) {
            console.log(err);
        });

    }
    gameStatus() {
        var dCtrl = this;
        dCtrl.$interval(function() {
            dCtrl.$http.post(dCtrl.MainConfig.defaultUrl + 'api/1/games/start/' + dCtrl.MainConfig.shared.gameId).then(function(resp) {
                console.log(resp.data.status);
                if (resp.data.status == 400) {
                    dCtrl.isGameActive = false;
                } else {
                    dCtrl.isGameActive = true;
                }
            }, function() {
                console.log(err);
            });
        }, 1000);
    }
    gameActive() {
        return this.isGameActive;
    }
}
DashboardController.$inject = ['$http', 'MainConfig', '$interval'];
/* beautify preserve:start */
export {DashboardController};
/* beautify preserve:end */
