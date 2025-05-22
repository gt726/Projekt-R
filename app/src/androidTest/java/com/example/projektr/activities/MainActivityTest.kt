package com.example.projektr.activities

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.projektr.R
import com.example.projektr.database.FinishedWorkoutRepository
import com.example.projektr.database.TemplateRepository
import com.example.projektr.fragments.WorkoutFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class MainActivityTest {

    @Before
    fun setUp() {
        // mockaj FirebaseAuth i FirebaseUser
        val mockAuth = mock(FirebaseAuth::class.java)
        val mockUser = mock(FirebaseUser::class.java)

        `when`(mockUser.uid).thenReturn("testUserId")
        `when`(mockAuth.currentUser).thenReturn(mockUser)

        // injektiraj mock u repozitorije
        FinishedWorkoutRepository.instance = FinishedWorkoutRepository(mockAuth)
        TemplateRepository.instance = TemplateRepository(mockAuth)
    }

    @Test
    fun defaultFragmentIsWorkout() {
        // provjeri da je zadani fragment WorkoutFragment
        ActivityScenario.launch(MainActivity::class.java)
            .use { scenario ->
                scenario.onActivity { activity ->
                    val fragment =
                        activity.supportFragmentManager.findFragmentById(R.id.fragment_container)
                    assertTrue(fragment is WorkoutFragment)
                }
            }
    }

    // provjeri pokretanje History fragmenta
    @Test
    fun startHistoryFragment() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.nav_history)).perform(click())
        onView(withId(R.id.fragment_history_root)).check(matches(isDisplayed()))
    }

    // provjeri pokretanje Workout fragmenta
    @Test
    fun startWorkoutFragment() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.nav_workout)).perform(click())
        onView(withId(R.id.fragment_workout_root)).check(matches(isDisplayed()))
    }

    // provjeri pokretanje Exercises fragmenta
    @Test
    fun startExercisesFragment() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.nav_exercises)).perform(click())
        onView(withId(R.id.fragment_exercises_root)).check(matches(isDisplayed()))
    }
}