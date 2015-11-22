package com.fbrd.rsc2015.domain.model.event;

import com.dmacan.lightandroid.domain.util.Serializator;
import com.example.loginmodule.model.response.GetUserResponse;

/**
 * Created by david on 21.11.2015..
 */
public class GcmMessageEvent {

    private String action;
    private String data;
    private String message;

    public GcmMessageEvent(String action, String data, String message) {
        this.action = action;
        this.data = data;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getAction() {
        return action;
    }

    public String getDataJSON() {
        return data;
    }

    public GcmMessageEvent.Data getData() {
        return Serializator.deserialize(data, GcmMessageEvent.Data.class);
    }

    public class Data {

        private int myTeam;
        private int enemyTeam;
        private long teamId;
        private long gameId;
        private String url;

        public int getMyTeam() {
            return myTeam;
        }

        public void setMyTeam(int myTeam) {
            this.myTeam = myTeam;
        }

        public int getEnemyTeam() {
            return enemyTeam;
        }

        public void setEnemyTeam(int enemyTeam) {
            this.enemyTeam = enemyTeam;
        }

        public long getTeamId() {
            return teamId;
        }

        public void setTeamId(long teamId) {
            this.teamId = teamId;
        }

        public long getGameId() {
            return gameId;
        }

        public void setGameId(long gameId) {
            this.gameId = gameId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
