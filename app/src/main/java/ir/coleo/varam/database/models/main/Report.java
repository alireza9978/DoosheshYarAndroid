package ir.coleo.varam.database.models.main;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import ir.coleo.varam.models.MyDate;

/**
 * کلاس نگهدارنده اطلاعات گزارش
 */
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

    @ColumnInfo(name = "score_method_id")
    public Long scoreMethodId ;

    // zero base
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
    public Integer antiInflammatoryId;
    @ColumnInfo(name = "antibiotic_id")
    public Integer antibioticId;
    @ColumnInfo(name = "cure_id")
    public Integer cureId;

    @ColumnInfo(name = "description")
    public String description;
    @ColumnInfo(name = "cow_id")
    public Integer cowId;
    @ColumnInfo(name = "cure_duration")
    public long cureDuration;

//    @Override
//    public String toString() {
//        return "Report{" +
//                "id=" + id +
//                ", visit=" + visit +
//                ", nextVisit=" + nextVisit +
//                ", cowId=" + cowId +
//                '}';
//    }


    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", visit=" + visit +
                ", nextVisit=" + nextVisit +
                ", areaNumber=" + areaNumber +
                ", scoreType=" + scoreMethodId +
                ", score=" + score +
                ", sardalme=" + sardalme +
                ", khoni=" + khoni +
                ", kor=" + kor +
                ", cartieState=" + cartieState +
                ", pomadeId=" + pomadeId +
                ", serumId=" + serumId +
                ", antiInflammatoryId=" + antiInflammatoryId +
                ", antibioticId=" + antibioticId +
                ", cureId=" + cureId +
                ", description='" + description + '\'' +
                ", cowId=" + cowId +
                '}';
    }
}
