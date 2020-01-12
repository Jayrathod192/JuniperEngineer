package com.juniper.Model;

public class Data {
    public String title;
    public String body;
    public String info;

    public Data() {
    }

    public Data(String title, String body,String info ) {
        this.title = title;
        this.body = body;
        this.info=info;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
