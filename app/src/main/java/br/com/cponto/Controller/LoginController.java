package br.com.cponto.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import br.com.cponto.Activity.MainActivity;
import br.com.cponto.Config.FirebaseConfig;
import br.com.cponto.Model.Employee;
import br.com.cponto.helper.Loaders;

import static android.content.Context.MODE_PRIVATE;

public class LoginController {
    private FirebaseConfig firebaseConfig;
    private FirebaseAuth firebaseAuth;
    private Loaders loaders;
    private ProgressDialog load;
    private Employee employee;

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
    public void signIn(String email, String password, final Context context) {
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
                            //Save on sharedPrefferences
                            saveUser( task.getResult().getUser().getUid(), context );

                            //Go to feed
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

    private void saveUser(final String uuid, final Context context){
        Log.i("teste",uuid);
        firebaseConfig.getFirebaseDatabase().child(uuid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Log.i("teste",dataSnapshot.getValue().toString());
                    Employee employee = dataSnapshot.getValue(Employee.class);

                    String jsonUser = new Gson().toJson(employee);

                    SharedPreferences.Editor sharedPrefferenceEdit = context.getSharedPreferences(employee.SHARED_NAME,MODE_PRIVATE).edit();
                    sharedPrefferenceEdit.putString("data",jsonUser);
                    sharedPrefferenceEdit.apply();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("teste",databaseError.getMessage() );
            }
        });
    }

}
