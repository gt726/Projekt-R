package com.example.projektr.activities

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.projektr.R
import com.example.projektr.database.FinishedWorkoutRepository
import com.example.projektr.database.FirestoreFinishedWorkout.FinishedWorkoutExercise
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExerciseDetailActivity : AppCompatActivity() {

    //    private val workoutRepository = FinishedWorkoutRepository()
    private val workoutRepository = FinishedWorkoutRepository.instance
        ?: FinishedWorkoutRepository()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_exercise_detail)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        // dohvati podatke iz intenta
        val exerciseName = intent.getStringExtra("exercise_name") ?: ""

        // dohvati elemente
        val naslov = findViewById<TextView>(R.id.exercise_name)
        val maxWeight = findViewById<TextView>(R.id.max_weight_value)
        val maxSetVolume = findViewById<TextView>(R.id.max_set_value)
        val maxSessionVolume = findViewById<TextView>(R.id.max_session_value)

        // postavi naslov
        naslov.text = exerciseName

        lifecycleScope.launch {
            // dohvati sve zavrsene workoutove za tu vjezbu
            val entries = workoutRepository.getAllEntriesForExercise(exerciseName)
            val maxWeightValue: String
            val maxSetValue: String
            val maxSessionValue: String

            if (entries.isNotEmpty()) {
                // izracunaj trazene vrijednosti
                maxWeightValue = getMaxWeight(entries).toString()
                maxSetValue = getBestSet(entries)
                maxSessionValue = getBestSession(entries)


            } else {
                // default vrijednosti
                maxWeightValue = "---"
                maxSetValue = "--- kg x --- reps"
                maxSessionValue = "--- kg"
            }
            maxWeight.text = "$maxWeightValue kg"
            maxSetVolume.text = "$maxSetValue"
            maxSessionVolume.text = "$maxSessionValue"


            // max weight
            val maxWeightChart: LineChart = findViewById(R.id.max_weight_chart)
            val maxWeightEntries = getWeightHistory(entries, "maxWeight")

            if (maxWeightEntries.isEmpty()) {
                // ispisi "No Data" ako nema podataka
                maxWeightChart.setNoDataText("No Data Available")
                maxWeightChart.setNoDataTextColor(Color.WHITE)  // boja teksta
            } else {
                setupLineChart(maxWeightEntries, maxWeightChart)
            }

            // best set
            val bestSetChart: LineChart = findViewById(R.id.best_set_chart)
            val bestSetEntries = getWeightHistory(entries, "bestSet")

            if (bestSetEntries.isEmpty()) {
                // ispisi "No Data" ako nema podataka
                bestSetChart.setNoDataText("No Data Available")
                bestSetChart.setNoDataTextColor(Color.WHITE)  // boja teksta
            } else {
                setupLineChart(bestSetEntries, bestSetChart)
            }

            // session volume
            val sessionVolumeChart: LineChart = findViewById(R.id.session_volume_chart)
            val sessionVolumeEntries = getWeightHistory(entries, "sessionVolume")

            if (sessionVolumeEntries.isEmpty()) {
                // ispisi "No Data" ako nema podataka
                sessionVolumeChart.setNoDataText("No Data Available")
                sessionVolumeChart.setNoDataTextColor(Color.WHITE)  // boja teksta
            } else {
                setupLineChart(sessionVolumeEntries, sessionVolumeChart)
            }
        }
    }

    private suspend fun getWeightHistory(
        entries: List<FinishedWorkoutExercise>,
        type: String
    ): List<Entry> {
        // map oblika <datum, max tezina>
        val sessionMax = mutableMapOf<Long, Float>()

        for (entry in entries) {
            // razdvoji tezine i broj ponavljanja te dohvati workout ID i datum
            val weights = entry.weights.split(",").mapNotNull { it.toFloatOrNull() }
            val reps = entry.reps.split(",").mapNotNull { it.toIntOrNull() }
            val workoutId = entry.workoutId
            val date =
                workoutRepository.getFinishedWorkouts().find { it.id == workoutId }?.date ?: 0L

            // default vrijednost
            var maxWeight: Float = 0f

            // dohvati najvecu tezinu iz setova
            if (type == "maxWeight") {
                maxWeight = weights.maxOrNull() ?: 0f
            }
            // dohvati najbolji set iz sessiona
            else if (type == "bestSet") {
                for (i in weights.indices) {
                    if (i < reps.size) {
                        val score = weights[i] * reps[i]
                        maxWeight = maxOf(maxWeight, score)
                    }
                }
            }
            // dohvati volume za cijeli session
            else if (type == "sessionVolume") {
                for (i in weights.indices) {
                    if (i < reps.size) {
                        maxWeight += weights[i] * reps[i]
                    }
                }
            }
            // spremi najvecu tezinu za taj datum
            sessionMax[date] = maxOf(sessionMax.getOrDefault(date, 0f), maxWeight)
        }
        val chartEntries = mutableListOf<Entry>()

        for ((date, weight) in sessionMax) {
            chartEntries.add(Entry(date.toFloat(), weight))
        }


        return chartEntries.sortedBy { it.x }
    }

    private fun setupLineChart(entries: List<Entry>, chart: LineChart) {
        // kreiraj novi LineDataSet
        val dataSet = LineDataSet(entries, "Weight Progress")

        // formatiranje grafa
        dataSet.color = Color.BLUE
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 14f
        dataSet.lineWidth = 2f
        dataSet.setCircleColor(Color.WHITE) // tocke na grafu

        // formatiraj x os
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.textColor = Color.WHITE
        chart.xAxis.textSize = 12f

        // formatiraj y os
        chart.axisLeft.textColor = Color.WHITE
        chart.axisLeft.textSize = 12f
        chart.axisRight.isEnabled = false

        // ukloni nepotrebne elemente
        chart.description.isEnabled = false
        chart.legend.isEnabled = false
        chart.xAxis.setDrawGridLines(false)

        // postavi podatke na graf
        val lineData = LineData(dataSet)
        chart.data = lineData

        // formatiraj datume za prikaz na x osi
        chart.xAxis.valueFormatter = object : ValueFormatter() {
            private val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())

            override fun getFormattedValue(value: Float): String {
                return dateFormat.format(Date(value.toLong()))
            }
        }

        chart.extraLeftOffset = 14f  // padding lijevo
        chart.extraRightOffset = 14f // padding desno

        chart.invalidate() // osvjezi graf
    }
}

fun getMaxWeight(entries: List<FinishedWorkoutExercise>): Float {
    return entries.maxOfOrNull {
        it.weights.split(",").filter { w -> w.isNotEmpty() }.maxOfOrNull { w -> w.toFloat() }
            ?: 0f
    } ?: 0f
}

fun getBestSet(entries: List<FinishedWorkoutExercise>): String {
    var bestSet = ""
    var bestWeight = 0f
    for (entry in entries) {
        val weights = entry.weights.split(",").mapNotNull { it.toFloatOrNull() }
        val reps = entry.reps.split(",").mapNotNull { it.toIntOrNull() }
        for (i in weights.indices) {
            if (i < reps.size) {
                val score = weights[i] * reps[i]
                if (score > bestWeight) {
                    bestWeight = score
                    bestSet = "${weights[i]} kg x ${reps[i]} reps"
                }
            }
        }
    }
    return bestSet
}

fun getBestSession(entries: List<FinishedWorkoutExercise>): String {
    // grupiraj setove po workout ID-u
    val sessionVolumes = mutableMapOf<String, Float>()

    for (entry in entries) {
        // razdvoji tezine i broj ponavljanja te dohvati workout ID
        val weights = entry.weights.split(",").mapNotNull { it.toFloatOrNull() }
        val reps = entry.reps.split(",").mapNotNull { it.toIntOrNull() }
        val workoutId = entry.workoutId

        var sessionVolume = 0f
        for (i in weights.indices) {
            if (i < reps.size) {
                sessionVolume += weights[i] * reps[i]
            }
        }

        // dodaj ukupnom zbroju za taj workout ID
        sessionVolumes[workoutId] = sessionVolumes.getOrDefault(workoutId, 0f) + sessionVolume
    }

    // dohvati workout ID s najvecim volumenom
    val bestWorkoutId = sessionVolumes.maxByOrNull { it.value }?.key
    val bestVolume = sessionVolumes[bestWorkoutId] ?: 0f

    return if (bestWorkoutId != null) {
        "$bestVolume kg"
    } else {
        "---"
    }
}