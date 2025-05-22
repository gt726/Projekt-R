package com.example.projektr.activities

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.projektr.R
import com.example.projektr.adapters.finished_workout.FWExerciseAdapter
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

class FinishedWorkoutActivityTest {
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
    fun showHistoryDetails() {
        // pokreni FinishedWorkoutActivity
        val scenario = ActivityScenario.launch(FinishedWorkoutActivity::class.java)

        // ubaci mock podatke u adapter
        scenario.onActivity { activity ->
            val mockExercises = mutableListOf(
                FirestoreFinishedWorkout.FinishedWorkoutExercise(
                    id = "testExerciseId",
                    workoutId = "testWorkoutId",
                    exerciseName = "Bench Press",
                    weights = "100,105",
                    reps = "5,5"
                ),
                FirestoreFinishedWorkout.FinishedWorkoutExercise(
                    id = "testExercise2Id",
                    workoutId = "testWorkoutId",
                    exerciseName = "Squat",
                    weights = "200,150",
                    reps = "5,5"
                ),
                FirestoreFinishedWorkout.FinishedWorkoutExercise(
                    id = "testExercise3Id",
                    workoutId = "testWorkoutId",
                    exerciseName = "Deadlift",
                    weights = "300,245",
                    reps = "5,5"
                )
            )

            // kreiraj adapter s mock podacima
            val adapter = FWExerciseAdapter(mockExercises)

            // dodijeli adapter recyclerViewu
            activity.findViewById<RecyclerView>(R.id.recycler_view).apply {
                layoutManager = LinearLayoutManager(activity)
                this.adapter = adapter
            }
        }

        // provjeri da se prikazuje naziv vjezbe
        onView(withText("Bench Press")).check(matches(isDisplayed()))
        onView(withText("Squat")).check(matches(isDisplayed()))
        onView(withText("Deadlift")).check(matches(isDisplayed()))

        // provjeri da se prikazuju tezina i broj ponavljanja
        onView(withText("100 kg x 5")).check(matches(isDisplayed()))
        onView(withText("105 kg x 5")).check(matches(isDisplayed()))
    }
}