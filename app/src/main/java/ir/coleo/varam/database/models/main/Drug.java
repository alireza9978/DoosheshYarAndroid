package ir.coleo.varam.database.models.main;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Drug implements Comparable<Drug> {

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

    @Override
    public int compareTo(Drug drug) {
        if (drug.type.equals(this.type) && drug.name.equals(this.name)) {
            return 0;
        }
        return -1;
    }
}
