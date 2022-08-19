package com.at.mytestapplication.ui.gallery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.at.mytestapplication.EditProfile;
import com.at.mytestapplication.R;
import com.at.mytestapplication.databinding.FragmentGalleryBinding;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;

    TextView tvfirstname,tvlastname,tvemail,tvusername;
    Button btneditprofile,btnChangePwd;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        tvfirstname = root.findViewById(R.id.tvfirstname);
        tvlastname = root.findViewById(R.id.tvlastname);
        tvemail = root.findViewById(R.id.tvemail);
        tvusername = root.findViewById(R.id.tvusername);
        btneditprofile = root.findViewById(R.id.btneditprofile);
        btnChangePwd = root.findViewById(R.id.btnChangePwd);



        sharedPreferences = requireActivity().getSharedPreferences("Logindata", Context.MODE_PRIVATE);



        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        String firstname = sharedPreferences.getString("firstname","");
        String lastname = sharedPreferences.getString("lastname","");
        String email = sharedPreferences.getString("email","");
        String username = sharedPreferences.getString("username","");

        tvfirstname.setText(firstname);
        tvlastname.setText(lastname);
        tvemail.setText(email);
        tvusername.setText(username);

        btneditprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Edit Profile Clicked", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(requireContext(), EditProfile.class));
            }
        });

        btnChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(requireContext(), "Change Password Clicked", Toast.LENGTH_SHORT).show();

                NavHostFragment.findNavController(GalleryFragment.this).navigate(R.id.action_nav_gallery_to_nav_chsangePassword);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}