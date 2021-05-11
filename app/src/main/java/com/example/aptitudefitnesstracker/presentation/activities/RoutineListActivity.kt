package com.example.aptitudefitnesstracker.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aptitudefitnesstracker.R
import com.example.aptitudefitnesstracker.dummy.DummyContent
import com.example.aptitudefitnesstracker.persistence.local.RoutineEntity
import com.example.aptitudefitnesstracker.presentation.Presenter
import com.example.aptitudefitnesstracker.presentation.fragments.ExerciseDetailFragment
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

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
            val intent = Intent(this@RoutineListActivity, DatabaseTestActivity::class.java)
            startActivity(intent)
        }

        if (findViewById<NestedScrollView>(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        setupRecyclerView(findViewById(R.id.item_list))
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        val adapter = RoutineRecyclerViewAdapter(this, twoPane)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        presenter.routineList.observe(this, Observer { routines ->
            routines?.let { adapter.submitList(it) }
        })
    }

    class RoutineRecyclerViewAdapter(
        private val parentActivity: RoutineListActivity,
        private val twoPane: Boolean
    ) :
        ListAdapter<RoutineEntity, RoutineRecyclerViewAdapter.RoutineViewHolder>(RoutineComparator()) { //todo change this to Routine once RoutineEntity translator is in

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as DummyContent.DummyItem
                if (twoPane) {
                    val fragment = ExerciseDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(ExerciseDetailFragment.ARG_ITEM_ID, item.id)
                        }
                    }
                    parentActivity.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit()
                } else {
                    val intent = Intent(v.context, ExerciseDetailActivity::class.java).apply {
                        putExtra(ExerciseDetailFragment.ARG_ITEM_ID, item.id)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_content, parent, false)
            return RoutineViewHolder(view)
        }

        override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
            val item = getItem(position)
            holder.bind("id: " + item.id + " name: " + item.name)
        }

        inner class RoutineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val idView: TextView = view.findViewById(R.id.id_text)
            val contentView: TextView =
                view.findViewById(R.id.content) //no clue what this is, feel free to use it

            fun bind(text: String?) {
                idView.text = text
            }
        }

        class RoutineComparator : DiffUtil.ItemCallback<RoutineEntity>() {
            override fun areItemsTheSame(oldItem: RoutineEntity, newItem: RoutineEntity): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: RoutineEntity,
                newItem: RoutineEntity
            ): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }
}