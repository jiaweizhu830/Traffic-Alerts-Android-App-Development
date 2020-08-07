package com.laioffer.matrix;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.click;

@RunWith(AndroidJUnit4.class)
public class MatrixInstrumentedTest {
    @Rule
    public ActivityTestRule<ControlPanel> activityTestRule =
            new ActivityTestRule<>(ControlPanel.class);

    /**
     * test to verify if the activity successfully running
     */
    @Test
    public void verifyMatrix() {
        onView(withText("Matrix")).check(matches(isDisplayed()));
    }

    /**
     * test to verify if the user successfully log in by putting correct username and password
     */
    @Test
    public void verifySend() {
        onView(withId(R.id.fab)).perform(click());

        onView(withText("Police")).perform(click());

        onView(withText("SEND")).check(matches(isDisplayed()));
    }
}
