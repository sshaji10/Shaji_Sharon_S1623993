package com.example.rss_feed;
// Name: Sharon Shaji
// Student ID: S1623993

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

public class RssItem implements Serializable {
    private String title;
    private String description;
    private String link;
    private String pubDate;
    private int depth;
    private double magnitude;
    private String location;
    //private String category;
    private double latitude;
    private double longitude;

    public RssItem() {
        this.title = "";
        this.description = "";
        this.link = "";
        this.pubDate = "";
        this.depth = 1;
        this.magnitude = 88;
        this.location = "";
        //this.category = "";
        this.latitude = 88;
        this.longitude = 88;
    }


    public static String getTitle() {
        return "";
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public String getLocation() {
        String desc = getDescription();
        ;
        int from = getFrom("Location: ");
        int to = getTo(";", from);

        return desc.substring(from + 10, to);
    }

    private int getFrom(String string) {
        int from = getDescription().indexOf(string);
        return from;
    }

    private int getTo(String str, int from) {
        int to = getDescription().indexOf(str, from);
        return to;
    }

    /*public String getCategory(){
        return category;
    }
    public void setCategory(String category){
        this.category = category;
    }*/

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return new String((new StringBuilder())
                .append(description.substring(description.indexOf("Location: "), description.indexOf("Depth: ") - 27))
                .append("\n")
                .append(description.substring(description.indexOf("Magnitude: "))));

    }

    static Comparator<RssItem> magDesc = new Comparator<RssItem>() {

        @Override
        public int compare(RssItem o1, RssItem o2) {
            return Double.compare(o2.getMagnitude(), o1.getMagnitude());
        }
    };
}




