package com.carrymybag.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

import java.util.HashMap;
import java.util.Map;

public class ConfirmPage extends AppCompatActivity {

    public String travellerName = "Traveller Name", travellerContact = "Traveller Contact";
    public String addressOrigin1 = "Address Origin", addressDest1 = "Address Destination";
    public String addressOrigin2 = "Address Origin", addressDest2 = "Address Destination";
    public String stateOrigin1 = "State Origin", stateDest1 = "State Destination";
    public String stateOrigin2 = "State Origin", stateDest2 = "State Destination";
    public String pinOrigin1 = "Pin Origin", pinDest1 = "Pin Destinaion";
    public String pinOrigin2 = "Pin Origin", getPinDest2 = "Pin Destinaion";
    public String datePickup1 = "Pickup Date", dateDelivery1 = "Delivery Date";
    public String datePickup2 = "Pickup Date", dateDelivery2 = "Delivery Date";

    public TextView nameTraveller, contactTraveller;
    public TextView originAddress1, originAddress2, destAddress1, destAddress2;
    public TextView originState1, originState2, destState1, destState2;
    public TextView originPin1, originPin2, destPin1, destPin2;
    public TextView pickupDate1, pickupDate2,  deliveryDate1, deliveryDate2;

    public LinearLayout layoutLeg2;
    public Button buttonSubmit, buttonEdit;

    Boolean isTwoWay = true;

    //Register Details....................................................................................

    // 1.For luggage.php

    public static final String KEY_USERID = "user_id";
    public static final String KEY_BAGSIZE = "bag_size";
    public static final String KEY_BAGTYPE = "bag_type";
    public static final String KEY_BAGCOLOR = "bag_color";
    public static final String KEY_PRICEID = "price_id";

    //2.order_details.php

    public static final String KEY_TOTPRICE = "total_price";
    public static final String KEY_PICDATE = "pic_up_date";
    public static final String KEY_DELDATE = "delivery_date";
    public static final String KEY_PICKADD = "pickup_add";
    public static final String KEY_DELADD = "delivery_add";

    //3.user_details.php

    public static final String KEY_NAME = "name";
    public static final String KEY_PHONE = "phone_no";

    //....................................................................................................
    public boolean luggageflag;
    public boolean userflag;
    public boolean orderflag;

    public AppController globalVariable;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_page);

        globalVariable = (AppController) getApplicationContext();
        isTwoWay = globalVariable.isTwoWay();

        buttonEdit = (Button)findViewById(R.id.btn_edit);
        buttonSubmit = (Button)findViewById(R.id.btn_submit);

        nameTraveller = (TextView)findViewById(R.id.text_traveller_name);
        contactTraveller = (TextView)findViewById(R.id.text_traveller_contact);

        originAddress1 = (TextView)findViewById(R.id.text_address_origin1);
        destAddress1 = (TextView)findViewById(R.id.text_address_dest1);

        originState1 = (TextView)findViewById(R.id.text_state_origin1);
        destState1 = (TextView)findViewById(R.id.text_state_dest1);

        originPin1 = (TextView)findViewById(R.id.text_pin_origin1);
        destPin1 = (TextView)findViewById(R.id.text_pin_dest1);

        pickupDate1 = (TextView)findViewById(R.id.text_date_pickup1);
        deliveryDate1 = (TextView)findViewById(R.id.text_date_delivery1);

        nameTraveller.setText(LoginActivity.User_name);
        contactTraveller.setText(globalVariable.getContactOrigin());

        originAddress1.setText(globalVariable.getAddress1Origin());
        destAddress1.setText(globalVariable.getAddress1Dest());

        originState1.setText(stateOrigin1);
        destState1.setText(stateDest1);

        originPin1.setText(globalVariable.getPinOrigin());
        destPin1.setText(globalVariable.getPinDest());

        pickupDate1.setText(globalVariable.getPickupDate1());
        deliveryDate1.setText(globalVariable.getDeliveryDate1());

        layoutLeg2 = (LinearLayout)findViewById(R.id.layout_leg2);
        if(isTwoWay==false) {
            layoutLeg2.setVisibility(View.GONE);
        }
        else
        {
            originAddress2 = (TextView)findViewById(R.id.text_address_origin2);
            destAddress2 = (TextView)findViewById(R.id.text_address_dest2);

            originState2 = (TextView)findViewById(R.id.text_state_origin2);
            destState2 = (TextView)findViewById(R.id.text_state_dest2);

            originPin2 = (TextView)findViewById(R.id.text_pin_origin2);
            destPin2 = (TextView)findViewById(R.id.text_pin_dest2);

            pickupDate2 = (TextView)findViewById(R.id.text_date_pickup2);
            deliveryDate2 = (TextView)findViewById(R.id.text_date_delivery2);

            originAddress2.setText(globalVariable.getAddressOrigin2());
            destAddress2.setText(globalVariable.getAddressDest2());

            originState2.setText(stateOrigin2);
            destState2.setText(stateDest2);

            originPin2.setText(globalVariable.getPinOrigin2());
            destPin2.setText(globalVariable.getPinDest2());

            pickupDate2.setText(globalVariable.getPickupDate2());
            deliveryDate2.setText(globalVariable.getDeliveryDate2());


        }
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmPage.this,
                        EnterDetails.class);
                startActivity(intent);
                finish();
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(register())
                {
                    Intent intent = new Intent(ConfirmPage.this,
                            PaymentActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

        requestQueue = Volley.newRequestQueue(ConfirmPage.this);
    }
    private boolean register()
    {
        return registerLuggage() && registerUser() && registerOrder();
    }
    private boolean registerLuggage() {

        for(int i=0;i<(int)globalVariable.getQtySmall1();i++)
        {
            final String bagSize = "Small";
            final String bagType = "Duffle";
            final String bagColor = globalVariable.getColorSmallOrigin(i);
            final String priceID = "1";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_StoreLuggage,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(ConfirmPage.this,response,Toast.LENGTH_LONG).show();
                            luggageflag = true;
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ConfirmPage.this,error.toString(),Toast.LENGTH_LONG).show();
                            luggageflag = false;
                        }
                    }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put(KEY_USERID,globalVariable.getUserEmail());
                    params.put(KEY_BAGSIZE,bagSize);
                    params.put(KEY_BAGTYPE,bagType);
                    params.put(KEY_BAGCOLOR,bagColor);
                    params.put(KEY_PRICEID,priceID);

                    return params;
                }

            };
            requestQueue.add(stringRequest);
        }
        for(int i=0;i<(int)globalVariable.getQtyMed1();i++)
        {
            final String bagSize = "Medium";
            final String bagType = "Duffle";
            final String bagColor = globalVariable.getColorMediumOrigin(i);
            final String priceID = "1";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_StoreLuggage,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(ConfirmPage.this,response,Toast.LENGTH_LONG).show();
                            luggageflag = true;
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ConfirmPage.this,error.toString(),Toast.LENGTH_LONG).show();
                            luggageflag = false;
                        }
                    }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put(KEY_USERID,globalVariable.getUserEmail());
                    params.put(KEY_BAGSIZE,bagSize);
                    params.put(KEY_BAGTYPE,bagType);
                    params.put(KEY_BAGCOLOR,bagColor);
                    params.put(KEY_PRICEID,priceID);

                    return params;
                }

            };
            requestQueue.add(stringRequest);
        }
        for(int i=0;i<(int)globalVariable.getQtyLarge1();i++)
        {
            final String bagSize = "Large";
            final String bagType = "Duffle";
            final String bagColor = globalVariable.getColorLargeOrigin(i);
            final String priceID = "1";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_StoreLuggage,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(ConfirmPage.this,response,Toast.LENGTH_LONG).show();
                            luggageflag = true;
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ConfirmPage.this,error.toString(),Toast.LENGTH_LONG).show();
                            luggageflag = false;
                        }
                    }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put(KEY_USERID,globalVariable.getUserEmail());
                    params.put(KEY_BAGSIZE,bagSize);
                    params.put(KEY_BAGTYPE,bagType);
                    params.put(KEY_BAGCOLOR,bagColor);
                    params.put(KEY_PRICEID,priceID);

                    return params;
                }

            };
            requestQueue.add(stringRequest);
        }
        return luggageflag;
    }
    private boolean registerUser() {

        final String userEmail = globalVariable.getUserEmail();
        final String userName = globalVariable.getUserName();
        final String userPhone = globalVariable.getContactOrigin();
        final String userAdd = globalVariable.getAddress1Origin();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_StoreUser,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(ConfirmPage.this,response,Toast.LENGTH_LONG).show();
                        userflag = true;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ConfirmPage.this,error.toString(),Toast.LENGTH_LONG).show();
                        userflag = false;
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_USERID,userEmail);
                params.put(KEY_NAME,userName);
                params.put(KEY_PHONE,userPhone);
                params.put(KEY_PICKADD,userAdd);

                return params;
            }


        };
        requestQueue = Volley.newRequestQueue(ConfirmPage.this);
        requestQueue.add(stringRequest);
        return userflag;
    }
    private boolean registerOrder() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_StoreOrder,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(ConfirmPage.this,response,Toast.LENGTH_LONG).show();
                        orderflag = true;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ConfirmPage.this,error.toString(),Toast.LENGTH_LONG).show();
                        orderflag = false;
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_USERID,LoginActivity.User_name);
                params.put(KEY_TOTPRICE, String.valueOf(globalVariable.getTotalPrice()));
                params.put(KEY_PICDATE,globalVariable.getPickupDate1());
                params.put(KEY_DELDATE,globalVariable.getPickupDate1());
                params.put(KEY_PICKADD,globalVariable.getAddress1Origin());
                params.put(KEY_DELADD,globalVariable.getAddress1Dest());

                return params;
            }

        };
        requestQueue = Volley.newRequestQueue(ConfirmPage.this);
        requestQueue.add(stringRequest);
        return orderflag;
    }
}