package com.example.projektr.activities.login

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.projektr.R
import com.example.projektr.activities.MainActivity
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class RegisterActivityTest {

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun registerAndStartMainActivity() {

        // pokreni RegisterActivity
        val scenario = ActivityScenario.launch(RegisterActivity::class.java)

        // mockaj FirebaseAuth
        scenario.onActivity { activity ->
            val mockAuth = mock(FirebaseAuth::class.java)
            val mockTask = Tasks.forResult(mock(AuthResult::class.java))
            `when`(mockAuth.createUserWithEmailAndPassword(anyString(), anyString()))
                .thenReturn(mockTask)
            activity.auth = mockAuth
        }

        // unesi testne podatke
        onView(withId(R.id.name)).perform(typeText("Test User"), closeSoftKeyboard())
        onView(withId(R.id.email)).perform(typeText("test@example.com"), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(typeText("password123"), closeSoftKeyboard())
        onView(withId(R.id.login_btn)).perform(click())

        // provjeri da je pokrenuta MainActivity
        intended(hasComponent(MainActivity::class.java.name))
    }
}