package com.allanlin97gmail.sanal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BuildingActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    FloatingActionButton addBuildingFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);

        addBuildingFab = findViewById(R.id.addBuildingFab);
        addBuildingFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBox();

            }
        });

        expandableListView = findViewById(R.id.expandableListView);
        expandableListDetail = ExpandableListViewData.getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);


        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                Intent intent = new Intent(BuildingActivity.this, ExtinguisherActivity.class);
                startActivity(intent);

                return false;
            }
        });
    }

    public void dialogBox() {

        final View dialogView = View.inflate(this,R.layout.add_building,null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Add Building");

        // set up the edit text
        final EditText buildingName = (EditText) dialogView.findViewById(R.id.addBuildingName);
        final EditText buildingAddress = (EditText) dialogView.findViewById(R.id.addBuildingAddress);
        final EditText buildingCity = (EditText) dialogView.findViewById(R.id.addBuildingCity);
        final EditText buildingPostalCode = (EditText) dialogView.findViewById(R.id.addBuildingPostalCode);

        final EditText extinguisherArea = (EditText) dialogView.findViewById(R.id.addExtinguisherArea);
        final EditText extinguisherLocation = (EditText) dialogView.findViewById(R.id.addExtinguisherLocation);

        alertDialogBuilder.setPositiveButton("Create",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        buildingName.getText().toString().trim().length();
                        buildingAddress.getText().toString().trim().length();
                        buildingCity.getText().toString().trim().length();
                        buildingPostalCode.getText().toString().trim().length();

                        String extinguisherChild = extinguisherArea.getText().toString() + " " + extinguisherLocation.getText().toString();


                        HashMap<String, List<String>> expandableListViewData = new HashMap<String, List<String>>();

                        expandableListTitle.add(buildingName.getText().toString());
                        List<String> buildingExtinguisher = new ArrayList<String>();

                        buildingExtinguisher.add(extinguisherChild);
                        expandableListDetail.put(buildingName.getText().toString(), buildingExtinguisher);

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
