package com.at.mytestapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Registration extends AppCompatActivity {

    public static String PREFS_NAME = "Logindata";

    EditText etfirstname,etlastname,etemail,etusername,etpassword;
    Button btnreg;
    TextView tvreg;
    ProgressDialog progressDialog;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Spinner spncity;
    String[] cities = {"Vadodara","Ahemdabad","surat"};

    String selected_city="";

    ArrayList<String> cityname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etfirstname = findViewById(R.id.etfirstname);
        etlastname = findViewById(R.id.etlastname);
        etemail = findViewById(R.id.etemail);
        etusername = findViewById(R.id.etusername);
        etpassword = findViewById(R.id.etpassword);
        btnreg = findViewById(R.id.btnreg);
        tvreg = findViewById(R.id.tvreg);
        spncity = findViewById(R.id.spncity);

        progressDialog = new ProgressDialog(Registration.this);

        sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        getSupportActionBar().setTitle("Registration");

        cityname = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();


            getcity();

    /*
       //only use these format of defining array adapter if you are using the static array from string.xml
       ArrayAdapter aa = ArrayAdapter.createFromResource(getApplicationContext(), R.array.Weight, android.R.layout.simple_spinner_item);
       // use this format if you are dynamically populating the spinner or hardcoding the values in code
        ArrayAdapter aa = new ArrayAdapter(getApplicationContext(),  android.R.layout.simple_spinner_item,cities);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spncity.setAdapter(aa);*/
        spncity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // selected_city = cityname.get(position);


                selected_city = spncity.getSelectedItem().toString().trim();

                Toast.makeText(Registration.this, "selected city :: "+selected_city, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tvreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        });

        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstname = etfirstname.getText().toString().trim();
                String lastname = etlastname.getText().toString().trim();
                String email = etemail.getText().toString().trim();
                String username = etusername.getText().toString().trim();
                String password = etpassword.getText().toString().trim();

                if(firstname.isEmpty())
                {
                    etfirstname.setError("First Name can't be empty");
                    etfirstname.requestFocus();
                    return;
                }
                if(!isValidEmail(email))
                {
                    etemail.setError("email can't be empty");
                    etemail.requestFocus();
                    return;
                }
                if(username.length() != 10)
                {
                    etusername.setError("Please enter valid 10 digit mobile no.");
                    etusername.requestFocus();
                    return;
                }

                if(password.isEmpty())
                {
                    etpassword.setError("Password can't be empty");
                    etpassword.requestFocus();
                    return;
                }

                if(selected_city.isEmpty())
                {
                    Toast.makeText(Registration.this, "Please select City", Toast.LENGTH_SHORT).show();
                    return;
                }
                // disabling the button so the user can't click multiple times and call the api how many times he clicks on button
                btnreg.setEnabled(false);
                //enable the button in api failure/error

                reg(firstname,lastname,email,username,password);
            }
        });
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void reg(String firstname,String lastname,String email,String username,String password)
    {
       String refererid ="0";
       String cityid ="0";

        progressDialog.setMessage("Registering the user. Please wait...");
        progressDialog.show();
        String url = "https://urmilkman.com/apppanel/res/newregister1.php?" + "p1=" + firstname.replace(" ", "%20") + "&p2=" + lastname.replace(" ", "%20") + "&p3=" + username.replace(" ", "%20") + "&p4=" + email.replace(" ", "%20") + "&p5=" + password.replace(" ", "%20")+ "&p6=" + refererid.replace(" ", "%20")+ "&p7=" + selected_city.replace(" ", "%20");
        Log.e("cs","url-=>"+url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                btnreg.setEnabled(true);
                progressDialog.dismiss();
                if(response != null)
                {
                    Log.e("cs","response-=>"+response.toString());
                    try {
                        JSONArray user = response.getJSONArray("item");
                        for (int i = 0; i < user.length(); i++) {
                            JSONObject j2 = null;

                            j2 = user.getJSONObject(i);


                            String result = j2.get("p1").toString();

                            if (result.equals("1")) {

                                editor.putString("username",username).apply();
                                editor.putString("password",password).apply();
                                editor.putString("firstname",firstname).apply();
                                editor.putString("lastname",lastname).apply();
                                editor.putString("email",email).apply();

                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                intent.putExtra("usernam",username);

                                // startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(Registration.this, "There was some issue. Please try after some time.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {

                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btnreg.setEnabled(true);
                progressDialog.dismiss();
                Log.e("cs","volley error=>"+error.toString());
            }
        });

        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);

    }

    public void getcity()
    {
        String refererid ="0";
        String cityid ="0";
        cityname.clear();
        progressDialog.setMessage("Getting the Cities. Please wait...");
        progressDialog.show();
        String url = "https://urmilkman.com/apppanel/res/wsgetcity.php";
        Log.e("cs","url-=>"+url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                progressDialog.dismiss();
                if(response != null)
                {
                    Log.e("cs","response-=>"+response.toString());
                    try {
                        JSONArray user = response.getJSONArray("item");
                        for (int i = 0; i < user.length(); i++) {
                            JSONObject j2 = null;

                            j2 = user.getJSONObject(i);


                            String result = j2.get("p1").toString();

                            if (result.equals("1")) {

                               String p3 = j2.get("p4").toString();
                               cityname.add(p3);
                            }
                            else
                            {
                                Toast.makeText(Registration.this, "There was some issue. Please try after some time.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        ArrayAdapter arrayAdapter = new ArrayAdapter(Registration.this, android.R.layout.simple_spinner_item,cityname);

                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spncity.setAdapter(arrayAdapter);
                    } catch (JSONException e) {

                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Log.e("cs","volley error=>"+error.toString());
            }
        });

        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);

    }
}