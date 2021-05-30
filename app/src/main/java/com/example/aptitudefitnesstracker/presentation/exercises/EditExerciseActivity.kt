package com.example.aptitudefitnesstracker.presentation.exercises

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.*
import com.example.aptitudefitnesstracker.R
import com.example.aptitudefitnesstracker.application.data.Exercise
import com.example.aptitudefitnesstracker.application.Session
import com.example.aptitudefitnesstracker.presentation.settings.ThemeUtils
import java.util.*

class EditExerciseActivity : AppCompatActivity() {
    private var inputDetailsLayout: RecyclerView? = null
    private var inputName: EditText? = null
    private var inputNotes: EditText? = null
    private var inputDetails: EditText? = null
    private var inputDetailsValue: EditText? = null
    private var btnSave: Button? = null
    private var btnDelete: Button? = null
    private var btnAddDetail: Button? = null

    private lateinit var adapter : ExerciseDetailsRecyclerViewAdapter

    private val session: Session by lazy { application as Session }
    private lateinit var exercise: Exercise

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.setThemeApp(this)
        ThemeUtils.setAppFont(this)
        ThemeUtils.setAppFontFamily(this)
        setContentView(R.layout.activity_add_exercise)
        inputName = findViewById<View>(R.id.name) as EditText
        inputDetailsLayout = findViewById(R.id.detail_list)
        inputDetails = findViewById(R.id.detail_name)
        inputDetailsValue = findViewById(R.id.detail_value)
        inputNotes = findViewById<View>(R.id.Notes) as EditText
        btnSave = findViewById<View>(R.id.btn_save) as Button
        btnDelete = findViewById<View>(R.id.btn_delete) as Button
        btnAddDetail = findViewById<View>(R.id.btn_add_detail) as Button

        findViewById<Toolbar>(R.id.toolbar).title = "Edit Exercise"

        exercise = session.activeExercise!!
        inputName!!.setText(exercise.name)
        inputNotes!!.setText(exercise.notes)

        // Save / update the exercise
        btnSave!!.text = "Save"
        btnSave!!.setOnClickListener {
            val name = inputName!!.text.toString()
            val notes = inputNotes!!.text.toString()
//            val details = inputDetails!!.text.toString()
//            val detailsValue = inputDetailsValue!!.text.toString()
//            exercise.details[details] = detailsValue.toDouble()

            /**
             * Pop up dialog to confirm saving changes
             */
            val saveDialog = AlertDialog.Builder(this)
            saveDialog.setTitle("Confirm Changes")
            saveDialog.setMessage("Are you sure you would like to save changes?")

            saveDialog.setPositiveButton("Save") { _, _ ->
                if (inputName!!.text.toString() != "") {
                    exercise.name = name
                }

                exercise.details = adapter.detailList!!

                if (inputNotes!!.text.toString() != "") {
                    exercise.notes = notes
                }

                session.updateExercise(exercise)
                finish()
            }
            saveDialog.setNegativeButton(android.R.string.no) { _, _ ->
                finish()
            }
            saveDialog.show()
        }

        btnDelete!!.setOnClickListener {
            val deleteDialog = AlertDialog.Builder(this)
            deleteDialog.setTitle("Delete Exercise")
            deleteDialog.setMessage("Are you sure?")

            deleteDialog.setPositiveButton("Delete") { dialog, which ->
                session.deleteExercise(exercise)
                finish()
            }
            deleteDialog.setNegativeButton(android.R.string.no) { dialog, which ->
                finish()
            }
            deleteDialog.show()
        }

        btnAddDetail!!.setOnClickListener{
            exercise.details.add(Exercise.Detail())
            session.updateExercise(exercise)
            setupRecyclerView()
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.detail_list)
        adapter = ExerciseDetailsRecyclerViewAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter.setList(exercise.details)
    }
}