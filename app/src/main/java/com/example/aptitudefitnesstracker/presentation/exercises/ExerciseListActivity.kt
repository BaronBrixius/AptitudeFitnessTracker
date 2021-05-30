package com.example.aptitudefitnesstracker.presentation.exercises

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.*
import com.example.aptitudefitnesstracker.R
import com.example.aptitudefitnesstracker.application.data.Exercise
import com.example.aptitudefitnesstracker.application.IFirebaseModeObserver
import com.example.aptitudefitnesstracker.application.Session
import com.example.aptitudefitnesstracker.presentation.authentication.LoginActivity
import com.example.aptitudefitnesstracker.presentation.routines.EditRoutineActivity
import com.example.aptitudefitnesstracker.presentation.settings.SettingsActivity
import com.example.aptitudefitnesstracker.presentation.settings.ThemeUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*


class ExerciseListActivity : AppCompatActivity(), IFirebaseModeObserver {
    val session: Session by lazy {
        val session = application as Session
        session.addObserver(this)
        session
    }
    private lateinit var newExerciseFAB: FloatingActionButton
    private lateinit var newExerciseButton: FloatingActionButton
    private lateinit var editRoutineButton: FloatingActionButton


    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_open_anim
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_close_anim
        )
    }
    private val fromBott: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.from_bott_anim
        )
    }
    private val toBott: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.to_bott_anim
        )
    }
    private var clicked = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.setThemeApp(this) // for set theme
        ThemeUtils.setAppFont(this) // for set font size
        ThemeUtils.setAppFontFamily(this) // for set font family
        setContentView(R.layout.activity_exercise_list)
        val toolbar = findViewById<Toolbar>(R.id.exercise_toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = "Exercises in " + session.activeRoutine!!.name

        newExerciseFAB = findViewById(R.id.newExerciseFAB)
        newExerciseButton = findViewById(R.id.newExerciseButton)
        editRoutineButton = findViewById(R.id.editRoutineButton)

        if(session.firebaseMode){
            editRoutineButton.setImageResource(R.drawable.ic_baseline_cloud_download_24)
            newExerciseButton.visibility = View.GONE
        }

        newExerciseFAB.setOnClickListener { view ->
            newExerciseFABClicked()
        }

        newExerciseButton.setOnClickListener {
            session.createExerciseInRoutine(Exercise("New Exercise"), session.activeRoutine!!)
            newExerciseFABClicked()
//            intent = Intent(this, EditExerciseActivity::class.java)
//            startActivity(intent)
        }

        editRoutineButton.setOnClickListener {

            if(session.firebaseMode){
                val downloadDialog = AlertDialog.Builder(this)
                downloadDialog.setTitle("Download Routine")
                downloadDialog.setMessage("Are you sure you would like to download and add this routine to your local list?")

                downloadDialog.setPositiveButton("Download") { dialog, which ->
                    var routine = session.activeRoutine
                    session.saveRemoteRoutineLocally(routine!!)

                }
                downloadDialog.setNegativeButton(android.R.string.no) { dialog, which ->
                }
                downloadDialog.show()
            }else{
                intent = Intent(this, EditRoutineActivity::class.java)
                startActivity(intent)
            }

        }
        setupRecyclerView()
    }


    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.item_parent_list_exercise)
        val adapter = ExerciseRecyclerViewAdapter(this)
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        if (session.activeRoutine == null) {
            finish()
            finishAffinity()
        }

        val exerciseList: LiveData<List<Exercise>>? = session.getProperExercises()
        exerciseList!!.observe(this, { exercises ->
            exercises?.let { adapter.setList(it) }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_settings, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.actionAppSettings -> {
                startActivity(Intent(this@ExerciseListActivity, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun newExerciseFABClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
        setClickable(clicked)
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            if (!session.firebaseMode) {
                newExerciseButton.visibility = View.VISIBLE
                editRoutineButton.visibility = View.VISIBLE
            }
        } else {

            if (!session.firebaseMode) {
                newExerciseButton.visibility = View.INVISIBLE
                editRoutineButton.visibility = View.INVISIBLE
            }
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {

            newExerciseFAB.startAnimation(rotateOpen)
            newExerciseButton.startAnimation(fromBott)
            editRoutineButton.startAnimation(fromBott)
        } else {

            newExerciseFAB.startAnimation(rotateClose)

            newExerciseButton.startAnimation(toBott)
            editRoutineButton.startAnimation(toBott)

        }
    }

    private fun setClickable(clicked: Boolean) {

        newExerciseButton.isClickable = clicked
        editRoutineButton.isClickable = clicked

    }

    override fun notify(mode: Boolean) {
        setupRecyclerView()
    }
}