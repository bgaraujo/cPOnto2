package br.com.cponto.Activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import br.com.cponto.Config.FirebaseConfig;
import br.com.cponto.Controller.EmployeeController;
import br.com.cponto.Model.Register;
import br.com.cponto.R;

public class MainActivity extends AppCompatActivity {

    private EmployeeController employeeController;
    private Register register;
    private FirebaseConfig firebaseConfig;
    double longitude;
    double latitude;

    private FloatingActionButton btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);

        register = new Register();

        employeeController = new EmployeeController();

        btnConfirm = findViewById(R.id.feed_btn_cad);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LocationManager lm = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
                if (
                        ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        ) {
                    //Va para login
                    
                    return;
                }
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                //Get allData
                register.setLatitude((int) location.getLatitude());
                register.setLongitude((int) location.getLongitude());
                register.setTime((int) System.currentTimeMillis());
                register.setUuid(firebaseConfig.getFirebaseAuth().getCurrentUser().getUid());

                //Register
                employeeController.setPoint(MainActivity.this,register);

                Toast.makeText(MainActivity.this,String.valueOf(latitude)+"_"+String.valueOf(longitude),Toast.LENGTH_LONG).show();
                Log.i("teste", String.valueOf(latitude)+"_"+String.valueOf(longitude) );

            }
        });

    }

}
