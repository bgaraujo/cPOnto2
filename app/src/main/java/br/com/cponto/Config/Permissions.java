package br.com.cponto.Config;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissions {

    public static boolean requestPermission(int requestCode, Activity activity, String[] permissions){

        if(Build.VERSION.SDK_INT >= 23){

            List<String> listPermission = new ArrayList<String>();

            for (String permission : permissions){
                Boolean validPemission = ContextCompat.checkSelfPermission(activity,permission) == PackageManager.PERMISSION_GRANTED;
                if( !validPemission )
                    listPermission.add(permission);
            }

            if ( listPermission.isEmpty() ){
                return true;
            }

            String[] newPermissions = new String[listPermission.size()];
            listPermission.toArray(newPermissions);

            ActivityCompat.requestPermissions(activity,newPermissions,requestCode);

        }

        return true;
    }
}
