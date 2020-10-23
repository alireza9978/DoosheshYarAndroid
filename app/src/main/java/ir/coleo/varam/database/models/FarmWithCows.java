package ir.coleo.varam.database.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class FarmWithCows {
    @Embedded
    public Farm farm;
    @Relation(
            parentColumn = "id",
            entityColumn = "farm_id"
    )
    public List<Cow> cows;
}

