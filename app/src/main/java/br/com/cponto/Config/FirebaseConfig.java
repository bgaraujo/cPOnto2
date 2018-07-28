package br.com.cponto.Config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class FirebaseConfig {

    private static DatabaseReference firebaseDatabase;
    private static FirebaseAuth firebaseAuth;

    public static DatabaseReference getFirebaseDatabase(){

        if( firebaseAuth.getCurrentUser() != null ) {
            firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        }

        return firebaseDatabase;

    }

    public static FirebaseAuth getFirebaseAuth() {
        if( firebaseAuth == null ) {
            firebaseAuth = FirebaseAuth.getInstance();
        }

        return firebaseAuth;
    }
}
