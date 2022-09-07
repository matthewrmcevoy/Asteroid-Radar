package com.udacity.asteroidradar

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.main.AsteroidRadarApiStatus

@BindingAdapter("imageUrl")
fun bindAPODImg(imgView: ImageView, imgUrl: String? ){
    Picasso.get().load(imgUrl).into(imgView)
}

@BindingAdapter("asteroidApiStatus")
fun bindAsteroidApiStatus(apiStatusProgressBar: ProgressBar, apiStatus: AsteroidRadarApiStatus?){
    when(apiStatus){
        AsteroidRadarApiStatus.LOADING -> {
            apiStatusProgressBar.visibility = View.VISIBLE
        }
        AsteroidRadarApiStatus.DONE -> {
            apiStatusProgressBar.visibility = View.GONE
        }
        AsteroidRadarApiStatus.ERROR -> {
            apiStatusProgressBar.visibility = View.GONE
        }
        AsteroidRadarApiStatus.NO_ITEMS -> {
            apiStatusProgressBar.visibility = View.GONE
        }
    }
}
@BindingAdapter("apiStatusImage")
fun bindApiStatusImage(apiStatusImageView: ImageView, apiStatus: AsteroidRadarApiStatus?) {
    when (apiStatus) {
        AsteroidRadarApiStatus.LOADING -> {
            apiStatusImageView.visibility = View.GONE
        }
        AsteroidRadarApiStatus.DONE -> {
            apiStatusImageView.visibility = View.GONE
        }
        AsteroidRadarApiStatus.ERROR -> {
            apiStatusImageView.visibility = View.VISIBLE
            apiStatusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        AsteroidRadarApiStatus.NO_ITEMS -> {
            apiStatusImageView.visibility = View.VISIBLE
            apiStatusImageView.setImageResource(R.drawable.ic_connection_error)
        }
    }
}

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}
@BindingAdapter("statusImgContentDesc")
fun bindStatusImgContentDesc(imageView: ImageView, isHazardous: Boolean){
    if(isHazardous){
        imageView.contentDescription = imageView.context.getString(R.string.potentially_hazardous_asteroid_image)
    }else{
        imageView.contentDescription = imageView.context.getString(R.string.not_hazardous_asteroid_image)
    }
}
