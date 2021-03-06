package ir.coleo.varam.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import ir.coleo.varam.database.dao.MyDao;
import ir.coleo.varam.database.models.main.Cow;
import ir.coleo.varam.database.models.main.Drug;
import ir.coleo.varam.database.models.main.Farm;
import ir.coleo.varam.database.models.main.Report;
import ir.coleo.varam.database.models.main.ScoreMethod;
import ir.coleo.varam.database.utils.DateConverter;
import ir.coleo.varam.database.utils.ListConverter;

/**
 * کلاس اصلی و معرت پایگاه داده
 */
@Database(entities = {Farm.class, Cow.class, Report.class, Drug.class, ScoreMethod.class}, version = 5, exportSchema = false)
@TypeConverters({DateConverter.class, ListConverter.class})
public abstract class DataBase extends RoomDatabase {
    private static final String dataBaseName = "temp";
    private static DataBase dataBase;

    public static synchronized DataBase getInstance(Context context) {
        if (dataBase == null) {
            dataBase = Room.databaseBuilder(context.getApplicationContext(), DataBase.class,
                    dataBaseName).fallbackToDestructiveMigration().build();

        }
        return dataBase;
    }

    public abstract MyDao dao();

}
