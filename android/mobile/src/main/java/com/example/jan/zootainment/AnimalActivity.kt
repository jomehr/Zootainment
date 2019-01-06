package com.example.jan.zootainment

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.Log
import com.example.jan.zootainment.adapter.FragmentTabAdapter
import com.example.jan.zootainment.data.OverviewData
import com.example.jan.zootainment.fragments.AnimalFragmentTabOverview
import com.example.jan.zootainment.fragments.AnimalFragmentTabQuiz
import com.example.jan.zootainment.util.ProximityContentUtils
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_animal.*
import kotlinx.android.synthetic.main.content_animal_overview.*
import kotlinx.android.synthetic.main.content_animal_quiz.*

class AnimalActivity : FragmentActivity() {

    private lateinit var fragmentTabAdapter: FragmentTabAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var databaseRef: DatabaseReference

    private lateinit var animal: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal)

        animal = intent.getStringExtra("animal")
        animal_name.text = animal
        loadAnimalPicture(animal)
        animal_background.setBackgroundColor(ContextCompat.getColor(this, ProximityContentUtils.getColor(animal)))

        val quizBundle = Bundle()
        val overviewBundle = Bundle()
        quizBundle.putString("animal", animal)
        overviewBundle.putString("animal", animal)

        databaseRef = FirebaseDatabase.getInstance().reference.child("enclosure_1").child("data")
        databaseRef.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val data = dataSnapshot.getValue(OverviewData::class.java)
                    overviewBundle.putString("limit", data?.limit)
                    overviewBundle.putString("cost", data?.cost)
                    quizBundle.putString("questions", data?.questions)

                    feeding_limit?.text = data?.limit
                    feeding_cost?.text = data?.cost
                    question_total?.text = data?.questions
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "loadQuestion:onCancelled", error.toException())
                }
            }
        )

        val quiz: Fragment = AnimalFragmentTabQuiz()
        val overview: Fragment = AnimalFragmentTabOverview()
        quiz.arguments = quizBundle
        overview.arguments = overviewBundle

        fragmentTabAdapter = FragmentTabAdapter(supportFragmentManager)
        fragmentTabAdapter.addFragment(overview, "Overview")
        fragmentTabAdapter.addFragment(quiz, "Quiz")

        viewPager = findViewById(R.id.animal_viewpager)
        viewPager.adapter = fragmentTabAdapter

        tabLayout = findViewById(R.id.animal_tab)
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun loadAnimalPicture(animal: String) {
        when (animal) {
            "elephant" -> animal_name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.elephant_icon,0, 0, 0)
            //TODO add other animal pictures
        }
    }

    companion object {
        private const val TAG = "AnimalActivity"
    }
}