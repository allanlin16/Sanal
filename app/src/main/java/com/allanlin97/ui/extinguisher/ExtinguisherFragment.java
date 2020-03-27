package com.allanlin97.ui.extinguisher;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.allanlin97.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class ExtinguisherFragment extends Fragment {

    private ExtinguisherViewModel extinguisherViewModel;
    final Calendar myCalendar = Calendar.getInstance();
    EditText makeEditText, serialNumberEditText, barcodeEditText, areaEditText, locationEditText, commentEditText;
    //dates
    EditText mDateEditText, hDateEditText, sDateEditText, nSDateEditText;
    ImageView imageView;
    Button photoButton, updateButton;
    public  static final int RequestPermissionCode  = 1 ;
    Spinner type, rating, status;
    RequestQueue requestQueue;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        extinguisherViewModel = ViewModelProviders.of(this).get(ExtinguisherViewModel.class);
        View root = inflater.inflate(R.layout.fragment_extinguisher, container, false);
//        final TextView textView = root.findViewById(R.id.);
//        extinguisherViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        requestQueue = Volley.newRequestQueue(getContext());

        final String[] typeSpinner = new String[] {
                "Water", "Foam", "Dry Powder", "CO2", "Wet Chemical"
        };

        String[] ratingSpinner = new String[] {
                "A", "B", "C", "ABC"
        };

        String[] statusSpinner = new String[] {
                "Passed", "Failed"
        };

        type = root.findViewById(R.id.extinguisherType);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, typeSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);

        rating = root.findViewById(R.id.extinguisherRating);
        final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, ratingSpinner);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rating.setAdapter(adapter2);

        status = root.findViewById(R.id.extinguisherStatus);
        final ArrayAdapter<String> adapter3 = new ArrayAdapter<String> (getContext(),
                android.R.layout.simple_spinner_item, statusSpinner);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status.setAdapter(adapter3);

        makeEditText = root.findViewById(R.id.extingMake);
        serialNumberEditText = root.findViewById(R.id.extinguisherSerialNumber);
        barcodeEditText = root.findViewById(R.id.extinguisherBarcode);
        areaEditText = root.findViewById(R.id.extinguisherArea);
        locationEditText = root.findViewById(R.id.extinguisherLocation);
        commentEditText = root.findViewById(R.id.extinguisherComment);
        //dates
        mDateEditText = root.findViewById(R.id.extinguisherMDate);
        hDateEditText = root.findViewById(R.id.extinguisherHDate);
        sDateEditText = root.findViewById(R.id.extinguisherSDate);
        nSDateEditText = root.findViewById(R.id.extinguisherNSDate);

        imageView = root.findViewById(R.id.imageView1);
        photoButton = root.findViewById(R.id.updatePhotoButton);
        updateButton = root.findViewById(R.id.updateExtinguisherButton);

        imageView.setRotation(90);

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(intent, 7);

            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                JSONObject object = new JSONObject();
                try {
                    //input your API parameters
                    object.put("extinguisher_make", makeEditText.getText().toString());
                    object.put("extinguisher_serialnumber", serialNumberEditText.getText().toString());
                    object.put("extinguisher_barcodenumber", barcodeEditText.getText().toString());
                    object.put("extinguisher_locationarea", areaEditText.getText().toString());
                    object.put("extinguisher_locationdescription", locationEditText.getText().toString());
                    object.put("extinguisher_type", type.getSelectedItem().toString());
                    object.put("extinguisher_rating", rating.getSelectedItem().toString());
                    object.put("extinguisher_manufacturedate", mDateEditText.getText().toString());
                    object.put("extinguisher_htestdate", hDateEditText.getText().toString());
                    object.put("extinguisher_servicedate", sDateEditText.getText().toString());
                    object.put("extinguisher_nextservicedate", nSDateEditText.getText().toString());
                    object.put("extinguisher_comment", commentEditText.getText().toString());
                    object.put("extinguisher_status", status.getSelectedItem().toString());
                    object.put("extinguisher_photourl", "gg");
                    object.put("building_id",6);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Enter the correct url for your api service site
                String url = "https://alin.scweb.ca/SanalAPI/api/extinguisher/9";
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, object,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(getContext(), "Extinguisher Updated!", Toast.LENGTH_LONG).show();
                                //TODO: update the layout

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

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int  dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int  dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateHLabel();
            }
        };

        final DatePickerDialog.OnDateSetListener date3 = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int  dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateSLabel();
            }
        };

        final DatePickerDialog.OnDateSetListener date4 = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int  dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateNSLabel();
            }
        };

        mDateEditText.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    new DatePickerDialog(getContext(), date,
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
                    new DatePickerDialog(getContext(), date2,
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
                    new DatePickerDialog(getContext(), date3,
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
                    new DatePickerDialog(getContext(), date4,
                            myCalendar.get(Calendar.YEAR),
                            myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
                return true;
            }
        });


        // GET request for getting the extinguisher
        String url = "https://alin.scweb.ca/SanalAPI/api/extinguisher?building_id=6";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject extinguisher = jsonArray.getJSONObject(i);

                                //Long id = extinguisher.getLong("id");
                                String make = extinguisher.getString("extinguisher_make");
                                String serialNumber = extinguisher.getString("extinguisher_serialnumber");
                                String barcode = extinguisher.getString("extinguisher_barcodenumber");
                                String area = extinguisher.getString("extinguisher_locationarea");
                                String location = extinguisher.getString("extinguisher_locationdescription");
                                String typeValue = extinguisher.getString("extinguisher_type");
                                String ratingValue = extinguisher.getString("extinguisher_rating");
                                String mDate = extinguisher.getString("extinguisher_manufacturedate");
                                String hDate = extinguisher.getString("extinguisher_htestdate");
                                String sDate = extinguisher.getString("extinguisher_servicedate");
                                String nSDate = extinguisher.getString("extinguisher_nextservicedate");
                                String statusValue = extinguisher.getString("extinguisher_status");
                                String comment = extinguisher.getString("extinguisher_comment");
                                String photoUrl = extinguisher.getString("extinguisher_photourl");

                                makeEditText.setText(make);
                                serialNumberEditText.setText(serialNumber);
                                barcodeEditText.setText(barcode);
                                areaEditText.setText(area);
                                locationEditText.setText(location);
                                int hold = adapter.getPosition(typeValue);//finding value in spinner list
                                type.setSelection(hold);//setting spinner
                                int hold2 = adapter2.getPosition(ratingValue);
                                rating.setSelection(hold2);
                                mDateEditText.setText(mDate);
                                hDateEditText.setText(hDate);
                                sDateEditText.setText(sDate);
                                nSDateEditText.setText(nSDate);
                                int hold3 = adapter3.getPosition(statusValue);
                                status.setSelection(hold3);
                                commentEditText.setText(comment);

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

        return root;
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mDateEditText.setText(sdf.format(myCalendar.getTime()));

    }

    private void updateHLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        hDateEditText.setText(sdf.format(myCalendar.getTime()));

    }

    private void updateSLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        sDateEditText.setText(sdf.format(myCalendar.getTime()));

    }

    private void updateNSLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        nSDateEditText.setText(sdf.format(myCalendar.getTime()));

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 7 && resultCode == RESULT_OK) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            imageView.setImageBitmap(bitmap);
        }


    }
}