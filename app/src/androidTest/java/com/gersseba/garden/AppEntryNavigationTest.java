package com.gersseba.garden;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AppEntryNavigationTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void appLaunch_showsEntryChoices() {
        onView(withId(R.id.my_plants_button)).check(matches(isDisplayed()));
        onView(withId(R.id.care_plan_button)).check(matches(isDisplayed()));
        onView(withText(R.string.my_plants_action)).check(matches(isDisplayed()));
        onView(withText(R.string.care_plan_action)).check(matches(isDisplayed()));
    }

    @Test
    public void selectingMyPlants_navigatesToMockPlantList() {
        onView(withId(R.id.my_plants_button)).perform(click());

        onView(withId(R.id.my_plants_title)).check(matches(isDisplayed()));
        onView(withText(R.string.my_plants_screen_title)).check(matches(isDisplayed()));
    }

    @Test
    public void selectingCarePlan_navigatesToMockCarePlan() {
        onView(withId(R.id.care_plan_button)).perform(click());

        onView(withId(R.id.care_plan_title)).check(matches(isDisplayed()));
        onView(withText(R.string.care_plan_screen_title)).check(matches(isDisplayed()));
    }
}

