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
import com.example.aptitudefitnesstracker.application.IFirebaseModeObserver
import com.example.aptitudefitnesstracker.application.Routine
import com.example.aptitudefitnesstracker.application.Session
import com.example.aptitudefitnesstracker.presentation.ThemeUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_exercise_list.*
import kotlinx.android.synthetic.main.activity_exercise_list.downloadExerciseButton
import kotlinx.android.synthetic.main.activity_exercise_list.newExerciseButton
import kotlinx.android.synthetic.main.activity_exercise_list.newExerciseFromRoutineButton
import kotlinx.android.synthetic.main.activity_item_list.*


/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ExerciseDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class RoutineListActivity : AppCompatActivity(), IFirebaseModeObserver {
    private val session: Session by lazy {
       val session = application as Session
        session.addObserver(this)
        session
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.setThemeApp(this) // for set theme
        ThemeUtils.setAppFont(this) // for set font size
        ThemeUtils.setAppFontFamily(this) // for set font family
        setContentView(R.layout.activity_item_list)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title

        //Add new
        findViewById<FloatingActionButton>(R.id.newRoutineFAB).setOnClickListener { view ->
            newRoutineFABClicked()
        }

        findViewById<FloatingActionButton>(R.id.downloadExerciseButton).setOnClickListener { view ->
            downloadButtonClicked()
        }

        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerView()
    }

    private fun downloadButtonClicked() {
        if (session.userIsLoggedIn()) {
            session.toggleFirebaseMode()
        }
        else {
            startActivity(Intent(this@RoutineListActivity, LoginActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.item_list)
        val adapter = RoutineRecyclerViewAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val routineList: LiveData<List<Routine>> = session.getProperRoutines()
        routineList.observe(this, { routines ->
            routines?.let { adapter.submitList(it) }
        })
    }

    class RoutineRecyclerViewAdapter(
        private val parentActivity: RoutineListActivity
    ) :
        ListAdapter<Routine, RoutineRecyclerViewAdapter.RoutineViewHolder>(RoutineComparator()) {

        private val onClickListener: View.OnClickListener = View.OnClickListener { v ->
            val item = v.tag as Routine

            parentActivity.session.activeRoutine = item
            val intent = Intent(v.context, ExerciseListActivity::class.java)
            v.context.startActivity(intent)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_content, parent, false)
            return RoutineViewHolder(view)
        }

        override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
            val routine = getItem(position)
            //holder.bind("id: " + item.id + " name: " + item.name)
            holder.idView.text =
                "id: " + routine.id    //fixme placeholder stuff for database testing
            holder.contentView.text = " name: " + routine.name

            with(holder.itemView) {
                tag = routine
                setOnClickListener(onClickListener)
            }
        }

        inner class RoutineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val idView: TextView = view.findViewById(R.id.id_text)
            val contentView: TextView =
                view.findViewById(R.id.content) //no clue what this is, feel free to use it

            /*fun bind(text: String?) {
                idView.text = text
            }*/

        }

        class RoutineComparator : DiffUtil.ItemCallback<Routine>() {
            override fun areItemsTheSame(oldItem: Routine, newItem: Routine): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: Routine,
                newItem: Routine
            ): Boolean {
                return oldItem.name == newItem.name //todo?
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
                startActivity(Intent(this@RoutineListActivity, SettingsActivity::class.java))
                true
            }
            R.id.AccountSettingsItem -> {
                startActivity(Intent(this@RoutineListActivity, AccountActivity::class.java))
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
            newRoutineFAB.startAnimation(rotateOpen)
        } else {
            newExerciseButton.startAnimation(toBott)
            newExerciseFromRoutineButton.startAnimation(toBott)
            downloadExerciseButton.startAnimation(toBott)
            newRoutineFAB.startAnimation(rotateClose)
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

    override fun notify(mode: Boolean) {
        setupRecyclerView()
    }

}