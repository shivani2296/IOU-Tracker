package com.nitc.iou_tracker;

import java.util.HashMap;

public class Bill {
    public String billNo;
    public HashMap<String, Double> listOfPerson = new HashMap<>();
    public Double amount;


    public Bill() {

    }

    public Bill(String billNo, HashMap<String, Double> map , Double amount) {
        this.billNo = billNo;
        this.amount = amount;
        this.listOfPerson = map;

    }

    public String getBillNo() {
        return billNo;
    }

    public HashMap<String, Double> getListOfPerson() {
        return listOfPerson;
    }

    public double getAmount() {
        return amount;
    }
}
