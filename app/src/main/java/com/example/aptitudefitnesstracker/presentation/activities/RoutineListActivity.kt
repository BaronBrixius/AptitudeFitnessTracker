package com.example.aptitudefitnesstracker.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aptitudefitnesstracker.R
import com.example.aptitudefitnesstracker.application.Routine
import com.example.aptitudefitnesstracker.presentation.ThemeUtils
import com.example.aptitudefitnesstracker.presentation.Presenter
import com.google.android.material.floatingactionbutton.FloatingActionButton


/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ExerciseDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class RoutineListActivity : AppCompatActivity() {
    private val presenter: Presenter by lazy { application as Presenter }

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
        findViewById<FloatingActionButton>(R.id.newExerciseFAB).setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
            presenter.addNewRoutineButtonPressed()
        }

        //Click "account settings" button to go to account settings (AccountActivity)
        findViewById<Button>(R.id.AccountSettings).setOnClickListener { view ->
            presenter.accountSettingButton()
        }

        setupRecyclerView()
    }

    fun toggleDownloadMode() {
        presenter.session.firebaseMode = !presenter.session.firebaseMode
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.item_list)
        val adapter = RoutineRecyclerViewAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val routineList: LiveData<List<Routine>> = if (presenter.session.firebaseMode) presenter.session.repository.downloadRemoteRoutines() else presenter.routineList
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

            parentActivity.presenter.session.activeRoutine = item
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
            R.id.actionSetting -> {
                startActivity(Intent(this@RoutineListActivity, SettingsActivity::class.java))
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
}