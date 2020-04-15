package com.example.noforeignlandexam.ui.list


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.noforeignlandexam.R
import com.example.noforeignlandexam.db.DestinationsDatabase
import com.example.noforeignlandexam.db.PlacesEntity
import kotlinx.android.synthetic.main.list_places.*

class ListDestinations : AppCompatActivity() {

    private var adapter: ListDestinationsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_places)

        val database =
            Room.databaseBuilder(applicationContext, DestinationsDatabase::class.java, "places.db")
                .allowMainThreadQueries().build()

        val listOfLocations = database.placesDAO().getAllPlaces()

        recyclerView_List.layoutManager = LinearLayoutManager(this)

        renderLocations(listOfLocations)

    }

    private fun renderLocations(listOfLocations: List<PlacesEntity>) {

        runOnUiThread {
            adapter = ListDestinationsAdapter(listOfLocations as MutableList<PlacesEntity>)
            recyclerView_List.adapter = adapter
            adapter!!.notifyDataSetChanged()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        val searchItem: MenuItem = menu.findItem(R.id.menu_item_Search)
        val searchView: SearchView = searchItem.actionView as SearchView

        searchView.imeOptions = EditorInfo.IME_ACTION_DONE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter?.filter?.filter(newText)
                return false
            }
        })
        return true
    }
}




