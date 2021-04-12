package fi.hyria.weatherapp

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONObject
import kotlin.math.round

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getCurrentWeatherData("riihimäki")
    }

    fun getCurrentWeatherData(city: String) {
        val queue = Volley.newRequestQueue(this)
        val url = "https://api.openweathermap.org/data/2.5/weather?lang=fi&units=metric&q=" + city + "&appid=" + API_KEY
        val stringRequest = StringRequest(
            Request.Method.GET, url,
                { response ->
                    val data = JSONObject(response)
                    findViewById<TextView>(R.id.cityName).text = data.get("name").toString()
                    val weather = data.getJSONArray("weather")
                    val weather_info: JSONObject = weather.get(0) as JSONObject
                    val icon = weather_info.get("icon")
                    val weatherIconView: ImageView = findViewById(R.id.weatherIcon)
                    Picasso.get().load("https://openweathermap.org/img/wn/" + icon + "@2x.png").into(
                        weatherIconView
                    );
                    findViewById<TextView>(R.id.weatherDesc).text = weather_info.get("description").toString().capitalize()

                    val main = data.getJSONObject("main")
                    var temp: Double = round(10 * main.get("temp") as Double) / 10
                    findViewById<TextView>(R.id.temperature).text = temp.toString()+" °C"
                },
                {
                    Log.d("ERROR", "Getting weather information failed!")
                })
        queue.add(stringRequest)

    }
}