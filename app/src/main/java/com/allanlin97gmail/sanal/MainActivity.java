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
import android.widget.ImageView;
import android.widget.PopupMenu;

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
                //showDiag();
            }
        });

        final View inflatedView = getLayoutInflater().inflate(R.layout.client_item, null);
        final ImageView editDeleteMenu =  inflatedView.findViewById(R.id.menuButton);

        editDeleteMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("d", "asd");
            }
        });

        jsonParse();


        // ArrayList for client info
        //clientList = new ArrayList<>();
//        clientList.add(new ClientItem("Bob", "bob@example.com","5190000000","4514 Example St"));
//        clientList.add(new ClientItem("Allan", "allan@example.com","5190000000","4514 Example Ave"));

        recyclerView = findViewById(R.id.clientRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
//        adapter = new ClientAdapter(clientList);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(adapter);

    }

    private void jsonParse() {
        String url = "https://alin.scweb.ca/SanalAPI/client";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject client = jsonArray.getJSONObject(i);

                                String name = client.getString("client_name");
                                String email = client.getString("client_email");
                                String phone = client.getString("client_phone");
                                String address = client.getString("client_address");

                                Log.d("sdf", name + email + phone + address);


                                clientList = new ArrayList<>();
                                clientList.add(new ClientItem(name, email, phone, address));
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

    public void editDeleteMenu(View view) {

        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.edit_delete_menu, popup.getMenu());


        popup.show();

    }


    public void dialogBox() {

        final View dialogView = View.inflate(this,R.layout.add_client,null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Add Client");

        // set up the edit text
        final EditText name = (EditText) dialogView.findViewById(R.id.addName);
        final EditText address = (EditText) dialogView.findViewById(R.id.addAddress);
        final EditText phone = (EditText) dialogView.findViewById(R.id.addPhone);
        final EditText email = (EditText) dialogView.findViewById(R.id.addEmail);

        alertDialogBuilder.setPositiveButton("Create",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        name.getText().toString().trim().length();
                        address.getText().toString().trim().length();
                        phone.getText().toString().trim().length();
                        email.getText().toString().trim().length();

                        ClientItem clientItem = new ClientItem(name.getText().toString(), address.getText().toString(), phone.getText().toString(), email.getText().toString());
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


