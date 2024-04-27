package com.example.midtest.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.midtest.Model.User;
import com.example.midtest.R;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<User> {
    Activity context;
    int idLayout;
    List<User> data;
    List<User> data_all;
    public CustomAdapter(Activity context, int idLayout, ArrayList<User> data) {
        super(context, idLayout, data);
        this.context = context;
        this.idLayout = idLayout;
        this.data = data;
        this.data_all = new ArrayList<>(data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(idLayout, parent, false);

        User user = data.get(position);

        ImageView ivHinh = convertView.findViewById(R.id.ivHinh);
//        Picasso.get().load(user.getAvatar()).into(ivHinh);
        Glide.with(context).load(user.getAvatar()).transform(new RoundedCorners(30)).into(ivHinh);

        TextView tvLogin = convertView.findViewById(R.id.tvLogin);
        tvLogin.setText(user.getLogin());

        TextView tvURL = convertView.findViewById(R.id.tvURL);
        tvURL.setText(user.getUrl());

        return convertView;
    }

    public  void SearchData(String msg){
        data.clear();
        if(msg.equals("")){
            data.addAll(data_all);
        }
        else {
            for (User user :data_all){
                if(user.getLogin().contains(msg))
                    data.add(user);
            }
        }
        notifyDataSetChanged();
    }
}
