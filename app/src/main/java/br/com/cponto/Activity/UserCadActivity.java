package br.com.cponto.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.location.Location;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
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

import br.com.cponto.Config.FirebaseConfig;
import br.com.cponto.Controller.EmployeeController;
import br.com.cponto.Model.Register;
import br.com.cponto.R;
import br.com.cponto.helper.Picture;

public class UserCadActivity extends AppCompatActivity implements SurfaceHolder.Callback, OnMapReadyCallback {

    private EmployeeController employeeController;
    private Register register;
    private Camera camera;
    private Picture picture;
    private SurfaceView surface;
    double longitude;
    double latitude;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private TextView txtDate,txtTime,txtAux;
    FirebaseConfig firebaseConfig;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cad);

        employeeController = new EmployeeController();
        register = new Register();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        Button btnSave = findViewById(R.id.user_cad_add);
        txtDate = findViewById(R.id.user_cad_date);
        txtTime = findViewById(R.id.user_cad_time);

        //Camera
        camera = Camera.open(1);
        camera.setDisplayOrientation(90);

        //Date Time
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        txtTime.setText(timeFormat.format(date));
        txtDate.setText(dateFormat.format(date));

        surface = findViewById(R.id.userCad_surface_camera);
        surface.getHolder().addCallback(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setMap();

        // /storage/emulated/0/Pictures/cPonto/IMG_20180801_201718.jpg
        String root_sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/cPonto".toString();
        File file = new File( root_sd ) ;

        File list[] = file.listFiles();

        for( int i=0; i< list.length; i++) {
            Log.i("teste", list[i].getName().toString());
        }



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get allData
                register.setLatitude((float) latitude);
                register.setLongitude((float) longitude);
                register.setTime((int) System.currentTimeMillis());
                register.setUuid(firebaseConfig.getFirebaseAuth().getCurrentUser().getUid());
                register.setTxtAux("Vamos Teste");
                camera.takePicture(null,null,mPicture);
                //Register
                employeeController.setPoint(UserCadActivity.this,register);
            }
        });
        Toast.makeText(UserCadActivity.this,String.valueOf(latitude)+"_"+String.valueOf(longitude),Toast.LENGTH_LONG).show();
        Log.i("teste", String.valueOf(latitude)+"_"+String.valueOf(longitude) );
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
