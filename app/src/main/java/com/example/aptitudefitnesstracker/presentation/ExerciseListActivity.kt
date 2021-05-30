package com.example.aptitudefitnesstracker.presentation

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
import androidx.recyclerview.widget.ListAdapter
import com.example.aptitudefitnesstracker.R
import com.example.aptitudefitnesstracker.application.Exercise
import com.example.aptitudefitnesstracker.application.IFirebaseModeObserver
import com.example.aptitudefitnesstracker.application.Session
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
    private lateinit var newExerciseFromRoutineButton: FloatingActionButton
    private lateinit var viewOnlineExercisesButton: FloatingActionButton


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
        newExerciseFromRoutineButton = findViewById(R.id.newExerciseFromRoutineButton)
        viewOnlineExercisesButton = findViewById(R.id.viewOnlineExercisesButton)

        newExerciseFAB.setOnClickListener { view ->
            newExerciseFABClicked()
        }

        newExerciseButton.setOnClickListener {
            session.createExerciseInRoutine(Exercise("New Exercise"), session.activeRoutine!!)
            newExerciseFABClicked()
            intent = Intent(this, EditExerciseActivity::class.java)
            startActivity(intent)
        }

        newExerciseFromRoutineButton.setOnClickListener {
            Toast.makeText(this, "newExerciseFromRoutineButton", Toast.LENGTH_SHORT).show()
            //TODO Implement
            newExerciseFABClicked()

        }

        viewOnlineExercisesButton.setOnClickListener {
            newExerciseFABClicked()
            viewOnlineExercisesButtonClicked()

        }

        toolbar.setOnClickListener{
            intent = Intent(this, EditRoutineActivity::class.java)
//            var deleteButton:Button = findViewById(R.id.btn_delete)
//            deleteButton.visibility = View.VISIBLE
//            deleteButton.isClickable = true
//            deleteButton.focusable = View.FOCUSABLE
            startActivity(intent)
        }
        setupRecyclerView()
    }

    val itemTouchHelperCallback = object :
        ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0
//            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val fromPosition = viewHolder.bindingAdapterPosition
            val toPosition = target.bindingAdapterPosition
            val exerciseList = (recyclerView.adapter as ExerciseRecyclerViewAdapter).exerciseList!!

            exerciseList[fromPosition].position = toPosition
            if (fromPosition < toPosition) {
                for (i in fromPosition until toPosition) {
                    exerciseList[i + 1].position = i
                }
            } else {
                for (i in fromPosition downTo toPosition + 1) {
                    exerciseList[i - 1].position = i
                }
            }

            recyclerView.adapter?.notifyItemMoved(fromPosition, toPosition)
            return true
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            (recyclerView.adapter as ExerciseRecyclerViewAdapter).saveList()
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
            //required override, not used
        }
    }

    private fun viewOnlineExercisesButtonClicked() {
        if (session.userIsLoggedIn())
            session.toggleAndGetFirebaseMode()
        else
            startActivity(Intent(this@ExerciseListActivity, LoginActivity::class.java))
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.item_parent_list_exercise)
        val adapter = ExerciseRecyclerViewAdapter(this)
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        val mDividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            (recyclerView.layoutManager as LinearLayoutManager).orientation
        )
        recyclerView.addItemDecoration(mDividerItemDecoration)

        if (session.firebaseMode) {
           // toolbar.title = "Viewing Online Exercises"
            viewOnlineExercisesButton.setImageResource(R.drawable.ic_baseline_system_update_24)
        } else {
//            toolbar.title = "Personal Exercises"
            viewOnlineExercisesButton.setImageResource(R.drawable.ic_baseline_cloud_download_24)

            if (session.activeRoutine == null) {
                finish()
                finishAffinity()
            }
        }
        val exerciseList: LiveData<List<Exercise>>? = session.getProperExercises()
        exerciseList!!.observe(this, { exercises ->
            exercises?.let { adapter.setList(it) }
        })

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    class ExerciseRecyclerViewAdapter(private val parentActivity: ExerciseListActivity) :
        ListAdapter<Exercise, ExerciseRecyclerViewAdapter.ExerciseViewHolder>(ExerciseComparator()) {

        var exerciseList: List<Exercise>? = null

        private val onClickListener: View.OnClickListener = View.OnClickListener { v ->
            val exercise = v.tag as Exercise

            parentActivity.session.activeExercise = exercise
            val intent = Intent(v.context, EditExerciseActivity::class.java)
            v.context.startActivity(intent)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_exercise, parent, false)
            return ExerciseViewHolder(view)
        }

        override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
            val exercise = getItem(position)
            holder.exerciseName.text = exercise.name

            val details: LinkedHashMap<String, Double> = exercise.details

            if (details.isNotEmpty()) {
                holder.exerciseDetail.text = details.entries.elementAt(0).key
                holder.exerciseDetailValue.text =
                    details.entries.elementAt(0).value.toString()
            }

            with(holder.itemView) {
                tag = exercise
                setOnClickListener(onClickListener)
            }
        }

        fun setList(it: List<Exercise>) {
            submitList(it)
            this.exerciseList = it.toList()
        }

        fun saveList() {
            parentActivity.session.updateExercises(exerciseList!!)
        }

        inner class ExerciseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val exerciseName: TextView = view.findViewById(R.id.exercise_name)
            val exerciseDetail: TextView = view.findViewById(R.id.exercise_detail)
            val exerciseDetailValue: TextView = view.findViewById(R.id.exercise_detailValue)

            //no clue what this is, feel free to use it
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
                return oldItem.id == newItem.id && oldItem.routineId == newItem.routineId
            }
        }
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
            newExerciseFromRoutineButton.visibility = View.VISIBLE

            if (!session.firebaseMode) {
                viewOnlineExercisesButton.visibility = View.VISIBLE
                newExerciseButton.visibility = View.VISIBLE
            }
        } else {
            newExerciseFromRoutineButton.visibility = View.INVISIBLE

            if (!session.firebaseMode) {
                viewOnlineExercisesButton.visibility = View.INVISIBLE
                newExerciseButton.visibility = View.INVISIBLE
            }
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            newExerciseFromRoutineButton.startAnimation(fromBott)
            newExerciseFAB.startAnimation(rotateOpen)
            if (!session.firebaseMode) {
                newExerciseButton.startAnimation(fromBott)
                viewOnlineExercisesButton.startAnimation(fromBott)
            }
        } else {
            newExerciseFromRoutineButton.startAnimation(toBott)
            newExerciseFAB.startAnimation(rotateClose)
            if (!session.firebaseMode) {
                viewOnlineExercisesButton.startAnimation(toBott)
                newExerciseButton.startAnimation(toBott)
            }
        }
    }

    private fun setClickable(clicked: Boolean) {
        newExerciseFromRoutineButton.isClickable = clicked

        if (!session.firebaseMode) {
            newExerciseButton.isClickable = clicked
            viewOnlineExercisesButton.isClickable = clicked
        }
    }

    override fun notify(mode: Boolean) {
        setupRecyclerView()
    }
}