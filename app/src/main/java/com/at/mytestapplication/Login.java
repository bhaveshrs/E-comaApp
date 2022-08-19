package com.at.mytestapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;

public class Login extends AppCompatActivity {


    // Declare the views from xml and variable that you are going to use globally

    EditText etusername,etpassword; // declaring variables of Edit text Views
    Button btnlogin;               // declaring variable of Button View
    ProgressBar pb;                // declaring variable of ProgressBar View
    ProgressDialog progressDialog;  // declaring variable of ProgressDialog View
    TextView tvreg,tvtitle;        // declaring variables of Text Views

    SharedPreferences sharedPreferences;  // declaring variables of Shared Preference
    SharedPreferences.Editor editor;      // declaring variables of Shared Preference Editor

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // your layout file


        // Text views for displaying the labels, titles and other details like name, email (not editable)
        // Edit text you can display the text through settext but also it is editable .
        //          Mainly used for getting the user input via keyboard
        // Buttons for click events



        // defining the views , linking them with their layout counter part through find view by id
        etusername = findViewById(R.id.etusername);
        etpassword = findViewById(R.id.etpassword);
        btnlogin = findViewById(R.id.btnlogin);

        pb = findViewById(R.id.pb);
        tvreg = findViewById(R.id.tvreg);
        tvtitle = findViewById(R.id.tvtitle);

        progressDialog = new ProgressDialog(Login.this);

        // shared preference for storing and retrieving the data throughout the app
        sharedPreferences = getSharedPreferences("Logindata",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // setting the title of the actionbar
        getSupportActionBar().setTitle("Login");

    }

    @Override
    protected void onResume() {
        super.onResume();


        // Click Listener event on Textview
        tvreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //starting activity so user can go to Registration Activity from Login Activity
                // Intent is function has 2 input parameters, 1 . Activity/Application Context
                //                                            2 . Destination Activity Name with . class

                startActivity(new Intent(getApplicationContext(),Registration.class));
                finish();
            }
        });
        // Click Listener event on Button
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // getting the username and password from edit text and storing it into String variables
                String username = etusername.getText().toString().trim();
                String password = etpassword.getText().toString().trim();

               // tvtitle.setText(username);


                // validation for the user inputs
                // checking  the input is empty or not

               if(username.isEmpty())
                {
                    etusername.setError("Username can't be empty");
                    etusername.requestFocus();
                    return;
                }
                if(password.isEmpty())
                {
                    etpassword.setError("Password can't be empty");
                    etpassword.requestFocus();
                    return;
                }

                // after the validation sending the data to server for verification
                login(username,password);
            }
        });
    }

    public void login(String username,String Password)
    {

        pb.setVisibility(View.GONE);

        progressDialog.setMessage("Verifying the credentials. Please wait...");
        progressDialog.show();

        // making the url with parameters sending username in p1 variable and password in p2 variable
        String url = "https://urmilkman.com/apppanel/res/wsloginnew.php?" + "p1="+username+"&p2="+Password;

        // logging the full url
        Log.e("cs","url-=>"+url);



       /* JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                if(response!= null)
                {
                    try {
                        JSONArray jsonArray = response.getJSONArray("item");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(Login.this, "There was some issue. Please try after some time", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });*/

//  creating the volley request as a jsonobject request as the response is in json object format
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pb.setVisibility(View.GONE);
                progressDialog.dismiss();
                if(response != null)
                {
                    Log.e("cs","response-=>"+response.toString());
                    try {
                    JSONArray  user = response.getJSONArray("item");
                    for (int i = 0; i < user.length(); i++) {
                        JSONObject j2 = null;

                            j2 = user.getJSONObject(i);


                        String result = j2.get("p1").toString();

                        if (result.equals("1")) {

                            // storing the data from api into shared preference

                            editor.putString("username",username).apply();
                            editor.putString("password",Password).apply();
                            editor.putString("firstname",j2.get("p3").toString()).apply();
                            editor.putString("lastname",j2.get("p4").toString()).apply();
                            editor.putString("email",j2.get("p6").toString()).apply();

                            // validation is successful so starting the main activity
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);

                          //  startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else
                        {
                            // validation failed so showing toast message
                            Toast.makeText(Login.this, "Invalid Username/Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                    } catch (JSONException e) {
                        //catching  Json exception
                        pb.setVisibility(View.GONE);
                        progressDialog.dismiss();

                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //showing volley error in log you can also place toast message here

                pb.setVisibility(View.GONE);
                progressDialog.dismiss();
                Log.e("cs","volley error=>"+error.toString());
            }
        });

        // sending the volley request
        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);

    }
}