package ir.coleo.varam.database.models.main;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;
import androidx.room.Entity;

@Entity
public class Drug {

    @PrimaryKey
    public Integer id;

    @ColumnInfo(name = "type")
    public Integer type;
    //0 -> pomade
    //1 -> anit butic
    //2 -> sorom
    //3 -> darman hemaiati
    //4 -> zede eltehab

    @ColumnInfo(name = "name")
    public String name;

}
