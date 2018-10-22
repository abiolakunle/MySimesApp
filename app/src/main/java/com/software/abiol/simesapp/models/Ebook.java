package com.software.abiol.simesapp.models;

public class Ebook {

    private String file_name;
    private String file_size;
    private String time_uploaded;
    private String uploaded_by;
    private String file_type;
    private String url;
    private String user_id;


    public Ebook() {
    }


    public Ebook(String file_name, String file_size, String time_uploaded, String uploaded_by, String file_type, String url) {
        this.file_name = file_name;
        this.file_size = file_size;
        this.time_uploaded = time_uploaded;
        this.uploaded_by = uploaded_by;
        this.file_type = file_type;
        this.url = url;

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_size() {
        return file_size;
    }

    public void setFile_size(String file_size) {
        this.file_size = file_size;
    }

    public String getTime_uploaded() {
        return time_uploaded;
    }

    public void setTime_uploaded(String time_uploaded) {
        this.time_uploaded = time_uploaded;
    }

    public String getUploaded_by() {
        return uploaded_by;
    }

    public void setUploaded_by(String uploaded_by) {
        this.uploaded_by = uploaded_by;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }






}


