package org.techtown.schedulerproject;

public class MonthSchedules {
    String contents;
    String date;
    int marker;
    String dataid;

    public MonthSchedules(String contents, String date, int marker ,String dataid) {
        this.contents = contents;
        this.date = date;
        this.marker = marker;
        this.dataid = dataid;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMarker() {
        return marker;
    }

    public void setMarker(int marker) {
        this.marker = marker;
    }

    public String getDataid() {
        return dataid;
    }

    public void setDataid(String dataid) {
        this.dataid = dataid;
    }
}
