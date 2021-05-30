package com.example.aptitudefitnesstracker.presentation.routines

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
import com.example.aptitudefitnesstracker.application.data.Routine
import com.example.aptitudefitnesstracker.presentation.exercises.ExerciseListActivity

class RoutineRecyclerViewAdapter(private val parentActivity: RoutineListActivity) :
    ListAdapter<Routine, RoutineRecyclerViewAdapter.RoutineViewHolder>(RoutineComparator()) {
    var routineList: List<Routine>? = null

    private val onClickListener: View.OnClickListener = View.OnClickListener { v ->
        parentActivity.session.activeRoutine = v.tag as Routine
        val intent = Intent(v.context, ExerciseListActivity::class.java)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_routine, parent, false)
        return RoutineViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
        val routine = getItem(position)
        holder.contentView.text = routine.name


        holder.circle.text = routine.name.first().toString()


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
        val contentView: TextView = view.findViewById(R.id.content)
        var circle: TextView = view.findViewById(R.id.routine_first_letter)
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

    private val itemTouchHelperCallback = object :
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
}