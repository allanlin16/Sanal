package com.allanlin97;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class LoginInActivity extends AppCompatActivity {


    TextView registerTextView;
    EditText email, password;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_in);

        requestQueue = Volley.newRequestQueue(this);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        // store get user access token in the shared preference
        SharedPreferences preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String username = preferences.getString("TOKEN",null);

        // check shared preference if not null then sign in user
        if (username != null ) {
            Intent myIntent = new Intent(LoginInActivity.this, MainActivity.class);
            startActivity(myIntent);
        }


        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = "https://alin.scweb.ca/SanalAPI/api/auth/login";
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                // response
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String token = jsonObject.getString("access_token");

                                    // store get user access token in the shared preference
                                    SharedPreferences preferences = getApplication().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                                    preferences.edit().putString("TOKEN",token).apply();

                                    if (preferences != null) {
                                        // if user is not null we auto sign them in
                                        Intent myIntent = new Intent(LoginInActivity.this, MainActivity.class);
                                        startActivity(myIntent);
                                    } else {
                                        Toast.makeText(getBaseContext(), "Please Log in", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getBaseContext(), "Please Log in", Toast.LENGTH_LONG).show();
                                }


                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "Invalid Account", Toast.LENGTH_LONG).show();

                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String>  params = new HashMap<>();
                        params.put("email", email.getText().toString());
                        params.put("password", password.getText().toString());

                        return params;
                    }
                };
                requestQueue.add(postRequest);

            }
        });


        registerTextView = findViewById(R.id.registerAccount);

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerView();
            }
        });


    }

    public void registerView() {

        final View dialogView = View.inflate(this,R.layout.register_user,null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Register for Account");

        // set up the edit text
        final EditText name = dialogView.findViewById(R.id.registerName);
        final EditText email = dialogView.findViewById(R.id.registerEmail);
        final EditText password = dialogView.findViewById(R.id.registerPassword);
        final EditText confirmPassword = dialogView.findViewById(R.id.registerConfirmPassword);


        alertDialogBuilder.setPositiveButton("Register",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        String url = "https://alin.scweb.ca/SanalAPI/api/auth/signup";
                        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>()
                                {
                                    @Override
                                    public void onResponse(String response) {

                                        Toast.makeText(getApplicationContext(), "Account Created", Toast.LENGTH_LONG).show();
                                    }

                                },
                                new Response.ErrorListener()
                                {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getApplicationContext(), "Invalid Credentials, Please Try Again", Toast.LENGTH_LONG).show();

                                    }
                                }
                        ) {
                            @Override
                            protected Map<String, String> getParams()
                            {
                                Map<String, String>  params = new HashMap<>();
                                params.put("name", name.getText().toString());
                                params.put("email", email.getText().toString());
                                params.put("password", password.getText().toString());
                                params.put("password_confirmation", confirmPassword.getText().toString());
                                return params;
                            }
                        };
                        requestQueue.add(postRequest);


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


