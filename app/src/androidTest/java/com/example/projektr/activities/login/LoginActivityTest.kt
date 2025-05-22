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

class LoginActivityTest {

//    @get:Rule
//    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun loginAndStartMainActivity() {

        // pokreni LoginActivity
        val scenario = ActivityScenario.launch(LoginActivity::class.java)

        // mockaj FirebaseAuth
        scenario.onActivity { activity ->
            val mockAuth = mock(FirebaseAuth::class.java)
            val mockTask = Tasks.forResult(mock(AuthResult::class.java))
            `when`(mockAuth.signInWithEmailAndPassword(anyString(), anyString()))
                .thenReturn(mockTask)

            activity.auth = mockAuth
        }

        // upisi testne podatke
        onView(withId(R.id.email)).perform(typeText("test@example.com"), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(typeText("password123"), closeSoftKeyboard())
        onView(withId(R.id.login_btn)).perform(click())

        // provjeri da je MainActivity pokrenuta
        intended(hasComponent(MainActivity::class.java.name))
    }

}