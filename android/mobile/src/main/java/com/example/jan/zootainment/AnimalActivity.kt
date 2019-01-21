package com.example.jan.zootainment

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.Log
import com.example.jan.zootainment.adapter.DeviceContentAdapter
import com.example.jan.zootainment.adapter.FragmentTabAdapter
import com.example.jan.zootainment.data.DeviceData
import com.example.jan.zootainment.fragments.AnimalFragmentTabOverview
import com.example.jan.zootainment.fragments.AnimalFragmentTabQuiz
import com.example.jan.zootainment.util.ProximityContentUtils
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_animal.*

class AnimalActivity : FragmentActivity() {

    private lateinit var fragmentTabAdapter: FragmentTabAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var deviceList: ArrayList<DeviceData>
    private lateinit var overview: AnimalFragmentTabOverview
    private lateinit var deviceContentAdapter: DeviceContentAdapter

    private val firebase = FirebaseDatabase.getInstance().reference

    private lateinit var animalDataRef: DatabaseReference

    private lateinit var animal: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal)

        //get data from previous activity
        animal = intent.getStringExtra("animal")
        val questionProg = intent.getIntExtra("progress", 0)
        Log.d(TAG, "progress:" + questionProg.toString())

        //load view with intent data
        animal_name.text = animal
        loadAnimalPicture(animal)
        animal_background.setBackgroundColor(ContextCompat.getColor(this, ProximityContentUtils.getColor(animal)))

        animalDataRef = firebase.child(animal).child("data")

        val quiz = AnimalFragmentTabQuiz()
        overview = AnimalFragmentTabOverview()

        deviceList = ArrayList()

        fragmentTabAdapter = FragmentTabAdapter(supportFragmentManager)
        fragmentTabAdapter.addFragment(overview, "Overview")
        fragmentTabAdapter.addFragment(quiz, "Quiz")

        viewPager = findViewById(R.id.animal_viewpager)
        viewPager.adapter = fragmentTabAdapter

        tabLayout = findViewById(R.id.animal_tab)
        tabLayout.setupWithViewPager(viewPager)

        animalDataRef.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    //create bundle for fragments with intent data
                    val quizBundle = Bundle()

                    quizBundle.putString("animal", animal)
                    quizBundle.putInt("progress", questionProg)
                    quizBundle.putInt("questions", 6)
                    quiz.putQuizArguments(quizBundle)

                    //deviceList[dataSnapshot.childrenCount.toInt()]
                    for (data in dataSnapshot.children) {

                        val device = data.getValue(DeviceData::class.java)
                        deviceList.add(device!!)
                    }
                    overview.changeDeviceList(deviceList, animal)
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

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart:")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume:")
        if (deviceList.size != 0) {
            Log.d(TAG, "onResume:list not null")
            overview.changeDeviceList(deviceList, animal)
        }
    }

    companion object {
        private const val TAG = "AnimalActivity"
    }
}