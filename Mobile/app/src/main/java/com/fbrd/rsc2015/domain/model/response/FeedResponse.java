package com.fbrd.rsc2015.domain.model.response;

import com.example.loginmodule.model.response.Response;

import java.util.List;

/**
 * Created by noxqs on 21.11.15..
 */
public class FeedResponse extends Response {

    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data{

       private String title;
       private String description;
       private String picture;
       private String offerUrl;

       public String getTitle() {
           return title;
       }

       public void setTitle(String title) {
           this.title = title;
       }

       public String getDescription() {
           return description;
       }

       public void setDescription(String description) {
           this.description = description;
       }

       public String getPicture() {
           return picture;
       }

       public void setPicture(String picture) {
           this.picture = picture;
       }

       public String getOfferUrl() {
           return offerUrl;
       }

       public void setOfferUrl(String offerUrl) {
           this.offerUrl = offerUrl;
       }
   }


}
