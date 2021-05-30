package com.example.aptitudefitnesstracker.presentation.exercises

import android.os.Bundle
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
import com.example.aptitudefitnesstracker.application.data.Exercise
import com.example.aptitudefitnesstracker.application.Session
import com.example.aptitudefitnesstracker.presentation.settings.ThemeUtils
import java.util.*
import kotlin.collections.LinkedHashMap
import kotlin.random.Random

class EditExerciseActivity : AppCompatActivity() {
    private var inputDetailsLayout: RecyclerView? = null
    private var inputName: EditText? = null
    private var inputNotes: EditText? = null
    private var inputDetails: EditText? = null
    private var inputDetailsValue: EditText? = null
    private var btnSave: Button? = null
    private var btnDelete: Button? = null
    private var btnAddDetail: Button? = null

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
        inputDetails = findViewById(R.id.detail_name)
        inputDetailsValue = findViewById(R.id.detail_value)
        inputNotes = findViewById<View>(R.id.Notes) as EditText
        btnSave = findViewById<View>(R.id.btn_save) as Button
        btnDelete = findViewById<View>(R.id.btn_delete) as Button
        btnAddDetail = findViewById<View>(R.id.btn_add_detail) as Button

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

            saveDialog.setPositiveButton("Save") { _, _ ->
                if (inputName!!.text.toString() != "") {
                    exercise.name = name
                }

                exercise.details = adapter.detailList!!

                if (inputNotes!!.text.toString() != "") {
                    exercise.notes = notes
                }

                session.updateExercise(exercise)
                finish()
            }
            saveDialog.setNegativeButton(android.R.string.no) { _, _ ->
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

        btnAddDetail!!.setOnClickListener{
//            exercise.details[Random.nextInt().toString()] = Random.nextDouble(0.0,10.0)
            exercise.details.add(Exercise.Detail())
            session.updateExercise(exercise)
            setupRecyclerView()
        }

        setupRecyclerView()
    }

    private lateinit var adapter : ExerciseDetailsRecyclerViewAdapter

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.detail_list)
        adapter = ExerciseDetailsRecyclerViewAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter.setList(exercise.details)

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private val itemTouchHelperCallback = object :
        ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val fromPosition = viewHolder.bindingAdapterPosition
            val toPosition = target.bindingAdapterPosition
            val detailList = (recyclerView.adapter as ExerciseDetailsRecyclerViewAdapter).detailList!!

            if (fromPosition < toPosition) {
                for (i in fromPosition until toPosition) {
                    Collections.swap(detailList, i, i+1)
                }
            } else {
                for (i in fromPosition downTo toPosition + 1) {
                    Collections.swap(detailList, i, i-1)
                }
            }

            recyclerView.adapter?.notifyItemMoved(fromPosition, toPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
            //required override, not used
        }
    }

    class ExerciseDetailsRecyclerViewAdapter(private val parentActivity: EditExerciseActivity) :
        ListAdapter<Exercise.Detail, ExerciseDetailsRecyclerViewAdapter.ExerciseDetailsViewHolder>(
            ExerciseDetailComparator()
        ) {
        var detailList: ArrayList<Exercise.Detail>? = null

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ExerciseDetailsViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_exercise_details_content, parent, false)
            return ExerciseDetailsViewHolder(view)
        }

        override fun onBindViewHolder(holder: ExerciseDetailsViewHolder, position: Int) {
            val detail = getItem(position)
            holder.name.setText(detail.key)
            holder.value.setText(detail.value.toString())

            holder.btnDeleteDetail.setOnClickListener{
//                btnDeleteDetail!!.startAnimation(scaleUp)

                val saveDialog = AlertDialog.Builder(parentActivity)
                saveDialog.setTitle("Delete Detail?")
//                saveDialog.setMessage("Are you sure you would like to delete the detail?")

                saveDialog.setPositiveButton("DELETE") { dialog, which ->
                    Toast.makeText(parentActivity, "DELETE", Toast.LENGTH_SHORT).show()
                }
                saveDialog.setNegativeButton(android.R.string.no) { _, _ ->
                }
                saveDialog.show()
            }

//            with(holder.itemView) {
//                tag = detail
//                setOnClickListener(onClickListener)
//            }
        }

        fun setList(detailList: ArrayList<Exercise.Detail>) {
            submitList(detailList)
            this.detailList = detailList
        }

        inner class ExerciseDetailsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val name: EditText = view.findViewById(R.id.detail_name)
            val value: EditText = view.findViewById(R.id.detail_value)
            val btnDeleteDetail:Button = view.findViewById(R.id.delete_Detail)
        }

        class ExerciseDetailComparator : DiffUtil.ItemCallback<Exercise.Detail>() {
            override fun areItemsTheSame(
                oldItem: Exercise.Detail,
                newItem: Exercise.Detail
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: Exercise.Detail,
                newItem: Exercise.Detail
            ): Boolean {
                return oldItem.key == newItem.key && oldItem.value == newItem.value
            }
        }
    }
}