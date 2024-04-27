package com.example.midtest.Ativity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.midtest.Adapter.CustomAdapter;
import com.example.midtest.Model.User;
import com.example.midtest.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Exercise7Fragment extends Fragment {
    ListView lvDanhSach;
    ArrayList<User> data = new ArrayList<>();
    SearchView searchView;
    CustomAdapter adapter;
    View view;
    Context context ;

    List<User> data_all=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_exercise7, container, false);
        setControl();
        setEvent();
        return view;
    }

    private void setEvent() {
        DocDL();
        adapter = new CustomAdapter((MainActivity) getContext(), R.layout.layout_item_user, data);
        lvDanhSach.setAdapter(adapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                data.clear();
                if(newText.equals("")) {
                    data.addAll(data_all);
                } else {
                    for (User user : data_all) {
                        if (user.getLogin().contains(newText)) {
                            data.add(user);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        DocDL();
    }

        private void DocDL() {
        data.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String url = "https://api.github.com/users";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                        for (int i = 0; i < response.length(); i++) {
                                JSONObject item = response.getJSONObject(i);
                                User user = new User();
                                user.setLogin(item.getString("login"));
                                user.setAvatar(item.getString("avatar_url"));
                                user.setUrl(item.getString("url"));
                                data.add(user);
                                data_all.add(user);
                        } } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error loading data", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(request);
    }

//    private void DocDL() {
//        data.clear();
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        String url = "https://api.github.com/search/users?q=mojombo";
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            String total_count=response.getString("total_count");
//                            Toast.makeText(getContext(), "Total account: "+total_count, Toast.LENGTH_LONG).show();
//                            JSONArray items = response.getJSONArray("items");
//                            for (int i = 0; i < items.length(); i++) {
//                                JSONObject item = items.getJSONObject(i);
//                                User user = new User();
//                                user.setLogin(item.getString("login"));
//                                user.setAvatar(item.getString("avatar_url"));
//                                user.setUrl(item.getString("html_url"));
//                                data.add(user);
//                                data_all.add(user);
//                            }
//                            adapter.notifyDataSetChanged();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getContext(), "Error loading data", Toast.LENGTH_SHORT).show();
//                    }
//                });
//        requestQueue.add(request);
//    }

    private void setControl() {
        context=getContext();
        lvDanhSach = view.findViewById(R.id.lvDanhSach);
        searchView = view.findViewById(R.id.sV);
    }
}