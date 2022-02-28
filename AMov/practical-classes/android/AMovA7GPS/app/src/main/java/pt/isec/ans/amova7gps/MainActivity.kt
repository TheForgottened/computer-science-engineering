package pt.isec.ans.amova7gps

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import pt.isec.ans.amova7gps.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var fineLocationPermission = false
    private var coarseLocationPermission = false

    private lateinit var b : ActivityMainBinding
    lateinit var lm : LocationManager
    private lateinit var floc : FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        lm = getSystemService(LOCATION_SERVICE) as LocationManager
        floc = FusedLocationProviderClient(this)

        fineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        coarseLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!fineLocationPermission && !coarseLocationPermission)
            requestLocationPermissions.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private val requestLocationPermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        fineLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        coarseLocationPermission =
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        startLocationUpdates()
    }

    private var locationEnabled = false

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        if (locationEnabled || (!fineLocationPermission && !coarseLocationPermission))
            return

        currentLocation = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
                                ?: Location("AMOV").apply {
            latitude = -12.34  // valores para teste na aula
            longitude = 43.21  //   colocar em vez de PASSIVE, por exemplo, "AMOV"
        }

        if (fineLocationPermission)
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,10,10f,locationCallback)
        else // coarseLocationPermission é true
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,10,10f,locationCallback)

        floc.requestLocationUpdates(locReq, locationCallback, null)

        locationEnabled = true
    }

    fun stopLocationUpdates() {
        if (!locationEnabled)
            return
        // lm.removeUpdates(locationCallback)

        floc.removeLocationUpdates(locationCallback)

        locationEnabled = false
    }

    val locationCallback = LocationListener { location ->
        currentLocation = location
    }

    var location = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            p0.locations.forEach {location ->

            }
        }
    }

    private var currentLocation = Location("")
        get() = field
        set(value) {
            field = value
            b.tvLat.text = String.format("%10.5f",value.latitude)
            b.tvLon.text = String.format("%10.5f",value.longitude)
        }

}