package com.example.aptitudefitnesstracker.presentation.activities

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import com.example.aptitudefitnesstracker.R
import com.example.aptitudefitnesstracker.application.Exercise
import com.example.aptitudefitnesstracker.application.Session
import com.example.aptitudefitnesstracker.presentation.fragments.ExerciseDetailFragment
import com.example.aptitudefitnesstracker.presentation.ThemeUtils
import com.google.android.material.appbar.CollapsingToolbarLayout

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [RoutineListActivity].
 */
class ExerciseDetailActivity : AppCompatActivity() {
    private val session: Session by lazy { application as Session }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.setThemeApp(this)
        ThemeUtils.setAppFont(this)
        ThemeUtils.setAppFontFamily(this)
        setContentView(R.layout.activity_item_detail)
        setSupportActionBar(findViewById(R.id.detail_toolbar))

        var exercise: Exercise = session.activeExercise!!



        findViewById<FloatingActionButton>(R.id.editExerciseFAB).setOnClickListener { view ->
//            var exercise = Exercise()

            intent = Intent(this, EditExerciseActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "editExerciseFAB", Toast.LENGTH_SHORT).show()

        }

        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout)?.title = exercise.name
        println(exercise.name)


        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don"t need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
//        if (savedInstanceState == null) {
//            // Create the detail fragment and add it to the activity
//            // using a fragment transaction.
//            val fragment = ExerciseDetailFragment().apply {
//                arguments = Bundle().apply {
//                    putString(
//                        ExerciseDetailFragment.ARG_ITEM_ID,
//                        intent.getStringExtra(ExerciseDetailFragment.ARG_ITEM_ID),
//                    )
//                }
//            }
//
//            supportFragmentManager.beginTransaction()
//                .add(R.id.item_detail_container, fragment)
//                .commit()
//        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back

                navigateUpTo(Intent(this, RoutineListActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}