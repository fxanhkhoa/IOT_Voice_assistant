package com.example.anhkhoa.iot_voice;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class ButtonSide extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String IP;

    Button device1_btn, device2_btn;
    TextView got;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_side);

        // Initialize

        sharedPreferences = getSharedPreferences("MY_SMART_HOME", Context.MODE_PRIVATE);
        IP = sharedPreferences.getString("IP", "");

        Toast.makeText(getApplicationContext(), IP, Toast.LENGTH_LONG).show();

        device1_btn = (Button) findViewById(R.id.Device1_btn);
        device2_btn = (Button) findViewById(R.id.Device2_btn);

        device1_btn.setOnClickListener(mdevice1_btn);
        device2_btn.setOnClickListener(mdevice2_btn);

        /////////////////////////// End of Initialize /////////////////////////////////

        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            private  long time = 0;
            @Override
            public void run() {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://" + IP + "/device1";
                //got.setText(url);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //got.setText(response);
                        if (response.contains("1")){
                            device1_btn.setText("Turn Off Device 1");
                            device1_btn.setBackgroundColor(Color.parseColor("#ff5441"));
                            //device1_btn.setTextColor(Color.parseColor("#ff5441"));
                        }
                        else if(response.contains("0")){
                            device1_btn.setText("Turn On Device 1");
                            device1_btn.setBackgroundColor(Color.parseColor("#64fa59"));
                            //device1_btn.setTextColor(Color.parseColor("#64fa59"));
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                queue.add(stringRequest);

                url = "http://" + IP + "/device2";
                stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("1")){
                            device2_btn.setText("Turn Off Device 2");
                            device2_btn.setBackgroundColor(Color.parseColor("#ff5441"));
                        }
                        else if(response.contains("0")){
                            device2_btn.setText("Turn On Device 2");
                            device2_btn.setBackgroundColor(Color.parseColor("#64fa59"));
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                queue.add(stringRequest);

                h.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private View.OnClickListener mdevice1_btn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = "http://" + IP + "/smarthome?device1=1";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.contains("1")){
                    }
                    else if(response.contains("0")){
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            queue.add(stringRequest);
        }
    };

    private View.OnClickListener mdevice2_btn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = "http://" + IP + "/smarthome?device2=1";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.contains("1")){
                    }
                    else if(response.contains("0")){
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            queue.add(stringRequest);
        }
    };

}
