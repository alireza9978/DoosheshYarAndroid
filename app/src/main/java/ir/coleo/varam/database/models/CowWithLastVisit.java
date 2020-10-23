package ir.coleo.varam.database.models;

import android.content.Context;

import ir.coleo.varam.R;
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

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getNumber(Context context) {
        return context.getString(R.string.cow_title) + number;
    }

    public MyDate getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(MyDate lastVisit) {
        this.lastVisit = lastVisit;
    }
}
