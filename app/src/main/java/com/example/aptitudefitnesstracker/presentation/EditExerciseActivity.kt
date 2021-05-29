package com.example.aptitudefitnesstracker.presentation

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aptitudefitnesstracker.R
import com.example.aptitudefitnesstracker.application.Exercise
import com.example.aptitudefitnesstracker.application.Session

class EditExerciseActivity : AppCompatActivity() {
    private var inputDetails: RecyclerView? = null
    private var inputName: EditText? = null
    private var inputNotes: EditText? = null
    private var btnSave: Button? = null
    private var userId: String? = null

    private val session: Session by lazy { application as Session }
    private lateinit var exercise: Exercise

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.setThemeApp(this)
        ThemeUtils.setAppFont(this)
        ThemeUtils.setAppFontFamily(this)
        setContentView(R.layout.activity_add_exercise)
        // Displaying toolbar icon
//        supportActionBar!!.setDisplayShowHomeEnabled(true)
//        supportActionBar!!.setIcon(R.mipmap.ic_launcher)
        inputName = findViewById<View>(R.id.name) as EditText
        inputDetails = findViewById(R.id.detail_list)
        inputNotes = findViewById<View>(R.id.Notes) as EditText
        btnSave = findViewById<View>(R.id.btn_save) as Button
        findViewById<Toolbar>(R.id.toolbar).title = "Edit Exercise"

        exercise = session.activeExercise!!
        inputName!!.hint = exercise.name


        // Save / update the exercise
        btnSave!!.setOnClickListener {
            val name = inputName!!.text.toString()
            val notes = inputNotes!!.text.toString()

            exercise.name = name
            exercise.notes = notes

        }
        toggleButton()

        setupRecyclerView()

    }

    // Changing button text
    private fun toggleButton() {
        if (TextUtils.isEmpty(userId)) {
            btnSave!!.text = "Save"
        } else {
            btnSave!!.text = "Update"
        }
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.detail_list)
        val adapter = ExerciseDetailsRecyclerViewAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

//        val exerciseList: LiveData<List<Exercise>>? = session.getProperExercises()    //todo updates
//        exerciseList!!.observe(this, { exercises ->
//            exercises?.let {
//                adapter.submitList(it) }
//        })

        adapter.submitList(exercise.details.entries.toList())

//        setupRecyclerView()
    }

    class ExerciseDetailsRecyclerViewAdapter(
        private val parentActivity: EditExerciseActivity
    ) :
        ListAdapter<Map.Entry<String, Double>, ExerciseDetailsRecyclerViewAdapter.ExerciseDetailsViewHolder>(
            ExerciseDetailComparator()
        ) {
        private val onClickListener: View.OnClickListener = View.OnClickListener { v ->
            val detail = v.tag as Map.Entry<String, Double>

//            parentActivity.session.activeExercise = exercise
//            val intent = Intent(v.context, EditExerciseActivity::class.java)
//            v.context.startActivity(intent)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ExerciseDetailsViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.details_content, parent, false)
            return ExerciseDetailsViewHolder(view)
        }

        override fun onBindViewHolder(holder: ExerciseDetailsViewHolder, position: Int) {
            val detail = getItem(position)
            holder.name.text = detail.key
            holder.value.text = detail.value.toString()

//            with(holder.itemView) {
//                tag = detail
//                setOnClickListener(onClickListener)
//            }
        }

        inner class ExerciseDetailsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val name: TextView = view.findViewById(R.id.detail_name)
            val value: TextView = view.findViewById(R.id.detail_value)
        }

        class ExerciseDetailComparator : DiffUtil.ItemCallback<Map.Entry<String, Double>>() {
            override fun areItemsTheSame(
                oldItem: Map.Entry<String, Double>,
                newItem: Map.Entry<String, Double>
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: Map.Entry<String, Double>,
                newItem: Map.Entry<String, Double>
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}