package com.example.projektr.fragments

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.projektr.R
import com.example.projektr.activities.FinishedWorkoutActivity
import com.example.projektr.activities.MainActivity
import com.example.projektr.activities.SettingsActivity
import com.example.projektr.adapters.HistoryAdapter
import com.example.projektr.database.FinishedWorkoutRepository
import com.example.projektr.database.FirestoreFinishedWorkout
import com.example.projektr.database.TemplateRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`

class HistoryFragmentTest {
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
        // oslobodi espresso intents
        Intents.release()
    }

    @Test
    fun startSettingsActivity() {
        // pokreni MainActivity
        ActivityScenario.launch(MainActivity::class.java)

        // otvori fragment History
        onView(withId(R.id.nav_history)).perform(click())
        onView(withId(R.id.fragment_history_root)).check(matches(isDisplayed()))

        // klikni na ikonu postavki
        onView(withId(R.id.settings_icon)).perform(click())
        // provjeri da je otvorena SettingsActivity
        intended(hasComponent(SettingsActivity::class.java.name))
    }


    @Test
    fun showHistoryDetails() {
        // pokreni MainActivity
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        // pokreni History fragment
        onView(withId(R.id.nav_history)).perform(click())

        scenario.onActivity { activity ->
            val fragment =
                activity.supportFragmentManager.findFragmentById(R.id.fragment_container) as? HistoryFragment
            // umetni mock data u adapter
            if (fragment != null) {
                fragment.view?.findViewById<RecyclerView>(R.id.recycler_view)?.adapter =
                    HistoryAdapter(
                        mutableListOf(
                            FirestoreFinishedWorkout.FinishedWorkout(
                                "test123",
                                "Test workout",
                                System.currentTimeMillis(),
                                "testUserId",
                            )
                                    to listOf(
                                FirestoreFinishedWorkout.FinishedWorkoutExercise(
                                    "testExercise123",
                                    "test123",
                                    "Bench press",
                                )
                            )
                        ),
                        fragment.viewLifecycleOwner
                    )
            }
        }

        // klikni na prvi item
        onView(withId(R.id.recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )

        // provjeri intent
        intended(hasComponent(FinishedWorkoutActivity::class.java.name))
    }
}