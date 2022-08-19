package com.at.mytestapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SplashScreen extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                .LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();
        sharedPreferences = getSharedPreferences("Logindata",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String username = sharedPreferences.getString("username","");
        String password = sharedPreferences.getString("password","");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!username.isEmpty() && !password.isEmpty())
                {
                    login(username,password);
                }
                 else
                {
                    startActivity(new Intent(getApplicationContext(),Login.class));
                    finish();
                }
            }
        },5000);



    }

    public void login(String username,String Password)
    {

        String url = "https://urmilkman.com/apppanel/res/wsloginnew.php?" + "p1="+username+"&p2="+Password;
        Log.e("cs","url-=>"+url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                if(response != null)
                {
                    Log.e("cs","response-=>"+response.toString());
                    try {
                        JSONArray user = response.getJSONArray("item");
                        for (int i = 0; i < user.length(); i++) {
                            JSONObject j2 = null;

                            j2 = user.getJSONObject(i);


                            String result = j2.get("p1").toString();

                            if (result.equals("0")) {

                                Toast.makeText(SplashScreen.this, "Invalid Username/Password", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),Login.class));
                                finish();


                            }
                            else
                            {
                                editor.putString("username",username).apply();
                                editor.putString("password",Password).apply();
                                editor.putString("firstname",j2.get("p3").toString()).apply();
                                editor.putString("lastname",j2.get("p4").toString()).apply();
                                editor.putString("email",j2.get("p6").toString()).apply();


                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                intent.putExtra("usernam",username);

                                // startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                startActivity(intent);
                                finish();
                            }
                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("cs","volley error=>"+error.toString());
            }
        });

        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);

    }
}