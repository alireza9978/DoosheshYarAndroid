package ir.coleo.varam.constants;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import ir.coleo.varam.R;

public class Constants {

    //intent to start activity data
    public static final String FARM_ID = "sdaxce";
    public static final String REPORT_ID = "sda32xc2e";
    public static final String COW_ID = "Addssaxce";
    public static final String DRUG_ID = "AddssIddsaxce";
    public static final String DRUG_TYPE = "drugdasknal";
    public static final String MORE_INFO_STATE = "sdwvvgr";
    public static final int CHOOSE_FILE_REQUEST_CODE = 99;
    public static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 98;
    public static final int FARM_SELECTION_SEARCH_COW = 101;
    public static final int DATE_SELECTION_SEARCH_COW = 102;
    public static final int DATE_SELECTION_SEARCH_FARM = 104;
    public static final int DATE_SELECTION_REPORT_CREATE = 103;
    public static final int DATE_SELECTION_REPORT_CREATE_END = 110;
    public static final int DATE_SELECTION_REPORT_FACTOR = 105;
    public static final int FARM_SELECTION_REPORT_FACTOR = 106;
    public static final int DATE_SELECTION_REPORT_INJURY = 107;
    public static final int FARM_SELECTION_REPORT_INJURY = 108;
    public static final int DATE_SELECTION_OK = 200;
    public static final int DATE_SELECTION_FAIL = 400;

    public static final int DRUG_SELECTION = 500;

    public static final String DATE_SELECTION_RESULT = "res_xc";
    public static String ADD_FARM_MODE = "SADASDXCVV";
    public static String FARM_CREATE = "CREATE_NEW_FARM";
    public static String EDIT_FARM = "EDIT_OLD_FARM";
    public static String REPORT_MODE = "SADAS DXCCXZVV";
    public static String REPORT_CREATE = "CREATE_NEW_REPORT";
    public static String EDIT_REPORT = "EDIT_OLD_REPORT";

    public static String NO_LANGUAGE = "en";
    private static String LANGUAGE_STORAGE = "someWhereInDarkness";
    private static String LANGUAGE_DATA = "someWhereInDarkness12";

    public static String NO_TOKEN = "NO TOKEN";
    private static String TOKEN_STORAGE = "someWhereInDarknessTOK";
    private static String TOKEN_DATA = "someWhereInDarkness12TOKTOK";

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void setImageFront(Context context, ImageView imageView) {
        if (Constants.getDefualtlanguage(context).equals("fa")) {
            imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_front));
        } else {
            imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_back));
        }
    }

    public static void setImageBack(Context context, ImageView imageView) {

        if (Constants.getDefualtlanguage(context).equals("fa")) {
            imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_back));
        } else {
            imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_front));
        }
    }

    public static void setImageBackBorder(Context context, ImageView imageView) {
        if (Constants.getDefualtlanguage(context).equals("fa")) {
            imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_next));
        } else {
            imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_previous));
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean checkPermission(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            return true;
        }
        return false;
    }

    public static void gridRtl(Context context, View view) {
        if (getDefualtlanguage(context).equals("fa")) {
            view.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            view.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
    }


    /**
     * گرفتن کلید ارتباط با سرور
     */
    public static String getToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TOKEN_STORAGE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TOKEN_DATA,NO_TOKEN);
    }

    /**
     * ذخیره کلید ارطباط با سرور در حافظه
     */
    public static void setToken(Context context, String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TOKEN_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN_DATA, token);
        editor.apply();
    }


    /**
     * گرفتن کلید ارتباط با سرور
     */
    public static String getDefualtlanguage(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LANGUAGE_STORAGE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LANGUAGE_DATA, "");
    }

    /**
     * ذخیره کلید ارطباط با سرور در حافظه
     */
    public static void setLanguage(Context context, String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LANGUAGE_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LANGUAGE_DATA, token);
        editor.apply();
    }

    public static class DateSelectionMode {
        public static String SINGLE = "asdasdngy";
        public static String RANG = "vuwasdngy";
    }

}
