package ir.coleo.varam.database.models.main;

import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

import ir.coleo.varam.R;

@Entity
public class ScoreMethod implements Serializable {

    @PrimaryKey
    public Long id;

    @ColumnInfo(name = "scores_count")
    public Integer scoresCount;

    @ColumnInfo(name = "scores_name_list")
    public List<String> scoresNameList;

    @Override
    public @NotNull String toString() {
        return "ScoreMethod{" +
                "scoresCount=" + scoresCount +
                ", scoresNameList=" + scoresNameList.toString() +
                '}';
    }

    public String getScoreName(Integer index){
        if (scoresNameList.size() > index){
            return scoresNameList.get(index);
        }
        return "unknown";
    }

    public int getText() {
        switch (scoresCount){
            case 3:{
                return R.string.three_level;
            }
            case 4:{
                return R.string.four_level;
            }
            case 5:{
                return R.string.five_level;
            }
        }
        return R.string.no_level;
    }
}
