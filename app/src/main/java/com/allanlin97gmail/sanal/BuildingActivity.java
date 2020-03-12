package com.allanlin97gmail.sanal;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.clans.fab.FloatingActionButton;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    ImageView extinguisherImageView;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);

        requestQueue = Volley.newRequestQueue(this);
        addBuildingFab = findViewById(R.id.addBuildingFab);
        addExtinguisherFab = findViewById(R.id.addExtinguisherFab);
        generatePDFFab = findViewById(R.id.generatePDFFab);

        //add back button
        getSupportActionBar().setHomeButtonEnabled(true);

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

        getBuilding();

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

                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        JSONObject object = new JSONObject();
                        try {
                            //input your API parameters
                            object.put("building_name", buildingName.getText().toString());
                            object.put("building_address", buildingAddress.getText().toString());
                            object.put("building_city", buildingCity.getText().toString());
                            object.put("building_postalcode", buildingPostalCode.getText().toString());
                            object.put("client_id",6);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Enter the correct url for your api service site
                        String url = "https://alin.scweb.ca/SanalAPI/building?client_id=6";
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Toast.makeText(getApplicationContext(), "Building Created!", Toast.LENGTH_LONG).show();
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        });
                        requestQueue.add(jsonObjectRequest);

                        //String extinguisherChild = extinguisherArea.getText().toString() + " " + extinguisherLocation.getText().toString();


//                        expandableListTitle.add(buildingName.getText().toString() + buildingAddress.getText().toString() + buildingCity.getText().toString());
//                       List<String> buildingExtinguisher = new ArrayList<>();
//                       buildingExtinguisher.add("ggg");
//
//                        //buildingExtinguisher.add(extinguisherChild);
//                        expandableListDetail.put(buildingName.getText().toString(), buildingExtinguisher);

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
        final Spinner typeSpinner, ratingSpinner, statusSpinner, buildingSpinner;
        final Button photoButtton;
        //final ImageView extinguisherImageView;

        final Calendar myCalendar = Calendar.getInstance();

        String[] buildingSpinnerArray = new String[] {
                "Building 1", "Building 2"
        };

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
        buildingSpinner = dialogView.findViewById(R.id.selectBuildingSpinner);

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

        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, buildingSpinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buildingSpinner.setAdapter(adapter4);

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

        photoButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);


                startActivityForResult(intent, 7);
            }
        });



        alertDialogBuilder.setPositiveButton("Create",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        JSONObject object = new JSONObject();
                        try {
                            //input your API parameters
                            object.put("extinguisher_make", makeEditText.getText().toString());
                            object.put("extinguisher_serialnumber",serialNumberEditText.getText().toString());
                            object.put("extinguisher_barcodenumber",barcodeEditText.getText().toString());
                            object.put("extinguisher_locationarea",areaEditText.getText().toString());
                            object.put("extinguisher_locationdescription",locationEditText.getText().toString());
                            object.put("extinguisher_type",typeSpinner.getSelectedItem().toString());
                            object.put("extinguisher_rating",ratingSpinner.getSelectedItem().toString());
                            object.put("extinguisher_manufacturedate",mDateEditText.getText().toString());
                            object.put("extinguisher_htestdate",hDateEditText.getText().toString());
                            object.put("extinguisher_servicedate",sDateEditText.getText().toString());
                            object.put("extinguisher_nextservicedate",nSDateEditText.getText().toString());
                            object.put("extinguisher_status",statusSpinner.getSelectedItem().toString());
                            object.put("extinguisher_comment",commentEditText.getText().toString());
                            object.put("extinguisher_photourl","photo");
                            object.put("building_id",6);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Enter the correct url for your api service site
                        String url = "https://alin.scweb.ca/SanalAPI/extinguisher?building_id=6";
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Toast.makeText(getApplicationContext(), "Extinguisher Created!", Toast.LENGTH_LONG).show();
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        });
                        requestQueue.add(jsonObjectRequest);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 7 && resultCode == RESULT_OK) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            extinguisherImageView.setImageBitmap(bitmap);
            extinguisherImageView.setRotation(90);
        }
    }

    public void pdfDialogBox() {
        final View dialogView = View.inflate(this,R.layout.generate_pdf,null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Select Building to Generate PDF");

        Spinner buildingPDFSPinner = dialogView.findViewById(R.id.buildingPDFSpinner);

        String[] statusSpinnerArray = new String[] {
                "Building 1", "Building 2"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, statusSpinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buildingPDFSPinner.setAdapter(adapter);


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

    private void getBuilding() {
        String url = "https://alin.scweb.ca/SanalAPI/building?client_id=6";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject building = jsonArray.getJSONObject(i);

                                Long id = building.getLong("id");
                                String building_name = building.getString("building_name");
                                String building_email = building.getString("building_address");
                                String building_phone = building.getString("building_city");
                                String building_address = building.getString("building_postalcode");

                                Log.d("asd", building_name);
                                expandableListTitle.add(building_name);

//                                List<String> buildingExtinguisher = new ArrayList<>();
//                                buildingExtinguisher.add("ggg");
//                                expandableListDetail.put(building_name, buildingExtinguisher);


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
}
