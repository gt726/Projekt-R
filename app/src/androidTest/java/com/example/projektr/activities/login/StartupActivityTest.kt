package com.example.projektr.activities.login

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
import com.example.projektr.activities.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
class StartupActivityTest {

    private lateinit var mockAuth: FirebaseAuth

    @Before
    fun setUp() {
        // pokrece espresso intents za pracenje navigacije
        Intents.init()

        // mockaj FirebaseAuth da vrati null korisnika
        mockAuth = Mockito.mock(FirebaseAuth::class.java)
        Mockito.`when`(mockAuth.currentUser).thenReturn(null)
        FirebaseAuth.getInstance().signOut()

    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun startLoginActivityWorks() {
        // pokreni StartupActivity
        ActivityScenario.launch(StartupActivity::class.java)

        // provjeri da se gumbi prikazuju
        onView(withId(R.id.log_in_btn)).check(matches(isDisplayed()))
        onView(withId(R.id.sign_up_btn)).check(matches(isDisplayed()))

        // klik na "Log In" otvara LoginActivity
        onView(withId(R.id.log_in_btn)).perform(click())
        intended(hasComponent(LoginActivity::class.java.name))
    }

    @Test
    fun startRegisterActivityWorks() {
        // pokreni StartupActivity
        ActivityScenario.launch(StartupActivity::class.java)

        // provjeri da se gumbi prikazuju
        onView(withId(R.id.log_in_btn)).check(matches(isDisplayed()))
        onView(withId(R.id.sign_up_btn)).check(matches(isDisplayed()))

        // klik na "Sign Up" otvara RegisterActivity
        onView(withId(R.id.sign_up_btn)).perform(click())
        intended(hasComponent(RegisterActivity::class.java.name))
    }

    //@Test
    fun starMainWhenLoggedIn() {
        val mockUser = Mockito.mock(FirebaseUser::class.java)
        Mockito.`when`(mockAuth.currentUser).thenReturn(mockUser)

        ActivityScenario.launch(StartupActivity::class.java)

        // provjeri da se MainActivity pokreće
        intended(hasComponent(MainActivity::class.java.name))

        Intents.release()
    }
}
