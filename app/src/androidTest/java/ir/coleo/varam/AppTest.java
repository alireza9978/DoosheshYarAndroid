package ir.coleo.varam;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.Locale;

import ir.coleo.varam.activities.SplashActivity;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AppTest {
    public final static String TAG = "AppTest";
    @Rule
    public ActivityTestRule<SplashActivity> activityTestRule = new ActivityTestRule<>(SplashActivity.class, false, false);


    public static void pressBack() {
        onView(isRoot()).perform(ViewActions.pressBack());
    }

    private String getResourceString(int id) {
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Configuration conf = targetContext.getResources().getConfiguration();
        conf.locale = new Locale("fa");
        DisplayMetrics metrics = new DisplayMetrics();
        Resources resources = new Resources(targetContext.getAssets(), metrics, conf);
        return resources.getString(id);
    }

    public void clearData() {
        File root = ApplicationProvider.getApplicationContext().getFilesDir().getParentFile();
        String[] sharedPreferencesFileNames = new File(root, "shared_prefs").list();
        if (sharedPreferencesFileNames != null)
            for (String fileName : sharedPreferencesFileNames) {
                ApplicationProvider.getApplicationContext().getSharedPreferences(fileName.replace(".xml", ""), Context.MODE_PRIVATE).edit().clear().commit();
            }
        else {
            Log.i(TAG, "clearData: did not work");
        }
    }

    @Before
    public void setup() {
        Log.i(TAG, "setup: ");
        activityTestRule.launchActivity(null);
    }

    public void createFarm(String name) {
//        onView(withTagValue(is("add_farm_get"))).perform(click());
//        onView(withId(R.id.farm_title_input)).perform(clearText(), typeText(name), closeSoftKeyboard());
//        onView(withId(R.id.control_system_input)).perform(clearText(), typeText("control"), closeSoftKeyboard());
//        onView(withId(R.id.something_count)).perform(clearText(), typeText("132"), closeSoftKeyboard());
//        onView(withId(R.id.submit)).perform(click());
    }

    public void addReport(String name, String cowNumber) {
//        onView(withText(name)).perform(click());
//        onView(withText(R.string.add_new_cow)).perform(click());
//        onView(withId(R.id.cow_number_text)).perform(clearText(), typeText(cowNumber), closeSoftKeyboard());
//        onView(withId(R.id.next_button_info)).perform(click());
//        onView(withText(R.string.reason_2)).perform(click());
//        onView(withText(R.string.reason_7)).perform(click());
//        onView(withId(R.id.reason_scrollView)).perform(ViewActions.swipeUp());
//        onView(withId(R.id.next_button_reason)).perform(click());
//        onView(withId(R.id.three_right)).perform(click());
//        onView(withId(R.id.input)).perform(clearText(), typeText("5"), closeSoftKeyboard());
//        onView(withText(R.string.more_info_reason_1)).perform(click());
//        onView(withText(R.string.more_info_reason_6)).perform(click());
//        onView(withId(R.id.ok)).perform(click());
//        onView(withId(R.id.injury_scroller)).perform(ViewActions.swipeUp());
//        onView(withId(R.id.next_button_injury)).perform(click());
//        onView(withId(R.id.more_info_edit)).perform(clearText(), typeText("this is some description"), closeSoftKeyboard());
//        onView(withText(R.string.more_info_reason_7)).perform(click());
//        onView(withId(R.id.next_button)).perform(ViewActions.swipeUp());
//        pressBack();
    }

    public void markFarm(String name) {
        onView(withText(name)).perform(click());
        onView(withId(R.id.bookmark_image)).perform(click());
        pressBack();
    }

    public void checkMarkedFarm(String name) {
        onView(withTagValue(is(getResourceString(R.string.marked)))).perform(click());
        SystemClock.sleep(2000);
        String tagValue = "marked_tab_1";
        ViewInteraction viewWithTagVI;

        viewWithTagVI = onView(withTagValue(is((Object) tagValue)));
        viewWithTagVI.perform(click());

        tagValue = "marked_" + name;
        viewWithTagVI = onView(withTagValue(is((Object) tagValue)));
        viewWithTagVI.perform(click());
    }

    public void markCow(String name) {
        onView(withText(name)).perform(click());
        onView(withId(R.id.bookmark_image)).perform(click());
    }

    @Test
    public void testB() {
        clearData();
        String test = "farm test 1";
        String cowNumber = "123";
        createFarm(test);
        SystemClock.sleep(2000);
        addReport(test, cowNumber);
        SystemClock.sleep(2000);
        markFarm(test);
        SystemClock.sleep(2000);
        checkMarkedFarm(test);
        markCow(cowNumber);
        pressBack();
        pressBack();
    }
}