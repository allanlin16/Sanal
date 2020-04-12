package com.allanlin97.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allanlin97.ClientAdapter;
import com.allanlin97.ClientItem;
import com.allanlin97.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.auth0.android.jwt.JWT;
import com.github.clans.fab.FloatingActionButton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton addClientFab;
    ArrayList<ClientItem> clientList;
    RequestQueue requestQueue;
    JWT jwt;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // get the acess token from the shared preferences
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);//second parameter default value.
        //create a new jwt
        jwt = new JWT(retrivedToken);

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        addClientFab =  root.findViewById(R.id.addClientFab);

        requestQueue = Volley.newRequestQueue(getContext());

        addClientFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogBox();
            }
        });

        // ArrayList for client info
        clientList = new ArrayList<>();

        //set up the recycler view
        recyclerView = root.findViewById(R.id.clientRecyclerView);
        jsonParse();


        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        adapter = new ClientAdapter(clientList);
        recyclerView.setLayoutManager(layoutManager);
        // add the client adapter
        recyclerView.setAdapter(adapter);

        final TextView textView = root.findViewById(R.id.recent_title_label);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        return root;
    }

    //
    private void jsonParse() {
        // local - http://api.sanalapi.test/api/client?user_id=10
        // scweb - https://alin.scweb.ca/SanalAPI/api/client?user_id=7

        //get request for clients realted to the user subject, so id
        String url = "https://alin.scweb.ca/SanalAPI/api/client?user_id="+jwt.getSubject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            clientList = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject client = jsonArray.getJSONObject(i);

                                Long id = client.getLong("id");
                                String name = client.getString("client_name");
                                String email = client.getString("client_email");
                                String phone = client.getString("client_phone");
                                String address = client.getString("client_address");

                                // create a new client object and add to list
                                clientList.add(new ClientItem(id, R.drawable.ic_more_vert_black_24dp, name, email, phone, address));

                                // add it to the recycler view
                                adapter = new ClientAdapter(clientList);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        error.printStackTrace();
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    // Method for adding the clients
    public void dialogBox() {

        final View dialogView = View.inflate(getContext(),R.layout.add_client,null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("Add Client");

        // set up the edit text
        final EditText name =  dialogView.findViewById(R.id.addName);
        final EditText address = dialogView.findViewById(R.id.addAddress);
        final EditText phone =  dialogView.findViewById(R.id.addPhone);
        final EditText email =  dialogView.findViewById(R.id.addEmail);

        alertDialogBuilder.setPositiveButton("Create",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        JSONObject object = new JSONObject();
                        try {
                            //input your API parameters
                            object.put("client_name", name.getText().toString());
                            object.put("client_address",address.getText().toString());
                            object.put("client_phone",phone.getText().toString());
                            object.put("client_email",email.getText().toString());
                            object.put("user_id",jwt.getSubject());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Enter the correct url for your api service site
                        //local http://api.sanalapi.test/api/client?user_id=10
                        // scweb = https://alin.scweb.ca/SanalAPI/api/client?user_id=7
                        String url = "https://alin.scweb.ca/SanalAPI/api/client?user_id="+jwt.getSubject();
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Toast.makeText(getContext(), "Client Created!", Toast.LENGTH_LONG).show();
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        });
                        requestQueue.add(jsonObjectRequest);

                        ClientItem clientItem = new ClientItem(1,R.drawable.ic_more_vert_black_24dp, name.getText().toString(), address.getText().toString(), phone.getText().toString(), email.getText().toString());
                        clientList.add(clientItem);
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setView(dialogView);
        alertDialog.show();
    }

}