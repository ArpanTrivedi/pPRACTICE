package com.example.orahi;

public class DataDAO {
    String month;
    int stat;

    public DataDAO(String month, int stat) {
        this.month = month;
        this.stat = stat;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getStat() {
        return stat;
    }

    public void setStat(int stat) {
        this.stat = stat;
    }

    @Override
    public String toString() {
        return "DataDAO{" +
                "month='" + month + '\'' +
                ", stat=" + stat +
                '}';
    }
}
