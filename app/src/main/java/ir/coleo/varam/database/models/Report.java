package ir.coleo.varam.database.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import ir.coleo.varam.R;
import ir.coleo.varam.models.MyDate;

import java.util.ArrayList;

@Entity
public class Report {
    @PrimaryKey
    public Integer id;

    @ColumnInfo(name = "visit_date")
    public MyDate visit;
    @ColumnInfo(name = "next_visit_date")
    public MyDate nextVisit;

    @ColumnInfo(name = "leg_area_number")
    public Integer legAreaNumber;
    @ColumnInfo(name = "finger_number")
    public Integer fingerNumber;
    @ColumnInfo(name = "right_side")
    public Boolean rightSide;

    @ColumnInfo(name = "other_info_wound")
    public Boolean otherInfoWound;
    @ColumnInfo(name = "other_info_gel")
    public Boolean otherInfoGel;
    @ColumnInfo(name = "other_info_boarding")
    public Boolean otherInfoBoarding;
    @ColumnInfo(name = "other_info_ecchymosis")
    public Boolean otherInfoEcchymosis;
    @ColumnInfo(name = "other_info_no_injury")
    public Boolean otherInfoNoInjury;
    @ColumnInfo(name = "other_info_recovered")
    public Boolean otherInfoRecovered;
    @ColumnInfo(name = "other_info_hoof_trim")
    public Boolean otherInfoHoofTrim;
    @ColumnInfo(name = "reference_cause_hundred_days")
    public Boolean referenceCauseHundredDays;
    @ColumnInfo(name = "reference_cause_dryness")
    public Boolean referenceCauseDryness;
    @ColumnInfo(name = "reference_cause_high_score")
    public Boolean referenceCauseHighScore;
    @ColumnInfo(name = "reference_cause_referential")
    public Boolean referenceCauseReferential;
    @ColumnInfo(name = "reference_cause_lagged")
    public Boolean referenceCauseLagged;
    @ColumnInfo(name = "reference_cause_heifer")
    public Boolean referenceCauseHeifer;
    @ColumnInfo(name = "reference_cause_long_hoof")
    public Boolean referenceCauseLongHoof;
    @ColumnInfo(name = "reference_cause_new_limp")
    public Boolean referenceCauseNewLimp;
    @ColumnInfo(name = "reference_cause_limp_visit")
    public Boolean referenceCauseLimpVisit;
    @ColumnInfo(name = "reference_cause_group_hoof_trim")
    public Boolean referenceCauseGroupHoofTrim;
    @ColumnInfo(name = "description")
    public String description;
    @ColumnInfo(name = "cow_id")
    public Integer cowId;

    public ArrayList<Integer> getSelectedOtherInfo() {
        ArrayList<Integer> list = new ArrayList<>();
        if (otherInfoWound) {
            list.add(R.string.more_info_reason_1);
        }
        if (otherInfoGel) {
            list.add(R.string.more_info_reason_5);
        }
        if (otherInfoBoarding) {
            list.add(R.string.more_info_reason_6);
        }
        if (otherInfoEcchymosis) {
            list.add(R.string.more_info_reason_2);
        }
        if (otherInfoNoInjury) {
            list.add(R.string.more_info_reason_4);
        }
        if (otherInfoRecovered) {
            list.add(R.string.more_info_reason_3);
        }
        if (otherInfoHoofTrim) {
            list.add(R.string.more_info_reason_7);
        }
        return list;
    }

    public ArrayList<Integer> getSelectedReason() {
        ArrayList<Integer> list = new ArrayList<>();
        if (referenceCauseHundredDays) {
            list.add(R.string.reason_1);
        }
        if (referenceCauseDryness) {
            list.add(R.string.reason_2);
        }
        if (referenceCauseHighScore) {
            list.add(R.string.reason_6);
        }
        if (referenceCauseReferential) {
            list.add(R.string.reason_7);
        }
        if (referenceCauseLagged) {
            list.add(R.string.reason_4);
        }
        if (referenceCauseHeifer) {
            list.add(R.string.reason_9);
        }
        if (referenceCauseLongHoof) {
            list.add(R.string.reason_8);
        }
        if (referenceCauseNewLimp) {
            list.add(R.string.reason_4);
        }
        if (referenceCauseLimpVisit) {
            list.add(R.string.reason_5);
        }
        if (referenceCauseGroupHoofTrim) {
            list.add(R.string.reason_10);
        }
        return list;
    }

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
