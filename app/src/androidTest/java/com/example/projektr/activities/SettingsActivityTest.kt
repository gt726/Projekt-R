package com.example.projektr.activities

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.projektr.R
import com.example.projektr.activities.login.StartupActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class SettingsActivityTest {

    @Before
    fun setUp() {
        // pokrece espresso intents za pracenje navigacije
        Intents.init()

        // mockaj FirebaseAuth i FirebaseUser
        val mockAuth = mock(FirebaseAuth::class.java)
        val mockUser = mock(FirebaseUser::class.java)

        `when`(mockUser.uid).thenReturn("testUserId")
        `when`(mockAuth.currentUser).thenReturn(mockUser)
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun logoutWorks() {
        ActivityScenario.launch(SettingsActivity::class.java)

        // provjeri da se gumbi prikazuju
        onView(withId(R.id.log_out_btn)).check(matches(isDisplayed()))

        // izlogiraj se
        onView(withId(R.id.log_out_btn)).perform(click())
        // provjeri da se vraca na StartupActivity
        intended(hasComponent(StartupActivity::class.java.name))
    }
}