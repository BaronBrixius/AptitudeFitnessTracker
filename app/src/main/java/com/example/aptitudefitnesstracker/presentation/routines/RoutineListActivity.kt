package com.example.aptitudefitnesstracker.presentation.routines

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.*
import com.example.aptitudefitnesstracker.R
import com.example.aptitudefitnesstracker.application.IFirebaseModeObserver
import com.example.aptitudefitnesstracker.application.data.Routine
import com.example.aptitudefitnesstracker.application.Session
import com.example.aptitudefitnesstracker.presentation.authentication.LoginActivity
import com.example.aptitudefitnesstracker.presentation.settings.SettingsActivity
import com.example.aptitudefitnesstracker.presentation.settings.ThemeUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class RoutineListActivity : AppCompatActivity(), IFirebaseModeObserver {
    val session: Session by lazy {
        val session = application as Session
        session.addObserver(this)
        session
    }
    private var clicked = false

    private lateinit var newRoutineFAB: FloatingActionButton
    private lateinit var newRoutineButton: FloatingActionButton
    private lateinit var viewOnlineRoutinesButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.setThemeApp(this) // for set theme
        ThemeUtils.setAppFont(this) // for set font size
        ThemeUtils.setAppFontFamily(this) // for set font family
        setContentView(R.layout.activity_routine_list)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        newRoutineFAB = findViewById(R.id.newRoutineFAB)
        newRoutineButton = findViewById(R.id.newRoutineButton)
        viewOnlineRoutinesButton = findViewById(R.id.viewOnlineRoutinesButton)

        //Add new
        findViewById<FloatingActionButton>(R.id.newRoutineFAB).setOnClickListener { view ->
            newRoutineFABClicked()
        }

        newRoutineButton.setOnClickListener {
            val intent = Intent(this, AddRoutineActivity::class.java)
            startActivity(intent)
        }

        viewOnlineRoutinesButton.setOnClickListener {
            viewOnlineRoutinesButtonClicked()
        }

        setupRecyclerView()

        if (session.userIsLoggedIn()) {
            println("USER LOGGED IN")
        } else {
            println("NOT LOGGED IN!")
        }
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerView()
    }

    private fun viewOnlineRoutinesButtonClicked() {
        if (session.userIsLoggedIn())
            session.toggleAndGetFirebaseMode()
        else
            startActivity(Intent(this@RoutineListActivity, LoginActivity::class.java))
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.item_parent_list_routine)
        val adapter = RoutineRecyclerViewAdapter(this)
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        if (session.firebaseMode) {
            toolbar.title = "Viewing Online Routines"
            viewOnlineRoutinesButton.setImageResource(R.drawable.ic_baseline_system_update_24)
        } else {
            toolbar.title = "Personal Routines"
            viewOnlineRoutinesButton.setImageResource(R.drawable.ic_baseline_cloud_download_24)
        }

        val routineList: LiveData<List<Routine>> = session.getProperRoutines()
        routineList.observe(this, { routines ->
            routines?.let { adapter.setList(it) }
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
                startActivity(Intent(this@RoutineListActivity, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun newRoutineFABClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
        setClickable(clicked)
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            newRoutineButton.visibility = View.VISIBLE
            viewOnlineRoutinesButton.visibility = View.VISIBLE
        } else {
            newRoutineButton.visibility = View.INVISIBLE
            viewOnlineRoutinesButton.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            newRoutineFAB.startAnimation(rotateOpen)
            if (!session.firebaseMode) {
                newRoutineButton.startAnimation(fromBott)
                viewOnlineRoutinesButton.startAnimation(fromBott)
            }

        } else {
            newRoutineFAB.startAnimation(rotateClose)
            if (!session.firebaseMode) {
                newRoutineButton.startAnimation(toBott)
                viewOnlineRoutinesButton.startAnimation(toBott)
            }
        }
    }

    private fun setClickable(clicked: Boolean) {
        viewOnlineRoutinesButton.isClickable = clicked
        if (!session.firebaseMode)
            newRoutineButton.isClickable = clicked
    }

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

    override fun notify(mode: Boolean) {
        setupRecyclerView()
    }
}