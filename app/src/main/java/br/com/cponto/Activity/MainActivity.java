package br.com.cponto.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import br.com.cponto.Adapters.AdapterMainActivity;
import br.com.cponto.Config.FirebaseConfig;
import br.com.cponto.Model.Register;
import br.com.cponto.R;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private FirebaseConfig firebaseConfig;
    private FloatingActionButton btnConfirm;
    private ArrayList<Register> registerArrayList;
    private AdapterMainActivity adapterMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.main_list_view);
        registerArrayList = new ArrayList<>();
        adapterMainActivity = new AdapterMainActivity(this, registerArrayList);
        listView.setAdapter(adapterMainActivity);

        getData();

        btnConfirm = findViewById(R.id.feed_btn_cad);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,UserRegisterActivity.class);
                startActivity(intent);

            }
        });

    }

    //Feed
    private void getData() {
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        String month = String.valueOf(Calendar.getInstance().get(Calendar.MONTH));
        firebaseConfig.getFirebaseDatabase().child("points").child(year).child(month).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Register register = snapshot.getValue(Register.class);
                        registerArrayList.add(register);
                    }
                    adapterMainActivity.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
