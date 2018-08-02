package br.com.cponto.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.Calendar;

import br.com.cponto.Config.FirebaseConfig;
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

        firebaseConfig.getFirebaseDatabase().child("points").child(year).child(month).child(day).push().setValue(register);

    }

    public void isLoggedIn(Context context){
        if( firebaseConfig.getFirebaseAuth().getCurrentUser() != null ){
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
            ((Activity)context).finish();
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public void signIn(String email, String password,final Context context) {
        loaders = new Loaders();
        load = loaders.loading(context);
        load.show();
        // [START sign_in_with_email]
        firebaseConfig.getFirebaseAuth()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Login
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                            ((Activity)context).finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                            load.dismiss();

                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            ;//mStatusTextView.setText(R.string.auth_failed);
                        }
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

}
