package com.example.aptitudefitnesstracker.presentation

import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.shapes.Shape
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
import com.example.aptitudefitnesstracker.application.Routine
import com.example.aptitudefitnesstracker.application.Session
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class RoutineListActivity : AppCompatActivity(), IFirebaseModeObserver {
    private val session: Session by lazy {
       val session = application as Session
        session.addObserver(this)
        session
    }

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

        newRoutineButton.setOnClickListener{
            val intent = Intent(this, AddRoutineActivity::class.java )
            startActivity(intent)
        }

        viewOnlineRoutinesButton.setOnClickListener{
            viewOnlineRoutinesButtonClicked()
        }

        setupRecyclerView()

        if (session.userIsLoggedIn()){
            println("USER LOGGED IN")
        }
        else{
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
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val toolbar: Toolbar = findViewById(R.id.toolbar)

        if (session.firebaseMode) {
            toolbar.title = "Viewing Online Routines"
            viewOnlineRoutinesButton.setImageResource(R.drawable.ic_baseline_system_update_24)
        }
        else {
            toolbar.title = "Personal Routines"
            viewOnlineRoutinesButton.setImageResource(R.drawable.ic_baseline_cloud_download_24)
        }

        val routineList: LiveData<List<Routine>> = session.getProperRoutines()
        routineList.observe(this, { routines ->
            routines?.let { adapter.setList(it) }
        })

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    val itemTouchHelperCallback = object :
        ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val fromPosition = viewHolder.bindingAdapterPosition
            val toPosition = target.bindingAdapterPosition
            val routineList = (recyclerView.adapter as RoutineRecyclerViewAdapter).routineList!!

            routineList[fromPosition].position = toPosition
            if (fromPosition < toPosition) {
                for (i in fromPosition until toPosition) {
                    routineList[i + 1].position = i
                }
            } else {
                for (i in fromPosition downTo toPosition + 1) {
                    routineList[i - 1].position = i
                }
            }

            recyclerView.adapter?.notifyItemMoved(fromPosition, toPosition)
            return true
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            (recyclerView.adapter as RoutineRecyclerViewAdapter).saveList()
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
            //required override, not used
        }
    }

    class RoutineRecyclerViewAdapter(
        private val parentActivity: RoutineListActivity
    ) :
        ListAdapter<Routine, RoutineRecyclerViewAdapter.RoutineViewHolder>(RoutineComparator()) {

        var routineList: List<Routine>? = null

        private val onClickListener: View.OnClickListener = View.OnClickListener { v ->
            val item = v.tag as Routine

            parentActivity.session.activeRoutine = item
            val intent = Intent(v.context, ExerciseListActivity::class.java)
            v.context.startActivity(intent)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_routine, parent, false)
            return RoutineViewHolder(view)
        }

        override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
            val routine = getItem(position)
            holder.contentView.text = routine.name

            with(holder.itemView) {
                tag = routine
                setOnClickListener(onClickListener)
            }
        }

        fun setList(it: List<Routine>) {
            submitList(it)
            this.routineList = it.toList()
        }

        fun saveList() {
            parentActivity.session.updateRoutines(routineList!!)
        }

        inner class RoutineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//            val idView: TextView = view.findViewById(R.id.id_text)
            val contentView: TextView = view.findViewById(R.id.content)
            var circle: View? = view.findViewById(R.id.circle)

        //no clue what this is, feel free to use it


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
                return oldItem.id == newItem.id
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
    private var clicked = false

    override fun notify(mode: Boolean) {
        setupRecyclerView()
    }

}