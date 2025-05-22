package com.example.projektr.activities

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.projektr.R
import com.example.projektr.database.FinishedWorkoutRepository
import com.example.projektr.database.TemplateRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`

class ExerciseDetailActivityTest {

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
    fun testExerciseNameIsDisplayed() {
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), ExerciseDetailActivity::class.java)
        intent.putExtra("exercise_name", "Bench Press")

        ActivityScenario.launch<ExerciseDetailActivity>(intent).use {
            onView(withId(R.id.exercise_name)).check(matches(withText("Bench Press")))

            onView(withId(R.id.max_weight)).check(matches(isDisplayed()))
            onView(withId(R.id.max_set_volume)).check(matches(isDisplayed()))
            onView(withId(R.id.max_session_volume)).check(matches(isDisplayed()))
        }
    }
}