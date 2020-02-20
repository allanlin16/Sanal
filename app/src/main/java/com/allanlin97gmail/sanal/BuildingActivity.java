package com.allanlin97gmail.sanal;

import androidx.appcompat.app.AppCompatActivity;
import com.github.clans.fab.FloatingActionButton;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class BuildingActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    FloatingActionButton addBuildingFab, addExtinguisherFab, generatePDFFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);

        addBuildingFab = findViewById(R.id.addBuildingFab);
        addExtinguisherFab = findViewById(R.id.addExtinguisherFab);
        generatePDFFab = findViewById(R.id.generatePDFFab);

        //when add building fab clicked open form for adding building
        addBuildingFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildingDialogBox();

            }
        });

        addExtinguisherFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                extinguisherDialogBox();
            }
        });

        generatePDFFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdfDialogBox();
            }
        });

        //set the expanablelistview
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

    // opens dialog box adding building form
    public void buildingDialogBox() {

        final View dialogView = View.inflate(this,R.layout.add_building,null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Add Building");



        // set up the edit text
        final EditText buildingName = dialogView.findViewById(R.id.addBuildingName);
        final EditText buildingAddress = dialogView.findViewById(R.id.addBuildingAddress);
        final EditText buildingCity = dialogView.findViewById(R.id.addBuildingCity);
        final EditText buildingPostalCode = dialogView.findViewById(R.id.addBuildingPostalCode);

        //final EditText extinguisherArea = dialogView.findViewById(R.id.addExtinguisherArea);
        //final EditText extinguisherLocation = dialogView.findViewById(R.id.addExtinguisherLocation);

        alertDialogBuilder.setPositiveButton("Create",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        buildingName.getText().toString().trim().length();
                        buildingAddress.getText().toString().trim().length();
                        buildingCity.getText().toString().trim().length();
                        buildingPostalCode.getText().toString().trim().length();

                        //String extinguisherChild = extinguisherArea.getText().toString() + " " + extinguisherLocation.getText().toString();


                        expandableListTitle.add(buildingName.getText().toString());
                       List<String> buildingExtinguisher = new ArrayList<String>();

                        //buildingExtinguisher.add(extinguisherChild);
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

    public void extinguisherDialogBox() {
        final View dialogView = View.inflate(this,R.layout.add_extinguisher,null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Add Extinguisher");

        final EditText makeEditText, serialNumberEditText, barcodeEditText, areaEditText, locationEditText,
                mDateEditText, hDateEditText, sDateEditText, nSDateEditText, commentEditText;
        final Spinner typeSpinner, ratingSpinner, statusSpinner;
        final Button photoButtton;
        final ImageView extinguisherImageView;

        final Calendar myCalendar = Calendar.getInstance();

        String[] typeSpinnerArray = new String[] {
                "Water", "Foam", "Dry Powder", "CO2", "Wet Chemical"
        };

        String[] ratingSpinnerArray = new String[] {
                "A", "B", "C", "ABC"
        };

        String[] statusSpinnerArray = new String[] {
                "Passed", "Failed"
        };

        makeEditText= dialogView.findViewById(R.id.addMakeEditText);
        serialNumberEditText = dialogView.findViewById(R.id.addSerialNumberEditText);
        barcodeEditText = dialogView.findViewById(R.id.addBarcodeEditText);
        areaEditText = dialogView.findViewById(R.id.addAreaEditText);
        locationEditText = dialogView.findViewById(R.id.addLocationEditText);
        //Dates
        mDateEditText = dialogView.findViewById(R.id.addMDateEditText);
        hDateEditText = dialogView.findViewById(R.id.addHDateEditText);
        sDateEditText = dialogView.findViewById(R.id.addSDateEditText);
        nSDateEditText = dialogView.findViewById(R.id.addNSDateEditText);
        commentEditText = dialogView.findViewById(R.id.addCommentEditText);

        typeSpinner = dialogView.findViewById(R.id.addTypeSpinner);
        ratingSpinner = dialogView.findViewById(R.id.addRatingSpinner);
        statusSpinner = dialogView.findViewById(R.id.addStatusSpinner);

        photoButtton = dialogView.findViewById(R.id.addPhotoButton);
        extinguisherImageView = dialogView.findViewById(R.id.addExtinguisherImageView);

        //set the spinner array
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, typeSpinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, ratingSpinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ratingSpinner.setAdapter(adapter2);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, statusSpinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter3);

        final DatePickerDialog.OnDateSetListener mDate = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int  dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                mDateEditText.setText(sdf.format(myCalendar.getTime()));
            }
        };

        final DatePickerDialog.OnDateSetListener hDate = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int  dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                hDateEditText.setText(sdf.format(myCalendar.getTime()));
            }
        };

        final DatePickerDialog.OnDateSetListener sDate = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int  dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                sDateEditText.setText(sdf.format(myCalendar.getTime()));
            }
        };

        final DatePickerDialog.OnDateSetListener nSDate = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int  dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                nSDateEditText.setText(sdf.format(myCalendar.getTime()));
            }
        };


        mDateEditText.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    new DatePickerDialog(BuildingActivity.this, mDate,
                            myCalendar.get(Calendar.YEAR),
                            myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
                return true;
            }
        });

        hDateEditText.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    new DatePickerDialog(BuildingActivity.this, hDate,
                            myCalendar.get(Calendar.YEAR),
                            myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
                return true;
            }
        });

        sDateEditText.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    new DatePickerDialog(BuildingActivity.this, sDate,
                            myCalendar.get(Calendar.YEAR),
                            myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
                return true;
            }
        });

        nSDateEditText.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    new DatePickerDialog(BuildingActivity.this, nSDate,
                            myCalendar.get(Calendar.YEAR),
                            myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
                return true;
            }
        });


        alertDialogBuilder.setPositiveButton("Create",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        System.out.println("hi");

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

    public void pdfDialogBox() {
        final View dialogView = View.inflate(this,R.layout.generate_pdf,null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Select Building to Generate PDF");

        Spinner buildingPDFSPinner = dialogView.findViewById(R.id.buildingPDFSpinner);


        alertDialogBuilder.setPositiveButton("Create",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                        System.out.println("hi");

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
