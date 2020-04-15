package com.example.noforeignlandexam.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.noforeignlandexam.R
import com.example.noforeignlandexam.data.Places
import com.example.noforeignlandexam.db.DestinationsDatabase
import com.example.noforeignlandexam.db.PlacesEntity
import com.example.noforeignlandexam.ui.list.ListDestinations
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashactivity)

        val database = Room.databaseBuilder(applicationContext, DestinationsDatabase::class.java, "places.db").allowMainThreadQueries().build()

        Handler().postDelayed({
            startActivity(Intent(this, ListDestinations::class.java))
            finish()
        }, 2000)

        callFromWebJson(database)
    }

    private fun callFromWebJson(database: DestinationsDatabase) {
        println("Attempting to Fetch! JSON")

        val url = "https://www.noforeignland.com/home/api/v1/places/"

        val client = OkHttpClient()

        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()

                val gson = GsonBuilder().create()

                val places = gson.fromJson(body, Places::class.java)

                if(database.placesDAO().getAllPlaces().isEmpty()){

                    places.features.forEach{

                        val thread = Thread{

                            val placesEntity = PlacesEntity()
                            placesEntity.placesId = it.properties.id
                            placesEntity.placeName = it.properties.name
                            placesEntity.placeLon = it.geometry.coordinates[0]
                            placesEntity.placeLat = it.geometry.coordinates[1]

                            database.placesDAO().insertPlaces(placesEntity)
                        }
                        thread.start()
                    }
                }else {
                    Log.d("database", "Fetching to locale storage")
                }
                Log.d("database", "Fetching from API")

            }

            override fun onFailure(call: Call, e: IOException) {

                println("Failed to fetch, JSON")
            }
        })
    }


}
