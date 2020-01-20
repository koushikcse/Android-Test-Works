package com.kusu.designtest;

import java.io.Serializable;

/**
 * Created by Koushik on 20/1/20.
 */
public class DataModel implements Serializable {
    private String head;
    private String des;
    private String date;

    public DataModel(String head, String des, String date) {
        this.head = head;
        this.des = des;
        this.date = date;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
