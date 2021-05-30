package com.example.aptitudefitnesstracker.presentation.exercises

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aptitudefitnesstracker.R
import com.example.aptitudefitnesstracker.application.data.Exercise
import java.util.*

class ExerciseDetailsRecyclerViewAdapter(private val parentActivity: EditExerciseActivity) :
    ListAdapter<Exercise.Detail, ExerciseDetailsRecyclerViewAdapter.ExerciseDetailsViewHolder>(
        ExerciseDetailComparator()
    ) {
    var detailList: ArrayList<Exercise.Detail>? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

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

        holder.name.doAfterTextChanged { text -> detail.key = text.toString() }
        holder.value.doAfterTextChanged { text ->
            val str: String = text.toString()
            detail.value = if (str.isEmpty()) 0.0 else str.toDouble() }

        if (parentActivity.session.firebaseMode) {
            holder.btnDeleteDetail.visibility = View.GONE
        }

        holder.btnDeleteDetail.setOnClickListener {
            val saveDialog = AlertDialog.Builder(parentActivity)
            saveDialog.setTitle("Delete Detail?")
            saveDialog.setPositiveButton("DELETE") { _, _ ->
                detailList!!.remove(detail)
                setList(detailList!!)
                notifyDataSetChanged()
            }
            saveDialog.setNegativeButton(android.R.string.no) { _, _ ->
            }

        }
    }

    fun setList(detailList: ArrayList<Exercise.Detail>) {
        submitList(detailList)
        this.detailList = detailList
    }

    inner class ExerciseDetailsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: EditText = view.findViewById(R.id.detail_name)
        val value: EditText = view.findViewById(R.id.detail_value)
        val btnDeleteDetail: Button = view.findViewById(R.id.delete_Detail)
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

    private val itemTouchHelperCallback = object :
        ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val fromPosition = viewHolder.bindingAdapterPosition
            val toPosition = target.bindingAdapterPosition
            val detailList =
                (recyclerView.adapter as ExerciseDetailsRecyclerViewAdapter).detailList!!

            if (fromPosition < toPosition) {
                for (i in fromPosition until toPosition) {
                    Collections.swap(detailList, i, i + 1)
                }
            } else {
                for (i in fromPosition downTo toPosition + 1) {
                    Collections.swap(detailList, i, i - 1)
                }
            }

            recyclerView.adapter?.notifyItemMoved(fromPosition, toPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
            //required override, not used
        }
    }
}
