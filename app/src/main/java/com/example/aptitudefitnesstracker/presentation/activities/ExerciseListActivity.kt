package com.example.aptitudefitnesstracker.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aptitudefitnesstracker.R
import com.example.aptitudefitnesstracker.application.Exercise
import com.example.aptitudefitnesstracker.application.Session
import com.example.aptitudefitnesstracker.presentation.ThemeUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_exercise_list.*


/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ExerciseDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ExerciseListActivity : AppCompatActivity() {
    private val session: Session by lazy { application as Session }

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
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = "Exercises"

        findViewById<FloatingActionButton>(R.id.newExerciseFAB).setOnClickListener { view ->
            newExerciseFABClicked()
        }

        newExerciseButton.setOnClickListener {
            Toast.makeText(this, "New Exercise Button", Toast.LENGTH_SHORT).show()
            //TODO Implement
        }
        newExerciseFromRoutineButton.setOnClickListener {
            Toast.makeText(this, "newExerciseFromRoutineButton", Toast.LENGTH_SHORT).show()
            //TODO Implement
        }
        downloadExerciseButton.setOnClickListener {
            Toast.makeText(this, "downloadExerciseButton", Toast.LENGTH_SHORT).show()
            //TODO Implement
        }

        setupRecyclerView()
    }

    fun toggleDownloadMode() {
        session.firebaseMode = !session.firebaseMode
        setupRecyclerView()
    }

    fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.item_list)
        val adapter = ExerciseRecyclerViewAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val routineList: LiveData<List<Exercise>>? =
            if (session.firebaseMode) session.downloadRemoteExercises() else session.activeRoutine?.exercises
        routineList!!.observe(this, { routines ->
            routines?.let { adapter.submitList(it) }
        })
    }

    class ExerciseRecyclerViewAdapter(
        private val parentActivity: ExerciseListActivity
    ) :
        ListAdapter<Exercise, ExerciseRecyclerViewAdapter.ExerciseViewHolder>(ExerciseComparator()) {
        private val onClickListener: View.OnClickListener = View.OnClickListener { v ->
            val exercise = v.tag as Exercise

            parentActivity.session.activeExercise = exercise
            val intent = Intent(v.context, ExerciseDetailActivity::class.java)
            v.context.startActivity(intent)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_content, parent, false)
            return ExerciseViewHolder(view)
        }

        override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
            val exercise = getItem(position)
            //holder.bind("id: " + item.id + " name: " + item.name)
            holder.idView.text =
                "id: " + exercise.id    //fixme placeholder stuff for database testing
            holder.contentView.text = " name: " + exercise.name

            with(holder.itemView) {
                tag = exercise
                setOnClickListener(onClickListener)
            }
        }

        inner class ExerciseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val idView: TextView = view.findViewById(R.id.id_text)
            val contentView: TextView =
                view.findViewById(R.id.content) //no clue what this is, feel free to use it
            /*fun bind(text: String?) {
                idView.text = text
            }*/
        }

        class ExerciseComparator : DiffUtil.ItemCallback<Exercise>() {
            override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: Exercise,
                newItem: Exercise
            ): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }

    /* START HERE For create Setting option menu  */
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
            R.id.AccountSettingsItem -> {
                startActivity(Intent(this@ExerciseListActivity, AccountActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /* END HERE For create Setting option menu */
    /* for destroy all previous activity */
//    override fun onBackPressed() {
//        super.onBackPressed()
//        finish()
//        finishAffinity()
//    }

    private fun newExerciseFABClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
        setClickable(clicked)
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            newExerciseButton.visibility = View.VISIBLE
            newExerciseFromRoutineButton.visibility = View.VISIBLE
            downloadExerciseButton.visibility = View.VISIBLE
        } else {
            newExerciseButton.visibility = View.INVISIBLE
            newExerciseFromRoutineButton.visibility = View.INVISIBLE
            downloadExerciseButton.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            newExerciseButton.startAnimation(fromBott)
            newExerciseFromRoutineButton.startAnimation(fromBott)
            downloadExerciseButton.startAnimation(fromBott)
            newExerciseFAB.startAnimation(rotateOpen)
        } else {
            newExerciseButton.startAnimation(toBott)
            newExerciseFromRoutineButton.startAnimation(toBott)
            downloadExerciseButton.startAnimation(toBott)
            newExerciseFAB.startAnimation(rotateClose)
        }
    }

    private fun setClickable(clicked: Boolean) {
        if (!clicked) {
            newExerciseButton.isClickable = false
            newExerciseFromRoutineButton.isClickable = false
            downloadExerciseButton.isClickable = false


        } else {
            newExerciseButton.isClickable = true
            newExerciseFromRoutineButton.isClickable = true
            downloadExerciseButton.isClickable = true

        }
    }
}