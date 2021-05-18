package ir.coleo.varam.database.models.main;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * کلاس نگهدارنده اطلاعات یک گاوداری
 */
@Entity
public class Farm {
    @PrimaryKey
    public Integer id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "birth_count")
    public Integer birthCount;

    @ColumnInfo(name = "shower_count")
    public Integer showerCount;

    @ColumnInfo(name = "bed_type")
    public String bedType;

    @ColumnInfo(name = "dry_method")
    public Boolean dryMethod;

    @ColumnInfo(name = "scoreMethod")
    public Boolean scoreMethod;

    @ColumnInfo(name = "shower_unit_count")
    public Integer showerUnitCount;

    @ColumnInfo(name = "shower_pit_count")
    public Integer showerPitCount;

    @ColumnInfo(name = "is_favorite")
    public Boolean favorite;

}
