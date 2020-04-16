package com.allanlin97.ui.extinguisher;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProviders;
import androidx.loader.content.CursorLoader;

import com.allanlin97.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
    long extinguisher_id;
    Bitmap bitmap;
    String photoUrl2;

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

        // get extinguisher id from the building fragment
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            extinguisher_id = bundle.getLong("extinguisher_id");

        }

        requestQueue = Volley.newRequestQueue(getContext());

        // spinner values
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


                // PUT Request for extinguisher we check the extinguisher id
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Enter the correct url for your api service site
                String url = "https://alin.scweb.ca/SanalAPI/api/extinguisher/"+extinguisher_id;
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, object,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(getContext(), "Extinguisher Updated!", Toast.LENGTH_LONG).show();

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
        String url = "https://alin.scweb.ca/SanalAPI/api/extinguisher/"+extinguisher_id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        JSONObject jsonResponse = null;
                        try {
                            jsonResponse = response.getJSONObject("data");
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
                            String photo = jsonResponse.getString("extinguisher_photourl");

                            // set the edit text from get request response
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

                            photoUrl2 = "https://alin.scweb.ca/SanalAPI/"+photo;

                            System.out.println(photoUrl2);
                            Picasso.get().load(photoUrl2).into(imageView);

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

        if (bitmap != null) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();


            final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            JSONObject object = new JSONObject();
            try {
                object.put("file", byteArray);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            String url2 = "https://alin.scweb.ca/SanalAPI/api/extinguisher/" + extinguisher_id + "/file-upload";
            JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.POST, url2, object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(getContext(), "Photo Uploaded", Toast.LENGTH_LONG).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }

            });
            requestQueue.add(jsonObjectRequest2);
        }

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

    // Camera Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 7 && resultCode == RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);

            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = getImageUri(getContext(), bitmap);
            String filePath = getPath(tempUri);

            String url = "https://alin.scweb.ca/SanalAPI/api/extinguisher/" + extinguisher_id + "/file-upload";

            SimpleMultiPartRequest request = new SimpleMultiPartRequest(url, new File(filePath), new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    ;
                }
            });

            RequestQueue mRequestQueue = Volley.newRequestQueue(getContext());
            mRequestQueue.add(request);
            mRequestQueue.start();

        }
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    //Set Image path
    private String getPath(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
}