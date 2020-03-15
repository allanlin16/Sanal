package com.allanlin97gmail.sanal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.clans.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton addClientFab;
    ArrayList<ClientItem> clientList;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addClientFab = findViewById(R.id.addClientFab);
        requestQueue = Volley.newRequestQueue(this);

        addClientFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogBox();
            }
        });

        // ArrayList for client info
        clientList = new ArrayList<>();

        recyclerView = findViewById(R.id.clientRecyclerView);
        jsonParse();


        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new ClientAdapter(clientList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


    private void jsonParse() {
        String url = "https://alin.scweb.ca/SanalAPI/api/client?user_id=6";

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

                                clientList.add(new ClientItem(id, R.drawable.ic_more_vert_black_24dp, name, email, phone, address));

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



    // display the app bar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }


    // if menu is selected if opens the setting activity
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settingItem:
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    public void dialogBox() {

        final View dialogView = View.inflate(this,R.layout.add_client,null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
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

                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        JSONObject object = new JSONObject();
                        try {
                            //input your API parameters
                            object.put("client_name", name.getText().toString());
                            object.put("client_address",address.getText().toString());
                            object.put("client_phone",phone.getText().toString());
                            object.put("client_email",email.getText().toString());
                            object.put("user_id",6);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Enter the correct url for your api service site
                        String url = "https://alin.scweb.ca/SanalAPI/api/client?user_id=6";
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Toast.makeText(getApplicationContext(), "Client Created!", Toast.LENGTH_LONG).show();
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




