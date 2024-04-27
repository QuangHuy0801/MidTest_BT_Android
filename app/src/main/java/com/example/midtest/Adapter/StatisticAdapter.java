package com.example.midtest.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.midtest.Ativity.AgeGroupStatisticActivity;
import com.example.midtest.Ativity.MainActivity;
import com.example.midtest.Ativity.ProductsSoldActivity;
import com.example.midtest.Ativity.ProductsStatisticActivity;
import com.example.midtest.Ativity.StatisticsByMonthActivity;
import com.example.midtest.Ativity.UnitOfProductStatisticActivity;
import com.example.midtest.Model.Statistic;
import com.example.midtest.Ativity.RevenueStatisticActivity;
import com.example.midtest.R;

import java.util.ArrayList;

public class StatisticAdapter extends ArrayAdapter<Statistic> {
    Context context;
    int resource;
    ArrayList<Statistic> data;

    public StatisticAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Statistic> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    public int count()
    {
        return data.size();
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource,null);

        TextView title = convertView.findViewById(R.id.titleStatistic);
        TextView text = convertView.findViewById(R.id.textStatistic);
        ImageButton btnEdit = convertView.findViewById(R.id.btnEdit);

        Statistic entry = data.get(position);

        title.setText(entry.getTitle());
        text.setText(entry.getText());

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (entry.getId()){
                    case 1:
                        Intent intent1 = new Intent(context, RevenueStatisticActivity.class);
                        intent1.putExtra("detail", entry);
                        ((MainActivity)context).startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(context, ProductsSoldActivity.class);
                        intent2.putExtra("detail", entry);
                        ((MainActivity)context).startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(context, ProductsStatisticActivity.class);
                        intent3.putExtra("detail", entry);
                        ((MainActivity)context).startActivity(intent3);
                        break;
                    case 4:
                        Intent intent4 = new Intent(context, UnitOfProductStatisticActivity.class);
                        intent4.putExtra("detail", entry);
                        ((MainActivity)context).startActivity(intent4);
                        break;
                    case 5:
                        Intent intent5 = new Intent(context, AgeGroupStatisticActivity.class);
                        intent5.putExtra("detail", entry);
                        ((MainActivity)context).startActivity(intent5);
                        break;
                    case 6:
                        Intent intent6 = new Intent(context, StatisticsByMonthActivity.class);
                        intent6.putExtra("detail", entry);
                        ((MainActivity)context).startActivity(intent6);
                        break;
                    default:
                        break;
                }
            }
        });

        return convertView;
    }
}
