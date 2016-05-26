package com.atally.models;

import org.litepal.crud.DataSupport;

public class P_zhangben extends DataSupport {
    private int id;
    private String zbname;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getZbname() {
        return zbname;
    }

    public void setZbname(String zbname) {
        this.zbname = zbname;
    }
}
