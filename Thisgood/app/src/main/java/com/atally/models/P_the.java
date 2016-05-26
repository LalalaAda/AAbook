package com.atally.models;

public class P_the implements Comparable<P_the>{
    private int id;
    private int bigtype;//0代表收入1代表支出
    private String type;
    private double coin;
    private long time;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBigtype() {
        return bigtype;
    }

    public void setBigtype(int bigtype) {
        this.bigtype = bigtype;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getCoin() {
        return coin;
    }

    public void setCoin(double coin) {
        this.coin = coin;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }


    @Override
    public int compareTo(P_the another) {
        if (this.getTime()>another.getTime()){
            return -1;
        }
        return 0;
    }
}
