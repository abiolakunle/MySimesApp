package com.software.abiol.simesapp.models;

public class UploadFileDetails {


    public String name;
    public String status;
    public String tags;

    UploadFileDetails(){

    }

    public UploadFileDetails(String name, String status, String tags) {
        this.name = name;
        this.status = status;
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }



}
