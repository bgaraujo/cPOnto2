package br.com.cponto.Adapters;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import com.google.android.gms.maps.SupportMapFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import br.com.cponto.Model.Register;
import br.com.cponto.R;

public class AdapterMainActivity extends BaseAdapter implements OnMapReadyCallback {
    private Context context;
    private ArrayList<Register> registerArrayList;
    private GoogleMap mMap;

    public AdapterMainActivity(Context context, ArrayList<Register> registerArrayList) {
        this.context = context;
        this.registerArrayList = registerArrayList;
    }

    @Override
    public int getCount() {
        return registerArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return registerArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cardView = convertView;
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        Register register = registerArrayList.get(position);

        if( cardView == null )
            cardView = layoutInflater.inflate(R.layout.card_activity_main_feed, null);

        TextView hour = cardView.findViewById(R.id.card_activity_m_f_time);
        TextView date = cardView.findViewById(R.id.card_activity_m_f_date);
        ImageView profilePicture = cardView.findViewById(R.id.card_activity_image_profile);


        Uri imgUri = Uri.parse(register.getPicture());
        profilePicture.setImageURI(null);
        profilePicture.setImageURI(imgUri);
        date.setText(register.getDate());
        hour.setText(register.getHour());

        return cardView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    private void setMap(float latitude, float longitude){
        // Add a marker in Sydney and move the camera
        LatLng location = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(location).title("Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.setMaxZoomPreference(15);
        mMap.setMinZoomPreference(15);

    }

}
