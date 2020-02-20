package com.allanlin97gmail.sanal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton addClientFab;
    ArrayList<ClientItem> clientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addClientFab = findViewById(R.id.addClientFab);

        addClientFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogBox();
                //showDiag();
            }
        });

        // ArrayList for client info
        clientList = new ArrayList<>();
        clientList.add(new ClientItem("Bob", "bob@gmail.com","5199838383","4514 Dougall"));
        clientList.add(new ClientItem("2", "2","2","2"));
        clientList.add(new ClientItem("as", "asd","asd","sda"));

        recyclerView = findViewById(R.id.clientRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new ClientAdapter(clientList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
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


