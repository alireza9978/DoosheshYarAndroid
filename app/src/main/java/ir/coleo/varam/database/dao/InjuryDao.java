package ir.coleo.varam.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import ir.coleo.varam.database.models.InjureyReport;
import ir.coleo.varam.models.MyDate;

import java.util.List;

@Dao
public interface InjuryDao {

    @Query("SELECT COUNT(Report.id) FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == :farmId " +
            "AND Report.visit_date >= :start " +
            "AND Report.visit_date <= :end " +
            "AND Report.leg_area_number == 0 " +
            "AND Report.reference_cause_new_limp " +
            "GROUP BY Report.cow_id, Report.visit_date ")
    Integer felemons(Integer farmId, MyDate start, MyDate end);

    @Query("SELECT COUNT(Report.id) FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :start " +
            "AND Report.visit_date <= :end " +
            "AND Report.leg_area_number == 10 " +
            "AND Report.reference_cause_new_limp " +
            "GROUP BY Report.cow_id, Report.visit_date ")
    Integer deramatit(Integer farmId, MyDate start, MyDate end);

    @Query("SELECT Report.cow_id AS cowId, Report.visit_date AS date, Report.finger_number AS fingerNumber " +
            "FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :start " +
            "AND Report.visit_date <= :end " +
            "AND Report.leg_area_number == 4 " +
            "AND Report.reference_cause_new_limp " +
            "GROUP BY Report.cow_id, Report.visit_date ")
    List<InjureyReport> woundHoofBottom(Integer farmId, MyDate start, MyDate end);

    @Query("SELECT Report.cow_id AS cowId, Report.visit_date AS date, Report.finger_number AS fingerNumber" +
            " FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :start " +
            "AND Report.visit_date <= :end " +
            "AND (Report.leg_area_number == 2 OR Report.leg_area_number == 3) " +
            "AND Report.reference_cause_new_limp " +
            "GROUP BY Report.finger_number,Report.cow_id, Report.visit_date ")
    List<InjureyReport> whiteLineWound(Integer farmId, MyDate start, MyDate end);

    @Query("SELECT Report.cow_id AS cowId, Report.visit_date AS date, Report.finger_number AS fingerNumber" +
            " FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :start " +
            "AND Report.visit_date <= :end " +
            "AND (Report.leg_area_number == 5 OR Report.leg_area_number == 1)" +
            "AND Report.reference_cause_new_limp " +
            "GROUP BY Report.finger_number, Report.cow_id, Report.visit_date ")
    List<InjureyReport> pangeWound(Integer farmId, MyDate start, MyDate end);

    @Query("SELECT Report.cow_id AS cowId, Report.visit_date AS date, Report.finger_number AS fingerNumber" +
            " FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :start " +
            "AND Report.visit_date <= :end " +
            "AND Report.leg_area_number == 6 " +
            "AND Report.reference_cause_new_limp " +
            "GROUP BY Report.finger_number, Report.cow_id, Report.visit_date ")
    List<InjureyReport> pashneWound(Integer farmId, MyDate start, MyDate end);

    @Query("SELECT COUNT(Report.id) FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :start " +
            "AND Report.visit_date <= :end " +
            "AND (Report.leg_area_number == 7 OR Report.leg_area_number == 8 " +
            "OR Report.leg_area_number == 11 OR Report.leg_area_number == 12) " +
            "AND Report.reference_cause_new_limp " +
            "GROUP BY Report.finger_number, Report.cow_id, Report.visit_date ")
    Integer wallWound(Integer farmId, MyDate start, MyDate end);

    @Query("SELECT COUNT(Report.id) FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :start " +
            "AND Report.visit_date <= :end " +
            "AND Report.leg_area_number == 9 " +
            "AND Report.reference_cause_new_limp " +
            "GROUP BY Report.cow_id, Report.visit_date ")
    Integer reigenNine(Integer farmId, MyDate start, MyDate end);

    @Query("SELECT Report.id FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "GROUP BY Report.cow_id")
    List<Integer> box(Integer farmId, MyDate date, MyDate endDate);

    @Query("SELECT Report.id FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.reference_cause_limp_visit " +
            "GROUP BY Report.cow_id")
    List<Integer> visit(Integer farmId, MyDate date, MyDate endDate);

    @Query("SELECT Report.id FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.reference_cause_new_limp " +
            "GROUP BY Report.cow_id")
    List<Integer> newLimp(Integer farmId, MyDate date, MyDate endDate);

    @Query("SELECT Report.id FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.reference_cause_hundred_days " +
            "GROUP BY Report.cow_id")
    List<Integer> sadRoze(Integer farmId, MyDate date, MyDate endDate);

    @Query("SELECT Report.id FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.reference_cause_dryness " +
            "GROUP BY Report.cow_id")
    List<Integer> dryness(Integer farmId, MyDate date, MyDate endDate);

    @Query("SELECT Report.id FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.reference_cause_lagged " +
            "GROUP BY Report.cow_id")
    List<Integer> delayed(Integer farmId, MyDate date, MyDate endDate);

    @Query("SELECT Report.id FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.reference_cause_group_hoof_trim " +
            "GROUP BY Report.cow_id")
    List<Integer> group(Integer farmId, MyDate date, MyDate endDate);

    @Query("SELECT Report.id FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.reference_cause_high_score " +
            "GROUP BY Report.cow_id")
    List<Integer> high(Integer farmId, MyDate date, MyDate endDate);

    @Query("SELECT Report.id FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.reference_cause_referential " +
            "GROUP BY Report.cow_id")
    List<Integer> refrence(Integer farmId, MyDate date, MyDate endDate);

    @Query("SELECT Report.id FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.reference_cause_heifer " +
            "GROUP BY Report.cow_id")
    List<Integer> heifer(Integer farmId, MyDate date, MyDate endDate);

    @Query("SELECT Report.id FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.reference_cause_long_hoof " +
            "GROUP BY Report.cow_id")
    List<Integer> longHoof(Integer farmId, MyDate date, MyDate endDate);

    @Query("SELECT Report.id FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.other_info_hoof_trim " +
            "GROUP BY Report.cow_id")
    List<Integer> somChini(Integer farmId, MyDate date, MyDate endDate);

    @Query("SELECT Report.id FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.other_info_boarding " +
            "GROUP BY Report.cow_id")
    List<Integer> boarding(Integer farmId, MyDate date, MyDate endDate);


    //
    //

    @Query("SELECT Report.id FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.other_info_boarding ")
    List<Integer> boardingFactorAll(Integer farmId, MyDate date, MyDate endDate);

    @Query("SELECT Report.id FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Cow.number == :cowNumber " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.other_info_boarding ")
    List<Integer> boardingFactor(Integer farmId, MyDate date, MyDate endDate, Integer cowNumber);

    @Query("SELECT Report.id FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.other_info_hoof_trim ")
    List<Integer> hoofTrimFactorAll(Integer farmId, MyDate date, MyDate endDate);

    @Query("SELECT Report.id FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Cow.number == :cowNumber " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.other_info_hoof_trim ")
    List<Integer> hoofTrimFactor(Integer farmId, MyDate date, MyDate endDate, Integer cowNumber);

    @Query("SELECT Report.id FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.other_info_gel ")
    List<Integer> gelFactorAll(Integer farmId, MyDate date, MyDate endDate);

    @Query("SELECT Report.id FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Cow.number == :cowNumber " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.other_info_gel ")
    List<Integer> gelFactor(Integer farmId, MyDate date, MyDate endDate, Integer cowNumber);

    @Query("SELECT Report.id FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.reference_cause_limp_visit ")
    List<Integer> visitFactorAll(Integer farmId, MyDate date, MyDate endDate);

    @Query("SELECT Report.id FROM Report,Cow,Farm " +
            "WHERE Report.cow_id == Cow.id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Cow.number == :cowNumber " +
            "AND Farm.id == :farmId " +
            "AND Report.visit_date >= :date  AND Report.visit_date <= :endDate  " +
            "AND Report.reference_cause_limp_visit ")
    List<Integer> visitFactor(Integer farmId, MyDate date, MyDate endDate, Integer cowNumber);

}
