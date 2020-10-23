package ir.coleo.varam.database.models;

import android.content.Context;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import ir.coleo.varam.R;

@Entity
public class Cow {
    @PrimaryKey
    private Integer id;
    @ColumnInfo(name = "number")
    private Integer number;
    @ColumnInfo(name = "number_string")
    private String numberString;
    @ColumnInfo(name = "favorite")
    private Boolean favorite;
    @ColumnInfo(name = "farm_id")
    private Integer farm;

    public Cow(Integer number, Boolean favorite, Integer farm) {
        this.number = number;
        this.numberString = "" + number;
        this.favorite = favorite;
        this.farm = farm;
    }

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

    public Integer getFarm() {
        return farm;
    }

    public void setFarm(Integer farm) {
        this.farm = farm;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public String getNumberString() {
        return numberString;
    }

    public void setNumberString(String numberString) {
        this.numberString = numberString;
    }
}

