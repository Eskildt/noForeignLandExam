package com.example.noforeignlandexam.ui.details

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noforeignlandexam.R
import com.example.noforeignlandexam.data.FromPlaceId
import com.example.noforeignlandexam.ui.map.MapsActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.places_details_row.view.*

class PlaceDetailsAdapter(val fromPlaceId: FromPlaceId) :
    RecyclerView.Adapter<PlaceDetailsAdapter.PlaceDetailsViewHolder>() {

    override fun getItemCount(): Int {
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceDetailsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val customView = layoutInflater.inflate(R.layout.places_details_row, parent, false)

        return PlaceDetailsViewHolder(
            customView,
            fromPlaceId
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PlaceDetailsViewHolder, position: Int) {

        val place = fromPlaceId.place

        val defaultComment = "No comments has been made yet of this place."


        val comments = place.comments
            .replace("<p>", "")
            .replace("</p>", "")
            .replace("<br/>", "")
            .replace("<br>", "")
            .replace("<b>", "")
            .replace("</b>", "")

        val bannerPlaceImageView = holder.customView.imageView_place_banner

        val imageUrl = place.banner

        val defaultImage = R.drawable.no_image_found

        val feature = fromPlaceId

        holder.customView.textView_placeDetailName.text = "Place name: " + place.name

        if (comments.isEmpty()) {
            holder.customView.textView_places_comments.text = defaultComment
        } else {
            holder.customView.textView_places_comments.text = comments
        }

        holder.customView.textView_latitude.text = "Latitude: " + place.lat.toString()
        holder.customView.textView_longitude.text = "Longitude: " + place.lon.toString()

        if (imageUrl.isEmpty()) {
            Picasso.get().load(defaultImage).into(bannerPlaceImageView)
        } else {

            Picasso.get().load(imageUrl).into(bannerPlaceImageView)
        }

        val placeName = fromPlaceId.place.name
        val placeLon = fromPlaceId.place.lon
        val placeLat = fromPlaceId.place.lat

        holder.customView.imageView_ic_google_marker.setOnClickListener {
            println("Hello From navigation Link")
            goToMap(holder.customView, placeName, placeLon, placeLat)
        }

        holder.fromPlaceId = feature
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

    class PlaceDetailsViewHolder(val customView: View, var fromPlaceId: FromPlaceId? = null) :
        RecyclerView.ViewHolder(customView) {

        companion object {
            const val PLACE_ID_KEY = "PLACE_ID"
            const val PLACE_LAT = "PLACE_LAT"
            const val PLACE_LON = "PLACE_LON"
            const val PLACE_INFO = "PLACE_INF"
        }

    }

}