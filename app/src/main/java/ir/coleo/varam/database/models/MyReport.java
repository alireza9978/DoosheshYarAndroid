package ir.coleo.varam.database.models;

import androidx.room.Embedded;

import ir.coleo.varam.database.models.main.Report;

public class MyReport {

    @Embedded
    public Report report;

    public Integer cowNumber;

}
