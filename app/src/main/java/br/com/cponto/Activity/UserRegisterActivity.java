package br.com.cponto.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.location.Location;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.cponto.Config.FirebaseConfig;
import br.com.cponto.Controller.EmployeeController;
import br.com.cponto.Model.Employee;
import br.com.cponto.Model.Register;
import br.com.cponto.R;
import br.com.cponto.helper.Picture;

public class UserRegisterActivity extends AppCompatActivity implements SurfaceHolder.Callback, OnMapReadyCallback {

    private EmployeeController employeeController;
    private Register register = new Register();
    private Camera camera;
    private Picture picture;
    private SurfaceView surface;
    double longitude;
    double latitude;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private TextView txtDate,txtTime,txtAux;
    FirebaseConfig firebaseConfig;
    private Employee employee;
    private RadioButton checkIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        employeeController = new EmployeeController();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        employee = new Employee();

        Button btnSave = findViewById(R.id.user_cad_add);
        txtDate = findViewById(R.id.user_cad_date);
        txtTime = findViewById(R.id.user_cad_time);
        txtAux = findViewById(R.id.user_cad_ext_text);
        checkIn = findViewById(R.id.user_register_group_1);

        //Camera
        camera = Camera.open(1);
        camera.setDisplayOrientation(90);

        List<Camera.Size> sizes = camera.getParameters().getSupportedPictureSizes();

        Camera.Parameters parameters = camera.getParameters();
        parameters.setPictureSize(sizes.get( sizes.size()-2 ).width,sizes.get( sizes.size()-2 ).height);
        camera.setParameters(parameters);

        //Date Time
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        txtTime.setText(timeFormat.format(date));
        txtDate.setText(dateFormat.format(date));

        surface = findViewById(R.id.userCad_surface_camera);
        surface.getHolder().addCallback(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setMap();


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( checkIn.isChecked() ){
                    register.setAction(1);
                }else{
                    register.setAction(2);
                }

                //Get allData
                register.setLatitude((float) latitude);
                register.setLongitude((float) longitude);
                register.setTime( System.currentTimeMillis() );
                register.setUuid(firebaseConfig.getFirebaseAuth().getCurrentUser().getUid());
                register.setTxtAux(txtAux.getText().toString());
                register.setPicture("");
                camera.takePicture(null,null,mPicture);
                //Register
                //employeeController.setPoint(UserRegisterActivity.this,register);
            }
        });
    }

    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            picture = new Picture();
            File pictureFile = picture.getOutputMediaFile();
            if (pictureFile == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();

                picture.saveinFirebase(pictureFile);

                register.setPicture(Uri.fromFile(pictureFile).toString());
                employeeController.setPoint(UserRegisterActivity.this,register);
            } catch (FileNotFoundException e) {

            } catch (IOException e) {
            }
        }
    };

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try{

            camera.setPreviewDisplay(holder);

            camera.startPreview();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if(holder.getSurface() != null){
            try{
                camera.stopPreview();
            }catch (Exception e){

            }
        }
        try{
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void setMap(){
        //Get location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    Log.i("teste",location.toString());
                    // Add a marker in Sydney and move the camera
                    LatLng place = new LatLng(location.getLatitude(), location.getLongitude());
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    mMap.addMarker(new MarkerOptions().position(place).title("Voce esta aqui!"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(place));
                    mMap.setMaxZoomPreference(15);
                    mMap.setMinZoomPreference(15);
                }
            }
        });
    }
}
