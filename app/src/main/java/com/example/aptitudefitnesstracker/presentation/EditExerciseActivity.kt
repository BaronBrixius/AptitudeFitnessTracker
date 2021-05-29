package com.example.aptitudefitnesstracker.presentation

import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.example.aptitudefitnesstracker.R
import com.example.aptitudefitnesstracker.application.Exercise
import com.example.aptitudefitnesstracker.application.Session

class EditExerciseActivity : AppCompatActivity() {
    private var inputDetailsLayout: RecyclerView? = null
    private var inputName: EditText? = null
    private var inputNotes: EditText? = null
    private var inputDetails: EditText? = null
    private var inputDetailsValue: EditText? = null
    private var btnSave: Button? = null
    private var btnDelete: Button? = null
    private var userId: String? = null

    private val session: Session by lazy { application as Session }
    private lateinit var exercise: Exercise

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.setThemeApp(this)
        ThemeUtils.setAppFont(this)
        ThemeUtils.setAppFontFamily(this)
        setContentView(R.layout.activity_add_exercise)
        inputName = findViewById<View>(R.id.name) as EditText
        inputDetailsLayout = findViewById(R.id.detail_list)
        inputDetails = findViewById<EditText>(R.id.detail_name)
        inputDetailsValue = findViewById<EditText>(R.id.detail_value)
        inputNotes = findViewById<View>(R.id.Notes) as EditText
        btnSave = findViewById<View>(R.id.btn_save) as Button
        btnDelete = findViewById<View>(R.id.btn_delete) as Button


        findViewById<Toolbar>(R.id.toolbar).title = "Edit Exercise"

        exercise = session.activeExercise!!
        inputName!!.hint = exercise.name
//        inputDetails!!.hint = exercise.details[]
//        inputDetailsValue!!.hint = exercise.details[]
        inputNotes!!.hint = exercise.notes
        // Save / update the exercise
        btnSave!!.text = "Save"
        btnSave!!.setOnClickListener {
            val name = inputName!!.text.toString()
            val notes = inputNotes!!.text.toString()
//            val details = inputDetails!!.text.toString()
//            val detailsValue = inputDetailsValue!!.text.toString()

            exercise.name = name
            exercise.notes = notes
//            exercise.details[details] = detailsValue.toDouble()

            /**
             * Pop up dialog to confirm saving changes
             */
            val saveDialog = AlertDialog.Builder(this)
            saveDialog.setTitle("Confirm Changes")
            saveDialog.setMessage("Are you sure you would like to save changes?")

            saveDialog.setPositiveButton("Save") { dialog, which ->
                session.updateExercise(exercise)
                finish()
            }
            saveDialog.setNegativeButton(android.R.string.no) { dialog, which ->
                finish()
            }
            saveDialog.show()
        }


        btnDelete!!.setOnClickListener {
            val saveDialog = AlertDialog.Builder(this)
            saveDialog.setTitle("Delete Exercise")
            saveDialog.setMessage("Are you sure?")

            saveDialog.setPositiveButton("Delete") { dialog, which ->
                session.deleteExercise(exercise)
                finish()
            }
            saveDialog.setNegativeButton(android.R.string.no) { dialog, which ->
                finish()
            }
            saveDialog.show()
        }
        setupRecyclerView()
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