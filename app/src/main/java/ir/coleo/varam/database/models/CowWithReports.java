package ir.coleo.varam.database.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class CowWithReports {
    @Embedded
    public Cow cow;
    @Relation(
            parentColumn = "id",
            entityColumn = "cow_id"
    )
    public List<Report> reports;
}

