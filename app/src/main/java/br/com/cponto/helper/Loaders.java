package br.com.cponto.helper;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.AbsListView;
import android.widget.ProgressBar;

public class Loaders {

    public ProgressDialog loading(Context context){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle( progressDialog.STYLE_SPINNER );
        progressDialog.setIndeterminate(true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return progressDialog;
    }

}
