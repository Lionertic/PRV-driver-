package com.example.lionertic.main.AsyncTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.lionertic.main.CONSTANTS;
import com.example.lionertic.main.Fragments.LogIn;
import com.example.lionertic.main.Fragments.Maps;
import com.example.lionertic.main.MainActivity;
import com.example.lionertic.main.R;
import com.example.lionertic.main.RequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class KeyCheck extends AsyncTask<URL, Void, String> {

    Context context;
    Activity activity;
    Intent i;


    public KeyCheck(Context cnt,Activity act){
        context=cnt;
        activity=act;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(final URL... urls) {
        // Create URL object

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                CONSTANTS.CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(jsonObject.getInt("success")==1) {
                                activity.setTitle("Maps");
                                Maps m = new Maps();
                                FragmentManager fm = ((FragmentActivity)activity).getSupportFragmentManager();
                                fm.beginTransaction().replace(R.id.fragment, m).commit();
                            }
                            else{
                                activity.setTitle("Log In");
                                LogIn m = new LogIn();
                                FragmentManager fm = ((FragmentActivity)activity).getSupportFragmentManager();
                                fm.beginTransaction().replace(R.id.fragment, m).commit();
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, error.getMessage()+"asdfghjkl", Toast.LENGTH_LONG).show();

                        }
        }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("key", MainActivity.KEY);
                            return params;
                        }
                    };
                    RequestHandler.getInstance(context).addToRequestQueue(stringRequest);


                return null;
    }

    @Override
    protected void onPostExecute(String earthquake) {
    }
}
