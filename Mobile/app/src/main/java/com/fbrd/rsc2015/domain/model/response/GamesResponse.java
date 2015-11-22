package com.fbrd.rsc2015.domain.model.response;

import com.example.loginmodule.model.response.Response;
import com.fbrd.rsc2015.domain.model.request.Team;

import java.util.List;

/**
 * Created by noxqs on 22.11.15..
 */
public class GamesResponse extends Response {

    private List<Data> list;

    public List<Data> getList() {
        return list;
    }

    public class Data{

        private int id;
        private int length;
        private boolean started;
        private boolean finished;
        private Team team;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public boolean isStarted() {
            return started;
        }

        public void setStarted(boolean started) {
            this.started = started;
        }

        public boolean isFinished() {
            return finished;
        }

        public void setFinished(boolean finished) {
            this.finished = finished;
        }

        public Team getTeam() {
            return team;
        }

        public void setTeam(Team team) {
            this.team = team;
        }
    }
}
