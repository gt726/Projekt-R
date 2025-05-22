package com.example.projektr.fragments

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.projektr.R
import com.example.projektr.activities.ExerciseDetailActivity
import com.example.projektr.activities.MainActivity
import com.example.projektr.activities.SettingsActivity
import com.example.projektr.database.FinishedWorkoutRepository
import com.example.projektr.database.TemplateRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`

class ExercisesFragmentTest {
    @Before
    fun setUp() {
        // pokrece espresso intents za pracenje navigacije
        Intents.init()

        // mockaj FirebaseAuth i FirebaseUser
        val mockAuth = Mockito.mock(FirebaseAuth::class.java)
        val mockUser = Mockito.mock(FirebaseUser::class.java)

        `when`(mockUser.uid).thenReturn("testUserId")
        `when`(mockAuth.currentUser).thenReturn(mockUser)

        // injektiraj mock u repozitorije
        TemplateRepository.instance = TemplateRepository(mockAuth)
        FinishedWorkoutRepository.instance = FinishedWorkoutRepository(mockAuth)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun startSettingsActivity() {
        // pokreni MainActivity
        ActivityScenario.launch(MainActivity::class.java)

        // otvori fragment Exercises
        onView(withId(R.id.nav_exercises)).perform(click())
        onView(withId(R.id.fragment_exercises_root)).check(matches(isDisplayed()))

        // klikni na ikonu postavki
        onView(withId(R.id.settings_icon)).perform(click())
        // provjeri da je otvorena SettingsActivity
        intended(hasComponent(SettingsActivity::class.java.name))

        Intents.release()
    }

    @Test
    fun showExerciseDetails() {
        // pokreni MainActivity
        ActivityScenario.launch(MainActivity::class.java)

        // otvori fragment Exercises
        onView(withId(R.id.nav_exercises)).perform(click())
        onView(withId(R.id.fragment_exercises_root)).check(matches(isDisplayed()))

        // klikni na vjezbu s popisa i provjeri da je otvorena ExerciseDetailActivity
        onView(withId(R.id.recycler_view)).perform(click())
        intended(hasComponent(ExerciseDetailActivity::class.java.name))
        Intents.release()
    }
}