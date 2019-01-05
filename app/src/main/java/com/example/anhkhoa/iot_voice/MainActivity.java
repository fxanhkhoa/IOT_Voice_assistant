package com.example.anhkhoa.iot_voice;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.util.*;
import java.net.*;
import java.io.*;


public class MainActivity extends AppCompatActivity {

    private static Boolean isFabOpen;
    FloatingActionButton fabButton;
    FloatingActionButton fabSpeak;
    FloatingActionButton fabMain;
    View bgFabMenu;
    TextView textShow;

    private static final int REQ_SPEECH_RESULT = 1;
    Button sendCommandButton;
    Button saveButton;
    TextView device1;
    TextView device2;

    public static final int MY_REQUEST_CODE = 100;

    SharedPreferences sharedPreferences;
    String IP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // initialized
        isFabOpen = false;
        sharedPreferences = getSharedPreferences("MY_SMART_HOME", Context.MODE_PRIVATE);

        EditText edt = (EditText) findViewById(R.id.IP);
        IP = edt.getText().toString();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("IP", edt.getText().toString());

        editor.commit();

        Toast.makeText(getApplicationContext(), edt.getText().toString(), Toast.LENGTH_SHORT).show();

        device1 = (TextView) findViewById(R.id.Device1_stt);
        device2 = (TextView) findViewById(R.id.Device2_stt);

        /////////////// End Of Initialize /////////////////////

        fabButton = (FloatingActionButton) findViewById(R.id.fab_button);
        fabSpeak = (FloatingActionButton) findViewById(R.id.fab_speak);
        fabMain = (FloatingActionButton) findViewById(R.id.fab_main);
        bgFabMenu = (View) findViewById(R.id.bg_fab_menu);

        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFabOpen){
                    ShowFabMenu();
                }
                else{
                    CloseFabMenu();
                }
            }
        });

        bgFabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CloseFabMenu();
            }
        });



        //sendCommandButton = (Button) findViewById(R.id.btnCommand);
        //sendCommandButton.setOnClickListener(msendcommand);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ButtonSide.class);
                startActivity(intent);
            }
        });

        saveButton = (Button) findViewById(R.id.saveBtn);
        saveButton.setOnClickListener(msaveButton);

        fabSpeak.setOnClickListener(mfabSpeak);

        ///////////////////// Interval //////////////////////
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
                            device1.setText("On");
                            device1.setBackgroundColor(Color.parseColor("#64fa59"));
                            //device1_btn.setTextColor(Color.parseColor("#ff5441"));
                        }
                        else if(response.contains("0")){
                            device1.setText("Off");
                            device1.setBackgroundColor(Color.parseColor("#ff5441"));
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
                            device2.setText("On");
                            device2.setBackgroundColor(Color.parseColor("#64fa59"));
                        }
                        else if(response.contains("0")){
                            device2.setText("Off");
                            device2.setBackgroundColor(Color.parseColor("#ff5441"));
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

    private View.OnClickListener mfabSpeak = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startVoiceCommand();
            CloseFabMenu();
        }
    };

    private View.OnClickListener msaveButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            EditText edt = (EditText) findViewById(R.id.IP);

            IP = edt.getText().toString();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("IP", edt.getText().toString());

            editor.commit();

            Toast.makeText(getApplicationContext(), "IP Saved", Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener msendcommand = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startVoiceCommand();
            //httpRequestGet();
        }
    };

    private void httpRequestGet(){
        RequestQueue queue = Volley.newRequestQueue(this);
//        String url ="http://www.google.com";
//        textShow = (TextView) findViewById(R.id.textShow);
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
//                        textShow.setText("Response is: "+ response.substring(0,500));
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                textShow.setText("That didn't work!");
//            }
//        });

        // Add the request to the RequestQueue.
        //queue.add(stringRequest);
    }

    private void ToggleDeviceOne(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + IP + "/smarthome?device1=1";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //textShow.setText("That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void ToggleDeviceTwo(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + IP + "/smarthome?device2=1";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //textShow.setText("That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void startVoiceCommand(){
        //Log.d(TAG, "Starting Voice intent...");
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Tell me, I'm ready!");

        try{
            startActivityForResult(i, REQ_SPEECH_RESULT);
        }
        catch (Exception e){
            View contextView = findViewById(R.id.bg_fab_menu);
            Snackbar.make(contextView, "Speech to text not supported", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check the Request code
        if (requestCode ==  REQ_SPEECH_RESULT) {
            //Log.d(TAG, "Request speech result..");
            try{
                ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String command = results.get(0);
                //Log.d(TAG, "Current command ["+command+"]");
                // Now we send commands to the IoT device
                if (command.contains("turn on device one") || command.contains("turn on device 1")){
                    ToggleDeviceOne();
                    Toast.makeText(getApplicationContext(), "Device 1 on", Toast.LENGTH_LONG);
                }
                if (command.contains("turn on device 2") || command.contains("turn on device 2")){
                    ToggleDeviceTwo();
                }

                if (command.contains("turn off device one") || command.contains("turn off device 1")){
                    Toast.makeText(getApplicationContext(), "Device 1 off", Toast.LENGTH_SHORT);
                    ToggleDeviceOne();
                }
                if (command.contains("turn off device 2") || command.contains("turn off device 2")){
                    ToggleDeviceTwo();
                }
            }
            catch (Exception e){

            }
        }
    }

    private void ShowFabMenu(){
        isFabOpen = true;
        fabButton.setVisibility(View.VISIBLE);
        fabSpeak.setVisibility(View.VISIBLE);
        bgFabMenu.setVisibility(View.VISIBLE);

        fabMain.animate().rotation(135f);
        bgFabMenu.animate().alpha(1f);
        fabButton.animate().translationY(-getResources().getDimension((R.dimen.standard_100))).rotation(0f);
        fabSpeak.animate().translationY(-getResources().getDimension((R.dimen.standard_55))).rotation(0f);
    }

    private void CloseFabMenu(){
        isFabOpen = false;

        fabMain.animate().rotation(0f);
        bgFabMenu.animate().alpha(0f);
        fabButton.animate().translationY(0f).rotation(90f);
        fabSpeak.animate().translationY(0f).rotation(90f);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
