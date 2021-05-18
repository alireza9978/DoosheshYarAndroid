package ir.coleo.varam.database.models;

import ir.coleo.varam.models.MyDate;

public class CowWithLastVisit {

    private Integer id;
    private Integer number;
    private MyDate lastVisit;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return String.valueOf(number);
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public MyDate getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(MyDate lastVisit) {
        this.lastVisit = lastVisit;
    }
}
