package com.carrymybag.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.carrymybag.R;
import com.carrymybag.app.AppConfig;
import com.carrymybag.app.AppController;
import com.carrymybag.helper.SQLiteHandler;
import com.carrymybag.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends android.support.v4.app.Fragment {

    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";

    EditText newpass;
    TextView alert;
    Button changepass;
    Button cancel;
    public AppController globalVariable;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_change_pass, container, false);

        cancel = (Button) view.findViewById(R.id.btcancel);
        globalVariable = (AppController) getActivity().getApplicationContext();
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                Intent login = new Intent(getActivity().getApplicationContext(), LoginActivity.class);

                startActivity(login);
            }

        });

        newpass = (EditText) view.findViewById(R.id.newpass);
        alert = (TextView)view.findViewById(R.id.alertpass);
        changepass = (Button) view.findViewById(R.id.btchangepass);

        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });


        return view;
    }

    void changePassword() {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CHANGEPASS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    if (json.getString(KEY_SUCCESS) != null) {
                        alert.setText("");
                        String res = json.getString(KEY_SUCCESS);
                        String red = json.getString(KEY_ERROR);

                        if (Integer.parseInt(res) == 1) {

                            alert.setText("Your Password is successfully changed.");

                        } else if (Integer.parseInt(red) == 2) {
                            alert.setText("Invalid old Password.");
                        } else {
                            alert.setText("Error occured in changing Password.");
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                String email = globalVariable.getUserEmail();
                params.put("email", email);
                params.put("newpas",newpass.getText().toString());
                return params;
            }

        };

        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(strReq);
    }

}