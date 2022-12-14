package com.example.wander

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.wander.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.GroundOverlayOptions
import com.google.android.gms.maps.model.MapStyleOptions
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback
{

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private val TAG = MapsActivity::class.java.simpleName

    private val REQUEST_LOCATION_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap)
    {
        map = googleMap

        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        map.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        //Messala Location: 29.315276, 30.852601
        val latitude = 29.315276
        val longitude =  30.852601
        val zoomLevel = 10f
        val overlaySize = 100f

        val mesalaLocation = LatLng(latitude, longitude)
        map.addMarker(MarkerOptions().position(mesalaLocation).title("????????????"))
        //map.moveCamera(CameraUpdateFactory.newLatLng(mesalaLocation))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(mesalaLocation, zoomLevel))

        val androidOverlay = GroundOverlayOptions().image(BitmapDescriptorFactory.fromResource(R.drawable.android)).position(mesalaLocation, overlaySize)
        map.addGroundOverlay(androidOverlay)

        setMapLongClicked(map)
        setPoiClick(map)
        setMapStyle(map)
        enableMyLocation()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        menuInflater.inflate(R.menu.map_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when(item.itemId)
        {
            //change type of map to normal type
            R.id.normal_map -> map.mapType = GoogleMap.MAP_TYPE_NORMAL

            //change type of map to Satellite type
            R.id.satellite_map -> map.mapType = GoogleMap.MAP_TYPE_SATELLITE

            //change type of map to Hybrid type(normal + satellite)
            R.id.hybrid_map -> map.mapType = GoogleMap.MAP_TYPE_HYBRID

            //change type of map to Terrain type
            R.id.terrain_map -> map.mapType = GoogleMap.MAP_TYPE_TERRAIN
        }
        return true
    }

    private fun setMapLongClicked(map: GoogleMap)
    {
        map.setOnMapLongClickListener { latLng ->
            // A Snippet is Additional text that's displayed below the title.
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                latLng.latitude,
                latLng.longitude
            )
            map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(getString(R.string.dropped_pin))
                    .snippet(snippet)
                //change color of marker
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            )
            //to show marker info
            ?.showInfoWindow()

        }
    }

    private fun setPoiClick(map: GoogleMap)
    {
        map.setOnPoiClickListener {
            val poiMarker = map.addMarker(MarkerOptions().position(it.latLng).title(it.name))

            poiMarker?.showInfoWindow()
        }
    }

    private fun setMapStyle(map: GoogleMap)
    {
        try {
            // Customize the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success = map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))

            if(!success)
            {
                Log.i("Felo", "Style parsing failed")
            }

        }catch(e: Exception)
        {
            Log.i("Felo", "Exception")
            Log.i("Felo", "Can't find style. Error: ${e.toString()}")
        }
    }

    private fun isPermissionGranted(): Boolean
    {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) === PackageManager.PERMISSION_GRANTED
    }

    private fun enableMyLocation()
    {
        if(isPermissionGranted())
        {
            map.setMyLocationEnabled(true)
        }
        else
        {
            ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        Log.i("Felo", "Check Location")
        if(requestCode == REQUEST_LOCATION_PERMISSION)
        {
            if(grantResults.size > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED))
            {
                enableMyLocation()
            }
        }
    }
    
}