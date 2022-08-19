package com.at.mytestapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangePasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePasswordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangePasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangePasswordFragment newInstance(String param1, String param2) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    Button btnCancel,btnUpdate;
    EditText etCurrentPwd,etnewPwd,etReNewPwd;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String username,password;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_change_password, container, false);


        btnCancel = root.findViewById(R.id.btnCancel);
        btnUpdate = root.findViewById(R.id.btnUpdate);
        etCurrentPwd = root.findViewById(R.id.etCurrentPwd);
        etnewPwd = root.findViewById(R.id.etnewPwd);
        etReNewPwd = root.findViewById(R.id.etReNewPwd);

        sharedPreferences = requireActivity().getSharedPreferences("Logindata", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        username = sharedPreferences.getString("username","");
        password = sharedPreferences.getString("password","");

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(ChangePasswordFragment.this).popBackStack();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPassword = etCurrentPwd.getText().toString().trim();
                String newPassword = etnewPwd.getText().toString().trim();
                String retypeNewPassword = etReNewPwd.getText().toString().trim();

                if(!currentPassword.equals(password))
                {
                    etCurrentPwd.setError("Current Password does not match");
                    etCurrentPwd.requestFocus();
                    return;
                }
                if(newPassword.isEmpty())
                {
                    etnewPwd.setError("New Password can't be empty");
                    etnewPwd.requestFocus();
                    return;
                }

                if(!newPassword.equals(retypeNewPassword))
                {
                    etReNewPwd.setError("Retyped New Password does not match New Password");
                    etReNewPwd.requestFocus();
                    return;
                }
                changepassword(retypeNewPassword);

            }
        });

        return root;
    }

    public void changepassword(String NewPass)
    {
        String url = "https://urmilkman.com/apppanel/res/changepassword.php?" + "p1="+username.replace(" ","%20")+"&p2="+password.replace(" ","%20")+"&p3="+NewPass.replace(" ","%20");
        Log.e("cs","url=>"+url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response!=null)
                {
                    try {
                        JSONArray jsonArray = response.getJSONArray("item");
                        for (int i = 0; i< jsonArray.length();i++)
                        {
                            JSONObject j2 = jsonArray.getJSONObject(i);
                            String result = j2.getString("p1");

                            if(result.equals("1"))
                            {
                                Toast.makeText(requireContext(), "Password Updated Successfully", Toast.LENGTH_SHORT).show();
                                editor.putString("password",NewPass).apply();
                                NavHostFragment.findNavController(ChangePasswordFragment.this).popBackStack();
                            }
                            else
                            {
                                Toast.makeText(requireContext(), "Password Update Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "There was some issue. Please try after some time.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(requireContext(), "There was some issue. Please try after some time.", Toast.LENGTH_SHORT).show();

            }
        });

        Volley.newRequestQueue(requireContext()).add(jsonObjectRequest);
    }
}