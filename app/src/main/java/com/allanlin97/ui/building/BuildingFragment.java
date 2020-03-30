package com.allanlin97.ui.building;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.allanlin97.BuildingItem;
import com.allanlin97.CustomExpandableListAdapter;
import com.allanlin97.ExtinguisherItem;
import com.allanlin97.R;
import com.allanlin97.ui.extinguisher.ExtinguisherFragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.github.clans.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class BuildingFragment extends Fragment {

    private BuildingViewModel buildingViewModel;
    ExpandableListView expandableListView;
    CustomExpandableListAdapter expandableListAdapter;

    List<BuildingItem> buildingDetails;
    HashMap<Long, List<ExtinguisherItem>> buildingIdExtinguisher;

    FloatingActionButton addBuildingFab, addExtinguisherFab, generatePDFFab;
    ImageView extinguisherImageView;
    RequestQueue requestQueue;
    Bundle bundle;
    long selectedBuildingId;
    ExtinguisherItem extinguisherItem;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        buildingViewModel = ViewModelProviders.of(this).get(BuildingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_building, container, false);

        final TextView textView = root.findViewById(R.id.recent_title_label);
        buildingViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        requestQueue = Volley.newRequestQueue(getContext());
        addBuildingFab = root.findViewById(R.id.addBuildingFab);
        addExtinguisherFab = root.findViewById(R.id.addExtinguisherFab);
        generatePDFFab = root.findViewById(R.id.generatePDFFab);


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
        expandableListView = root.findViewById(R.id.expandableListView);
        //expandableListDetail = ExpandableListViewData.getData();

        final List<BuildingItem> buildingItems = new ArrayList<>();


        buildingDetails = new ArrayList(buildingItems);
        buildingIdExtinguisher = new HashMap<Long, List<ExtinguisherItem>>();

        expandableListAdapter = new CustomExpandableListAdapter(getContext(), buildingDetails, buildingIdExtinguisher);
        expandableListView.setAdapter(expandableListAdapter);

        bundle = this.getArguments();
        if (bundle != null) {
            final long clientId = bundle.getLong("clientId");
            final String nameString = bundle.getString("clientName");

            buildingViewModel.getText().observe(this, new Observer<String>() {
                @Override
                public void onChanged(@Nullable String s) {
                    textView.setText(nameString + "'s Buildings");
                }
            });

            String url = "https://alin.scweb.ca/SanalAPI/api/building?client_id="+clientId;

            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject building = jsonArray.getJSONObject(i);

                                    Long id = building.getLong("id");
                                    final String building_name = building.getString("building_name");
                                    final String building_address = building.getString("building_address");
                                    final String building_city = building.getString("building_city");
                                    final String building_postalcode = building.getString("building_postalcode");

                                    final BuildingItem buildingItem = new BuildingItem(id, building_name, building_address, building_city, building_postalcode);
                                    // add the building object
                                    buildingDetails.add(buildingItem);

                                    String extinguisherUrl ="https://alin.scweb.ca/SanalAPI/api/extinguisher?building_id="+id;

                                    // Formulate the request and handle the response.
                                    JsonObjectRequest extinguisherRequest = new JsonObjectRequest(Request.Method.GET, extinguisherUrl, null,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {

                                                    try {
                                                        JSONArray jsonArray = response.getJSONArray("data");
                                                        List<ExtinguisherItem> buildingExtinguisher = new ArrayList<>();
                                                        for (int i = 0; i < jsonArray.length(); i++) {
                                                            JSONObject jsonResponse = jsonArray.getJSONObject(i);
                                                            Long id = jsonResponse.getLong("id");
                                                            String make = jsonResponse.getString("extinguisher_make");
                                                            String serialNumber = jsonResponse.getString("extinguisher_serialnumber");
                                                            String barcode = jsonResponse.getString("extinguisher_barcodenumber");
                                                            String area = jsonResponse.getString("extinguisher_locationarea");
                                                            String location = jsonResponse.getString("extinguisher_locationdescription");
                                                            String typeValue = jsonResponse.getString("extinguisher_type");
                                                            String ratingValue = jsonResponse.getString("extinguisher_rating");
                                                            String mDate = jsonResponse.getString("extinguisher_manufacturedate");
                                                            String hDate = jsonResponse.getString("extinguisher_htestdate");
                                                            String sDate = jsonResponse.getString("extinguisher_servicedate");
                                                            String nSDate = jsonResponse.getString("extinguisher_nextservicedate");
                                                            String statusValue = jsonResponse.getString("extinguisher_status");
                                                            String comment = jsonResponse.getString("extinguisher_comment");
                                                            String photoUrl = jsonResponse.getString("extinguisher_photourl");

                                                            ExtinguisherItem extinguisherItem = new ExtinguisherItem(id, make,
                                                                    serialNumber, barcode, area, location, typeValue, ratingValue,
                                                                    mDate, hDate, sDate, nSDate, statusValue, comment, photoUrl);


                                                            buildingExtinguisher.add(extinguisherItem);
                                                        }
                                                        //add the building and with extinguisher object
                                                        buildingIdExtinguisher.put(Long.valueOf(buildingItem.getId()), buildingExtinguisher);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    // Handle error
                                                }
                                            });
                                    requestQueue.add(extinguisherRequest);
                                }
                                expandableListAdapter.notifyDataSetChanged();
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

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                ExtinguisherFragment myFragment = new ExtinguisherFragment();

                Bundle bundle = new Bundle();
                long buildingId = buildingDetails.get(groupPosition).getId();
                List<ExtinguisherItem> eItems = buildingIdExtinguisher.get(Long.valueOf(buildingId));
                bundle.putLong("extinguisher_id", eItems.get(childPosition).getId());
                myFragment.setArguments(bundle);

                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, myFragment)
                        .addToBackStack(null).commit();
                expandableListAdapter.notifyDataSetChanged();
                return false;
            }
        });

        return root;
    }

    // opens dialog box adding building form
    public void buildingDialogBox() {

        final View dialogView = View.inflate(getContext(),R.layout.add_building,null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("Add Building");

        // set up the edit text
        final EditText buildingName = dialogView.findViewById(R.id.addBuildingName);
        final EditText buildingAddress = dialogView.findViewById(R.id.addBuildingAddress);
        final EditText buildingCity = dialogView.findViewById(R.id.addBuildingCity);
        final EditText buildingPostalCode = dialogView.findViewById(R.id.addBuildingPostalCode);

        alertDialogBuilder.setPositiveButton("Create",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        JSONObject object = new JSONObject();
                        try {
                            //input your API parameters
                            final long clientId = bundle.getLong("clientId");
                            object.put("building_name", buildingName.getText().toString());
                            object.put("building_address", buildingAddress.getText().toString());
                            object.put("building_city", buildingCity.getText().toString());
                            object.put("building_postalcode", buildingPostalCode.getText().toString());
                            object.put("client_id", clientId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        final long clientId = bundle.getLong("clientId");
                        String url = "https://alin.scweb.ca/SanalAPI/api/building?client_id="+clientId;
                        System.out.println("" + clientId);
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Toast.makeText(getContext(), "Building Created!", Toast.LENGTH_LONG).show();
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

    public void extinguisherDialogBox() {
        final View dialogView = View.inflate(getContext(),R.layout.add_extinguisher,null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("Add Extinguisher");

        final EditText makeEditText, serialNumberEditText, barcodeEditText, areaEditText, locationEditText,
                mDateEditText, hDateEditText, sDateEditText, nSDateEditText, commentEditText;
        final Spinner typeSpinner, ratingSpinner, statusSpinner, buildingSpinner;
        final Button photoButtton;
        //final ImageView extinguisherImageView;

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
        buildingSpinner = dialogView.findViewById(R.id.selectBuildingSpinner);

        photoButtton = dialogView.findViewById(R.id.addPhotoButton);
        extinguisherImageView = dialogView.findViewById(R.id.addExtinguisherImageView);

        //set the spinner array
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, typeSpinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, ratingSpinnerArray);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ratingSpinner.setAdapter(adapter2);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, statusSpinnerArray);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter3);


        final ArrayAdapter<BuildingItem> adapter4 = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, buildingDetails);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buildingSpinner.setAdapter(adapter4);
        buildingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // get the building id
                selectedBuildingId = buildingDetails.get(i).getId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


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
                    new DatePickerDialog(getContext(), mDate,
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
                    new DatePickerDialog(getContext(), hDate,
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
                    new DatePickerDialog(getContext(), sDate,
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
                    new DatePickerDialog(getContext(), nSDate,
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
                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        JSONObject object = new JSONObject();
                        try {
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
                            object.put("building_id", selectedBuildingId);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Enter the correct url for your api service site
                        String url = "https://alin.scweb.ca/SanalAPI/api/extinguisher?building_id=48";
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Toast.makeText(getContext(), "Extinguisher Created!", Toast.LENGTH_LONG).show();
                                        //Todo: hashmap

                                        JSONObject jsonResponse = null;
                                        try {

                                            jsonResponse = response.getJSONObject("data");
                                            Long id = jsonResponse.getLong("id");

                                            extinguisherItem = new ExtinguisherItem(id, makeEditText.getText().toString(),
                                                    serialNumberEditText.getText().toString(),
                                                    barcodeEditText.getText().toString(),
                                                    areaEditText.getText().toString(),
                                                    locationEditText.getText().toString(),
                                                    typeSpinner.getSelectedItem().toString(),
                                                    ratingSpinner.getSelectedItem().toString(),
                                                    mDateEditText.getText().toString(),
                                                    hDateEditText.getText().toString(),
                                                    sDateEditText.getText().toString(),
                                                    nSDateEditText.getText().toString(),
                                                    statusSpinner.getSelectedItem().toString(),
                                                    commentEditText.getText().toString(),
                                                    "photo");

                                            //create array
                                            List<ExtinguisherItem> buildingExtinguisher = new ArrayList<>();
                                            buildingExtinguisher.add(extinguisherItem);

                                            buildingIdExtinguisher.put(selectedBuildingId, buildingExtinguisher);

                                            expandableListAdapter.notifyDataSetChanged();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
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

    public void pdfDialogBox() {
        final View dialogView = View.inflate(getContext(),R.layout.generate_pdf,null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("Select Building to Generate PDF");

        Spinner buildingPDFSPinner = dialogView.findViewById(R.id.buildingPDFSpinner);



        ArrayAdapter<BuildingItem> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, buildingDetails);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buildingPDFSPinner.setAdapter(adapter);

        buildingPDFSPinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedBuildingId = buildingDetails.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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


}