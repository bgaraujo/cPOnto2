package br.com.cponto.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.cponto.Model.Company;
import br.com.cponto.R;

public class CadCompanyActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    Company company = new Company();
    public String TAG = "teste";
    //Button cadCompany;

    //Fields

    EditText cadCompanyCnpj;
    EditText cadComapanyName;
    EditText cadCompanyEmail;
    EditText cadCompanyPassword;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_company);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        Button cadCompany = findViewById(R.id.cadCompanySave);
        cadCompanyCnpj = (EditText) findViewById( R.id.cadCompanyCnpj );
        cadComapanyName = (EditText) findViewById( R.id.cadCompanyName );
        cadCompanyEmail = (EditText) findViewById( R.id.cadCompanyEmail );
        cadCompanyPassword = (EditText) findViewById( R.id.cadCompanyPassword );



        cadCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptCad();
            }
        });


    }


    private void attemptCad() {

        // Reset errors.
        cadCompanyCnpj.setError(null);
        cadComapanyName.setError(null);
        cadCompanyEmail.setError(null);
        cadCompanyPassword.setError(null);

        // Store values at the time of the login attempt.
        String cnpj = cadCompanyCnpj.getText().toString();
        String name = cadComapanyName.getText().toString();
        String email = cadCompanyEmail.getText().toString();
        String password = cadCompanyPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(cnpj)) {
            cadCompanyCnpj.setError("Campo Obrigatorio");
            focusView = cadCompanyCnpj;
            cancel = true;
        }

        if (TextUtils.isEmpty(name)) {
            cadComapanyName.setError("Campo Obrigatorio");
            focusView = cadComapanyName;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !company.isPasswordValid(password)) {
            cadCompanyPassword.setError(getString(R.string.error_invalid_password));
            focusView = cadCompanyPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            cadCompanyEmail.setError(getString(R.string.error_field_required));
            focusView = cadCompanyEmail;
            cancel = true;
        } else if (!company.isEmailValid(email)) {
            cadCompanyEmail.setError(getString(R.string.error_invalid_email));
            focusView = cadCompanyEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            cadCompany();
            //Funçao de login
            //signIn(email,password);
        }
    }




    private void cadCompany( ){
        company.setCnpj( Integer.parseInt( cadCompanyCnpj.getText().toString()  ) );
        company.setName( cadComapanyName.getText().toString() );
        company.setEmail( cadCompanyEmail.getText().toString() );
        String password = cadCompanyPassword.getText().toString();


        mAuth.createUserWithEmailAndPassword(company.getEmail(), password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        DatabaseReference myRef = mDatabase.getReference();

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(CadCompanyActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }else{

                            Log.i("teste",mAuth.getCurrentUser().getUid().toString());

                            myRef.setValue(mAuth.getCurrentUser().getUid());

                            myRef.child(mAuth.getCurrentUser().getUid()).setValue(company);


                        }

                    }
                });


    }

    private void login (String email, String password){
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser cCompany = mAuth.getCurrentUser();
                            company.setEmail(cCompany.getDisplayName());
                            company.setUid(cCompany.getUid());


                            System.out.print(cCompany);

                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(CadCompanyActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
