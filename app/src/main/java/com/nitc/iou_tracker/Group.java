package com.nitc.iou_tracker;

import java.util.List;
public class Group {
    private String gName,createdBy;
    private int noOfmembers;
    List<String> names;
    public Group(){

    }
    public Group(String gName,int noOfmembers, List<String> names, String createdBy){
        this.gName = gName;
        this.noOfmembers = noOfmembers;
        this.names = names;
        this.createdBy = createdBy;
    }

    public String getgName() {
        return gName;
    }

    public int getNoOfmembers() {
        return noOfmembers;
    }

    public List<String> getNames() {
        return names;
    }
    public String getCreatedBy(){
        return createdBy;
    }
}
