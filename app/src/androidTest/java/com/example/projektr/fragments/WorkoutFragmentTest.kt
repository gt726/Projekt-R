package com.example.projektr.fragments

import androidx.lifecycle.LifecycleOwner
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
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.projektr.R
import com.example.projektr.activities.MainActivity
import com.example.projektr.activities.SettingsActivity
import com.example.projektr.activities.template.AddExerciseActivity
import com.example.projektr.activities.template.EditTemplateActivity
import com.example.projektr.adapters.TemplateAdapter
import com.example.projektr.database.FirestoreTemplate
import com.example.projektr.database.TemplateRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class WorkoutFragmentTest {

    @Before
    fun setUp() {
        // mockaj FirebaseAuth i FirebaseUser
        val mockAuth = mock(FirebaseAuth::class.java)
        val mockUser = mock(FirebaseUser::class.java)

        `when`(mockUser.uid).thenReturn("testUserId")
        `when`(mockAuth.currentUser).thenReturn(mockUser)

        // injektiraj mock u TemplateRepository
        TemplateRepository.instance = TemplateRepository(mockAuth)
    }


    // provjeri pokretanje Settings aktivnosti
    @Test
    fun startSettingsActivity() {
        Intents.init()
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.settings_icon)).perform(click())
        intended(hasComponent(SettingsActivity::class.java.name))

        Intents.release()
    }

    // provjeri pokretanje AddExercise aktivnosti
    @Test
    fun startAddExerciseActivity() {
        Intents.init()
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.create_template_button)).perform(click())
        intended(hasComponent(AddExerciseActivity::class.java.name))

        Intents.release()
    }

    //@Test
    fun templateListIsDisplayed() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
    }

    //@Test
    fun renameTemplate() = runBlocking {
        // Mock podaci
        val mockTemplates = mutableListOf(
            Pair(
                FirestoreTemplate.Template(
                    id = "template1",
                    name = "Push Day",
                    userId = "testUserId"
                ),
                emptyList<FirestoreTemplate.TemplateExercise>()
            ),
            Pair(
                FirestoreTemplate.Template(
                    id = "template2",
                    name = "Pull Day",
                    userId = "testUserId"
                ),
                emptyList<FirestoreTemplate.TemplateExercise>()
            )
        )

        // mock repo
        val mockTemplateRepository = mock(TemplateRepository::class.java)

        // kreiraj adapter s mock repo
        val adapter = TemplateAdapter(
            templatesWithExercises = mockTemplates,
            lifecycleOwner = mock(LifecycleOwner::class.java),
            templateRepository = mockTemplateRepository
        )


        val newName = "Leg Day"
        mockTemplateRepository.renameTemplate("template1", newName)

        verify(mockTemplateRepository).renameTemplate("template1", newName)
    }

    //@Test
    fun testPopupMenuOptions_showsAndResponds() {
        Intents.init()
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0, click()
                )
            )

        onView(allOf(withId(R.id.overflow_menu), isDisplayed()))
            .perform(click())

        onView(withText("Edit")).check(matches(isDisplayed()))
        onView(withText("Rename")).check(matches(isDisplayed()))
        onView(withText("Delete")).check(matches(isDisplayed()))

        onView(withText("Edit")).perform(click())
        intended(hasComponent(EditTemplateActivity::class.java.name))

        Intents.release()
    }

}