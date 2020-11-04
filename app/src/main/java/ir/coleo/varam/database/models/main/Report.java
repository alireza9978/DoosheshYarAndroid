package ir.coleo.varam.database.models.main;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import ir.coleo.varam.models.MyDate;

@Entity
public class Report {
    @PrimaryKey
    public Integer id;

    @ColumnInfo(name = "visit_date")
    public MyDate visit;
    @ColumnInfo(name = "next_visit_date")
    public MyDate nextVisit;

    @ColumnInfo(name = "area_number")
    public Integer areaNumber;

    @ColumnInfo(name = "score_type")
    public Boolean scoreType;
    // true -> 3
    // false -> 4
    @ColumnInfo(name = "score")
    public Integer score;

    @ColumnInfo(name = "sardalme")
    public Boolean sardalme;
    @ColumnInfo(name = "khoni")
    public Boolean khoni;
    @ColumnInfo(name = "kor")
    public Boolean kor;

    @ColumnInfo(name = "cartie_state")
    public Integer cartieState;

    @ColumnInfo(name = "pomade_id")
    public Integer pomadeId;
    @ColumnInfo(name = "serum_id")
    public Integer serumId;
    @ColumnInfo(name = "anti_inflammatory_id")
    public Integer AntiInflammatoryId;
    @ColumnInfo(name = "antibiotic_id")
    public Integer antibioticId;
    @ColumnInfo(name = "cure_id")
    public Integer cureId;

    @ColumnInfo(name = "description")
    public String description;
    @ColumnInfo(name = "cow_id")
    public Integer cowId;

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", visit=" + visit +
                ", nextVisit=" + nextVisit +
                ", cowId=" + cowId +
                '}';
    }
}
