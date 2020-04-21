package com.example.noforeignlandexam.ui.list


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.noforeignlandexam.R
import com.example.noforeignlandexam.db.PlacesEntity
import com.example.noforeignlandexam.ui.details.DestinationsDetails
import com.example.noforeignlandexam.ui.map.MapsActivity
import kotlinx.android.synthetic.main.places_row.view.*

class ListDestinationsAdapter(
    private var placeListFull: MutableList<PlacesEntity> = mutableListOf()
        ) : RecyclerView.Adapter<ListDestinationsAdapter.ListPlaceViewHolder?>(), Filterable {

    private var featureListToShow: MutableList<PlacesEntity> = mutableListOf()

    init {
        featureListToShow = placeListFull
    }

    override fun getItemCount(): Int {
        return featureListToShow.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPlaceViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.places_row, parent, false)

        return ListPlaceViewHolder(
            cellForRow
        )
    }

    override fun onBindViewHolder(holder: ListPlaceViewHolder, position: Int) {

        val feature = featureListToShow[position]

        holder.view.textView_places_row.text = feature.placeName

        val placeName = feature.placeName
        val placeLon = feature.placeLon
        val placeLat = feature.placeLat

        holder.view.imageView_places_row.setOnClickListener {
            println("Hello From navigation Link")
            goToMap(holder.view, placeName, placeLon, placeLat)
        }

        holder.feature = feature

    }

    private fun goToMap(view: View, name: String, lon: Double, lat: Double) {

        val PLACE_NAME_KEY = "PLACE_NAME"
        val PLACE_LON_KEY = "PLACE_LON"
        val PLACE_LAT_KEY = "PLACE_LAT"

        val intent = Intent(view.context, MapsActivity::class.java)
        intent.putExtra(PLACE_NAME_KEY, name)
        intent.putExtra(PLACE_LON_KEY, lon)
        intent.putExtra(PLACE_LAT_KEY, lat)
        view.context.startActivity(intent)
    }

    override fun getFilter(): Filter {
        return placeFilter
    }

    private val placeFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults? {
            val aFilteredList: MutableList<PlacesEntity> =
                if (constraint == null || constraint.isEmpty()) {
                    placeListFull

                } else {
                    placeListFull.filter {
                        it.placeName.contains(
                            constraint.toString(),
                            ignoreCase = true
                        )
                    } as MutableList<PlacesEntity>

                }

            val result = FilterResults()
            result.values = aFilteredList
            return result
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

            results?.values.let {
                featureListToShow = it as MutableList<PlacesEntity>
            }
            notifyDataSetChanged()
        }
    }

    class ListPlaceViewHolder(
        val view: View,
        var feature: PlacesEntity? = null
    ) :
        RecyclerView.ViewHolder(view) {

        companion object {
            const val PLACE_TITLE_KEY = "PLACE_TITLE"
            const val PLACE_ID_KEY = "PLACE_ID"

        }

        init {
            view.setOnClickListener {

                val intent = Intent(view.context, DestinationsDetails::class.java)

                intent.putExtra(PLACE_TITLE_KEY, feature?.placeName)
                intent.putExtra(PLACE_ID_KEY, feature?.placesId)

                view.context.startActivity(intent)
            }
        }
    }
}

