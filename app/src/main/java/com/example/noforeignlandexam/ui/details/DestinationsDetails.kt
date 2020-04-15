package com.example.noforeignlandexam.ui.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noforeignlandexam.R
import com.example.noforeignlandexam.data.FromPlaceId
import com.example.noforeignlandexam.data.Place
import com.example.noforeignlandexam.ui.list.ListDestinationsAdapter
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.list_places.*
import okhttp3.*
import java.io.IOException

class DestinationsDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.list_places)

        recyclerView_List.layoutManager = LinearLayoutManager(this)

        val navBarTitle = intent.getStringExtra(ListDestinationsAdapter.ListPlaceViewHolder.PLACE_TITLE_KEY)
        supportActionBar?.title = navBarTitle

        fetchJSON()
    }

    private fun fetchJSON() {

        val placeId = intent.getLongExtra(ListDestinationsAdapter.ListPlaceViewHolder.PLACE_ID_KEY, -1)
        val placeDetailUrl = "https://www.noforeignland.com/home/api/v1/place?id=$placeId"

        val client = OkHttpClient()
        val request = Request.Builder().url(placeDetailUrl).build()
        client.newCall(request).enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()

                val gson = GsonBuilder().create()

                val fromPlaceId = gson.fromJson(body, FromPlaceId::class.java)
                val place = gson.fromJson(body, Place::class.java)

                runOnUiThread {
                    recyclerView_List.adapter =
                        PlaceDetailsAdapter(
                            fromPlaceId
                        )
                }

            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failed to fetch the api")
            }
        })
    }
}