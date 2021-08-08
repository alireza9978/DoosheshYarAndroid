package ir.coleo.varam.database.utils;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ir.coleo.varam.models.MyDate;


public class ListConverter {
    @TypeConverter
    public static List<String> stringToList(String value) {
        return new ArrayList<>(Arrays.asList(value.split("#")));
    }

    @TypeConverter
    public static String listToString(List<String> list) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < list.size() - 1; i++) {
            result.append(list.get(i)).append("#");
        }
        result.append(list.get(list.size() - 1));
        return result.toString();
    }
}