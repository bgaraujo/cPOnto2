package br.com.cponto.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import br.com.cponto.Config.FirebaseConfig;
import br.com.cponto.Model.Employee;
import br.com.cponto.Model.Register;
import br.com.cponto.Activity.MainActivity;
import br.com.cponto.helper.Loaders;

public class EmployeeController {
    FirebaseConfig firebaseConfig;
    private Loaders loaders;
    private ProgressDialog load;


    public void setPoint(Context context , Register register) {
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        String month = String.valueOf(Calendar.getInstance().get(Calendar.MONTH));
        String day = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        firebaseConfig.getFirebaseDatabase().child("points").child(year).child(month).push().setValue(register);

    }

}
