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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import br.com.cponto.Model.ModelToAdapterPoints;
import br.com.cponto.Model.Register;
import br.com.cponto.R;

public class AdapterMainActivity extends BaseAdapter {
    private Context context;
    private ArrayList<Register> registerArrayList;
    private ArrayList<ModelToAdapterPoints> matriz;
    private GoogleMap mMap;

    public AdapterMainActivity(Context context, ArrayList<ModelToAdapterPoints> matriz) {
        this.context = context;
        this.matriz = matriz;
    }

    @Override
    public int getCount() {
        return matriz.size();
    }

    @Override
    public Object getItem(int position) {
        return matriz.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cardView = convertView;
        ModelToAdapterPoints modelToAdapterPoints = new ModelToAdapterPoints();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        ArrayList<TextView> textViewArrayList = new ArrayList<TextView>();
        ArrayList<LinearLayout> linearLayoutArrayList = new ArrayList<LinearLayout>();

        if( cardView == null )
            cardView = layoutInflater.inflate(R.layout.card_activity_main_feed, null);

        //Header date
        TextView headerDate = cardView.findViewById(R.id.card_activit_header_date);

        //In
        textViewArrayList.add((TextView) cardView.findViewById(R.id.card_main_text_time_in));
        linearLayoutArrayList.add((LinearLayout) cardView.findViewById(R.id.card_main_linear_in));

        //Lunch Start
        textViewArrayList.add((TextView) cardView.findViewById(R.id.card_main_text_lunch_start) );
        linearLayoutArrayList.add((LinearLayout) cardView.findViewById(R.id.card_main_linear_lunch_start) );

        //Lunch Finish
        textViewArrayList.add((TextView) cardView.findViewById(R.id.card_main_text_lunch_finish) );
        linearLayoutArrayList.add((LinearLayout) cardView.findViewById(R.id.card_main_linear_lunch_finish) );

        //Lunch Out
        textViewArrayList.add((TextView) cardView.findViewById(R.id.card_main_text_out) );
        linearLayoutArrayList.add((LinearLayout) cardView.findViewById(R.id.card_main_linear_out) );


        Log.i("teste", String.valueOf(matriz.get(0)));

        modelToAdapterPoints = matriz.get(position);

        for (int i = 0; i < modelToAdapterPoints.registerArrayList.size() ; i++ ){
            Register register = modelToAdapterPoints.registerArrayList.get(i);

            headerDate.setText(register.getDate());

            if(i < 3) {
                textViewArrayList.get(i).setText(register.getHour());
                linearLayoutArrayList.get(i).setVisibility(View.VISIBLE);
            }
            //Uri imgUri = Uri.parse(register.getPicture());
            //profilePicture.setImageURI(null);
            //profilePicture.setImageURI(imgUri);
            //date.setText(register.getDate());
            //hour.setText(register.getHour());
        }

        //Register register = registerArrayList.get(position);

        return cardView;
    }



}
