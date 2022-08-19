package com.at.mytestapplication;

import static com.at.mytestapplication.Registration.PREFS_NAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EditProfile extends AppCompatActivity {

    Button btnCancel,btnUpdate;
    EditText etFirstName,etLastName,etEmail;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String username,password;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        btnCancel = findViewById(R.id.btnCancel);
        btnUpdate = findViewById(R.id.btnUpdate);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);

        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String firstname = sharedPreferences.getString("firstname","Enter First Name");
        String lastname = sharedPreferences.getString("lastname","Enter Last Name");
        String email = sharedPreferences.getString("email","Enter Email");
        username = sharedPreferences.getString("username","");
        password = sharedPreferences.getString("password","");

        etFirstName.setText(firstname);
        etLastName.setText(lastname);
        etEmail.setText(email);

        progressDialog = new ProgressDialog(EditProfile.this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname = etFirstName.getText().toString().trim();
                String lastname = etLastName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();

                if(firstname.isEmpty())
                {
                    etFirstName.setError("First name can't be empty");
                    etFirstName.requestFocus();
                    return;
                }
                if(!isValidEmail(email))
                {
                    etEmail.setError("Please enter valid email");
                    etEmail.requestFocus();
                    return;
                }

                profileupdate(firstname,lastname,email);
            }
        });
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void profileupdate(String firstname,String lastname,String email)
    {
        progressDialog.setMessage("Updating Profile. Please wait...");
        progressDialog.show();
        String url = "https://urmilkman.com/apppanel/res/profileupdate.php?" + "p1=" + username.replace(" ", "%20") + "&p2=" +password.replace(" ", "%20") + "&p3=" + firstname.replace(" ", "%20") + "&p4=" + lastname.replace(" ", "%20") + "&p5=" + email.replace(" ", "%20");

        Log.e("cs","url=>"+url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            progressDialog.dismiss();
                if(response != null)
                {
                    try {
                        JSONArray jsonArray = response.getJSONArray("item");
                        for(int i = 0; i< jsonArray.length();i++)
                        {
                            JSONObject j2 = jsonArray.getJSONObject(i);

                            String result = j2.getString("p1");

                            if(result.equals("1"))
                            {

                                editor.putString("firstname",firstname).apply();
                                editor.putString("lastname",lastname).apply();
                                editor.putString("email",email).apply();

                                Toast.makeText(EditProfile.this, "Profile updated succesfully.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else
                            {
                                Toast.makeText(EditProfile.this, "There was some issue. Please try after some time.", Toast.LENGTH_SHORT).show();
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("cs","JSONException=>"+e.toString());
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("cs","volley error=>"+error.toString());
                Toast.makeText(EditProfile.this, "There was some network problem. Please try after some time.", Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);


    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }
}