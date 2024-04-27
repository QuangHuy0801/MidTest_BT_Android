package com.example.midtest.Ativity;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.midtest.Adapter.ProductAdapter;
import com.example.midtest.Model.SanPham;
import com.example.midtest.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Exercise8Fragment extends Fragment {

    EditText edtNhapThongBao, edtTenSP, edtGiaSP;
    Button btnThongBao, btnThem;
    ListView lv;

    List<SanPham> data;
    ProductAdapter adapter;
    View view;
    Context context ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_exercise8, container, false);
        setControl();
        setEvent();
        return view;
    }

    private void setEvent() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        DatabaseReference dataSP = database.getReference("sanpham");

        // Initialize adapter
        adapter = new ProductAdapter(getActivity(), R.layout.layout_item_product, (ArrayList<SanPham>) data);;
        lv.setAdapter(adapter);

        btnThongBao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.setValue(edtNhapThongBao.getText().toString());
            }
        });
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String maSP = dataSP.push().getKey();
                SanPham sanPham = new SanPham();
                sanPham.setMaSP(maSP);
                sanPham.setTenSP(edtTenSP.getText().toString());
                sanPham.setGiaSP(edtGiaSP.getText().toString());
                sanPham.setLoaiSP("samsung");
                dataSP.child(maSP).setValue(sanPham);
                Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();

            }
        });

        dataSP.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    SanPham sp = new SanPham();
                    sp.setMaSP(item.child("maSP").getValue().toString());
                    sp.setTenSP(item.child("tenSP").getValue().toString());
                    sp.setGiaSP(item.child("giaSP").getValue().toString());
                    sp.setLoaiSP(item.child("loaiSP").getValue().toString());
                    data.add(sp);
                }
                adapter.notifyDataSetChanged(); // Notify adapter of changes
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myRef.setValue("Hello, World!");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                Toast.makeText(getContext(), value, Toast.LENGTH_SHORT).show();
                edtNhapThongBao.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setControl() {
        context=getContext();
        edtNhapThongBao = view.findViewById(R.id.edtNhapThongBao);
        btnThongBao = view.findViewById(R.id.btnThongBao);
        edtTenSP = view.findViewById(R.id.edtTenSP);
        edtGiaSP = view.findViewById(R.id.edtGiaSP);
        btnThem = view.findViewById(R.id.btnThem);
        lv = view.findViewById(R.id.lv);
        data = new ArrayList<>();
    }
}