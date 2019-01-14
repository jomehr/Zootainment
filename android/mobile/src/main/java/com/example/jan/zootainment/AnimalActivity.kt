package com.example.jan.zootainment

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.Log
import com.example.jan.zootainment.adapter.FragmentTabAdapter
import com.example.jan.zootainment.data.OverviewData
import com.example.jan.zootainment.fragments.AnimalFragmentTabOverview
import com.example.jan.zootainment.fragments.AnimalFragmentTabQuiz
import com.example.jan.zootainment.fragments.PointCounter
import com.example.jan.zootainment.util.ProximityContentUtils
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_animal.*

class AnimalActivity : FragmentActivity(), AnimalFragmentTabOverview.OnFragmentInteractionListener, PointCounter.OnPointsCounterListener {

    override fun messageFromCounter(count: Int) {
        Log.d(TAG, "received communication from counter fragment: $count")
    }

    override fun messageFromParent() {
        Log.d(TAG, "received communication from parent fragment")
    }

    private lateinit var fragmentTabAdapter: FragmentTabAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    private val firebase = FirebaseDatabase.getInstance().reference

    private lateinit var animalDataRef: DatabaseReference

    private lateinit var animal: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal)

        //get data from previous activity
        animal = intent.getStringExtra("animal")

        //load view with intent data
        animal_name.text = animal
        loadAnimalPicture(animal)
        animal_background.setBackgroundColor(ContextCompat.getColor(this, ProximityContentUtils.getColor(animal)))

        //create bundle for fragments with intent data
        val quizBundle = Bundle()
        val overviewBundle = Bundle()
        quizBundle.putString("animal", animal)
        overviewBundle.putString("animal", animal)

        animalDataRef = firebase.child(animal).child("data")


        animalDataRef.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val data = dataSnapshot.getValue(OverviewData::class.java)

                    overviewBundle.putString("limit", data?.limit)
                    overviewBundle.putInt("cost", data?.cost!!)
                    quizBundle.putInt("questions", data.questions)

                    val quiz = AnimalFragmentTabQuiz()
                    val overview = AnimalFragmentTabOverview()

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
                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "loadQuestion:onCancelled", error.toException())
                }
            }
        )
    }

    private fun loadAnimalPicture(animal: String) {
        when (animal) {
            "elephants" -> animal_image.setImageDrawable(getDrawable(R.drawable.ic_icon_elephant))
            "baboons" -> animal_image.setImageDrawable(getDrawable(R.drawable.ic_icon_monkey))
            "giraffes" -> animal_image.setImageDrawable(getDrawable(R.drawable.ic_icon_giraffe))

        }
    }

    companion object {
        private const val TAG = "AnimalActivity"
    }
}