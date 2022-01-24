package ir.coleo.varam.database.models.main;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

@Entity
public class ScoreMethod implements Serializable {

    @PrimaryKey
    public Long id;

    @ColumnInfo(name = "scores_count")
    public Integer scoresCount;

    @ColumnInfo(name = "scores_name_list")
    public List<String> scoresNameList;

    @Override
    public @NotNull
    String toString() {
        return "ScoreMethod{" +
                "scoresCount=" + scoresCount +
                ", scoresNameList=" + scoresNameList.toString() +
                '}';
    }

    public String getScoreName(Integer index) {
        if (scoresNameList.size() > index) {
            return scoresNameList.get(index);
        }
        return "unknown";
    }

    public String getText() {
        return scoresCount + " سطحی";
    }

    public String getTextLong() {
        return "تعیین سطوح (" + scoresCount + " سطح تعیین شده است)";
    }
}
