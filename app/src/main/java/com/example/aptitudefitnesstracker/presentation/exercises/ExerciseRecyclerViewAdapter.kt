package com.example.aptitudefitnesstracker.presentation.exercises

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aptitudefitnesstracker.R
import com.example.aptitudefitnesstracker.application.data.Exercise

class ExerciseRecyclerViewAdapter(private val parentActivity: ExerciseListActivity) :
    ListAdapter<Exercise, ExerciseRecyclerViewAdapter.ExerciseViewHolder>(ExerciseComparator()) {

    var exerciseList: List<Exercise>? = null

    private val onClickListener: View.OnClickListener = View.OnClickListener { v ->
        val exercise = v.tag as Exercise

        parentActivity.session.activeExercise = exercise
        val intent = Intent(v.context, EditExerciseActivity::class.java)
        v.context.startActivity(intent)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_exercise, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = getItem(position)
        holder.exerciseName.text = exercise.name
        holder.circle.text = exercise.name.first().toString()

        val details: ArrayList<Exercise.Detail> = exercise.details

        if (details.isNotEmpty()) {
            holder.exerciseDetail.text = details.elementAt(0).key
            holder.exerciseDetailValue.text =
                details.elementAt(0).value.toString()
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
        var circle: TextView = view.findViewById(R.id.exercise_first_letter)
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

    private val itemTouchHelperCallback = object :
        ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {

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
}