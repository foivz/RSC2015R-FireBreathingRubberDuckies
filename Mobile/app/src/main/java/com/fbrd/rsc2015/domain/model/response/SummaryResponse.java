package com.fbrd.rsc2015.domain.model.response;

import com.example.loginmodule.model.response.Response;

import java.util.List;

/**
 * Created by david on 22.11.2015..
 */
public class SummaryResponse extends Response {

    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data {

        private int totalKills;
        private Challenger challengerOne;
        private Challenger challengerTwo;

        public Challenger getChallengerOne() {
            return challengerOne;
        }

        public void setChallengerOne(Challenger challengerOne) {
            this.challengerOne = challengerOne;
        }

        public Challenger getChallengerTwo() {
            return challengerTwo;
        }

        public void setChallengerTwo(Challenger challengerTwo) {
            this.challengerTwo = challengerTwo;
        }

        public int getTotalKills() {
            return totalKills;
        }

        public void setTotalKills(int totalKills) {
            this.totalKills = totalKills;
        }
    }

    public class Challenger {
        private List<UserResponse> users;
        private String name;

        public List<UserResponse> getUsers() {
            return users;
        }

        public void setUsers(List<UserResponse> users) {
            this.users = users;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public class UserResponse {

        private String userName;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

    }

    @Override
    public String toString() {
        String result = "";
        if (!data.isEmpty()) {
            Data d = data.get(0);
            result += "Total kills: " + d.getTotalKills() + "\n\n";
            result += "Team " + d.getChallengerOne().getName() + "\n";
            for (UserResponse response : d.getChallengerOne().getUsers()) {
                result += response.getUserName() + "\n";
            }
            result += "\nTeam " + d.getChallengerTwo().getName() + "\n";
            for (UserResponse response : d.getChallengerTwo().getUsers()) {
                result += response.getUserName() + "\n";
            }
        }
        return result;
    }
}
