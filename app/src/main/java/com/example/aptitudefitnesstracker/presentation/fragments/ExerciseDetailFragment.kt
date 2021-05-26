package com.example.aptitudefitnesstracker.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.aptitudefitnesstracker.R
import com.google.android.material.appbar.CollapsingToolbarLayout

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [RoutineListActivity]
 * in two-pane mode (on tablets) or a [ExerciseDetailActivity]
 * on handsets.
 */
class ExerciseDetailFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                activity?.findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout)?.title =
                    "Item Detail ;D"
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.item_detail, container, false)

        // Show the dummy content as text in a TextView.
//        item?.let {
//            rootView.findViewById<TextView>(R.id.item_detail).text = it.details
//        }

        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }
}