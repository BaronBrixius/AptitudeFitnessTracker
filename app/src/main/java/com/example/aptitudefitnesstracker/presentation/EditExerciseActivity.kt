package com.example.aptitudefitnesstracker.presentation

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.ListAdapter
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
//            exercise.details[details] = detailsValue.toDouble()

            /**
             * Pop up dialog to confirm saving changes
             */
            val saveDialog = AlertDialog.Builder(this)
            saveDialog.setTitle("Confirm Changes")
            saveDialog.setMessage("Are you sure you would like to save changes?")

            saveDialog.setPositiveButton("Save") { dialog, which ->
                if (inputName!!.text.toString() != "")
                    exercise.name = name

                if (inputNotes!!.text.toString() != "")
                    exercise.notes = notes

                session.updateExercise(exercise)
                finish()
            }
            saveDialog.setNegativeButton(android.R.string.no) { dialog, which ->
                finish()
            }
            saveDialog.show()
        }


        btnDelete!!.setOnClickListener {
            val deleteDialog = AlertDialog.Builder(this)
            deleteDialog.setTitle("Delete Exercise")
            deleteDialog.setMessage("Are you sure?")

            deleteDialog.setPositiveButton("Delete") { dialog, which ->
                session.deleteExercise(exercise)
                finish()
            }
            deleteDialog.setNegativeButton(android.R.string.no) { dialog, which ->
                finish()
            }
            deleteDialog.show()
        }
        setupRecyclerView()
    }


    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.detail_list)
        val adapter = ExerciseDetailsRecyclerViewAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter.submitList(exercise.details.entries.toList())
    }

    val itemTouchHelperCallback = object :
        ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onMoved(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            fromPos: Int,
            target: RecyclerView.ViewHolder,
            toPos: Int,
            x: Int,
            y: Int
        ) {
            super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
            println(
                viewHolder.absoluteAdapterPosition
            )
        }

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