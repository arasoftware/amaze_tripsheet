package com.ara.advent.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ara.advent.R;
import com.ara.advent.models.TripsheetListModel;

import java.util.ArrayList;

/**
 * Created by Sharp Kumar on 13-05-2018.
 */

public class TripsheetListAdapter extends ArrayAdapter<TripsheetListModel> {


    ArrayList<TripsheetListModel> ara = new ArrayList<TripsheetListModel>();

    public TripsheetListAdapter(Context applicationContext, int stockreportitems, ArrayList<TripsheetListModel> reports_dataModelArrayList) {
        super(applicationContext, stockreportitems, reports_dataModelArrayList);
        ara = reports_dataModelArrayList;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        TripsheetListModel tmodel = ara.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listitems, null);
            viewHolder.customerName = (TextView) convertView.findViewById(R.id.text_tbcustname);
            viewHolder.tripNo = (TextView) convertView.findViewById(R.id.text_tbno);
            viewHolder.date = (TextView) convertView.findViewById(R.id.text_tbdate);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.customerName.setText(ara.get(position).getCustomer_name());
        viewHolder.tripNo.setText(ara.get(position).getTripBooking_no());
        viewHolder.date.setText( ara.get(position).getTripBooking_date());
        return convertView;
    }
}

class ViewHolder {

    TextView customerName, tripNo, date;

}
