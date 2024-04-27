package com.example.midtest.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.midtest.Model.SanPham;
import com.example.midtest.Model.User;
import com.example.midtest.R;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends ArrayAdapter<SanPham> {
    Activity context;
    int idLayout;
    List<SanPham> data;

    public ProductAdapter(Activity context, int idLayout, ArrayList<SanPham> data) {
        super(context, idLayout, data);
        this.context = context;
        this.idLayout = idLayout;
        this.data = data;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(idLayout, parent, false);

        SanPham sanPham = data.get(position);

        ImageView ivHinh = convertView.findViewById(R.id.ivHinh);
//        Picasso.get().load(user.getAvatar()).into(ivHinh);
        Glide.with(context).load(R.drawable.product).into(ivHinh);;

        TextView tvTenSP = convertView.findViewById(R.id.tvTenSP);
        tvTenSP.setText(sanPham.getTenSP());

        TextView tvGia = convertView.findViewById(R.id.tvGiaSP);
        tvGia.setText(sanPham.getGiaSP());

        return convertView;
    }
}
