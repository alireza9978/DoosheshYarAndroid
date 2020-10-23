package ir.coleo.varam.database.models;

import androidx.room.ColumnInfo;

public class FarmWithCowCount {

    public Integer farmId;
    public String farmName;

    @ColumnInfo(name = "cow_count")
    public Integer cowCount;

}
