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
import com.example.aptitudefitnesstracker.application.IFirebaseModeObserver
import com.example.aptitudefitnesstracker.application.Session
import com.example.aptitudefitnesstracker.databinding.ActivityExerciseListBinding
import com.example.aptitudefitnesstracker.presentation.ThemeUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ExerciseDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ExerciseListActivity : AppCompatActivity(), IFirebaseModeObserver {
    private val session: Session by lazy {
        val session = application as Session
        session.addObserver(this)
        session
    }
    private lateinit var binding: ActivityExerciseListBinding

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
        
        binding = ActivityExerciseListBinding.inflate(layoutInflater)
        
        
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = "Exercises"

        binding.newExerciseFAB.setOnClickListener { view ->
            newExerciseFABClicked()
        }

        binding.newExerciseButton.setOnClickListener {
            Toast.makeText(this, "New Exercise Button", Toast.LENGTH_SHORT).show()
            var exercise = Exercise()
            session.insertExercise( exercise )
            intent = Intent(this, EditExerciseActivity::class.java)
            session.activeExercise = exercise
            startActivity(intent)


        }
        binding.newExerciseFromRoutineButton.setOnClickListener {
            Toast.makeText(this, "newExerciseFromRoutineButton", Toast.LENGTH_SHORT).show()
            //TODO Implement
        }

//        downloadExerciseButton.setOnClickListener {   //fixme merge conflict between this and below, get Ben and Malek to sync them up
//            downloadButtonClicked()
//        }

        binding.viewOnlineExercisesButton.setOnClickListener {
            if (!session.firebaseMode) {
                toolbar.title = "Viewing Online Exercises"
                binding.viewOnlineExercisesButton.setImageResource(R.drawable.ic_baseline_system_update_24)
            } else {
                binding.viewOnlineExercisesButton.setImageResource(R.drawable.ic_baseline_cloud_download_24)
                if (session.activeRoutine == null) {
                    finish()
                    finishAffinity()
                }

            }
        }

        setupRecyclerView()
    }

    private fun downloadButtonClicked() {
        if (session.userIsLoggedIn()) {
            session.toggleFirebaseMode()
        }
        else {
            startActivity(Intent(this@ExerciseListActivity, LoginActivity::class.java))
        }
    }

    fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.item_list)
        val adapter = ExerciseRecyclerViewAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val routineList: LiveData<List<Exercise>>? = session.getProperExercises()
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
            else -> super.onOptionsItemSelected(item)
        }
    }

    /* END HERE For create Setting option menu */


    private fun newExerciseFABClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
        setClickable(clicked)
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            binding.newExerciseButton.visibility = View.VISIBLE
            binding.newExerciseFromRoutineButton.visibility = View.VISIBLE
            binding.viewOnlineExercisesButton.visibility = View.VISIBLE
        } else {
            binding.newExerciseButton.visibility = View.INVISIBLE
            binding.newExerciseFromRoutineButton.visibility = View.INVISIBLE
            binding.viewOnlineExercisesButton.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            binding.newExerciseButton.startAnimation(fromBott)
            binding.newExerciseFromRoutineButton.startAnimation(fromBott)
            binding.viewOnlineExercisesButton.startAnimation(fromBott)
            binding.newExerciseFAB.startAnimation(rotateOpen)
        } else {
            binding.newExerciseButton.startAnimation(toBott)
            binding.newExerciseFromRoutineButton.startAnimation(toBott)
            binding.viewOnlineExercisesButton.startAnimation(toBott)
            binding.newExerciseFAB.startAnimation(rotateClose)
        }
    }

    private fun setClickable(clicked: Boolean) {
        if (!clicked) {
            binding.newExerciseButton.isClickable = false
            binding.newExerciseFromRoutineButton.isClickable = false
            binding.viewOnlineExercisesButton.isClickable = false


        } else {
            binding.newExerciseButton.isClickable = true
            binding.newExerciseFromRoutineButton.isClickable = true
            binding.viewOnlineExercisesButton.isClickable = true

        }
    }

    override fun notify(mode: Boolean) {
        setupRecyclerView()
    }
}