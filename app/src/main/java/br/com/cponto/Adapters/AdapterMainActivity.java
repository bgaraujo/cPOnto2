package br.com.cponto.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.cponto.Model.Register;
import br.com.cponto.R;

public class AdapterMainActivity extends BaseAdapter {
    private Context context;
    private ArrayList<Register> registerArrayList;

    public AdapterMainActivity(Context context, ArrayList<Register> registerArrayList) {
        this.context = context;
        this.registerArrayList = registerArrayList;
    }

    @Override
    public int getCount() {
        return registerArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return registerArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cardView = convertView;
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        Register register = registerArrayList.get(position);

        if( cardView == null )
            cardView = layoutInflater.inflate(R.layout.card_activity_main_feed, null);

        TextView hour = cardView.findViewById(R.id.card_activity_m_f_time);
        TextView date = cardView.findViewById(R.id.card_activity_m_f_date);
        date.setText(register.getDate());
        hour.setText(register.getHour());

        return cardView;
    }
}
