package br.com.cponto.Activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import br.com.cponto.Config.Permissions;
import br.com.cponto.Controller.EmployeeController;
import br.com.cponto.Controller.LoginController;
import br.com.cponto.Model.Company;
import br.com.cponto.R;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    // Login
    private LoginController login;

    //Controllers
    private EmployeeController employeeController;

    //Activity Cadastrar lojas
    private Intent intent;

    //Permissions
    private String[] permissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    Company company = new Company();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = new LoginController();

        login.isLoggedIn(this);

        Permissions.requestPermission( permissions.length,this, permissions);

        //Activity Cadastrar lojas
        intent = new Intent( this , CadCompanyActivity.class );


        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        Button mCadCompanyButton = findViewById(R.id.login_cad_company);

        //Logar
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        //Cadastrar
        mCadCompanyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        login = new LoginController();
        employeeController = new EmployeeController();

        // Set up the login form.
        mEmailView = findViewById(R.id.login_email);
        mPasswordView = findViewById(R.id.login_password);

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !company.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!company.isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            //Funçao de login
            login.signIn(email,password,this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int result : grantResults){
            if (result == PackageManager.PERMISSION_DENIED){
                alertValidationPermission();
            }
        }
    }

    private void alertValidationPermission(){
        AlertDialog.Builder buider = new AlertDialog.Builder( this );
        buider.setTitle("Permissões negadas");
        buider.setMessage("Não fode!");

        buider.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        AlertDialog dialog = buider.create();
        dialog.show();

    }


}

