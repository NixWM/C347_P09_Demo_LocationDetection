package sg.edu.ro.c346.id16046530.c347_p08_demo_locationdetection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    Button btnGetLastLocation, btnGetLocationUpdate, btnRemoveLocationUpdate;
    FusedLocationProviderClient client;
    LocationRequest mLocationRequest;
    LocationCallback mLocationCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGetLastLocation = findViewById(R.id.btnGetLastLocation);
        btnGetLocationUpdate = findViewById(R.id.btnGetLocationUpdate);
        btnRemoveLocationUpdate = findViewById(R.id.btnRemoveLocationUpdate);

       // client = LocationServices.getFusedLocationProviderClient(this);






         mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult !=null) {
                    Location data = locationResult.getLastLocation();
                    String msg = "New Location Detected\n" + "Lat: " + data.getLongitude() + ", " + "Lng: " + data.getLongitude();
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                } else {
                    String msg = "No Updated Location found";
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                }

            }
        };

         btnGetLocationUpdate.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (checkPermission() == true) {
                     client = LocationServices.getFusedLocationProviderClient(MainActivity.this);
                     mLocationRequest = new LocationRequest();
                     mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                     mLocationRequest.setInterval(100);
                     mLocationRequest.setFastestInterval(50);
                     mLocationRequest.setSmallestDisplacement(0);
                     client.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
                 }
                 else {
                     String msg = "Permission not granted to retrieve location info";
                     Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                     ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                 }
             }
         });
         btnRemoveLocationUpdate.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 client.removeLocationUpdates(mLocationCallback);
             }
         });


       btnGetLastLocation.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               if (checkPermission() == true) {
                   client = LocationServices.getFusedLocationProviderClient(MainActivity.this);
                   Task<Location> task = client.getLastLocation();
                   task.addOnSuccessListener(new OnSuccessListener<Location>() {
                       @Override
                       public void onSuccess(Location location) {
                           if (location !=null) {
                               String msg = "Lat : " + location.getLatitude() + " Lng : " + location.getLongitude();
                               Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                           } else {
                               String msg = "No Last Known Location found";
                               Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                           }
                       }
                   });
               }
           }
       });
    }
    private boolean checkPermission(){
        int permissionCheck_Coarse = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED
                || permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED) {
            return true;
        } else {
//            String msg = "Permission not granted to retrieve location info";
//            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(MainActivity.this,

                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            return false;
        }

    }

}