package ir.coleo.varam.database.models;

import androidx.room.Embedded;

public class MyReport {

    @Embedded
    public Report report;

    public Integer cowNumber;

}
