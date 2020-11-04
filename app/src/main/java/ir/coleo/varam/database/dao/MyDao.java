package ir.coleo.varam.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ir.coleo.varam.database.models.CowForMarked;
import ir.coleo.varam.database.models.CowWithLastVisit;
import ir.coleo.varam.database.models.FarmWithCowCount;
import ir.coleo.varam.database.models.FarmWithNextVisit;
import ir.coleo.varam.database.models.LastReport;
import ir.coleo.varam.database.models.MyReport;
import ir.coleo.varam.database.models.NextReport;
import ir.coleo.varam.database.models.NextVisit;
import ir.coleo.varam.database.models.SearchFarm;
import ir.coleo.varam.database.models.main.Cow;
import ir.coleo.varam.database.models.main.Farm;
import ir.coleo.varam.database.models.main.Drug;
import ir.coleo.varam.database.models.main.Report;
import ir.coleo.varam.models.MyDate;

@Dao
public interface MyDao {


    @Query("SELECT Cow.id AS cowId,Cow.number AS cowNumber, MAX(Report.visit_date) AS lastVisit," +
            " Farm.name AS farmName " +
            "FROM Cow,Farm,Report " +
            "WHERE Cow.number_string LIKE :id " +
            "AND Report.cow_id == Cow.id " +
            "AND Cow.farm_id == farm.id " +
            "GROUP BY Cow.id")
    List<CowForMarked> searchCow(String id);


    @Query("SELECT Farm.id AS farmId, Farm.name AS farmName, COUNT(Cow.id) AS cowCount , " +
            "MAX(Report.visit_date) AS lastVisit  " +
            "FROM Cow,Farm,Report " +
            "WHERE Report.visit_date <= :end " +
            "AND Report.visit_date >= :start " +
            "AND Cow.farm_id == farm.id " +
            "AND Report.cow_id == Cow.id " +
            "GROUP BY Farm.id")
    List<SearchFarm> searchFarm(MyDate start, MyDate end);

    @Query("SELECT Cow.id AS cowId,Cow.number AS cowNumber, MAX(Report.visit_date) AS lastVisit, Farm.name AS farmName " +
            "FROM Cow,Farm,Report " +
            "WHERE Cow.number_string LIKE :id " +
            "AND Report.visit_date >= :start " +
            "AND Report.visit_date <= :end " +
            "AND Report.cow_id == Cow.id " +
            "AND Cow.farm_id == farm.id " +
            "GROUP BY Cow.id")
    List<CowForMarked> searchCow(String id, MyDate start, MyDate end);

    @Query("SELECT Cow.id AS cowId,Cow.number AS cowNumber, MAX(Report.visit_date) AS lastVisit, Farm.name AS farmName " +
            "FROM Cow,Farm,Report " +
            "WHERE Cow.number_string LIKE :id " +
            "AND Report.cow_id == Cow.id " +
            "AND Cow.farm_id == farm.id " +
            "AND Farm.id == :farmId " +
            "GROUP BY Cow.id")
    List<CowForMarked> searchCow(String id, Integer farmId);

    @Query("SELECT Cow.id AS cowId,Cow.number AS cowNumber, MAX(Report.visit_date) AS lastVisit, Farm.name AS farmName " +
            "FROM Cow,Farm,Report " +
            "WHERE Cow.number_string LIKE :id " +
            "AND Report.visit_date >= :start " +
            "AND Report.visit_date <= :end " +
            "AND Report.cow_id == Cow.id " +
            "AND Cow.farm_id == farm.id " +
            "AND Farm.id == :farmId " +
            "GROUP BY Cow.id")
    List<CowForMarked> searchCow(String id, MyDate start, MyDate end, Integer farmId);

    @Query("SELECT Cow.id AS cowId,Cow.number AS cowNumber," +
            " Farm.name AS farmName, MAX(Report.visit_date) AS lastVisit " +
            "FROM Cow,Farm,Report " +
            "WHERE Cow.favorite AND " +
            "Cow.farm_id == Farm.id AND " +
            "Report.cow_id == Cow.id GROUP BY Cow.id")
    List<CowForMarked> getMarkedCows();

    @Query("SELECT Farm.id AS farmId, Farm.name AS farmName, COUNT(Cow.id) AS cow_count " +
            "FROM Farm, Cow " +
            "WHERE Farm.id == Cow.farm_id GROUP BY Farm.id")
    List<FarmWithCowCount> getFarmWithCowCount();

    @Query("SELECT Farm.id AS farmId, Farm.name AS farmName, COUNT(Cow.id) AS cow_count " +
            "FROM Farm,Cow " +
            "WHERE Farm.is_favorite AND " +
            "Cow.farm_id == Farm.id GROUP BY Farm.id")
    List<FarmWithCowCount> getMarkedFarmWithCowCount();

    @Query("SELECT * " +
            "FROM Farm " +
            "WHERE Farm.is_favorite")
    List<Farm> getMarkedFarm();

    @Query("SELECT Farm.name AS farmName, Cow.number AS cowNumber, Cow.id AS cowId, MAX(Report.next_visit_date) AS nextVisitDate" +
            " FROM Cow, Report, Farm" +
            " WHERE Report.next_visit_date >= :now AND" +
            " Farm.id == Cow.farm_id AND" +
            " Cow.id == Report.cow_id GROUP BY Cow.id")
    List<NextReport> getAllNextVisit(MyDate now);

    @Query("SELECT Report.next_visit_date AS nextVisitDate, Farm.name AS farmName, Cow.number AS cowNumber, Cow.id AS cowId" +
            " FROM Cow, Report, Farm" +
            " WHERE nextVisitDate == :now AND" +
            " Cow.id == Report.cow_id AND" +
            " Farm.id == Cow.farm_id ")
    List<NextReport> getAllVisitInDay(MyDate now);

    @Query("SELECT Report.next_visit_date AS nextVisitDate, Farm.name AS farmName, Cow.number AS cowNumber,Cow.id AS cowId" +
            " FROM Cow, Report, Farm" +
            " WHERE nextVisitDate == :now AND" +
            " Cow.id == Report.cow_id AND" +
            " Farm.id == Cow.farm_id")
    List<NextReport> getAllNextVisitInDay(MyDate now);

    @Query("SELECT MAX(Report.next_visit_date) AS visitDate, Cow.id AS cowId, Cow.number AS cowNumber" +
            " FROM Cow, Report, Farm" +
            " WHERE Report.next_visit_date >= :now AND" +
            " Report.cow_id == Cow.id AND" +
            " Cow.farm_id == Farm.id AND" +
            " Farm.id == :farmId GROUP BY Cow.id")
    List<NextVisit> getAllNextVisitFroFarm(MyDate now, Integer farmId);

    @Query("SELECT Report.next_visit_date AS nextVisit, MAX(Report.visit_date) AS lastVisit" +
            " FROM Report" +
            " WHERE Report.cow_id == :cowId")
    LastReport getLastReport(Integer cowId);

    @Query("SELECT * FROM Farm")
    List<Farm> getAll();

    @Query("SELECT Cow.id AS id, Cow.number AS number, MAX(Report.visit_date) AS lastVisit " +
            " FROM Cow, Report" +
            " WHERE Cow.farm_id == :id AND" +
            " Report.cow_id == Cow.id GROUP BY Cow.id")
    List<CowWithLastVisit> getAllCowOfFarmWithLastVisit(Integer id);


    @Query("SELECT * FROM Cow WHERE Cow.farm_id == :id")
    List<Cow> getAllCowOfFarm(Integer id);

    @Query("SELECT * FROM Report WHERE Report.cow_id == :id")
    List<Report> getAllReportOfCow(Integer id);

    @Query("SELECT *, Cow.number AS cowNumber FROM Cow,Report WHERE Report.cow_id == Cow.id AND Cow.farm_id == :id")
    List<MyReport> getAllMyReportFarm(Integer id);

    @Query("SELECT *, Cow.number AS cowNumber FROM Cow,Report WHERE Report.id == :id")
    MyReport myReportWithCow(Integer id);

    @Query("SELECT * FROM Farm WHERE Farm.id == :id")
    Farm getFarm(Integer id);

    @Query("SELECT * FROM Drug WHERE Drug.id == :id")
    Drug getDrug(Integer id);

    @Query("SELECT Farm.id AS farmId, MIN(Report.next_visit_date) AS nextVisit " +
            "FROM Farm,Cow,Report " +
            "WHERE Farm.id == :id " +
            "AND Cow.farm_id == Farm.id " +
            "AND Report.cow_id == Cow.id")
    FarmWithNextVisit getFarmWithNextVisit(Integer id);


    @Query("SELECT * FROM Cow WHERE Cow.number == :cowNumber AND Cow.farm_id == :farmId")
    Cow getCow(Integer cowNumber, Integer farmId);

    @Query("SELECT * FROM Cow WHERE Cow.id == :id")
    Cow getCow(Integer id);

    @Query("SELECT * FROM Report WHERE Report.id == :id")
    Report getReport(Integer id);

    @Query("SELECT * FROM Drug WHERE Drug.type == :type")
    List<Drug> getAllDrug(Integer type);

    @Delete
    void deleteCow(Cow... cows);

    @Delete
    void deleteFarm(Farm... farms);

    @Delete
    void deleteReport(Report... reports);

    @Delete
    void deleteDrug(Drug drug);

    @Update
    void update(Cow cow);

    @Update
    void update(Farm farm);

    @Update
    void update(Report report);

    @Insert
    void insert(Drug drug);

    @Insert
    void insert(Report report);

    @Insert
    void insert(Farm farm);

    @Insert
    long insertGetId(Farm farm);

    @Insert
    void insert(Cow Cow);

    @Insert
    long insertGetId(Cow Cow);

    @Insert
    void insertAll(Report... reports);

    @Insert
    void insertAll(Cow... cows);

    @Insert
    void insertAll(Farm... farms);


}
