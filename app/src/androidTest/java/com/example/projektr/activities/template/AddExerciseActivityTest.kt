package com.example.projektr.activities.template

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.projektr.R
import com.example.projektr.activities.MainActivity
import com.example.projektr.database.FinishedWorkoutRepository
import com.example.projektr.database.TemplateRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`

class AddExerciseActivityTest {
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
    fun cancelButtonTest() {
        // pokreni AddExerciseActivity
        ActivityScenario.launch(AddExerciseActivity::class.java)

        // provjeri da je cancel button vidljiv
        onView(withId(R.id.cancel_button)).check(matches(isDisplayed()))

        // klikni na cancel button
        onView(withId(R.id.cancel_button)).perform(click())

        // provjeri da je MainActivity pokrenut
        intended(hasComponent(MainActivity::class.java.name))
    }

    @Test
    fun addExerciseToList() {
        // pokreni AddExerciseActivity
        ActivityScenario.launch(AddExerciseActivity::class.java)

        // klikni na vjezbu s popisa
        onView(withId(R.id.exercises_recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )
        // unesi broj setova u popup
        onView(withId(R.id.sets)).perform(typeText("4"), closeSoftKeyboard())
        // klikni na ok button
        onView(withId(R.id.ok_button)).perform(click())

        // provjeri da je EditTemplateActivity pokrenut
        intended(hasComponent(EditTemplateActivity::class.java.name))

        // provjeri da na zaslonu pise bench press
        onView(withId(R.id.exercise_name)).check(matches(withText("Ab Rollout")))
    }

    @Test
    fun scrollTest() {
        // pokreni MainActivity
        ActivityScenario.launch(AddExerciseActivity::class.java)

        // scrollaj do 20. vjezbe
        onView(withId(R.id.exercises_recycler_view))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(19))
    }
}