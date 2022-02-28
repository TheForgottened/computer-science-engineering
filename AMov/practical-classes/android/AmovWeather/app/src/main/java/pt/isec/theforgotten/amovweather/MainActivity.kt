package pt.isec.theforgotten.amovweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import org.json.JSONObject
import pt.isec.theforgotten.amovweather.databinding.ActivityMainBinding

class MyViewModel : ViewModel() {
    private val apikey = "3c80922da94c468abbd105545210712"
    val cidade = "Coimbra"
    val webContent : MutableLiveData<String?> = MutableLiveData()

    fun updateInfo() {
        NetUtils.getDataAsync("https://api.weatherapi.com/v1/forecast.json?key=$apikey&q=$cidade&days=3&aqi=no&alerts=no", webContent)
    }
}

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding : ActivityMainBinding
    private val model : MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        if (!NetUtils.verifyNetworkStateV2(this)) {
            Toast.makeText(this, "No network available", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        model.webContent.observe(this) { webContent ->
            try {
                val json = JSONObject(webContent)
                val stringBuilder = StringBuilder("Resultado:\n")

                val current = json.getJSONObject("current")

                val condition = json.getJSONObject("condition")
                val icon = condition["icon"]

                Glide.with(this).load("https://$icon").into(viewBinding.imageView)

                val temp = current["temp_c"]
                stringBuilder.append("Temperatura atual: $temp\n")

                val forecast = json.getJSONObject("forecast")
                val forecastday = forecast.getJSONArray("forecastday")

                for (dia in 0 until forecastday.length()) {
                    val day = forecastday.getJSONObject(dia)
                    val date = day["date"]
                    stringBuilder.append("Info: $date\n")
                }

                viewBinding.tvContent.text = stringBuilder.toString()
            } catch (_:Exception) {
                viewBinding.tvContent.text = "What a terrible failure!"
            }

            viewBinding.tvContent.text = webContent
        }

        model.updateInfo()
    }
}