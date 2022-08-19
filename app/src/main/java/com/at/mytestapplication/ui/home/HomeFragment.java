package com.at.mytestapplication.ui.home;

import static com.at.mytestapplication.Registration.PREFS_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.at.mytestapplication.AdapterForCategory;
import com.at.mytestapplication.AdapterForProduct;
import com.at.mytestapplication.GetSetCategory;
import com.at.mytestapplication.GetSetProducts;
import com.at.mytestapplication.R;
import com.at.mytestapplication.SliderAdapter;
import com.at.mytestapplication.SliderData;
import com.at.mytestapplication.databinding.FragmentHomeBinding;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    SliderView slider;

    ArrayList<SliderData> sliderDataArrayList;
    SliderAdapter sliderAdapter;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String username,password;

    RecyclerView recyclerview_cat,recyclerview_products;
    ArrayList<GetSetCategory> getSetCategoryArrayList;
    AdapterForCategory adapterForCategory;

    ArrayList<GetSetProducts> getSetProductsArrayList;
    AdapterForProduct adapterForProduct;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        slider = root.findViewById(R.id.slider);
        recyclerview_cat = root.findViewById(R.id.recyclerview_cat);
        recyclerview_products = root.findViewById(R.id.recyclerview_products);

        sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        username = sharedPreferences.getString("username","");
        password = sharedPreferences.getString("password","");

        sliderDataArrayList = new ArrayList<>();
        sliderAdapter = new SliderAdapter(getContext(),getActivity(),sliderDataArrayList);

        getSetCategoryArrayList = new ArrayList<>();
        getSetProductsArrayList = new ArrayList<>();

        getbannerimage();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        recyclerview_cat.setHasFixedSize(true);
        recyclerview_cat.setNestedScrollingEnabled(false);
        recyclerview_cat.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));

        adapterForCategory = new AdapterForCategory(getSetCategoryArrayList,getContext(),getActivity());
        recyclerview_cat.setAdapter(adapterForCategory);

        getcategories();

        recyclerview_products.setHasFixedSize(true);
        recyclerview_products.setNestedScrollingEnabled(false);
        recyclerview_products.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));

        adapterForProduct = new AdapterForProduct(getContext(),getActivity(),getSetProductsArrayList);
        recyclerview_products.setAdapter(adapterForProduct);

        getProducts();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void getbannerimage()
    {
        String pincode = "";
        String url =    "https://urmilkman.com/apppanel/res/wsgetbanner.php?p1=" + username.replace(" ", "%20") + "&p2=" + password.replace(" ", "%20") + "&p3=" + pincode.replace(" ", "%20");

        Log.e("cs","url=>"+url);

        sliderDataArrayList.clear();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                if(response!=null)
                {
                    Log.e("cs","response=>"+response.toString());
                    try {
                        JSONArray jsonArray = response.getJSONArray("item");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject j2 = jsonArray.getJSONObject(i);
                            if(j2.getString("p1").equals("1"))
                            {
                                SliderData sliderData = new SliderData();
                                sliderData.setImgurl("https://urmilkman.com/apppanel/images/"+j2.getString("p3"));

                                sliderDataArrayList.add(sliderData);

                               // sliderDataArrayList.add(new SliderData(j2.getString("p3")));
                            }
                            else
                            {
                                Toast.makeText(requireContext(),"No Images",Toast.LENGTH_LONG).show();
                            }
                        }
                        slider.setSliderAdapter(sliderAdapter);
                       slider.setScrollTimeInSec(5);
                        slider.setIndicatorEnabled(true);

                        // to set it scrollable automatically
                        // we use below method.
                        slider.setAutoCycle(true);
                        slider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                        slider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                        slider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);

                        // to start autocycle below method is used.
                        slider.startAutoCycle();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(),"There was some issue.Please try after some time.",Toast.LENGTH_LONG).show();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(requireContext(),"There was some issue.Please try after some time.",Toast.LENGTH_LONG).show();
            }
        });

        Volley.newRequestQueue(requireContext()).add(jsonObjectRequest);
    }

    public void getcategories()
    {
        getSetCategoryArrayList.clear();
        String pincode = "0";
        String url = "https://urmilkman.com/apppanel/res/getmycategories.php?" + "p1=" + username.replace(" ", "%20") + "&p2=" + password.replace(" ", "%20") + "&p3=" + pincode;

        Log.e("cs","url=>"+url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                if(response!= null)
                {
                    Log.e("cs","response=>"+response.toString());
                    try {
                        JSONArray jsonArray = response.getJSONArray("item");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String result = jsonObject.getString("p1");
                            if(result.equals("1"))
                            {
                                GetSetCategory getSetCategory = new GetSetCategory();
                                getSetCategory.setCatid(jsonObject.getString("p3"));
                                getSetCategory.setCatname(URLDecoder.decode(jsonObject.getString("p4"),"UTF-8"));
                                getSetCategory.setCatimage("https://urmilkman.com/apppanel/images/"+jsonObject.getString("p5"));

                                getSetCategoryArrayList.add(getSetCategory);
                            }
                            else
                            {
                                Toast.makeText(getContext(), "No data found.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        if(getSetCategoryArrayList.size() > 0)
                        {
                            recyclerview_cat.setAdapter(adapterForCategory);
                        }

                    } catch (JSONException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "There was some issue. Please try after some time.", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "There was some issue. Please try after some time.", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(requireContext()).add(jsonObjectRequest);
    }

    public void getProducts()
    {
        getSetProductsArrayList.clear();

        String url = "https://urmilkman.com/apppanel/res/itemreletedlistnew.php?" + "p1=" + username.replace(" ", "%20") + "&p2=" + password.replace(" ", "%20") ;

        Log.e("cs","url=>"+url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                if(response!= null)
                {
                    Log.e("cs","response=>"+response.toString());
                    try {
                        JSONArray jsonArray = response.getJSONArray("item");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String result = jsonObject.getString("p1");
                            if(result.equals("1"))
                            {
                                GetSetProducts getSetProducts = new GetSetProducts();
                                getSetProducts.setPid(jsonObject.getString("p2"));
                                getSetProducts.setPname(URLDecoder.decode(jsonObject.getString("p4"),"UTF-8"));
                                getSetProducts.setPimg("https://urmilkman.com/apppanel/images/"+jsonObject.getString("p6"));
                                getSetProducts.setPdetail(URLDecoder.decode(jsonObject.getString("p5"),"UTF-8"));
                                getSetProducts.setPmrp(jsonObject.getString("p7"));
                                getSetProducts.setPprice(jsonObject.getString("p8"));
                                getSetProducts.setPdiscount(jsonObject.getString("p9"));


                                getSetProductsArrayList.add(getSetProducts);
                            }
                            else
                            {
                                Toast.makeText(getContext(), "No data found.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        Log.e("cs","getSetProductsArrayList.size()=>"+getSetProductsArrayList.size());
                        if(getSetProductsArrayList.size() > 0)
                        {
                            recyclerview_products.setAdapter(adapterForProduct);
                        }

                    } catch (JSONException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "There was some issue. Please try after some time.", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "There was some issue. Please try after some time.", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(requireContext()).add(jsonObjectRequest);
    }
}