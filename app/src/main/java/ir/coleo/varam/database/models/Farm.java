package ir.coleo.varam.database.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Farm {
    @PrimaryKey
    public Integer id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "birth_count")
    public Integer birthCount;

    @ColumnInfo(name = "control_system")
    public String controlSystem;

    @ColumnInfo(name = "is_favorite")
    public Boolean favorite;

}
