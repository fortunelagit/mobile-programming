package com.project.dialog_listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class contactAdapter extends ArrayAdapter<contact> {

    public contactAdapter(@NonNull Context context, int resource,
                          @NonNull List<contact> objects) {
        super(context, resource, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent){

        contact datacontact = getItem(position);
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).
                            inflate(R.layout.item_contact, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvPhoneNumber = (TextView) convertView.findViewById(R.id.tvPhoneNumber);

        tvName.setText(datacontact.getName());
        tvPhoneNumber.setText(datacontact.getPhoneNumber());

        return convertView;
    }

}
